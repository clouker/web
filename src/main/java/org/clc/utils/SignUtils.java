package org.clc.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class SignUtils {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SignIgnore {
    }

    public static String getSignStr(Object object) {
        Class cls = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (cls != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(cls.getDeclaredFields()));
            cls = cls.getSuperclass(); //得到父类,然后赋给自己
        }

        Map<String, Object> signMap = fieldList.stream()
                .filter(SignUtils::shouldFilter)
                .collect(HashMap::new,
                        (m, v) -> m.put(v.getName(),
                                getFieldValue(v, object)),
                        HashMap::putAll);

        return signMap.entrySet().stream()
                .filter(p -> p.getValue() != null && p.getValue().toString().trim().length() > 0)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(p -> p.getKey() + "=" + p.getValue())
                .reduce((p1, p2) -> p1 + "&" + p2)
                .orElse("");
    }

    public static String getSignStr(Map<String, Object> signMap) {
        return signMap.entrySet().stream()
                .filter(p -> p.getValue() != null && p.getValue().toString().trim().length() > 0)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(p -> {
                    Object value = p.getValue();
                    if ("amount".equals(p.getKey()) || "charges".equals(p.getKey())) {
                        value = new BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP);
                    }
                    return p.getKey() + "=" + value;
                })
                .reduce((p1, p2) -> p1 + "&" + p2)
                .orElse("");
    }

    private static Object getFieldValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            return "";
        }
    }

    private static boolean shouldFilter(Field field) {
        return field.getAnnotation(SignIgnore.class) == null;
    }
}