package cn.tianhan.kit.func;

import cn.tianhan.kit.ThreadUtil;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Author NieAnTai
 * @Date 2022/10/18 10:40
 * @Version 1.0
 * @Email nieat@foxmail.com
 * @Description
 **/
public final class Func {

    private static void retry(BooleanSupplier func, Runnable fail, int max, long sleep) {
        int trySize = 0;
        while (func.getAsBoolean() && trySize < max) {
            try {
                ThreadUtil.sleep(sleep);
            } catch (Exception ignore) {
            }
            trySize++;
            fail.run();
        }
    }

    private static <T> T retry(Supplier<T> func, Runnable fail, int max, long sleep) {
        int trySize = 0;
        T result = null;
        while ((result = func.get()) == null && trySize < max) {
            try {
                ThreadUtil.sleep(sleep);
            } catch (Exception ignore) {
            }
            trySize++;
            fail.run();
        }
        return result;
    }

    public static <T> void executeIsNull(T any, Runnable func) {
        if (!predicateNotNull(any)) func.run();
    }

    public static <T> void executeIfNotNull(T any, Runnable func) {
        if (predicateNotNull(any)) func.run();
    }

    public static <T> void executeIfNotNull(T val, Consumer<T> func) {
        if (predicateNotNull(val)) func.accept(val);
    }

    public static <T> void executeIfNotNull(T val, Consumer<T> func, Runnable error) {
        if (predicateNotNull(val)) func.accept(val);
        else error.run();
    }

    private static boolean predicateNotNull(Object any) {
        if (any == null) return false;
        if (any instanceof Iterable) {
            Iterable it = (Iterable) any;
            return it.iterator().hasNext();
        }
        return true;
    }
}
