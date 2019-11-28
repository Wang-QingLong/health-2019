import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/24 9:36
 * @description: 对poi操作进行测试
 * HSSF － 提供读写Microsoft Excel
 * XLS格式档案的功能
 * XSSF － 提供读写Microsoft Excel OOXML
 * XLSX格式档案的功能
 * HWPF － 提供读写Microsoft Word DOC格式档案的功能
 * HSLF － 提供读写Microsoft PowerPoint格式档案的功能
 * HDGF － 提供读Microsoft Visio格式档案的功能
 * HPBF － 提供读Microsoft Publisher格式档案的功能
 * HSMF － 提供读Microsoft Outlook格式档案的功能
 *
 * XSSFWorkbook：工作簿
 * XSSFSheet：工作表
 * Row：行
 * Cell：单元格
 */
public class PoIReadTest {
    @Test
    public void ReadPoiTest() {
        try {
            //创建工作簿
            XSSFWorkbook workbook = new XSSFWorkbook("F:\\ordersetting_template.xlsx");
            //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
            XSSFSheet sheet = workbook.getSheetAt(0);
            //遍历工作表获得行对象
            for (Row row : sheet) {
                //遍历行对象获取单元格对象
                for (Cell cell : row) {
                    //获得单元格中的值
                    String value = cell.getStringCellValue();
                    System.out.println(value);
                }
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
