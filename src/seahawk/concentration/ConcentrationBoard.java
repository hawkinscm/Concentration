package seahawk.concentration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConcentrationBoard {
  private static final int MIN_BOARD_SIZE = 2;
  private static final int MAX_BOARD_SIZE = 8;

  private final String[] board;
  private Random random = new Random();

  public ConcentrationBoard(int height, int width, List<String> words) {
    if (height < MIN_BOARD_SIZE || height > MAX_BOARD_SIZE || width < MIN_BOARD_SIZE || width > MAX_BOARD_SIZE) {
     throw new IllegalArgumentException("height and width must be between " + MIN_BOARD_SIZE + " and " + MAX_BOARD_SIZE +
         ": height=" + height + " width=" + width);
    }

    int boardSize = height * width;
    if (boardSize != words.size() * 2) {
      throw new IllegalArgumentException("height times width must match the two times the number of words given" +
         ": height=" + height + " width=" + width + " wordCount=" + words.size());
    }

    this.board = new String[boardSize];
    addWordsToRandomBoardLocations(words);
  }

  private void addWordsToRandomBoardLocations(List<String> words) {
    List<String> randomizedList = new ArrayList<String>(words.size());
    for (String word : words) {
      randomizedList.add(random.nextInt(randomizedList.size() + 1), word);
      randomizedList.add(random.nextInt(randomizedList.size() + 1), word);
    }

    for (int idx = 0; idx<= words.size(); idx++) {
      randomizedList.toArray(board);
    }
  }

  public boolean isMatch(int index1, int index2) {
    return board[index1].equals(board[index2]);
  }
}
