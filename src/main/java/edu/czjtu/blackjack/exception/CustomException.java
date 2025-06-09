package edu.czjtu.blackjack.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException{
    private String code;
    private String message;

    public CustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
