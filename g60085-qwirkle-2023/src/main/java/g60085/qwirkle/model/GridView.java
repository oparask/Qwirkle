package g60085.qwirkle.model;

import java.util.Collections;

public class GridView {
    private Grid grid;

    public GridView(Grid grid) {
        this.grid = grid;
    }

    public Tile get(int row, int col) {
        return this.grid.get(row, col);
    }

    public boolean isEmpty() {
        return this.grid == null;
    }
}
