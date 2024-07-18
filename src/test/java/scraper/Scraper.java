package scraper;
/*import java.util.ArrayList;
import java.util.List;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.testng.annotations.Test;
import java.io.IOException;
import utilities.BrowserUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
	public class Scraper extends BrowserUtilities
	{
		
		
		
		public void clickonReceipe() throws IOException, InterruptedException
		{
		        try (final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
				webClient.getOptions().setJavaScriptEnabled(false);
		            webClient.getOptions().setCssEnabled(false);
		            HtmlPage Homepage = webClient.getPage("https://www.tarladalal.com/");
		            HtmlAnchor latestPostLink
		            = (HtmlAnchor) Homepage.getByXPath("//div[@id='toplinks']/a[5]").get(0);
		            HtmlPage postPage = latestPostLink.click();
		            Thread.sleep(3000);
		          
		           
		    		HtmlPage Apage = webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=A&pageindex=1"); 	           
		           
//                  Click on A 
		         
//		            HtmlAnchor anchor = (HtmlAnchor)postPage.getFirstByXPath("//a[@href='/RecipeAtoZ.aspx?beginswith=A?pageindex=1']");
//		            HtmlPage Apage = anchor.click();
		    		
		    		List<HtmlAnchor> pages = Apage.getByXPath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a");
		    		int pagecount = pages.size()+1;
		    		logger.info("No of A Pages = "+pagecount);
		    		
		    		for (int p=1; p<=pagecount;p++)
		    			
		    		{
		    			List<HtmlAnchor> nextPageLink =  Apage.getByXPath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a[text()='"+p+"']");
		    			logger.info(nextPageLink.toString());
		    		
		    			boolean hasNextPage = true;
		    			
		    			 for (HtmlAnchor next : nextPageLink) {
		    				 HtmlPage n = next.click();
		    				 List<HtmlAnchor> recipeLinks = n.getByXPath("//div[@class='rcc_recipecard']//span[@class='rcc_recipename']/a");	               
		    		           
		 					for (HtmlAnchor recipeLink : recipeLinks) {
		 						
		 						
								  HtmlPage recipePage = recipeLink.click();
								  WebClient webClient2 = new WebClient(); 
								  
								  webClient2.waitForBackgroundJavaScript(3000);
								  String title =recipePage.getTitleText();
								 
								  logger.info("----------------------------------------");
								  logger.info("Recipe title: " + title);
								 
								 
								 
								 
						  List<HtmlElement> ingredients=recipePage.getByXPath(  "//span[@itemprop='recipeIngredient']/a/span");
						  List<String> ingredientsList=new ArrayList<>();
						  List<HtmlElement>recipeDescPath=recipePage.getByXPath("//span[@itemprop='description']");	  
								  List<String> recipeDescList=new ArrayList<>();
								  if (ingredients.isEmpty()) 
								  {
								  logger.info("No ingredients found for: " + title);
								  }
								  else
								  {
								  logger.info("Ingredients:"); 
								  for (HtmlElement ingredientElement :ingredients)
								  {
								 
								  ingredientsList.add(ingredientElement.getTextContent().trim());
					
								  } 
								  for (HtmlElement recipeDescription : recipeDescPath) 
								  {
								
								
								   recipeDescList.add(recipeDescription.getTextContent().trim());
								
								  }
								  
								  }
								
								
								 logger.info(ingredientsList);
								 for (String ingredient :  ingredientsList)
								 { 
									 logger.info(ingredient);
								 } 
								 for (String recipeDesc : recipeDescList)
								 { 
									 logger.info(recipeDesc); }
								
		 					     }
		    				
		    				
		    			 }
						
		    		
		    		}
		          
	    		}
		        catch (Exception e)
		        {
		            e.printStackTrace();
			       
		    	 }
		        
		}
	}*/

 import analyzer.MyRetryAnalyzer;
