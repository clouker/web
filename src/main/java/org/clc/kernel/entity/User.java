package org.clc.kernel.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * MySQLUpperCaseStrategy类中定义表名、字段命名规范
 */
@Entity
@Data
//@Table(name = "user") //表名对应类名（默认全大写，可省去@Table注解）
// 返回null值时，省去对象中的该字段
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String nickName;
    private Integer deleteFlag;
    private Integer createUser;
    private Timestamp createTime;
    private Integer updateUser;
    private Timestamp updateTime;
}
