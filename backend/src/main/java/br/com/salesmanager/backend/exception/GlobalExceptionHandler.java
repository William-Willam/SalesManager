package br.com.salesmanager.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroResposta erro = new ErroResposta(
                LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado", ex.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> tratarRegraNegocio(RegraNegocioException ex) {
        ErroResposta erro = new ErroResposta(
                LocalDateTime.now(), HttpStatus.CONFLICT.value(),
                "Violação de regra de negócio", ex.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        ErroResposta erro = new ErroResposta(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                "Dados inválidos", "Um ou mais campos estão inválidos", detalhes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> tratarGenerico(Exception ex) {
        ErroResposta erro = new ErroResposta(
                LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno", "Ocorreu um erro inesperado no servidor", List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErroResposta> tratarIntegridade(org.springframework.dao.DataIntegrityViolationException ex) {
        ErroResposta erro = new ErroResposta(
                LocalDateTime.now(), HttpStatus.CONFLICT.value(),
                "Violação de integridade",
                "Não é possível excluir: este registro está sendo usado em outro lugar do sistema",
                List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException.class)
    public ResponseEntity<ErroResposta> tratarNaoEncontradoDelete(org.springframework.dao.EmptyResultDataAccessException ex) {
        ErroResposta erro = new ErroResposta(
                LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado", "O registro que você tentou excluir não existe", List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErroResposta> tratarAcessoNegado(org.springframework.security.access.AccessDeniedException ex) {
        ErroResposta erro = new ErroResposta(
                LocalDateTime.now(), HttpStatus.FORBIDDEN.value(),
                "Acesso negado", "Você não tem permissão para realizar esta ação", List.of());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

}