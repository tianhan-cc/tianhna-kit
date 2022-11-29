package cn.tianhan.kit.exceptions;


/**
 * @Author: NieAnTai
 * @Description: 自定义类型转换异常类
 * @Date: 16:30 2019/1/3
 */
public class ConvertTypeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ConvertTypeException() {
        super();
    }

    public ConvertTypeException(String message) {
        super(message);
    }
}
