package g60085.qwirkle.view;

import g60085.qwirkle.model.*;

public class App {
    public static void main(String[] args) {
        System.out.println("DEV2: jeu de QWIRKLE");
        System.out.println();

/*
        System.out.println(Arrays.toString(Color.values())); //tableau reprenant toutes le couleurs de l'enumeration

        for(int i = 0; i<Color.values().length; i++){
            System.out.print(Color.values()[i] + ", ");
        }*/


        /*Tile_bis tile = new Tile_bis(Color.RED, Shape.SQUARE);
        System.out.println(tile); //Grace à la methode toString
        //c'est ici qu'on donnera les vrai valeur des shape et color*/

    /*
        Tile_Bis tile_bis = new Tile_Bis(Color.BLUE, Shape.CROSS);
        System.out.println(tile_bis); //Grace à la methode toString;
        mais par defaut record fait aussi la methode ToString qu'on a crée
        tile_bis.color(); //getter que le record a cree lui meme;
     */


       /* Bag tiles = new Bag();
        System.out.println(tiles);*/
/*

        System.out.println(Color.values().length);
        System.out.println(Shape.values().length);

*/
        Bag bag1 = Bag.getInstance();
        System.out.println("bag1: " + bag1);
        Bag bag2 = Bag.getInstance();
        System.out.println("bag2: " + bag2);
        if (bag1 == bag2) {
            System.out.println("bag1 et bag2 referance le meme objet");
        }
        System.out.println("La taille de bag au départ: " + bag1.size() + " tuiles");
        System.out.println();

        Tile[] randomTilesBag = bag1.getRandomTiles(3);
        afficherTab(randomTilesBag);;


        System.out.println("La taille de bag apres avoir enleve des tuiles : " + bag2.size());
        System.out.println(bag1);

        //pas sassez de tuiles dans le sac:
        Tile[] randomTilesBag2 = bag1.getRandomTiles(106);
        afficherTab(randomTilesBag2);


    }

    public static void afficherTab(Tile[]randomTilesBag) {
        System.out.print("rabdonTilesBag: " + "[" + randomTilesBag[0] + ", ");
        for (int i = 1; i < randomTilesBag.length - 1; i++) {
            System.out.print(randomTilesBag[i] + ", ");
        }
        System.out.println(randomTilesBag[randomTilesBag.length - 1] + "]");
        System.out.println();
    }
}
