package g60085.qwirkle.view;

import g60085.qwirkle.model.Color;
import g60085.qwirkle.model.GridView;
import g60085.qwirkle.model.Player;
import g60085.qwirkle.model.Shape;

public class View {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_ORANGE = "033 [48:5:208m%s033 [mn";

    public static void display(GridView grid) {
        for (int i = 37; i < 51; i++) {
            System.out.print(i + " |");
            for (int j = 43; j < 53; j++) {
                String shape = "";
                switch (grid.get(i, j).shape()) {
                    case SQUARE -> shape = "[]";
                    case CROSS -> shape = "X";
                    case ROUND -> shape = "O";
                    case DIAMOND -> shape = "<>";
                    case STAR -> shape = "*";
                    case PLUS -> shape = "+";
                }
                switch (grid.get(i, j).color()) {
                    case BLUE -> System.out.print(ANSI_BLUE + shape + ANSI_RESET);
                    case RED -> System.out.print(ANSI_RED + shape + ANSI_RESET);
                    case GREEN -> System.out.print(ANSI_GREEN + shape + ANSI_RESET);
                    case ORANGE -> System.out.print(ANSI_ORANGE + shape + ANSI_RESET);
                    case PURPLE -> System.out.print(ANSI_PURPLE + shape + ANSI_RESET);
                    case YELLOW -> System.out.print(ANSI_YELLOW + shape + ANSI_RESET);
                }
            }
        }
        System.out.println();
        for(int j = 0; j<53; j++){
            System.out.print(j + " ");
        }
        System.out.println();
    }

    public static void display(Player player) {
        System.out.println("Name: " + player.getName());
        System.out.println("Hand: " + player.getTiles());

    }

    public static void displayHelp() {
        System.out.println("Q W I R K L E");
        System.out.println("Qwirkle command:");
        System.out.println("- play 1 tile : o <row> <col> <i>");
        System.out.println("- play line: l <row> <col> <direction> <i1> [<i2>]");
        System.out.println("- play plic-ploc : m <row1> <col1> <i1> [<row2> <col2> <i2>]");
        System.out.println("- play first : f <i1> [<i2>]");
        System.out.println("- pass : p");
        System.out.println("- quit : q");
        System.out.println("    i : index in list of tiles");
        System.out.println("    d : direction in l (left), r (right), u (up), d(down)");
    }

    public static void displayError(String message) {
        System.out.println(message);

    }
}
