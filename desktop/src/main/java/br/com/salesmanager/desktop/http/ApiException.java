package br.com.salesmanager.desktop.http;

public class ApiException extends RuntimeException {
    private final int statusCode;

    public ApiException(int statusCode, String mensagem) {
        super(mensagem);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}