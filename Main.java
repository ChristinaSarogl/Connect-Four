/*
This class is used to get the input for the user for the depth of the search and starts the game.
 */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Let's play Seven Six Puzzle. I am X and you are O.\n" +
                "But before we start, you have to select a depth between 1 - 7.\n" +
                "Please enter the looking-ahead depth: ");

        int depth = 0;
        //Checks the validity of the input
        do {
            while (!in.hasNextInt()) {
                System.out.print("Oops! Something went wrong with your input!\n" +
                        "Please enter a positive number for the depth: ");
                in.next();
            }
            depth = in.nextInt();
            if (depth <=0){
                System.out.print("Oops! I need a positive number for the depth: ");
            }
        } while (depth <= 0 );

        Seven_Six_Puzzle game = new Seven_Six_Puzzle(depth);
        game.runGame();
    }
}