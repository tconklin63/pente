public class Move {
  private int xPos;
  private int yPos;
  private double heuristicScore;

  public Move(int x, int y) {
    this.xPos = x;
    this.yPos = y;
  }

  public Move(int x, int y, double hs) {
    this.xPos = x;
    this.yPos = y;
    this.heuristicScore = hs;
  }

  public int getXPos() {
    return this.xPos;
  }

  public int getYPos() {
    return this.yPos;
  }

  public double getHeuristicScore() {
    return this.heuristicScore;
  }

  public void setXPos(int x) {
    this.xPos = x;
  }

  public void setYPost(int y) {
    this.yPos = y;
  }

  public void setHeuristicScore(double hs) {
    this.heuristicScore = hs;
  }
}