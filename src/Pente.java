import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

public class Pente extends DBApplet implements MouseListener {
  private static final long serialVersionUID = 1L;
  int[][] mainBoard = new int[19][19];
  int currentPlayer;
  int redCaptures = 0;
  int blueCaptures = 0;
  int redMove = 0;
  String message = "";
  Stack<GameState> undoStack;
  Stack<GameState> redoStack;
  final int UNOCCUPIED = 0;
  final int RED = 1;
  final int BLUE = -1;
  final int NEITHER = 0;

  public void init() {
    addMouseListener(this);
    for (int i = 0; i < 19; i++) {
      for (int j = 0; j < 19; j++) {
        this.mainBoard[i][j] = 0;
      }
    }
    this.currentPlayer = 1;
    this.undoStack = new Stack();
    this.redoStack = new Stack();
  }

  public void paint(Graphics g) {
    Color boardColor = new Color(255, 255, 150);
    Color bgColor = new Color(100, 0, 0);
    Color startButtonColor = new Color(220, 220, 220);
    Color messageColor = Color.white;
    Color undoColor = new Color(255, 50, 255);
    Color redoColor = new Color(50, 255, 50);
    setBackground(bgColor);

    g.setColor(bgColor);
    g.fillRect(0, 0, 550, 550);

    g.setColor(startButtonColor);
    g.fillRect(5, 5, 70, 25);
    g.fillRect(80, 5, 100, 25);
    g.setColor(undoColor);
    g.fillRect(460, 5, 40, 25);
    g.setColor(redoColor);
    g.fillRect(505, 5, 40, 25);

    g.setColor(Color.black);
    g.drawString("New Game", 10, 20);
    g.drawString("Computer Move", 85, 20);
    g.drawString("undo", 465, 20);
    g.drawString("redo", 510, 20);

    g.setColor(boardColor);
    g.fillRect(45, 45, 460, 460);
    g.setColor(Color.black);
    g.drawLine(44, 44, 44, 505);
    g.drawLine(45, 44, 45, 505);
    g.drawLine(44, 45, 505, 45);
    g.drawLine(44, 44, 505, 44);
    g.drawLine(505, 44, 505, 505);
    g.drawLine(504, 44, 504, 505);
    g.drawLine(45, 505, 505, 505);
    g.drawLine(45, 504, 505, 504);
    for (int i = 50; i <= 500; i += 25) {
      g.drawLine(i, 50, i, 500);
    }
    for (int i = 50; i <= 500; i += 25) {
      g.drawLine(50, i, 500, i);
    }
    g.drawLine(274, 50, 274, 500);
    g.drawLine(276, 50, 276, 500);
    g.drawLine(50, 274, 500, 274);
    g.drawLine(50, 276, 500, 276);
    g.setColor(boardColor);
    g.fillOval(121, 121, 8, 8);
    g.fillOval(121, 271, 8, 8);
    g.fillOval(121, 421, 8, 8);
    g.fillOval(196, 196, 8, 8);
    g.fillOval(196, 346, 8, 8);
    g.fillOval(271, 121, 8, 8);
    g.fillOval(271, 421, 8, 8);
    g.fillOval(346, 196, 8, 8);
    g.fillOval(346, 346, 8, 8);
    g.fillOval(421, 121, 8, 8);
    g.fillOval(421, 271, 8, 8);
    g.fillOval(421, 421, 8, 8);
    g.setColor(bgColor);
    g.fillOval(271, 271, 8, 8);
    g.setColor(Color.black);
    g.drawOval(121, 121, 8, 8);
    g.drawOval(121, 271, 8, 8);
    g.drawOval(121, 421, 8, 8);
    g.drawOval(271, 121, 8, 8);
    g.drawOval(271, 421, 8, 8);
    g.drawOval(196, 196, 8, 8);
    g.drawOval(196, 346, 8, 8);
    g.drawOval(346, 196, 8, 8);
    g.drawOval(346, 346, 8, 8);
    g.drawOval(421, 121, 8, 8);
    g.drawOval(421, 271, 8, 8);
    g.drawOval(421, 421, 8, 8);
    for (int i = 0; i < 19; i++) {
      for (int j = 0; j < 19; j++) {
        if (this.mainBoard[i][j] == 1) {
          g.setColor(Color.red);
          g.fillOval(i * 25 + 40, j * 25 + 40, 20, 20);
        }
        if (this.mainBoard[i][j] == -1) {
          g.setColor(Color.blue);
          g.fillOval(i * 25 + 40, j * 25 + 40, 20, 20);
        }
      }
    }
    g.setColor(Color.red);
    g.fillRect(290, 5, 20, 20);
    g.fillRect(375, 5, 20, 20);
    g.setColor(Color.blue);
    g.fillRect(310, 5, 20, 20);
    g.fillRect(395, 5, 20, 20);
    g.setColor(messageColor);
    g.drawString("Captures:", 230, 18);
    g.drawString("Move:", 340, 18);
    g.drawString(this.message, 275 - this.message.length() * 3, 40);
    g.drawString(Integer.toString(this.redCaptures), 296, 18);
    g.drawString(Integer.toString(this.blueCaptures), 316, 18);
    int x = 380;
    if (this.currentPlayer == -1) {
      x = 400;
    }
    g.fillOval(x, 10, 10, 10);
  }

