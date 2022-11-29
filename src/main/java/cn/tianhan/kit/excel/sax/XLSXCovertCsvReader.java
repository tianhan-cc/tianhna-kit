package cn.tianhan.kit.excel.sax;


import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: NieAnTai
 * @Description: 读取超大Excel表格工具类
 * @Date: 9:58 2018/7/27
 */
public class XLSXCovertCsvReader {
    private OPCPackage opcPackage;

    /**
     * 业务逻辑实现类
     */
    private IRowReader rowReader;

    public XLSXCovertCsvReader(OPCPackage opcPackage) {
        this.opcPackage = opcPackage;
    }

    public void process() throws IOException, OpenXML4JException,
            ParserConfigurationException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.opcPackage);
        XSSFReader xssfReader = new XSSFReader(this.opcPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        // 默认只读取第一张表头
        while (iter.hasNext() && index < 1) {
            index++;
            InputStream stream = iter.next();
            String sheetName = iter.getSheetName();
            processSheet(styles, sheetName, strings, stream);
        }
    }

    private void processSheet(StylesTable styles, String sheetName,
                              ReadOnlySharedStringsTable strings, InputStream sheetInputStream)
            throws IOException, ParserConfigurationException, SAXException {

        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        CustomSheetHandler myHandler = new CustomSheetHandler(styles, strings, this.getRowReader(), sheetName);
        sheetParser.setContentHandler(myHandler);
        sheetParser.parse(sheetSource);
    }

    public IRowReader getRowReader() {
        if (this.rowReader == null) {
            throw new NullPointerException("IRowReader为NULL");
        }
        return this.rowReader;
    }

    public void setRowReader(IRowReader rowReader) {
        this.rowReader = rowReader;
    }
}
