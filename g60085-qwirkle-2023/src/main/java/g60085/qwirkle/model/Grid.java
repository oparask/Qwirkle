package g60085.qwirkle.model;

/**
 * Grid represents the game grid.
 */
public class Grid {
    private final Tile[][] tiles;
    private boolean isEmpty;

    /**
     * Initializes the attribute tiles by creating an array of size 91x91.
     */
    public Grid() {
        this.tiles = new Tile[91][91];
        this.isEmpty = true;
    }

    /**
     * Gives access to the attribute tiles.
     *
     * @param row the row of a tile in the array.
     * @param col the column of a tile in the array.
     * @return the tile located at the row and column passed as parameters
     * or null if the requested position is outside the grid game.
     */
    public Tile get(int row, int col) {
        if (row > 91 || row < 0 || col > 91 || col < 0) {
            return null;
        }
        return this.tiles[row][col];
    }

    /**
     * Adds the first tiles respecting the rules of the game by placing them “in the middle” of the game board.
     * This method can only be used once when there are no tiles on the grid game.
     *
     * @param d    the direction of tiles placement.
     * @param line tiles to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public void firstAdd(Direction d, Tile... line) throws QwirkleException {
        if (!this.isEmpty) {
            throw new QwirkleException("The grid game is not empty!");
        }
        directionValidation(d);
        qwirkleExceptionLine(line);
        if (line.length > 1) {
            verifyColorShape(line);
        }
        int row = 45;
        int col = 45;
        this.tiles[row][col] = line[0];
        for (int i = 1; i < line.length; i++) {
            row = row + d.getDeltaRow();
            col = col + d.getDeltaCol();
            this.tiles[row][col] = line[i];
        }
        isEmpty();
    }

    /**
     * Adds a tile to a certain position respecting the rules of the game.
     *
     * @param row  position row.
     * @param col  position column.
     * @param tile the tile to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public void add(int row, int col, Tile tile) throws QwirkleException {
        qwirkleExceptionRowCol(row, col);
        qwirkleExceptionLine(tile);
        this.tiles[row][col] = tile;
    }

    /**
     * Adds multiple tiles at a certain position in a given direction.
     *
     * @param row  the row of the first tile position.
     * @param col  the column of the first tile position.
     * @param d    the direction of tiles placement.
     * @param line tiles to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public void add(int row, int col, Direction d, Tile... line) throws QwirkleException {
        qwirkleExceptionRowCol(row, col);
        directionValidation(d);
        if (row + line.length > 91 || row - line.length < 0 || col + line.length > 91 || col - line.length < 0) {
            throw new QwirkleException("Above the limit");
        }
        qwirkleExceptionLine(line);
        if (line.length > 1) {
            verifyColorShape(line);
        }

        this.tiles[row][col] = line[0];
        for (int i = 1; i < line.length; i++) {
            row = row + d.getDeltaRow();
            col = col + d.getDeltaCol();
            if (this.tiles[row][col] != null) {
                throw new QwirkleException("there is already a tile at this position");
            }
            this.tiles[row][col] = line[i];
        }
    }

    /**
     * Adds tiles at "any" position on the grid.
     *
     * @param line the different tiles in different position to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public void add(TileAtPosition... line) throws QwirkleException {
        if (line == null || line.length > 6 ) {
            throw new QwirkleException("Invalid number of tiles");
        }

        Tile[] lineTiles = new Tile[line.length];
        for (int i = 0; i < line.length; i++) {
            lineTiles[i] = line[i].tile();
        }
        if (line.length > 1) {
            verifyColorShape(lineTiles);
        }

        for (int i = 0; i < line.length; i++) {
            if (line[i].row() > 91 || line[i].row() < 0 || line[i].col() > 91 || line[i].col() < 0) {
                throw new QwirkleException("Outside the grid");
            }
            if (line[i].row() == 45 && line[i].col() == 45) {
                throw new QwirkleException("at this position you must call the firtAdd method");
            }
            if (this.tiles[line[i].row()][line[i].col()] != null) {
                throw new QwirkleException("there is already a tile at this position");
            }
            this.tiles[line[i].row()][line[i].col()] = line[i].tile();
        }
    }

    /**
     * Verifies if the direction is valid or not.
     *
     * @param d direction.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private void directionValidation(Direction d) throws QwirkleException {
        if (d == null) {
            throw new QwirkleException("Invalid direction!");
        }
    }

    /**
     * Verifies if the tiles have the same color.
     *
     * @param tiles an array of tiles to verify.
     * @return true if the tiles are of the same color or false otherwise.
     */
    public boolean sameColor(Tile[] tiles) {
        int i = 1;
        while (i < tiles.length && tiles[0].color() == tiles[i].color()) {
            i++;
        }
        return i >= tiles.length;
    }

    /**
     * Verifies if the tiles have the same shape.
     *
     * @param tiles an array of tiles to verify.
     * @return true if the tiles are of the same shape or false otherwise.
     */
    public boolean sameShape(Tile[] tiles) {
        int i = 1;
        while (i < tiles.length && tiles[0].shape() == tiles[i].shape()) {
            i++;
        }
        return i >= tiles.length;
    }

    /**
     * Verifies if there are 2 times the same tile.
     *
     * @param tiles an array of tiles to verify.
     * @return true if there are 2 times the same tile or false otherwise.
     */
    public boolean sameTile(Tile[] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = i + 1; j < tiles.length; j++) {
                if (tiles[i].equals(tiles[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the tiles passed in parameters are of the same color or shape.
     *
     * @param line an array of tiles.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private void verifyColorShape(Tile... line) throws QwirkleException {
        if (!sameColor(line)) {
            if (!sameShape(line)) {
                throw new QwirkleException("Tiles must be of the same color or shape");
            }
        }
        if (sameTile(line)) {
            throw new QwirkleException("Cannot have the same tile twice");
        }
    }

    /**
     * Checks if the row and col parameters are valid or not.
     *
     * @param row the row of a tile position.
     * @param col the column of a tile position.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private void qwirkleExceptionRowCol(int row, int col) throws QwirkleException {
        if (row > 91 || row < 0 || col > 91 || col < 0) {
            throw new QwirkleException("Outside the grid");
        }
        if (this.isEmpty) {
            throw new QwirkleException("The grid game is empty! You must call the firtAdd method");
        }
        if (this.tiles[row][col] != null) {
            throw new QwirkleException("there is already a tile at this position");
        }
    }

    /**
     * Checks if the tiles passed in parameters are valid or not.
     *
     * @param line an array of tiles.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private void qwirkleExceptionLine(Tile... line) throws QwirkleException {
        if (line == null || line.length > 6) {
            throw new QwirkleException("Invalid number of tiles!");
        }
        int i = 0;
        while (i < line.length && line[i] != null) {
            i++;
        }
        if (i < line.length) {
            throw new QwirkleException("Some tiles are not initialized!");
        }
    }

    /**
     * Initialize the "isEmpty" attribute to false in order to show that the game grid is no longer empty.
     */
    public void isEmpty() {
        this.isEmpty = false;
    }

}
