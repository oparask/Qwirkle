package g60085.qwirkle.view;

import g60085.qwirkle.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public static void colorShape(Tile tile) {
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
            case ORANGE -> System.out.print(ANSI_CYAN + shape + ANSI_RESET);
            case PURPLE -> System.out.print(ANSI_PURPLE + shape + ANSI_RESET);
            case YELLOW -> System.out.print(ANSI_YELLOW + shape + ANSI_RESET);
        }
    }

    public static void display(GridView grid) {
        for (int i = 37; i < 51; i++) {
            System.out.print(i + " |");
            for (int j = 43; j < 53; j++) {
                if (grid.get(i, j) == null) {
                    System.out.print("   ");
                } else {
                    colorShape(grid.get(i, j));
                }
            }
            System.out.println();
        }
        System.out.print("    ");
        for (int j = 43; j < 53; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
    }

    public static void displayPlayer(Game game) {
        System.out.println(game.getCurrentPlayerName() + "! It's your turn!");
        System.out.print("Your hand is : ");
        List<Tile> hand = game.getCurrentPlayerHand();
        for (Tile tile : hand) {
            colorShape(tile);
        }
        System.out.println();
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

    public static void beginning() {
        System.out.println("Q W I R K L E");
        System.out.println();
    }

    public static int robustReadingInt() {
        Scanner keyboard = new Scanner(System.in);
        while (!keyboard.hasNextInt()) {
            System.out.println("Invalid number, try again!: ");
            keyboard.next();
        }
        return keyboard.nextInt();
    }

    public static int robustReadingPlayers() {
        int number = robustReadingInt();
        while (number < 2 || number > 4) {
            System.out.println("The number of players must be between 2 and 4, try again! : ");
            number = robustReadingInt();
        }
        return number;
    }

    public static String robustReadingString() {
        Scanner keyboard = new Scanner(System.in);
        while (!keyboard.hasNextLine()) {
            System.out.println("Invalid name, try again! : ");
            keyboard.next();
        }
        return keyboard.nextLine();
    }

    public static String robustReadingDirection() {
        Scanner keyboard = new Scanner(System.in);
        String direction = robustReadingString();
        while ((!direction.equals("l")) && (!direction.equals("u")) && (!direction.equals("r")) && (!direction.equals("d"))) {
            System.out.print("The direction letter is invalid, do you need help (y or n) ? : ");
            String help = keyboard.nextLine();
            if (help.equals("y")) {
                View.displayHelp();
            }
            direction = robustReadingString();
        }
        return direction;
    }


    public static int robustReadingIndexes() {
        System.out.println("Enter the index of the tile that you want to play: ");
        int index = robustReadingInt();
        while (index < 0 || index > 5) {
            System.out.println("The index must be between 0 and 5 , try again! : ");
            index = robustReadingInt();
        }
        return index;
    }


    public static int nbPlayers() {
        System.out.print("Enter the number of players: ");
        return robustReadingPlayers();
    }

    public static List<Player> namePlayers(int nbPlayers) {
        List<Player> playersList = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            System.out.print("Enter the name of the player " + (i + 1) + ": ");
            String name = robustReadingString();
            Player player = new Player(name);
            playersList.add(player);
        }
        System.out.println();
        System.out.print("The players of this Qwirkle game are: ");
        for (int i = 0; i < playersList.size(); i++) {
            System.out.print(playersList.get(i).getName() + " ");
        }
        System.out.println();
        return playersList;
    }

    public static void initTiles(Game game) {
        for (int i = 0; i < game.getPlayers().length; i++) {
            game.getPlayers()[i].refill();
        }
    }

    public static void startPlayer(Game game) {
        System.out.println("Who is starting ? ");
        game.setCurrentPlayer(robustReadingString());
    }

    public static void firstAdd(Game game) {
        System.out.println("Start!");
        displayPlayer(game);
        game.first(direction(), indexes());
        display(game.getGrid());
    }

    public static Direction direction() {
        System.out.print("Enter the direction of your tiles placement: ");
        String direction = robustReadingDirection();
        return switch (direction) {
            case "d" -> Direction.DOWN;
            case "u" -> Direction.UP;
            case "l" -> Direction.LEFT;
            case "r" -> Direction.RIGHT;
            default -> throw new RuntimeException("invalid direction");
        };
    }

    public static int[] indexes() {
        Scanner keyboard = new Scanner(System.in);
        List<Integer> indexes = new ArrayList<>();
        boolean more = true;
        while (indexes.size() < 6 && more) {
            int index = robustReadingIndexes();
            indexes.add(index);
            System.out.println("Do you want to add more tiles ? (y or n)");
            String yesOrNo = keyboard.nextLine();
            if (yesOrNo.equals("n")) {
                more = false;
            }
        }
        int[] indexesTab = new int[indexes.size()];
        for (int i = 0; i < indexesTab.length; i++) {
            indexesTab[i] = indexes.get(i);
        }
        return indexesTab;
    }


    public static void add(Game game) {
        displayPlayer(game);
        System.out.print("Enter the type of play (o, l, m) : ");
        String play = robustReadingString();
        switch (play){
            case "o" : playOneTile(game);
            break;
            case "l" : playLine(game);
            break;
            case "m" : playMultiple(game);
            break;
        }
        display(game.getGrid());
    }

    public static void playOneTile(Game game){
        int index = robustReadingIndexes();
        System.out.print("Enter the row where you want to place the tile: ");
        int row = robustReadingInt();
        System.out.print("Enter the col where you want to place the tile: ");
        int col = robustReadingInt();
        game.play(row, col, index);
    }

    public static void playLine(Game game){
        System.out.print("Enter the row where you want to place the tile: ");
        int row = robustReadingInt();
        System.out.println("Enter the col where you want to place the tile: ");
        int col = robustReadingInt();
        game.play(row, col,direction(), indexes());
    }

    public static void playMultiple(Game game){
        Scanner keyboard = new Scanner(System.in);
        System.out.print("How many tiles do you want to place ? ");
        int nbTiles = robustReadingInt();
        while(nbTiles<2 || nbTiles >6){
            System.out.println("Number of tiles must be between 2 and 6, try again!");
            nbTiles = robustReadingInt();
        }
        int[] playMultipleTiles = new int[nbTiles*3];
        int indexTab = 0;
        for(int i = 0; i<nbTiles; i++){
            System.out.print("Enter the row where you want to place the tile " + (i+1) + ": ");
            int row = robustReadingInt();
            System.out.println("Enter the col where you want to place the tile: " + (i+1) + ": ");
            int col = robustReadingInt();
            int tileIndex = robustReadingIndexes();
            playMultipleTiles[indexTab] = row;
            playMultipleTiles[indexTab+1] = col;
            playMultipleTiles[indexTab+2] = tileIndex;
            indexTab = indexTab+3;
        }
        game.play(playMultipleTiles);
    }

    public static void endGame(){
        System.out.println("Bye Bye! Hope you liked it!");
    }
}
