package g60085.qwirkle;

import g60085.qwirkle.model.Bag;
import g60085.qwirkle.model.Direction;
import g60085.qwirkle.model.Game;
import g60085.qwirkle.model.QwirkleException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static g60085.qwirkle.view.View.*;

/**
 * The App class serves as the main entry point for the application.
 * It controls the application and manages its flow.
 * The App class is responsible for:
 * Handling user inputs and translating them into appropriate actions.
 * Managing the game flow, including starting, pausing, and ending the game.
 * Coordinating communication between the user interface, the game model, and the view.
 * Requesting the view to display relevant information, such as the game state, scores, and messages.
 */
public class App {

    /**
     * The main entry point of the Qwirkle game.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        Game game = null;
        displayTitle();

        // Resume a previously saved game;
        if (resumeGame() && displaySerializedFiles()) {
            do {
                try {
                    String filename = robustReadingString("Enter the name of the file: ");
                    game = Game.getFromFile(filename);
                    displayMessage(ANSI_GREEN + "The game was successfully deserialized!" + ANSI_RESET);
                } catch (QwirkleException e) {
                    displayMessage(ANSI_ORANGE + e.getMessage() + ANSI_RESET);
                }
            } while (game == null && resumeGame());
        }

        // Starts a new game;
        if (game == null) {
            int numberPlayers = nbPlayers();
            game = new Game(namePlayers(numberPlayers));
            game.initPlayerHand();
            startPlayer(game);
            displayHelp();
        }

        // The flow of the game;
        boolean continueGame = true;
        while (continueGame) {
            try {
                if (game.isOver()) {
                    continueGame = false;
                    displayGameOver(determineWinners(game));
                } else {
                    if (Bag.getInstance().size() == 0) {
                        displayMessage(ANSI_YELLOW + "The bag is empty! " +
                                "You are almost at the end of the game!" + ANSI_RESET);
                    }
                    if (!tryToPlay("Try to play! ", game)) {
                        continueGame = false;
                        if (saveGame()) {
                            boolean gameIsSaved = false;
                            do {
                                try {
                                    displaySerializedFiles();
                                    String filename = robustReadingString("Enter the name of the file: ");
                                    Game.write(game, filename);
                                    gameIsSaved = true;
                                    displayMessage(ANSI_GREEN + "The game was successfully serialized!" + ANSI_RESET);
                                } catch (QwirkleException e) {
                                    displayMessage(ANSI_ORANGE + e.getMessage() + ANSI_RESET);
                                }
                            } while (!gameIsSaved && saveGame());
                        }
                        displayGameOver(determineWinners(game));
                    }
                }
            } catch (QwirkleException e) {
                displayError(e.getMessage());
            }
        }
    }

    /**
     * Checks if the user wants to resume a game.
     *
     * @return {@code true} if the user wants to resume a game, {@code false} otherwise.
     */
    private static boolean resumeGame() {
        String message = ANSI_CYAN + "Do you want to resume a game? (y or n)" + ANSI_RESET;
        return yesOrNoRobustReading(message).equalsIgnoreCase("y");
    }

    /**
     * Asks the user to enter the number of players.
     *
     * @return The number of players entered by the user.
     */
    private static int nbPlayers() {
        return robustReadingPlayers();
    }

    /**
     * Executes a robust reading of the number of players,
     * ensuring it follows the game rules.
     *
     * @return A valid number of players (between 2 and 4).
     */
    private static int robustReadingPlayers() {
        Scanner scanner = new Scanner(System.in);
        int number;
        String regex = "[2-4]"; // This regex pattern will match any single digit that is either 2, 3, or 4.
        displayMessage(ANSI_CYAN + "Enter the number of players (between 2 and 4): " + ANSI_RESET);
        String input = scanner.nextLine();

        while (!input.matches(regex)) {
            displayInvalidInput("Invalid input! Please enter a number between 2 and 4.");
            input = scanner.nextLine();
        }

        number = Integer.parseInt(input);
        return number;
    }

