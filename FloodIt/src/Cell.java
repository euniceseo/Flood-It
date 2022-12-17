import java.awt.*;
import java.util.ArrayList;

// Represents a single square of the game area
public class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  // Design choice --> we made it so the x and y are the top left corners of each tile. so the first
  // tile would be 0,0
  private int x;
  private int y;
  private Color color;
  private boolean flooded;

  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  // initialized as all null at first
  Cell(int x, int y, Color color, boolean flooded) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;

    // we initialized all the neighboring cells as null at first, and change them
    // through a method later. if the method conditions don't satisfy, it stays null.
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
  }

  // drawCell method
  public WorldImage drawCell() {
    WorldImage drawCell = new RectangleImage(20, 20, OutlineMode.SOLID, this.color);

    return drawCell;
  }

  // prevents field of field access, checks if a field is flooded
  boolean isFlooded() {
    return this.flooded;
  }

  // called on one singular cell to change boolean to true if all conditions of both
  // scenarios are met for cell to be flooded. if met, it adds it to the worklist floodedTrueCells
  public void floodedBooleanChange(Color oldColor,
                                   Color clickedColor, ArrayList<Cell> floodedTrueCells) {
    if (oldColor.equals(clickedColor)) {
      this.flooded = true;
    }

    // checking for left
    if (this.left != null
            && ((!this.left.isFlooded() && this.left.color.equals(clickedColor))
            || (this.left.isFlooded() && !this.left.color.equals(clickedColor)))) {
      this.left.flooded = true;
      floodedTrueCells.add(this.left);
    }

    // checking for right
    if (this.right != null
            && ((!this.right.isFlooded() && this.right.color.equals(clickedColor))
            || (this.right.isFlooded() && !this.right.color.equals(clickedColor)))) {
      this.right.flooded = true;
      floodedTrueCells.add(this.right);
    }

    // checking for top
    if (this.top != null
            && ((!this.top.isFlooded() && this.top.color.equals(clickedColor))
            || (this.top.isFlooded() && !this.top.color.equals(clickedColor)))) {
      this.top.flooded = true;
      floodedTrueCells.add(this.top);
    }

    // checking for bottom
    if (this.bottom != null
            && ((!this.bottom.isFlooded() && this.bottom.color.equals(clickedColor))
            || (this.bottom.isFlooded() && !this.bottom.color.equals(clickedColor)))) {
      this.bottom.flooded = true;
      floodedTrueCells.add(this.bottom);
    }
  }
}
