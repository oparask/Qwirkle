package g60085.qwirkle.model;

import java.io.*;
import java.util.List;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;


/**
 * Game represents the facade of the model.
 * It is with this class that the view interacts.
 */
public class Game implements Serializable {
    private Grid grid;
    private final Player[] players;
    private int currentPlayer;

    private Bag bag; //for serializable

    /**
     * Initializes the array of players, the game grid and initialize the current player to 0.
     *
     * @param players a list of String representing the names of the players and players.
     */
    public Game(List<String> players) {
        this.grid = new Grid();
        this.players = new Player[players.size()];
        for (int i = 0; i < players.size(); i++) {
            this.players[i] = new Player(players.get(i));
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
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public void first(Direction d, int... is) throws QwirkleException {
        Player firstPlayer = this.players[this.currentPlayer];
        Tile[] tiles = new Tile[is.length];
        for (int i = 0; i < is.length; i++) {
            tiles[i] = firstPlayer.getHand().get(is[i]);
        }
        firstPlayer.addScore(this.grid.firstAdd(d, tiles));
        firstPlayer.remove(tiles);
        firstPlayer.refill();
        changeCurrentPlayer();
    }

    /**
     * Attempts to play one tile for the current player.
     *
     * @param row   the row of the tile.
     * @param col   the column of the tile.
     * @param index the index of the tile.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public void play(int row, int col, int index) throws QwirkleException {
        Player player = this.players[this.currentPlayer];
        Tile tile = player.getHand().get(index);
        player.addScore(this.grid.add(row, col, tile));
        player.remove(tile);
        player.refill();
        changeCurrentPlayer();
    }

    /**
     * Attempts to play several aligned tiles for the current player.
     *
     * @param row     the row of the first tile.
     * @param col     the column of the first tile.
     * @param d       tile placement direction.
     * @param indexes indexes of the tiles.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public void play(int row, int col, Direction d, int... indexes) throws QwirkleException {
        Player player = this.players[this.currentPlayer];
        Tile[] tiles = new Tile[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            tiles[i] = player.getHand().get(indexes[i]);
        }
        player.addScore(this.grid.add(row, col, d, tiles));
        player.remove(tiles);
        player.refill();
        changeCurrentPlayer();
    }

    /**
     * Attempts to play any tiles for the current player.
     *
     * @param is tiles to play.
     * @throws QwirkleException if it doesn't respect the rules of the Qwirkle game.
     */
    public void play(int... is) throws QwirkleException {
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
        player.addScore(this.grid.add(tiles));
        player.remove(tilesToRemove);
        player.refill();
        changeCurrentPlayer();
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
     * @return the score of the current player.
     */
    public int getCurrentPlayerScore() {
        return this.players[this.currentPlayer].getScore();
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
     * @param player the index of the player.
     */
    public void setCurrentPlayer(int player) {
        this.currentPlayer = player;
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

    /**
     * Checks if game is over;
     *
     * @return true if game is over : one of the players has played all his tiles;
     * or if none of the players can add tiles to the existing lines;
     */
    public boolean isOver() {
        boolean isOver = false;
        Bag bag = Bag.getInstance();
        if (bag.size() == 0) { //no more tiles
            //Game over if one of the players has played all his tiles;
            for (Player player : this.players) {
                if (player.getHand().size() == 0) {
                    player.addScore(6);//add 6 points to the player that has played all his tiles
                    isOver = true;//Game is over
                }
            }
            //Game over if none of the players can add tiles to the existing lines;
            if (!isOver && nobodyCanPlay()) {
                isOver = true;//game is not over
            }
        }
        return isOver;
    }

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

    private boolean canAddTilesFromHand(int indexPlayer) {
        boolean canAddTile = false;

        int indexRow = 0; //pour chaque ligne
        while (indexRow < 89 && !canAddTile) {
            int indexCol = 0;
            while (indexCol < 89 && !canAddTile) { //pour chaque colonne
                if (this.grid.get(indexRow, indexCol) != null) { //si il y a une tuile sur la grille
                    //je verifie que avant cette tuile je peux placer une de mes tuiles
                    int indexTileHand = 0;
                    Player player = this.players[indexPlayer];
                    while (indexTileHand < player.getHand().size() && !canAddTile) {
                        canAddTile = grid.validRulesAdd(indexRow, indexCol - 1, Direction.RIGHT,
                                getCurrentPlayerHand().get(indexTileHand));
                        indexTileHand++;
                    }
                    if (!canAddTile) { //si je ne peut pas ajouter une des mes tuiles
                        //parcourir toutes les tuiles qui se trouvent apres la tuile de la grille
                        while (this.grid.get(indexRow, indexCol) != null) {//tant qu'il y a une tuile, avancer
                            indexCol++;
                        }
                    }
                } else {
                    indexCol++; //j'avance si y a pas de tuile
                }
            }
            indexRow++;
        }
        return canAddTile;
    }


    //serialiser
    public static void write(Game game, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream("game.ser/" +filename);
             ObjectOutputStream objOut = new ObjectOutputStream(fileOut)) {
            objOut.writeObject(game);
            System.out.println("Le jeu a été sérialisé avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sérialisation du jeu : " + e.getMessage());
        }
    }

    public static Game getFromFile(String filename) {
        try (FileInputStream fileIn = new FileInputStream("game.ser/" + filename);
             ObjectInputStream objIn = new ObjectInputStream(fileIn)) {
            Game game = (Game) objIn.readObject();
            System.out.println("Le jeu a été désérialisé avec succès !");
            return game;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la désérialisation du jeu : " + e.getMessage());
            return null;
        }
    }




}

