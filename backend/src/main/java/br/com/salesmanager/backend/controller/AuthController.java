package br.com.salesmanager.backend.controller;

import br.com.salesmanager.backend.config.JwtUtil;
import br.com.salesmanager.backend.dto.LoginRequest;
import br.com.salesmanager.backend.dto.LoginResponse;
import br.com.salesmanager.backend.exception.RegraNegocioException;
import br.com.salesmanager.backend.model.Usuario;
import br.com.salesmanager.backend.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RegraNegocioException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new RegraNegocioException("E-mail ou senha inválidos");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail(), usuario.getPapel().name());

        return ResponseEntity.ok(new LoginResponse(
                token, usuario.getNome(), usuario.getEmail(), usuario.getPapel()));
    }
}