package com.bblanqiu.pay;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bblanqiu.common.mysql.model.BasketBall;


public class PayTest {
	public static void main(String []args)throws Exception{
		InputStream is = new FileInputStream("C:\\Users\\Lenovo\\Desktop\\deviceOrbasketball.xls");
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet devicebook = workbook.getSheetAt(0);
		if(devicebook != null){
			int total = devicebook.getLastRowNum();
			if(total > 0 ){
				for(int num = 1; num <= total; num ++){
					HSSFRow row = devicebook.getRow(num);
					String ballSn = row.getCell(0).getStringCellValue();
					if(ballSn != null && ballSn.length() == 8){
						System.out.println(ballSn);
					}
				}
			}
		}
		workbook.close();
	}
}
