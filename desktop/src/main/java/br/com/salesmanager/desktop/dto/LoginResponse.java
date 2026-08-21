package br.com.salesmanager.desktop.dto;

public record LoginResponse(String token, String nome, String email, String papel) {
}