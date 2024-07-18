package utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToDataBase_PostgreSQL {
	
	public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
		
	
	String url = "jdbc:postgresql://localhost:5432/Team8_DataScrapers_DataBase";
    String user = "postgres";
    String password = "RatPraChan@2504";

    try (Connection con = DriverManager.getConnection(url, user, password);
         FileInputStream fis = new FileInputStream("D:\\Rathna\\Team8_DataScrapers_RecipeScrapingHackathonJuly2024\\Team8_DataScraper_Allergies.xlsx");
         XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

        createTable(con, "Allergies_SOY");
        insertData(con, workbook.getSheet("Allergies_SOY"), "Allergies_SOY");
        
        createTable(con, "Allergies_EGG");
        insertData(con, workbook.getSheet("Allergies_EGG"), "Allergies_EGG");
        
        createTable(con, "Allergies_MILK");
        insertData(con, workbook.getSheet("Allergies_MILK"), "Allergies_MILK");
        
        createTable(con, "Allergies_SESAME");
        insertData(con, workbook.getSheet("Allergies_SESAME"), "Allergies_SESAME");
        
        createTable(con, "Allergies_Walnut");
        insertData(con, workbook.getSheet("Allergies_SESAME"), "Allergies_Walnut");
        createTable(con, "Allergies_Almond");
        insertData(con, workbook.getSheet("Allergies_Almond"), "Allergies_Almond");
        
        createTable(con, "Allergies_Hazelnut");
        insertData(con, workbook.getSheet("Allergies_Hazelnut"), "Allergies_Hazelnut");
        
        createTable(con, "Allergies_Pecan");
        insertData(con, workbook.getSheet("Allergies_Pecan"), "Allergies_Pecan");
        
        createTable(con, "Allergies_Cashew");
        insertData(con, workbook.getSheet("Allergies_Cashew"), "Allergies_Cashew");
        
        createTable(con, "Allergies_Pistachio");
        insertData(con, workbook.getSheet("Allergies_Pistachio"), "Allergies_Pistachio");
        
        createTable(con, "Allergies_Shellfish");
        insertData(con, workbook.getSheet("Allergies_Shellfish"), "Allergies_Shellfish");
        
        createTable(con, "Allergies_Seafood");
        insertData(con, workbook.getSheet("Allergies_Seafood"), "Allergies_Seafood");
        
        createTable(con, "Allergies_PEANUT");
        insertData(con, workbook.getSheet("Allergies_PEANUT"), "Allergies_PEANUT");
    	
        createTable(con, "LFV_Elimination");
        insertData(con, workbook.getSheet("LFV-Elimination"), "LFV_Elimination");

        createTable(con, "LFV_Add");
        insertData(con, workbook.getSheet("LFV-Add"), "LFV_Add");

        createTable(con, "LCHF_Elimination");
        insertData(con, workbook.getSheet("LCHF-Elimination"), "LCHF_Elimination");

        createTable(con, "LFV_Recipes_To_Avoid");
        insertData(con, workbook.getSheet("LFV-Recipes-To-Avoid"), "LFV_Recipes_To_Avoid");

        createTable(con, "LCHF_Food_Processing");
        insertData(con, workbook.getSheet("LCHF-Food-Processing"), "LCHF_Food_Processing");

        createTable(con, "LFV_Add_Non_VEGAN");
        insertData(con, workbook.getSheet("LFV-Add-non-VEGAN"), "LFV_Add_Non_VEGAN");

        createTable(con, "LCHF_Add");
        insertData(con, workbook.getSheet("LCHF-Add"), "LCHF_Add");
    }
}

private static void createTable(Connection con, String tableName) throws SQLException {
    String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
            "Recipe_Id VARCHAR," +
            "Recipe_Name VARCHAR," +
            "Recipe_Category VARCHAR," +
            "Food_Category VARCHAR," +
            "Ingredients VARCHAR," +
            "Preperation_Time VARCHAR," +
            "Cooking_Time VARCHAR," +
            "Tag VARCHAR," +
            "No_Of_Servings VARCHAR," +
            "Cuisine_Category VARCHAR," +
            "Recipe_Desc VARCHAR," +
            "Preperation_Method VARCHAR," +
            "Nutrient_Values VARCHAR," +
            "Recipe_URL VARCHAR)";
    try (Statement stm = con.createStatement()) {
        stm.execute(sql);
    }
}

private static void insertData(Connection con, XSSFSheet sheet, String tableName) throws SQLException {
    String sql = "INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        int rows = sheet.getLastRowNum();
        for (int r = 1; r <= rows; r++) {  // Ensure <= to include last row
            XSSFRow row = sheet.getRow(r);
            if (row == null) continue; // Skip null rows
            

            pst.setString(1, row.getCell(0).getStringCellValue());
            pst.setString(2, row.getCell(1).getStringCellValue());
            pst.setString(3, row.getCell(2).getStringCellValue());
            pst.setString(4, row.getCell(3).getStringCellValue());
            pst.setString(5, row.getCell(4).getStringCellValue());
            pst.setString(6, row.getCell(5).getStringCellValue());
            pst.setString(7, row.getCell(6).getStringCellValue());
            pst.setString(8, row.getCell(7).getStringCellValue());
            String cellValue = (row.getCell(8) == null || row.getCell(8).getCellType() == CellType.BLANK) ? "NA" : row.getCell(8).getStringCellValue();
            pst.setString(9, cellValue);
            String cellValue1 = (row.getCell(9) == null || row.getCell(9).getCellType() == CellType.BLANK) ? "NA" : row.getCell(9).getStringCellValue();
            pst.setString(10, cellValue1);
            String cellValue3 = (row.getCell(10) == null || row.getCell(10).getCellType() == CellType.BLANK) ? "NA" : row.getCell(10).getStringCellValue();
            pst.setString(11, cellValue3);
            String cellValue4 = (row.getCell(11) == null || row.getCell(11).getCellType() == CellType.BLANK) ? "NA" : row.getCell(11).getStringCellValue();
            pst.setString(12, cellValue4);
            String cellValue5 = (row.getCell(12) == null || row.getCell(12).getCellType() == CellType.BLANK) ? "NA" : row.getCell(12).getStringCellValue();
            pst.setString(13, cellValue5);
            String cellValue6 = (row.getCell(13) == null || row.getCell(13).getCellType() == CellType.BLANK) ? "NA" : row.getCell(13).getStringCellValue();
            pst.setString(14, cellValue6);

            pst.addBatch();

            if (r % 100 == 0 || r == rows) {
                pst.executeBatch(); // Execute every 100 rows or at the end
            }
        }
    }
}


}
