package g60085.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * The `Grid` class represents the game grid in the Qwirkle game.
 * It holds the tiles placed on the grid and provides operations for manipulating the grid.
 * The grid is represented as a two-dimensional array of tiles.
 * Each cell in the grid can hold a single tile object.
 * The `Grid` class supports operations such as placing tiles, checking if the grid is empty,
 * and accessing specific cells or tiles on the grid.
 * It is serializable to support saving and loading grid state along with the game.
 */
public class Grid implements Serializable {
    private Tile[][] line;
    private boolean isEmpty;

    /**
     * Initializes a new instance of the `Grid` class.
     * The grid is created as a two-dimensional array with a size of 91x91.
     * Initially, the grid is considered empty.
     */
    public Grid() {
        this.line = new Tile[91][91];
        this.isEmpty = true;
    }

    /**
     * Gives access to the tile at the specified position in the game grid.
     *
     * @param row The row index of the tile in the grid.
     * @param col The column index of the tile in the grid.
     * @return The tile located at the specified position, or null if the position is outside the grid.
     */
    public Tile get(int row, int col) {
        if (row > 91 || row < 0 || col > 91 || col < 0) {
            return null;
        }
        return this.line[row][col];
    }

    /**
     * Adds the first set of tiles to the game grid, following the rules of the Qwirkle game.
     * This method can only be used once when the grid is empty.
     *
     * @param direction The direction in which the tiles will be placed.
     * @param line      The tiles to add to the grid.
     * @return The total number of tiles added to the grid, which corresponds to the score obtained.
     * @throws QwirkleException if the addition of tiles does not comply with the rules of the Qwirkle game.
     */
    public int firstAdd(Direction direction, Tile... line) throws QwirkleException {
        // Check if the grid is empty
        if (!isEmpty()) {
            throw new QwirkleException("The grid is not empty!");
        }
        // Check the line, tiles, and direction
        if (!validLine(line)) {
            throw new QwirkleException("Invalid number of tiles!");
        }
        if (!validDirection(direction)) {
            throw new QwirkleException("Invalid direction!");
        }
        if (!isTileInit(line)) {
            throw new QwirkleException("Some tiles are not initialized!");
        }
        // Check if there is a common characteristic between tiles
        if (line.length > 1) {
            if (!validColorOrShape(line)) {
                throw new QwirkleException("Tiles must share the same color or shape!");
            }
            if (hasDuplicateTiles(line)) {
                throw new QwirkleException("You cannot add the same tile!");
            }
        }
        // Add the tiles to the grid
        int row = 45;
        int col = 45;
        this.line[row][col] = line[0];
        for (int i = 1; i < line.length; i++) {
            row += direction.getDeltaRow();
            col += direction.getDeltaCol();
            this.line[row][col] = line[i];
        }
        this.isEmpty = false; // Set the "isEmpty" attribute to false
        // Return the total number of tiles added to the grid
        return line.length == 6 ? line.length + 6 : line.length;
    }

    /**
     * Adds a tile to the specified position on the grid, following the rules of the Qwirkle game.
     *
     * @param row  The row of the tile to be added.
     * @param col  The column of the tile to be added.
     * @param tile The tile to be added.
     * @return The score obtained by adding the tile.
     * @throws QwirkleException if the addition of the tile does not comply with the rules of the Qwirkle game.
     */
    public int add(int row, int col, Tile tile) throws QwirkleException {
        if (isEmpty()) {
            throw new QwirkleException("The grid game is empty! You must call the firstAdd method!");
        }
        if (!validPosition(row, col, Direction.RIGHT, tile)) {
            throw new QwirkleException("Position outside the grid!");
        }
        if (!validLine(tile)) {
            throw new QwirkleException("Invalid number of tiles!");
        }
        if (!isTileInit(tile)) {
            throw new QwirkleException("Some tiles are not initialized!");
        }
        if (existAlreadyTile(row, col, Direction.RIGHT, tile)) {
            throw new QwirkleException("There is already a tile at this position!");
        }
        if (!adjacentTiles(row, col, Direction.RIGHT, tile)) {
            throw new QwirkleException("The tiles are not connected to another tile!");
        }
        if (!validRulesAdd(row, col, Direction.RIGHT, tile)) {
            throw new QwirkleException("Does not respect the rules of Qwirkle game!");
        }

        // Add the tile to the grid
        this.line[row][col] = tile;

        // Calculate and return the score obtained
        return score(row, col, Direction.RIGHT, tile);
    }

