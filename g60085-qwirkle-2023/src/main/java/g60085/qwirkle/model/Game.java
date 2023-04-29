package g60085.qwirkle.model;

import java.util.List;

/**
 * Game represents the facade of the model.
 * It is with this class that the view interacts.
 */
public class Game {
    private final Grid grid;
    private final Player[] players;
    private int currentPlayer;

    /**
     * Initializes the array of players, the game grid and initialize the current player to 0.
     *
     * @param players a list of String representing the names of the players and players.
     */
    public Game(List<Player> players) {
        this.grid = new Grid();
        this.players = new Player[players.size()];
        for (int i = 0; i < players.size(); i++) {
            this.players[i] = players.get(i);
        }
        this.currentPlayer = 0;
    }

    //pas sure

    /**
     * Gives access to the attribute "Players".
     *
     * @return the attribute players.
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Attempts to play the move for the first player.
     *
     * @param d  tile placement direction.
     * @param is tiles to play.
     */
    public void first(Direction d, int... is) {
        try {
            Player firstPlayer = this.players[this.currentPlayer];
            Tile[] tiles = new Tile[is.length];
            for (int i = 0; i < is.length; i++) {
                tiles[i] = firstPlayer.getHand().get(is[i]);
            }
            this.grid.firstAdd(d, tiles);
            firstPlayer.remove(tiles);
            firstPlayer.refill();
            changeCurrentPlayer();
        } catch (QwirkleException e) {
            throw new QwirkleException(e.getMessage());
        }
    }

    /**
     * Attempts to play one tile for the current player.
     *
     * @param row   the row of the tile.
     * @param col   the column of the tile.
     * @param index the index of the tile.
     */
    public void play(int row, int col, int index) {
        try {
            Player player = this.players[this.currentPlayer];
            Tile tile = player.getHand().get(index);
            this.grid.add(row, col, tile);
            player.remove(tile);
            player.refill();
            changeCurrentPlayer();
        } catch (QwirkleException e) {
            throw new QwirkleException(e.getMessage());
        }
    }

    /**
     * Attempts to play several aligned tiles for the current player.
     *
     * @param row     the row of the first tile.
     * @param col     the column of the first tile.
     * @param d       tile placement direction.
     * @param indexes indexes of the tiles.
     */
    public void play(int row, int col, Direction d, int... indexes) {
        try {
            Player player = this.players[this.currentPlayer];
            Tile[] tiles = new Tile[indexes.length];
            for (int i = 0; i < indexes.length; i++) {
                tiles[i] = player.getHand().get(indexes[i]);
            }
            this.grid.add(row, col, d, tiles);
            player.remove(tiles);
            player.refill();
            changeCurrentPlayer();
        } catch (QwirkleException e) {
            throw new QwirkleException(e.getMessage());
        }
    }

    /**
     * Attempts to play any tiles for the current player.
     *
     * @param is tiles to play.
     */
    public void play(int... is) {
        try {
            Player player = this.players[this.currentPlayer];
            TileAtPosition[] tiles = new TileAtPosition[is.length / 3];
            Tile[] tilesToRemove = new Tile[is.length / 3];
            int tilesIndex = 0;
            for (int i = 0; i < is.length; i = i + 3) {
                int row = is[i];
                int col = is[i + 1];
                Tile tile = player.getHand().get(is[i + 2]);
                tiles[tilesIndex] = new TileAtPosition(row, col, tile);
                tilesToRemove[tilesIndex] = tile;
                tilesIndex++;
            }
            this.grid.add(tiles);
            player.remove(tilesToRemove);
            player.refill();
            changeCurrentPlayer();
        } catch (QwirkleException e) {
            throw new QwirkleException(e.getMessage());
        }
    }

    /**
     * @return the name or nickname of the current player.
     */
    public String getCurrentPlayerName() {
        return this.players[this.currentPlayer].getName();
    }

    /**
     * @return the hand of the current player.
     */
    public List<Tile> getCurrentPlayerHand() {
        return this.players[this.currentPlayer].getHand();
    }

    /**
     * Passes turn when unable to play.
     */
    public void pass() {
        changeCurrentPlayer();
    }

    /**
     * Creates a grid view.
     *
     * @return the grid view.
     */
    public GridView getGrid() {
        return new GridView(this.grid);
    }

    //pas sure

    /**
     * Initializes the player who's starting the game.
     *
     * @param name the name of player.
     */
    public void setCurrentPlayer(String name) {
        int i = 0;
        while (i < this.players.length && !this.players[i].getName().equals(name)) {
            i = i + 1;
        }
        if (i == this.players.length) {
            System.out.println("This name doesn't exist! : ");
        } else {
            this.currentPlayer = i;
        }
    }

    /**
     * Passes the turn to the next player.
     */
    private void changeCurrentPlayer() {
        this.currentPlayer++;
        if (this.currentPlayer == this.players.length) {
            this.currentPlayer = 0;
        }
    }

}
