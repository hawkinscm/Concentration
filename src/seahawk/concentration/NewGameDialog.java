package seahawk.concentration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class NewGameDialog extends CustomDialog {
	private static final long serialVersionUID = 1L;

	private static final String[] SIZES = {"4x3", "4x4", "5x4", "6x5", "6x6"};
	private static final Integer[] WORD_COUNTS = {6, 8, 10, 15, 18};

  private JComboBox<String> sizeComboBox;
  private JComboBox<Integer> wordCountComboBox;
	private JPanel wordInputPanel;
  private JTextField imageFileTextField;
  private JTextField saveFileTextField;
  private boolean accepted = false;

  private BufferedImage selectedPuzzleImage;

	public NewGameDialog(final JFrame gui) {
		super(gui, "New");

		c.insets = new Insets(7, 7, 7, 7);
    c.anchor = GridBagConstraints.WEST;

		sizeComboBox = new JComboBox<>(SIZES);
    wordCountComboBox = new JComboBox<>(WORD_COUNTS);

    c.ipadx = 30;
    sizeComboBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Board Size:"));
    sizeComboBox.addItemListener(e -> {
      if (e.getStateChange() == ItemEvent.SELECTED) {
        wordCountComboBox.setSelectedIndex(sizeComboBox.getSelectedIndex());
      }
    });
		getContentPane().add(sizeComboBox, c);

		c.gridx++;
    c.ipadx = 50;
    wordCountComboBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "# Of Matches:"));
    wordCountComboBox.addItemListener(e -> {
      if (e.getStateChange() == ItemEvent.SELECTED) {
        sizeComboBox.setSelectedIndex(wordCountComboBox.getSelectedIndex());
        setWordInputPanel((Integer) wordCountComboBox.getSelectedItem());
      }
    });
		getContentPane().add(wordCountComboBox, c);

		c.gridx = 0;
    c.gridy++;
    c.ipadx = 0;
    c.gridwidth = 2;
    JPanel imageSelectionPanel = createPuzzleImageSelectionPanel();
    getContentPane().add(imageSelectionPanel, c);

    c.gridx = 0;
    c.gridy++;
    c.ipadx = 275;
    wordInputPanel = new JPanel();
    wordInputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Enter words/phrases for the matching game:"));
		getContentPane().add(wordInputPanel, c);

    c.gridy++;
    c.ipadx = 0;
    JPanel savePanel = createSavePanel();
    getContentPane().add(savePanel, c);

		c.gridy++;
    c.anchor = GridBagConstraints.CENTER;
    c.gridwidth = 1;
    CustomButton okButton = new CustomButton("OK") {
      @Override
      public void buttonClicked() {
        if (validateAndProcessInput()) {
          accepted = true;
          dispose();
        }
      }
    };
    getContentPane().add(okButton, c);

		sizeComboBox.setSelectedIndex(2);

		imageFileTextField.requestFocus();
	}

  private JPanel createPuzzleImageSelectionPanel() {
    JPanel imageSelectionPanel = new JPanel();
    imageSelectionPanel.setBorder(BorderFactory.createTitledBorder("Select the puzzle image"));
    imageSelectionPanel.setLayout(new GridBagLayout());
    GridBagConstraints panelC = new GridBagConstraints();

    panelC.insets = new Insets(5, 5, 5, 5);
    panelC.ipadx = 400;
    panelC.gridx = 0;
    panelC.gridy = 0;
    imageFileTextField = new JTextField();
    imageSelectionPanel.add(imageFileTextField, panelC);

    panelC.gridx++;
    panelC.ipadx = 0;
    panelC.fill = GridBagConstraints.NONE;
    CustomButton selectPuzzleImageButton = new CustomButton("...") {
      @Override
      public void buttonClicked() {
        JFileChooser imageChooser = new JFileChooser(".");
        imageChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif"));
        imageChooser.setDialogTitle("Choose a puzzle image");
        int result = imageChooser.showDialog(this, "OK");

        if (result == JFileChooser.APPROVE_OPTION) {
          imageFileTextField.setText(imageChooser.getSelectedFile().getAbsolutePath());
        }
      }
    };
    imageSelectionPanel.add(selectPuzzleImageButton, panelC);
    return imageSelectionPanel;
  }

  private JPanel createSavePanel() {
    JPanel savePanel = new JPanel();
    savePanel.setBorder(BorderFactory.createTitledBorder("Save"));
    savePanel.setLayout(new GridBagLayout());
    GridBagConstraints panelC = new GridBagConstraints();

    panelC.insets = new Insets(5, 5, 5, 5);
    panelC.ipadx = 400;
    panelC.gridx = 0;
    panelC.gridy = 0;
    saveFileTextField = new JTextField();
    savePanel.add(saveFileTextField, panelC);

    panelC.gridx++;
    panelC.ipadx = 0;
    panelC.fill = GridBagConstraints.NONE;
    CustomButton selectPuzzleImageButton = new CustomButton("...") {
      @Override
      public void buttonClicked() {
        JFileChooser saveChooser = new JFileChooser(".");
        saveChooser.setFileFilter(new FileNameExtensionFilter("Concentration Files", "con"));
        int result = saveChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
          String filePath = saveChooser.getSelectedFile().getAbsolutePath();
          if (!filePath.endsWith(".con")) {
            filePath += ".con";
          }
          saveFileTextField.setText(filePath);
        }
      }
    };
    savePanel.add(selectPuzzleImageButton, panelC);
    return savePanel;
  }

  private void setWordInputPanel(int wordCount) {
    wordInputPanel.setLayout(new GridLayout(wordCount, 1, 5, 5));
    while (wordInputPanel.getComponentCount() > wordCount) {
      wordInputPanel.remove(wordInputPanel.getComponentCount() - 1);
    }

    while (wordInputPanel.getComponentCount() < wordCount) {
      JTextField wordTextField = new JTextField();
      wordInputPanel.add(wordTextField);
    }

    refresh();
  }

  private boolean validateAndProcessInput() {
    String errorMessage = "";
    try {
      if (imageFileTextField.getText().trim().isEmpty()) {
        errorMessage += "Puzzle image not selected.";
      }
      else {
        selectedPuzzleImage = ImageIO.read(new File(imageFileTextField.getText()));
      }
    }
    catch (Exception e) {
      errorMessage += "Unable to read selected puzzle image.";
    }

    Set<String> wordList = getWordList();
    if (wordList.size() < (Integer) wordCountComboBox.getSelectedItem()) {
      if (!errorMessage.isEmpty()) {
        errorMessage += "\n";
      }
      errorMessage += "Not all words/phrases provided or duplicates used.";
    }

    if (!errorMessage.isEmpty()) {
      Messenger.error(errorMessage, "Invalid Input", this);
      return false;
    }

    String savePath = saveFileTextField.getText();
    if (savePath.trim().isEmpty()) {
      int result = JOptionPane.showConfirmDialog(this, "No save file selected. Continue without saving?", "No Save", JOptionPane.YES_NO_OPTION);
      if (result != JOptionPane.YES_OPTION) {
        return false;
      }
    }
    else {
      return saveGame(savePath);
    }

    return true;
  }

  private boolean saveGame(String savePath) {
    String filePath = savePath;
    if (!filePath.endsWith(".con")) {
      filePath += ".con";
    }

    try {
      File file = new File(filePath);
      //noinspection ResultOfMethodCallIgnored
      file.createNewFile();
      try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {

        writer.write(imageFileTextField.getText());
        writer.newLine();

        for (String word : getWordList()) {
          writer.write(word);
          writer.newLine();
        }

        writer.flush();
      }

      return true;
    }
    catch (IOException e) {
      Messenger.error("Unable to write to specified file", "Unable To Save");
      return false;
    }
  }

  public boolean isAccepted() {
    return accepted;
  }

  public int getColumnCount() {
    return Integer.parseInt(sizeComboBox.getSelectedItem().toString().substring(0, 1));
  }

  public int getRowCount() {
    return Integer.parseInt(sizeComboBox.getSelectedItem().toString().substring(2, 3));
  }

  public BufferedImage getSelectedPuzzleImage() {
    return selectedPuzzleImage;
  }

  public Set<String> getWordList() {
    Set<String> wordList = new HashSet<>(wordInputPanel.getComponentCount());
    for (Component component : wordInputPanel.getComponents()) {
      String text = ((JTextField) component).getText().trim();
      if (!text.isEmpty()) {
        wordList.add(text);
      }
    }
    return wordList;
  }
}
