package seahawk.concentration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class ConcentrationGUI extends JFrame {
  private static final long serialVersionUID = 1L;

  private static BufferedImage icon = getTransparentImage("concen.png");

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
  }

  private void createMenuBar() {
    JMenu gameMenu = new JMenu("Game");
    gameMenu.setMnemonic('g');

    JMenuItem newGameMenuItem = new JMenuItem("New Game");
    newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
    gameMenu.add(newGameMenuItem);

    JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
    gameMenu.add(loadGameMenuItem);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(gameMenu);
    this.setJMenuBar(menuBar);
  }

  private void newGame() {

  }

  private void loadGame() {

  }

  /*public void loadGame(ClueGameData gameData) {
    this.getContentPane().removeAll();
    ComponentListener[] storyMenuItem;
    int cardCountMenuItem = (storyMenuItem = this.getContentPane().getComponentListeners()).length;

    for (int cardsMenuItem = 0; cardsMenuItem < cardCountMenuItem; ++cardsMenuItem) {
      ComponentListener notepadMenuItem = storyMenuItem[cardsMenuItem];
      this.getContentPane().removeComponentListener(notepadMenuItem);
    }
  }*/

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new ConcentrationGUI();
        /*String prompt = "Would you like to host or join a CLUE game?";
        String[] options = new String[]{"Host", "Join", "Exit Program"};
        int result = JOptionPane.showOptionDialog(null, prompt, "Custom CLUE v1.2", 1, 3, icon, options, "Host");
        if (result == 0) {
          new HostGUI();
        }
        else if(result == 1) {
          new ParticipantGUI();
        }*/
      }
    });
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
