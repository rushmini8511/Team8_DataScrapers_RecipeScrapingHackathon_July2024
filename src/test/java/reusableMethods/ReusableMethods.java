/*package reusableMethods;

import java.util.List;

import utilities.ExcelUtilities;

public class ReusableMethods {
	
	String path = "C:\\DataScraping\\Elimination List.xlsx";
	
	public boolean checkForEliminatedIng(List<String> Ingredients, String sheetName) {
		ExcelUtilities xlutils = new ExcelUtilities(path);
		boolean containsEliminatedIng = false;
		
		List <String> eliminationList = xlutils.getEliminatedList(path, 0, sheetName);
		for(String Ing: eliminationList ) {
			
			if(Ingredients.contains(Ing)) {
				containsEliminatedIng = true;
				break;
			}
			else {
				containsEliminatedIng = false;
			}
		}
		return containsEliminatedIng;
		
	}

}*/
	
