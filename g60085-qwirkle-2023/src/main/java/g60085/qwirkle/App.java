package g60085.qwirkle;

import g60085.qwirkle.model.Game;
import g60085.qwirkle.model.Player;
import g60085.qwirkle.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        View.beginning();

        int numberPlayers = View.nbPlayers();

        Game game = new Game(View.namePlayers(numberPlayers));

        View.initTiles(game);

        View.startPlayer(game);


        boolean continueGame = true;
        View.firstAdd(game);
        while(continueGame){
            View.add(game);
            System.out.println("Enter q if you want to quit");
            String quitOrNo = View.robustReadingString();
            if(quitOrNo.equals("q")){
                continueGame = false;
                View.endGame();
            }
        }
    }
}
