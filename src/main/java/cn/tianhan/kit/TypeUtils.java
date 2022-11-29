package cn.tianhan.kit;


import cn.tianhan.kit.exceptions.ConvertTypeException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: NieAnTai
 * @Description: 类型转换工具类
 * @Date: 17:40 2019/1/2
 */
public class TypeUtils {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String toString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    public static Byte toByte(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Byte) {
            return (Byte) value;
        }
        if (value instanceof String) {
            String tmp = (String) value;
            if ("null".equals(tmp) ||
                    "NULL".equals(tmp) ||
                    tmp.length() == 0) {
                return null;
            }

            return Byte.parseByte(tmp);
        }

        throw new ConvertTypeException(String.format("Byte类型转换失败,原始值%s", value));
    }

    public static Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }
        if (value instanceof String) {
            String tmp = (String) value;
            if (tmp.length() == 0) {
                return null;
            }
            switch (tmp) {
                case "null":
                case "NULL":
                    return null;
                case "true":
                case "TRUE":
                case "1":
                    return Boolean.TRUE;
                case "false":
                case "FALSE":
                case "0":
                    return Boolean.FALSE;
                default:
                    break;
            }
        }

        throw new ConvertTypeException(String.format("Boolean类型转换失败,原始值:%s", value));
    }

    public static Float toFloat(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            String tmp = (String) value;
            if ("null".equals(tmp) ||
                    "NULL".equals(tmp) ||
                    tmp.length() == 0) {
                return null;
            }
            if (tmp.contains(",")) {
                tmp = tmp.replaceAll(",", "");
            }
            if (isNumber(tmp)) {
                return Float.parseFloat(tmp);
            }
        }

        throw new ConvertTypeException(String.format("Float类型转换失败,原始值:%s", value));
    }

    public static Double toDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            String tmp = (String) value;
            if ("null".equals(tmp) ||
                    "NULL".equals(tmp) ||
                    tmp.length() == 0) {
                return null;
            }
            if (tmp.contains(",")) {
                tmp = tmp.replaceAll(",", "");
            }
            if (isNumber(tmp)) {
                return Double.parseDouble(tmp);
            }
        }

        throw new ConvertTypeException(String.format("Double类型转换失败,原始值:%s", value));
    }

    public static Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            String tmp = (String) value;
            if ("null".equals(tmp) ||
                    "NULL".equals(tmp) ||
                    tmp.length() == 0) {
                return null;
            }
            if (tmp.contains(",")) {
                tmp = tmp.replaceAll(",", "");
            }
            if (isNumeric(tmp)) {
                return Integer.parseInt(tmp);
            }
        }

        throw new ConvertTypeException(String.format("Integer类型转换失败,原始值:%s", value));
    }

    public static String toPercent(Object value, int scale) {
        if (null == value) {
            return null;
        }

        BigDecimal div = BigDecimal.valueOf(100);
        if (value instanceof String) {
            String t = (String) value;
            t = t.replaceAll("%", "");
            if (isNumber(t)) {
                BigDecimal n = new BigDecimal(t);
                n = n.divide(div, scale, BigDecimal.ROUND_HALF_UP);
                return n.toString();
            }
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).divide(div, scale).toString();
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value).divide(div, scale).toString();
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue()).divide(div, scale).toString();
        }
        throw new ConvertTypeException(String.format("非百分比数值,原始值:%s", value));
    }

    public static Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            String tmp = (String) value;
            if ("null".equals(tmp) ||
                    "NULL".equals(tmp) ||
                    tmp.length() == 0) {
                return null;
            }
            if (tmp.contains(",")) {
                tmp = tmp.replaceAll(",", "");
            }
            if (isNumeric(tmp)) {
                return Long.parseLong(tmp);
            }
        }

        throw new ConvertTypeException(String.format("Long类型转换失败,原始值:%s", value));
    }

    public static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value);
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            String tmp = (String) value;
            if ("null".equals(tmp) ||
                    "NULL".equals(tmp) ||
                    tmp.length() == 0) {
                return null;
            }
            if (tmp.contains(",")) {
                tmp = tmp.replaceAll(",", "");
            }
            if (isNumber(tmp)) {
                return new BigDecimal(tmp);
            }
        }

        throw new ConvertTypeException(String.format("BigDecimal类型转换失败,原始值:%s", value));
    }

    public static Boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            char t = str.charAt(i);
            if (!Character.isDigit(t)) {
                if (t == '+' || t == '-') {
                    if (i == 0) {
                        continue;
                    }
                }
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static Boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            char t = str.charAt(i);
            if (!Character.isDigit(t)) {
                if (t == '.' && i != 0) {
                    continue;
                }
                if (t == '+' || t == '-') {
                    if (i == 0) {
                        continue;
                    }
                }
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static Date toDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        if (value instanceof String) {
            String tmp = (String) value;
            if (tmp.contains("/")) {
                tmp = tmp.replaceAll("/", "-");
            }
            if (tmp.contains(".")) {
                tmp = tmp.replaceAll("\\.", "-");
            }
            String dateFormat = null;
            if ("null".equals(tmp) ||
                    "NULL".equals(tmp) ||
                    tmp.length() == 0) {
                return null;
            }
            if (tmp.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}$")) {
                dateFormat = DEFAULT_DATE_FORMAT;
            }
            if (tmp.matches("^[0-9]{4}-[0-9]{1,2}$")) {
                dateFormat = "yyyy-M";
            }
            if (tmp.matches("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$")) {
                dateFormat = "yyyy-MM-dd";
            }
            if (tmp.matches("^[0-9]{17}$")) {
                dateFormat = "yyyyMMddHHmmssSSS";
            }
            if (tmp.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}$")) {
                dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
            }
            if (tmp.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}$")) {
                dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
            }
            if (tmp.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}$")) {
                dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            }
            if (dateFormat != null) {
                try {
                    return new SimpleDateFormat(dateFormat).parse(tmp);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new ConvertTypeException(String.format("Date类型转换失败 日期格式错误,原始值:%s", value));
                }
            }
        }

        throw new ConvertTypeException(String.format("Date类型转换失败,原始值:%s", value));
    }
}
