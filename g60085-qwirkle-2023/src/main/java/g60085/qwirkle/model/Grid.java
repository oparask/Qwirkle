package g60085.qwirkle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Grid represents the game grid.
 */
public class Grid {
    private Tile[][] line;
    private boolean isEmpty;

    /**
     * Initializes the attribute tiles by creating an array of size 91x91.
     */
    public Grid() {
        this.line = new Tile[91][91];
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
        return this.line[row][col];
    }

    /**
     * Adds the first tiles respecting the rules of the game by placing them “in the middle” of the game board.
     * This method can only be used once when there are no tiles on the grid game.
     *
     * @param d    the direction of tiles placement.
     * @param line tiles to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public int firstAdd(Direction d, Tile... line) throws QwirkleException {
        // Checks that the grid is empty;
        if (!isEmpty()) {
            throw new QwirkleException("The grid is not empty!");
        }
        // Checks the line, tiles and direction;
        if (!validLine(line)) {
            throw new QwirkleException("Invalid number of tiles!");
        }
        if (!validDirection(d)) {
            throw new QwirkleException("Invalid direction!");
        }
        if (!validTile(line)) {
            throw new QwirkleException("Some tiles are not initialized!");
        }
        // Checks if there is a common characteristic between tiles;
        if (line.length > 1) {
            if (!validColorOrShape(line)) {
                throw new QwirkleException("Tiles must share the same color or shape!");
            }
            if (sameTile(line)) {
                throw new QwirkleException("You cannot add the same tile!");
            }
        }
        // We can add the tiles;
        int row = 45;
        int col = 45;
        this.line[row][col] = line[0];
        for (int i = 1; i < line.length; i++) {
            row = row + d.getDeltaRow();
            col = col + d.getDeltaCol();
            this.line[row][col] = line[i];
        }
        this.isEmpty = false; // Set the "empty" attribute to false;
        return (line.length) == 6 ? line.length + 6 : line.length;
    }

    /**
     * Adds a tile to a certain position respecting the rules of the game.
     *
     * @param row  the row of the tile that we want to add.
     * @param col  the column of the tile that we want to add.
     * @param tile the tile that we want to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public int add(int row, int col, Tile tile) throws QwirkleException {
        if (isEmpty()) {
            throw new QwirkleException("The grid game is empty! You must call the firstAdd method!");
        }
        if (!validPosition(row, col, Direction.RIGHT, tile)) { // The direction doesn't matter;
            throw new QwirkleException("Position outside the grid!");
        }
        if (!validLine(tile)) {
            throw new QwirkleException("Invalid number of tiles!");
        }
        if (!validTile(tile)) {
            throw new QwirkleException("Some tiles are not initialized!");
        }
        if (existAlreadyTile(row, col, Direction.RIGHT, tile)) {
            throw new QwirkleException("There is already a tile at this position!");
        }
        if (!adjacentTiles(row, col, Direction.RIGHT, tile)) {
            throw new QwirkleException("The tiles are not connected to another tile!");
        }
        if (!validRulesAdd(row, col, Direction.RIGHT, tile)) {
            throw new QwirkleException("Does not respect the rules of qwirkle game!");
        }
        // We can add the tiles;
        this.line[row][col] = tile;
        return score(row, col, Direction.RIGHT, tile);
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
    public int add(int row, int col, Direction d, Tile... line) throws QwirkleException {
        if (isEmpty()) {
            throw new QwirkleException("The grid game is empty! You must call the firstAdd method!");
        }
        if (!validPosition(row, col, d, line)) { // The direction doesn't matter;
            throw new QwirkleException("Position outside the grid!");
        }
        if (!validLine(line)) {
            throw new QwirkleException("Invalid number of tiles!");
        }
        if (!validDirection(d)) {
            throw new QwirkleException("Invalid direction!");
        }
        if (!validTile(line)) {
            throw new QwirkleException("Some tiles are not initialized!");
        }
        if (existAlreadyTile(row, col, d, line)) {
            throw new QwirkleException("There is already a tile at this position!");
        }
        if (!adjacentTiles(row, col, d, line)) {
            throw new QwirkleException("The tiles are not connected to another tile!");
        }
        if (!validRulesAdd(row, col, d, line)) {
            throw new QwirkleException("Does not respect the rules of qwirkle game!");
        }
        // We can add the tiles;
        for (int i = 0; i < line.length; i++) {
            int newRow = row + i * d.getDeltaRow();
            int newCol = col + i * d.getDeltaCol();
            this.line[newRow][newCol] = line[i];
        }
        return score(row, col, d, line);
    }

    /**
     * Adds tiles at "any" position on the grid.
     *
     * @param line the tiles to add at any position.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public int add(TileAtPosition... line) throws QwirkleException {
        if (line.length == 1) {
            return add(line[0].row(), line[0].col(), line[0].tile());
        } else {
            if (isEmpty()) {
                throw new QwirkleException("The grid game is empty! You must call the firstAdd method!");
            }
            Tile[] lineTiles = new Tile[line.length];
            for (int i = 0; i < line.length; i++) {
                lineTiles[i] = line[i].tile();
            }
            if (!validLine(lineTiles)) {
                throw new QwirkleException("Invalid number of tiles!");
            }
            if (!validColorOrShape(lineTiles)) {
                throw new QwirkleException("Tiles must share the same color or shape!");
            }
            if (sameTile(lineTiles)) {
                throw new QwirkleException("You cannot add the same tile!");
            }
            // For each tile;
            List<Integer> tilesRow = new ArrayList<>();
            List<Integer> tilesCol = new ArrayList<>();
            List<Integer> sumRowCol = new ArrayList<>();
            for (TileAtPosition tileAtPosition : line) {
                int row = tileAtPosition.row();
                tilesRow.add(row);
                int col = tileAtPosition.col();
                tilesCol.add(col);
                Tile tile = tileAtPosition.tile();
                if (!validPosition(row, col, Direction.RIGHT, tile)) { // The direction doesn't matter;
                    throw new QwirkleException("Position outside the grid!");
                }
                if (!validTile(tile)) {
                    throw new QwirkleException("Some tiles are not initialized!");
                }
                if (existAlreadyTile(row, col, Direction.RIGHT, tile)) {
                    throw new QwirkleException("There is already a tile at this position!");
                }
                if (!tilesSameLine(tilesRow, tilesCol)) {
                    throw new QwirkleException("Tiles are not on the same line!");
                }
                sumRowCol.add(row + col);
                if (tileAtPositionSamePosition(sumRowCol)) {
                    throw new QwirkleException("You cannot put two tiles at the same position!");
                }
            }
            // We place the tiles in order to verify if tiles are adjacent;
            for (TileAtPosition tileAtPosition : line) {
                this.line[tileAtPosition.row()][tileAtPosition.col()] = tileAtPosition.tile();
            }

            if (!adjacentTileAtPosition(tilesRow, tilesCol, line[0])) {
                // We remove the tiles if not adjacent;
                for (TileAtPosition tileAtPosition : line) {
                    this.line[tileAtPosition.row()][tileAtPosition.col()] = null;
                }
                throw new QwirkleException("The tiles are not connected to another tile!");
            }
            // Qwirkle rules;
            for (TileAtPosition tileAtPosition : line) {
                if (!validRulesAdd(tileAtPosition.row(), tileAtPosition.col(), Direction.RIGHT, tileAtPosition.tile())) {
                    throw new QwirkleException("Does not respect the rules of qwirkle game!");
                }
            }
        }
        return scoreRulesAdd3(line);
    }

    /**
     * @return true if the grid is empty, false otherwise;
     */
    public boolean isEmpty() {
        return this.isEmpty;
    }

