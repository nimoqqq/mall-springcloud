package com.nimo.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: mall-springcloud
 * @ClassName: UmsAdmin
 * @description:
 * @author: chuf
 * @create: 2022-03-21 15:44
 **/
@Data
public class UmsAdmin implements Serializable {
    private Long id;

    private String username;

    private String password;

    private String icon;

    private String email;

    private String nickName;

    private String note;

    private Date createTime;

    private Date loginTime;

    private Integer status;

    private static final long serialVersionUID = 1L;
}
