package g60085.qwirkle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Grid represents the game grid.
 */
public class Grid {
    private Tile[][] tiles;
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
    public int firstAdd(Direction d, Tile... line) throws QwirkleException {
        // Checks that the grid is empty;
        if (!this.isEmpty) {
            throw new QwirkleException("The grid is not empty!");
        }
        // Checks the line and direction;
        exceptionValidLineDirection(d, line);

        // Checks if there is a common characteristic between tiles;
        if (line.length > 1) {
            verifyColorShape(line);
        }
        // We can add the tiles;
        int row = 45;
        int col = 45;
        this.tiles[row][col] = line[0];
        for (int i = 1; i < line.length; i++) {
            row = row + d.getDeltaRow();
            col = col + d.getDeltaCol();
            this.tiles[row][col] = line[i];
        }
        isEmpty(); // Set the "empty" attribute to false;
        return line.length;
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
        // Call the method that checks the game rules;
        int score = checkRulesAdd1(row, col, tile);
        // We can add the tiles;
        this.tiles[row][col] = tile;
        return score;
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
        // Call the method that checks the game rules;
        int score = checkRulesAdd2(row, col, d, line);
        // We can add the tiles;
        for (int i = 0; i < line.length; i++) {
            int newRow = row + i * d.getDeltaRow();
            int newCol = col + i * d.getDeltaCol();
            this.tiles[newRow][newCol] = line[i];
        }
        return score;
    }

    /**
     * Adds tiles at "any" position on the grid.
     *
     * @param line the tiles to add at any position.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public int add(TileAtPosition... line) throws QwirkleException {
        int score = 0;
        try {
            // Call the method that checks the game rules;
            score = checkRulesAdd3(line);
        } catch (QwirkleException e) {
            // If the rules are not respected, the tiles are removed from the grid;
            for (TileAtPosition tileAtPosition : line) {
                this.tiles[tileAtPosition.row()][tileAtPosition.col()] = null;
                throw new QwirkleException(e.getMessage());
            }
        }
        return score;
    }

    /**
     * Initialize the "isEmpty" attribute to false in order to show that the game grid is no longer empty.
     */
    public void isEmpty() {
        this.isEmpty = false;
    }

