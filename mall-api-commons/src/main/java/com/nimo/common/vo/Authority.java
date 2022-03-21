package com.nimo.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @program: mall-springcloud
 * @ClassName: Authority
 * @description:
 * @author: chuf
 * @create: 2022-03-21 15:46
 **/
@Data
@SuperBuilder
@NoArgsConstructor
public class Authority implements Serializable {

    private String authority;

}
