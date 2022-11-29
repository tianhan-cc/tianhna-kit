package cn.tianhan.kit.excel.sax;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author: NieAnTai
 * @Description: Excel表格工作页信息
 * @Date: 16:29 2019/1/4
 */
public class WorksheetInfo {
    private String sheetName;
    private int rowNum;
    private int columnNum;
    private final LinkedList<Map<String, Object>> rows;

    /**
     * 是否有合并单元格
     */
    private boolean checkMergeCell = false;
    private List<String> mergeCells;
    private int mergeCell;

    public WorksheetInfo(String sheetName) {
        this.sheetName = sheetName;
        rows = new LinkedList<>();
        mergeCells = new ArrayList<>();
    }

    public LinkedList<Map<String, Object>> getRows() {
        return rows;
    }

    public void addRows(Map<String, Object> r) {
        rows.add(r);
    }

    public boolean isCheckMergeCell() {
        return checkMergeCell;
    }

    public void setCheckMergeCell(boolean checkMergeCell) {
        this.checkMergeCell = checkMergeCell;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void addMergeCells(String c) {
        this.mergeCells.add(c);
    }

    public List<String> getMergeCells() {
        return mergeCells;
    }

    public int getMergeCell() {
        return mergeCell;
    }

    public void setMergeCell(int mergeCell) {
        this.mergeCell = mergeCell;
    }
}
