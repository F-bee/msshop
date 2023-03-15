package com.kk.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UserVO {
    private Integer userId;
    private String mobile;
    private String password;
    private String token;
}
