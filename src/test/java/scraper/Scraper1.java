package scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper1 {

    public static void main(String[] args) {
       
        WebDriver driver = new ChromeDriver();

        // Navigate to the recipe page
        driver.get("https://www.tarladalal.com/Sabudana-Khichdi-(-Tiffin-Treats-)-33685r");

        // Get the current URL of the recipe page
        String currentUrl = driver.getCurrentUrl();
        
        // Close the WebDriver
        driver.quit();

        // Extract recipe ID from URL
        String recipeId = extractRecipeId(currentUrl);
        System.out.println("Recipe ID: " + recipeId);
    }

    // Method to extract recipe ID from URL
    private static String extractRecipeId(String url) {
        // Example URL: https://www.tarladalal.com/Sabudana-Khichdi-(-Tiffin-Treats-)-33685r
        // Pattern to match the recipe ID at the end of the URL
        Pattern pattern = Pattern.compile("-([0-9]+)r$");
        Matcher matcher = pattern.matcher(url);
        
        if (matcher.find()) {
            return matcher.group(1); // Extract the ID captured by the regex
        } else {
            return null; // Handle case where no match is found
        }
    }
}



