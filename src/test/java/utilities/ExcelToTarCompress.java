package utilities;



import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class ExcelToTarCompress {
	
	
	public static void main(String[] args) {
		String path =("./src/test/resources/Data/ScrappedRecipes_List.xlsx");
		String output = ("./src/test/resources/Team8_DataScrapers_RecipeScrapingHackathon.tar");
		createTarFile(path, output);
	}
	
	
			
	

   
    
    public static void createTarFile(String excelFilePath, String tarFilePath) {
        try (FileOutputStream fos = new FileOutputStream(tarFilePath);
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos)) {

            File excelFile = new File(excelFilePath);
            addFileToTar(tarOut, excelFile, "");

        } catch (IOException e) {
            e.printStackTrace();
        }
        }
        private static void addFileToTar(TarArchiveOutputStream tarOut, File file, String parent) throws IOException {
            String entryName = parent + file.getName();
            TarArchiveEntry tarEntry = new TarArchiveEntry(file, entryName);
            tarOut.putArchiveEntry(tarEntry);

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    tarOut.write(buffer, 0, len);
                }
            }

            tarOut.closeArchiveEntry();
        }
    }


