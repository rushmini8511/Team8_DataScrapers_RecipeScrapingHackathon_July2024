package utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderCategory {
	private List<String> recipeCategories = Arrays.asList("Breakfast", "Lunch", "Snack", "Dinner");
	private List<String> cuisineCategories = Arrays.asList("Indian", "Indian", "Rajasthani", "Punjabi", "Bengali",
			"orissa", "Gujarati", "Maharashtrian", "Andhra", "Kerala", "Goan", "Kashmiri", "Himachali", "Tamil Nadu",
			"Karnataka", "Sindhi", "Chhattisgarhi", "Madhya Pradesh", "Assamese", "Manipuri", "Tripuri", "Sikkimese",
			"Mizo", "Arunachali", "uttarakhand", "Haryanvi", "Awadhi", "Bihari", "Uttar pradesh", "Delhi",
			"North Indian");
	private List<String> foodCategories = Arrays.asList("Jain", "Vegetarian", "Non-Veg", "Eggitarian", "Vegan", "Veg");

	public Map<String, String> processCategories(String tag) {
		Map<String, String> categoriesMap = new HashMap<>();

		String tagUpperCase = tag.toUpperCase();

		// Process recipe category
		for (String category : recipeCategories) {
			if (tagUpperCase.contains(category.toUpperCase())) {
				categoriesMap.put("RecipeCategory", category);
				break;
			}
		}
		// Process cuisine category
		for (String category : cuisineCategories) {
			if (tagUpperCase.contains(category.toUpperCase())) {
				categoriesMap.put("CuisineCategory", category);
				break;
			}
		}

		// Process food category
		for (String category : foodCategories) {
			if (tagUpperCase.contains(category.toUpperCase())) {
				categoriesMap.put("FoodCategory", category);
				break;
			}
		}
		categoriesMap.putIfAbsent("CuisineCategory", "Not Found in Recipe");
		categoriesMap.putIfAbsent("FoodCategory", "Not Found in Recipe");
		categoriesMap.putIfAbsent("RecipeCategory", "Not Found in Recipe");
		return categoriesMap;
	}
}