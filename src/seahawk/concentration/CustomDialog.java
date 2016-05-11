
package seahawk.concentration;

import javax.swing.*;
import java.awt.*;

/**
 * Standard dialog customized for better functionality and easier use.
 */
public class CustomDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The current graphics constraints.
	 */
	public GridBagConstraints c;

	/**
	 * Creates a new CustomDialog dialog with the given title text.
	 * @param title the text for the dialog title
	 */
	public CustomDialog(String title) {
		super();
		setTitle(title);
		init();
	}
	
	/**
	 * Creates a new CustomDialog dialog with the given GUI frame owner and title text.
	 * @param owner frame that contains/owns this dialog
	 * @param title the text for the dialog title
	 */
	public CustomDialog(Frame owner, String title) {
		super(owner, title);
		init();
	}

	/**
	 * Creates a new CustomDialog dialog with the given GUI dialog owner and title text.
	 * @param owner dialog that contains/owns this dialog
	 * @param title the text for the dialog title
	 */
	public CustomDialog(Dialog owner, String title) {
		super(owner, title);
		init();
	}
	
	/**
	 * Initializes and displays GUI controls.
	 */
	private void init() {
		setModal(true);
		
		setResizable(false);
		getContentPane().setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
	}
	
	/**
	 * Refreshes the display to fit the dialog title and GUI controls and to center the dialog in the application. 
	 */
	public void refresh() {
		pack();
		int titleWidth = (int)(getFontMetrics(getFont()).stringWidth(getTitle()) * 1.5);
		setMinimumSize(new Dimension(titleWidth, 50));
		setLocationRelativeTo(this.getOwner());
	}
}
