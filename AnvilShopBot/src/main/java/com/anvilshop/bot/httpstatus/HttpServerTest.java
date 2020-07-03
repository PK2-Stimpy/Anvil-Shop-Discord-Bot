package com.anvilshop.bot.httpstatus;

public class HttpServerTest {
    private static final String CONTEXT = "/app";
    private static final int PORT = 3000;
    public static void run() {
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer(PORT, CONTEXT,
                new HttpRequestHandler());
        simpleHttpServer.start();
        System.out.println("Server is started and listening on port "+ PORT);
    }
}