  public void mouseReleased(MouseEvent me) {
    handleMouseRelease(me.getX(), me.getY());
  }

  public void mouseClicked(MouseEvent me) {
  }

  public void mousePressed(MouseEvent me) {
  }

  public void mouseEntered(MouseEvent me) {
  }

  public void mouseExited(MouseEvent me) {
  }

  private void handleMouseRelease(int mouseX, int mouseY) {
    if ((mouseX > 5) && (mouseX < 75) && (mouseY > 5) && (mouseY < 30)) {
      for (int i = 0; i < 19; i++) {
        for (int j = 0; j < 19; j++) {
          this.mainBoard[i][j] = 0;
        }
      }
      this.currentPlayer = 1;
      this.redCaptures = 0;
      this.blueCaptures = 0;
      this.redMove = 0;
      this.message = "";
      this.undoStack = new Stack();
      this.redoStack = new Stack();
    }
    if ((mouseX > 80) && (mouseX < 180) && (mouseY > 5) && (mouseY < 30)) {
      this.message = "Not yet implemented";
    }
    if ((mouseX > 45) && (mouseX < 505) && (mouseY > 45) && (mouseY < 505)) {
      int x = (mouseX - 38) / 25;
      int y = (mouseY - 38) / 25;
      if (this.currentPlayer == 0) {
        this.message = "Click \"New Game\" to start a new game.";
      } else if (this.mainBoard[x][y] == 0) {
        if ((this.currentPlayer == 1) && (this.redMove == 0)
            && ((x != 9) || (y != 9))) {
          this.message = "First move must be in the center.";
        } else if ((this.currentPlayer == 1) && (this.redMove == 1) && (x > 6)
            && (x < 12) && (y > 6) && (y < 12)) {
          this.message = "Second move must be at least 3 away from center.";
        } else {
          this.undoStack.push(new GameState(this.mainBoard, this.currentPlayer,
              this.redCaptures, this.blueCaptures, this.redMove));
          this.redoStack = new Stack();
          this.mainBoard[x][y] = this.currentPlayer;
          capture(x, y);
          if (this.currentPlayer == 1) {
            this.redMove += 1;
          }
          this.message = "";
          int winner = checkForWinner(this.mainBoard);
          if (winner == 1) {
            this.message = "Game Over. Red wins!";
          }
          if (winner == -1) {
            this.message = "Game Over. Blue wins!";
          }
          if (winner != 0) {
            this.currentPlayer = 0;
          } else {
            this.currentPlayer = (-this.currentPlayer);
          }
        }
      }
    }
    if ((mouseX > 460) && (mouseX < 500) && (mouseY > 5) && (mouseY < 30)) {
      undo();
    }
    if ((mouseX > 505) && (mouseX < 545) && (mouseY > 5) && (mouseY < 30)) {
      redo();
    }
    repaint();
  }

