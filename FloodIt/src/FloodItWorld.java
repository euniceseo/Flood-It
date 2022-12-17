import java.util.ArrayList;

import java.util.Arrays;

import java.awt.Color;
import java.util.Random;

// floodItWorld class
class FloodItWorld {

  // constants
  static int BOARD_SIZE = 10;
  static int SIZE = 20;
  static int WIDTH = BOARD_SIZE * SIZE;
  static int HEIGHT = BOARD_SIZE * SIZE;
  static int COLOR_AMOUNT = 6;

  // all the cells of the game
  ArrayList<ArrayList<Cell>> board;

  // list of cells that are set to true in flooded, used for waterfall effect
  ArrayList<Cell> floodedTrueCells;

  // clicked color based on the position given by mouse click
  Color clickedColor;

  // number of clicks so far
  int totalClicks;

  // max clicks needed to not lose the game, hard coded at 35
  int maxClicks = 20;

  // time
  double time = 0;

  // constructor so you know what the colors are, called generateBoard and connectCells
  FloodItWorld(ArrayList<ArrayList<Cell>> board) {
    this.generateBoard();
    this.connectCells();
    this.floodedTrueCells = new ArrayList<Cell>();
    floodedTrueCells.add(this.board.get(0).get(0));
  }

  // constructor for calling generateBoard
  FloodItWorld() {
    this.generateBoard();
    this.connectCells();
    this.floodedTrueCells = new ArrayList<Cell>();
    floodedTrueCells.add(this.board.get(0).get(0));
  }

  // random constructor for color testing
  Random rand = new Random();

  // constructor for testing randomness
  FloodItWorld(Random rand) {
    this.generateBoard();
    this.connectCells();
    this.floodedTrueCells = new ArrayList<Cell>();
    floodedTrueCells.add(this.board.get(0).get(0));
    this.rand = rand;
  }

  // constructor for worldEnds testing
  FloodItWorld(int totalClicks, int maxClicks) {
    this.totalClicks = totalClicks;
    this.maxClicks = maxClicks;
  }


  // generates board, goes through all board
  void generateBoard() {
    this.board = new ArrayList<ArrayList<Cell>>();

    for (int i = 0; i < BOARD_SIZE; i++) {
      ArrayList<Cell> row = new ArrayList<Cell>();

      for (int j = 0; j < BOARD_SIZE; j++) {

        // if top left you add a cell with this.flooded = true
        if (i == 0 && j == 0) {
          Cell cell = new Cell(i, j, randColor(), true);
          clickedColor = cell.color;
          row.add(cell);
        }
        else {
          Cell cell = new Cell(i, j, randColor(), false);
          row.add(cell);
        }
      }
      this.board.add(row);
    }
  }

  // method for randomizing color, checks for boardsize to make sure the game isn't
  // too difficult/impossible to win
  public Color randColor() {

    // list of possible colors, max is 8
    ArrayList<Color> colors = new ArrayList<Color> (Arrays.asList
            (Color.RED,
                    Color.ORANGE,
                    Color.YELLOW,
                    Color.GREEN,
                    Color.BLUE,
                    Color.PINK,
                    Color.BLACK,
                    Color.CYAN));

    int numColor = rand.nextInt(COLOR_AMOUNT);
    Color color = colors.get(numColor);

    if (BOARD_SIZE == 2) {
      numColor = rand.nextInt(1);
      color = colors.get(numColor);
    }

    if (BOARD_SIZE == 3) {
      numColor = rand.nextInt(2);
      color = colors.get(numColor);
    }

    if (BOARD_SIZE == 4) {
      numColor = rand.nextInt(2);
      color = colors.get(numColor);
    }

    if (BOARD_SIZE == 5) {
      numColor = rand.nextInt(3);
      color = colors.get(numColor);
    }

    if (BOARD_SIZE == 6) {
      numColor = rand.nextInt(4);
      color = colors.get(numColor);
    }

    if (BOARD_SIZE == 7) {
      numColor = rand.nextInt(5);
      color = colors.get(numColor);
    }

    if (BOARD_SIZE >= 8) {
      numColor = rand.nextInt(COLOR_AMOUNT);
      color = colors.get(numColor);
    }

    return color;
  }

