package scraper;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.BrowserVersion;
import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.testng.annotations.Test;

import analyzer.MyRetryAnalyzer;
import java.io.IOException;
import utilities.ExcelUtilities;
import utilities.RecipeWriter;

public class ScraperTest_LtoO extends ExcelUtilities{
	static HtmlPage page;
	static HtmlPage recipePage;
	static List<String> ingredientsList;
	String recipeId;
	List<String> recipeDescList;
	static List<HtmlElement> recipeDescPath;
	RecipeWriter writer;
	HtmlElement preprationTime;
	HtmlElement cookingTime,recipeTags,servings;
	List<HtmlElement> preparationMethod;
	HtmlElement CuisineCategory;
	List<HtmlElement> NutrientValues;
	 
	
	 
	  
	@Test(retryAnalyzer = MyRetryAnalyzer.class)
	public void scrapReceipe() throws IOException {
		readExcel();
        writer = new RecipeWriter("src/test/java/resources/ScrappedRecipes.xlsx");
		 try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
	            webClient.getOptions().setJavaScriptEnabled(false);
	            webClient.getOptions().setCssEnabled(false);

	            page = webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx");

	            List<HtmlElement> recipeIDs = page.getByXPath("//div[@class='rcc_recipecard']");
	            List<String> recipeDescList = new ArrayList<>();

	            List<HtmlAnchor> azLinks = page.getByXPath("//table/tbody/tr/td/a");
try {
	            for (HtmlAnchor link : azLinks) {
	                String href = link.getAttribute("href");
	                if (href.startsWith("javascript:__doPostBack")) {
	                    String[] parts = href.split("'");
	                    String eventTarget = parts[1];
	                    String eventArgument = parts[3];
	                    
	                   
	                    String postbackData = "__EVENTTARGET=" + eventTarget + "&__EVENTARGUMENT=" + eventArgument;
	                    //String postbackData = "__EVENTTARGET=" + eventTarget;

	                    HtmlPage azPage = webClient.getPage(page.getUrl() + "?" + postbackData);
	                    System.out.println("main page:"+azPage);

	                    webClient.waitForBackgroundJavaScript(3000);

	                   
	                    int count = 0;

	                    List<HtmlAnchor> paginationLinks = azPage.getByXPath("//div[2]/a[contains(@href,'RecipeAtoZ.aspx?')]");
	                    if(! paginationLinks.isEmpty()) {
	                    	
	                    	String lastLink = paginationLinks.get(paginationLinks.size()-1).toString();
	                    	int startIndex=lastLink.indexOf("pageindex=")+"pageindex=".length();
	                    	lastLink = lastLink.substring(startIndex);
	                    	count = Integer.parseInt(lastLink.substring(0, lastLink.indexOf("\">]")));
	                    	 System.out.println("Total count: " + count);
	                    	 
	                    }
	                    if(! paginationLinks.isEmpty()) {
	                    	
	                        for (int i = 1; i <= count; i++) {
	                        	
	                        	
	                        	HtmlPage pageUrl = webClient.getPage(page.getUrl()+"?"+ "beginswith=" + eventArgument +"&pageindex=" + i);
	                            
	                        	System.out.println("sub page:"+pageUrl);
	                            processRecipesOnPage(pageUrl);
	                        }
	                    }
       }
	        }} finally {
	            writer.saveAndClose();
	        }
	            }catch (FailingHttpStatusCodeException | IOException e) {
	            e.printStackTrace();
	        }
//		 finally {
//	            writer.saveAndClose();
//	        }
	    }
	private  void processRecipesOnPage(HtmlPage page) throws IOException {
        List<HtmlAnchor> recipeLinks = page.getByXPath("//div[@class='rcc_recipecard']//span[@class='rcc_recipename']/a");
        
        

        for (HtmlAnchor recipeLink : recipeLinks) {
            ingredientsList = new ArrayList<>();
            recipePage = recipeLink.click();
           // webClient.waitForBackgroundJavaScript(3000);

            List<HtmlElement> ingredients = recipePage.getByXPath("//span[@itemprop='recipeIngredient']/a/span");
            recipeDescPath = recipePage.getByXPath("//span[@itemprop='description']");
           preprationTime= recipePage.getFirstByXPath("//time[@itemprop='prepTime']");
           cookingTime= recipePage.getFirstByXPath("//time[@itemprop='cookTime']");
           recipeTags=recipePage.getFirstByXPath("//div[@id='recipe_tags']");
           servings=recipePage.getFirstByXPath("//span[@itemprop='recipeYield']");
          CuisineCategory=recipePage.getFirstByXPath("//a[@itemprop='recipeCuisine']");
           preparationMethod=recipePage.getByXPath("//*[@id='ctl00_cntrightpanel_pnlRcpMethod']//li/span");
           List<HtmlElement> nutrientvalues = recipePage.getByXPath("//table[@id='rcpnutrients']/tbody/tr/td[1]");
           List<HtmlElement> perserving  = recipePage.getByXPath("//table[@id='rcpnutrients']/tbody/tr/td[2]");
           
           for(int i=0;i<nutrientvalues.size();i++)
           {
        	   System.out.println(nutrientvalues.get(i).getTextContent());
        	   System.out.println(perserving.get(i).getTextContent());
           }
           
           
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
           // lchfScrapedList();
           
        }
        
       
	}
	
	
	private  void lfvScrapedList() {
		String recipeDESC=null;

		for (HtmlElement recipeDescription : recipeDescPath) {
			recipeDESC=recipeDescription.getTextContent();
			//System.out.println(recipeDescription.getTextContent());
		}
		
		
		
		
		String prepTime=null;
		String cookTime=null;
		if(preprationTime==null||cookingTime==null) {
			prepTime="Not available";
			cookTime="Not available";
		}
		else {
			prepTime=preprationTime.getTextContent();
			cookTime=cookingTime.getTextContent();
		}
		
		
		
		
		
		
		boolean containsEliminatedIngredient = false;
		ing: for (String ingredient : ingredientsList) {
			for (String eliminatedIngredient : LFVEliminate) {
				if (ingredient.contains(eliminatedIngredient)) {
					containsEliminatedIngredient = true;

//					System.out.println("***********RECIPE NOT SCRAPED********");
//					System.out.println("Recipe title: " + recipePage.getTitleText());
					// System.out.println("Ingredient eliminated: " +eliminatedIngredient);
					 try {
		                    writer.WriteScrappedRecipes("LFV-Elimination", "NA",recipePage.getTitleText(),"NA","NA", ingredientsList,prepTime
		                    		,cookTime,recipeTags.getTextContent(),servings.getTextContent() ,"NA",recipeDESC,preparationMethod.toString(),"NA", recipePage.getUrl());
		                    writer.save(); // Save once at the end
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }
					break ing;
				}
			}
		}

		if (!containsEliminatedIngredient) {
			ing: for (String ingredient : ingredientsList) {
				for (String addIngredient : LFVAdd) {
					if (ingredient.contains(addIngredient)) {
						
//						System.out.println("***********RECIPE  SCRAPED********");
//						System.out.println("Recipe title: " + recipePage.getTitleText());
//						System.out.println("Ingredients: " + ingredientsList);
//						System.out.println("Add Ingredients: " + addIngredient);
//						System.out.println("URL:" + recipePage.getUrl());
						

						 try {
			                    writer.WriteScrappedRecipes("LFV-Add","NA",recipePage.getTitleText(),"NA","NA", ingredientsList,prepTime
			                    		,cookTime,recipeTags.getTextContent(),servings.getTextContent(),recipeDESC,preparationMethod.toString(),"NA", recipePage.getUrl());
			                    writer.save(); // Save once at the end
			                } catch (IOException e) {
			                    e.printStackTrace();
			                }
						
						
						break ing;
						
					}
//					else {
//						
//							for (String addnonvegan : LFVAdd_Nonvegan) {
//						if (ingredient.contains(addnonvegan)) {
//							 try {
//				                    writer.WriteScrappedRecipes("LFV-Add-non-VEGAN","NA",recipePage.getTitleText(),"NA","NA", ingredientsList,prepTime
//				                    		,cookTime,recipeTags.getTextContent(),servings.getTextContent() ,"NA",recipeDESC,preparationMethod.toString(),"NA", recipePage.getUrl());
//				                    writer.save(); // Save once at the end
//				                } catch (IOException e) {
//				                    e.printStackTrace();
//				                }
//							 break ing;
//							
//						}
//							}
//					}

					}
			}}
	}
	
	
