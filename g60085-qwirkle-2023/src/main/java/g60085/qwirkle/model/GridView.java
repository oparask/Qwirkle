package g60085.qwirkle.model;

import java.util.Collections;

/**
 * GridView represents a view on the grid that it will not be possible to modify.
 */
public class GridView {
    private Grid grid;

    /**
     * Initializes the attribute grid.
     *
     * @param grid the game grid.
     */
    public GridView(Grid grid) {
        this.grid = grid;
    }

    /**
     * Gives access to the grid game by accessing a given tile.
     *
     * @param row tile row.
     * @param col tile column.
     * @return the requested tile.
     */
    public Tile get(int row, int col) {
        return this.grid.get(row, col);
    }

    /**
     * @return true if the grid game is empty and false otherwise.
     */
    public boolean isEmpty() {
        return this.grid == null;
    }
}
