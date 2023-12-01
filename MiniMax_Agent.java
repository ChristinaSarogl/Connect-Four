/*
This class contains the MinMax logic.

It contains functions for selecting the next node,
generating the current node's successors and evaluating
them.
 */

import java.util.*;

public class MiniMax_Agent {
    private final int depth;
    private ArrayList<MiniMax_Leaf> successors = new ArrayList<MiniMax_Leaf>();

// ------ CONSTRUCTORS ------//
    public MiniMax_Agent(int depth) {
        this.depth = depth;
    }

// ------ MINIMAX FUNCTIONS ------ //
    //Returns the next selected node
    public MiniMax_Leaf playTurn(MiniMax_Leaf state){
        successors = new ArrayList<MiniMax_Leaf>();
        successors.add(state);
        MiniMax_Leaf result = null;

        // Generate successors of the given depth
        for (int d = 1; d <= depth; d++){
            if (d % 2 == 0) {
                successors = getSuccessors(successors, Seven_Six_Puzzle.USER_TOKEN);
            } else {
                successors = getSuccessors(successors, Seven_Six_Puzzle.AI_TOKEN);
            }
        }

        successors = evaluateSuccessors(successors, state);

        //Selecting the node with the biggest value
        int max = -10000000;
        for(MiniMax_Leaf suc : successors) {
            if(suc.getEvaluationCost() > max){
                max = suc.getEvaluationCost();
                result = suc;
            }
        }

        return result;
    }

    private ArrayList<MiniMax_Leaf> getSuccessors(ArrayList<MiniMax_Leaf> states, int mode){
        ArrayList<MiniMax_Leaf> tempStates = new ArrayList<MiniMax_Leaf>();

        for (MiniMax_Leaf state : states){
            tempStates.addAll(state.generateSuccessors(mode));
        }

        return tempStates;
    }

    //Evaluates all the successor nodes
    private ArrayList<MiniMax_Leaf> evaluateSuccessors(ArrayList<MiniMax_Leaf> suc, MiniMax_Leaf state){
        ArrayList<MiniMax_Leaf> parents = new ArrayList<MiniMax_Leaf>();
        ArrayList<MiniMax_Leaf> parentsOfParents = new ArrayList<MiniMax_Leaf>();
        int evalutionMetric = depth;

        //Locate all parents
        for(MiniMax_Leaf s : suc){
            //Calculate the successor's evaluation function (cost)
            s.calcEvaluation();
            MiniMax_Leaf par = s.getParentNode();
            if(!parents.contains(par)){
                parents.add(par);
            }
        }

        while(true){
            for(MiniMax_Leaf par : parents){
                /*
                If the depth is even, then we calculate the values for the
                MIN nodes, hence the parent MAX node will take the minimum
                value of the children
                */
                if (evalutionMetric % 2 == 0){
                    par.setEvaluation_cost(getMin(par.getSuccessors(),par));

                /*
                If the depth is odd, then we calculate the values for the
                MAX nodes, hence the parent MIN node will take the maximum
                value of the children
                */
                } else {
                    par.setEvaluation_cost(getMax(par.getSuccessors(),par));
                }

                MiniMax_Leaf parOfParent = par.getParentNode();
                //We have reached the initial node, so we need to return the successors
                if (parOfParent == null) {
                    return par.getSuccessors();
                } else if (!parentsOfParents.contains(parOfParent)){
                    parentsOfParents.add(parOfParent);
                }
            }

            // If the parents of the new nodes contain the initial state exit the loop.
            if(parentsOfParents.contains(state)){
                return parents;

            //Update the lists and evaluate the new parents
            } else {
                parents.removeAll(parents);
                parents.addAll(parentsOfParents);
                parentsOfParents.clear();
                evalutionMetric--;
            }
        }
    }

    private int getMin(ArrayList<MiniMax_Leaf> children, MiniMax_Leaf parent){
        int min = 10000;
        for (MiniMax_Leaf child : parent.getSuccessors()){
            if(child.getEvaluationCost() < min){
                min = child.getEvaluationCost();
            }
        }
        return min;
    }

    private int getMax(ArrayList<MiniMax_Leaf> children, MiniMax_Leaf parent){
        int max = -10000000;
        for(MiniMax_Leaf child : children) {
            if(child.getEvaluationCost() > max){
                max = child.getEvaluationCost();
            }
        }
        return max;
    }
}
