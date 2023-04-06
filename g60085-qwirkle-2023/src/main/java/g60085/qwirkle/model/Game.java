package g60085.qwirkle.model;

import java.util.List;

public class Game {
    private Grid grid;
    private Player[] players;
    private int currentPlayer;

    public Game(List<Player> players) {
        this.grid = new Grid();
        this.players = new Player[players.size()];
        for (int i = 0; i < players.size(); i++) {
            this.players[i] = players.get(i);
        }
        this.currentPlayer = 0;
    }

    //pas sure
    public Player[] getPlayers() {
        return this.players;
    }

    public void first(Direction d, int... is) {
        Player firstPlayer = this.players[this.currentPlayer];
        Tile[] tiles = new Tile[is.length];
        for (int i = 0; i < is.length; i++) {
            tiles[i] = firstPlayer.getTiles().get(is[i]);
        }
        this.grid.firstAdd(d, tiles);
        //System.out.println(getCurrentPlayerHand());
        firstPlayer.remove(tiles);
        firstPlayer.refill();
        //System.out.println(getCurrentPlayerHand());
        changeCurrentPlayer();
    }

    public void play(int row, int col, int index) {
        Player player = this.players[this.currentPlayer];
        Tile tile = player.getTiles().get(index);
        this.grid.add(row, col, tile);
        player.remove(tile);
        player.refill();
        changeCurrentPlayer();
    }

    public void play(int row, int col, Direction d, int... indexes) {
        Player player = this.players[this.currentPlayer];
        Tile[] tiles = new Tile[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            tiles[i] = player.getTiles().get(indexes[i]);
        }
        this.grid.add(row, col, d, tiles);
        player.remove(tiles);
        player.refill();
        changeCurrentPlayer();
    }

    public void play(int... is) {
        Player player = this.players[this.currentPlayer];
        TileAtPosition[] tiles = new TileAtPosition[is.length / 3];
        Tile[] tilesToRemove = new Tile[is.length / 3];
        int tilesIndex = 0;
        for (int i = 0; i < is.length; i = i + 3) {
            int row = is[i];
            int col = is[i + 1];
            Tile tile = player.getTiles().get(is[i + 2]);
            tiles[tilesIndex] = new TileAtPosition(row, col, tile);
            tilesToRemove[tilesIndex] = tile;
            tilesIndex++;
        }
        this.grid.add(tiles);
        player.remove(tilesToRemove);
        player.refill();
        changeCurrentPlayer();
    }

    public String getCurrentPlayerName() {
        return this.players[this.currentPlayer].getName();
    }

    public List<Tile> getCurrentPlayerHand() {

        return this.players[this.currentPlayer].getTiles();
    }

    public void pass() throws QwirkleException {
        throw new QwirkleException("Coup impossible");
    }

    public GridView getGrid() {
        GridView gridView = new GridView(this.grid);
        return gridView;
    }

    //pas sure
    public void setCurrentPlayer(String name) {
        int i = 0;
        while (i < this.players.length && !this.players[i].getName().equals(name)) {
            i= i+1;
        }
        if (i == this.players.length) {
            System.out.println("This name doesn't exist, try again! : ");
        } else {
            this.currentPlayer = i;
        }
    }

    public void changeCurrentPlayer(){
        this.currentPlayer++;
        if(this.currentPlayer == this.players.length){
            this.currentPlayer = 0;
        }
    }

}