    //Checks that the tiles to add are adjacent to an existing tile on the grid;
    private boolean adjacentTiles(int row, int col, Direction d, Tile... line) {
        boolean adjacent = true;
        if ((this.line[row + (line.length * d.getDeltaRow())][col + (line.length * d.getDeltaCol())] == null)) {
            if ((this.line[row + (d.opposite().getDeltaRow())][col + (d.opposite().getDeltaCol())] == null)) {
                int i;
                for (i = 0; i < line.length; i++) {
                    Direction diagonal = d.diagonal(); // The diagonal direction
                    if ((this.line[row + (i * d.getDeltaRow()) + diagonal.getDeltaRow()] //diagonal
                            [col + (i * d.getDeltaCol()) + diagonal.getDeltaCol()] != null) ||
                            (this.line[row + (i * d.getDeltaRow()) + diagonal.opposite().getDeltaRow()] //opposite diagonal
                                    [col + (i * d.getDeltaCol()) + diagonal.opposite().getDeltaCol()] != null)) {
                        break;
                    }
                }
                if (i == line.length) {
                    adjacent = false;
                }
            }
        }
        return adjacent;
    }

    //Checks that the horizontal and vertical tile line are composed of maximum 6 tiles;
    public boolean validRulesAdd(int row, int col, Direction d, Tile... line) {
        boolean validRules = true;

        List<Tile> tilesFromTheLine = tilesFromTheLine(row, col, d, line); // List of all the tiles found on the line;
        Tile[] tilesLineTab = tilesFromTheLine.toArray(Tile[]::new); // Converts the list into an array;
        if (lineCompleted(tilesLineTab) || !validColorOrShape(tilesLineTab) || sameTile(tilesLineTab)) {
            // If the line is already complete or if the tiles do not share a common characteristic
            // or if there is the same tile twice;
            validRules = false;
        }

        int indexLine = 0;
        while (indexLine < line.length && validRules) {
            int tileColumn = col + indexLine * d.getDeltaCol();
            int tileRow = row + indexLine * d.getDeltaRow();
            // List of all the tiles found on the opposite line of each tile;
            List<Tile> tilesFromOppLine = tilesFromTheLine(tileRow, tileColumn, d.diagonal(), line[indexLine]); // Diagonal direction;
            Tile[] tilesOppLineTab = tilesFromOppLine.toArray(Tile[]::new);
            if (lineCompleted(tilesOppLineTab) || !validColorOrShape(tilesOppLineTab) || sameTile(tilesOppLineTab)) {
                validRules = false;
            }
            tilesFromOppLine.clear();
            indexLine++;
        }
        return validRules;
    }