    /**
     * Asks the user to enter the names of the players.
     *
     * @param nbPlayers The number of players.
     * @return A list containing the names of all the players.
     */
    private static List<String> namePlayers(int nbPlayers) {
        List<String> playersList = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            String name = robustReadingString("Enter the name of the player " + (i + 1) + ": ");
            playersList.add(name);
        }
        displayPlayers(playersList);
        return playersList;
    }

    /**
     * Asks which player will start the game.
     *
     * @param game The Qwirkle game instance.
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
                game.setStarterPlayer(i);
            }
        }
    }

    /**
     * Performs a robust reading of a string from the user, excluding numbers.
     *
     * @param message The message to display when prompting for the string.
     * @return The string entered by the user.
     */
    private static String robustReadingString(String message) {
        Scanner keyboard = new Scanner(System.in);
        displayMessage(ANSI_CYAN + message + ANSI_RESET);

        String input = keyboard.nextLine();
        while (input.trim().isEmpty() || containsNumbers(input)) { //Ensures that the user cannot enter only whitespace as a valid input.
            displayInvalidInput("Invalid input! Please try again without numbers.");
            input = keyboard.nextLine();
        }

        return input;
    }

    /**
     * Checks if a string contains any numeric characters.
     *
     * @param input The string to check.
     * @return True if the string contains numbers, false otherwise.
     */
    private static boolean containsNumbers(String input) {
        boolean containsNumber = false;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                containsNumber = true;
            }
        }
        return containsNumber;
    }

    /**
     * Performs a robust reading of a yes or no answer from the user.
     *
     * @param message The message to display when prompting for the answer.
     * @return The valid yes or no answer entered by the user.
     */
    private static String yesOrNoRobustReading(String message) {
        String regex = "(?i)[yn]"; // Regular expression pattern for matching the characters 'y' or 'n' (case-insensitive)
        String yesOrNo = robustReadingString(message);

        while (!yesOrNo.matches(regex)) {
            yesOrNo = robustReadingString(message);
        }

        return yesOrNo;
    }

    /**
     * Handles the addition of tiles based on the input provided.
     *
     * @param game  the Qwirkle game.
     * @param input the input specifying the type of addition.
     */
    private static void add(Game game, String input) {
        String invalidInputMessage = "Invalid input! Try again!";
        String replayMessage = "Replay!";
        String[] detailInput = input.split(" ");
        String typeOfPlay = detailInput[0].toLowerCase();

        switch (typeOfPlay) {
            case "f":
                if (!firstAdd(input, game)) {
                    displayInvalidInput(invalidInputMessage);
                    tryToPlay(replayMessage, game);
                }
                break;
            case "o":
                if (!playOneTile(input, game)) {
                    displayInvalidInput(invalidInputMessage);
                    tryToPlay(replayMessage, game);
                }
                break;
            case "l":
                if (!playLine(input, game)) {
                    displayInvalidInput(invalidInputMessage);
                    tryToPlay(replayMessage, game);
                }
                break;
            case "m":
                if (!playTileAtPosition(input, game)) {
                    displayInvalidInput(invalidInputMessage);
                    tryToPlay(replayMessage, game);
                }
                break;
            case "p":
                game.pass();
                break;
            default:
                displayInvalidInput(invalidInputMessage);
                tryToPlay(replayMessage, game);
                break;
        }
    }

    /**
     * Handles the user's attempt to play a move.
     *
     * @param message The message to display before prompting for input.
     * @param game    The Qwirkle game.
     * @return True if the user wants to continue playing, false if they want to quit.
     */
    private static boolean tryToPlay(String message, Game game) {
        Scanner keyboard = new Scanner(System.in);

        displayGridView(game.getGrid());
        displayPlayerInfo(game.getCurrentPlayerName(), game.getCurrentPlayerHand(), game.getCurrentPlayerScore());
        displayMessage(message);

        String validCommandsRegex = "(?i)[folmpq]"; // Case-insensitive regular expression for matching the letters f, o, l, m, or p
        String input;
        String[] detailInput;
        String typeOfPlay;

        do {
            if (yesOrNoRobustReading(ANSI_ORANGE + "Do you need help (y or n)? : " + ANSI_RESET).equalsIgnoreCase("y")) {
                displayHelp();
            }
            displayEntrancePrompt();
            input = keyboard.nextLine().trim();
            detailInput = input.split(" ");
            typeOfPlay = detailInput[0];
        } while (!typeOfPlay.matches(validCommandsRegex));

        if (input.equalsIgnoreCase("q")) {
            return false; // User wants to quit
        } else {
            add(game, input);
            return true; // User wants to continue playing
        }
    }

    /**
     * Handles the first addition of tiles to the game grid.
     *
     * @param input the user's input for the first play of tiles.
     * @param game  the Qwirkle game.
     * @return true if the input is valid and the tiles are successfully added, false otherwise.
     */
    private static boolean firstAdd(String input, Game game) {
        boolean validInput = true;
        String regex = "(?i)^f [drul]( [0-5]){1,6}$"; // f [<direction>] <f1> [<f2> ...]; (case-insensitive);

        if (input.matches(regex)) {
            String[] detailInput = input.split(" ");
            Direction direction = direction(detailInput[1]);
            int[] indexes = new int[detailInput.length - 2];
            for (int i = 2; i < detailInput.length; i++) {
                indexes[i - 2] = Integer.parseInt(detailInput[i]);
            }
            displayMessage(ANSI_GREEN + "Start!" + ANSI_RESET);
            game.first(direction, indexes);
            displayGridView(game.getGrid());
        } else {
            validInput = false;
        }
        return validInput;
    }

    /**
     * Plays one tile on the game grid.
     *
     * @param input the user's input for playing one tile.
     * @param game  the Qwirkle game.
     * @return true if the input is valid and the tile is successfully added, false otherwise.
     */
    private static boolean playOneTile(String input, Game game) {
        boolean validInput = true;
        String regex = "(?i)^o( ([0-9]|[1-8][0-9])){2} [0-5]$"; // o <row> <col> <i>; (case-insensitive);

        if (input.matches(regex)) {
            String[] detailInput = input.split(" ");
            int row = Integer.parseInt(detailInput[1]);
            int col = Integer.parseInt(detailInput[2]);
            int tileIndex = Integer.parseInt(detailInput[3]);
            game.play(row, col, tileIndex);
            displayGridView(game.getGrid());
        } else {
            validInput = false;
        }
        return validInput;
    }

    /**
     * Plays a line of tiles on the game grid.
     *
     * @param input the user's input for playing a line of tiles.
     * @param game  the Qwirkle game.
     * @return true if the input is valid and the tiles are successfully added, false otherwise.
     */
    private static boolean playLine(String input, Game game) {
        boolean validInput = true;
        String regex = "(?i)^l( ([0-9]|[1-8][0-9])){2} [drul]( [0-5]){1,6}$"; // l <row> <col> <direction> <i1> [<i2>];
        // (case-insensitive);

        if (input.matches(regex)) {
            String[] detailInput = input.split(" ");
            int row = Integer.parseInt(detailInput[1]);
            int col = Integer.parseInt(detailInput[2]);
            Direction direction = direction(detailInput[3]);
            int[] indexes = new int[detailInput.length - 4];
            for (int i = 4; i < detailInput.length; i++) {
                indexes[i - 4] = Integer.parseInt(detailInput[i]);
            }
            game.play(row, col, direction, indexes);
            displayGridView(game.getGrid());
        } else {
            validInput = false;
        }
        return validInput;
    }

    /**
     * Plays tiles at specified positions on the game grid.
     *
     * @param input the user's input for playing tiles at positions.
     * @param game  the Qwirkle game.
     * @return true if the input is valid and the tiles are successfully played, false otherwise.
     */
    private static boolean playTileAtPosition(String input, Game game) {
        boolean validInput = true;
        String regex = "(?i)^m(( ([0-9]|[1-8][0-9])){2} [0-5]){1,6}$"; // m <row1> <col1> <i1> [<row2> <col2> <i2> ...];
        // (case-insensitive);

        if (input.matches(regex)) {
            String[] detailInput = input.split(" ");
            int[] detailTileAtPosition = new int[detailInput.length - 1];
            int indexTab = 0;
            for (int i = 1; i < detailInput.length; i += 3) {
                int row = Integer.parseInt(detailInput[i]);
                int col = Integer.parseInt(detailInput[i + 1]);
                int tileIndex = Integer.parseInt(detailInput[i + 2]);
                detailTileAtPosition[indexTab] = row;
                detailTileAtPosition[indexTab + 1] = col;
                detailTileAtPosition[indexTab + 2] = tileIndex;
                indexTab = indexTab + 3;
            }
            game.play(detailTileAtPosition);
            displayGridView(game.getGrid());
        } else {
            validInput = false;
        }
        return validInput;
    }

    /**
     * Converts a string representation of a direction into a Direction enum value.
     *
     * @param direction The string representation of the direction
     *                  ("d" for DOWN, "u" for UP, "l" for LEFT, "r" for RIGHT).
     * @return The corresponding Direction enum value, or null if the input is invalid.
     */
    private static Direction direction(String direction) {
        return switch (direction.toLowerCase()) {
            case "d" -> Direction.DOWN;
            case "u" -> Direction.UP;
            case "l" -> Direction.LEFT;
            case "r" -> Direction.RIGHT;
            default -> null;
        };
    }

    /**
     * Determines the winner(s) with the highest score in the game.
     *
     * @param game The current game instance.
     * @return A list of the name(s) of the winner(s).
     */
    private static List<String> determineWinners(Game game) {
        List<Integer> scores = new ArrayList<>();

        for (int score : game.getPlayersScore()) {
            scores.add(score);
        }

        Collections.sort(scores);
        int maxScore = scores.get(scores.size() - 1);

        List<String> winners = new ArrayList<>();

        if (maxScore > 0) {
            for (int i = 0; i < game.getPlayersScore().length; i++) {
                if (game.getPlayersScore()[i] == maxScore) {
                    winners.add(game.getPlayersName()[i]);
                }
            }
        }

        return winners;
    }

    /**
     * Checks if the user wants to save the game.
     *
     * @return {@code true} if the user wants to save the game, {@code false} otherwise.
     */
    private static boolean saveGame() {
        String message = ANSI_CYAN + "Do you want to save the game? (y or n)" + ANSI_RESET;
        return yesOrNoRobustReading(message).equalsIgnoreCase("y");
    }

}