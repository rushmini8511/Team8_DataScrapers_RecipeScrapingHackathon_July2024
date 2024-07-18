package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelUtilities {
	static String filePath="./src/test/resources/Data/IngredientsAndComorbidities-ScrapperHackathon.xlsx";
	
	public static List<String> LFVEliminate=new ArrayList<>();
	public static List<String> LFVAdd=new ArrayList<>();
	public static List<String> LFVAdd_Nonvegan=new ArrayList<>();
	public static List<String> LCHFEliminate=new ArrayList<>();
	public static List<String> LCHFAdd=new ArrayList<>();
	public static List <String> LFV_RecipesToAvoid =  new ArrayList<>();
	public static List <String> LFV_OptionalRecipe = new ArrayList<>();
	public static List<String> LCHF_RecipesToAvoid = new ArrayList<>();
	public static List<String> LCHF_FoodProcessing = new ArrayList<>();
	public static  List<String> Allergies_List = new ArrayList<String>();
	
	public static void readExcel() throws IOException {
		

        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int s = 1; s < numberOfSheets; s++) {
            XSSFSheet sheet = workbook.getSheetAt(s);
            processSheet(sheet, s);
        }
     

        workbook.close();
        file.close();

        
    }
		
	
	 private static void processSheet(XSSFSheet sheet,int sheetIndex) {
	        int rows = sheet.getLastRowNum();
	        if(sheetIndex==1) {
	        for (int i = 2; i <= rows; i++) {
	            Row row = sheet.getRow(i);

	            // get eliminate list for LFV
	            Cell cell = row.getCell(0);
	            if (cell != null && cell.getCellType() != CellType.BLANK) {
	                String value = cell.getStringCellValue();
	                LFVEliminate.add(value.trim().toUpperCase());
	            }

	            // get "to add" data from LFV
	            cell = row.getCell(1);
	            if (cell != null && cell.getCellType() != CellType.BLANK) {
	                String value = cell.getStringCellValue();
	                LFVAdd.add(value.trim().toUpperCase());
	            }

	            // get "non vegan" data from LFV
	            cell = row.getCell(2);
	            if (cell != null && cell.getCellType() != CellType.BLANK) {
	                String value = cell.getStringCellValue();
	                LFVAdd_Nonvegan.add(value.trim().toUpperCase());
	            }
	            cell = row.getCell(3);
	            if (cell != null && cell.getCellType() != CellType.BLANK) {
	                String value = cell.getStringCellValue();
	                LFV_RecipesToAvoid.add(value.trim().toUpperCase());
	            }
	            cell = row.getCell(4);
	            if (cell != null && cell.getCellType() != CellType.BLANK) {
	                String value = cell.getStringCellValue();
	                LFV_OptionalRecipe.add(value.trim().toUpperCase());
	            }
	        }
	    }
	        else if (sheetIndex==2) {
	        	for (int i = 2; i <= rows; i++) {
		            Row row = sheet.getRow(i);

		            // get eliminate list for LCHFElimination
		            Cell cell = row.getCell(0);
		            if (cell != null && cell.getCellType() != CellType.BLANK) {
		                String value = cell.getStringCellValue();
		                LCHFEliminate.add(value.trim().toUpperCase());
		            }
		            // get add list for LCHFAdd
		            cell = row.getCell(1);
		            if (cell != null && cell.getCellType() != CellType.BLANK) {
		                String value = cell.getStringCellValue();
		                LCHFAdd.add(value.trim().toUpperCase());
		            }
		            cell = row.getCell(2);
		            if (cell != null && cell.getCellType() != CellType.BLANK) {
		                String value = cell.getStringCellValue();
		                LCHF_RecipesToAvoid.add(value.trim().toUpperCase());
		            }
		            cell = row.getCell(3);
		            if (cell != null && cell.getCellType() != CellType.BLANK) {
		                String value = cell.getStringCellValue();
		                String cellvalue = value.replaceAll("[^a-z A-Z	0-9\\s]", "").substring(2);
		                LCHF_FoodProcessing.add(cellvalue.trim().toUpperCase());

	        } 
	 }
	

}
	        else if (sheetIndex==3) {
	        	for (int i = 2; i <= rows; i++) {
		            Row row = sheet.getRow(i);

		            // get eliminate list for Allergies
		            Cell cell = row.getCell(0);
		            if (cell != null && cell.getCellType() != CellType.BLANK) {
		                String value = cell.getStringCellValue();
		               
						Allergies_List.add(value.trim().toUpperCase());
		            }
		           
	 }
	

}
	
}

}
