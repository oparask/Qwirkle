package g60085.qwirkle.view;

import g60085.qwirkle.model.Game;
import g60085.qwirkle.model.GridView;
import g60085.qwirkle.model.Tile;

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
     * Displays game title at the beginning.
     */
    public static void beginning() {
        System.out.println(ANSI_CYAN + "  _ _ _ _ _ _ _");
        System.out.println("| Q W I R K L E |");
        System.out.println("  ------------- " + ANSI_RESET);
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
            System.out.print(i + " |");
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
        System.out.println();
    }

    /**
     * Displays the current player's name and hand.
     *
     * @param game the Qwirkle game.
     */
    public static void displayPlayer(Game game) {
        System.out.println(ANSI_CYAN + game.getCurrentPlayerName() + ", It's your turn!");
        System.out.print("Your hand is : " + ANSI_RESET);
        List<Tile> hand = game.getCurrentPlayerHand();
        for (Tile tile : hand) {
            ViewColorShape(tile);
        }
        System.out.println();
    }

    /**
     * Displays Qwirkle game commands.
     */
    public static void displayHelp() {
        System.out.println(ANSI_PURPLE + "Qwirkle command:\n"
                + "- play 1 tile : o <row> <col> <i>\n"
                + "- play line: l <row> <col> <direction> <i1> [<i2>]\n"
                + "- play plic-ploc : m <row1> <col1> <i1> [<row2> <col2> <i2>]\n"
                + "- play first : f <i1> [<i2>]\n"
                + "- pass : p\n"
                + "- quit : q\n"
                + "    i : index in list of tiles\n"
                + "    d : direction in l (left), r (right), u (up), d(down)\n" + ANSI_RESET);
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
     * Displays a game over message.
     */
    public static void endGame() {
        System.out.println();
        System.out.println(ANSI_CYAN + "Bye Bye! Hope you liked it!" + ANSI_RESET);
    }
}