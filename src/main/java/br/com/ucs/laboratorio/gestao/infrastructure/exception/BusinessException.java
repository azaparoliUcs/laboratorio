package br.com.ucs.laboratorio.gestao.infrastructure.exception;

public class BusinessException  extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}