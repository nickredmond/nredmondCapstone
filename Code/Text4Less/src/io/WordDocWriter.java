package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.Main;

public class WordDocWriter {
	private String documentMetadata;
	private List<String> translations;
	
	private final String OPEN_PAGE_TAGS = "<wx:sect><w:p><w:r><w:t>";
	private final String CLOSE_PAGE_TAGS = "</w:t></w:r></w:p></wx:sect>";
	private final String LINE_BREAK_TAG = "<w:br/>";
	private final String PAGE_BREAK_TAGS = "<w:p><w:r><w:br w:type=\"page\" /></w:r></w:p>";
	private final String CLOSE_DOC_TAGS = "</w:body></w:wordDocument>";
	
	private final String LINE_DELIMITER = "\r\n";
	
	public static final String DOC_EXTENSION = ".doc";
	
	public WordDocWriter() throws IOException{
		translations = new ArrayList<String>();
		try {
			addMetadata();
		} catch (IOException e) {
			Logger.logMessage("Could not read metadata for Word document (Source: WordDocWriter); Error Message: " + e.getMessage());
			throw e;
		}
	}
	
	private void addMetadata() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(new File(new Main().getWorkingDirectory() + "/metadataFiles/wordDocMetadata.txt")));
		
		String nextLine = "";
		documentMetadata = "";
		
		while ((nextLine = reader.readLine()) != null){
			documentMetadata += nextLine;
		}
		
		reader.close();
	}
	
	public void addImageTranslation(String translation){
		translations.add(translation);
	}
	
	public void addTranslations(List<String> translations){
		this.translations.addAll(translations);
	}
	
	public void clearTranslations(){
		translations.clear();
	}
	
	public void writeDocument(File documentFile) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(documentFile));
		writer.write(documentMetadata);
		
		for (int i = 0; i < translations.size(); i++){
			writer.write(OPEN_PAGE_TAGS);
			
			String nextPage = translations.get(i);
			String[] lines = nextPage.split(LINE_DELIMITER);
			
			for (int j = 0; j < lines.length; j++){
				writer.write(lines[j]);
				
				if (j < lines.length - 1){
					writer.write(LINE_BREAK_TAG);
				}
			}
			
			writer.write(CLOSE_PAGE_TAGS);
			
			if (i < translations.size() - 1){
				writer.write(PAGE_BREAK_TAGS);
			}
		}
		
		writer.write(CLOSE_DOC_TAGS);
		writer.close();
	}
}
