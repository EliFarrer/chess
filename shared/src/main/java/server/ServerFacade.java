package server;


import com.google.gson.Gson;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.LoginResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    String url;
    public ServerFacade(int port) {
        url = "http://localhost:" + port;
    }

    public void clear() throws ResponseException {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public LoginResult register(RegisterRequest req) throws ResponseException {
        String path = "/user";
        return this.makeRequest("POST", path, req, LoginResult.class, null);
    }

    public void logout(String authToken) throws ResponseException {
        String path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public LoginResult login(LoginRequest req) {
        String path = "/session";
        return this.makeRequest("POST", path, req, LoginResult.class, null);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest req) {
        String path = "/game";
        return this.makeRequest("POST", path, req, CreateGameResult.class, authToken);
    }

    public ListGamesResult listGames(String authToken) {
        String path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResult.class, authToken);
    }

    public void joinGame(String authToken, JoinGameRequest req) {
        String path = "/game";
        this.makeRequest("PUT", path, req, null, authToken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authorization) throws ResponseException {
        // note that this is a generic method (T) that returns something of type T and takes in a class of type T as the response
        try {
            URL url = (new URI(this.url + path)).toURL();   // create a URL
            HttpURLConnection http = (HttpURLConnection) url.openConnection();  // get a connection
            http.setRequestMethod(method);  // set the HTTP method
            http.setDoOutput(true); // we will be sending data over

            if (authorization != null) {
                http.setRequestProperty("Authorization", authorization);
            }
            if (request != null) {
                writeBody(request, http);
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        // this just writes the body of our request.
        if (request != null) {  // make sure our request is valid
            http.addRequestProperty("Content-Type", "application/json");    // specify in header that our data is json
            String reqData = new Gson().toJson(request);    // convert request to json
            try (OutputStream reqBody = http.getOutputStream()) {   // get the body and write to it
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;  // set our response
        if (http.getContentLength() < 0) {  // if we don't know (-1)
            try (InputStream respBody = http.getInputStream()) {    // get the input stream
                InputStreamReader reader = new InputStreamReader(respBody); // convert that to a reader
                if (responseClass != null) {
                    // take the things in our reader and convert it to whatever class we passed in
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) { // getting the error message
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);    // if there isn't an error message
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;   // if we got a 200 status code
    }
}
