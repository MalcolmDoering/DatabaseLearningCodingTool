package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.*;

import main.Globals;
import toolbox.DebugTools;
import toolbox.notification.NotificationHandler;

public class FileIO {

	public void loadData() {
		
		boolean success = loadData(Globals.getDataFileName());
		
		if (success == false) return;
		
		DebugTools.print("Loading data from " + Globals.getDataFileName());
		DebugTools.print("Total records: " + Globals.dataManager.getLastRecordID());
		
		NotificationHandler.notify(Globals.Notifications.LIST_SELECTION);
	}
	
	
	
	public boolean loadData(String filename) {
		
		filename = "data" + File.separator + filename;
		
		//dataManager.records.clear();
		
		File f = new File(filename);
		
		if (f.exists() == false) {
			return false;
		}
		
		System.out.println("Now loading from " + f.getAbsolutePath());
		
		
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(f));
//			String line;
//			boolean isFirst = true;
//			
//			while ((line = br.readLine()) != null) {
//				
//				if (isFirst) {
//					isFirst = false;
//					continue;
//				}
//				
//				if (line.trim().length() > 0) {
//					try {
//						Globals.dataManager.addRecord(Record.fromString(line.trim()));
//					} catch (NumberFormatException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
		
		
		Reader in;
		BufferedReader br;
		try {
			
//			in = new FileReader(f);
//			
//			Iterable<CSVRecord> csvRecords = CSVFormat.EXCEL.withQuote('"').withHeader().parse(in);
//			
//			for (CSVRecord csvRecord : csvRecords) {
//				Globals.dataManager.addRecord(Record.fromCSVRecord(csvRecord));
//			}
		
			br = new BufferedReader(new FileReader(filename));

			CSVParser parser = CSVParser.parse(br, CSVFormat.EXCEL.withQuote('"').withHeader());
			
			// get the headers...
			Map<String, Integer> headerMap = parser.getHeaderMap();
			ArrayList<String> headers = new ArrayList<String>();
			
			for (int i = 0; i < headerMap.size(); i++) {
				  headers.add(Integer.toString(0));
				}
			
			for (String header: headerMap.keySet()) {
				headers.set(headerMap.get(header), header);
			}
			
			Globals.dataManager.setHeaders(headers);
			
			// load the records...
			for (CSVRecord csvRecord : parser.getRecords()) {
				Globals.dataManager.addRecord(Record.fromCSVRecord(csvRecord));
			}	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return true;
	}
	
	

	public boolean saveData() {
		return saveData(Globals.getDataFileName());
		//return false;
	}
	
	
	public boolean saveData_old(String filename) {
		String extFilename = "data" + File.separator + filename;
		try {
			OutputStreamWriter fileWriter = new OutputStreamWriter(
					new FileOutputStream(extFilename),
					Charset.forName("UTF-8").newEncoder() 
					);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write(Record.generateHeader() + "\n");
			for (Record record:Globals.dataManager.getRecords()) {
				writer.write(record.toCSVString() + "\n");
			}
			writer.close();
			DebugTools.print("CSV File written: " + extFilename);
			return true;
		} catch (IOException e) {
			System.out.println("Could not create output file.");
			return false;
		}
	}
	
	public boolean saveData_string(String filename) {
		String extFilename = "data" + File.separator + filename;
		
		try {
			OutputStreamWriter fileWriter = new OutputStreamWriter(
					new FileOutputStream(extFilename),
					Charset.forName("UTF-8").newEncoder() 
					);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180);
	        
	        //csvPrinter.printRecord(this.getHeaders());
			
			for (Record record:Globals.dataManager.getRecords()) {
				csvPrinter.printRecord(record.toCSVString());
			}
			
	        csvPrinter.flush();
			
			writer.close();
			DebugTools.print("CSV File written: " + extFilename);
			return true;
		} catch (IOException e) {
			System.out.println("Could not create output file.");
			return false;
		}
	}
	
	
	public boolean saveData(String filename) {
		String extFilename = "data" + File.separator + filename;
		
		try {
			OutputStreamWriter fileWriter = new OutputStreamWriter(
					new FileOutputStream(extFilename),
					Charset.forName("UTF-8").newEncoder() 
					);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180);
	        
	        csvPrinter.printRecord(Globals.dataManager.getHeaders());
			
			for (Record record:Globals.dataManager.getRecords()) {
				csvPrinter.printRecord(record.toArrayList());
			}
			
	        csvPrinter.flush();
			
			writer.close();
			DebugTools.print("CSV File written: " + extFilename);
			return true;
		} catch (IOException e) {
			System.out.println("Could not create output file.");
			return false;
		}
	}
	
	
	public boolean saveResults(String filename, String results) {
		String extFilename = "data" + File.separator + filename;
		try {
			OutputStreamWriter fileWriter = new OutputStreamWriter(
					new FileOutputStream(extFilename),
					Charset.forName("UTF-8").newEncoder() 
					);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write(results);
			
			writer.close();
			DebugTools.print("CSV File written: " + extFilename);
			return true;
		} catch (IOException e) {
			System.out.println("Could not create output file: " + extFilename);
			return false;
		}
	}
	
	

