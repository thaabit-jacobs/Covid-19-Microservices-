package org.jcode.exceptions;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ExceptionModel {
    private String msg;
    private final Throwable throwable;
    private final ZonedDateTime timeStamp;

    public ExceptionModel(
            String msg, Throwable throwable, ZonedDateTime timeStamp) {
        this.msg = msg;
        this.throwable = throwable;
        this.timeStamp = timeStamp;
    }
}
