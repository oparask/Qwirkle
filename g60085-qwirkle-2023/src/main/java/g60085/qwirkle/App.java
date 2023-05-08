package g60085.qwirkle;

import g60085.qwirkle.model.*;
import g60085.qwirkle.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static g60085.qwirkle.view.View.*;

/**
 * App controls the application.
 * It manages user inputs, will take care of relaying the actions to the model
 * and will request the different displays at view.
 */
public class App {
    /**
     * Contains the flow of the game.
     *
     * @param args An array of strings;
     */
    public static void main(String[] args) {
        View.beginning();

        int numberPlayers = nbPlayers(); // Asks for the number of players;
        Game game = new Game(namePlayers(numberPlayers));
        // Shows the players;
        initTiles(game); // Initializes each player's hand;
        startPlayer(game); // Asks who is starting the game;
        View.displayPlayer(game.getCurrentPlayer());
        firstAdd(game); // First attempt;

        Bag bag = Bag.getInstance();
        List<String> playerPass = new ArrayList<>();
        boolean continueGame = true;
        while (continueGame) {

            if (bag.size() == 0) {
                System.out.println("The bag is empty! You are almost at the end of the game!");
            }
            add(game, playerPass);//play one, several or multiple tiles;
            System.out.println(ANSI_GREEN + "Enter 'q' if you want to quit" + ANSI_RESET);
            String quitOrNo = robustReadingString();
            if (quitOrNo.equalsIgnoreCase("q")) {
                continueGame = false;
                View.endGame(winner(game));
            }
            if (game.isOver(playerPass)) {
                continueGame = false;
                View.endGame(winner(game));
            }
        }
    }

    /**
     * Asks what type of addition of tiles we want to make.
     *
     * @param game the Qwirkle game.
     */
    public static void add(Game game, List<String> playerPass) {
        System.out.println();
        View.display(game.getGrid());
        View.displayPlayer(game.getCurrentPlayer()); //Shows the player's hand;
        System.out.print("Enter the type of play (f, o, l, m, p) : ");
        String play = robustReadingAddType();
        switch (play.toLowerCase()) {
            case "f" -> {
                playerPass.clear();
                firstAdd(game);
            }
            case "o" -> {
                playerPass.clear();
                playOneTile(game);
            }
            case "l" -> {
                playerPass.clear();
                playLine(game);
            }
            case "m" -> {
                playerPass.clear();
                playMultiple(game);
            }
            case "p" -> {
                playerPass.add(game.getCurrentPlayerName());
                game.pass();
            }
        }
    }

    /**
     * Requests the necessary information to add the first tiles of the game.
     *
     * @param game the Qwirkle game.
     */
    public static void firstAdd(Game game) {
        try {
            System.out.println(ANSI_GREEN + "Start!" + ANSI_RESET);
            game.first(direction(), indexes());
            //View.display(game.getGrid());
        } catch (QwirkleException e) {
            View.displayError(e.getMessage()); //Shows the message of the exception;
        }
    }

    /**
     * Requests the necessary information to add one tile.
     *
     * @param game the Qwirkle game.
     */
    public static void playOneTile(Game game) {
        try {
            int index = robustReadingIndexes();
            System.out.print("Enter the row where you want to place the tile: ");
            int row = robustReadingInt();
            System.out.print("Enter the column where you want to place the tile: ");
            int col = robustReadingInt();
            game.play(row, col, index);
        } catch (QwirkleException e) {
            View.displayError(e.getMessage()); //Shows the message of the exception;
        }
    }

    /**
     * Requests the necessary information to add several aligned tiles.
     *
     * @param game the Qwirkle game.
     */
    public static void playLine(Game game) {
        try {
            System.out.print("Enter the row where you want to place the first tile: ");
            int row = robustReadingInt();
            System.out.println("Enter the column where you want to place the first tile: ");
            int col = robustReadingInt();
            game.play(row, col, direction(), indexes());
        } catch (QwirkleException e) {
            View.displayError(e.getMessage()); //Shows the message of the exception;
        }
    }