    /**
     * Tests for the first add method:
     * Checks that the grid is not empty because if it is, the firstAdd method must be used;
     * Checks that the position of the tile placement is valid: row and col must be in the grid, i.e. 91*91;
     * Checks that the tile is well initialized;
     * Checks that at this position of the grid does not already exist a tile.
     * Checks that the tile to add respect the rules of the Qwrikle game;
     *
     * @param row  the row of the tile that we want to add.
     * @param col  the column of the tile that we want to add.
     * @param tile the tile that we want to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private int checkRulesAdd1(int row, int col, Tile tile) throws QwirkleException {
        //Checks that the grid is not empty because if it is, the firstAdd method must be used;
        if (this.isEmpty) {
            throw new QwirkleException("The grid game is empty! You must call the firstAdd method!");
        }
        //Checks that the position of the tile placement is valid: row and col must be in the grid, i.e. 91*91;
        if (row > 89 || row < 1 || col > 89 || col < 1) {
            throw new QwirkleException("Position outside the grid!");
        }
        //Checks that the tile is well initialized;
        if (tile == null) {
            throw new QwirkleException("It seems that the tile is not initialized!");
        }
        //Checks that at this position of the grid does not already exist a tile;
        if (this.tiles[row][col] != null) {
            throw new QwirkleException("There is already a tile at this position!");
        }
        //Checks that the tile to add is adjacent to an existing tile on the grid;
        //Checks that the horizontal and vertical tile line is composed of a maximum of 6 tiles;
        //Checks that the horizontal and vertical tiles share the same characteristic;
        return checkRulesAdd(row, col, Direction.RIGHT, tile);// it doesn't matter the direction here
        // because there is only one tile;
    }

    /**
     * Tests for the second add method:
     * If there is only one tile to add then call the first add method;
     * Checks that the grid is not empty because if it is, the firstAdd method must be used;
     * Checks that the position of the first and last tile is valid:
     * all "row" and "col" must be in the grid, i.e. 91*91;
     * Checks that the tiles as well as the direction are well initialized;
     * Checks that at this position of the grid does not already exist a tile;
     * Checks that the tiles to add respect the rules of the Qwrikle game;
     *
     * @param row  the row of the first tile to add.
     * @param col  the column of the first tile to add.
     * @param d    the direction of tiles placement.
     * @param line the tiles to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private int checkRulesAdd2(int row, int col, Direction d, Tile... line) throws QwirkleException {
        //If there is only one tile to add then call the first add method;
        if (line.length == 1) {
            return add(row, col, line[0]);
        } else {
            //Checks that the grid is not empty because if it is, the firstAdd method must be used;
            if (this.isEmpty) {
                throw new QwirkleException("The grid game is empty! You must call the firstAdd method!");
            }
            //Checks that the position of the first and last tile is valid:
            //all "row" and "col" must be in the grid, i.e. 91*91;
            if (row > 89 || row < 1 || col > 89 || col < 1) {
                throw new QwirkleException("Position outside the grid!");
            }
            switch (d) {
                case LEFT -> {
                    if (col - line.length - 1 < 1) {
                        throw new QwirkleException("Position outside the grid!");
                    }
                }
                case RIGHT -> {
                    if (col + line.length - 1 > 89) {
                        throw new QwirkleException("Position outside the grid!");
                    }
                }
                case UP -> {
                    if (row - line.length - 1 < 1) {
                        throw new QwirkleException("Position outside the grid!");
                    }
                }
                case DOWN -> {
                    if (row + line.length - 1 > 89) {
                        throw new QwirkleException("Position outside the grid!");
                    }
                }
            }
            //Checks that the tiles as well as the direction are well initialized;
            exceptionValidLineDirection(d, line);
            //Checks that at this position of the grid does not already exist a tile;
            for (int i = 0; i < line.length; i++) {
                if (this.tiles[row + d.getDeltaRow() * i][col + d.getDeltaCol() * i] != null) {
                    throw new QwirkleException("There is already a tile at this position!");
                }
            }
            //Checks that the tiles you want to add respect the rules of the Qwrikle game;
            return checkRulesAdd(row, col, d, line);
        }
    }

    /**
     * Tests for the third add method:
     * If there is only one tile to add then call the first add method;
     * Checks that the grid is not empty because if it is, the firstAdd method must be used;
     * Checks that the tiles to be added share the same characteristic;
     * Checks that there are not two identical tiles;
     * Checks that the position of the tile placement is valid: row and col must be in the grid, i.e. 91*91;
     * Checks that the tile is well initialized;
     * Checks that at this position of the grid does not already exist a tile;
     * Checks that the tiles are on the same line;
     * Checks that there are not two identical positions;
     * Checks that the tiles to add respect the rules of the Qwrikle game;
     *
     * @param line the tiles to add at any position.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private int checkRulesAdd3(TileAtPosition... line) throws QwirkleException {
        // If there is only one tile to add then call the first add method;
        if (line.length == 1) {
            return add(line[0].row(), line[0].col(), line[0].tile());
        } else {
            // Checks that the grid is not empty because if it is, the firstAdd method must be used;
            if (this.isEmpty) {
                throw new QwirkleException("The grid game is empty! You must call the firstAdd method!");
            }
            if (line == null || line.length == 0 || line.length > 6) {
                throw new QwirkleException("Invalid number of tiles!");
            }
            // Checks that the tiles to be added share the same characteristic;
            // Checks that there are not two identical tiles;
            Tile[] lineTiles = new Tile[line.length];
            for (int i = 0; i < line.length; i++) {
                lineTiles[i] = line[i].tile();
            }
            verifyColorShape(lineTiles);
            List<Integer> tilesRow = new ArrayList<>();
            List<Integer> tilesCol = new ArrayList<>();
            List<Integer> sumRowCol = new ArrayList<>();
            String sameLine = "";
            for (TileAtPosition tileAtPosition : line) {
                int row = tileAtPosition.row();
                tilesRow.add(row);
                int col = tileAtPosition.col();
                tilesCol.add(col);
                Tile tile = tileAtPosition.tile();
                //Checks that the position of the tile placement is valid: row and col must be in the grid, i.e. 91*91;
                if (row > 89 || row < 1 || col > 89 || col < 1) {
                    throw new QwirkleException("Position outside the grid!");
                }
                //Checks that the tile is well initialized;
                if (tile == null) {
                    throw new QwirkleException("It seems that the tile is not initialized!");
                }
                //Checks that at this position of the grid does not already exist a tile;
                if (this.tiles[row][col] != null) {
                    throw new QwirkleException("There is already a tile at this position!");
                }
                //Checks that the tiles are on the same line;
                sameLine = tilesSameLine(tilesRow, tilesCol);
                //Checks that there are not two identical positions;
                sumRowCol.add(row + col);
                tilesSamePosition(sumRowCol);
            }
            // We place the tiles to be able to check the rules;
            for (TileAtPosition tileAtPosition : line) {
                this.tiles[tileAtPosition.row()][tileAtPosition.col()] = tileAtPosition.tile();
            }
            int score = 0;
            for (TileAtPosition tileAtPosition : line) {
                //Checks that the tile to add is adjacent to an existing tile on the grid;
                //Checks that the horizontal and vertical tile line is composed of a maximum of 6 tiles;
                //Checks that the horizontal and vertical tiles share the same characteristic;
                int newRow = tileAtPosition.row();
                int newCol = tileAtPosition.col();
                Tile tile = tileAtPosition.tile();
                score = score + checkRulesAdd(newRow, newCol, Direction.RIGHT, tile);// It doesn't matter the direction here;
            }
            //remove from the score the number of tiles that are repeated on the same line
            Direction d;
            if (sameLine.equals("horizontalLine")) {
                Collections.sort(tilesCol);
                for (int i = 0; i < tilesCol.size() - 1; i++) {//for each tile's col
                    int j;
                    for (j = tilesCol.get(i) + 1; j < tilesCol.get(i + 1); j++) {
                        if (this.tiles[line[0].row()][j] == null) {
                            break;
                        }
                    }
                    if (j == tilesCol.get(i + 1)) {
                        score = score - (tilesCol.get(i + 1) - tilesCol.get(i) + 1);
                    }
                }
            } else {
                Collections.sort(tilesRow);
                for (int i = 0; i < tilesRow.size() - 1; i++) {//for each tile's row
                    int j;
                    for (j = tilesRow.get(i) + 1; j < tilesRow.get(i + 1); j++) {
                        if (this.tiles[j][line[0].col()] == null) {
                            break;
                        }
                    }
                    if (j == tilesRow.get(i + 1)) {
                        score = score - (tilesRow.get(i + 1) - tilesRow.get(i) + 1);

                    }
                }
            }
            return score;
        }
    }

    /**
     * Checks if the tiles to add are adjacent to an existing tile on the grid;
     *
     * @param row  the row of the first tile to add.
     * @param col  the column of the first tile to add.
     * @param d    the direction of tiles placement.
     * @param line the tiles to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private void adjacentTiles(int row, int col, Direction d, Tile... line) {
        //Checks that the tiles to add are adjacent to an existing tile on the grid;
        if ((this.tiles[row + (line.length * d.getDeltaRow())][col + (line.length * d.getDeltaCol())] == null)) {
            if ((this.tiles[row + (d.opposite().getDeltaRow())][col + (d.opposite().getDeltaCol())] == null)) {
                int i;
                for (i = 0; i < line.length; i++) {
                    Direction diagonal = d.diagonal(); // The diagonal direction
                    if ((this.tiles[row + (i * d.getDeltaRow()) + diagonal.getDeltaRow()] //diagonal
                            [col + (i * d.getDeltaCol()) + diagonal.getDeltaCol()] != null) ||
                            (this.tiles[row + (i * d.getDeltaRow()) + diagonal.opposite().getDeltaRow()] //opposite diagonal
                                    [col + (i * d.getDeltaCol()) + diagonal.opposite().getDeltaCol()] != null)) {
                        break;
                    }
                }
                if (i == line.length) {
                    throw new QwirkleException("The tiles are not connected to another tile!");
                }
            }
        }
    }

    /**
     * Tests for add method when the direction is Left or Right:
     * Checks that the tiles to add are adjacent to an existing tile on the grid;
     * Checks that the horizontal and vertical tile line is composed of a maximum of 6 tiles;
     * Checks that the horizontal and vertical tiles share the same characteristic.
     *
     * @param row  the row of the first tile to add.
     * @param col  the column of the first tile to add.
     * @param d    the direction of tiles placement.
     * @param line the tiles to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private int checkRulesAdd(int row, int col, Direction d, Tile... line) throws QwirkleException { 
        //Checks that the tiles to add are adjacent to an existing tile on the grid;
        adjacentTiles(row, col, d, line);
        int score = 0;
        //Checks that the horizontal and vertical tile line is composed of a maximum of 6 tiles;
        List<Tile> directionTiles = new ArrayList<>();
        Collections.addAll(directionTiles, line); //add line(tiles to add) on the list
        int colDirection = col + line.length * d.getDeltaCol();
        int rowDirection = row + line.length * d.getDeltaRow();
        while (this.tiles[rowDirection][colDirection] != null) {
            directionTiles.add(this.tiles[rowDirection][colDirection]);
            colDirection = colDirection + d.getDeltaCol();
            rowDirection = rowDirection + d.getDeltaRow();
        }
        int oppCol = col + d.opposite().getDeltaCol();
        int oppRow = row + d.opposite().getDeltaRow();
        while (this.tiles[oppRow][oppCol] != null) {
            directionTiles.add(this.tiles[oppRow][oppCol]);
            oppCol = oppCol + d.opposite().getDeltaCol();
            oppRow = oppRow + d.opposite().getDeltaRow();
        }
        if (directionTiles.size() > 6) {
            throw new QwirkleException("The tile line is already complete!");
        } else {
            //Checks that the horizontal and vertical tiles share the same characteristic.
            //for the horizontal line
            Tile[] dTiles = new Tile[directionTiles.size()];
            for (int i = 0; i < dTiles.length; i++) {
                dTiles[i] = directionTiles.get(i);
            }
            verifyColorShape(dTiles);
            if (directionTiles.size() > 1) {
                score = score + directionTiles.size(); //If rules are respected;
                directionTiles.clear();
            }
        }
        List<Tile> oppDirectionTiles = new ArrayList<>();
        Direction diagonalDirection = d.diagonal(); //Diagonal direction
        for (int i = 0; i < line.length; i++) {
            oppDirectionTiles.add(line[i]);
            int diagonalRow = row + diagonalDirection.getDeltaRow();
            int diagonalCol = col + diagonalDirection.getDeltaCol();
            while (this.tiles[diagonalRow + (i * d.getDeltaRow())][diagonalCol + (i * d.getDeltaCol())] != null) {
                oppDirectionTiles.add(this.tiles[diagonalRow + (i * d.getDeltaRow())][diagonalCol + i * d.getDeltaCol()]);
                diagonalRow = diagonalRow + diagonalDirection.getDeltaRow();
                diagonalCol = diagonalCol + diagonalDirection.getDeltaCol();
            }
            int oppDiagonalRow = row + diagonalDirection.opposite().getDeltaRow();
            int oppDiagonalCol = col + diagonalDirection.opposite().getDeltaCol();
            while (this.tiles[oppDiagonalRow + (i * d.getDeltaRow())][oppDiagonalCol + (i * d.getDeltaCol())] != null) {
                oppDirectionTiles.add(this.tiles[oppDiagonalRow + (i * d.getDeltaRow())][oppDiagonalCol + i * d.getDeltaCol()]);
                oppDiagonalRow = oppDiagonalRow + diagonalDirection.opposite().getDeltaRow();
                oppDiagonalCol = oppDiagonalCol + diagonalDirection.opposite().getDeltaCol();

            }
            if (oppDirectionTiles.size() > 6) {
                throw new QwirkleException("The tile line is already complete!");
            } else {
                //for the vertical line
                Tile[] oppTiles = new Tile[oppDirectionTiles.size()];
                for (int j = 0; j < oppTiles.length; j++) {
                    oppTiles[j] = oppDirectionTiles.get(j);
                }
                verifyColorShape(oppTiles);
            }
            if (oppDirectionTiles.size() > 1) {
                score = score + oppDirectionTiles.size();
            }
            oppDirectionTiles.clear();
        }
        return score;
    }

    /**
     * Verifies if the tiles have the same color.
     *
     * @param tiles an array of tiles to verify.
     * @return true if the tiles are of the same color or false otherwise.
     */
    private boolean sameColor(Tile[] tiles) {
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
    private boolean sameShape(Tile[] tiles) {
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
    private boolean sameTile(Tile[] tiles) {
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
                throw new QwirkleException("Tiles must share the same color or shape");
            }
        }
        if (sameTile(line)) {
            throw new QwirkleException("Cannot have the same tile twice");
        }
    }

