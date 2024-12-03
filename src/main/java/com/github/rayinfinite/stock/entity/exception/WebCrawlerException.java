package com.github.rayinfinite.stock.entity.exception;

public class WebCrawlerException extends RuntimeException {
    public WebCrawlerException(String message) {
        super(message);
    }

    public WebCrawlerException(Throwable cause) {
        super(cause);
    }
}
