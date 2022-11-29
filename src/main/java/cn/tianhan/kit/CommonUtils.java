package cn.tianhan.kit;

import cn.tianhan.kit.exceptions.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author NieAnTai
 * @Date 2021/3/28 9:42 下午
 * @Version 1.0
 * @Email nieat@foxmail.com
 * @Description 通用工具类
 **/
public class CommonUtils {

	static Pattern p1= Pattern.compile("[a-z]+");
	static Pattern p2= Pattern.compile("[A-Z]+");
	static Pattern p3= Pattern.compile("[0-9]+");
	static Pattern p4= Pattern.compile("\\pP");

	/**
	 * 验证密码复杂度 8-16位 必须包含 大小写字母数字和标点符号
	 * @param password password
	 */
	public static void validatedPwd(String password){
		String msg = "密码复杂度不够,必须长度8-16位且包含大小写字母数字和标点符号";
		if(StringUtils.isBlank(password)) {
            throw new BusinessException(msg);
        }
        if(password.matches("^.{8,16}$")){
            Matcher m=p1.matcher(password);
            if(!m.find()) {
                throw new BusinessException(msg);
            }
            m.reset().usePattern(p2);
            if(!m.find()) {
                throw new BusinessException(msg);
            }
            m.reset().usePattern(p3);
            if(!m.find()) {
                throw new BusinessException(msg);
            }
            m.reset().usePattern(p4);
            if(!m.find()) {
                throw new BusinessException(msg);
            }
        }else{
           throw new BusinessException(msg);
        }
    }


	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String firstWordUpperCase(String str){
		char[] chars = str.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
	}

	/**
	 * 驼峰转下划线 如 userName -> user_name
	 * @param str
	 * @return
	 */
	public static String camelCaseToUnderscore(String str){
		if(StringUtils.isBlank(str)) {
			return str;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= 65 && c <= 90) {
				if (i != 0) {
					builder.append("_");
				}
				builder.append(String.valueOf(c).toLowerCase());
			} else {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	/**
	 * 下划线转驼峰 如 user_name -> userName
	 * @param str
	 * @return
	 */
	public static String underscoreToCamelCase(String str){
		if(StringUtils.isBlank(str)) {
            return str;
        }
		StringBuilder sb = new StringBuilder(str.length());
		for(int i = 0;i<str.length();i++){
			char c = str.charAt(i);
			if(c=='_'){
				sb.append(Character.toUpperCase(str.charAt(i+1)));
				i++;
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 基本数据类型返回 true 包含(String)
	 */
	public static boolean isWrapClass(Class<?> clz) {
		try {
			return ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return clz == String.class || clz == Date.class || clz == java.sql.Date.class;
		}
	}
}