//	public void loadCrossvalidatedResults(String filename) {
//		File f = new File("data" + File.separator + filename);
//		System.out.println("Now loading crossvalidated results from " + f.getAbsolutePath());
//		int recordIndex = Globals.dataManager.getLastRecordID() + 1;
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(f));
//			String line;
//			Record record = new Record();
//			int counter = 1;
//			while ((line = br.readLine()) != null) {
//				switch(counter) {
//				case 1:
//					record.action1 = lineToAction(line.trim());
//					++counter;
//					break;
//				case 2:
//					record.action2 = lineToAction(line.trim());
//					++counter;
//					break;
//				case 3:
//					record.action3 = lineToAction(line.trim());
//					++counter;
//					break;
//				case 4:
//					parseResponses(record, line.trim());
//					record.id = recordIndex;
//					Globals.dataManager.addRecord(record);
//					++recordIndex;
//					record = new Record();
//					counter = 1;
//					break;
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	

//	private void parseResponses(Record record, String line) {
//		// Example:
//		// "0","R","","","","","","","","","",
//		// "PRESENT_X CANON come and have a look at it and it's a very different kind of camera","",
//		// "PRESENT_X CANON can I ask if you'll be going freelance or if your organisation helping you to buy this","",
//		// "PRESENT_X CANON no problem and if you have old lenses next time you come in please bring them and we can test them with this camera","",
//		// "PRESENT_X CANON no problem and if you have old lenses next time you come in please bring them and we can test them with this camera","",
//		// "PRESENT_X CANON we have a brochure with the full specifications and out Cod if you would like that as well",""
//
//		String cleanLine = line.replaceAll("_X", "");
//		cleanLine = cleanLine.replaceAll("\"", "");
//		String[] fields = cleanLine.split(",");
//		if (fields[1].equals("R") == false) {
//			DebugTools.print("ERROR: Second field is not 'R'.");
//			DebugTools.print(line);
//		}
//		record.response[0] = fields[11];
//		record.response[1] = fields[13];
//		record.response[2] = fields[15];
//		record.response[3] = fields[17];
//		record.response[4] = fields[19];		
//	}
//	
//	
//
//	private String lineToAction(String line) {
//		// Example: "0","0","PRESENT_X","CANON","NONE","NONE","CANON","NONE","NONE","CANON","hi the how many colors do you have for this model"
//		// Customer from, to, location, Shopkeeper From, To, Location
//		line = line.replaceAll("\"", "");	
//		String[] fields = line.split(",");
//		StringBuilder sb = new StringBuilder();
//		String spatialString = "";
//		String spatialState = fields[2];
//		if (spatialState.equals("PRESENT_X")) {
//			// Present Canon
//			spatialString = "Present " + capitalize(fields[3]);
//
//		} else if (spatialState.equals("NONE")) {
//			// First check if this is an empty history slot
//			if (fields[4].equals("NONE")
//					&& fields[5].equals("NONE")
//					&& fields[6].equals("NONE")
//					&& fields[7].equals("NONE")
//					&& fields[8].equals("NONE")
//					&& fields[9].equals("NONE")) {
//				// Empty
//				spatialString = " ";
//			} else {
//				// Show locations or destinations of shopkeeper and customer
//				String customerLocation = fields[6];
//				if (customerLocation.equals("NONE")) {
//					customerLocation = "moving: " + capitalize(fields[4]) + " -> " + capitalize(fields[5]);
//				} else {
//					customerLocation = "at " + capitalize(fields[5]);
//				}
//				String shopkeeperLocation = fields[9];
//				if (shopkeeperLocation.equals("NONE")) {
//					shopkeeperLocation = "moving: " + capitalize(fields[7]) + " -> " + capitalize(fields[8]);
//				} else {
//					shopkeeperLocation = "at " + capitalize(fields[8]);
//				}
//				spatialString = "Customer " + customerLocation + ";Shopkeeper " + shopkeeperLocation;
//			}
//
//		} else if (spatialState.equals("FACE_TO_FACE")) {
//			String customerLocation = fields[6];
//			spatialString = "Face-to-face at " + capitalize(customerLocation);
//
//		} else if (spatialState.equals("WAITING")) {
//			// Show locations or destinations of shopkeeper and customer
//			String customerLocation = fields[6];
//			if (customerLocation.equals("NONE")) {
//				customerLocation = "moving: " + capitalize(fields[4]) + " -> " + capitalize(fields[5]);
//			} else {
//				customerLocation = "at " + capitalize(fields[5]);
//			}
//
//			spatialString = "Customer " + customerLocation + ";Shopkeeper waiting at Service Counter";
//		}
//		String utterance = fields[10];
//		if (utterance.equals("NONE")){
//			utterance = "...";
//		}
//		sb.append(spatialString).append(";").append(utterance);
//
//		return sb.toString();
//	}
	
	
	private String capitalize(final String line) {
		return Character.toUpperCase(line.charAt(0)) + line.toLowerCase().substring(1);
	}
}
