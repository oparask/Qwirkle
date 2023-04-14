package g60085.qwirkle;

import g60085.qwirkle.model.Game;
import g60085.qwirkle.view.View;

/**
 * App controls the application.
 * It manages user inputs, will take care of relaying the actions to the model
 * and will request the different displays at view.
 */
public class App {
    /**
     * Contains the flow of the game.
     * 
     * @param args
     */
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
