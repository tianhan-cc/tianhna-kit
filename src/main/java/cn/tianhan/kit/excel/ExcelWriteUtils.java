package cn.tianhan.kit.excel;

import com.tianhan.cloud.common.core.utils.excel.write.GeneralExcel;
import com.tianhan.cloud.common.core.utils.excel.write.IWrite;
import com.tianhan.cloud.common.core.utils.excel.write.MultipleSheetExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

/**
 * @Author: NieAnTai
 * @Description: Excel表格工具类
 * @Date: 19:25 2019/4/24
 */
public class ExcelWriteUtils {

    /**
     * HTTP Excel 下载头
     */
    private static final String EXCEL = "application/vnd.ms-excel";

    /**
     * 导出方法
     *
     * @param response HTTP 响应流
     * @param fileName 文件名字
     * @param data     导出数据
     * @param zClass   数据对象
     */
    public static void exportExcel(HttpServletResponse response, String fileName, List<?> data, Class zClass) {
        try {
            response.setContentType(EXCEL);
            // 公开Content-Disposition头信息
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + Base64.getEncoder().encodeToString(fileName.getBytes()));
            write(response.getOutputStream(), data, zClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("HttpServletResponse 输出流读取失败");
        }
    }

    public static void exportMultipleSheetExcel(HttpServletResponse response, String fileName, MultipleSheetExcel write) {
        try {
            response.setContentType(EXCEL);
            // 公开Content-Disposition头信息
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.addHeader("Content-disposition",
                    "attachment;filename=" + Base64.getEncoder().encodeToString(fileName.getBytes()));
            write(response.getOutputStream(), write);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("HttpServletResponse 输出流读取失败");
        }
    }

    public static void write(OutputStream out, List<?> data, Class zClass) {
        write(out, new GeneralExcel(data, zClass));
    }

    /**
     * @param out    输出流
     * @param iWrite IWrite 接口实现类
     * @throws Exception 异常
     */
    public static void write(OutputStream out, IWrite iWrite) {
        try {
            iWrite.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Excel导出失败!");
        }
    }
}
