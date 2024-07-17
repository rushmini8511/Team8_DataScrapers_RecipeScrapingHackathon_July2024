package utilities;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class ExcelUtilities {
	private FileOutputStream fos;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	//private String filePath="./src/test/resources/Data/ScrappedRecipes.xlsx";
	private String filePath;
	
	
	public ExcelUtilities(String filePath) throws IOException {
        this.filePath = filePath;
        //fos = new FileOutputStream(filePath);
        workbook = new XSSFWorkbook();
       // createFileSheet(sheetName);
    }
	
	
    public void createFileSheet(String sheetName) throws IOException {
    	
    	 XSSFSheet sheet = workbook.getSheet(sheetName);
         if (sheet == null) {
             sheet = workbook.createSheet(sheetName);
             sheet.setDefaultColumnWidth(20);
//             String[] headers = new String[]{
//                     "Recipe Name",
//                     "Ingredients",
//                     "Recipe Description",
//                     "Recipe URL"
//             };
             String[] headers = new String[] {
                   "Recipe Id",
                   "Recipe Name",
                   "Recipe Category(Breakfast/lunch/snack/dinner)",
                   "Food Category(Veg/non-veg/vegan/Jain)",
                   "Ingredients",
                   "Prepration Time",
                   "Cooking Time",
                   "Tag",
                   "No of servings",
                   "Cuisine category",
                   "Recipe Description",
                   "Preparation method",
                   "Nutrient values",
                   "Recipe URL"
                  
               };
//           String[] headers = new String[] {
//                   "Recipe Name",
//                   "Ingredients",
//                   "Recipe Description",
//                   "Recipe URL"
//                  
//               };
         
         Row headerRow = sheet.createRow(0);
         for (int i = 0; i < headers.length; i++) {
             headerRow.createCell(i).setCellValue(headers[i]);
         }
         }
        
    }
    
    public void WriteScrappedRecipes(String sheetName,String recipeName,List<String> ingredients,String recipeDesc,URL url) throws IOException {
    	
    		createFileSheet(sheetName);
    		 XSSFSheet sheet = workbook.getSheet(sheetName);
    	int nextRow = sheet.getLastRowNum() + 1;
        XSSFRow row = sheet.createRow(nextRow);
        row.createCell(0).setCellValue(recipeName);
        row.createCell(1).setCellValue(String.join(", ", ingredients));
        row.createCell(2).setCellValue(recipeDesc);
        row.createCell(3).setCellValue(url.toString());
       
       
    }
    public void saveAndClose() throws IOException {
    	try (FileOutputStream fileOut = new FileOutputStream("./src/test/resources/Data/ScrappedRecipes_List.xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
    
    
    public void save() throws IOException {
    	try (FileOutputStream fos = new FileOutputStream("./src/test/resources/Data/ScrappedRecipes_List.xlsx")) {
            workbook.write(fos);
        }
    }
}
    
    	 
            
         
    
     
   





