package com.diancu.webserver.httpapi;


import com.diancu.webserver.serverapp.ServerConfiguration;
import com.diancu.webserver.websiteapi.WebSite;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerTest {

    private ServerConfiguration config;
    private HttpServer server;
    private Path tempRoot;

    @Before
    public void setup() throws IOException, InterruptedException {
        tempRoot = Files.createTempDirectory("httpsrv");
        config = new ServerConfiguration(tempRoot.toFile().getPath());
        WebSite website = new WebSite(config);
        HttpHandlers handlers = new HttpHandlers(website);
        ExecutorService executor = Executors.newFixedThreadPool(config.getWorkerThreads());
        server = new HttpServer(config, handlers, executor);
        server.start();
        //todo: need better way to wait until server is ready
        Thread.sleep(1000);
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testUnsupportedMethod() throws IOException {
        URL url = new URL("http", config.getServerHost(), config.getServerPort(), "/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("TRACE");

        Assert.assertEquals("status code error", 501, con.getResponseCode());
        con.disconnect();
    }

    @Test
    public void testGetMethod() throws IOException {

        File index = createFileInSiteRoot("index.html", "hello");

        HttpURLConnection con = openConnectionToResource(index.getName());
        con.setRequestMethod("GET");

        Assert.assertEquals("status code error", 200, con.getResponseCode());
        Assert.assertEquals("response message error", "OK", con.getResponseMessage());
        Assert.assertEquals( "content type error", "text/html", con.getHeaderField(HttpHeaders.CONTENT_TYPE));
        Assert.assertEquals(String.valueOf("hello".getBytes().length), con.getHeaderField(HttpHeaders.CONTENT_LENGTH));

        StringBuilder content = readContent(con);
        Assert.assertEquals("file content error", "hello", content.toString());

        con.disconnect();

        con = openConnectionToResource("missing.html");
        con.setRequestMethod("GET");

        Assert.assertEquals("status code error", 404, con.getResponseCode());
        con.disconnect();

        String siteFolder = "testFolder";
        if (createFolderInSiteRoot(siteFolder)) {
            con = openConnectionToResource("/");
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            Assert.assertEquals("status code error", 200, con.getResponseCode());
            Assert.assertEquals( "content type error", "text/html", con.getHeaderField(HttpHeaders.CONTENT_TYPE));
            content = readContent(con);
            String folderHtmlContent = content.toString();
            Assert.assertTrue(folderHtmlContent.contains("<h1>Root</h1>"));
            Assert.assertTrue(folderHtmlContent.contains(siteFolder));
        } else {
            Assert.fail("could not create folder in website");
        }
    }

    private File createFileInSiteRoot(String fileName, String fileContent) throws IOException {
        File newFile = new File(tempRoot.toFile(), fileName);
        FileWriter fileWriter = new FileWriter(newFile);
        fileWriter.write(fileContent);
        fileWriter.close();
        return newFile;
    }

    private boolean createFolderInSiteRoot(String testFolder) {
        File folder = new File(tempRoot.toFile(), testFolder);
        return folder.mkdir();
    }

    private StringBuilder readContent(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content;
    }

    @Test
    public void testPutMethod() throws IOException {
        String uri = "/sayHello.html";
        String body = "hello world";

        HttpURLConnection connection = openConnectionToResource(uri);
        connection.setRequestMethod("PUT");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/html");

        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        osw.write(body);
        osw.close();

        Assert.assertEquals(201, connection.getResponseCode());
        connection.disconnect();

        File fileOnSite = new File(tempRoot.toFile(), uri);
        Assert.assertTrue(fileOnSite.exists());
        Assert.assertEquals(body.getBytes().length, fileOnSite.length());
    }

    @Test
    public void testDeleteMethod() throws IOException {
        String fileName = "deleteme.txt";
        File file =  createFileInSiteRoot(fileName, "some text");
        Assert.assertTrue(file.exists());
        HttpURLConnection con = openConnectionToResource(fileName);
        con.setRequestMethod("DELETE");
        Assert.assertEquals(204, con.getResponseCode());
        con.disconnect();

        Assert.assertFalse(new File(file.getAbsolutePath()).exists());

        //try to delete already deleted file
        con = openConnectionToResource(fileName);
        con.setRequestMethod("DELETE");
        Assert.assertEquals(404, con.getResponseCode());
        con.disconnect();

    }

    @Test
    public void testOptionsMethod() throws IOException {
        URL url = new URL("http","localhost", config.getServerPort(),  "/*");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("OPTIONS");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        Assert.assertEquals(200, connection.getResponseCode());
        Assert.assertEquals("GET,HEAD,PUT,DELETE,OPTIONS", connection.getHeaderField(HttpHeaders.ALLOW));
        Assert.assertEquals("0", connection.getHeaderField(HttpHeaders.CONTENT_LENGTH));

        connection.disconnect();
        connection = openConnectionToResource("/missing.txt");
        connection.setRequestMethod("OPTIONS");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        Assert.assertEquals(404, connection.getResponseCode());
        connection.disconnect();
    }

    private HttpURLConnection openConnectionToResource(String s) throws IOException {
        URL url;
        HttpURLConnection connection;
        url = new URL("http", "localhost", config.getServerPort(), s);
        connection = (HttpURLConnection) url.openConnection();
        return connection;
    }

}
