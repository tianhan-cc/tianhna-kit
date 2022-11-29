package cn.tianhan.kit;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: NieAnTai
 * @Description:
 * @Date: 16:13 2019/1/2
 */
public class ConvertMap implements Map<String, Object>, Serializable {
    private final static long serialVersionUID = 1L;
    private final static int DEFAULT_INT = 2 << 3;
    private final Map<String, Object> map;

    public ConvertMap() {
        this(DEFAULT_INT, false);
    }

    public ConvertMap(int size) {
        this(size, false);
    }

    public ConvertMap(boolean linked) {
        this(DEFAULT_INT, linked);
    }

    public ConvertMap(int size, boolean linked) {
        if (linked) {
            map = new LinkedHashMap<>(size);
        } else {
            map = new HashMap<>(size);
        }
    }

    public ConvertMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getString(String key) {
        return TypeUtils.toString(get(key));
    }
    public String getString(String... key) {
        return TypeUtils.toString(get(key));
    }

    public Byte getByte(String key) {
        return TypeUtils.toByte(get(key));
    }
    public Byte getByte(String... key) {
        return TypeUtils.toByte(get(key));
    }

    public Integer getInteger(String key)  {
        return TypeUtils.toInteger(get(key));
    }
    public Integer getInteger(String... key) {
        return TypeUtils.toInteger(get(key));
    }

    public Float getFloat(String key) {
        return TypeUtils.toFloat(key);
    }
    public Float getFloat(String... key) {
        return TypeUtils.toFloat(key);
    }

    public Double getDouble(String key) {
        return TypeUtils.toDouble(get(key));
    }
    public Double getDouble(String... key) {
        return TypeUtils.toDouble(get(key));
    }
    public Long getLong(String key) {
        return TypeUtils.toLong(get(key));
    }
    public Long getLong(String... key) {
        return TypeUtils.toLong(get(key));
    }

    public Boolean getBoolean(String key) {
        return TypeUtils.toBoolean(get(key));
    }
    public Boolean getBoolean(String... key) {
        return TypeUtils.toBoolean(get(key));
    }

    public String getPercent(String key) {
        return getPercent(key, 4);
    }
    public String getPercent(String... key) {
        return getPercent(4,key);
    }

    public String getPercent(String key, int scale) {
        return TypeUtils.toPercent(get(key), scale);
    }

    public String getPercent(int scale, String... key) {
        return TypeUtils.toPercent(get(key), scale);
    }

    public BigDecimal getBigDecimal(String key) {
        return TypeUtils.toBigDecimal(get(key));
    }

    public BigDecimal getBigDecimal(String... key) {
        return TypeUtils.toBigDecimal(key);
    }

    public Date getDate(String key) {
        return TypeUtils.toDate(get(key));
    }

    public Date getDate(String... key) {
        return TypeUtils.toDate(get(key));
    }

    public Object get(String... args) {
        ConvertMap map = this;
        Object value = null;
        for (int i = 0; i < args.length; i++) {
            String currentKey = args[i];
            value = map.get(currentKey);
            if (value instanceof ConvertMap) {
                map = (ConvertMap) value;
            }
        }
        return value;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
}
