/*
This class contains the logic for each leaf node of the MiniMax algorithm.

The object contains attributes such as the node's parent, its status (in this
case the board configuration), the successors of the node and its evaluation (cost).
It also contains function for generating and evaluating its successors.
 */

import java.util.*;
public class MiniMax_Leaf {
    private final MiniMax_Leaf parentNode;
    private final Game_Board currentBoardObj;
    private final int[][] currentBoard;
    private final ArrayList<MiniMax_Leaf> successors = new ArrayList<MiniMax_Leaf>();

    private int evaluation_cost;
    private int vPoints, hPoints, dPoints, aPoints;

// ------ CONSTRUCTOR ------ //
    public MiniMax_Leaf(Game_Board currentBoardObj, MiniMax_Leaf parentNode) {
        this.currentBoardObj = currentBoardObj;
        this.parentNode = parentNode;
        currentBoard = currentBoardObj.clone();
    }

// ------ GETTERS AND SETTERS ------ //
    public MiniMax_Leaf getParentNode() {
        return parentNode;
    }

    public Game_Board getCurrentBoardObj() {
        return currentBoardObj;
    }

    public int getEvaluationCost() {
        return evaluation_cost;
    }

    public void setEvaluation_cost(int evaluation_cost) {
        this.evaluation_cost = evaluation_cost;
    }

    public ArrayList<MiniMax_Leaf> getSuccessors() {
        return successors;
    }

    // ------ LEAF FUNCTIONS ------ //
    //Generate the successors of the node based on its type (MAX or MIN)
    public ArrayList<MiniMax_Leaf> generateSuccessors(int agent){
        ArrayList<Integer> availColumns = currentBoardObj.available_Columns();

        for(int col : availColumns){
            for(int r = 0; r < currentBoardObj.getRows(); r++){
                if (currentBoard[col][r] != Seven_Six_Puzzle.AI_TOKEN && currentBoard[col][r] != Seven_Six_Puzzle.USER_TOKEN){
                    Game_Board newBoard = new Game_Board(currentBoard);
                    int [][] newBoardState = currentBoardObj.clone();
                    newBoardState[col][r] = agent;
                    newBoard.setBoard(newBoardState);
                    successors.add(new MiniMax_Leaf(newBoard, this));
                    break;
                }
            }
        }

        return successors;
    }

    //Evaluate the node
    public void calcEvaluation(){
        vPoints = 0;
        hPoints = 0;
        dPoints = 0;
        aPoints = 0;
        int result;

        // Check row possible combinations
        //Remove the last 3 columns because there is not enough space
        //to create 4 in a row
        result = iterateThrough(currentBoardObj.getColumns() - 3,
                currentBoardObj.getRows(), 1, 0);
        hPoints = result;

        // Check column possible combinations
        //Remove the last 3 rows because there is not enough space
        //to create 4 in a column
        result = iterateThrough(currentBoardObj.getColumns(),
                currentBoardObj.getRows() - 3, 0, 1);
        vPoints = result;

        // Check diagonal possible combinations
        //Remove the last 3 rows  and last 3 columns because there is
        // not enough space to create 4 in a diagonal
        result = iterateThrough(currentBoardObj.getColumns() - 3,
                currentBoardObj.getRows() - 3, 1, 1);
        dPoints = result;

        // Check anti-diagonal possible combinations
        //Remove the last 3 rows  and last 3 columns because there is
        // not enough space to create 4 in an anti-diagonal
        result = 0;
        for (int c = currentBoardObj.getColumns()-1; c > 2; c--){
            for (int r =0; r < currentBoardObj.getRows() - 3; r ++){
                result += evalPosition(c, r, -1, 1);

                if(result < -1000 || result > 10000){
                    break;
                }
            }
        }
        aPoints = result;

        evaluation_cost = hPoints + vPoints + dPoints + aPoints;
    }


    private int iterateThrough(int col, int row, int col_incr, int row_incr){
        int points = 0;
        for(int c = 0; c < col; c++){
            for(int r = 0; r < row; r++){
                points += evalPosition(c, r, col_incr, row_incr);

                if(points < -1000 || points > 10000){
                    return points;
                }
            }
        }
        return points;
    }

    //Evaluate a specific position in the board configuration
    private int evalPosition(int col, int row, int col_incr, int row_incr){
        int userPoints = 0, minMaxPoints = 0;

        //Calculate combinations of 4 in a row
        for (int i = 0; i < 4; i++){
            if (currentBoard[col][row] == Seven_Six_Puzzle.AI_TOKEN){
                //Found a USER token in the combination before cancel the point
                //for both
                if(userPoints != 0){
                    userPoints = 0;
                    break;
                } else{
                    minMaxPoints++;
                }
            } else if(currentBoard[col][row] == Seven_Six_Puzzle.USER_TOKEN){
                //Found a MinMax token in the combination before cancel the point
                //for both
                if(minMaxPoints != 0){
                    minMaxPoints = 0;
                    break;
                } else {
                    userPoints++;
                }
            }

            col += col_incr;
            row += row_incr;
        }

        if(minMaxPoints == 4){
            return 10000;
        } else if (userPoints == 4){
            return -10000;
        } else {
            return minMaxPoints - userPoints;
        }
    }
}