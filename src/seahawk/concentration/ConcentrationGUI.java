package seahawk.concentration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConcentrationGUI extends JFrame {
  private static final long serialVersionUID = 1L;

  private static BufferedImage icon = getTransparentImage("concen.png");

  private char keyBuffer = 0;
  private List<CustomButton> buttonList = new ArrayList<>();
  private boolean isDisplayingMatch = false;
  private int selectedButtonIndex = -1;
  private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

  private ConcentrationBoard concentrationBoard;

  public ConcentrationGUI() {
    super("CONCENTRATION");

    this.setIconImage(icon);
    this.setLocation(200, 50);
    this.setSize(800, 800);
    this.setMinimumSize(new Dimension(800, 800));
    this.setResizable(true);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    UIManager.put("textInactiveText", new ColorUIResource(Color.BLACK));

    this.createMenuBar();

    this.setVisible(true);

    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (isNumeric(e.getKeyChar())) {
          if (keyBuffer == 0) {
            keyBuffer = e.getKeyChar();
          }
          else {
            selectButton(Integer.parseInt("" + keyBuffer + e.getKeyChar()) - 1);
          }
        }
        else {
          keyBuffer = 0;
        }
      }
    });
  }

  private boolean isNumeric(char key) {
    return key >= '0' && key <= '9';
  }

  private void createMenuBar() {
    JMenu gameMenu = new JMenu("Game");
    gameMenu.setMnemonic('g');

    JMenuItem newGameMenuItem = new JMenuItem("New Game");
    newGameMenuItem.addActionListener(e -> newGame());
    newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
    gameMenu.add(newGameMenuItem);

    JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
    loadGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
    loadGameMenuItem.addActionListener(e -> loadGame());
    gameMenu.add(loadGameMenuItem);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(gameMenu);
    this.setJMenuBar(menuBar);
  }

  private void newGame() {
    NewGameDialog newGameDialog = new NewGameDialog(this);
    newGameDialog.setVisible(true);
    if (!newGameDialog.isAccepted()) {
      return;
    }

    ConcentrationBoard board = new ConcentrationBoard(newGameDialog.getWordList());
    loadBoard(newGameDialog.getSelectedPuzzleImage(), board);
  }

  private void loadGame() {
    JFileChooser gameChooser = new JFileChooser(".");
    gameChooser.setFileFilter(new FileNameExtensionFilter("Concentration Files", "con"));
    gameChooser.setDialogTitle("Select Concentration File");
    int result = gameChooser.showDialog(this, "OK");

    if (result == JFileChooser.APPROVE_OPTION) {
      try (BufferedReader reader = new BufferedReader(new FileReader(gameChooser.getSelectedFile()))) {
        BufferedImage puzzleImage = ImageIO.read(new File(reader.readLine()));
        Set<String> wordList = new HashSet<>();
        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
          if (!nextLine.trim().isEmpty()) {
            wordList.add(nextLine);
          }
        }

        ConcentrationBoard board = new ConcentrationBoard(wordList);
        loadBoard(puzzleImage, board);
      }
      catch (Exception e) {
        Messenger.error(e, "Unable to load Concentration file", "Loading Error");
      }
    }
  }

  private void loadBoard(BufferedImage puzzleImage, ConcentrationBoard board) {
    keyBuffer = 0;
    selectedButtonIndex = -1;
    isDisplayingMatch = false;

    this.getContentPane().removeAll();
    buttonList.clear();

    BackgroundPanel backgroundPanel = new BackgroundPanel(puzzleImage);
    this.getContentPane().add(backgroundPanel);

    int row = board.getRowCount();
    int column = board.getColumnCount();
    concentrationBoard = board;

    backgroundPanel.setLayout(new GridLayout(row, column));

    String[] wordList = board.getWords();
    for (int idx = 0; idx < wordList.length; idx++) {
      CustomButton button = new CustomButton("" + (idx + 1)) {
        @Override
        public void buttonClicked() {
          selectButton(buttonList.indexOf(this));
        }
      };
      button.setFont(new Font("Arial", Font.PLAIN, 20));
      button.setFocusable(false);
      backgroundPanel.add(button);
      buttonList.add(button);
    }

    this.pack();
  }

  private synchronized void selectButton(int buttonIndex) {
    keyBuffer = 0;

    if (isDisplayingMatch || buttonIndex < 0 || buttonIndex >= buttonList.size()) {
      return;
    }

    CustomButton selectedButton = buttonList.get(buttonIndex);
    selectedButton.setEnabled(false);
    selectedButton.setText(getHtmlWrappedText(concentrationBoard.getWords()[buttonIndex]));

    if (selectedButtonIndex < 0) {
      selectedButtonIndex = buttonIndex;
    }
    else {
      isDisplayingMatch = true;
      executor.schedule(createMatchEvaluationTask(buttonIndex, selectedButton), 1, TimeUnit.SECONDS);
    }
  }

  private String getHtmlWrappedText(String text) {
    return "<html><center><font color=black>" + text + "</font></center></html>";
  }

  private Runnable createMatchEvaluationTask(int buttonIndex, CustomButton selectedButton) {
    return () -> {
      CustomButton previouslySelectedButton = buttonList.get(selectedButtonIndex);
      if (concentrationBoard.isMatch(buttonIndex, selectedButtonIndex)) {
        selectedButton.setVisible(false);
        previouslySelectedButton.setVisible(false);
      }
      else {
        selectedButton.setEnabled(true);
        selectedButton.setText("" + (buttonIndex + 1));
        previouslySelectedButton.setEnabled(true);
        previouslySelectedButton.setText("" + (selectedButtonIndex + 1));
      }

      selectedButtonIndex = -1;
      isDisplayingMatch = false;
    };
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(ConcentrationGUI::new);
  }

  private static BufferedImage getTransparentImage(String filename) {
    BufferedImage image;
    try {
      image = ImageIO.read(ConcentrationGUI.class.getResource(filename));
    }
    catch (Exception e) {
      Messenger.error(e.getMessage(), filename);
      return null;
    }

    final int width = image.getWidth();
    final int height = image.getHeight();
    BufferedImage transparentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = transparentImage.createGraphics();
    g.setComposite(AlphaComposite.Src);
    g.drawImage(image, null, 0, 0);
    g.dispose();
    for(int i = 0; i < height; i++) {
      for(int j = 0; j < width; j++) {
        if(transparentImage.getRGB(j, i) == Color.WHITE.getRGB()) {
          transparentImage.setRGB(j, i, 0x8F1C1C);
        }
      }
    }
    return transparentImage;
  }
}
