package cn.tianhan.kit.exceptions;

/**
 * @Author NieAnTai
 * @Date 2022/11/29 16:44
 * @Version 1.0
 * @Email nieat@foxmail.com
 * @Description
 **/
public class BusinessException extends RuntimeException {
    private Integer code;
    private String message;

    public BusinessException(String message) {
        this.code = 500;
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
