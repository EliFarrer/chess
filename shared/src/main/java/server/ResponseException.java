package server;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResponseException extends RuntimeException {
  int statusCode;
  public ResponseException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public String toJson() {
    // convert this error to a json file
    return new Gson().toJson(Map.of("message", getMessage(), "status", getStatusCode()));
  }

  public static ResponseException fromJson(InputStream stream) {
    var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);  // create a map of the string
    var status = ((Double) map.get("status")).intValue(); // get the status code
    String message = map.get("message").toString();       // get the message
    return new ResponseException(status, message);        // return the exception
  }

  public static ResponseException fromJson(int statusCode, InputStream stream) {
    var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);  // create a map of the string
    String message = map.get("message").toString();       // get the message
    return new ResponseException(statusCode, message);        // return the exception
  }
}
