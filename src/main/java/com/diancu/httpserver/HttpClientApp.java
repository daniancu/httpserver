package com.diancu.httpserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class HttpClientApp {


    public static void main(String[] args) throws IOException {
        URL url = new URL("http://localhost/date.txt");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(String.valueOf(new Date()));
        out.flush();
        out.close();

        int response = con.getResponseCode();
        System.out.println("response = " + response);
        con.disconnect();
    }

}
