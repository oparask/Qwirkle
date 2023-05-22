package g60085.qwirkle.model;

import java.io.*;
import java.util.List;
import java.util.Locale;


/**
 * The `Game` class represents the facade of the model in the Qwirkle game.
 * It encapsulates the game logic and provides a simplified interface for the view to interact with.
 * The `Game` class manages the game state, including the grid, players, and current player.
 * It allows for playing moves, switching players, and accessing game-related information.
 * The `Game` class is serializable to support saving and loading game state.
 */
public class Game implements Serializable {
    private Grid grid;
    private final Player[] players;
    private int currentPlayer;

    /**
     * Initializes the game with the given list of player names.
     *
     * @param names A list of player names.
     */
    public Game(List<String> names) {
        this.grid = new Grid();
        this.players = new Player[names.size()];

        for (int i = 0; i < names.size(); i++) {
            this.players[i] = new Player(names.get(i));
        }

        this.currentPlayer = 0;
    }

    /**
     * Reads a serialized `Game` object from a file and returns it.
     *
     * @param filename The name of the file containing the serialized `Game` object.
     * @return The deserialized `Game` object.
     * @throws QwirkleException If an I/O error occurs while reading the file
     *                          or if the class of the serialized object cannot be found.
     */
    public static Game getFromFile(String filename) throws QwirkleException {
        try (FileInputStream fileIn = new FileInputStream("game.ser/" + filename);
             ObjectInputStream objIn = new ObjectInputStream(fileIn)) {
            return (Game) objIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Locale.setDefault(Locale.ENGLISH);
            String errorMessage = "An error occurred while reading the file: " + e.getMessage();
            throw new QwirkleException(errorMessage);
        }
    }

    /**
     * Attempts to play the move for the first player.
     *
     * @param direction The tile placement direction.
     * @param indices   The indices of the tiles to play.
     * @throws QwirkleException If the move does not comply with the rules of the Qwirkle game.
     */
    public void first(Direction direction, int... indices) throws QwirkleException {
        Player firstPlayer = this.players[this.currentPlayer];
        Tile[] tiles = new Tile[indices.length];

        for (int i = 0; i < indices.length; i++) {
            tiles[i] = firstPlayer.getHand().get(indices[i]);
        }

        firstPlayer.addScore(this.grid.firstAdd(direction, tiles));
        firstPlayer.remove(tiles);
        firstPlayer.refill();
        changeCurrentPlayer();
    }

    /**
     * Attempts to play a tile for the current player.
     *
     * @param row   The row of the tile.
     * @param col   The column of the tile.
     * @param index The index of the tile.
     * @throws QwirkleException If the move does not comply with the rules of the Qwirkle game.
     */
    public void play(int row, int col, int index) throws QwirkleException {
        Player currentPlayer = this.players[this.currentPlayer];
        Tile tile = currentPlayer.getHand().get(index);
        currentPlayer.addScore(this.grid.add(row, col, tile));
        currentPlayer.remove(tile);
        currentPlayer.refill();
        changeCurrentPlayer();
    }

    /**
     * Attempts to play several aligned tiles for the current player.
     *
     * @param row     The row of the first tile.
     * @param col     The column of the first tile.
     * @param d       The tile placement direction.
     * @param indexes The indexes of the tiles.
     * @throws QwirkleException If the move does not comply with the rules of the Qwirkle game.
     */
    public void play(int row, int col, Direction d, int... indexes) throws QwirkleException {
        Player currentPlayer = this.players[this.currentPlayer];
        Tile[] tiles = new Tile[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            tiles[i] = currentPlayer.getHand().get(indexes[i]);
        }
        currentPlayer.addScore(this.grid.add(row, col, d, tiles));
        currentPlayer.remove(tiles);
        currentPlayer.refill();
        changeCurrentPlayer();
    }

    /**
     * Attempts to play any tiles for the current player.
     *
     * @param is The tiles to play, specified as row, column, and tile index triplets.
     * @throws QwirkleException If the move does not comply with the rules of the Qwirkle game.
     */
    public void play(int... is) throws QwirkleException {
        Player currentPlayer = this.players[this.currentPlayer];
        TileAtPosition[] tiles = new TileAtPosition[is.length / 3];
        Tile[] tilesToRemove = new Tile[is.length / 3];
        int tilesIndex = 0;
        for (int i = 0; i < is.length; i += 3) {
            int row = is[i];
            int col = is[i + 1];
            int tileIndex = is[i + 2];
            Tile tile = currentPlayer.getHand().get(tileIndex);
            tiles[tilesIndex] = new TileAtPosition(row, col, tile);
            tilesToRemove[tilesIndex] = tile;
            tilesIndex++;
        }
        currentPlayer.addScore(this.grid.add(tiles));
        currentPlayer.remove(tilesToRemove);
        currentPlayer.refill();
        changeCurrentPlayer();
    }

    /**
     * Returns the name or nickname of the current player.
     *
     * @return The name or nickname of the current player.
     */
    public String getCurrentPlayerName() {
        return this.players[this.currentPlayer].getName();
    }

    /**
     * Returns the names or nicknames of the players.
     *
     * @return An array containing the names or nicknames of the players.
     */
    public String[] getPlayersName() {
        String[] names = new String[this.players.length];
        for (int i = 0; i < this.players.length; i++) {
            names[i] = this.players[i].getName();
        }
        return names;
    }

