package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        HttpConnection httpConnection = new HttpConnection();
        try {
            httpConnection.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
