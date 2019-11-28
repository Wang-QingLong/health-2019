import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/24 10:35
 * @description: 使用POI可以在内存中创建一个Excel文件并将数据写入到这个文件，最后通过输出流将内存中的Excel文件下载到磁盘
 * <p>
 * XSSFWorkbook：工作簿
 * XSSFSheet：工作表
 * Row：行
 * Cell：单元格
 */
public class PoIWriteTest01 {
    /**
     * 指定日期递增
     *
     * @throws ParseException
     */
    @Test
    public void WriteTest() throws ParseException {
        try {
            //在内存中创建一个Excel文件
            XSSFWorkbook workbook = new XSSFWorkbook();
            //创建工作表，指定工作表名称
            XSSFSheet sheet = workbook.createSheet("Wang-Excel");

            //创建行，0表示第一行
            XSSFRow row = sheet.createRow(0);
            //创建单元格，0表示第一个单元格
            row.createCell(0).setCellValue("日期");
            row.createCell(1).setCellValue("可预约人数");

            for (int i = 1; i <= 10000; i++) {
                //创建行
                XSSFRow row1 = sheet.createRow(i);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar ca = Calendar.getInstance();
                ca.set(2010, 01, 01);
                ca.add(Calendar.DAY_OF_YEAR, i);
                Date time = ca.getTime();
                String s = format.format(time);

                //创建该行第一个单元格
                row1.createCell(0).setCellValue(s);
                //创建该行第二个单元格
                row1.createCell(1).setCellValue(300);

            }

//            XSSFRow row2 = sheet.createRow(2);
//            row2.createCell(0).setCellValue("2");
//            row2.createCell(1).setCellValue("小王");
//            row2.createCell(2).setCellValue("20");

            //通过输出流将workbook对象下载到磁盘
            FileOutputStream out = new FileOutputStream("F:\\Wang03.xlsx");
            workbook.write(out);
            out.flush();
            out.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
