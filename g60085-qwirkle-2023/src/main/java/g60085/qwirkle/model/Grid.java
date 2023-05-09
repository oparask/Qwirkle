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
            if (!verifyColorShape(line)) {
                throw new QwirkleException("Tiles must share the same color or shape");
            }
            if (sameTile(line)) {
                throw new QwirkleException("You cannot add the same tile");
            }
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
        if (!validPosition(row, col, Direction.RIGHT, tile)) { //peu importe la direction
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
            /*throw new QwirkleException("The tile line is already complete!");
            throw new QwirkleException("Tiles must share the same color or shape");
            throw new QwirkleException("You cannot add the same tile");*/
            throw new QwirkleException("Does not respect the rules of qwirkle game!");
        }
        // We can add the tiles;
        this.tiles[row][col] = tile;
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
        if (!validPosition(row, col, d, line)) { //peu importe la direction
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
            /*throw new QwirkleException("The tile line is already complete!");
            throw new QwirkleException("Tiles must share the same color or shape");
            throw new QwirkleException("You cannot add the same tile");*/
            throw new QwirkleException("Does not respect the rules of qwirkle game!");
        }
        // We can add the tiles;
        for (int i = 0; i < line.length; i++) {
            int newRow = row + i * d.getDeltaRow();
            int newCol = col + i * d.getDeltaCol();
            this.tiles[newRow][newCol] = line[i];
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
            // Checks that the tiles to be added share the same characteristic;
            if (!verifyColorShape(lineTiles)) {
                throw new QwirkleException("Tiles must share the same color or shape");
            }
            // Checks that there are not two identical tiles;
            if (sameTile(lineTiles)) {
                throw new QwirkleException("You cannot add the same tile");
            }
            List<Integer> tilesRow = new ArrayList<>();
            List<Integer> tilesCol = new ArrayList<>();
            List<Integer> sumRowCol = new ArrayList<>();
            for (TileAtPosition tileAtPosition : line) {
                int row = tileAtPosition.row();
                tilesRow.add(row);
                int col = tileAtPosition.col();
                tilesCol.add(col);
                Tile tile = tileAtPosition.tile();
                if (!validPosition(row, col, Direction.RIGHT, tile)) { //peu importe la direction
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
                if (tilesSamePosition(sumRowCol)) {
                    throw new QwirkleException("You cannot put two tiles at the same position!");
                }
            }
            for (TileAtPosition tileAtPosition : line) {
                //Checks that the tile to add is adjacent to an existing tile on the grid;
                //Checks that the horizontal and vertical tile line is composed of a maximum of 6 tiles;
                //Checks that the horizontal and vertical tiles share the same characteristic;
                int newRow = tileAtPosition.row();
                int newCol = tileAtPosition.col();
                Tile tile = tileAtPosition.tile();
                if (!adjacentTiles(newRow, newCol, Direction.RIGHT, tile)) {
                    throw new QwirkleException("The tiles are not connected to another tile!");
                }
                if (!validRulesAdd(newRow, newCol, Direction.RIGHT, tile)) {
                    throw new QwirkleException("Does not respect the rules of qwirkle game!");
                }
            }
            // We place the tiles
            for (TileAtPosition tileAtPosition : line) {
                this.tiles[tileAtPosition.row()][tileAtPosition.col()] = tileAtPosition.tile();
            }
            return scoreRulesAdd3(line);
        }

    }

    /**
     * @return true if the grid is empty, false otherwise;
     */
    public boolean isEmpty() {
        return this.isEmpty;
    }

    private boolean adjacentTiles(int row, int col, Direction d, Tile... line) {
        boolean adjacent = true;
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
                    adjacent = false;
                }
            }
        }
        return adjacent;
    }

    private boolean validRulesAdd(int row, int col, Direction d, Tile... line) {
        boolean valid = true;
        //List of all the tiles found on the line of the tile passed as parameter
        List<Tile> tilesFromTheLine = tilesFromTheLine(row, col, d, line);
        //Checks that the horizontal and vertical tile line is composed of a maximum of 6 tiles;
        Tile[] tilesTab = tilesFromTheLine.toArray(Tile[]::new);
        if (lineCompleted(tilesTab)) {
            return false;
        } else {
            if (!verifyColorShape(tilesTab)) {
                return false;
            }
            if (sameTile(tilesTab)) {
                return false;
            }
        }
        //List of all the tiles found on the opposite line of the tile passed as parameter
        for (int i = 0; i < line.length; i++) {
            //besoin de savoir ou se trouve cette tuile
            int tileColumn = col + i * d.getDeltaCol();
            int tileRow = row + i * d.getDeltaRow();
            //je peux appeler la methode qui reprend les tuiles autour
            List<Tile> tilesFromThOppositeLine = tilesFromTheLine(tileRow, tileColumn, d.diagonal(), line[i]); //Diagonal direction
            Tile[] oppTilesTab = tilesFromThOppositeLine.toArray(Tile[]::new);
            if (lineCompleted(oppTilesTab)) {
                return false;
            } else {
                if (!verifyColorShape(oppTilesTab)) {
                    return false;
                }
                if (sameTile(oppTilesTab)) {
                    return false;
                }
            }
            tilesFromThOppositeLine.clear();
        }
        return valid;
    }

    private List<Tile> tilesFromTheLine(int row, int col, Direction d, Tile... line) {
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
        return directionTiles;
    }

    private boolean lineCompleted(Tile[] tiles) {
        return tiles.length > 6;
    }

    private boolean sameColor(Tile[] tiles) {
        int i = 1;
        while (i < tiles.length && tiles[0].color() == tiles[i].color()) {
            i++;
        }
        return i >= tiles.length;
    }

    private boolean sameShape(Tile[] tiles) {
        int i = 1;
        while (i < tiles.length && tiles[0].shape() == tiles[i].shape()) {
            i++;
        }
        return i >= tiles.length;
    }

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

    private boolean verifyColorShape(Tile... line) {
        if (!sameColor(line)) {
            if (!sameShape(line)) {
                return false;
            }
        }
        return true;
    }

    private boolean sameRow(List<Integer> row) {
        for (int i = 0; i < row.size() - 1; i++) {
            if (!row.get(i).equals(row.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    private boolean sameCol(List<Integer> col) {
        for (int i = 0; i < col.size() - 1; i++) {
            if (!col.get(i).equals(col.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    private boolean tilesSameLine(List<Integer> row, List<Integer> col) {
        boolean valid = true;
        if (!sameRow(row)) {
            if (!sameCol(col)) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean tilesSamePosition(List<Integer> sum) {
        boolean samePosition = false;
        for (int i = 0; i < sum.size(); i++) {
            for (int j = i + 1; j < sum.size(); j++) {
                if (sum.get(i).equals(sum.get(j))) {
                    samePosition = true;
                    break;
                }
            }
        }
        return samePosition;
    }

    private boolean validPosition(int row, int col, Direction d, Tile... line) {
        boolean valid = true;
        //Checks that the position of the first and last tile is valid:
        //all "row" and "col" must be in the grid, i.e. 91*91;
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

    private boolean validLine(Tile... line) {
        //Checks if the line is well initialized;
        return line != null && line.length != 0 && line.length <= 6;
    }

    private boolean validDirection(Direction d) {
        //Checks if the direction is well initialized;
        return d != null;
    }

    private boolean validTile(Tile... line) {
        boolean valid = true;
        //Checks if the tile are well initialized;
        for (Tile tile : line) {
            if (tile == null) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    private boolean existAlreadyTile(int row, int col, Direction d, Tile... line) {
        //Checks that at this position of the grid does not already exist a tile;
        boolean exist = false;
        for (int i = 0; i < line.length; i++) {
            if (this.tiles[row + d.getDeltaRow() * i][col + d.getDeltaCol() * i] != null) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    private int score(int row, int col, Direction d, Tile... line) {
        int score = 0;
        List<Tile> tilesFromTheLine = tilesFromTheLine(row, col, d, line);
        if (tilesFromTheLine.size() > 1) {
            score = score + tilesFromTheLine.size(); //If rules are respected;
        }
        if (tilesFromTheLine.size() == 6) {
            score = score + 6; //Qwirkle
        }
        //List of all the tiles found on the opposite line of the tile passed as parameter
        for (int i = 0; i < line.length; i++) {
            int tileColumn = col + i * d.getDeltaCol();
            int tileRow = row + i * d.getDeltaRow();
            List<Tile> tilesFromThOppositeLine = tilesFromTheLine(tileRow, tileColumn, d.diagonal(), line[i]);//Diagonal direction
            if (tilesFromThOppositeLine.size() > 1) {
                score = score + tilesFromThOppositeLine.size(); //If rules are respected;
            }
            if (tilesFromThOppositeLine.size() == 6) {
                score = score + 6; //Qwirkle
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
            ;// It doesn't matter the direction here
        }
        //remove from the score the number of tiles that are repeated on the same line
        Direction d;
        if (sameRow(tilesRow)) { //si line horizontale
            d = Direction.RIGHT;
        } else {
            d = Direction.UP;
        }
        List<TileAtPosition> lineList = new ArrayList<>();
        Collections.addAll(lineList, line);
        for (int i = 0; i < lineList.size(); i++) {//for each tileAtPosition
            int row = lineList.get(i).row();
            int col = lineList.get(i).col();
            Tile tile = lineList.get(i).tile();
            List<Tile> directionTiles = tilesFromTheLine(row, col, d, tile);
            int nbTiles = 0;
            //verify if there is several tiles that we want to add in the list
            for (int j = 0; j < lineList.size(); j++) {
                if (directionTiles.contains(lineList.get(j).tile())) {
                    nbTiles++;
                    lineList.remove(j);
                    j--;
                }
            }
            //We had to remove at least one tile from the list;
            i--;
            score = score - (nbTiles - 1) * directionTiles.size();
        }
        return score;
    }

}


