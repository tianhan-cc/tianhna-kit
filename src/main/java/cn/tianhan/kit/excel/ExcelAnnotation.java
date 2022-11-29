package cn.tianhan.kit.excel;


import cn.tianhan.kit.TypeUtils;
import cn.tianhan.kit.excel.annotation.ExcelTarget;
import cn.tianhan.kit.exceptions.ConvertTypeException;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: NieAnTai
 * @Description: Excel注解分析工具类
 * @Date: 16:10 2018/8/22
 */
public class ExcelAnnotation {
    /**
     * 注解类
     */
    private final Class<? extends Annotation> annClass = ExcelTarget.class;
    /**
     * 标记类
     */
    private final Class<?> zClass;
    /**
     * 是否被标记
     */
    private boolean flag = true;
    /**
     * 注解字段
     */
    private List<Field> annField;
    /**
     * 注解内容
     */
    private List<String> annValue;

    public ExcelAnnotation(Class<?> zClass) {
        this.zClass = zClass;
        analyze();
    }

    /**
     * 解析注解信息
     */
    private void analyze() {
        List<Field> fields = Arrays.asList(zClass.getDeclaredFields());
        this.annField = fields.stream().filter(f -> f.isAnnotationPresent(annClass))
                .sorted((f1, f2) -> {
                    ExcelTarget ann = (ExcelTarget) f1.getAnnotation(annClass);
                    ExcelTarget ann2 = (ExcelTarget) f2.getAnnotation(annClass);
                    return ann2.order() - ann.order();
                }).collect(Collectors.toList());
        if (this.annField.size() == 0) {
            flag = false;
            return;
        }
        // 获取Value值
        this.annValue = this.annField.stream().map(f -> ((ExcelTarget) f.getAnnotation(annClass)).value()).collect(Collectors.toList());
    }

    /**
     * 反射,给类字段设置值
     *
     * @param field 类字段
     * @param value 值
     * @param nObj  实例类
     */
    public void setFieldPojo(Field field, Object value, Object nObj) throws Exception {
        if (value == null) {
            return;
        }
        try {
            String name = field.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            String typeName = field.getGenericType().getTypeName();
            Method setMethod = nObj.getClass().getMethod("set" + name, Class.forName(typeName));

            switch (typeName) {
                case "java.lang.String":
                    value = String.valueOf(value);
                    break;
                case "java.util.Date":
                    value = TypeUtils.toDate(value);
                    break;
                case "java.util.Integer":
                    value = TypeUtils.toInteger(value);
                    break;
                case "java.math.BigDecimal":
                    value = TypeUtils.toBigDecimal(value);
                    break;
                default:
                    break;
            }

            setMethod.invoke(nObj, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalStateException("请使用包装数据类型");
        } catch (ConvertTypeException e) {
            LoggerFactory.getLogger(ExcelAnnotation.class).error(e.getMessage());
        }
    }

    /**
     * 反射 获得类字段的值
     */
    public String getFieldPojo(Field field, Object nObj) throws Exception {
        if (nObj == null) return "";
        String name = field.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        Method getMethod = nObj.getClass().getMethod("get" + name);
        Object v = getMethod.invoke(nObj);
        if (v == null) return "";
        if (field.getType().isAssignableFrom(Date.class)) {
            // 日期格式化
            ExcelTarget target = (ExcelTarget) field.getAnnotation(annClass);
            String template = target.dateFormat();
            return new SimpleDateFormat(template).format((Date) v);
        } else {
            return TypeUtils.toString(v);
        }
    }

    public boolean getFlag() {
        return flag;
    }

    public List<Field> getAnnField() {
        return this.annField;
    }

    public List<String> getAnnValue() {
        return this.annValue;
    }

    public List<String> getColumn() {
        return this.annField.stream().map(f -> ((ExcelTarget) f.getAnnotation(annClass)).column()).collect(Collectors.toList());

    }
}
