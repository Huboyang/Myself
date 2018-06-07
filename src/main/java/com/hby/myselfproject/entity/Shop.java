package com.hby.myselfproject.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 实体类 字段类型与数据库对应
 */

//lombak的注解 自动set get 需要在IDEA 里下载插件
@Data
@Entity
public class Shop {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer age;

    private String cupsize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCupsize() {
        return cupsize;
    }

    public void setCupsize(String cupsize) {
        this.cupsize = cupsize;
    }
}