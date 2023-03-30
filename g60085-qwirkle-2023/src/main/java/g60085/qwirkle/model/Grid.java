package g60085.qwirkle.model;

/**
 * Grid represents the game grid
 */
public class Grid {
    private Tile[][] tiles;
    private boolean isEmpty;

    //constructeur
    public Grid() {
        this.tiles = new Tile[91][91];
        this.isEmpty = true;
    }

    //getter
    public Tile get(int row, int col) {
        return this.tiles[row][col];
    }

    //ajouter les premieres tuiles
    public void firstAdd(Direction d, Tile... line){ //var rags; line represente un tableau de tuiles passees en parametres
        if(!isEmpty){
        }
        this.tiles[45][45] = line[0];
        for(int i =1; i< line.length ; i++){
            
        }


    }
}
