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
	//static String sheetName="Final list for LFV Elimination ";
	public static List<String> LFVEliminate=new ArrayList<>();
	public static List<String> LFVAdd=new ArrayList<>();
	public static List<String> LFVAdd_Nonvegan=new ArrayList<>();
	public static List<String> LCHFEliminate=new ArrayList<>();
	public static List<String> LCHFAdd=new ArrayList<>();
	
	public static void readExcel() throws IOException {
		

        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        //XSSFSheet sheet = workbook.getSheet(sheetName);
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int s = 1; s < numberOfSheets; s++) {
            XSSFSheet sheet = workbook.getSheetAt(s);
            processSheet(sheet, s);
        }
//        int rows = sheet.getLastRowNum();
//        for (int i = 2; i <= rows; i++) {
//            Row row = sheet.getRow(i);
//
//            //get eliminate list for LFV
//            Cell cell = row.getCell(0);
//            if (cell != null && cell.getCellType() != CellType.BLANK) {
//                String value = cell.getStringCellValue();
//                
//                
//                LFVEliminate.add(value.trim().toUpperCase());
//                
//
//            }
//
//            //get "to add" data from LFV
//            cell = row.getCell(1);
//            if (cell != null && cell.getCellType() != CellType.BLANK) {
//                String value = cell.getStringCellValue();
//                
//                	LFVAdd.add(value.trim().toUpperCase());
//                
//
//            }
//          //get "non vegan" data from LFV
//            cell = row.getCell(2);
//            if (cell != null && cell.getCellType() != CellType.BLANK) {
//                String value = cell.getStringCellValue();
//                
//                	LFVAdd_Nonvegan.add(value.trim().toUpperCase());
//                
//
//            }

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

	        } 
	 }
	

}
//	 public static void main(String[] args) throws IOException {
//		 readExcel();
//		 System.out.println("LFVEliminate: " + LFVEliminate);
//         System.out.println("LFVAdd: " + LFVAdd);
//         System.out.println("LFVAdd_Nonvegan: " + LFVAdd_Nonvegan);
//         System.out.println("LCHFEliminate: " + LCHFEliminate);
//         System.out.println("LCHFAdd: " + LCHFAdd);
//	}
	 }
/*package utilities;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtilities {
	public FileInputStream fi;
	public FileOutputStream fo;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row;
	public XSSFCell cell;
	public CellStyle style;   
	String path;
	

	
	public ExcelUtilities(String path)
	{
		this.path=path;
	}


	public String getCellData(String sheetName,int rownum,int colnum) throws IOException
	{
		fi=new FileInputStream(path);
		workbook=new XSSFWorkbook(fi);
		sheet=workbook.getSheet(sheetName);
		row=sheet.getRow(rownum);
		cell=row.getCell(colnum);

		DataFormatter formatter = new DataFormatter();
		String data;
		try{
			data = formatter.formatCellValue(cell); //Returns the formatted value of a cell as a String regardless of the cell type.
		}
		catch(Exception e)
		{
			data="";
		}
		workbook.close();
		fi.close();
		return data;
	}

	public void setCellData(String sheetName,int rownum,int colnum,String data) throws IOException
	{
		File xlfile=new File(path);
		if(!xlfile.exists())    // If file not exists then create new file
		{
			workbook=new XSSFWorkbook();
			fo=new FileOutputStream(path);
			workbook.write(fo);
		}

		fi=new FileInputStream(path);
		workbook=new XSSFWorkbook(fi);

		if(workbook.getSheetIndex(sheetName)==-1) // If sheet not exists then create new Sheet
			workbook.createSheet(sheetName);

		sheet=workbook.getSheet(sheetName);

		if(sheet.getRow(rownum)==null)   // If row not exists then create new Row
			sheet.createRow(rownum);
		row=sheet.getRow(rownum);

		cell=row.createCell(colnum);
		cell.setCellValue(data);

		fo=new FileOutputStream(path);
		workbook.write(fo);		
		workbook.close();
		fi.close();
		fo.close();
	}		
	
	public List<String> getEliminatedList(String filePath, int colunum, String sheetname) {
	
		List<String> eliminatedList = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			workbook = new XSSFWorkbook(fis);
			
			XSSFSheet sheet = workbook.getSheet(sheetname);
			
			Iterator<Row> rowiterator = sheet.rowIterator(); 
			while (rowiterator.hasNext()) {
				Row row = rowiterator.next();
				Cell cell = row.getCell(colunum);
				if (cell != null) {
					eliminatedList.add(cell.getStringCellValue());
                    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return eliminatedList;
		
	}
}*/