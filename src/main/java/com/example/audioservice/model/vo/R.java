package com.example.audioservice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Ruby
 * @create 2020-10-02 09:46:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class R<T> {
    private Integer code;
    private String msg;
    private Long count;
    private T data;


    public static R ok() {
        return new R(0, "ok", null, null);
    }

    public static <T> R ok(T data) {
        return new R(0, "ok", null, data);
    }

    public static <T> R ok(Long count, T data) {
        return new R(0, "ok", count, data);
    }

    public static R error() {
        return new R(9999, "error", null, null);
    }

    public static <T> R error(T data) {
        return new R(9999, "error", null, data);
    }
}
