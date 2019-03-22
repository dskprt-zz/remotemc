package com.github.dskprt.remotemc.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.github.dskprt.remotemc.RemoteMC;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class HTTPProcessor {

    private static RemoteMC rmc;
    private static HttpServer server;

    public HTTPProcessor(RemoteMC rmc) throws IOException {
        this.rmc = rmc;

        server = HttpServer.create(new InetSocketAddress(1234), 0);

        server.createContext("/", new RootHandler());
        server.createContext("/chatMessage", new ChatMessageHandler());

        server.setExecutor(null);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            HTTPProcessor.writeResponse(exchange, "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h2>remoteMC</h2>\n" +
                    "\n" +
                    "<form action=\"/chatMessage\">\n" +
                    "  Chat Message:<br>\n" +
                    "  <input type=\"text\" name=\"msg\" value=\"Message\">\n" +
                    "  <br><br>\n" +
                    "  <input type=\"submit\" value=\"Send Chat Message\">\n" +
                    "</form> \n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n");
        }
    }

    static class ChatMessageHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = HTTPProcessor.queryToMap(exchange.getRequestURI().getQuery());

            rmc.getMinecraft().thePlayer.sendChatMessage(params.get("msg"));

            HTTPProcessor.writeResponse(exchange, "<html><head><script>\n" +
                    "  window.location.href = \"/\";\n" +
                    "</script></head></html>");
        }
    }

    private static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());

        OutputStream os = httpExchange.getResponseBody();

        os.write(response.getBytes());
        os.close();
    }

    private static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<>();

        for(String param : query.split("&")) {
            String pair[] = param.split("=");

            if(pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }

        return result;
    }
}
