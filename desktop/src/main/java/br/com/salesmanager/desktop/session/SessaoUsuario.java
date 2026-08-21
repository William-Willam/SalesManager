package br.com.salesmanager.desktop.session;

public class SessaoUsuario {

    private static final SessaoUsuario INSTANCIA = new SessaoUsuario();

    private String token;
    private String nome;
    private String email;
    private String papel;

    private SessaoUsuario() {
    }

    public static SessaoUsuario getInstancia() {
        return INSTANCIA;
    }

    public void iniciar(String token, String nome, String email, String papel) {
        this.token = token;
        this.nome = nome;
        this.email = email;
        this.papel = papel;
    }

    public void encerrar() {
        this.token = null;
        this.nome = null;
        this.email = null;
        this.papel = null;
    }

    public boolean estaLogado() {
        return token != null;
    }

    public String getToken() {
        return token;
    }

    public String getNome() {
        return nome;
    }

    public String getPapel() {
        return papel;
    }
}