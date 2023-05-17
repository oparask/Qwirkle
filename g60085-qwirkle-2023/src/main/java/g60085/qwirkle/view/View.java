package g60085.qwirkle.view;

import g60085.qwirkle.model.GridView;
import g60085.qwirkle.model.Tile;

import java.io.File;
import java.util.List;

/**
 * Contains only static methods to display different parts of the game in the console.
 */
public class View {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\033[38;5;196m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[93m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_ORANGE = "\u001B[38;5;208m";

    /**
     * Displays the game title;
     */
    public static void displayTitle() {
        String[] qwirkle = {
                "\u001B[35m   QQ  W           W  III  RRRR    K   K   L     EEEEE  \u001B[0m",
                "\u001B[36m Q    Q w         w    I   R   R   K  K    L     E      \u001B[0m",
                "\u001B[34m Q    Q  w   W   w     I   R RR    KKK     L     EEEEE  \u001B[0m",
                "\u001B[32m Q    Q   w  w  w      I   R   R   K   K   L     E      \u001B[0m",
                "\u001B[33m   QQ Q    W   W      III  R     R K    K  LLLLL EEEEE  \u001B[0m"
        };

        for (String line : qwirkle) {
            System.out.println(line);
        }
        System.out.println();
    }

    /**
     * Gives values to the color and shape enumerations
     * in order to be able to display the tiles.
     *
     * @param tile the tile to display.
     */
    public static void ViewColorShape(Tile tile) {
        String shape = "";
        switch (tile.shape()) {
            case SQUARE -> shape = "[] ";
            case CROSS -> shape = "X  ";
            case ROUND -> shape = "O  ";
            case DIAMOND -> shape = "<> ";
            case STAR -> shape = "*  ";
            case PLUS -> shape = "+  ";
        }
        switch (tile.color()) {
            case BLUE -> System.out.print(ANSI_BLUE + shape + ANSI_RESET);
            case RED -> System.out.print(ANSI_RED + shape + ANSI_RESET);
            case GREEN -> System.out.print(ANSI_GREEN + shape + ANSI_RESET);
            case ORANGE -> System.out.print(ANSI_ORANGE + shape + ANSI_RESET);
            case PURPLE -> System.out.print(ANSI_PURPLE + shape + ANSI_RESET);
            case YELLOW -> System.out.print(ANSI_YELLOW + shape + ANSI_RESET);
        }
    }

    /**
     * Shows only the central rectangular part of the grid game that contains tiles.
     *
     * @param grid the grid view to display.
     */
    public static void display(GridView grid) {
        System.out.println();
        int colLeft = 45;
        int colRight = 45;
        for (int i = 3; i < 87; i++) { //row
            for (int j = 0; j < 45; j++) { //left col
                if (grid.get(i, j) != null && j < colLeft) {
                    colLeft = j;
                    break;
                }
            }
            for (int j = 86; j > 45; j--) { //right col
                if (grid.get(i, j) != null && j > colRight) {
                    colRight = j;
                    break;
                }
            }
        }
        int rowUp = 45;
        int rowDown = 45;
        for (int j = colLeft; j <= colRight; j++) { //col
            for (int i = 0; i < 45; i++) { //up row
                if (grid.get(i, j) != null && i < rowUp) {
                    rowUp = i;
                    break;
                }
            }
            for (int i = 86; i > 45; i--) {//down row
                if (grid.get(i, j) != null && i > rowDown) {
                    rowDown = i;
                    break;
                }
            }
        }
        for (int i = rowUp - 3; i < rowDown + 4; i++) { //row
            System.out.print(ANSI_CYAN + i + " |");
            for (int j = colLeft - 3; j < colRight + 4; j++) {
                if (grid.get(i, j) == null) {
                    System.out.print("   ");
                } else {
                    ViewColorShape(grid.get(i, j));
                }
            }
            System.out.println();
        }
        System.out.print("    "); //col
        for (int j = colLeft - 3; j < colRight + 4; j++) {
            System.out.print(j + " ");
        }
        System.out.println(ANSI_RESET);
        System.out.println();
    }

    /**
     * Displays the name, hand and score of the player;
     *
     * @param name  name of the player;
     * @param hand  hand of the player;
     * @param score score of the player;
     */
    public static void displayPlayer(String name, List<Tile> hand, int score) {
        System.out.println(ANSI_YELLOW + name + ANSI_RESET + ANSI_CYAN + ", It's your turn!");
        System.out.println("Your score is: " + ANSI_RESET + ANSI_YELLOW + score + ANSI_RESET);
        System.out.print(ANSI_CYAN + "Your hand is: " + ANSI_RESET);
        for (Tile tile : hand) {
            ViewColorShape(tile);
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Displays Qwirkle game commands.
     */
    public static void displayHelp() {
        System.out.println(ANSI_PURPLE + "Qwirkle command:\n"
                + "- play first : f [<direction>] <f1> [<f2> …]\n"
                + "- play 1 tile : o <row> <col> <i>\n"
                + "- play line: l <row> <col> <direction> <i1> [<i2>]\n"
                + "- play plic-ploc : m <row1> <col1> <i1> [<row2> <col2> <i2>]\n"
                + "- pass : p\n"
                + "- quit : q\n"
                + "    i : index in list of tiles\n"
                + "    d : direction in l (left), r (right), u (up), d (down)\n" + ANSI_RESET);
    }

    /**
     * Displays an error message.
     *
     * @param message the error message.
     */
    public static void displayError(String message) {
        System.out.println(ANSI_ORANGE + "Impossible move!");
        System.out.println("Reason: " + message + ANSI_RESET);
        System.out.println(ANSI_GREEN + "Try again!" + ANSI_RESET);
        System.out.println();
    }

    /**
     * Displays the word "Start";
     */
    public static void displayStart() {
        System.out.println(ANSI_GREEN + "Start!" + ANSI_RESET);
    }

    /**
     * Displays a message to inform that the game is almost over;
     */
    public static void displayGameAlmostOver() {
        System.out.println(ANSI_YELLOW + "The bag is empty! You are almost at the end of the game!" + ANSI_RESET);
    }

    /**
     * Displays the players of the Qwirkle game;
     */
    public static void displayAllPlayers(List<String> playersList) {
        System.out.println();
        System.out.print(ANSI_CYAN + "The players of this Qwirkle game are: ");
        for (String playerName : playersList) {
            System.out.print(playerName + " ");
        }
        System.out.println(ANSI_RESET);
    }

    /**
     * Displays a game over message.
     */
    public static void endGame(List<String> names) {
        if (names.size() == 1) {
            System.out.println(ANSI_CYAN + "THE WINNER IS " + ANSI_RESET +
                    ANSI_YELLOW + names.get(0) + ANSI_RESET + ANSI_CYAN + "!!!");
        } else {
            System.out.print(ANSI_CYAN + "THE WINNERS ARE " + ANSI_RESET);
            for (int i = 0; i < names.size() - 2; i++) {
                System.out.print(ANSI_YELLOW + names.get(i) + ", ");
            }
            System.out.print(ANSI_YELLOW + names.get(names.size() - 2) + ANSI_RESET + ANSI_CYAN + " AND " + ANSI_RESET);
            System.out.println(ANSI_YELLOW + names.get(names.size() - 1) + ANSI_RESET + ANSI_CYAN + "!!!");
        }
        System.out.println("Well played!");
        System.out.println("Bye Bye, hope you liked it ;)" + ANSI_RESET);
        System.out.println();
    }

    /**
     * Displays the serialized files if the directory contains any;
     *
     * @return true if the directory is empty;
     */
    public static boolean noSerializedFiles() {
        boolean emptyDirectory = false;
        String directoryPath = "game.ser/";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null && files.length > 0) {
            System.out.println(ANSI_GREEN + "Serialized files in the directory \"" + directoryPath + "\": ");
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("-" + file.getName());
                }
            }
            System.out.println(ANSI_RESET);
        } else {
            emptyDirectory = true;
            System.out.println(ANSI_ORANGE + "Invalid or empty directory : " + directoryPath + ANSI_RESET);
            System.out.println();
        }
        return emptyDirectory;
    }

    /**
     * Asks the name of the file;
     */
    public static void asksFileName() {
        System.out.println(ANSI_CYAN + "Enter the name of the file: " + ANSI_RESET);
    }

    /**
     * Displays a message to inform that the game was successfully (de)serialized;
     */
    public static void successMsg(String process) {
        System.out.println(ANSI_GREEN + "The game was successfully " + process + "!" + ANSI_RESET);
    }

    /**
     * Displays a message to inform that an error has occurred while (de)serializing the game;
     */
    public static void errorMsg(String error, String process) {
        System.out.println(ANSI_ORANGE + "Error while " + process + " the game : " + error + ANSI_RESET);
        System.out.println();
    }


}