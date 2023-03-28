package g60085.qwirkle.view;

import g60085.qwirkle.model.*;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        System.out.println("DEV2: jeu de QWIRKLE");

/*
        System.out.println(Arrays.toString(Color.values())); //tableau reprenant toutes le couleurs de l'enumeration

        for(int i = 0; i<Color.values().length; i++){
            System.out.print(Color.values()[i] + ", ");
        }*/


     /*
        Tile tile = new Tile(Color.RED, Shape.SQUARE);
        System.out.println(tile); //Grace à la methode toString
        //c'est ici qu'on donnera les vrai valeur des shape et color
     */
    /*
        Tile_Bis tile_bis = new Tile_Bis(Color.BLUE, Shape.CROSS);
        System.out.println(tile_bis); //Grace à la methode toString;
        mais par defaut record fait aussi la methode ToString qu'on a crée
        tile_bis.color(); //getter que le record a cree lui meme;
     */


       /* Bag tiles = new Bag();
        System.out.println(tiles);*/

        System.out.println(Color.values().length);
        System.out.println(Shape.values().length);



    }

}
