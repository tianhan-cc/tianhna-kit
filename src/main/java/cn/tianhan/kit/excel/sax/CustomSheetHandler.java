package cn.tianhan.kit.excel.sax;


import com.tianhan.cloud.common.core.utils.excel.enums.XSSFDataType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * @Author: NieAnTai
 * @Description: 自定义类
 * @Date: 13:37 2019/1/2
 */
public class CustomSheetHandler extends DefaultHandler {
    private final static Log log = LogFactory.getLog(CustomSheetHandler.class);

    private IRowReader iRowReader;
    private WorksheetInfo worksheetInfo;

    private StringBuffer value;
    private LinkedHashMap<String, Object> nowLine;

    private int thisColumn = -1;
    private String thisKey;

    private short formatIndex;
    private String formatString;

    private StylesTable stylesTable;
    private ReadOnlySharedStringsTable readOnlySharedStringsTable;
    private XSSFDataType nextDataType;
    private final DataFormatter formatter;

    public CustomSheetHandler(StylesTable styles, ReadOnlySharedStringsTable strings,
                              IRowReader iRowReader, String sheetName) {
        this.stylesTable = styles;
        this.readOnlySharedStringsTable = strings;
        this.iRowReader = iRowReader;
        this.worksheetInfo = new WorksheetInfo(sheetName);
        this.formatter = new DataFormatter();
        value = new StringBuffer();
        nowLine = new LinkedHashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        switch (name) {
            case "inlineStr":
            case "v":
                this.vAndInlineStrStartElement();
                break;
            case "c":
                this.cStartElement(attributes);
                break;
            case "mergeCell":
                this.mergeCellStartElement(attributes);
                break;
            case "dimension":
                this.dimensionStartElement(attributes);
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        switch (name) {
            case "v":
                this.vEndElement();
                break;
            case "row":
                this.rowEndElement();
                break;
            case "sheetData":
                this.sheetDataElement();
                break;
            case "mergeCells":
                this.mergeCellsEndElement();
                break;
            case "worksheet":
                this.worksheetEndElement();
                break;
            default:
                break;
        }
    }

    /**
     * 处理读取的内容
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        value.append(ch, start, length);
    }

    private int nameToColumn(String name) {
        int column = -1;
        for (int i = 0; i < name.length(); ++i) {
            int c = name.charAt(i);
            column = (column + 1) * 26 + c - 'A';
        }
        return column;
    }

    /**
     * 处理 c 节点
     */
    private void cStartElement(Attributes attributes) {
        thisKey = attributes.getValue("r");
        int firstDigit = -1;
        for (int c = 0; c < thisKey.length(); c++) {
            if (Character.isDigit(thisKey.charAt(c))) {
                firstDigit = c;
                break;
            }
        }

        thisColumn = nameToColumn(thisKey.substring(0, firstDigit));

        this.nextDataType = XSSFDataType.NUMBER;
        this.formatIndex = -1;
        this.formatString = null;
        String cellType = attributes.getValue("t");
        cellType = cellType == null ? "null" : cellType;
        String cellStyleStr = attributes.getValue("s");
        switch (cellType) {
            case "b":
                nextDataType = XSSFDataType.BOOL;
                break;
            case "e":
                nextDataType = XSSFDataType.ERROR;
                break;
            case "inlineStr":
                nextDataType = XSSFDataType.INLINESTR;
                break;
            case "s":
                nextDataType = XSSFDataType.SSTINDEX;
                break;
            case "str":
                nextDataType = XSSFDataType.FORMULA;
                break;
            default:
                if (cellStyleStr != null) {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null) {
                        this.formatString = BuiltinFormats
                                .getBuiltinFormat(this.formatIndex);
                    }
                }
                break;
        }
    }

    /**
     * 处理 v 与 inline 节点
     */
    private void vAndInlineStrStartElement() {
        // 重置value,开始读取数据
        value.setLength(0);
    }

