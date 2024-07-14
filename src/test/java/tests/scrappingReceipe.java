package tests;

import org.testng.annotations.Test;

import utilities.ExcelUtilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.BrowserVersion;
import org.htmlunit.ScriptException;
import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;


public class scrappingReceipe extends ExcelUtilities {

	static HtmlPage page;
	static HtmlPage recipePage;
	List<String> ingredientsList;
	String recipeId;
	List<String> recipeDescList;
	List<HtmlElement> recipeDescPath;

	@Test
	public void receipescrapping() throws IOException, InterruptedException {
		readExcel();

		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
			//webClient.getOptions().setJavaScriptEnabled(true);
			//webClient.getOptions().setCssEnabled(true);

			//webClient.getOptions().setThrowExceptionOnScriptError(false);
			
						
			
			
			page = webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=A&pageindex=1");
			webClient.waitForBackgroundJavaScriptStartingBefore(200);
		    webClient.waitForBackgroundJavaScript(20000);

			List<HtmlElement> recipeIDs = page.getByXPath("//div[@class='rcc_recipecard']");

			recipeDescList = new ArrayList<>();

			List<HtmlAnchor> azLinks = page.getByXPath("//table/tbody/tr/td/a");
			
			for (int i = 2; i < 3; i++) {
				String dynamicXPath = "(//table/tbody/tr/td/a)[" + i + "]";
                HtmlAnchor link = (HtmlAnchor) page.getFirstByXPath(dynamicXPath);
				
                System.out.println("loop count:" +i);
                
                if (link != null) {

				HtmlPage azPage = link.click();
				System.out.println("Clicked on link: " + link.getTextContent());
				
				 webClient.waitForBackgroundJavaScript(5000 * 2); 
				 
				

				List<HtmlAnchor> recipeLinks = azPage
						.getByXPath("//div[@class='rcc_recipecard']//span[@class='rcc_recipename']/a");

				List<HtmlAnchor> allRecipeLinks = new ArrayList<>();
				allRecipeLinks.addAll(recipeLinks);
				int recipelinkscount=allRecipeLinks.size();

				for (HtmlAnchor recipeLink : allRecipeLinks) {

					ingredientsList = new ArrayList<>();

					recipePage = recipeLink.click();

					// String title = recipePage.getTitleText();
//				System.out.println("Recipe title: " + title);
//	                String recipeId = recipeCards.getAttribute("id");
//	                System.out.println("Recipe ID: " + recipeId);
//				for(HtmlElement recipeid1:recipeIDs) {
//             	   recipeId=recipeid1.getAttribute("id");
//             	  
//                }

					// URL URL = recipePage.getUrl();

					// System.out.println("Recipe URL: " + URL);

					List<HtmlElement> ingredients = recipePage
							.getByXPath("//span[@itemprop='recipeIngredient']/a/span");

					recipeDescPath = recipePage.getByXPath("//span[@itemprop='description']");

					if (ingredients.isEmpty()) {
						System.out.println("No ingredients found for: " + recipePage.getTitleText());
					} else {

						for (HtmlElement ingredientElement : ingredients) {

							String ingredientText = ingredientElement.getTextContent().trim().toUpperCase();
							String[] splitIngredients = ingredientText.split(",");
							for (String splitIngredient : splitIngredients) {
								ingredientsList.add(splitIngredient.trim());
							}

						}
					}

					lfvScrapedList();

				}
                }
				
			}
		}
	}

	public void lfvScrapedList() {

		boolean containsEliminatedIngredient = false;
		ing: for (String ingredient : ingredientsList) {
			for (String eliminatedIngredient : LFVEliminate) {
				if (ingredient.contains(eliminatedIngredient)) {
					containsEliminatedIngredient = true;

					System.out.println("***********RECIPE NOT SCRAPED********");
					System.out.println("Recipe title: " + recipePage.getTitleText());
					// System.out.println("Ingredient eliminated: " +eliminatedIngredient);

					break ing;
				}
			}
		}

		if (!containsEliminatedIngredient) {
			ing: for (String ingredient : ingredientsList) {
				for (String addIngredient : LFVAdd) {
					if (ingredient.contains(addIngredient)) {
						System.out.println("***********RECIPE  SCRAPED********");
						System.out.println("Recipe title: " + recipePage.getTitleText());
						System.out.println("Ingredients: " + ingredientsList);
						System.out.println("Add Ingredients: " + addIngredient);
						System.out.println("URL:" + recipePage.getUrl());

						// System.out.println("recipe ID:"+recipeId);
						for (HtmlElement recipeDescription : recipeDescPath) {
							System.out.println(recipeDescription.getTextContent());
						}
						break ing;

					}
					// break;
				}

			}
		}
		// Print the title and ingredients if no eliminated ingredients are found

	}

}
