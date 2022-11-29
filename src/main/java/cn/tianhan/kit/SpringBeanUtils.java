package cn.tianhan.kit;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author NieAnTai
 * @Date 2021/3/28 12:02 上午
 * @Version 1.0
 * @Email nieat@foxmail.com
 * @Description 获取SpringBean 工具
 **/
public class SpringBeanUtils implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (CONTEXT == null) {
            CONTEXT = applicationContext;
        }
    }

    public static <T> T getBean(Class<T> zClass) {
        return CONTEXT.getBean(zClass);
    }

    public static <T> T getBean(String name) {
        return (T) CONTEXT.getBean(name);
    }
}
