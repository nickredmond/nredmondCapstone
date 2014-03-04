package threading;

import io.Logger;

import java.awt.Frame;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class UncaughtExceptionHandler extends ThreadGroup{

	public UncaughtExceptionHandler() {
		super("UncaughtExceptionHandler");
	}

	@Override
	public void uncaughtException(Thread t, Throwable e){
		Logger.logMessage("Unknown error occured while running application. Message: \"" + e.toString() + "\"; Source: " + e.getStackTrace()[0].toString());

		SwingUtilities.invokeLater(new Thread(){
			public void run(){
				JOptionPane.showMessageDialog(null, "Due to an unknown error, the application must terminate. For more information, \nview the application log file " +
						"or contact the developer at nicholas.redmond@ymail.com. \nSorry for the inconvenience.", "Unknown Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		});
	}
}