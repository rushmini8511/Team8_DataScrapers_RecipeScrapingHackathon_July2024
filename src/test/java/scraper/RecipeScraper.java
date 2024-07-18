package scraper;

import org.testng.annotations.Test;

import analyzer.MyRetryAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import utilities.ExcelUtilities;

import utilities.HeaderCategory;
import utilities.RecipeWriter;

public class RecipeScraper extends ExcelUtilities {
	static HtmlPage page;
	static HtmlPage recipePage;
	static List<String> ingredientsList;
	String recipeId;
	List<String> recipeDescList, recipePreparationData;
	static List<HtmlElement> recipeDescPath;
	RecipeWriter writer;
	HtmlElement preprationTime;
	HtmlElement cookingTime, recipeTags, servings;
	List<HtmlElement> preparationMethod;

	HeaderCategory headers = new HeaderCategory();
	List<String> nutrientsData;
	String recipeCategory, foodCategory, cuisineCategory = null;
	Map<String, String> categories;

	@Test (retryAnalyzer = MyRetryAnalyzer.class)
	public void scrapReceipe() throws IOException {
		readExcel();
		writer = new RecipeWriter("./src/test/resources/Data/ScrappedRecipes.xlsx");
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setCssEnabled(false);

			page = webClient.getPage("https://www.tarladalal.com/RecipeAtoZ.aspx");

			List<HtmlElement> recipeIDs = page.getByXPath("//div[@class='rcc_recipecard']");
			List<String> recipeDescList = new ArrayList<>();

			List<HtmlAnchor> azLinks = page.getByXPath("//table/tbody/tr/td/a");
			try {
				boolean startScraping = false;

				for (HtmlAnchor link : azLinks) {
					String href = link.getAttribute("href");
					if (link.getTextContent().equalsIgnoreCase("A")) {
						startScraping = true;
					}

					if (startScraping && href.startsWith("javascript:__doPostBack")) {

						if (href.startsWith("javascript:__doPostBack")) {
							String[] parts = href.split("'");
							String eventTarget = parts[1];
							String eventArgument = parts[3];

							String postbackData = "_EVENTTARGET=" + eventTarget + "&_EVENTARGUMENT=" + eventArgument;

							HtmlPage azPage = webClient.getPage(page.getUrl() + "?" + postbackData);
							System.out.println("main page:" + azPage);

							webClient.waitForBackgroundJavaScript(3000);

							int count = 0;

							List<HtmlAnchor> paginationLinks = azPage
									.getByXPath("//div[2]/a[contains(@href,'RecipeAtoZ.aspx?')]");
							if (!paginationLinks.isEmpty()) {

								String lastLink = paginationLinks.get(paginationLinks.size() - 1).toString();
								int startIndex = lastLink.indexOf("pageindex=") + "pageindex=".length();
								lastLink = lastLink.substring(startIndex);
								count = Integer.parseInt(lastLink.substring(0, lastLink.indexOf("\">]")));
								System.out.println("Total count: " + count);

							}
							if (!paginationLinks.isEmpty()) {

								for (int i = 1; i <= count; i++) {

									HtmlPage pageUrl = webClient.getPage(
											page.getUrl() + "?" + "beginswith=" + eventArgument + "&pageindex=" + i);

									System.out.println("sub page:" + pageUrl);
									processRecipesOnPage(pageUrl);
								}
							}
						}
					}
				}
			} finally {
				writer.saveAndClose();
			}
		}
	}

	private void processRecipesOnPage(HtmlPage page) throws IOException {
		List<HtmlAnchor> recipeLinks = page
				.getByXPath("//div[@class='rcc_recipecard']//span[@class='rcc_recipename']/a");

		for (HtmlAnchor recipeLink : recipeLinks) {
			ingredientsList = new ArrayList<>();
			recipePage = recipeLink.click();

			List<HtmlElement> ingredients = recipePage.getByXPath("//span[@itemprop='recipeIngredient']/a/span");
			recipeDescPath = recipePage.getByXPath("//span[@itemprop='description']");
			preprationTime = recipePage.getFirstByXPath("//time[@itemprop='prepTime']");
			cookingTime = recipePage.getFirstByXPath("//time[@itemprop='cookTime']");
			recipeTags = recipePage.getFirstByXPath("//div[@id='recipe_tags']");
			servings = recipePage.getFirstByXPath("//span[@itemprop='recipeYield']");
			preparationMethod = recipePage.getByXPath("//*[@id='ctl00_cntrightpanel_pnlRcpMethod']//li/span");

			recipePreparationData = new ArrayList<String>();
			String tag = recipeTags.getTextContent();
//         
			Map<String, String> categories = headers.processCategories(tag);
			recipeCategory = categories.get("RecipeCategory");
			foodCategory = categories.get("FoodCategory");
			cuisineCategory = categories.get("CuisineCategory");
			for (HtmlElement prep : preparationMethod) {

				recipePreparationData.add(prep.getTextContent());
			}

			String URL = recipePage.getUrl().toString();

			String[] params = URL.split("-");
			String lastPart = params[params.length - 1]; // Get the last part after splitting by "-"

			recipeId = lastPart.replaceAll("[^\\d]", "");
			nutrientsData = new ArrayList<String>();

			List<HtmlElement> nutrientvalues = recipePage.getByXPath("//table[@id='rcpnutrients']/tbody/tr/td[1]");
			List<HtmlElement> perserving = recipePage.getByXPath("//table[@id='rcpnutrients']/tbody/tr/td[2]");
			for (int i = 0; i < nutrientvalues.size(); i++) {

				nutrientsData.add(nutrientvalues.get(i).getTextContent() + ":" + perserving.get(i).getTextContent());

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
			lfvRecipeAvoid();
			lfvOptionalRecipe();
			lchfScrapedList();
			lchfRecipeAvoid();
			lchfFoodProcessing();
			ScrapedListAllergies();

		}

	}

	private void lfvOptionalRecipe() {

		String recipeDESC = null;
		String tag = recipeTags.getTextContent();

		String tagsUpperCase = tag.toUpperCase().replaceAll("\\s+", " ");

		for (HtmlElement recipeDescription : recipeDescPath) {
			recipeDESC = recipeDescription.getTextContent();
		}
		String prepTime = preprationTime != null ? preprationTime.getTextContent() : "Not available";
		String cookTime = cookingTime != null ? cookingTime.getTextContent() : "Not available";

		// Check for HERBAL recipes in the title
		if (recipePage.getTitleText().toUpperCase().contains("HERBAL")) {
			for (String ingredient : ingredientsList) {
				if (!ingredient.contains("SUGAR")) {
					System.out.println("**** Herbal Recipe Found ****");
					System.out.println("Herbal ingredients: " + ingredientsList);

					try {
						writer.WriteScrappedRecipes("LFV-OptionalRecipes-Herbal", recipeId, recipePage.getTitleText(),
								recipeCategory, foodCategory, ingredientsList, prepTime, cookTime,
								recipeTags.getTextContent(), servings.getTextContent(), cuisineCategory, recipeDESC,
								recipePreparationData, nutrientsData, recipePage.getUrl());
						writer.save();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		// Check for TEA or COFFEE recipes in the title or tags
		if ((recipePage.getTitleText().toUpperCase().contains("TEA")
				|| recipePage.getTitleText().toUpperCase().contains("COFFEE"))) {
			for (String ingredient : ingredientsList) {

				if (!ingredient.contains("SUGAR")) {
					System.out.println("**** Tea/COFFEE Recipe Found ****");
					System.out.println("Tea/Coffee ingredients: " + ingredientsList);

					try {
						writer.WriteScrappedRecipes("LFV-OptionalRecipes-Cofee-Tea", recipeId,
								recipePage.getTitleText(), recipeCategory, foodCategory, ingredientsList, prepTime,
								cookTime, recipeTags.getTextContent(), servings.getTextContent(), cuisineCategory,
								recipeDESC, recipePreparationData, nutrientsData, recipePage.getUrl());
						writer.save();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}

		// If a recipe was processed, break the outer loop

	}

	private void lfvRecipeAvoid() {
		String recipeDESC = null;

		for (HtmlElement recipeDescription : recipeDescPath) {
			recipeDESC = recipeDescription.getTextContent();

		}
		String prepTime = preprationTime != null ? preprationTime.getTextContent() : "Not available";
		String cookTime = cookingTime != null ? cookingTime.getTextContent() : "Not available";

		LFV_RecipesToAvoid.add("DEEP-FRIED");
		LFV_RecipesToAvoid.add("DEEPFRY");
		LFV_RecipesToAvoid.add("MICROWAVE");
		LFV_RecipesToAvoid.add("READY MEALS");

		String tag = recipeTags.getTextContent();

		String tagsUpperCase = tag.toUpperCase().replaceAll("\\s+", " ");

		boolean containsAvoidedItem = false;
		for (String item : LFV_RecipesToAvoid) {
			if (tagsUpperCase.contains(item)) {
				containsAvoidedItem = true;

				try {
					writer.WriteScrappedRecipes("LFV-Recipes-To-Avoid", recipeId, recipePage.getTitleText(),
							recipeCategory, foodCategory, ingredientsList, prepTime, cookTime,
							recipeTags.getTextContent(), servings.getTextContent(), cuisineCategory, recipeDESC,
							recipePreparationData, nutrientsData, recipePage.getUrl());
					writer.save();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

		}

	}

	private void lchfRecipeAvoid() {
		String recipeDESC = null;

		for (HtmlElement recipeDescription : recipeDescPath) {
			recipeDESC = recipeDescription.getTextContent();

		}
		String prepTime = preprationTime != null ? preprationTime.getTextContent() : "Not available";
		String cookTime = cookingTime != null ? cookingTime.getTextContent() : "Not available";

		LCHF_RecipesToAvoid.add("PROCESSED");

		String tag = recipeTags.getTextContent();

		String tagsUpperCase = tag.toUpperCase().replaceAll("\\s+", " ");

		boolean containsAvoidedItem = false;
		for (String item : LCHF_RecipesToAvoid) {
			if (tagsUpperCase.contains(item)) {
				containsAvoidedItem = true;

				try {
					writer.WriteScrappedRecipes("LCHF-Recipes-To-Avoid", recipeId, recipePage.getTitleText(),
							recipeCategory, foodCategory, ingredientsList, prepTime, cookTime,
							recipeTags.getTextContent(), servings.getTextContent(), cuisineCategory, recipeDESC,
							recipePreparationData, nutrientsData, recipePage.getUrl());
					writer.save();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

		}

	}

	private void lchfFoodProcessing() {
		String recipeDESC = null;

		for (HtmlElement recipeDescription : recipeDescPath) {
			recipeDESC = recipeDescription.getTextContent();

		}
		String prepTime = preprationTime != null ? preprationTime.getTextContent() : "Not available";
		String cookTime = cookingTime != null ? cookingTime.getTextContent() : "Not available";

		String tag = recipeTags.getTextContent();

		String tagsUpperCase = tag.toUpperCase().replaceAll("\\s+", " ");

		boolean containsAvoidedItem = false;
		for (String item : LCHF_FoodProcessing) {
			if (tagsUpperCase.contains(item)) {
				containsAvoidedItem = true;

				try {
					writer.WriteScrappedRecipes("LCHF-Food-Processing", recipeId, recipePage.getTitleText(),
							recipeCategory, foodCategory, ingredientsList, prepTime, cookTime,
							recipeTags.getTextContent(), servings.getTextContent(), cuisineCategory, recipeDESC,
							recipePreparationData, nutrientsData, recipePage.getUrl());
					writer.save();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

		}

	}

	private void lfvScrapedList() {

		String recipeDESC = null;

		for (HtmlElement recipeDescription : recipeDescPath) {
			recipeDESC = recipeDescription.getTextContent();

		}
		String prepTime = preprationTime != null ? preprationTime.getTextContent() : "Not available";
		String cookTime = cookingTime != null ? cookingTime.getTextContent() : "Not available";

		boolean containsEliminatedIngredient = false;
		ing: for (String ingredient : ingredientsList) {
			for (String eliminatedIngredient : LFVEliminate) {
				if (ingredient.contains(eliminatedIngredient)) {
					containsEliminatedIngredient = true;

					try {
						writer.WriteScrappedRecipes("LFV-Elimination", recipeId, recipePage.getTitleText(),
								recipeCategory, foodCategory, ingredientsList, prepTime, cookTime,
								recipeTags.getTextContent(), servings.getTextContent(), cuisineCategory, recipeDESC,
								recipePreparationData, nutrientsData, recipePage.getUrl());
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

						try {
							writer.WriteScrappedRecipes("LFV-Add", recipeId, recipePage.getTitleText(), recipeCategory,
									foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
									servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData,
									nutrientsData, recipePage.getUrl());
							writer.save(); // Save once at the end
						} catch (IOException e) {
							e.printStackTrace();
						}

						break ing;

					} else {

						for (String addnonvegan : LFVAdd_Nonvegan) {
							if (ingredient.contains(addnonvegan)) {
								try {
									writer.WriteScrappedRecipes("LFV-Add-non-VEGAN", recipeId,
											recipePage.getTitleText(), recipeCategory, foodCategory, ingredientsList,
											prepTime, cookTime, recipeTags.getTextContent(), servings.getTextContent(),
											cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
											recipePage.getUrl());
									writer.save(); // Save once at the end
								} catch (IOException e) {
									e.printStackTrace();
								}
								break ing;

							}
						}
					}

				}
			}
		}
	}

	private void lchfScrapedList() {
		String recipeDESC = null;
		LCHFEliminate.add("MILK");
		LCHFEliminate.add("OIL");

		for (HtmlElement recipeDescription : recipeDescPath) {
			recipeDESC = recipeDescription.getTextContent();

		}
		String prepTime = preprationTime != null ? preprationTime.getTextContent() : "Not available";
		String cookTime = cookingTime != null ? cookingTime.getTextContent() : "Not available";
		boolean containsLCHFEliminatedIngredient = false;
		ing: for (String ingredient : ingredientsList) {
			for (String eliminatedLCHFIngredient : LCHFEliminate) {
				if (ingredient.contains(eliminatedLCHFIngredient)) {
					containsLCHFEliminatedIngredient = true;

					try {
						writer.WriteScrappedRecipes("LCHF-Elimination", recipeId, recipePage.getTitleText(),
								recipeCategory, foodCategory, ingredientsList, prepTime, cookTime,
								recipeTags.getTextContent(), servings.getTextContent(), cuisineCategory, recipeDESC,
								recipePreparationData, nutrientsData, recipePage.getUrl());
						writer.save(); // Save once at the end
					} catch (IOException e) {
						e.printStackTrace();
					}
					break ing;
				}
			}
		}

		if (!containsLCHFEliminatedIngredient) {
			ing: for (String ingredient : ingredientsList) {
				for (String addLCHFIngredient : LCHFAdd) {
					if (ingredient.contains(addLCHFIngredient)) {

						try {
							writer.WriteScrappedRecipes("LCHF-Add", recipeId, recipePage.getTitleText(), recipeCategory,
									foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
									servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData,
									nutrientsData, recipePage.getUrl());
							writer.save(); // Save once at the end
						} catch (IOException e) {
							e.printStackTrace();
						}

						break ing;

					}

				}
			}
		}

	}

	private void ScrapedListAllergies() {

		String recipeDESC = null;

		for (HtmlElement recipeDescription : recipeDescPath) {
			recipeDESC = recipeDescription.getTextContent();

		}
		String prepTime = preprationTime != null ? preprationTime.getTextContent() : "Not available";
		String cookTime = cookingTime != null ? cookingTime.getTextContent() : "Not available";

		boolean containsMilk = false, containsSoy = false, containsegg = false, containsSesame = false,
				containsPeanut = false, containsWalnut = false, containsAlmond = false, containsHazelnut = false,
				containsPecan = false, containsCashew = false, containsPistachio = false, containsShellfish = false,
				containsSeafood = false;

		for (String ingredient : ingredientsList) {
			if (ingredient.toUpperCase().contains("MILK")) {
				containsMilk = true;
			}
			if (ingredient.toUpperCase().contains("SOY")) {
				containsSoy = true;
			}
			if (ingredient.contains("EGG")) {
				containsegg = true;

			}
			if (ingredient.contains("SESAME")) {
				containsSesame = true;

			}
			if (ingredient.contains("PEANUT")) {
				containsPeanut = true;

			}
			if (ingredient.contains("WALNUT")) {
				containsWalnut = true;

			}
			if (ingredient.contains("ALMOND") || ingredient.contains("BADAM")) {
				containsAlmond = true;

			}
			if (ingredient.contains("HAZELNUT")) {
				containsHazelnut = true;

			}
			if (ingredient.contains("PECAN")) {
				containsPecan = true;

			}
			if (ingredient.contains("CASHEW")) {
				containsCashew = true;

			}
			if (ingredient.contains("PISTACHIO") || ingredient.contains("PISTA")) {
				containsPistachio = true;

			}
			if (ingredient.contains("SHELLFISH")) {
				containsShellfish = true;

			}
			if (ingredient.contains("SEAFOOD")) {
				containsSeafood = true;

			}

		}
		if (!containsMilk) {

			try {
				writer.WriteScrappedRecipes("Allergies_MILK", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains MILK, skipping...");
		}

		if (!containsSoy) {

			try {
				writer.WriteScrappedRecipes("Allergies_SOY", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains SOY, skipping...");
		}
		if (!containsegg) {

			try {
				writer.WriteScrappedRecipes("Allergies_EGG", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains EGG, skipping...");
		}
		if (!containsSesame) {

			try {
				writer.WriteScrappedRecipes("Allergies_SESAME", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains SESAME, skipping...");
		}
		if (!containsPeanut) {

			try {
				writer.WriteScrappedRecipes("Allergies_PEANUT", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains PEANUT, skipping...");
		}
		if (!containsWalnut) {

			try {
				writer.WriteScrappedRecipes("Allergies_Walnut", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains Walnut, skipping...");
		}
		if (!containsAlmond) {

			try {
				writer.WriteScrappedRecipes("Allergies_Almond", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains Almond, skipping...");
		}
		if (!containsHazelnut) {

			try {
				writer.WriteScrappedRecipes("Allergies_Hazelnut", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains Hazelnut, skipping...");
		}
		if (!containsPecan) {

			try {
				writer.WriteScrappedRecipes("Allergies_Pecan", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains Pecan, skipping...");
		}
		if (!containsCashew) {

			try {
				writer.WriteScrappedRecipes("Allergies_Cashew", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains Cashew, skipping...");
		}
		if (!containsPistachio) {

			try {
				writer.WriteScrappedRecipes("Allergies_Pistachio", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains Pistachio, skipping...");
		}
		if (!containsShellfish) {

			try {
				writer.WriteScrappedRecipes("Allergies_Shellfish", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains Shellfish, skipping...");
		}
		if (!containsSeafood) {

			try {
				writer.WriteScrappedRecipes("Allergies_Seafood", recipeId, recipePage.getTitleText(), recipeCategory,
						foodCategory, ingredientsList, prepTime, cookTime, recipeTags.getTextContent(),
						servings.getTextContent(), cuisineCategory, recipeDESC, recipePreparationData, nutrientsData,
						recipePage.getUrl());
				writer.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Recipe contains Seafood, skipping...");
		}
	}

}
