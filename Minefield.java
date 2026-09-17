// Import Section
import java.util.Objects;
import java.util.Random;


public class Minefield {
    /**
     * Global Section
     */
    int numCols = 0;
    int numRows = 0;
    int numFlags = 0;
    Cell[][] minefield;
    boolean isGameOver;
    int[][] numbers;

    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";
    public static final String ANSI_MAGENTA = "\033[35m";




    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java interface to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java interface to know what type of queue you will be working with and methods you can utilize
     */


    /**
     * Minefield
     * <p>
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     *
     * @param rows    Number of rows.
     * @param columns Number of columns.
     * @param flags   Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {
        this.isGameOver = false;
        this.numFlags = flags;
        this.numCols = columns;
        this.numRows = rows;
        minefield = new Cell[rows][columns];
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[i].length; j++) {
                this.minefield[i][j] = new Cell(false, "-");
            }
        }
        numbers = new int[rows][columns];
    }


    public static boolean inBounds(Cell[][] field, int x, int y) {  // x and y are the starting coordinates

        if (x < 0 || x >= field.length) {
            return false;
        }

        if (y < 0 || y >= field[0].length) {
            return false;
        }

        return true;
    }


    /**
     * evaluateField
     *
     * @function: Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     *
     */
    public void evaluateField() {
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[i].length; j++) {
                Cell spot = minefield[i][j];
                if (spot.getStatus().equals("M")) {
                    int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}, {1, -1}, {-1, 1}, {1, 1}, {-1, -1}};
                    for (int n = 0; n < directions.length; n++) {
                        int[] x = directions[n];
                        int adjacentX = i + x[0];
                        int adjacentY = j + x[1];
                        if (inBounds(minefield, adjacentX, adjacentY) && !minefield[adjacentX][adjacentY].getStatus().equals("M")) {
                            numbers[adjacentX][adjacentY]++;
                            minefield[adjacentX][adjacentY].setStatus(numbers[adjacentX][adjacentY] + "");
                        }


                    }


                }
            }
        }

    }

    /**
     * createMines
     * <p>
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     *
     * @param x     Start x, avoid placing on this square.
     * @param y     Start y, avoid placing on this square.
     * @param mines Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        Random r = new Random();

        while (mines > 0) {
            int randomX = r.nextInt(numRows);
            int randomY = r.nextInt(numCols);


            if (randomX == x && randomY == y) {
                continue;
            }


            if (minefield[randomX][randomY].getStatus().equals("M")) {
                continue;
            }


            minefield[randomX][randomY].setStatus("M");
            mines--;
        }
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[i].length; j++) {
                if (!minefield[i][j].getStatus().equals("M")) {
                    minefield[i][j].setStatus("0");
                }
            }

        }
    }

    /**
     * guess
     * <p>
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     *
     * @param x    The x value the user entered.
     * @param y    The y value the user entered.
     * @param flag A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        Cell spot = minefield[x][y];
        if (inBounds(minefield, x, y)) {
            // if the user wants to place a flag
            if (flag == true) {
                if (spot.getRevealed()) {
                    return false;
                }
                if (numFlags > 0) {
                    numFlags--;
                    spot.setStatus("F");
                    spot.setRevealed(true);
                    return false;
                }
            } else {

                if (spot.getStatus().equals("0")) {
                    revealZeroes(x, y);
                }
                if (spot.getStatus().equals("M")) {
                    isGameOver = true;
                    return true;
                }

                spot.setRevealed(true);


            }
        }
            return false;
        }


    /**
     * gameOver
     * <p>
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otherwise return true.
     */
    public boolean gameOver() {
        // checks to see if mine has been found
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[i].length; j++) {
                if (minefield[i][j].getRevealed() == true && minefield[i][j].getStatus().equals("M")) {
                    return true;
                }



            }
        }

        // checks if every bomb is still hidden

        for (int n = 0; n < minefield.length; n++) {
            for (int k = 0; k < minefield[n].length; k++) {
                if (minefield[n][k].getRevealed() == false && !minefield[n][k].getStatus().equals("M")) {
                    return false;
                }
            }
        }


        return true;

    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     * <p>
     * This method should follow the psuedo-code given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack1Gen<int[]> stack = new Stack1Gen<>();


        stack.push(new int[]{x, y});

        while(stack.isEmpty() != true) {

            int[] removed = stack.pop();
            int newX = removed[0];
            int newY = removed[1];
            Cell spot = minefield[newX][newY];
            if (spot.getRevealed()) {
                continue;
            }
            spot.setRevealed(true);


            if (inBounds(minefield, newX - 1, newY) && minefield[newX - 1][newY].getStatus().equals("0")) {
                stack.push(new int[]{newX - 1, newY});
            }

            if (inBounds(minefield, newX, newY - 1) && minefield[newX][newY - 1].getStatus().equals("0")) {
                stack.push(new int[]{newX, newY - 1});
            }

            if (inBounds(minefield, newX + 1, newY) && minefield[newX + 1][newY].getStatus().equals("0")) {
                stack.push(new int[]{newX + 1, newY});
            }

            if (inBounds(minefield, newX, newY + 1) && minefield[newX][newY + 1].getStatus().equals("0")) {
                stack.push(new int[]{newX, newY + 1});
            }
        }


    }

    /**
     * revealStartingArea
     * <p>
     * On the starting move only reveal the neighboring cells of the initial cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     * <p>
     * This method should follow the psuedo-code given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        Q1Gen<int[]> queue = new Q1Gen<>();
        queue.add(new int[]{x, y});

        while (queue.length() > 0) {
            int[] removed = queue.remove();
            int newX = removed[0];
            int newY = removed[1];
            Cell spot = minefield[newX][newY];

            if (spot.getStatus().equals("M")) {
                break;
            }
            spot.setRevealed(true);
            // checks to the left
            if (inBounds(minefield, newX - 1, newY) && minefield[newX - 1][newY].getRevealed() == false) {
                queue.add(new int[]{newX - 1, newY});
            }
            // checks down
            if (inBounds(minefield, newX, newY + 1) && minefield[newX][newY + 1].getRevealed() == false) {
                queue.add(new int[]{newX, newY + 1});
            }
            //checks up
            if (inBounds(minefield, newX, newY - 1) && minefield[newX][newY - 1].getRevealed() == false) {
                queue.add(new int[]{newX, newY - 1});
            }
            //checks to the right
            if (inBounds(minefield, newX + 1, newY) && minefield[newX + 1][newY].getRevealed() == false) {
                queue.add(new int[]{newX + 1, newY});
            }

        }


    }

    /**
     * For both printing methods utilize the ANSI color codes provided!
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * This method should print out when debug mode has been selected. It is very similar to the toString method below.
     */
    public void debug() {
        System.out.println("DEBUG VIEW");


        System.out.print("   ");
        for (int col = 1; col <= minefield[0].length; col++)
            System.out.print(col + " ");

        System.out.println();

        for (int i = 0; i < minefield.length; i++) {
            System.out.print((i + 1) + "  ");
            for (int j = 0; j < minefield[i].length; j++) {
                String s = minefield[i][j].getStatus();

                switch (s) {
                    case "M": System.out.print(ANSI_RED + "M " + ANSI_GREY_BACKGROUND); break;
                    case "F": System.out.print(ANSI_YELLOW + "F " + ANSI_GREY_BACKGROUND); break;
                    case "0": System.out.print(ANSI_YELLOW_BRIGHT + "0 " + ANSI_GREY_BACKGROUND); break;
                    case "1": System.out.print(ANSI_CYAN + "1 " + ANSI_GREY_BACKGROUND); break;
                    case "2": System.out.print(ANSI_BLUE + "2 " + ANSI_GREY_BACKGROUND); break;
                    case "3": System.out.print(ANSI_GREEN + "3 " + ANSI_GREY_BACKGROUND); break;
                    case "4": System.out.print(ANSI_PURPLE + "4 " + ANSI_GREY_BACKGROUND); break;
                    case "5": System.out.print(ANSI_RED_BRIGHT + "5 " + ANSI_GREY_BACKGROUND); break;
                    case "6": System.out.print(ANSI_BLUE_BRIGHT + "6 " + ANSI_GREY_BACKGROUND); break;
                    case "7": System.out.print(ANSI_PURPLE_BACKGROUND + "7 " + ANSI_GREY_BACKGROUND); break;
                    case "8": System.out.print(ANSI_MAGENTA + "8 " + ANSI_GREY_BACKGROUND); break;
                    default:  System.out.print(s + " ");
                }
            }
            System.out.println();
        }


        System.out.println("PLAYERS VIEW");
        System.out.print("   ");
        for (int col = 1; col <= minefield[0].length; col++) System.out.print(col + " ");
        System.out.println();

        for (int i = 0; i < minefield.length; i++) {
            System.out.print((i + 1) + "  ");
            for (int j = 0; j < minefield[i].length; j++) {
                Cell spot = minefield[i][j];
                String s = spot.getStatus();

                if (spot.getRevealed()) {
                    switch (s) {
                        case "M": System.out.print(ANSI_RED + "M " + ANSI_GREY_BACKGROUND); break;
                        case "F": System.out.print(ANSI_YELLOW + "F " + ANSI_GREY_BACKGROUND); break;
                        case "0": System.out.print(ANSI_YELLOW_BRIGHT + "0 " + ANSI_GREY_BACKGROUND); break;
                        case "1": System.out.print(ANSI_CYAN + "1 " + ANSI_GREY_BACKGROUND); break;
                        case "2": System.out.print(ANSI_BLUE + "2 " + ANSI_GREY_BACKGROUND); break;
                        case "3": System.out.print(ANSI_GREEN + "3 " + ANSI_GREY_BACKGROUND); break;
                        case "4": System.out.print(ANSI_PURPLE + "4 " + ANSI_GREY_BACKGROUND); break;
                        case "5": System.out.print(ANSI_RED_BRIGHT + "5 " + ANSI_GREY_BACKGROUND); break;
                        case "6": System.out.print(ANSI_BLUE_BRIGHT + "6 " + ANSI_GREY_BACKGROUND); break;
                        case "7": System.out.print(ANSI_PURPLE_BACKGROUND + "7 " + ANSI_GREY_BACKGROUND); break;
                        case "8": System.out.print(ANSI_MAGENTA + "8 " + ANSI_GREY_BACKGROUND); break;
                    }
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }





    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    @Override
    public String toString() {
        String s = " ";
        // prints out column number
        for(int n = 0; n < minefield[0].length; n++){
            s += " " + (n + 1) + " ";
        }
        s += "\n";

        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[i].length; j++) {
                // prints out row number
                if(j == 0){
                    s += (i + 1) + " ";
                }

                Cell spot = minefield[i][j];
                String stat = spot.getStatus();

                if (spot.getRevealed()) {

                    if (stat.equals("M")) {
                        s += " " + ANSI_RED + stat + ANSI_GREY_BACKGROUND + " ";

                    } else if (stat.equals("F")) {
                        s += " " + ANSI_YELLOW + stat + ANSI_GREY_BACKGROUND + " ";
                    } else if (stat.equals("0")) {
                        s += " " + ANSI_YELLOW_BRIGHT + stat + ANSI_GREY_BACKGROUND + " ";
                    } else if (stat.equals("1")) {
                        s += " " + ANSI_CYAN + stat + ANSI_GREY_BACKGROUND + " ";
                    } else if (stat.equals("2")) {
                        s += " " + ANSI_BLUE + stat  + ANSI_GREY_BACKGROUND + " ";
                    } else if (stat.equals("3")) {
                        s += " " + ANSI_GREEN + stat + ANSI_GREY_BACKGROUND + " ";
                    } else if (stat.equals("4")) {
                        s +=  " " + ANSI_PURPLE + stat + ANSI_GREY_BACKGROUND + " ";
                    } else if (stat.equals("5")) {
                        s += " " + ANSI_RED_BRIGHT + stat + ANSI_GREY_BACKGROUND + " ";
                    } else if (stat.equals("6")) {
                         s += " " + ANSI_BLUE_BRIGHT + stat + ANSI_GREY_BACKGROUND + " ";
                    } else if (stat.equals("7")) {
                         s += " " + ANSI_PURPLE_BACKGROUND + stat + ANSI_GREY_BACKGROUND + " ";
                    } else if (stat.equals("8")) {
                        s += " " + ANSI_MAGENTA + stat + ANSI_GREY_BACKGROUND + " ";
                    }
                    else {
                       System.out.print(stat);
                    }

                }
                else{
                    s += ANSI_GREY_BACKGROUND + " - ";
                }

            }
            s = s + "\n";
        }
        return s;
    }

}
