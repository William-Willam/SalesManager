package br.com.salesmanager.backend.service;

import br.com.salesmanager.backend.dto.UsuarioRequest;
import br.com.salesmanager.backend.dto.UsuarioResponse;
import br.com.salesmanager.backend.dto.UsuarioUpdateRequest;
import br.com.salesmanager.backend.exception.RecursoNaoEncontradoException;
import br.com.salesmanager.backend.exception.RegraNegocioException;
import br.com.salesmanager.backend.model.Usuario;
import br.com.salesmanager.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<UsuarioResponse> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(UsuarioResponse::de);
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = buscarEntidadePorId(id);
        return UsuarioResponse.de(usuario);
    }

    public UsuarioResponse criar(UsuarioRequest request) {
        usuarioRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new RegraNegocioException("Já existe um usuário com este e-mail: " + request.email());
        });

        Usuario usuario = new Usuario(
                request.nome(),
                request.email(),
                passwordEncoder.encode(request.senha()),
                request.papel(),
                null
        );

        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    public UsuarioResponse atualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = buscarEntidadePorId(id);

        usuarioRepository.findByEmail(request.email()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new RegraNegocioException("Já existe um usuário com este e-mail: " + request.email());
            }
        });

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setPapel(request.papel());

        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }

        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    public void excluir(Long id) {
        buscarEntidadePorId(id);
        usuarioRepository.deleteById(id);
    }

    private Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id: " + id));
    }
}