    // Returns the tile line of the tiles passed in parameter;
    private List<Tile> tilesFromTheLine(int row, int col, Direction d, Tile... line) {
        List<Tile> directionTiles = new ArrayList<>();
        Collections.addAll(directionTiles, line); // Adds line(tiles to add) on the list;
        int colDirection = col + line.length * d.getDeltaCol();
        int rowDirection = row + line.length * d.getDeltaRow();
        while (this.line[rowDirection][colDirection] != null) {
            directionTiles.add(this.line[rowDirection][colDirection]);
            colDirection = colDirection + d.getDeltaCol();
            rowDirection = rowDirection + d.getDeltaRow();
        }
        int oppCol = col + d.opposite().getDeltaCol();
        int oppRow = row + d.opposite().getDeltaRow();
        while (this.line[oppRow][oppCol] != null) {
            directionTiles.add(this.line[oppRow][oppCol]);
            oppCol = oppCol + d.opposite().getDeltaCol();
            oppRow = oppRow + d.opposite().getDeltaRow();
        }
        return directionTiles;
    }

    private boolean lineCompleted(Tile[] tiles) {
        return tiles.length > 6;
    }

    private boolean sameTile(Tile[] tiles) {
        boolean sameTile = false;
        int indexTile = 0;
        while (indexTile < tiles.length && !sameTile) {
            int i = indexTile + 1;
            while (i < tiles.length && !sameTile) {
                if (tiles[indexTile].equals(tiles[i])) {
                    sameTile = true;
                }
                i++;
            }
            indexTile++;
        }
        return sameTile;
    }

    private boolean validColorOrShape(Tile... line) {
        boolean validColorOrShape = false;
        int i = 1;
        while (i < line.length && line[0].color() == line[i].color()) {
            i++;
        }
        if(i >= line.length){
            validColorOrShape = true;
        }
        if(!validColorOrShape){
            int j = 1;
            while (j < line.length && line[0].shape() == line[j].shape()) {
                j++;
            }
            if(j >= line.length){
                validColorOrShape = true;
            }
        }
        return validColorOrShape;
    }

    private boolean sameRowOrCol(List<Integer> rowOrCol) {
        boolean sameRowOrCol = true;
        int indexRow = 0;
        while (indexRow < rowOrCol.size() - 1 && sameRowOrCol) {
            if (!rowOrCol.get(indexRow).equals(rowOrCol.get(indexRow + 1))) {
                sameRowOrCol = false;
            }
            indexRow++;
        }
        return sameRowOrCol;
    }

    private boolean tilesSameLine(List<Integer> row, List<Integer> col) {
        return sameRowOrCol(row) || sameRowOrCol(col);
    }

    private boolean tileAtPositionSamePosition(List<Integer> sumRowCol) {
        boolean samePosition = false;
        int i = 0;
        while (i < sumRowCol.size() && !samePosition) {
            int j = i + 1;
            while (j < sumRowCol.size() && !samePosition) {
                if (sumRowCol.get(i).equals(sumRowCol.get(j))) {
                    samePosition = true;
                }
                j++;
            }
            i++;
        }
        return samePosition;
    }

