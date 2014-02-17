package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Logger {
	private static final String DEFAULT_LOG_FILENAME = "appLog";
	private static final String LOG_EXTENSION = ".txt";
	private static final String FILEPATH = "logFiles/";
	
	public static void logMessage(String message){
		logMessage(message, DEFAULT_LOG_FILENAME);
	}
	
	public static void logMessage(String message, String logFilename){		
		try {
			File logFile = new File(FILEPATH + logFilename + LOG_EXTENSION);
			if (!logFile.exists()){
				logFile.createNewFile();
			}
			
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = fmt.format(new Date());
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(date + " --- " + message + "\r\n");
			writer.close();
		} catch (IOException e) {
			try {
				throw new Exception("Could not write to log file (something is really messed up).");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
