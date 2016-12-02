package tttweb.controllers;

import httpserver.Request;
import httpserver.httprequests.RequestHeader;
import httpserver.routing.Method;

import java.net.URI;

class RequestDouble implements Request {

    private final Method method;
    private final String url;
    private String cookie;
    private String data;

    public RequestDouble(String url, Method method) {
        this.url = url;
        this.method = method;
    }

    public boolean hasHeader(RequestHeader requestHeader) {
        if (requestHeader == RequestHeader.COOKIE) {
            return cookie != null;
        } else {
            return data != null;
        }
    }

    public String getValue(RequestHeader requestHeader) {
        if (requestHeader == RequestHeader.COOKIE) {
            return cookie;
        } else {
            return data;
        }
    }

    public Method getMethod() {
        return method;
    }

    public URI getRequestURI() {
        return null;
    }

    public void addCookie(String id) {
        this.cookie = id;
    }

    public void addData(String gameType) {
        this.data = gameType;
    }
}