    /**
     * v 节点结束时记录数据
     */
    private void vEndElement() {
        Object val = null;
        switch (nextDataType) {
            case BOOL:
                char first = this.value.charAt(0);
                val = (first == '0');
                break;
            case ERROR:
                val = "ERROR:" + this.value.toString();
                break;
            case FORMULA:
                val = this.value.toString();
                break;
            case INLINESTR:
                XSSFRichTextString richTextString = new XSSFRichTextString(this.value.toString());
                val = richTextString.toString();
                break;
            case SSTINDEX:
                String sstIndex = this.value.toString();
                try {
                    int idx = Integer.parseInt(sstIndex);
                    XSSFRichTextString rtss = new XSSFRichTextString(
                            readOnlySharedStringsTable.getEntryAt(idx));
                    val = rtss.toString();
                } catch (NumberFormatException ex) {
                    log.error("Failed to parse SST index '" + sstIndex
                            + "': " + ex.toString());
                }
                break;
            case NUMBER:
                String n = this.value.toString();
                // 判断是否是日期格式
                if (HSSFDateUtil.isADateFormat(this.formatIndex, n)) {
                    Double d = Double.parseDouble(n);
                    val = HSSFDateUtil.getJavaDate(d);
                } else if (null != this.formatString) {
                    val = this.formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
                } else {
                    val = n;
                }
                break;
            default:
                val = "(TODO: Unexpected type: " + nextDataType + ")";
                break;
        }

        nowLine.put(thisKey, val);
    }

    /**
     * 处理 MergeCell 节点
     */
    private void mergeCellStartElement(Attributes attributes) {
        this.worksheetInfo.addMergeCells(attributes.getValue("ref"));
    }

    /**
     * 处理 MergeCells 结束节点
     */
    private void mergeCellsEndElement() {
        this.worksheetInfo.setCheckMergeCell(true);
        List<String> refs = this.worksheetInfo.getMergeCells();
        for (int i = 0; i < refs.size(); i++) {
            String t = refs.get(i);

            int r = t.indexOf(":");
            String beforeStr = t.substring(0, r);
            int beforeRow = Integer.parseInt(beforeStr.replaceAll("[a-zA-Z]", ""));
            if (beforeRow == 1) {
                String afterStr = t.substring(r + 1, t.length());
                if (beforeStr.replaceAll("[0-9]", "").equals(afterStr.replaceAll("[0-9]", ""))) {
                    int afterRow = Integer.parseInt(afterStr.replaceAll("[a-zA-Z]", ""));
                    this.worksheetInfo.setMergeCell(afterRow);
                    break;
                }
            }
        }
    }

    /**
     * dimension 节点处理 获取列与行的范围
     */
    private void dimensionStartElement(Attributes attributes) {
        String ref = attributes.getValue("ref");
        int i;
        if (ref != null && (i = ref.indexOf(":")) != -1) {
            String beforeStr = ref.substring(0, i);
            String afterStr = ref.substring(i + 1, ref.length());

            StringBuffer rowStr = new StringBuffer();
            StringBuffer columnStr = new StringBuffer();
            int rowX, rowY, columnZ, columnW;
            for (int b = 0; b < beforeStr.length(); b++) {
                char tb = beforeStr.charAt(b);
                if (Character.isDigit(tb)) {
                    rowStr.append(tb);
                } else {
                    columnStr.append(tb);
                }
            }
            rowX = Integer.parseInt(rowStr.toString());
            columnZ = nameToColumn(columnStr.toString());

            rowStr.setLength(0);
            columnStr.setLength(0);
            for (int a = 0; a < afterStr.length(); a++) {
                char ta = afterStr.charAt(a);
                if (Character.isDigit(ta)) {
                    rowStr.append(ta);
                } else {
                    columnStr.append(ta);
                }
            }
            rowY = Integer.parseInt(rowStr.toString());
            columnW = nameToColumn(columnStr.toString());

            this.worksheetInfo.setRowNum((rowY - rowX) + 1);
            this.worksheetInfo.setColumnNum((columnW - columnZ) + 1);
        }
    }

    /**
     * row 节点结束时记录数据
     */
    @SuppressWarnings("unchecked")
    private void rowEndElement() {
        if (!nowLine.isEmpty()) {
            this.worksheetInfo.addRows((LinkedHashMap<String, Object>) nowLine.clone());
            nowLine.clear();
        }
    }

    private void sheetDataElement() {
        nowLine.clear();
    }

    private void worksheetEndElement() {
        this.iRowReader.dealRows(this.worksheetInfo);
    }
}
