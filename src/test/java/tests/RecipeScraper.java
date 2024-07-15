package tests;

import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.BrowserVersion;
import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.testng.annotations.Test;

import utilities.ExcelUtilities;

public class RecipeScraper extends ExcelUtilities{
	static HtmlPage page;
	static HtmlPage recipePage;
	static List<String> ingredientsList;
	String recipeId;
	List<String> recipeDescList;
	static List<HtmlElement> recipeDescPath;
	@Test
	public void scrapReceipe() throws IOException {
		readExcel();
		 try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
	            webClient.getOptions().setJavaScriptEnabled(false);
	            webClient.getOptions().setCssEnabled(false);

	            page = webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx");

	            List<HtmlElement> recipeIDs = page.getByXPath("//div[@class='rcc_recipecard']");
	            List<String> recipeDescList = new ArrayList<>();

	            List<HtmlAnchor> azLinks = page.getByXPath("//table/tbody/tr/td/a");

	            for (HtmlAnchor link : azLinks) {
	                String href = link.getAttribute("href");
	                if (href.startsWith("javascript:__doPostBack")) {
	                    String[] parts = href.split("'");
	                    String eventTarget = parts[1];
	                    String eventArgument = parts[3];

	                    String postbackData = "__EVENTTARGET=" + eventTarget + "&__EVENTARGUMENT=" + eventArgument;

	                    HtmlPage azPage = webClient.getPage(page.getUrl() + "?" + postbackData);
	                    System.out.println("main page:"+azPage);

	                    webClient.waitForBackgroundJavaScript(3000);

	                    //processRecipesOnPage(azPage);
	                    int count = 0;

	                    List<HtmlAnchor> paginationLinks = azPage.getByXPath("//div[2]/a[contains(@href,'RecipeAtoZ.aspx?')]");
	                    if(! paginationLinks.isEmpty()) {
	                    	String lastLink = paginationLinks.get(paginationLinks.size()-1).toString();
	                    	int startIndex=lastLink.indexOf("pageindex=")+"pageindex=".length();
	                    	lastLink = lastLink.substring(startIndex);
	                    	count = Integer.parseInt(lastLink.substring(0, lastLink.indexOf("\">]")));
	                    	 System.out.println("Total count: " + count);
	                    }
	                    for(int i=0;i<count;i++) {
	                    //for (HtmlAnchor paginationLink : paginationLinks) {
	                        HtmlPage paginatedPage = paginationLinks.get(i).click();
	                       
	                       //Add next or hidden and reappeared links
	                        
	                       
	                        webClient.waitForBackgroundJavaScript(3000);
	                        
	                        System.out.println("sub page:"+paginatedPage);

	                        processRecipesOnPage(paginatedPage);
	                    }
	                }
	            }
	        } catch (FailingHttpStatusCodeException | IOException e) {
	            e.printStackTrace();
	        }
	    }
	private static void processRecipesOnPage(HtmlPage page) throws IOException {
        List<HtmlAnchor> recipeLinks = page.getByXPath("//div[@class='rcc_recipecard']//span[@class='rcc_recipename']/a");

        for (HtmlAnchor recipeLink : recipeLinks) {
            ingredientsList = new ArrayList<>();
            recipePage = recipeLink.click();
           // webClient.waitForBackgroundJavaScript(3000);

            List<HtmlElement> ingredients = recipePage.getByXPath("//span[@itemprop='recipeIngredient']/a/span");
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
	
	
	private static void lfvScrapedList() {
        
        boolean containsEliminatedIngredient = false;
		ing: for (String ingredient : ingredientsList) {
			for (String eliminatedIngredient : LFVEliminate) {
				if (ingredient.contains(eliminatedIngredient)) {
					containsEliminatedIngredient = true;

//					System.out.println("***********RECIPE NOT SCRAPED********");
//					System.out.println("Recipe title: " + recipePage.getTitleText());
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
    }
}
	



	
 


