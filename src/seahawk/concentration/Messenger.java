
package seahawk.concentration;

import javax.swing.*;
import java.awt.*;

/**
 * Static-type class for displaying simple messages (Custom JOptionPane message dialogs).
 */
public class Messenger {

	/**
	 * Displays a message dialog with a title centered in the parent for the user to read; 
	 * will be a JOptionPane type of message; closes with an OK button.
	 * @param message message to display
	 * @param title title for the message dialog
	 * @param parent GUI component where this message dialog will be centered; if null, centered on the screen
	 * @param messageType the JOptionPane type of message this is
	 */
	private static void showMessage(String message, String title, Component parent, int messageType) {
		JOptionPane.showMessageDialog(parent, message, title, messageType);
	}
	
	/**
	 * Displays a message dialog with a title centered in the parent for the user to read; closes with an OK button.
	 * @param message message to display
	 * @param title title for the message dialog
	 * @param parent GUI component where this message dialog will be centered; if null, centered on the screen
	 */
	public static void display(String message, String title, Component parent) {
		showMessage(message, title, parent, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Displays a message dialog with a title centered on the screen for the user to read; closes with an OK button.
	 * @param message message to display
	 * @param title title for the message dialog
	 */
	public static void display(String message, String title) {
		showMessage(message, title, null, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Displays an error dialog with a title centered in the parent for the user to read; closes with an OK button.
	 * @param message error message to display
	 * @param title title for the error dialog
	 * @param parent GUI component where this error dialog will be centered; if null, centered on the screen
	 */
	public static void error(String message, String title, Component parent) {
		showMessage(message, title, parent, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Displays an error dialog with a title centered on the screen for the user to read; closes with an OK button.
	 * @param message error message to display
	 * @param title title for the error dialog
	 */
	public static void error(String message, String title) {
		showMessage(message, title, null, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Displays an error dialog with a title centered on the screen for the user to read; closes with an OK button.
	 * @param ex exception that was thrown which will be printed to system error
	 * @param message error message to display
	 * @param title title for the error dialog
	 */
	public static void error(Exception ex, String message, String title) {
		ex.printStackTrace();
		showMessage(message, title, null, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Displays an warning dialog with a title centered in the parent for the user to read; closes with an OK button.
	 * @param message warning message to display
	 * @param title title for the error dialog
	 * @param parent GUI component where this warning dialog will be centered; if null, centered on the screen
	 */
	public static void warn(String message, String title, Component parent) {
		showMessage(message, title, parent, JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Displays a warning dialog with a title centered on the screen for the user to read; closes with an OK button.
	 * @param message warning message to display
	 * @param title title for the warning dialog
	 */
	public static void warn(String message, String title) {
		showMessage(message, title, null, JOptionPane.WARNING_MESSAGE);
	}
}
