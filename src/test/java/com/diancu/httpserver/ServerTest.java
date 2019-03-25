package com.diancu.httpserver;


import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerTest {

    @Test
    public void testWhenCLlentInitiateConnectionServerAcceptIt() throws IOException {

        Path tempRoot = Files.createTempDirectory("httpsrv");
        File index = new File(tempRoot.toFile(), "index.html");
        FileWriter fileWriter = new FileWriter(index);
        String token = "hello";
        fileWriter.write(token);
        fileWriter.close();


        ServerConfiguration config = new ServerConfiguration(tempRoot.toFile());
        DemoHttpServer server = new DemoHttpServer(config);
        server.start();

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