    /**
     * Adds multiple tiles at a specified position in the given direction, following the rules of the Qwirkle game.
     *
     * @param row  The row of the first tile position.
     * @param col  The column of the first tile position.
     * @param d    The direction of tile placement.
     * @param line The tiles to be added.
     * @return The score obtained by adding the tiles.
     * @throws QwirkleException if the addition of tiles does not comply with the rules of the Qwirkle game.
     */
    public int add(int row, int col, Direction d, Tile... line) throws QwirkleException {
        if (isEmpty()) {
            throw new QwirkleException("The grid game is empty! You must call the firstAdd method!");
        }
        if (!validPosition(row, col, d, line)) {
            throw new QwirkleException("Position outside the grid!");
        }
        if (!validLine(line)) {
            throw new QwirkleException("Invalid number of tiles!");
        }
        if (!validDirection(d)) {
            throw new QwirkleException("Invalid direction!");
        }
        if (!isTileInit(line)) {
            throw new QwirkleException("Some tiles are not initialized!");
        }
        if (existAlreadyTile(row, col, d, line)) {
            throw new QwirkleException("There is already a tile at this position!");
        }
        if (!adjacentTiles(row, col, d, line)) {
            throw new QwirkleException("The tiles are not connected to another tile!");
        }
        if (!validRulesAdd(row, col, d, line)) {
            throw new QwirkleException("Does not respect the rules of Qwirkle game!");
        }

        // Add the tiles to the grid
        for (int i = 0; i < line.length; i++) {
            int newRow = row + i * d.getDeltaRow();
            int newCol = col + i * d.getDeltaCol();
            this.line[newRow][newCol] = line[i];
        }

        // Calculate and return the score obtained
        return score(row, col, d, line);
    }

    /**
     * Adds tiles at any position on the grid, following the rules of the Qwirkle game.
     *
     * @param line The tiles to add at any position.
     * @return The score obtained by adding the tiles.
     * @throws QwirkleException if the addition of tiles does not comply with the rules of the Qwirkle game.
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
            if (hasDuplicateTiles(lineTiles)) {
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
                if (!isTileInit(tile)) {
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
                // Qwirkle rules;
                if (!validRulesAdd(row, col, Direction.RIGHT, tile)) {
                    throw new QwirkleException("Does not respect the rules of qwirkle game!");
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

        }
        return scoreRulesAdd3(line);
    }

    /**
     * Checks if the grid is empty.
     *
     * @return true if the grid is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.isEmpty;
    }

    /**
     * Checks if the tiles to add are adjacent to an existing tile on the grid.
     *
     * @param row  the row of the first tile position.
     * @param col  the column of the first tile position.
     * @param d    the direction of tiles placement.
     * @param line tiles to add.
     * @return true if the tiles are adjacent to an existing tile, false otherwise.
     */
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

    /**
     * Checks if the horizontal and vertical tile line are composed of a maximum of 6 tiles.
     * Additionally, it verifies that the line does not contain duplicate tiles and that all tiles
     * in the line share a common characteristic (color or shape).
     *
     * @param row  the row of the first tile position.
     * @param col  the column of the first tile position.
     * @param d    the direction of tiles placement.
     * @param line tiles to add.
     * @return true if the rules of adding tiles are valid, false otherwise.
     */
    public boolean validRulesAdd(int row, int col, Direction d, Tile... line) {
        boolean validRules = true;

        List<Tile> tilesFromTheLine = tilesFromTheLine(row, col, d, line);
        Tile[] tilesLineTab = tilesFromTheLine.toArray(Tile[]::new);
        if (!doesNotExceedMaxTiles(tilesLineTab) || !validColorOrShape(tilesLineTab) || hasDuplicateTiles(tilesLineTab)) {
            validRules = false;
        }

        int indexLine = 0;
        while (indexLine < line.length && validRules) {
            int tileColumn = col + indexLine * d.getDeltaCol();
            int tileRow = row + indexLine * d.getDeltaRow();
            List<Tile> tilesFromOppLine = tilesFromTheLine(tileRow, tileColumn, d.diagonal(), line[indexLine]);
            Tile[] tilesOppLineTab = tilesFromOppLine.toArray(Tile[]::new);
            if (!doesNotExceedMaxTiles(tilesOppLineTab) || !validColorOrShape(tilesOppLineTab) || hasDuplicateTiles(tilesOppLineTab)) {
                validRules = false;
            }
            tilesFromOppLine.clear();
            indexLine++;
        }
        return validRules;
    }

