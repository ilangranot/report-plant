package com.reportplant.Controller;

import com.reportplant.Main;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestConnection {
    private static final String PROTOCOL = "https";
    private static final String HOST = "<URL>";
    private static final int PORT = 443;
    private static final String PATH = "<PATH>";
    private static final String USERNAME = "<USERNAME>";
    private static final String PASSWORD = "<PASSWORD>";

    private static Logger logger;

    public RestConnection(){
        logger = Logger.getLogger(this.getClass().getName());
        Utils.configureLogger(logger);
    }

    public static Client getClient(){
        return getClient(logger, USERNAME, PASSWORD);
    }

    public static Client getClient(Logger logger, String username, String password){
        Feature httpAuthenticationFeature = HttpAuthenticationFeature.basic(username, password);
        Feature loggingFeature = new LoggingFeature(logger);
        return ClientBuilder.newBuilder()
                .register(httpAuthenticationFeature)
                .register(loggingFeature)
                .build();
    }


    public static String getJsonResponseString(Client client, String params, Logger logger)
            throws RuntimeException, UnsupportedEncodingException, URISyntaxException {
        String uriString = new URI(PROTOCOL, null, HOST, PORT,
                PATH, params, null).toString();
        WebTarget webTarget = client.target(uriString);
        Invocation.Builder requestInvocationBuilder = webTarget.request();
        requestInvocationBuilder.accept(MediaType.APPLICATION_JSON);
        Response response = requestInvocationBuilder.get();

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            logger.log(Level.ALL,"Success! " + response.getStatus());
        } else {
            logger.log(Level.ALL,"ERROR! " + response.getStatus());
            logger.log(Level.ALL,(String) response.getEntity());
            logger.log(Level.ALL,response.readEntity(String.class));
            throw new RuntimeException(){
                //...
            };
        }
        return response.readEntity(String.class);
    }
}
