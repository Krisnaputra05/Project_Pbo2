package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.PrivateKey;
import java.util.concurrent.Executors;

public class HttpConnection {
    private ItemsHandler itemsHandler;
    private CustomersHandler customersHandler;
    private ShippingAddresses ShippingAddresses;
    private SubscriptionsHandler SubscriptionsHandler;
    private CardsHandler CardsHandler;
    private  SubscriptionsItemsHandler SubscriptionsItemsHandler;
    public HttpConnection() {
        DatabaseKeren database = new DatabaseKeren();
        itemsHandler = new ItemsHandler(database);
        customersHandler = new CustomersHandler(database);
        ShippingAddresses = new ShippingAddresses(database);
        SubscriptionsHandler = new SubscriptionsHandler(database);
        CardsHandler = new CardsHandler(database);
        SubscriptionsItemsHandler = new SubscriptionsItemsHandler(database);
    }

    public void startServer() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 9023), 0);
        httpServer.createContext("/", new Handler());
        httpServer.setExecutor(Executors.newSingleThreadExecutor());
        httpServer.start();
    }

    private class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String[] path = exchange.getRequestURI().getPath().split("/");
            String query = exchange.getRequestURI().getQuery();
            String response = "";
            if (method.equals("GET")) {
                if (path[1].equals("items")) {
                    response = itemsHandler.getProductsMethod(path, query);
                } else if (path[1].equals("customers")) {
                    response = customersHandler.getCustomersMethod(path, query);
                } else if (path[1].equals("shipping_addresses")) {
                    response = ShippingAddresses.getShippingAddressesMethod(path, query);
                } else if (path[1].equals("Subscriptions")) {
                    response = SubscriptionsHandler.getSubscriptionsMethod(path, query);
                } else if (path[1].equals("Cards")) {
                    response = CardsHandler.getCardsMethod(path, query);
                } else if (path[1].equals("Subscriptions_items")) {
                    response = SubscriptionsItemsHandler.getSubscriptionsItemsMethod(path, query);
                }
            } else if (method.equals("POST")) {
                if (path[1].equals("items")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = itemsHandler.postMethod(requestBodyJson);
                } else if (path[1].equals("customers")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = customersHandler.postMethod(requestBodyJson);
                } else if (path[1].equals("shipping_addresses")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = ShippingAddresses.postMethod(requestBodyJson);
                } else if (path[1].equals("Subscriptions")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = SubscriptionsHandler.postMethod(requestBodyJson);
                }else if (path[1].equals("Cards")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = CardsHandler.postMethod(requestBodyJson);
                } else if (path[1].equals("subscriptions_Items")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response =SubscriptionsItemsHandler.postMethod(requestBodyJson);
                }
            } else if (method.equals("DELETE")) {
                if (path[1].equals("items")) {
                    response = itemsHandler.deleteMethod(Integer.parseInt(path[2]));
                } else if (path[1].equals("customers")) {
                    response = customersHandler.deleteMethod(Integer.parseInt(path[2]));
                } else if (path[1].equals("shipping_addresses")) {
                    response = ShippingAddresses.deleteMethod(Integer.parseInt(path[2]));
                } else if (path[1].equals("Subscriptions")) {
                    response = SubscriptionsHandler.deleteMethod(Integer.parseInt(path[2]));
                }else if (path[1].equals("Cards")) {
                    response = CardsHandler.deleteMethod(Integer.parseInt(path[2]));
                } else if (path[1].equals("Subscriptions_Items")) {
                    response = SubscriptionsItemsHandler.deleteMethod(Integer.parseInt(path[2]));
                }
            }else if(method.equals("PUT")) {
                if (path[1].equals("items")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = itemsHandler.putMethod(path[2], requestBodyJson);
                } else if (path[1].equals("customers")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = customersHandler.putMethod(path[2], requestBodyJson);
                } else if (path[1].equals("shipping_addresses")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = ShippingAddresses.putMethod(path[2], requestBodyJson);
                } else if (path[1].equals("Subscriptions")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = SubscriptionsHandler.putMethod(path[2], requestBodyJson);
                }else if (path[1].equals("Cards")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = CardsHandler.putMethod(path[2], requestBodyJson);
                } else if (path[1].equals("Subscription_Items")) {
                    JSONObject requestBodyJson = parseRequestBody(exchange.getRequestBody());
                    response = SubscriptionsItemsHandler.putMethod(path[2], requestBodyJson);
                }
            }
            OutputStream outputStream = exchange.getResponseBody();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }

        private JSONObject parseRequestBody(InputStream requestBody) throws IOException {
            byte[] requestBodyBytes = requestBody.readAllBytes();
            String requestBodyString = new String(requestBodyBytes);
            return new JSONObject(requestBodyString );
        }
    }
}
