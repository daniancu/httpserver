package com.diancu.webserver.it;


import com.diancu.webserver.httpapi.HttpHandlers;
import com.diancu.webserver.httpapi.HttpHeaders;
import com.diancu.webserver.httpapi.HttpServer;
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

public class HttpServerIT {

    private ServerConfiguration config;
    private HttpServer server;
    private File index;
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

        index = new File(tempRoot.toFile(), "index.html");
        FileWriter fileWriter = new FileWriter(index);
        String token = "hello";
        fileWriter.write(token);
        fileWriter.close();

        URL url = new URL("http", config.getServerHost(), config.getServerPort(), index.getName());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Assert.assertEquals("status code error", 200, con.getResponseCode());
        Assert.assertEquals("response message error", "OK", con.getResponseMessage());
        Assert.assertEquals( "content type error", "text/html", con.getHeaderField(HttpHeaders.CONTENT_TYPE));
        Assert.assertEquals(String.valueOf(token.getBytes().length), con.getHeaderField(HttpHeaders.CONTENT_LENGTH));

        StringBuilder content = readContent(con);
        Assert.assertEquals("file content error", token, content.toString());

        con.disconnect();

        url = new URL("http", config.getServerHost(), config.getServerPort(), "missing.html");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Assert.assertEquals("status code error", 404, con.getResponseCode());
        con.disconnect();

        String testFolder = "testFolder";
        File folder = new File(tempRoot.toFile(), testFolder);
        if (folder.mkdir()) {
            url = new URL("http", config.getServerHost(), config.getServerPort(), testFolder   );
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            Assert.assertEquals("status code error", 200, con.getResponseCode());
            Assert.assertEquals( "content type error", "text/html", con.getHeaderField(HttpHeaders.CONTENT_TYPE));
            content = readContent(con);
            Assert.assertTrue(content.toString().contains("<h1>" + testFolder + "</h1>"));
        } else {
            Assert.fail("could not create folder in website");
        }


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
        URL url = new URL("http","localhost", config.getServerPort(),  uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/html");

        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        osw.write(body);
        osw.close();

        Assert.assertEquals(201, connection.getResponseCode());
        connection.disconnect();

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        Assert.assertEquals(200, connection.getResponseCode());
        Assert.assertEquals(String.valueOf(body.getBytes().length), connection.getHeaderFields().get("Content-length").get(0));

    }

    @Test
    public void testDeleteMethod() {

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
        url = new URL("http","localhost", config.getServerPort(),  "/missing.txt");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("OPTIONS");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        Assert.assertEquals(404, connection.getResponseCode());
        connection.disconnect();
    }

}
