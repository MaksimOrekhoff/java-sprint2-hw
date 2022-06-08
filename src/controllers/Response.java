package controllers;

public  class Response {
    private final String response;
    private final int code;
    public Response(String response, int code) {
        this.response = response;
        this.code = code;
    }
    public String getResponse() {
        return response;
    }
    public int getCode() {
        return code;
    }
}
