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
    public void firstAdd(Direction d, Tile... line) throws QwirkleException { //var rags; line represente un tableau de tuiles passees en parametres
        if (line == null) {
            throw new QwirkleException("line is not initalized");
        }

        if (!this.isEmpty) {
            throw new QwirkleException("Not empty");
        }

        if (line.length > 6) {
            throw new QwirkleException("The length is above the limit");
        }
        if (line.length > 1) {
            if (!sameColor(line)) {
                if (!sameShape(line)) {
                    throw new QwirkleException("Tiles must be of the same color or shape");
                }
            }
            if (sameTile(line)) {
                throw new QwirkleException("Cannot have the same tile twice");
            }
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

    public boolean sameColor(Tile[] tiles) {
        int i = 1;
        while (i < tiles.length && tiles[0].color() == tiles[i].color()) {
            i++;
        }
        return i < tiles.length ? false : true;
    }

    public boolean sameShape(Tile[] tiles) {
        int i = 1;
        while (i < tiles.length && tiles[0].shape() == tiles[i].shape()) {
            i++;
        }
        return i < tiles.length ? false : true;
    }

    public boolean sameTile(Tile[] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = i + 1; j < tiles.length; j++) {
                if (tiles[i].equals(tiles[j])) {
                    return true;
                }
            }
        }
        // aucun doublon n'est trouvé
        return false;
    }

    public void isEmpty() {
        this.isEmpty = false;
    }


    //ajoute une tuile à une certaine position en respectant les règles du jeu
    public void add(int row, int col, Tile tile) throws QwirkleException {
        if (row > 91 || row < 0 || col > 91 || col < 0) {
            throw new NullPointerException();
        }
        if (row == 45 && col == 45) {
            throw new QwirkleException("at this position you must call the firtAdd method");
        }
        if (this.tiles[row][col] != null) {
            throw new QwirkleException("there is already a tile at this position");
        }
        this.tiles[row][col] = tile;
    }

    public void add(int row, int col, Direction d, Tile... line) {
        if (line == null) {
            throw new QwirkleException("line is not initalized");
        }
        if (row > 91 || row < 0 || col > 91 || col < 0) {
            throw new NullPointerException();
        }
        if (row == 45 && col == 45) {
            throw new QwirkleException("at this position you must call the firtAdd method");
        }
        if (this.tiles[row][col] != null) {
            throw new QwirkleException("there is already a tile at this position");
        }
        if (line.length > 6) {
            throw new QwirkleException("The length is above the limit");
        }
        if (line.length > 1) {
            if (!sameColor(line)) {
                if (!sameShape(line)) {
                    throw new QwirkleException("Tiles must be of the same color or shape");
                }
            }
            if (sameTile(line)) {
                throw new QwirkleException("Cannot have the same tile twice");
            }
        }
        if (row + line.length > 91 || row - line.length < 0 || col + line.length > 91 || col - line.length < 0) {
            throw new QwirkleException("Cannot have the same tile twice");
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

    public void add(TileAtPosition... line) {
        if (line == null) {
            throw new QwirkleException("line is not initalized");
        }
        if (line.length > 6) {
            throw new QwirkleException("The length is above the limit");
        }

        Tile[] lineTiles = new Tile[line.length];
        for (int i = 0; i < line.length; i++) {
            lineTiles[i] = line[i].tile();
        }
        if (line.length > 1) {
            if (!sameColor(lineTiles)) {
                if (!sameShape(lineTiles)) {
                    throw new QwirkleException("Tiles must be of the same color or shape");
                }
            }
            if (sameTile(lineTiles)) {
                throw new QwirkleException("Cannot have the same tile twice");
            }
        }

        for (int i = 0; i < line.length; i++) {
            if (line[i].row() > 91 || line[i].row() < 0 || line[i].col() > 91 || line[i].col() < 0) {
                throw new NullPointerException();
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


}
