package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.Globals;
import toolbox.DebugTools;
import toolbox.notification.NotificationHandler;

public class DataManager {
	
	private ArrayList<Record> records = new ArrayList<Record>();
	private HashMap<Integer,Record> recordMap = new HashMap<Integer,Record>();
	private Record currentRecord = new Record();
	private ArrayList<String> headers;
	
	private int previousTrialId = 0;
	private int currentTrialId = 0;
	private String previousAgentUtterance = "";
	private String currentConversationHistory = "";

	
	public void setHeaders(ArrayList<String> headers) {
		this.headers = headers;
	}
	
	public ArrayList<String> getHeaders() {
		return this.headers;
	}
	
	public int getLastRecordID() {
		if (records.isEmpty()) {
			return 0;
		}
		return records.get(records.size()-1).turnId;
	}
	
	// this is called when loading the data from csv
	// the turns have to be in order for the conversation history to be properly generated
	//
	public void addRecord(Record record) {
		
		currentTrialId = record.trialId;
		
		
		
		record.turnId = Globals.dataManager.getLastRecordID()+1;
		
		if (recordMap.containsKey(record.turnId)) {
			DebugTools.print("ERROR: Record with ID " + record.turnId + " already exists.");
		} 
		else {
			
			if (currentTrialId == previousTrialId) {
				
				currentConversationHistory += "Shopkeeper:  " + previousAgentUtterance + "\n\n";
				currentConversationHistory += "Customer:     " + record.action1 + "\n\n";
				
			}
			else {
				currentConversationHistory = "Customer:\t" + record.action1 + "\n\n";
				
			}
			
			record.conversationHistory = currentConversationHistory;
			
			records.add(record);
			recordMap.put(record.turnId, record);
			
			
			previousTrialId = currentTrialId;
			previousAgentUtterance = record.action2;
		}
	}

	public void clear() {
		records.clear();
	}

	public ArrayList<Record> getRecords() {
		return records;
	}

	public Record getCurrentRecord() {
		return currentRecord;
	}

	public void setSelectedRecord(int selectedIndex) {
		//DebugTools.print("Setting selected record index to " + selectedIndex);
		currentRecord = recordMap.get(selectedIndex);
		//DebugTools.print("Current record: " + currentRecord.toCSVString());
		NotificationHandler.notify(Globals.Notifications.LIST_SELECTION);
	}

	public Integer[] getRecordIDs() {
		
		Integer[] indices = new ArrayList<Integer>(recordMap.keySet()).toArray(new Integer[recordMap.size()]);
		return indices;
	}

	public int getCurrentRecordID() {
		if (currentRecord == null) {
			return -1;
		} else {
			return currentRecord.turnId;

		}
	}

	public Record getRecord(int id) {
		return recordMap.get(id);
	}
}
