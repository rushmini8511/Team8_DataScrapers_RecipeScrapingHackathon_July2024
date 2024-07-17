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

public class ScraperTest_L extends BrowserUtilities {

	@Test
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
				System.out.println("This Page begins with the letter =" + c);
				PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=" + c);
			}

			// PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=A");
			// PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=M");
			// PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=N");
			// PagesList.add("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=O");
			
			for (int i = 0; i < PagesList.size(); i++) {
				System.out.println("_______" + PagesList.get(i));
				HtmlPage page = webClient.getPage(PagesList.get(i));

				List<HtmlAnchor> pages = page.getByXPath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a");
				int pagecount = pages.size() + 1;
				System.out.println("No of Pages = " + pagecount);

				for (int p = 1; p <= pagecount; p++)

				{
					List<HtmlAnchor> nextPageLink = page.getByXPath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a[text()='" + p + "']");
					System.out.println(nextPageLink.toString());

					for (HtmlAnchor next : nextPageLink) {
						HtmlPage n = next.click();
						
						List<HtmlAnchor> recipeLinks = n.getByXPath("//div[@class='rcc_recipecard']//span[@class='rcc_recipename']/a");

						for (HtmlAnchor recipeLink : recipeLinks) {

							HtmlPage recipePage = recipeLink.click();
							WebClient webClient2 = new WebClient();
							webClient2.waitForBackgroundJavaScript(3000);
							String title = recipePage.getTitleText();

							System.out.println("----------------------------------------");
							System.out.println("Recipe title: " + title);

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
							List<HtmlElement> Fcategory = recipePage.getByXPath("//div[@id='show_breadcrumb']//span[@itemprop='itemListElement'] [3]");
							List<String> FcategoryList = new ArrayList<>();
							List<HtmlElement> Tcategory = recipePage.getByXPath("//div[@class='tags']");
							List<String>TcategoryList = new ArrayList<>();
							
							
							
							if (ingredients.isEmpty()) {
								System.out.println("No ingredients found for: " + title);
							} else {
								System.out.println("Ingredients:");
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
								for (HtmlElement tags_category :Tcategory) {

									TcategoryList.add(tags_category.getTextContent().trim());
                               
								}
								
								
								
							}

							//System.out.println(ingredientsList);
							for (String ingredient : ingredientsList) {
								System.out.println(ingredient);
							}
							for (String recipeDesc : recipeDescList) {
								System.out.println("the description of the recipe is :" +recipeDesc);
							}
							for (String PreparationTime : PreparationList) {
								System.out.println("The Preparationtime is :" +PreparationTime);
							}
							for (String CookingTime : cookingList) {
								System.out.println("The cookingtime is : " +CookingTime);
							}
						
							for (String Cuisinecategory : cuisineList) {
								System.out.println("The Cuisine category is : "+Cuisinecategory);
							
							
							}
							for (String foodcategory : FcategoryList) {
								System.out.println("The food category is :" +foodcategory);
							
							}
							for (String Tags : TcategoryList) {
								System.out.println("The Tags is :" +Tags);
							
							}
							
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
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	
    }
 }
