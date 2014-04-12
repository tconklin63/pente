public class GameState {
  int[][] board;
  int turn;
  int redCaptures;
  int blueCaptures;
  int redMove;

  public GameState(int[][] b, int t, int rc, int bc, int rm) {
    this.board = new int[19][19];
    for (int i = 0; i < 19; i++) {
      for (int j = 0; j < 19; j++) {
        this.board[i][j] = b[i][j];
      }
    }
    this.turn = t;
    this.redCaptures = rc;
    this.blueCaptures = bc;
    this.redMove = rm;
  }

  public int[][] getBoard() {
    return this.board;
  }

  public int getTurn() {
    return this.turn;
  }

  public int getRedCaptures() {
    return this.redCaptures;
  }

  public int getBlueCaptures() {
    return this.blueCaptures;
  }

  public int getRedMove() {
    return this.redMove;
  }
}