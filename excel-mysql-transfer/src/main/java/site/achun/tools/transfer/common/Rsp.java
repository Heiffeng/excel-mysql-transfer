package site.achun.tools.transfer.common;


import lombok.Data;

import java.io.Serializable;

/**
 * Common Response
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 15:12:04
 */
@Data
public class Rsp<T> implements Serializable {

    public static final Integer CODE_SUCCESS = 0;
    public static final Integer CODE_ERROR_COMMON = 1;
    private Integer code;
    private String mark;
    private String info;
    private T data;

    public Rsp() {
    }

    public static <T> Rsp<T> success(T data, String info) {
        Rsp<T> result = new Rsp<>();
        result.code = CODE_SUCCESS;
        result.info = info;
        result.data = data;
        return result;
    }

    public static <T> Rsp<T> success(T data) {
        return success(data, "");
    }

    public static <T> Rsp<T> error(String info) {
        return error(info, CODE_ERROR_COMMON);
    }

    public static <T> Rsp<T> error(String info, Integer code) {
        if (CODE_SUCCESS.equals(code)) {
            throw new IllegalArgumentException(String.format("code(%d) 必须非0", code));
        } else {
            Rsp<T> result = new Rsp<>();
            result.code = code;
            result.info = info;
            return result;
        }
    }

    public static <T, S> Rsp<T> error(Rsp<S> rsp, T data) {
        if (rsp == null) {
            return null;
        } else {
            Rsp<T> newRsp = new Rsp<>();
            newRsp.setCode(rsp.getCode());
            newRsp.setInfo(rsp.getInfo());
            newRsp.setMark(rsp.getMark());
            newRsp.setData(data);
            return newRsp;
        }
    }

    public static <T> Rsp<T> error(String mark, String info) {
        Rsp<T> rsp = error(info, CODE_ERROR_COMMON);
        rsp.setMark(mark);
        return rsp;
    }

    public Boolean success() {
        return null != this.code && this.code.equals(CODE_SUCCESS);
    }

}
