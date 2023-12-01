/*
This class represents the state of the board of the game.

It stores the board dimensions and contains operations regarding
the board, such as placing the user's token, returning the
available (free) columns and prints the state of the board.
 */

import java.util.*;

public class Game_Board {
    private int[][] board;
    private final int columns;
    private final int rows;

// ------ CONSTRUCTOR ------ //
    public Game_Board(int[][] board) {
        this.board = board;
        columns = board.length;
        rows = board[0].length;
    }

// ------ GETTERS AND SETTERS ------//
    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

// ------ BOARD FUNCTIONS ------ //
    //Places user's token in the board if the selected column has space
    public boolean placeToken(int colIndex){
        int[] column = board[colIndex];

        //If there is empty space, place the token
        if (Arrays.stream(column).anyMatch(x -> x == Seven_Six_Puzzle.EMPTY_TOKEN)) {
           for (int i = 0; i < column.length; i++){
               if (column[i] == Seven_Six_Puzzle.EMPTY_TOKEN){
                   column[i] = Seven_Six_Puzzle.USER_TOKEN;
                   return true;
               }
           }
        }

        return false;
    }

    // Returns all the columns that contain at least 1 empty space
    public ArrayList<Integer> available_Columns(){
        ArrayList<Integer> availColumns = new ArrayList<Integer>();
        for (int i = 0; i < columns; i++){
            if (Arrays.stream(board[i]).anyMatch(x -> x == Seven_Six_Puzzle.EMPTY_TOKEN)){
                availColumns.add(i);
            }
        }
        return availColumns;
    }

    //Clones the board without reference
    public int[][] clone(){
        int[][] clonedBoard = new int[columns][];
        for (int i = 0; i < columns; i++){
            clonedBoard[i] = new int[rows];
            System.arraycopy(board[i],0, clonedBoard[i],0, rows);
        }
        return clonedBoard;
    }

    public String printBoard(){
        String printed = "";

        String blue = "\u001B[34m";
        String red = "\u001B[31m";
        String reset = "\u001B[0m";

        for(int r = rows-1; r >= 0; r--){
            for(int c = 0; c < columns; c++){
                if (board[c][r] == Seven_Six_Puzzle.EMPTY_TOKEN){
                    printed += "|   ";
                } else if (board[c][r] == Seven_Six_Puzzle.AI_TOKEN){
                    printed += "| " + blue + "X" + reset + " ";
                } else {
                    printed += "| " + red + "O" + reset + " ";
                }
            }
            printed += "|\n";
        }

        //Add columns indexes for accessibility
        printed += "|---|---|---|---|---|---|\n" +
                "  0   1   2   3   4   5  \n";
        return printed;
    }
}