  private void undo() {
    if (this.undoStack.size() == 0) {
      this.message = "Can't undo";
    } else {
      this.redoStack.push(new GameState(this.mainBoard, this.currentPlayer,
          this.redCaptures, this.blueCaptures, this.redMove));
      this.mainBoard = copyBoard(((GameState) this.undoStack.peek()).getBoard());
      this.currentPlayer = ((GameState) this.undoStack.peek()).getTurn();
      this.redCaptures = ((GameState) this.undoStack.peek()).getRedCaptures();
      this.blueCaptures = ((GameState) this.undoStack.peek()).getBlueCaptures();
      this.redMove = ((GameState) this.undoStack.pop()).getRedMove();
      this.message = "";
    }
  }

  private void redo() {
    if (this.redoStack.size() == 0) {
      this.message = "Can't redo";
    } else {
      this.undoStack.push(new GameState(this.mainBoard, this.currentPlayer,
          this.redCaptures, this.blueCaptures, this.redMove));
      this.mainBoard = copyBoard(((GameState) this.redoStack.peek()).getBoard());
      this.currentPlayer = ((GameState) this.redoStack.peek()).getTurn();
      this.redCaptures = ((GameState) this.redoStack.peek()).getRedCaptures();
      this.blueCaptures = ((GameState) this.redoStack.peek()).getBlueCaptures();
      this.redMove = ((GameState) this.redoStack.pop()).getRedMove();
      this.message = "";
      int winner = checkForWinner(this.mainBoard);
      if (winner == 1) {
        this.message = "Game Over. Red wins!";
      }
      if (winner == -1) {
        this.message = "Game Over. Blue wins!";
      }
    }
  }

