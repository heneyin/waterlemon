package com.henvealf.watermelon.sql2;

/**
 * <p>
 *
 * <p>
 *
 * @author hongliang.yin/Henvealf on 2019/1/8
 */
public class IllegalTypeException extends IllegalArgumentException {

    private final static String MESSAGE = "The type is wrong.";

    public IllegalTypeException() {
        super(MESSAGE);
    }

    public IllegalTypeException(String message) {
        super(message);
    }

}
