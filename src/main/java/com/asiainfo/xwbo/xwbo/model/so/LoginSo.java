package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-11 18:00
 */
@Data
public class LoginSo extends SignSo{
    private String userCode;
    private String password;
}
