//Import Section
import java.util.Random;
import java.util.Scanner;

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 * 
 * Things to Note:
 * 1. Think back to Project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */

public class Main{


    public static void main(String[] args){
        System.out.println("=== Minefield ===");
        System.out.println("Choose difficulty:");
        System.out.println("1. Easy (5x5, 5 mines)");
        System.out.println("2. Medium (9x9, 12 mines)");
        System.out.println("3. Hard (20x20, 40 mines)");
        System.out.print("Enter 1, 2, or 3: ");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        int rows = 0;
        int cols = 0;
        int mines = 0;

        switch (choice) {
            case 1:
                rows = 5;
                cols = 5;
                mines = 5;
                break;
            case 2:
                rows = 9;
                cols = 9;
                mines = 12;
                break;
            case 3:
                rows = 20;
                cols = 20;
                mines = 40;
                break;
            default:
                System.out.println("Invalid choice. Exiting.");
                return;
        }

        System.out.print("Enable debug mode? (y or n): ");
        boolean debug = sc.next().equalsIgnoreCase("y");


        Minefield game = new Minefield(rows, cols, mines);

        System.out.println("Enter your starting coordinates (row col): ");

        int startX = sc.nextInt() - 1;
        int startY = sc.nextInt() - 1;




        game.createMines(startX, startY, mines);


        game.evaluateField();


        game.revealStartingArea(startX, startY);




        while (!game.gameOver()) {

            if(debug) {
                game.debug();
            }
            else {
                System.out.println(game);
            }

            System.out.println("\nEnter command:");
            System.out.println("type \"reveal x y\"");
            System.out.println("or");
            System.out.println("type \"flag x y\", flags remaining: " + game.numFlags);
            System.out.print("> ");

            String userMove = sc.next();
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            if (x < 0 || x >= rows || y < 0 || y >= cols) {
                System.out.println("Invalid coordinates. Try again.");
                continue;
            }


            boolean flag = userMove.equalsIgnoreCase("flag");
            // if user already used up all of their flags
            if (flag && game.numFlags == 0) {
                System.out.println("No flags remaining! You've used all your flags.");
                continue;
            }

            // for already flagged cells
            if (flag && game.minefield[x][y].getRevealed() && game.minefield[x][y].getStatus().equals("F")) {
                System.out.println("You already placed a flag here! Flags can't be removed once placed.");
                continue;
            }

            boolean hitMine = game.guess(x, y, flag);

            if (hitMine) {
                System.out.println("\n You hit a mine! Game Over.");
                break;
            }



            if (game.gameOver()) {
                System.out.println("\n You revealed all safe cells! You win!");
                break;
            }
        }

        System.out.println("\nFinal Board:");
        game.debug();
    }
    }