    /**
     * Requests the necessary information to add the multiple tiles.
     *
     * @param game the Qwirkle game.
     */
    public static void playMultiple(Game game) {
        try {
            System.out.print("How many tiles do you want to place ? ");
            int nbTiles = robustReadingInt();
            while (nbTiles < 1 || nbTiles > 6) {
                System.out.println(ANSI_ORANGE + "Number of tiles must be between 1 and 6, try again!" + ANSI_RESET);
                nbTiles = robustReadingInt();
            }
            int[] playMultipleTiles = new int[nbTiles * 3];
            int indexTab = 0;
            for (int i = 0; i < nbTiles; i++) {
                System.out.print("Enter the row where you want to place the tile " + (i + 1) + ": ");
                int row = robustReadingInt();
                System.out.println("Enter the column where you want to place the tile: " + (i + 1) + ": ");
                int col = robustReadingInt();
                int tileIndex = robustReadingIndexes();
                playMultipleTiles[indexTab] = row;
                playMultipleTiles[indexTab + 1] = col;
                playMultipleTiles[indexTab + 2] = tileIndex;
                indexTab = indexTab + 3;
            }
            game.play(playMultipleTiles);
        } catch (QwirkleException e) {
            View.displayError(e.getMessage()); //Shows the message of the exception;
        }
    }

    /**
     * Asks for the number of players.
     *
     * @return the number of players.
     */
    public static int nbPlayers() {
        System.out.print("Enter the number of players: ");
        return robustReadingPlayers();
    }

