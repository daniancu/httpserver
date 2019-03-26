package com.diancu.httpserver;


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

public class ServerTest {

    private ServerConfiguration config;
    private DemoHttpServer server;
    private File index;
    private Path tempRoot;
    private CompletableFuture<Void> serverHandler;

    @Before
    public void setup() throws IOException {
        tempRoot = Files.createTempDirectory("httpsrv");
        config = new ServerConfiguration(tempRoot.toFile());
        server = new DemoHttpServer(config);
        serverHandler =  server.start();
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testWhenCLlentInitiateConnectionServerAcceptIt() throws IOException {

        index = new File(tempRoot.toFile(), "index.html");
        FileWriter fileWriter = new FileWriter(index);
        String token = "hello";
        fileWriter.write(token);
        fileWriter.close();

        URL url = new URL("http", config.getServerHost(), config.getServerPort(), index.getName());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        Assert.assertEquals(200, con.getResponseCode());
        Assert.assertEquals("token not returned", token, content.toString());

    }


}
