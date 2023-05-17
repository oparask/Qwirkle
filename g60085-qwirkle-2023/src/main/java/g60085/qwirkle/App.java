package g60085.qwirkle;

import g60085.qwirkle.model.*;
import g60085.qwirkle.view.View;

import java.io.IOException;
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
        Scanner keyboard = new Scanner(System.in);
        Game game = null;
        displayTitle();

        // Resume a previously saved game;
        if (resumeGame()) {
            do {
                try {
                    if(noSerializedFiles()){ // If empty directory;
                        break;
                    } else {
                        // The method displays the files;
                        asksFileName();
                        String filename = keyboard.nextLine();
                        game = Game.getFromFile(filename);
                        successMsg("deserialized");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    errorMsg(e.getMessage(), "deserialized");
                }
            } while (game == null && resumeGame());
        }

        // Starts a new game;
        if (game == null) {
            int numberPlayers = nbPlayers(); // Asks for the number of players && Shows the players;
            game = new Game(namePlayers(numberPlayers));
            game.initPlayerHand(); // Initializes each player's hand;
            startPlayer(game); // It asks who's starting the game;
            displayHelp();
        }

        //The flow of the game;
        boolean continueGame = true;
        while (continueGame) {
            try {
                if (game.isOver()) {
                    continueGame = false;
                    endGame(winner(game));
                } else {
                    if (Bag.getInstance().size() == 0) {
                        displayGameAlmostOver();
                    }
                    add(game); // Play one, several or multiple tiles;
                    String quitGame = robustReadingString(ANSI_GREEN + "Enter 'q' if you want to quit" + ANSI_RESET);
                    if (quitGame.equalsIgnoreCase("q")) {
                        continueGame = false;
                        if (saveGame()) {
                            boolean gameIsSaved = false;
                            do{
                                try {
                                    asksFileName();
                                    String filename = keyboard.nextLine();
                                    Game.write(game, filename);
                                    gameIsSaved = true;
                                    successMsg("serialized");
                                } catch (IOException e) {
                                    errorMsg(e.getMessage(), "serialized");
                                }
                            }while (!gameIsSaved && saveGame());
                        }
                        endGame(winner(game));
                    }
                }
            } catch (QwirkleException e) {
                displayError(e.getMessage()); //Shows the message of the exception;
            }
        }
    }


    /**
     * Asks what type of addition of tiles we want to make.
     *
     * @param game the Qwirkle game.
     */
    private static void add(Game game) {
        System.out.println();
        display(game.getGrid());
        displayPlayer(game.getCurrentPlayerName(), game.getCurrentPlayerHand(), game.getCurrentPlayerScore());
        String typePlay = robustReadingAddType("Enter the type of play (f, o, l, m, p): ");
        switch (typePlay.toLowerCase()) {
            case "f" -> firstAdd(game);
            case "o" -> playOneTile(game);
            case "l" -> playLine(game);
            case "m" -> playMultiple(game);
            case "p" -> game.pass();
        }
    }

    /**
     * Requests the necessary information to add the first tiles of the game.
     *
     * @param game the Qwirkle game.
     */
    private static void firstAdd(Game game) {
        displayStart();
        game.first(direction(), indexes());
        display(game.getGrid());
    }

    /**
     * Requests the necessary information to add one tile.
     *
     * @param game the Qwirkle game.
     */
    private static void playOneTile(Game game) {
        int index = robustReadingIndexes();
        int row = robustReadingInt("Enter the row where you want to place the tile: ");
        int col = robustReadingInt("Enter the column where you want to place the tile: ");
        game.play(row, col, index);
        display(game.getGrid());
    }

    /**
     * Requests the necessary information to add several aligned tiles.
     *
     * @param game the Qwirkle game.
     */
    private static void playLine(Game game) {
        int row = robustReadingInt("Enter the row where you want to place the first tile: ");
        int col = robustReadingInt("Enter the column where you want to place the first tile: ");
        game.play(row, col, direction(), indexes());
        display(game.getGrid());
    }

    /**
     * Requests the necessary information to add the multiple tiles.
     *
     * @param game the Qwirkle game.
     */
    private static void playMultiple(Game game) {
        int nbTiles = robustReadingInt("How many tiles do you want to place ? ");
        while (nbTiles < 1 || nbTiles > 6) {
            nbTiles = robustReadingInt(ANSI_ORANGE + "Number of tiles must be between 1 and 6, try again!" + ANSI_RESET);
        }
        int[] playMultipleTiles = new int[nbTiles * 3];
        int indexTab = 0;
        for (int i = 0; i < nbTiles; i++) {
            int row = robustReadingInt("Enter the row where you want to place the tile " + (i + 1) + ": ");
            int col = robustReadingInt("Enter the column where you want to place the tile: " + (i + 1) + ": ");
            int tileIndex = robustReadingIndexes();
            playMultipleTiles[indexTab] = row;
            playMultipleTiles[indexTab + 1] = col;
            playMultipleTiles[indexTab + 2] = tileIndex;
            indexTab = indexTab + 3;
        }
        game.play(playMultipleTiles);
        display(game.getGrid());
    }

    /**
     * Asks for the number of players.
     *
     * @return the number of players.
     */
    private static int nbPlayers() {
        return robustReadingPlayers();
    }

    /**
     * Asks for the names of the players.
     *
     * @param nbPlayers number of players.
     * @return a list with all the player's name;
     */
    private static List<String> namePlayers(int nbPlayers) {
        Scanner keyboard = new Scanner(System.in);
        List<String> playersList = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            System.out.println("Enter the name of the player " + (i + 1) + ": ");
            String name = keyboard.nextLine();
            playersList.add(name);
        }
        displayAllPlayers(playersList);
        return playersList;
    }

    /**
     * Asks which player starts the game.
     *
     * @param game the Qwirkle game.
     */
    private static void startPlayer(Game game) {
        String name = robustReadingString("Who is starting ? ");
        boolean validName = false;
        while (!validName) {
            int i = 0;
            while (i < game.getPlayersName().length && !game.getPlayersName()[i].equals(name)) {
                i++;
            }
            if (i == game.getPlayersName().length) {
                name = robustReadingString("This name doesn't exist! : \n" + "Who is starting ? ");
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
    private static int robustReadingInt(String message) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println(message);
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
    private static int robustReadingPlayers() {
        Scanner scanner = new Scanner(System.in);
        int number;
        String regex = "[2-4]"; // This regex pattern will match any single digit that is either 2, 3, or 4.

        System.out.print("Enter the number of players (2-4): ");
        String input = scanner.nextLine();

        while (!input.matches(regex)) {
            System.out.println(ANSI_ORANGE + "Invalid input! Please enter a number between 2 and 4." + ANSI_RESET);
            input = scanner.nextLine();
        }

        number = Integer.parseInt(input);
        return number;
    }

    /**
     * Robust reading of a String.
     *
     * @return a String.
     */
    private static String robustReadingString(String message) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println(message);
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
    private static String robustReadingAddType(String message) {
        Scanner keyboard = new Scanner(System.in);

        System.out.print(message);
        String addType = keyboard.nextLine();
        String regex = "[folmp]"; // Regular expression for matching the letters f, o, l, m, or p

        while (!addType.toLowerCase().matches(regex)) {
            if (yesOrNoRobustLecture(ANSI_ORANGE + "Do you need help (y or n)? : " + ANSI_RESET).equalsIgnoreCase("y")) {
                displayHelp();
            }
            System.out.print(message);
            addType = keyboard.nextLine();
        }
        return addType;
    }

    private static String yesOrNoRobustLecture(String message) {
        Scanner keyboard = new Scanner(System.in);
        String regex = "[yn]"; // Regular expression for matching the letters y or n

        System.out.println(message);
        String yesOrNo = keyboard.nextLine();

        while (!yesOrNo.toLowerCase().matches(regex)) {
            System.out.println(message);
            yesOrNo = keyboard.nextLine();
        }
        return yesOrNo;
    }


    /**
     * Robust reading of an index.
     * Checks that the index respects the rules of the game.
     *
     * @return a valid index.
     */
    private static int robustReadingIndexes() {
        Scanner keyboard = new Scanner(System.in);
        String message = "Enter the index of the tile that you want to play: ";
        int index;
        String regex = "[0-5]"; // This regex pattern will match any single digit that is either 0, 1, 2, 3, 4, 5.

        System.out.println(message);
        String input = keyboard.nextLine();
        while (!input.matches(regex)) {
            System.out.println(ANSI_ORANGE + "The index must be between 0 and 5 , try again! : " + ANSI_RESET);
            System.out.println(message);
            input = keyboard.nextLine();
        }

        index = Integer.parseInt(input);
        return index;
    }

    /**
     * Robust reading of a direction.
     *
     * @return a valid direction.
     */
    private static String robustReadingDirection() {
        Scanner keyboard = new Scanner(System.in);
        String message = "Enter the direction of your tiles placement (d, u, l, r): ";
        String regex = "[dulr]"; // Regular expression for matching the letters d, u, l or r

        System.out.println(message);
        String direction = keyboard.nextLine();
        while (!direction.toLowerCase().matches(regex)) {
            if (yesOrNoRobustLecture(ANSI_ORANGE + "Do you need help (y or n)? : " + ANSI_RESET).equalsIgnoreCase("y")) {
                displayHelp();
            }
            System.out.println(message);
            direction = keyboard.nextLine();
        }
        return direction;
    }

    /**
     * Gives one of the value of the direction enumeration to the requested direction.
     *
     * @return the direction.
     */
    private static Direction direction() {
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
    private static int[] indexes() {
        Scanner keyboard = new Scanner(System.in);
        List<Integer> indexes = new ArrayList<>();
        boolean more = true;
        while (indexes.size() < 6 && more) {
            int index = robustReadingIndexes();
            indexes.add(index);
            if (yesOrNoRobustLecture("Do you want to add more tiles ? (y or n)").equalsIgnoreCase("n")) {
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
     * Checks who got the highest score
     *
     * @param game the current game;
     * @return the name/s of the winner/s;
     */
    private static List<String> winner(Game game) {
        int maxScore = 0;
        List<String> nameWinner = new ArrayList<>();
        for (int i = 0; i < game.getPlayersScore().length; i++) {
            if (game.getPlayersScore()[i] >= maxScore) {
                maxScore = game.getPlayersScore()[i];
                nameWinner.add(game.getPlayersName()[i]);
            }
        }
        return nameWinner;
    }

    private static boolean resumeGame() {
        String message = ANSI_CYAN + "Do you want to resume a game (y or n)?" + ANSI_RESET;
        return yesOrNoRobustLecture(message).equalsIgnoreCase("y");
    }

    private static boolean saveGame() {
        String message = ANSI_CYAN + "Do you want to save the game (y or n)?" + ANSI_RESET;
        return yesOrNoRobustLecture(message).equalsIgnoreCase("y");
    }


}