//	private  void lchfScrapedList() {
//		String recipeDESC=null;
//		LCHFEliminate.add("MILK");
//		LCHFEliminate.add("OIL");
//
//		for (HtmlElement recipeDescription : recipeDescPath) {
//			recipeDESC=recipeDescription.getTextContent();
//			//System.out.println(recipeDescription.getTextContent());
//		}
//		String prepTime=null;
//		String cookTime=null;
//		if(preprationTime==null||cookingTime==null) {
//			prepTime="Not available";
//			cookTime="Not available";
//		}
//		else {
//			prepTime=preprationTime.getTextContent();
//			cookTime=cookingTime.getTextContent();
//		}
//		boolean containsLCHFEliminatedIngredient = false;
//		ing: for (String ingredient : ingredientsList) {
//			for (String eliminatedLCHFIngredient : LCHFEliminate) {
//				if (ingredient.contains(eliminatedLCHFIngredient)) {
//					containsLCHFEliminatedIngredient = true;
//
//					System.out.println("***********RECIPE NOT SCRAPED for elimination********");
//					System.out.println("Recipe title: " + recipePage.getTitleText());
//					 System.out.println("Ingredient eliminated: " +eliminatedLCHFIngredient);
//					 try {
//		                    writer.WriteScrappedRecipes("LCHF-Elimination", "NA",recipePage.getTitleText(),"NA","NA", ingredientsList,prepTime
//		                    		,cookTime,recipeTags.getTextContent(),servings.getTextContent() ,"NA",recipeDESC,preparationMethod.toString(),"NA", recipePage.getUrl());
//		                    writer.save(); // Save once at the end
//		                } catch (IOException e) {
//		                    e.printStackTrace();
//		                }
//					break ing;
//				}
//			}
//		}
//
////		if (!containsLCHFEliminatedIngredient) {
////			ing: for (String ingredient : ingredientsList) {
////				for (String addLCHFIngredient : LCHFAdd) {
////					if (ingredient.contains(addLCHFIngredient)) {
////						
////						System.out.println("***********RECIPE  SCRAPED for add********");
////						System.out.println("Recipe title: " + recipePage.getTitleText());
////						System.out.println("Ingredients: " + ingredientsList);
////						System.out.println("Add Ingredients: " + addLCHFIngredient);
////						//System.out.println("URL:" + recipePage.getUrl());
////	
////						 try {
////			                    writer.WriteScrappedRecipes("LCHF-Add","NA",recipePage.getTitleText(),"NA","NA", ingredientsList,prepTime
////			                    		,cookTime,recipeTags.getTextContent(),servings.getTextContent() ,"NA",recipeDESC,preparationMethod.toString(),"NA", recipePage.getUrl());
////			                    writer.save(); // Save once at the end
////			                } catch (IOException e) {
////			                    e.printStackTrace();
////			                }
////						
////						
////						 break ing;
////						
////					}
////					
////
////					}
////			}}
//	}

}

	




	
 


