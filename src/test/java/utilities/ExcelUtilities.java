package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelUtilities {
	static String filePath="./src/test/resources/Data/IngredientsAndComorbidities-ScrapperHackathon.xlsx";
	static String sheetName="Final list for LFV Elimination ";
	public static List<String> LFVEliminate=new ArrayList<>();
	public static List<String> LFVAdd=new ArrayList<>();
	
	public static void readExcel() throws IOException {
		

        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(sheetName);

        int rows = sheet.getLastRowNum();
        for (int i = 2; i <= rows; i++) {
            Row row = sheet.getRow(i);

            //get eliminate list for LFV
            Cell cell = row.getCell(0);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = cell.getStringCellValue();
                
                
                LFVEliminate.add(value.trim().toUpperCase());
                

            }

            //get "to add" data from LFV
            cell = row.getCell(1);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = cell.getStringCellValue();
                
                	LFVAdd.add(value.trim().toUpperCase());
                

            }

        workbook.close();
        file.close();

        
    }
		
	}
	
	

}