import java.util.ArrayList;
import java.util.List;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.testng.annotations.Test;
import java.io.IOException;
import utilities.BrowserUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Scraper extends BrowserUtilities
{
	private static final Logger logger = LogManager.getLogger(Scraper.class);
	
	@Test(retryAnalyzer = MyRetryAnalyzer.class)
	public void clickonReceipe() throws IOException, InterruptedException {
		try (final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setCssEnabled(false);
//		            webClient.getOptions().setThrowExceptionOnScriptError(false);
//		           HtmlPage page =  webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx")
			HtmlPage Homepage = webClient.getPage("https://www.tarladalal.com/");
			HtmlAnchor AtoZ_link = (HtmlAnchor) Homepage.getByXPath("//div[@id='toplinks']/a[5]").get(0);
			HtmlPage postPage = AtoZ_link.click();
			Thread.sleep(3000);
			// HtmlPage Mpage =
			// webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=M&pageindex=1");
			// HtmlPage Npage =
			// webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=N&pageindex=1");
			// HtmlPage Opage =
			// webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=O&pageindex=1");
			// [HtmlAnchor[<a class="rescurrpg"
			// href="/RecipeAtoZ.aspx?beginswith=A&amp;pageindex=1">]]
//                  Click on L icon
//                  HtmlAnchor anchor = (HtmlAnchor)postPage.getFirstByXPath("//a[@href='/RecipeAtoZ.aspx?beginswith=L?pageindex=1']");
//		            HtmlPage Lpage = anchor.click();
			
			List<String> PagesList = new ArrayList<String>();
			char c;
			for (c = 'A'; c <= 'Z'; ++c) {
				logger.info("This Page begins with the letter =" + c);
				PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=" + c);
			}
			// PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=A");
			// PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=M");
			// PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=N");
			// PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=O");
			
			for (int i = 0; i < PagesList.size(); i++) {
				logger.info("_______" + PagesList.get(i));
				HtmlPage page = webClient.getPage(PagesList.get(i));
				List<HtmlAnchor> pages = page.getByXPath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a");
				int pagecount = pages.size() + 1;
				logger.info("No of Pages = " + pagecount);
				for (int p = 1; p <= pagecount; p++)
				{
					List<HtmlAnchor> nextPageLink = page.getByXPath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a[text()='" + p + "']");
					logger.info(nextPageLink.toString());
					for (HtmlAnchor next : nextPageLink) {
						HtmlPage n = next.click();
						
						List<HtmlAnchor> recipeLinks = n.getByXPath("//div[@class='rcc_recipecard']//span[@class='rcc_recipename']/a");
						for (HtmlAnchor recipeLink : recipeLinks) {
							HtmlPage recipePage = recipeLink.click();
							WebClient webClient2 = new WebClient();
							webClient2.waitForBackgroundJavaScript(3000);
							String title = recipePage.getTitleText();
							logger.info("----------------------------------------");
							logger.info("Recipe title: " + title);
							List<HtmlElement> ingredients = recipePage.getByXPath("//span[@itemprop='recipeIngredient']/a/span");
							List<String> ingredientsList = new ArrayList<>();
							List<HtmlElement> recipeDescPath = recipePage.getByXPath("//span[@itemprop='description']");
							List<String> recipeDescList = new ArrayList<>();
							List<HtmlElement> Preparation = recipePage.getByXPath("//time[@itemprop='prepTime']");
							List<String> PreparationList = new ArrayList<>();
							List<HtmlElement> cooking = recipePage.getByXPath("//time[@itemprop='cookTime']");
							List<String> cookingList = new ArrayList<>();
							List<HtmlElement> cuisine = recipePage.getByXPath("//div[@id='show_breadcrumb']//span[@itemprop='itemListElement'] [4]");
							List<String> cuisineList = new ArrayList<>();
							List<HtmlElement> Fcategory = recipePage.getByXPath("//div[@class='tags']//span[@itemprop='name']");
							List<String> FcategoryList = new ArrayList<>();
							List<HtmlElement> Rcategory = recipePage.getByXPath("//*[@id='nav']");
							List<String> RcategoryList = new ArrayList<>();
							
							
							
							if (ingredients.isEmpty()) {
								logger.info("No ingredients found for: " + title);
							} else {
								logger.info("Ingredients:");
								for (HtmlElement ingredientElement : ingredients) {
									ingredientsList.add(ingredientElement.getTextContent().trim());
								}
								for (HtmlElement recipeDescription : recipeDescPath) {
									recipeDescList.add(recipeDescription.getTextContent().trim());
								}
								for (HtmlElement Preparation_Time : Preparation) {
									PreparationList.add(Preparation_Time.getTextContent().trim());
								}
								for (HtmlElement cooking_Time :cooking) {
									cookingList.add(cooking_Time.getTextContent().trim());
								}
								for (HtmlElement recipe_cuisine :cuisine) {
									cuisineList.add(recipe_cuisine.getTextContent().trim());
								}
								for (HtmlElement food_category :Fcategory) {
									FcategoryList.add(food_category.getTextContent().trim());
								}
								for (HtmlElement recipe_category :Rcategory) {
									RcategoryList.add(recipe_category.getTextContent().trim());
								}
								
								
								
							}
							//logger.info(ingredientsList);
							for (String ingredient : ingredientsList) {
								logger.info(ingredient);
							}
							for (String recipeDesc : recipeDescList) {
								logger.info("the description of the recipe is :" +recipeDesc);
							}
							for (String PreparationTime : PreparationList) {
								logger.info("The Preparationtime is :" +PreparationTime);
							}
							for (String CookingTime : cookingList) {
								logger.info("The cookingtime is : " +CookingTime);
							}
						
							for (String Cuisinecategory : cuisineList) {
								logger.info("The Cuisine category is : "+Cuisinecategory);
							
							}
							for (String foodcategory : FcategoryList) {
								logger.info("The food category is :" +foodcategory);
							
							}
							for (String Recipecategory : RcategoryList) {
								logger.info("The Recipe category is :" +Recipecategory);
							
							}
							
						}
					}
					/*
					 * while (hasNextPage) { // Process the page content here
					 * //logger.info(page.asText()); // Attempt to find the link to the next
					 * page
					 *
					 *
					 * if (nextPageLink != null) { // Click the link to the next page Lpage =
					 * nextPageLink.click(); } else { hasNextPage = false; // No more pages }
					 *
					 * }
					 */
				}
			}
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		}
	}
}  