package br.com.ucs.laboratorio.gestao.application.exception;

public class BusinessException  extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}