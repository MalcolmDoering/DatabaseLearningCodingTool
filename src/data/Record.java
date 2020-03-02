package data;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import main.Globals;
import toolbox.DebugTools;
import org.apache.commons.csv.*;


public class Record {
	public CSVRecord csvRecord;

	public enum Rating {GOOD, 
						BAD, 
						UNSURE,
						INCOMPLETE}
	
	static String DELIMITER = ",";
	
	public int turnId = 0;
	public int trialId = 0;
	
	public int customerUtteranceId = 0;
	public int agentUtteranceId = 0;
	
	public boolean isAmbiguous = false;
	
	public int dbId;
	public String customerLocation;
	
	public String action1 = ""; // the customer's action
	public String action2 = ""; // the agent's action
	
	// contains the predictions in each of the conditions
	public String response[] = new String[Globals.NUM_PREDICTION_CONDITIONS];
	
	//public int customerActionId = 0;
	//public int trueAgentActionId = 0;
	
	//public int predictedActionIds[] = new int[Globals.NUM_PREDICTION_CONDITIONS];
	
	public Rating rater1[] = new Rating[Globals.NUM_PREDICTION_CONDITIONS]; // the first person's rating
	public Rating rater2[] = new Rating[Globals.NUM_PREDICTION_CONDITIONS]; // the second person's rating
	
