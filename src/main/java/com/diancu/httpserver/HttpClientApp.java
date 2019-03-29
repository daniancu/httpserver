package com.diancu.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientApp {


    public static void main(String[] args) {
        try {
            URL url = new URL("http://localhost/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "TestHttpClient");
            con.connect();
            int resposeCode = con.getResponseCode();
            System.out.println("resposeCode = " + resposeCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            System.out.println("content = " + content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
