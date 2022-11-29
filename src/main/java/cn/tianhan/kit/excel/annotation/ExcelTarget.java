package cn.tianhan.kit.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: NieAnTai
 * @Description:
 * @Date: 16:29 2018/8/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelTarget {
    /**
     * 字段名
     */
    String value() default "";

    /**
     * Excel列 例如：A、B、C
     */
    String column() default "";

    /**
     * 排序
     */
    int order() default -1;

    /**
     * 日期格式
     */
    String dateFormat() default "yyyy/MM/dd HH:mm:ss";
}
