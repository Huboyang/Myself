package com.hby.myselfproject.entity;

import lombok.Data;

/**
 * 实体类 字段类型与数据库对应
 */

//lombak的注解 自动set get 需要在IDEA 里下载插件
@Data
public class Shop {
    private Integer id;

    private Integer age;

    private String cupsize;

}
