package g60085.qwirkle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
public class excep {
    private void checkNumberColorShapeTiles(int row, int col,Direction d, Tile...line) throws QwirkleException { //verifier que la line de tuile horizontale et verticale soit composée de maximum 6 tuiles
        List<Tile> horizontalTiles = new ArrayList<>();
        Collections.addAll(horizontalTiles, line); //ajoute line dans la liste
        int colDirection = col + line.length * d.getDeltaCol();
        while (this.tiles[row][colDirection] != null) {
            horizontalTiles.add(this.tiles[row][colDirection]);
            colDirection = colDirection + d.getDeltaCol();
        }
        int oppositeCol = col + d.opposite().getDeltaCol();
        while (this.tiles[row][oppositeCol] != null) {
            horizontalTiles.add(this.tiles[row][oppositeCol]);
            oppositeCol = oppositeCol + d.opposite().getDeltaCol();
        }
        if (horizontalTiles.size() > 6) {
            throw new QwirkleException("The tile line is already complete!");
        } else {
            //vérifier que les tuiles horizontales et verticales partagent la même caractéristique
            //pour la line horizontale
            Tile[] hTiles = new Tile[horizontalTiles.size()];
            for (int i = 0; i < hTiles.length; i++) {
                hTiles[i] = horizontalTiles.get(i);
            }
            verifyColorShape(hTiles);
        }
        List<Tile> verticalTiles = new ArrayList<>();
        for (int i = 0; i < line.length; i++) {
            verticalTiles.add(line[i]);
            int rowUp = row + Direction.UP.getDeltaRow();
            while (this.tiles[rowUp][col + i * d.getDeltaCol()] != null) {
                verticalTiles.add(this.tiles[rowUp][col + i * d.getDeltaCol()]);
                rowUp = rowUp + Direction.UP.getDeltaRow();
            }
            int rowDown = row + Direction.DOWN.getDeltaRow();
            while (this.tiles[rowDown][col + i * d.getDeltaCol()] != null) {
                verticalTiles.add(this.tiles[rowDown][col + i * d.getDeltaCol()]);
                rowDown = rowDown + Direction.DOWN.getDeltaRow();
            }
            if (verticalTiles.size() > 6) {
                throw new QwirkleException("The tile line is already complete!");
            } else {
                //pour la ligne verticale
                Tile[] vTiles = new Tile[verticalTiles.size()];
                for (int j = 0; j < vTiles.length; j++) {
                    vTiles[j] = verticalTiles.get(j);
                }
                verifyColorShape(vTiles);
            }
            verticalTiles.removeAll(verticalTiles);
        }
        */
/*//*
/verifier que la line de tuile horizontale et verticale soit composée de maximum 6 tuiles
        List<Tile> horizontalTiles = new ArrayList<>();
        horizontalTiles.add(tile);
        int colLeft = col + Direction.LEFT.getDeltaCol();
        while (this.tiles[row][colLeft] != null) {
            horizontalTiles.add(this.tiles[row][colLeft]);
            colLeft = colLeft + Direction.LEFT.getDeltaCol();
        }
        int colRight = col + Direction.RIGHT.getDeltaCol();
        while (this.tiles[row][colRight] != null) {
            horizontalTiles.add(this.tiles[row][colRight]);
            colRight = colRight + Direction.RIGHT.getDeltaCol();
        }
        if (horizontalTiles.size() > 6) {
            throw new QwirkleException("The tile line is already complete!");
        }
        List<Tile> verticalTiles = new ArrayList<>();
        verticalTiles.add(tile);

        int rowUp = row + Direction.UP.getDeltaRow();
        while (this.tiles[rowUp][col] != null) {
            verticalTiles.add(this.tiles[rowUp][col]);
            rowUp = rowUp + Direction.UP.getDeltaRow();
        }
        int rowDown = row + Direction.DOWN.getDeltaRow();
        while (this.tiles[rowDown][col] != null) {
            verticalTiles.add(this.tiles[rowDown][col]);
            rowDown = rowDown + Direction.DOWN.getDeltaRow();
        }
        if (verticalTiles.size() > 6) {
            throw new QwirkleException("The tile line is already complete!");
        }
        //vérifier que les tuiles horizontales et verticales partagent la même caractéristique
        //pour la line horizontale
        if (horizontalTiles.size() > 1) {
            Tile[] hTiles = new Tile[horizontalTiles.size()];
            for (int i = 0; i < hTiles.length; i++) {
                hTiles[i] = horizontalTiles.get(i);
            }
            verifyColorShape(hTiles);
        }
        //pour la ligne verticale
        if (verticalTiles.size() > 1) {
            Tile[] vTiles = new Tile[verticalTiles.size()];
            for (int i = 0; i < vTiles.length; i++) {
                vTiles[i] = verticalTiles.get(i);
            }
            verifyColorShape(vTiles);
        }*//*

}
*/
