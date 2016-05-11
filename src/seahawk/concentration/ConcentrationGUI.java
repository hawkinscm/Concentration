package seahawk.concentration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Set;

public class ConcentrationGUI extends JFrame {
  private static final long serialVersionUID = 1L;

  private static BufferedImage icon = getTransparentImage("concen.png");

  private char keyBuffer = 0;

  public ConcentrationGUI() {
    super("CONCENTRATION");

    this.setIconImage(icon);
    this.setLocation(200, 50);
    this.setSize(800, 800);
    this.setMinimumSize(new Dimension(800, 800));
    this.setResizable(true);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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
            buttonSelected("" + keyBuffer + e.getKeyChar());
          }
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

    this.getContentPane().removeAll();

    int row = newGameDialog.getRowCount();
    int column = newGameDialog.getColumnCount();

    this.getContentPane().setLayout(new GridLayout(row, column));

    Set<String> wordList = newGameDialog.getWordList();
    for (int idx = 0; idx < wordList.size(); idx++) {
      CustomButton button = new CustomButton("" + (idx + 1)) {
        @Override
        public void buttonClicked() {
          buttonSelected(this.getText());
        }
      };
      button.setName(button.getText());
      button.setFocusable(false);
      this.getContentPane().add(button);
    }

    this.pack();
  }

  private void loadGame() {

  }

  private void buttonSelected(String buttonNumber) {
    Messenger.display(buttonNumber, "INFO", this);
    keyBuffer = 0;
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
