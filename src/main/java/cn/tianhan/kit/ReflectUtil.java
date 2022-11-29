package cn.tianhan.kit;


import java.lang.reflect.Field;

/**
 * @Author: NieAnTai
 * @Description: Java 字段反射工具类
 * @Date: 11:01 2019/4/17
 */
public class ReflectUtil {
    /**
     * 获取对象属性字段值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Object value = null;
        Field field = getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                value = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     * 获取对象属性字段
     */
    public static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException ignore) {
            }
        }
        return field;
    }

    /**
     * 设置对象属性值
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) {
        Field field = getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
