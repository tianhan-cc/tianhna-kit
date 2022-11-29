package cn.tianhan.kit.excel.write;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: NieAnTai
 * @Description:
 * @Date: 10:28 2018/8/20
 */
public interface IWrite {
    /**
     * 生成Excle文件
     *
     * @param out
     * @throws IOException
     * @throws NullPointerException
     */
    void write(OutputStream out) throws IOException, NullPointerException;
}