  // makeScene method, places cells in their respective conditions alongside the text and title and score
  public WorldScene makeScene() {
    WorldImage background = new RectangleImage(BOARD_SIZE * 50, BOARD_SIZE * 50, OutlineMode.SOLID, Color.white);

    // base case empty image
    WorldScene base = new WorldScene(BOARD_SIZE * 20, BOARD_SIZE * 20);

    base.placeImageXY(new TextImage("Flood It Game", 30, Color.PINK), 250, 340);
    base.placeImageXY(new TextImage("Score: " + Integer.toString(totalClicks) + " /" + Integer.toString(maxClicks) + " Timer: " + (int)this.time, 15, Color.BLACK), 250, 380);

    for (ArrayList<Cell> arr : board) {
      // WorldImage base = new EmptyImage();

      for (Cell cell : arr) {
        base.placeImageXY(cell.drawCell(), cell.x * 20 + 10, cell.y * 20 + 10);
      }
    }

    return base;
  }

  // connectCells method to connect each cell in each given direction. set it
  // to void because if the conditions don't satisfy we want it to return a null value
  public void connectCells() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {

        Cell currentCell = this.board.get(i).get(j);

        // connectLeft
        if (j - 1 >= 0) { // if i = 1,
          currentCell.left = this.board.get(i).get(j - 1);
        }

        // connectRight
        if (j + 1 < this.board.size()) {
          currentCell.right = this.board.get(i).get(j + 1);
        }

        // connectBottom
        if (i + 1 < this.board.size()) {
          currentCell.bottom = this.board.get(i + 1).get(j);
        }

        // connectTop
        if (i - 1 >= 0) {
          currentCell.top = this.board.get(i - 1).get(j);
        }
      }
    }
  }

  // checks if the entire board is the same color
  public boolean checkSameColor() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        if (this.board.get(0).get(0).color != this.board.get(i).get(j).color) {
          return false;
        }
      }
    }
    return true;
  }

  // gets cell clicked at a given posn location
  public Cell getCellClicked(Posn posn) {
    int x = Math.floorDiv(posn.x, 20);
    int y = Math.floorDiv(posn.y, 20);

    return this.board.get(x).get(y);
  }


  // gets cell clicked at this position and sets clickedColor ro it
  public void onMouseClicked(Posn posn) {
    clickedColor = this.getCellClicked(posn).color;
    this.floodedTrueCells.add(this.board.get(0).get(0));
    ArrayList<Cell> floodedTrueCellsCopy = new ArrayList<Cell>(this.floodedTrueCells);

    // increments clicks to account for win/loss
    totalClicks++;
  }

  // on tick method
  public void onTick() {

    ArrayList<Cell> floodedTrueCellsCopy = new ArrayList<Cell>();

    for (Cell c : floodedTrueCells) {
      c.color = clickedColor;
      c.floodedBooleanChange(c.color, clickedColor, floodedTrueCellsCopy);
    }

    this.floodedTrueCells.clear();
    this.floodedTrueCells.addAll(floodedTrueCellsCopy);

    // increments time
    this.time = time + 0.1;
  }

  // ends world for when user loses or wins
  public WorldEnd worldEnds() {
    if (totalClicks > maxClicks) {
      return new WorldEnd(true, this.loseScene());
    }

    if (checkSameColor() && (totalClicks <= maxClicks)) {
      return new WorldEnd(true, this.winScene());
    }

    else {
      return new WorldEnd(false, this.winScene());
    }
  }

  // losing screen
  public WorldScene loseScene() {
    WorldScene endScene = new WorldScene(500, 500);
    endScene.placeImageXY(new TextImage("You lose!", Color.BLACK), 250, 400);
    return endScene;
  }

  // winning screen
  public WorldScene winScene() {
    WorldScene endScene = new WorldScene(500, 500);
    endScene.placeImageXY(new TextImage("You win! Press 'r' to reset", Color.BLACK), 250, 400);
    return endScene;
  }

  // resets the game when you click key 'r'
  public void onKeyEvent(String key) {
    if (key.equalsIgnoreCase("r")) {
      this.board = new ArrayList<ArrayList<Cell>>();
      this.generateBoard();
      this.connectCells();
      this.totalClicks = 0;
    }
  }
}