    /**
     * Returns the hand of the current player.
     *
     * @return The hand of the current player.
     */
    public List<Tile> getCurrentPlayerHand() {
        return this.players[this.currentPlayer].getHand();
    }

    /**
     * Returns the score of the current player.
     *
     * @return The score of the current player.
     */
    public int getCurrentPlayerScore() {
        return this.players[this.currentPlayer].getScore();
    }

    /**
     * Returns the scores of the players.
     *
     * @return An array containing the scores of the players.
     */
    public int[] getPlayersScore() {
        int[] scores = new int[this.players.length];
        for (int i = 0; i < this.players.length; i++) {
            scores[i] = this.players[i].getScore();
        }
        return scores;
    }

    /**
     * Returns the grid view.
     *
     * @return The grid view object.
     */
    public GridView getGrid() {
        return new GridView(this.grid);
    }

    /**
     * Sets the player who will start the game.
     *
     * @param player The index of the player.
     */
    public void setStarterPlayer(int player) {
        this.currentPlayer = player;
    }

    /**
     * Changes the current player to the next player in the game.
     */
    private void changeCurrentPlayer() {
        this.currentPlayer++;
        if (this.currentPlayer == this.players.length) {
            this.currentPlayer = 0;
        }
    }

    /**
     * Initializes the hand of each player at the beginning of the game.
     * Fills the hand of each player with tiles.
     */
    public void initPlayerHand() {
        for (Player player : this.players) {
            player.refill();
        }
    }

    /**
     * Passes the turn to the next player when the current player is unable to play a move.
     */
    public void pass() {
        Player currentPlayer = this.players[this.currentPlayer];
        currentPlayer.addScore(-1);
        changeCurrentPlayer();
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over: either one of the players has reached 0 points, or
     * one of the players has played all their tiles,
     * or none of the players can add tiles to the existing lines.
     */
    public boolean isOver() {
        boolean isOver = false;
        Bag bag = Bag.getInstance();
        // Check if any player has 0 points
        for (Player player : this.players) {
            if (player.getScore() == 0) {
                isOver = true; // Game is over
                break;
            }
        }
        if (bag.size() == 0) { // no more tiles in the bag
            // Game over if one of the players has played all their tiles
            for (Player player : this.players) {
                if (player.getHand().size() == 0) {
                    player.addScore(6); // add 6 points to the player that has played all their tiles
                    isOver = true; // Game is over
                }
            }
            // Game over if none of the players can add tiles to the existing lines
            if (!isOver && nobodyCanPlay()) {
                isOver = true; // Game is over
            }
        }
        return isOver;
    }

    /**
     * Checks if none of the players can add tiles to the existing lines.
     *
     * @return true if none of the players can add tiles to the existing lines, false otherwise.
     */
    private boolean nobodyCanPlay() {
        boolean nobodyCanPlay = true;
        int indexPlayer = 0;
        while (indexPlayer < this.players.length && nobodyCanPlay) {
            if (canAddTilesFromHand(indexPlayer)) {
                nobodyCanPlay = false;
            }
            indexPlayer++;
        }
        return nobodyCanPlay;
    }

    /**
     * Checks if the player at the given index can add any tiles from their hand to the existing lines on the grid.
     *
     * @param indexPlayer the index of the player to check.
     * @return true if the player can add tiles from their hand to the existing lines, false otherwise.
     */
    private boolean canAddTilesFromHand(int indexPlayer) {
        boolean canAddTile = false;

        int indexRow = 0;
        while (indexRow < 89 && !canAddTile) { // For each row;
            int indexCol = 0;
            while (indexCol < 89 && !canAddTile) { // For each column;
                if (this.grid.get(indexRow, indexCol) != null) { // If there is a tile on the grid;
                    // Checks if there is a tile from the hand's player that can be added on the game grid before this tile;
                    int indexTileHand = 0;
                    Player player = this.players[indexPlayer];
                    while (indexTileHand < player.getHand().size() && !canAddTile) {
                        canAddTile = grid.validRulesAdd(indexRow, indexCol - 1, Direction.RIGHT,
                                getCurrentPlayerHand().get(indexTileHand));
                        indexTileHand++;
                    }
                    if (!canAddTile) { // If none of the tiles from the player's hand can be added;
                        // Continues iterating through the grid while there is a tile;
                        while (this.grid.get(indexRow, indexCol) != null) {
                            indexCol++;
                        }
                    }
                } else {
                    indexCol++; // Advances if there is no tile;
                }
            }
            indexRow++;
        }
        return canAddTile;
    }

    /**
     * Writes the specified Game object to a file using serialization.
     *
     * @param game     The Game object to be serialized and written to a file.
     * @param filename The name of the file to write the serialized Game object to.
     * @throws QwirkleException If an error occurs while writing the Game object.
     */
    public static void write(Game game, String filename) throws QwirkleException {
        try (FileOutputStream fileOut = new FileOutputStream("game.ser/" + filename);
             ObjectOutputStream objOut = new ObjectOutputStream(fileOut)) {
            objOut.writeObject(game);
        } catch (IOException e) {
            throw new QwirkleException("An error occurred while writing the Game object to the file: " + e.getMessage());
        }
    }

    //for tests
    /**
     * @return the array of players.
     */
    public Player[] getPlayers() {
        return players;
    }
}