    /**
     * Retrieves the tile line based on the given starting position and direction.
     *
     * @param row  the row of the starting tile position.
     * @param col  the column of the starting tile position.
     * @param d    the direction of the line.
     * @param line the tiles to add.
     * @return the list of tiles in the line.
     */
    private List<Tile> tilesFromTheLine(int row, int col, Direction d, Tile... line) {
        List<Tile> directionTiles = new ArrayList<>(Arrays.asList(line));
        int colDirection = col + line.length * d.getDeltaCol();
        int rowDirection = row + line.length * d.getDeltaRow();
        while (isTilePresent(rowDirection, colDirection)) {
            directionTiles.add(this.line[rowDirection][colDirection]);
            colDirection += d.getDeltaCol();
            rowDirection += d.getDeltaRow();
        }
        int oppCol = col + d.opposite().getDeltaCol();
        int oppRow = row + d.opposite().getDeltaRow();
        while (isTilePresent(oppRow, oppCol)) {
            directionTiles.add(this.line[oppRow][oppCol]);
            oppCol += d.opposite().getDeltaCol();
            oppRow += d.opposite().getDeltaRow();
        }
        return directionTiles;
    }

    /**
     * Checks if a tile is present at the specified row and column coordinates.
     *
     * @param row the row coordinate.
     * @param col the column coordinate.
     * @return true if a tile is present, false otherwise.
     */
    private boolean isTilePresent(int row, int col) {
        return this.line[row][col] != null;
    }

    /**
     * Checks if a line of tiles does not exceed the maximum number of tiles allowed.
     *
     * @param tiles the array of tiles in the line.
     * @return true if the line does not exceed the maximum number of tiles, false otherwise.
     */
    private boolean doesNotExceedMaxTiles(Tile[] tiles) {
        return tiles.length <= 6;
    }

