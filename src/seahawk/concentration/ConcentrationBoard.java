package seahawk.concentration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ConcentrationBoard {
  public static final BoardSize[] BOARD_SIZES = {
     new BoardSize(4, 3),
     new BoardSize(4, 4),
     new BoardSize(5, 4),
     new BoardSize(6, 5),
     new BoardSize(6, 6)
  };

  private Random random = new Random();

  private BoardSize boardSize;
  private final String[] board;

  public ConcentrationBoard(Set<String> words) {
    this.boardSize = getBoardSize(words.size());
    if (boardSize == null) {
      throw new IllegalArgumentException("invalid number of words given: " + words.size());
    }

    this.board = new String[words.size() * 2];
    addWordsToRandomBoardLocations(words);
  }

  private BoardSize getBoardSize(int wordCount) {
    for (BoardSize definedBoardSize : BOARD_SIZES) {
      if (definedBoardSize.getWordCount() == wordCount) {
        return definedBoardSize;
      }
    }
    return null;
  }

  private void addWordsToRandomBoardLocations(Set<String> words) {
    List<String> randomizedList = new ArrayList<>(words.size());
    for (String word : words) {
      randomizedList.add(random.nextInt(randomizedList.size() + 1), word);
      randomizedList.add(random.nextInt(randomizedList.size() + 1), word);
    }

    for (int idx = 0; idx <= words.size(); idx++) {
      randomizedList.toArray(board);
    }
  }

  public int getColumnCount() {
    return boardSize.getColumnCount();
  }

  public int getRowCount() {
    return boardSize.getRowCount();
  }

  public String[] getWords() {
    return board;
  }

  public boolean isMatch(int index1, int index2) {
    return board[index1].equals(board[index2]);
  }

  public static class BoardSize {
    private final int columnCount;
    private final int rowCount;

    private BoardSize(int columnCount, int rowCount) {
      this.columnCount = columnCount;
      this.rowCount = rowCount;
    }

    public int getColumnCount() {
      return columnCount;
    }

    public int getRowCount() {
      return rowCount;
    }

    public int getWordCount() {
      return (columnCount * rowCount) / 2;
    }
  }
}
