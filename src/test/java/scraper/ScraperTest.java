package scraper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;

import utilities.ExcelUtilities;

public class ScraperTest {

	@Test
	public void test1() throws IOException {
		
//	WebDriver driver = new ChromeDriver();
		
	String url = "https://www.tarladalal.com/RecipeAtoZ.aspx";
//	driver.manage().window().maximize();
//	driver.get(url);
	String path = ".\\recipes.xlsx";
	ExcelUtilities  xlUtil = new ExcelUtilities(path);
//	
	//Write headers in excel sheet
	xlUtil.setCellData("Sheet1", 0, 0, "Recipe ID");
	xlUtil.setCellData("Sheet1", 0, 1, "Recipe Name");
	xlUtil.setCellData("Sheet1", 0, 2, "Recipe Category");
	xlUtil.setCellData("Sheet1", 0, 3, "Tag");
	
//	WebElement recipeBlock = driver.findElement(By.xpath("//div[@id = 'block1']"));
//	int recipes = recipeBlock.findElements(By.xpath("//div[@class = 'rcc_recipecard']")).size();
//	
	
	
try {
		Document document = Jsoup.connect(url).get();
		Elements recipes = document.select(".rcc_recipecard");
		for (Element ele: recipes) {
			String recipeID = ele.select("div > span").text();
			String recipeName = ele.select(".rcc_recipename").text();
			//String recipeN = ele.select(" a").text();
			String actualRecipeID = (String) recipeID.subSequence(8, 13);
			for (int i =1; i <= 14;i++)
				
				xlUtil.setCellData("Sheet1", i, 1, recipeName);
				
				
				
				System.out.println(actualRecipeID +"recipeID");
				System.out.println(recipeName +"recipeName");
			//System.out.println(title + "-" +price);
//			String actualprice = price.substring(1);
//			if(Double.parseDouble(actualprice)<20.0) {
//				System.out.println(title+ "-" +price);
//			}
			
			//System.out.println(recipeN+ "recipeN");
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
}
