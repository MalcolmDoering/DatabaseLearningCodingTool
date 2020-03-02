package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.DataManager;
import data.FileIO;
import gui.ListPanel;
import toolbox.notification.NotificationHandler;

public class Globals {

	public static int REVIEWER_ID = 2;
	
	// 1 - from RSS - two prediction conditions
	// 2 - for THMS - three prediction conditions
	public static int BATCH_NUMBER = 458;
	
	public static boolean SINGLE_CODER = false;
	
	public static int MIN_BATCH_NUMBER_FOR_ANALYSIS = 458;
	public static int MAX_BATCH_NUMBER_FOR_ANALYSIS = 458;
	
	public static int NUM_PREDICTION_CONDITIONS = 3;
	public static String conditionNames[] = {"PROPOSED", "BASELINE1", "COREQA"}; // , "With Memory Perceptron"
	//public static String conditionNames[] = {"Proposed", "Proposed -state", "NMF", "RNN", "LSTM", "NearNeig"};
	
	public static List<Integer> trialsToEvaluate = new ArrayList<>(Arrays.asList(106, 107, 108, 109, 111, 114, 115, 116, 117, 118,
																				 122, 124, 126, 127, 131, 132, 134, 136, 137, 141,
																				 154, 156, 157, 161, 165, 174, 183, 193, 194, 201,
																				 202, 203, 204, 205, 206));
										
	//public static String RAW_DATA_FILE = "predictedresult_crossvalidated.csv";
	//public static String DATA_FILE = "results.csv";
	public static String STATS_FILE = "statistics.csv";
	
	public static FileIO fileIO;
	public static DataManager dataManager;
	public static ListPanel listPanel;
	
	public enum Notifications {LIST_SELECTION, LIST_CONTENTS}
	
	public static void initialize() {
		NotificationHandler.init(Notifications.values());
		fileIO = new FileIO();
		dataManager = new DataManager();
	}

	public static void shutdown() {
		NotificationHandler.destroy();
	}

	public static String getDataFileName() {
		return "predictions-" + BATCH_NUMBER + ".csv";
	}

}