    //Checks that the position of the first and last tile is valid:
    //all "row" and "col" must be in the grid, i.e. 91*91;
    private boolean validPosition(int row, int col, Direction d, Tile... line) {
        boolean valid = true;
        if (row > 89 || row < 1 || col > 89 || col < 1) {
            return false;
        }
        switch (d) {
            case LEFT -> {
                if (col - line.length - 1 < 1) {
                    valid = false;
                }
            }
            case RIGHT -> {
                if (col + line.length - 1 > 89) {
                    valid = false;
                }
            }
            case UP -> {
                if (row - line.length - 1 < 1) {
                    valid = false;
                }
            }
            case DOWN -> {
                if (row + line.length - 1 > 89) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    //Checks if the line is well initialized;
    private boolean validLine(Tile... line) {
        return line != null && line.length != 0 && line.length <= 6;
    }

    //Checks if the direction is well initialized;
    private boolean validDirection(Direction d) {
        return d != null;
    }

    //Checks if the tile are well initialized;
    private boolean validTile(Tile... line) {
        boolean valid = true;
        for (Tile tile : line) {
            if (tile == null) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    //Checks that at this position of the grid does not already exist a tile;
    private boolean existAlreadyTile(int row, int col, Direction d, Tile... line) {
        boolean existAlreadyTile = false;
        int i =0;
        while(i < line.length && !existAlreadyTile){
            if (this.line[row + d.getDeltaRow() * i][col + d.getDeltaCol() * i] != null) {
                existAlreadyTile = true;
            }
            i++;
        }
        return existAlreadyTile;
    }

    private int score(int row, int col, Direction d, Tile... line) {
        int score = 0;
        List<Tile> tilesFromTheLine = tilesFromTheLine(row, col, d, line);
        if (tilesFromTheLine.size() > 1) {
            score = score + tilesFromTheLine.size(); // If rules are respected;
        }
        if (tilesFromTheLine.size() == 6) {
            score = score + 6; // Qwirkle line;
        }
        // List of all the tiles found on the opposite line of the tile passed as parameter;
        for (int i = 0; i < line.length; i++) {
            int tileColumn = col + i * d.getDeltaCol();
            int tileRow = row + i * d.getDeltaRow();
            List<Tile> tilesFromThOppositeLine = tilesFromTheLine(tileRow, tileColumn, d.diagonal(), line[i]);// Diagonal direction;
            if (tilesFromThOppositeLine.size() > 1) {
                score = score + tilesFromThOppositeLine.size(); // If rules are respected;
            }
            if (tilesFromThOppositeLine.size() == 6) {
                score = score + 6; // Qwirkle line;
            }
            tilesFromThOppositeLine.clear();
        }
        return score;
    }

    private int scoreRulesAdd3(TileAtPosition... line) {
        int score = 0;
        List<Integer> tilesRow = new ArrayList<>();
        for (TileAtPosition tileAtPosition : line) {
            int newRow = tileAtPosition.row();
            tilesRow.add(newRow);
            int newCol = tileAtPosition.col();
            Tile tile = tileAtPosition.tile();
            score = score + score(newRow, newCol, Direction.RIGHT, tile);
            // It doesn't matter the direction here;
        }
        // Removes from the score the number of tiles that are repeated on the same line;
        Direction d;
        if (sameRowOrCol(tilesRow)) { // If horizontal line;
            d = Direction.RIGHT;
        } else {
            d = Direction.UP; // If vertical line;
        }
        TileAtPosition tile = line[0];
        List<Tile> tilesToRemove = tilesFromTheLine(tile.row(), tile.col(), d, tile.tile());
        score = score - (line.length - 1) * tilesToRemove.size();
        return score;
    }

    private boolean adjacentTileAtPosition(List<Integer> tilesRow, List<Integer> tilesCol, TileAtPosition tileAtPosition) {
        boolean valid = true;
        Direction d;
        if (sameRowOrCol(tilesRow)) {  // If horizontal line;
            d = Direction.RIGHT;
        } else {
            d = Direction.UP; // If vertical line;
        }
        // We place the tiles
        int min;
        int max;
        // Defines the min and max;
        if (d == Direction.RIGHT) {
            // We check the columns;
            Collections.sort(tilesCol);
            min = tilesCol.get(0);
            max = tilesCol.get(tilesCol.size() - 1);
            int j = min;
            while (j < max && get(tileAtPosition.row(), j) != null) {
                j++;
            }
            if (j != max) {
                valid = false;
            }
        } else {
            // We check the rows;
            Collections.sort(tilesRow);
            min = tilesRow.get(0);
            max = tilesRow.get(tilesRow.size() - 1);
            int i = min;
            while (i < max && get(i, tileAtPosition.col()) != null) {
                i++;
            }
            if (i != max) {
                valid = false;
            }
        }
        return valid;
    }

}


