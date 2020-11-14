package top.arien.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Arien-天柱
 **/
@Data
public class Result implements Serializable {
        private int code;//200是正常
        private String msg;
        private Object data;

        public static Result success(int code, String msg, Object data){
            Result r = new Result();
            r.setCode(code);
            r.setMsg(msg);
            r.setData(data);
            return r;
        }

    public static Result success(Object data){

        return success(200,"操作成功", data);
    }

    public static Result fail(String msg, Object data){

        return success(400, msg, data);
    }

    public static Result fail(String msg){

        return success(400, msg, null);
    }

    public static Result fail(int code, String msg, Object data){

        return success(code, msg, data);
    }
}
