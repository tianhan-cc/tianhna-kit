package cn.tianhan.kit;

/**
 * @Author NieAnTai
 * @Date 2022/10/18 10:42
 * @Version 1.0
 * @Email nieat@foxmail.com
 * @Description
 **/
public final class ThreadUtil {
    public static void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (Exception ignore) {
        }
    }
}