    /**
     * Checks if the tiles to add have the same column;
     *
     * @param row list of integers containing the rows of each tile that we want to add;
     * @return true if the tiles to add have the same column, false otherwise;
     */
    private boolean sameRow(List<Integer> row) {
        //row
        for (int i = 0; i < row.size() - 1; i++) {
            if (!row.get(i).equals(row.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the tiles to add have the same row;
     *
     * @param col list of integers containing the columns of each tile that we want to add;
     * @return true if the tiles to add have the same row, false otherwise;
     */
    private boolean sameCol(List<Integer> col) {
        //row
        for (int i = 0; i < col.size() - 1; i++) {
            if (!col.get(i).equals(col.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks that the tiles are on the same line: either same column or same row;
     *
     * @param row list of integers containing the rows of each tile that we want to add;
     * @param col list of integers containing the columns of each tile that we want to add;
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private String tilesSameLine(List<Integer> row, List<Integer> col) throws QwirkleException {
        if (!sameRow(row)) {
            if (!sameCol(col)) {
                throw new QwirkleException("Tiles are not on the same line!");
            }
            return "verticalLine";
        }
        return "horizontalLine";
    }

    /**
     * Checks that there is no identical position.
     *
     * @param sum list of integers containing the sum of the row and col of each position;
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private void tilesSamePosition(List<Integer> sum) throws QwirkleException {
        for (int i = 0; i < sum.size(); i++) {
            for (int j = i + 1; j < sum.size(); j++) {
                if (sum.get(i).equals(sum.get(j))) {
                    throw new QwirkleException("You cannot put two tiles at the same position!");
                }
            }
        }
    }

    /**
     * Checks if the direction and tiles passed in parameters are well initialized.
     *
     * @param d    the direction of tiles placement.
     * @param line the tiles we want to add.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    private void exceptionValidLineDirection(Direction d, Tile... line) throws QwirkleException {
        //Checks if the tile and direction are well initialized;
        if (line == null || line.length == 0 || line.length > 6) {
            throw new QwirkleException("Invalid number of tiles!");
        }
        for (Tile tile : line) {
            if (tile == null) {
                throw new QwirkleException("Some tiles are not initialized!");
            }
        }
        if (d == null) {
            throw new QwirkleException("Invalid direction!");
        }
    }
}
