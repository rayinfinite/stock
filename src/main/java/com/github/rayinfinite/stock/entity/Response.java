package com.github.rayinfinite.stock.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;
    private Object data;

    public Response() {
        this.code = "success";
        this.message = "success";
    }

    public Response(Object data) {
        this();
        this.data = data;
    }
}
