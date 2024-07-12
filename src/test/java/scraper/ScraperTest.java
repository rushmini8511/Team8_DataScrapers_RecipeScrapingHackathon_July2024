package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.BrowserVersion;
import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;

import reusableMethods.ReusableMethods;
import utilities.ExcelUtilities;

public class ScraperTest {
	
	
	@Test
	public void test2() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		ReusableMethods checkIng = new ReusableMethods();
		//initialize the browser
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		
		//configuring options
		webClient.getOptions().setUseInsecureSSL(true);
		
		webClient.getOptions().setCssEnabled(false);
		
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		
		webClient.getOptions().setJavaScriptEnabled(false);
		
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		
		//Fetching WebPage
		HtmlPage page = webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx");

		// List<HtmlAnchor> recipeLinks = page.getByXPath("//div[@class='rcc_rcpcore']/a[@href='a-checkerboard-of-roses--flower-arrangements-37154r']");
        
        List<HtmlAnchor> recipeLinks = page.getByXPath("//div[@class='rcc_recipecard']//span[@class='rcc_recipename']/a");
       
        
        
        for (HtmlAnchor recipeLink : recipeLinks) {
            HtmlPage recipePage = recipeLink.click();
           webClient.waitForBackgroundJavaScript(3000);
            String title = recipePage.getTitleText();
            System.out.println("Recipe title: " + title);
            
            
            int count = 0;
          List<HtmlElement>  ingredients=recipePage.getByXPath("//span[@itemprop='recipeIngredient']/a/span");
          List<String> ingredientsList=new ArrayList<>();
          List<HtmlElement> recipeDescPath=recipePage.getByXPath("//span[@itemprop='description']");
            List<String> recipeDescList=new ArrayList<>();
          if (ingredients.isEmpty()) {
                System.out.println("No ingredients found for: " + title);
            } else {
                System.out.println("Ingredients:");
                for (HtmlElement ingredientElement : ingredients) {
                    
                	
                    ingredientsList.add(ingredientElement.getTextContent().trim());
                    if(checkIng.checkForEliminatedIng(ingredientsList, "LFV Elimination List") != true) {
                    	System.out.println("*************No Elimination List Ingredients****************");
                    	
                    	
                    	   for (HtmlElement recipeDescription : recipeDescPath) {
                           	recipeDescList.add(recipeDescription.getTextContent().trim());
                          
                     
                   System.out.println(ingredientsList);
//                   for (String ingredient : ingredientsList) {
//                       System.out.println(ingredient);
//                   }
//                   for (String recipeDesc : recipeDescList) {
//                       System.out.println(recipeDesc);
//                   }
                 	   }
                    }
                   else
                   {
                   	System.out.println("***********RECIPE NOT SCRAPED********");
                   	System.out.println("Ingredient eliminated: " +ingredientsList);
                   	break;
                    }
                    
                    
                    }
                    
                }
            }
        }
    
	
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

