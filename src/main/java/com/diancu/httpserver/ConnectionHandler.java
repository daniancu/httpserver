package com.diancu.httpserver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
@Slf4j
public class ConnectionHandler implements Closeable, Runnable {
    private final Socket socket;


    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public void run() {
        PrintWriter out = null;
        InputStreamReader in;

        try (HttpInputReader httpReader = new HttpInputReader(socket.getInputStream());
             HttpOutputHandler outputHandler = new HttpOutputHandler(socket.getOutputStream())) {

            StatusLine statusLine = httpReader.readStatusLine();
            HttpHeaders headers = httpReader.readHeaders();

            HttpMethodHandler handler = getMethodHandler(statusLine.getMethod());
            //handle request depending on HTTP method
            handler.handle(statusLine, headers, outputHandler);

        } catch (IOException e) {
            log.error("Connection error", e);
        } catch (InvalidStatusLineException e) {

        }
    }
}
