/*
 * Top.java
 *
 * Version:
 *    $Id$
 *
 * Revisions:
 *    &Log$
 *
 */

import java.util.*;

/** 
 * This class represents a player who knows how to play the game of Mancala. 
 *
 * @author     YOUR NAME GOES HERE
 *
 */

public class Top {   

    /** 
     *  This method calls the appropriate strategy.
     *
     *  @param    board   the current state of the game
     *
     *  @return           the selected column of the board     
     */
    public int strategy(Board board) {

        //return strategy1(board);
        return randStrategy(board);
    }

    /**
     *  Take a turn using a random strategy.
     *
     *  @param    board   the current state of the game
     *
     *  @return           the selected column of the board  
     */
    public int randStrategy(Board board) {

        int row = 1, column = 1;
        boolean valid = false;
        
        while (!valid) {
            column = (int)(Math.random()*(board.WIDTH)) + 1;
            if ( (column > 0) && (column < board.WIDTH-1) 
                 && (board.getSeeds(row,column) != 0) )
                valid = true;
        }
        
        return column;
    }

    /**
     *  Take a turn using another strategy. 
     *
     *  @param    board   the current state of the game
     *
     *  @return           the selected column of the board
     */
    public int strategy1(Board board) {
        
        int row = 1;
        
        return -1;
    }

    /**
     *  What happens when a player makes a move.
     *
     *  @param    board    state of the game before move (changed here)
     *  @param    column   selected column of board 
     *
     *  @return            1 if extra_turn, 2 if capture
     */
    public int move(Board board, int column) {
        
        int row = 1;

        board.setSeeds(row,column,0);
        board.setColor(row,column,"Green");

        return -1;
    }
}
