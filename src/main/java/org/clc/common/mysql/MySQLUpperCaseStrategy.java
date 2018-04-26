package org.clc.common.mysql;

//import org.hibernate.boot.model.naming.Identifier;
//import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
//import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
//import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mysql实体/表命名规范定义
 */
//@Component
public class MySQLUpperCaseStrategy/* extends PhysicalNamingStrategyStandardImpl */{

    /**
     * 转换表名对应格式------全部转换成大写
     */
//    @Override
//    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
//        return name.toIdentifier(name.getText().toUpperCase());
//    }

    /**
     * 转换表字段对应格式------大写（含下划线）
     */
//    @Override
//    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
//        return name.toIdentifier(hump2upWithUnderline(name.getText()));
//    }

    /**
     * 驼峰转大写（含下划线）
     * @param propertyName 待转换对象属性
     */
    private static String hump2upWithUnderline(String propertyName) {
        Matcher matcher = Pattern.compile("[A-Z]").matcher(propertyName);
        while(matcher.find())
            propertyName = propertyName.replaceFirst(matcher.group(), "_" + matcher.group());
        return propertyName.toUpperCase();
    }
    private static String hump2upWithUnderline1(String propertyName) {
        return Pattern.compile("([A-Z])").matcher(propertyName).replaceAll("_$0").toUpperCase();
    }

    public static void main(String[] args) {
        String str = "iMissYou";
        str = hump2upWithUnderline1(str);
//        str = hump2upWithUnderline(str);
        System.out.println(str);
    }
}