	public String conversationHistory = "";
	
	
	public String toCSVString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(turnId).append(DELIMITER);
		sb.append(trialId).append(DELIMITER);
		sb.append(customerUtteranceId).append(DELIMITER);
		sb.append(agentUtteranceId).append(DELIMITER);
		sb.append(((Boolean)isAmbiguous).toString().toUpperCase()).append(DELIMITER);
		sb.append("\"" + action1 + "\"").append(DELIMITER);
		sb.append("\"" + action2 + "\"").append(DELIMITER);
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			sb.append("\"" + response[i] + "\"").append(DELIMITER);	
		}
		
		//sb.append(customerActionId).append(DELIMITER);
		//sb.append(trueAgentActionId).append(DELIMITER);
		
		//for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
		//	sb.append(predictedActionIds[i]).append(DELIMITER);	
		//}
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			sb.append(rater1[i]).append(DELIMITER);	
		}
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			sb.append(rater2[i]).append(DELIMITER);	
		}
		
		sb.deleteCharAt(sb.length()-1); // remove the last delimiter
		
		return sb.toString();
	}
	
	
	public ArrayList<String> toArrayList() {
		Map<String, String> recordMap = this.csvRecord.toMap();
		
		if (Globals.REVIEWER_ID == 1) {
			for (int i = 0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
				
				recordMap.put(
						Globals.conditionNames[i] + "_CORRECT_RATER1",
						this.rater1[i].toString());
			}
		} else if (Globals.REVIEWER_ID == 2) {
			for (int i = 0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
				
				recordMap.put(
						Globals.conditionNames[i] + "_CORRECT_RATER2",
						this.rater2[i].toString());
			}
		}
		
		ArrayList<String> recordList = new ArrayList<String>();
		
		for (int i=0; i<Globals.dataManager.getHeaders().size(); i++) {
			recordList.add(recordMap.get(Globals.dataManager.getHeaders().get(i)));
		}
		
		return recordList;
	}
	
	
	/***********************************************************************************
	public static Record fromString(String csvString) {
		
		String cleanCSV = csvString.replaceAll("\"", "");
		StringTokenizer st = new StringTokenizer(cleanCSV, DELIMITER);
		Record record = new Record();
		
		record.turnId = Integer.parseInt(st.nextToken());
		System.out.println(record.turnId);
		record.trialId = Integer.parseInt(st.nextToken());
		
		record.customerUtteranceId = Integer.parseInt(st.nextToken());
		record.agentUtteranceId = Integer.parseInt(st.nextToken());
		
		String isAmbiguous = st.nextToken();
		record.isAmbiguous = Boolean.parseBoolean(isAmbiguous.toUpperCase());
		
		record.action1 = st.nextToken();
		record.action2 = st.nextToken();
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			record.response[i] = st.nextToken();
		}
		
		//record.customerActionId = Integer.parseInt(st.nextToken());
		//record.trueAgentActionId = Integer.parseInt(st.nextToken());
		
		//for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
		//	record.predictedActionIds[i] = Integer.parseInt(st.nextToken());
		//}
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			record.rater1[i] = getRating(st.nextToken());
		}
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			record.rater2[i] = getRating(st.nextToken());
		}

		return record;
	}
	************************************************************************************/
	
	
	public static Record fromCSVRecord(CSVRecord csvRecord) {
		
		Record record = new Record();
		
		record.csvRecord = csvRecord;
		
		record.turnId = Integer.parseInt(csvRecord.get("DISPLAY_ID"));
		record.trialId = Integer.parseInt(csvRecord.get("TRIAL"));
		record.dbId = Integer.parseInt(csvRecord.get("DATABASE_ID"));
		
		String outputShopkeeperLocation = csvRecord.get("OUTPUT_SHOPKEEPER_LOCATION");
		String outputSpatialState = csvRecord.get("OUTPUT_SPATIAL_STATE");
		String outputStateTarget = csvRecord.get("OUTPUT_STATE_TARGET");
		String shopkeeperSpeech = csvRecord.get("SHOPKEEPER_SPEECH");
		record.customerLocation = csvRecord.get("CUSTOMER_LOCATION");
		String customerSpeech = csvRecord.get("CUSTOMER_SPEECH");
		
		// customer action
		record.action1 = ""; 
		//record.action1 += "\n\t";
		record.action1 += "At " + record.customerLocation;
		
		record.action1 += "\n\t";
		
		if (customerSpeech.length() > 0) {
			record.action1 += "\"" + customerSpeech + "\"";
		}
		else {
			record.action1 += "(no speech)";
		}
		
		//+"\n\t"+ csvRecord.get("CUSTOMER_SPEECH");
		
		
		// agent action
		record.action2 = "";
		//record.action2 += "\n\t";
		
		if (outputSpatialState.equals("WAITING") || outputSpatialState.equals("FACE_TO_FACE")) {
			record.action2 += outputSpatialState +" at "+ outputShopkeeperLocation;
		}
		else if (outputSpatialState.equals("PRESENT_X")) {
			record.action2 += "PRESENT " + outputStateTarget +" at "+ outputShopkeeperLocation;
		}
		
		record.action2 += "\n\t";
		
		if (shopkeeperSpeech.length() > 0) {
			record.action2 += "\"" + shopkeeperSpeech + "\"";
		}
		else {
			record.action2 += "(no speech)";
		}
		
		
		// predicted actions
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			
			String predShopkeeperSpeech = csvRecord.get(Globals.conditionNames[i] + "_PRED_SHOPKEEPER_SPEECH");
			String predOutputSpatialState = csvRecord.get(Globals.conditionNames[i] + "_PRED_OUTPUT_SPATIAL_STATE");
			String predOutputStateTarget = csvRecord.get(Globals.conditionNames[i] + "_PRED_OUTPUT_STATE_TARGET");
			String predOutputShopkeeperLocation = csvRecord.get(Globals.conditionNames[i] + "_PRED_OUTPUT_SHOPKEEPER_LOCATION");
			
			record.response[i] = "";
			
			if (predOutputSpatialState.equals("WAITING") || predOutputSpatialState.equals("FACE_TO_FACE")) {
				record.response[i] += predOutputSpatialState +" at "+ predOutputShopkeeperLocation;
			}
			else if (predOutputSpatialState.equals("PRESENT_X")) {
				record.response[i] += "PRESENT " + predOutputStateTarget +" at "+ predOutputShopkeeperLocation;
			}
			
			record.response[i] += "\n";
			
			if (predShopkeeperSpeech.length() > 0) {
				record.response[i] += "\"" + predShopkeeperSpeech + "\"";
			}
			else {
				record.response[i] += "(no speech)";
			}
		}
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			record.rater1[i] = getRating(csvRecord.get(Globals.conditionNames[i] + "_CORRECT_RATER1"));
		}
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			record.rater2[i] = getRating(csvRecord.get(Globals.conditionNames[i] + "_CORRECT_RATER2"));
		}

		return record;
	}

	
	private static Rating getRating(String string) {
		Rating rating;
		try {
			rating = Rating.valueOf(string);
		} catch (Exception e) {
			rating = Rating.INCOMPLETE;
		}
		return rating;
	}
	
	
	public static String generateHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("Turn ID").append(DELIMITER);
		sb.append("Trial ID").append(DELIMITER);
		sb.append("Customer Utterance ID").append(DELIMITER);
		sb.append("Agent Utterance ID").append(DELIMITER);
		sb.append("Is Ambiguous").append(DELIMITER);
		sb.append("Customer Utterance").append(DELIMITER);
		sb.append("True Agent Utterance").append(DELIMITER);
		
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			sb.append(Globals.conditionNames[i] + " Utterance").append(DELIMITER);
		}
		
		//sb.append("Customer Action").append(DELIMITER);
		//sb.append("True Agent Action").append(DELIMITER);
		
		//for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
		//	sb.append(Globals.conditionNames[i] + " Action").append(DELIMITER);
		//}
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			sb.append("Rater1-" + Integer.toString(i+1)).append(DELIMITER);
		}
		
		for (int i=0; i<Globals.NUM_PREDICTION_CONDITIONS; i++) {
			sb.append("Rater2-" + Integer.toString(i+1)).append(DELIMITER);
		}
		
		sb.deleteCharAt(sb.length()-1); // remove the last delimiter
		
		return sb.toString();
	}
	
	
	
//	public boolean isRepetition(int index) {
//		if (trialId == 0) return false;
//		
//		String actionUtterance = action2.substring(action2.lastIndexOf(";")+1);
//		
//		String thisUtterance = response[index].split(" ", 3)[2];
//		
//		
//		if (thisUtterance.equals(actionUtterance)
//				|| thisUtterance.equals("NONE") && actionUtterance.equals("...")) {
//			//				DebugTools.print("Repetition! Record " + id + ":" + index);
//			//				//System.out.println("A1: " + actionUtterances.get(0));
//			//				System.out.println("A2: " + actionUtterances.get(0));
//			//				//System.out.println("A3: " + actionUtterances.get(2));
//			//				System.out.println("  : " + thisUtterance);
//			//				System.out.println();
//			return true;
//		}
//
//		return false;
//	}
	
	
}
