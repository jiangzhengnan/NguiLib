package com.ng.ngcommon.http;

import java.io.Serializable;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by hd_01 on 2016/9/29.
 */

public class HttpResult implements Serializable {
    public Boolean suc;
    public String msg;
    public transient String rs;
    public Integer code;


    public Boolean getSuc() {
        return suc;
    }

    public void setSuc(Boolean suc) {
        this.suc = suc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