    /**
     * Asks for the names of the players.
     *
     * @param nbPlayers number of players.
     * @return an array list containing the names of the players.
     */
    public static List<String> namePlayers(int nbPlayers) {
        List<String> playersList = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            System.out.print("Enter the name of the player " + (i + 1) + ": ");
            String name = robustReadingString();
            playersList.add(name);
        }
        System.out.println();
        System.out.print("The players of this Qwirkle game are: ");
        for (String playerName : playersList) {
            System.out.print(playerName + " ");
        }
        System.out.println();
        return playersList;
    }

    /**
     * Initializes the 6 tiles for each player at the start of the game.
     *
     * @param game the Qwirkle game.
     */
    public static void initTiles(Game game) {
        for (int i = 0; i < game.getPlayers().length; i++) {
            game.getPlayers()[i].refill();
        }
    }

    /**
     * Asks which player starts the game.
     *
     * @param game the Qwirkle game.
     */
    public static void startPlayer(Game game) {
        System.out.println("Who is starting ? ");
        String name = robustReadingString();
        boolean validName = false;
        while (!validName) {
            int i = 0;
            while (i < game.getPlayers().length && !game.getPlayers()[i].getName().equals(name)) {
                i++;
            }
            if (i == game.getPlayers().length) {
                System.out.println("This name doesn't exist! : ");
                System.out.println("Who is starting ? ");
                name = robustReadingString();
            } else {
                validName = true;
                game.setCurrentPlayer(i);
            }
        }
    }

    /**
     * Robust reading of an integer.
     *
     * @return an integer.
     */
    public static int robustReadingInt() {
        Scanner keyboard = new Scanner(System.in);
        while (!keyboard.hasNextInt()) {
            System.out.println(ANSI_ORANGE + "Invalid number, try again!: " + ANSI_RESET);
            keyboard.next();
        }
        return keyboard.nextInt();
    }

    /**
     * Robust reading of the number of players.
     * Checks that this number respects the rules of the game.
     *
     * @return a valid number of players.
     */
    public static int robustReadingPlayers() {
        int number = robustReadingInt();
        while (number < 2 || number > 4) {
            System.out.println(ANSI_ORANGE + "The number of players must be between 2 and 4, try again! : "
                    + ANSI_RESET);
            number = robustReadingInt();
        }
        return number;
    }

    /**
     * Robust reading of a String.
     *
     * @return a String.
     */
    public static String robustReadingString() {
        Scanner keyboard = new Scanner(System.in);
        while (!keyboard.hasNextLine()) {
            System.out.println(ANSI_ORANGE + "Invalid String, try again! : " + ANSI_RESET);
            keyboard.next();
        }
        return keyboard.nextLine();
    }

    /**
     * Robust reading of a String.
     *
     * @return a String representing the type of add.
     */
    public static String robustReadingAddType() {
        Scanner keyboard = new Scanner(System.in);
        String addType = robustReadingString();
        while ((!addType.equalsIgnoreCase("f")) && (!addType.equalsIgnoreCase("o"))
                && (!addType.equalsIgnoreCase("l") && (!addType.equalsIgnoreCase("m"))
                && (!addType.equalsIgnoreCase("p")))) {
            System.out.print(ANSI_ORANGE + "The type of play letter is invalid, do you need help (y or n) ? : " + ANSI_RESET);
            String help = keyboard.nextLine();
            if (help.equalsIgnoreCase("y")) {
                View.displayHelp();
            }
            System.out.print("Enter the type of play (f, o, l, m, p) : ");
            addType = robustReadingString();
        }
        return addType;
    }

    /**
     * Robust reading of a direction.
     *
     * @return a valid direction.
     */
    public static String robustReadingDirection() {
        Scanner keyboard = new Scanner(System.in);
        String direction = robustReadingString();
        while ((!direction.equalsIgnoreCase("l")) && (!direction.equalsIgnoreCase("u"))
                && (!direction.equalsIgnoreCase("r")) && (!direction.equalsIgnoreCase("d"))) {
            System.out.print(ANSI_ORANGE + "The direction letter is invalid, do you need help (y or n) ? : "
                    + ANSI_RESET);
            String help = keyboard.nextLine();
            if (help.equalsIgnoreCase("y")) {
                View.displayHelp();
            }
            System.out.print("Enter the direction of your tiles placement (d, u, l, r): ");
            direction = robustReadingString();
        }
        return direction;
    }

    /**
     * Robust reading of an index.
     * Checks that the index respects the rules of the game.
     *
     * @return a valid index.
     */
    public static int robustReadingIndexes() {
        System.out.println("Enter the index of the tile that you want to play: ");
        int index = robustReadingInt();
        while (index < 0 || index > 5) {
            System.out.println(ANSI_ORANGE + "The index must be between 0 and 5 , try again! : " + ANSI_RESET);
            index = robustReadingInt();
        }
        return index;
    }

    /**
     * Gives one of the value of the direction enumeration to the requested direction.
     *
     * @return the direction.
     */
    public static Direction direction() {
        System.out.print("Enter the direction of your tiles placement (d, u, l, r): ");
        String direction = robustReadingDirection();
        return switch (direction.toLowerCase()) {
            case "d" -> Direction.DOWN;
            case "u" -> Direction.UP;
            case "l" -> Direction.LEFT;
            case "r" -> Direction.RIGHT;
            default -> throw new RuntimeException(ANSI_YELLOW + "Invalid direction :(" + ANSI_RESET);
        };
    }

    /**
     * Requests the indexes of the tiles to add.
     *
     * @return an array of tiles indexes.
     */
    public static int[] indexes() {
        Scanner keyboard = new Scanner(System.in);
        List<Integer> indexes = new ArrayList<>();
        boolean more = true;
        while (indexes.size() < 6 && more) {
            int index = robustReadingIndexes();
            indexes.add(index);
            System.out.println("Do you want to add more tiles ? (y or n)");
            String yesOrNo = keyboard.nextLine();
            if (yesOrNo.equalsIgnoreCase("n")) {
                more = false;
            }
        }
        int[] indexesTab = new int[indexes.size()];
        for (int i = 0; i < indexesTab.length; i++) {
            indexesTab[i] = indexes.get(i);
        }
        return indexesTab;
    }

    /**
     * checks who got the highest score
     *
     * @param game the current game;
     * @return the name of the winner;
     */
    public static String winner(Game game) {
        int maxScore = 0;
        String nameWinner = "";
        for (Player player : game.getPlayers()) {
            if (player.getScore() > maxScore) {
                maxScore = player.getScore();
                nameWinner = player.getName();
            }
        }
        return nameWinner;
    }
}