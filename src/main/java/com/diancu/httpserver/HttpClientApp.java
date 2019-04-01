package com.diancu.httpserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class HttpClientApp {


    public static void main(String[] args) throws IOException {
        Random random = new Random();
        URL url = new URL("http://localhost/test2.txt");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Connection", "close");
        connection.setRequestProperty("Connection", "close");
//        connection.setReadTimeout(5000);
        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        osw.write(String.format("{\"pos\":{\"left\":%1$d,\"top\":%2$d}}\r\n", random.nextInt(30), random.nextInt(20)));
        osw.write("\r\n");
        osw.flush();
        osw.close();

        System.err.println(connection.getResponseCode());
        connection.disconnect();
    }

}
