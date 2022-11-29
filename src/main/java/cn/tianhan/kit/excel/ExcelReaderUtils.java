package cn.tianhan.kit.excel;


import cn.tianhan.kit.ConvertMap;
import cn.tianhan.kit.excel.sax.*;
import org.apache.poi.openxml4j.opc.OPCPackage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * @Author: NieAnTai
 * @Description: Excel表格工具类
 * @Date: 9:28 2018/7/27
 */
public class ExcelReaderUtils {
    public static List<ConvertMap> execReader(String filePath) throws Exception {
        return execReader(filePath, Files.newInputStream(Paths.get(filePath)));
    }

    public static List<ConvertMap> execReader(String fileName, InputStream in) throws Exception {
        return execReader(fileName, in, new GeneralRowReader());
    }

    public static List<ConvertMap> complexReader(InputStream in) throws Exception {
        return execReader("t.xlsx", in, new ComplexRowReader());
    }

    /**
     * @param filePath 文件存放路径
     * @param ir       业务逻辑接口类
     */
    public static <T> List<T> execReader(String filePath, IRowReader ir) throws Exception {
        return execReader(filePath, Files.newInputStream(Paths.get(filePath)), ir);
    }

    public static <T> List<T> execReader(InputStream in, Class<T> zClass) throws Exception {
        return execReader("ann.xlsx", in, new GeneralAnnReader<T>(zClass));
    }

    /**
     * @param fileName 文件名称
     * @param in
     * @param ir
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> execReader(String fileName, InputStream in, IRowReader ir) throws Exception {
        if (!fileName.endsWith(".xlsx")) {
            throw new Exception("当前不支持Excel2003文件类型");
        }
        OPCPackage opc = OPCPackage.open(in);
        XLSXCovertCsvReader xlsxTocsv = new XLSXCovertCsvReader(opc);
        xlsxTocsv.setRowReader(ir);
        xlsxTocsv.process();

        return (List<T>) ir.getRowList();
    }
}
