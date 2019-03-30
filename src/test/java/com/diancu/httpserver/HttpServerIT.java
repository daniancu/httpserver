package com.diancu.httpserver;


import com.diancu.httpserver.server.HttpServer;
import com.diancu.httpserver.server.HttpHeaders;
import com.diancu.httpserver.server.ServerConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class HttpServerIT {

    private ServerConfiguration config;
    private HttpServer server;
    private File index;
    private Path tempRoot;
    private CompletableFuture<Void> serverHandler;

    @Before
    public void setup() throws IOException, InterruptedException {
        tempRoot = Files.createTempDirectory("httpsrv");
        config = new ServerConfiguration(tempRoot.toFile());
        server = new HttpServer(config);
        server.start();
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
        con.setRequestMethod("OPTIONS");

        Assert.assertEquals("status code error", 501, con.getResponseCode());
        con.disconnect();
    }

    @Test
    public void testGETMissingResourceReturns404() throws IOException {
        URL url = new URL("http", config.getServerHost(), config.getServerPort(), "missing.html");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Assert.assertEquals("status code error", 404, con.getResponseCode());
        con.disconnect();
    }

    @Test
    public void testSendBAdStatusLineReturns400() throws IOException {

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

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        Assert.assertEquals("file content error", token, content.toString());

        con.disconnect();

    }


}
