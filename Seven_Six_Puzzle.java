/*
This class contains the game logic.

It is responsible for setting the board and running the game,
as well as, checking for possible wins.
 */

import java.util.Scanner;

public class Seven_Six_Puzzle {
    public static final int AI_TOKEN = 1;
    public static final int USER_TOKEN = -1;
    public static final int EMPTY_TOKEN = 0;
    private final Game_Board gameBoard;
    private MiniMax_Leaf currentNode;

    private final MiniMax_Agent miniAI;

// ------ CONSTRUCTOR ------ //
    public Seven_Six_Puzzle(int depth) {
        gameBoard = new Game_Board(new int[6][7]);
        currentNode = new MiniMax_Leaf(gameBoard, null);
        miniAI = new MiniMax_Agent(depth);
    }

// ------ GAME FUNCTIONS ------ //
    public void runGame(){
        System.out.printf("------------------------------------\n" +
                "Let the games begin!!\n" +
                "This is the board:\n" +
                "%s", gameBoard.printBoard());
        System.out.print("\nand I play first!\n");

        while(true){
            currentNode = miniAI.playTurn(currentNode);
            System.out.printf("Here is my move:\n" +
                "%s", currentNode.getCurrentBoardObj().printBoard());

            if (checkWins()){
                break;
            }

            gameBoard.setBoard(currentNode.getCurrentBoardObj().getBoard());

            System.out.print("\nYour turn now!\n" +
                    "Select the column for your next move: ");

            userInput();
            System.out.printf("You played:\n" +
                    "%s", currentNode.getCurrentBoardObj().printBoard());

            if (checkWins()){
                break;
            }
        }

    }

    private void userInput(){
        Scanner in = new Scanner(System.in);
        while(true){
            int minMove = -1;

            //Checks the validity of the input
            do {
                while (!in.hasNextInt()) {
                    System.out.print("Oops! Something went wrong with your input!\n" +
                            "Please enter a number between 0 and 5 for the column (0-5): ");
                    in.next();
                }
                minMove = in.nextInt();
                if (minMove < 0 || minMove >= 6){
                    System.out.print("Oops! I need a number between 0 and 5 for the column (0-5): ");
                }
            } while (minMove < 0 || minMove >= 6);

            //Check if the selected column has space
            if(gameBoard.placeToken(minMove)){
                currentNode = new MiniMax_Leaf(gameBoard, null);
                break;
            } else {
                System.out.print("Oops! The column is already full!\n" +
                        "Please choose another one: ");
                minMove = -1;
            }
        }

    }

    //Check if the move resulting in a win
    private boolean checkWins(){
        currentNode.calcEvaluation();
        int eval = currentNode.getEvaluationCost();

        if (eval > 10000){
            System.out.printf("\nFinal Game Configuration\n" +
                    "%s", currentNode.getCurrentBoardObj().printBoard());
            System.out.print("\n>>> I am the Winner! <<< \n");
            return true;
        } else if (eval < -1000){
            System.out.printf("\nFinal Game Configuration\n" +
                    "%s", currentNode.getCurrentBoardObj().printBoard());
            System.out.print("\n>>> You are the Winner! <<< \n");
            return true;
        }

        if (currentNode.getCurrentBoardObj().available_Columns().isEmpty()){
            System.out.printf("\nFinal Game Configuration\n" +
                    "%s", currentNode.getCurrentBoardObj().printBoard());
            System.out.print("\n>>> There are no other empty spaces. It's a Draw! <<< \n");
            return true;
        }

        return false;
    }
}
