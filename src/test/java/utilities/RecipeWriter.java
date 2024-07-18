package utilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RecipeWriter {
	private FileOutputStream fos;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	private String filePath;

	public RecipeWriter(String filePath) throws IOException {
		this.filePath = filePath;

		workbook = new XSSFWorkbook();

	}

	public void createFileSheet(String sheetName) throws IOException {

		XSSFSheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			sheet = workbook.createSheet(sheetName);
			sheet.setDefaultColumnWidth(20);

			String[] headers = new String[] { "Recipe Id", "Recipe Name",
					"Recipe Category(Breakfast/lunch/snack/dinner)", "Food Category(Veg/non-veg/vegan/Jain)",
					"Ingredients", "Prepration Time", "Cooking Time", "Tag", "No of servings", "Cuisine category",
					"Recipe Description", "Preparation method", "Nutrient values", "Recipe URL"

			};

			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				headerRow.createCell(i).setCellValue(headers[i]);
			}
		}

	}

	public void WriteScrappedRecipes(String sheetName, String recipeID, String recipeName, String recipeCategory,
			String foodCategory, List<String> ingredients, String preprationTime, String cookingTime, String Tag,
			String servings, String cuisineCategory, String recipeDesc, List<String> preparationMethod,
			List<String> nutrientValues, URL url) throws IOException {

		createFileSheet(sheetName);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		int nextRow = sheet.getLastRowNum() + 1;
		XSSFRow row = sheet.createRow(nextRow);
		row.createCell(0).setCellValue(recipeID);
		row.createCell(1).setCellValue(recipeName);
		row.createCell(2).setCellValue(recipeCategory == null || recipeCategory.isEmpty() ? "NA" : recipeCategory);
		row.createCell(3).setCellValue(foodCategory == null || foodCategory.isEmpty() ? "NA" : foodCategory);
		row.createCell(4)
				.setCellValue(ingredients == null || ingredients.isEmpty() ? "NA" : String.join(", ", ingredients));
		row.createCell(5).setCellValue(preprationTime == null || preprationTime.isEmpty() ? "NA" : preprationTime);
		row.createCell(6).setCellValue(cookingTime == null || cookingTime.isEmpty() ? "NA" : cookingTime);
		row.createCell(7).setCellValue(Tag == null || Tag.isEmpty() ? "NA" : Tag);
		row.createCell(8).setCellValue(servings == null || servings.isEmpty() ? "NA" : servings);
		row.createCell(9).setCellValue(cuisineCategory == null || cuisineCategory.isEmpty() ? "NA" : cuisineCategory);
		row.createCell(10).setCellValue(recipeDesc);
		row.createCell(11).setCellValue(preparationMethod == null || preparationMethod.isEmpty() ? "NA"
				: String.join("\n--> ", preparationMethod));
		row.createCell(12).setCellValue(
				nutrientValues == null || nutrientValues.isEmpty() ? "NA" : String.join("\n", nutrientValues));
		row.createCell(13).setCellValue(url.toString());

	}

	public void saveAndClose() throws IOException {
		try (FileOutputStream fileOut = new FileOutputStream("./src/test/resources/Data/ScrappedRecipes.xlsx")) {
			workbook.write(fileOut);
		}
		workbook.close();
	}

	public void save() throws IOException {
		try (FileOutputStream fos = new FileOutputStream("./src/test/resources/Data/ScrappedRecipes.xlsx")) {
			workbook.write(fos);
		}
	}
}