    /**
     * Checks if there are any duplicate tiles in the given array.
     *
     * @param tiles the array of tiles to check.
     * @return true if there are duplicate tiles, false otherwise.
     */
    private boolean hasDuplicateTiles(Tile[] tiles) {
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

    /**
     * Checks if all the tiles in the given array share either the same color or the same shape.
     *
     * @param line the array of tiles to check.
     * @return true if all tiles share the same color or shape, false otherwise.
     */
    private boolean validColorOrShape(Tile... line) {
        boolean validColorOrShape = false;
        int i = 1;
        while (i < line.length && line[0].color() == line[i].color()) {
            i++;
        }
        if (i >= line.length) {
            validColorOrShape = true;
        }
        if (!validColorOrShape) {
            int j = 1;
            while (j < line.length && line[0].shape() == line[j].shape()) {
                j++;
            }
            if (j >= line.length) {
                validColorOrShape = true;
            }
        }
        return validColorOrShape;
    }

    /**
     * Checks if all the elements in the given list are the same.
     *
     * @param rowOrCol the list of row or column values to check.
     * @return true if all elements are the same, false otherwise.
     */
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

    /**
     * Checks if the list of row values or the list of column values contains the same value for all elements.
     *
     * @param row the list of row values.
     * @param col the list of column values.
     * @return true if either the row values or the column values are the same for all elements, false otherwise.
     */
    private boolean tilesSameLine(List<Integer> row, List<Integer> col) {
        return sameRowOrCol(row) || sameRowOrCol(col);
    }

    /**
     * Checks if the list of sum of row and column values has any duplicate values.
     *
     * @param sumRowCol the list of sum of row and column values.
     * @return true if there are duplicate values in the list, false otherwise.
     */
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

    /**
     * Checks if the position of the first and last tile is valid.
     *
     * @param row  the row of the first tile position.
     * @param col  the column of the first tile position.
     * @param d    the direction of tiles placement.
     * @param line the tiles to add.
     * @return true if the position is valid, false otherwise.
     */
    private boolean validPosition(int row, int col, Direction d, Tile... line) {
        if (row > 89 || row < 1 || col > 89 || col < 1) {
            return false;      // Check if the initial position is outside the grid
        }

        return switch (d) {
            case LEFT -> col - line.length >= 0; // Check if the line fits within the grid on the left
            case RIGHT -> col + line.length <= 90; // Check if the line fits within the grid on the right
            case UP -> row - line.length >= 0; // Check if the line fits within the grid upwards
            case DOWN -> row + line.length <= 90; // Check if the line fits within the grid downwards
        };
    }

    /**
     * Checks if the line of tiles is well initialized.
     *
     * @param line the tiles to check.
     * @return true if the line is well initialized, false otherwise.
     */
    private boolean validLine(Tile... line) {
        return line != null && line.length > 0 && line.length <= 6;
    }

    /**
     * Checks if the direction is well initialized.
     *
     * @param d the direction to check.
     * @return true if the direction is well initialized, false otherwise.
     */
    private boolean validDirection(Direction d) {
        return d != null;
    }

    /**
     * Checks if the tiles are well initialized.
     *
     * @param line the tiles to check.
     * @return true if the tiles are well initialized, false otherwise.
     */
    private boolean isTileInit(Tile... line) {
        boolean valid = true;
        for (Tile tile : line) {
            if (tile == null) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    /**
     * Checks if a tile already exists at the specified position on the grid.
     *
     * @param row  the row index of the position.
     * @param col  the column index of the position.
     * @param d    the direction of the line.
     * @param line the tiles to add.
     * @return true if a tile already exists at the position, false otherwise.
     */
    private boolean existAlreadyTile(int row, int col, Direction d, Tile... line) {
        boolean existAlreadyTile = false;
        int i = 0;
        while (i < line.length && !existAlreadyTile) {
            if (this.line[row + d.getDeltaRow() * i][col + d.getDeltaCol() * i] != null) {
                existAlreadyTile = true;
            }
            i++;
        }
        return existAlreadyTile;
    }

    /**
     * Calculates the score based on the tiles added to the grid.
     *
     * @param row  the row index of the position.
     * @param col  the column index of the position.
     * @param d    the direction of the line.
     * @param line the tiles to add.
     * @return the calculated score.
     */
    private int score(int row, int col, Direction d, Tile... line) {
        int score = 0;
        List<Tile> tilesFromTheLine = tilesFromTheLine(row, col, d, line);

        if (tilesFromTheLine.size() > 1) {
            score += tilesFromTheLine.size(); // If rules are respected
        }
        if (tilesFromTheLine.size() == 6) {
            score += 6; // Qwirkle line
        }

        // List of all the tiles found on the opposite line of the tile passed as a parameter
        for (int i = 0; i < line.length; i++) {
            int tileColumn = col + i * d.getDeltaCol();
            int tileRow = row + i * d.getDeltaRow();
            List<Tile> tilesFromOppositeLine = tilesFromTheLine(tileRow, tileColumn, d.diagonal(), line[i]); // Diagonal direction

            if (tilesFromOppositeLine.size() > 1) {
                score += tilesFromOppositeLine.size(); // If rules are respected
            }
            if (tilesFromOppositeLine.size() == 6) {
                score += 6; // Qwirkle line
            }

            tilesFromOppositeLine.clear();
        }

        return score;
    }

    /**
     * Calculates the score for adding tiles to the grid based on their positions.
     *
     * @param line the tiles with their positions to add.
     * @return the calculated score.
     */
    private int scoreRulesAdd3(TileAtPosition... line) {
        int score = 0;
        List<Integer> tilesRow = new ArrayList<>();

        for (TileAtPosition tileAtPosition : line) {
            int newRow = tileAtPosition.row();
            tilesRow.add(newRow);
            int newCol = tileAtPosition.col();
            Tile tile = tileAtPosition.tile();
            score += score(newRow, newCol, Direction.RIGHT, tile); // It doesn't matter the direction here
        }

        // Removes from the score the number of tiles that are repeated on the same line
        Direction d = sameRowOrCol(tilesRow) ? Direction.RIGHT : Direction.UP;
        TileAtPosition tile = line[0];
        List<Tile> tilesToRemove = tilesFromTheLine(tile.row(), tile.col(), d, tile.tile());
        score -= (line.length - 1) * tilesToRemove.size();

        return score;
    }

    /**
     * Checks if the given TileAtPosition is adjacent to the existing tiles in the grid.
     *
     * @param tilesRow       the list of row positions of the existing tiles.
     * @param tilesCol       the list of column positions of the existing tiles.
     * @param tileAtPosition the TileAtPosition to check for adjacency.
     * @return true if the tile is adjacent to the existing tiles, false otherwise.
     */
    private boolean adjacentTileAtPosition(List<Integer> tilesRow, List<Integer> tilesCol, TileAtPosition tileAtPosition) {
        boolean valid = true;
        Direction d = sameRowOrCol(tilesRow) ? Direction.RIGHT : Direction.UP;
        int min, max;

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

            valid = j == max;

        } else {
            // We check the rows;
            Collections.sort(tilesRow);
            min = tilesRow.get(0);
            max = tilesRow.get(tilesRow.size() - 1);
            int i = min;
            while (i < max && get(i, tileAtPosition.col()) != null) {
                i++;
            }
            valid = i == max;

        }
        return valid;
    }

}


