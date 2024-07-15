package scraper;
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
	public class Scraper extends BrowserUtilities {
		
		
		@Test
		public void clickonReceipe() throws IOException, InterruptedException {
		        try (final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
				webClient.getOptions().setJavaScriptEnabled(false);
		            webClient.getOptions().setCssEnabled(false);
//		            webClient.getOptions().setThrowExceptionOnScriptError(false);
//		           HtmlPage page =  webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx");
		       
		            HtmlPage Homepage = webClient.getPage("https://www.tarladalal.com/");
		            HtmlAnchor latestPostLink
		            = (HtmlAnchor) Homepage.getByXPath("//div[@id='toplinks']/a[5]").get(0);
		            HtmlPage postPage = latestPostLink.click();
		            Thread.sleep(3000);
		          
		           
		    		HtmlPage Apage = webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=A&pageindex=1"); 	           
		           
//                  Click on A icon
		         
//		            HtmlAnchor anchor = (HtmlAnchor)postPage.getFirstByXPath("//a[@href='/RecipeAtoZ.aspx?beginswith=A?pageindex=1']");
//		            HtmlPage Apage = anchor.click();
		    		
		    		List<HtmlAnchor> pages = Apage.getByXPath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a");
		    		int pagecount = pages.size()+1;
		    		System.out.println("No of A Pages = "+pagecount);
		    		
		    		for (int p=1; p<=pagecount;p++)
		    			
		    		{
		    			List<HtmlAnchor> nextPageLink =  Apage.getByXPath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a[text()='"+p+"']");
		    			System.out.println(nextPageLink.toString());
		    		//	List <int> Next_page= new ArrayList<>();
		    			boolean hasNextPage = true;
		    			
		    			 for (HtmlAnchor next : nextPageLink) {
		    				 HtmlPage n = next.click();
		    				 List<HtmlAnchor> recipeLinks = n.getByXPath("//div[@class='rcc_recipecard']//span[@class='rcc_recipename']/a");	               
		    		           
		 					for (HtmlAnchor recipeLink : recipeLinks) {
		 						
		 						
								  HtmlPage recipePage = recipeLink.click();
								  WebClient webClient2 = new
								  WebClient(); webClient2.waitForBackgroundJavaScript(3000);
								  String title =recipePage.getTitleText();
								 
								  System.out.println("----------------------------------------");
								  System.out.println("Recipe title: " + title);
								 
								 
								 
								 
								  List<HtmlElement> ingredients=recipePage.getByXPath(
								  "//span[@itemprop='recipeIngredient']/a/span"); List<String>
								  ingredientsList=new ArrayList<>(); List<HtmlElement>
								  recipeDescPath=recipePage.getByXPath("//span[@itemprop='description']");
								  List<String> recipeDescList=new ArrayList<>(); if (ingredients.isEmpty()) {
								  System.out.println("No ingredients found for: " + title); } else {
								  System.out.println("Ingredients:"); for (HtmlElement ingredientElement :ingredients) {
								 
								 ingredientsList.add(ingredientElement.getTextContent().trim());
								
								
								 } for (HtmlElement recipeDescription : recipeDescPath) {
								
								
								 recipeDescList.add(recipeDescription.getTextContent().trim());
								
								 } }
								
								
								 System.out.println(ingredientsList);
								 for (String ingredient :
								 ingredientsList) { System.out.println(ingredient); } for (String recipeDesc :
								 recipeDescList) { System.out.println(recipeDesc); }
								
		 					}
		    				
		    				
		    			 }
						/*
						 * while (hasNextPage) { // Process the page content here
						 * //System.out.println(page.asText()); // Attempt to find the link to the next
						 * page
						 *
						 *
						 * if (nextPageLink != null) { // Click the link to the next page Lpage =
						 * nextPageLink.click(); } else { hasNextPage = false; // No more pages }
						 *
						 * }
						 */
		    		
		    		}
		          
	    		}catch (Exception e) {
		            e.printStackTrace();
			       
		    				 }
		}
	}


