package cn.tianhan.kit.excel.sax;


import java.util.List;

/**
 * @Author: NieAnTai
 * @Description:
 * @Date: 9:31 2018/7/27
 */
public interface IRowReader {
    /**
     * 获取每行数据
     *
     * @return
     */
    List<?> getRowList();

    /**
     * 业务逻辑实现类
     *
     * @param info
     */
    void dealRows(WorksheetInfo info);
}