  private void capture(int x, int y) {
    int countCaptures = 0;
    if ((y > 2) && (this.mainBoard[x][(y - 1)] == -this.currentPlayer)
        && (this.mainBoard[x][(y - 2)] == -this.currentPlayer)
        && (this.mainBoard[x][(y - 3)] == this.currentPlayer)) {
      this.mainBoard[x][(y - 1)] = 0;
      this.mainBoard[x][(y - 2)] = 0;
      countCaptures++;
    }
    if ((x < 16) && (y > 2)
        && (this.mainBoard[(x + 1)][(y - 1)] == -this.currentPlayer)
        && (this.mainBoard[(x + 2)][(y - 2)] == -this.currentPlayer)
        && (this.mainBoard[(x + 3)][(y - 3)] == this.currentPlayer)) {
      this.mainBoard[(x + 1)][(y - 1)] = 0;
      this.mainBoard[(x + 2)][(y - 2)] = 0;
      countCaptures++;
    }
    if ((x < 16) && (this.mainBoard[(x + 1)][y] == -this.currentPlayer)
        && (this.mainBoard[(x + 2)][y] == -this.currentPlayer)
        && (this.mainBoard[(x + 3)][y] == this.currentPlayer)) {
      this.mainBoard[(x + 1)][y] = 0;
      this.mainBoard[(x + 2)][y] = 0;
      countCaptures++;
    }
    if ((x < 16) && (y < 16)
        && (this.mainBoard[(x + 1)][(y + 1)] == -this.currentPlayer)
        && (this.mainBoard[(x + 2)][(y + 2)] == -this.currentPlayer)
        && (this.mainBoard[(x + 3)][(y + 3)] == this.currentPlayer)) {
      this.mainBoard[(x + 1)][(y + 1)] = 0;
      this.mainBoard[(x + 2)][(y + 2)] = 0;
      countCaptures++;
    }
    if ((y < 16) && (this.mainBoard[x][(y + 1)] == -this.currentPlayer)
        && (this.mainBoard[x][(y + 2)] == -this.currentPlayer)
        && (this.mainBoard[x][(y + 3)] == this.currentPlayer)) {
      this.mainBoard[x][(y + 1)] = 0;
      this.mainBoard[x][(y + 2)] = 0;
      countCaptures++;
    }
    if ((x > 2) && (y < 16)
        && (this.mainBoard[(x - 1)][(y + 1)] == -this.currentPlayer)
        && (this.mainBoard[(x - 2)][(y + 2)] == -this.currentPlayer)
        && (this.mainBoard[(x - 3)][(y + 3)] == this.currentPlayer)) {
      this.mainBoard[(x - 1)][(y + 1)] = 0;
      this.mainBoard[(x - 2)][(y + 2)] = 0;
      countCaptures++;
    }
    if ((x > 2) && (this.mainBoard[(x - 1)][y] == -this.currentPlayer)
        && (this.mainBoard[(x - 2)][y] == -this.currentPlayer)
        && (this.mainBoard[(x - 3)][y] == this.currentPlayer)) {
      this.mainBoard[(x - 1)][y] = 0;
      this.mainBoard[(x - 2)][y] = 0;
      countCaptures++;
    }
    if ((x > 2) && (y > 2)
        && (this.mainBoard[(x - 1)][(y - 1)] == -this.currentPlayer)
        && (this.mainBoard[(x - 2)][(y - 2)] == -this.currentPlayer)
        && (this.mainBoard[(x - 3)][(y - 3)] == this.currentPlayer)) {
      this.mainBoard[(x - 1)][(y - 1)] = 0;
      this.mainBoard[(x - 2)][(y - 2)] = 0;
      countCaptures++;
    }
    if (this.currentPlayer == 1) {
      this.redCaptures += countCaptures;
    }
    if (this.currentPlayer == -1) {
      this.blueCaptures += countCaptures;
    }
  }

  private int checkForWinner(int[][] board) {
    if (this.redCaptures > 4) {
      return 1;
    }
    if (this.blueCaptures > 4) {
      return -1;
    }
    if (this.redMove < 5) {
      return 0;
    }
    for (int i = 0; i < 19; i++) {
      for (int j = 0; j < 19; j++) {
        if (board[i][j] != 0) {
          int winner = check5(board, i, j);
          if (winner != 0) {
            return winner;
          }
        }
      }
    }
    return 0;
  }

  private int check5(int[][] b, int x, int y) {
    int player = b[x][y];
    int n = 0;
    if (y < 15) {
      for (int i = 1; i < 5; i++) {
        if (b[x][(y + i)] != player) {
          break;
        }
        n++;
      }
    }
    if (n > 3) {
      return player;
    }
    n = 0;
    if ((x < 15) && (y < 15)) {
      for (int i = 1; i < 5; i++) {
        if (b[(x + i)][(y + i)] != player) {
          break;
        }
        n++;
      }
    }
    if (n > 3) {
      return player;
    }
    n = 0;
    if (x < 15) {
      for (int i = 1; i < 5; i++) {
        if (b[(x + i)][y] != player) {
          break;
        }
        n++;
      }
    }
    if (n > 3) {
      return player;
    }
    n = 0;
    if ((x > 3) && (y < 15)) {
      for (int i = 1; i < 5; i++) {
        if (b[(x - i)][(y + i)] != player) {
          break;
        }
        n++;
      }
    }
    if (n > 3) {
      return player;
    }
    return 0;
  }

  private int[][] copyBoard(int[][] board) {
    int[][] newBoard = new int[19][19];
    for (int i = 0; i < 19; i++) {
      for (int j = 0; j < 19; j++) {
        newBoard[i][j] = board[i][j];
      }
    }
    return newBoard;
  }

  private double minimax(int[][] board, int depth, double alpha, double beta,
      int player) {
    return 0.0D;
  }
}