/*
 * Bottom.java
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

public class Bottom
{   
    /** 
     *  This method calls the appropriate strategy.
     *
     *  @param    board   the current state of the game
     *
     *  @return           the selected column of the board     
     */
    public int strategy(Board board)
    {
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
    public int randStrategy(Board board)
    {

        int row = 2, column = 1;
        boolean valid = false;
        
        while(!valid)
        {
            column=(int)(Math.random()*(board.WIDTH))+1;
            if((column>0) && (column<board.WIDTH-1) && (board.getSeeds(row, column)!=0))
                valid = true;
        }
        
        //System.out.println(column);
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
        
        int row = 2;
        
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
    public int move(Board board, int c)
    {
        //default capabilities
		int r = 2;
		int min=1, max=7;
		board.setColor(r, c, "Red");
		
		//the orig # seeds in selected pit
		int seeds=board.getSeeds(r,c);
		//set the seeds in the selected pit to 0
		board.setSeeds(r, c, 0);
		
		int s=-1;
		
		//TODO fix bound cases
		//if(c==min) c+=1;
		
		//increment the seeds in the other pits to the appropriate amount according to the rules of the game
		for(int i=0; i<seeds; i++)
		{
			//counter-clockwise
			int dir=(r==1) ? -1 : 1;
			
			//if(c!=min) 
				c+=dir;
			s=board.getSeeds(r,c);
			board.setSeeds(r, c, s+1);
			
			//change color
			board.setColor(r, c, "Red");
			//System.out.println("r,c = " + r + "," + c);
			
			//if(c==min) c+=dir;
			
			//switch rows?
			if((i+1)<seeds)
			{
				if(c==min && r==1)
				{
					r=2;
					c--;	//if we continue, accomodate for dir calcs
				}
				else if(c==max && r==2)
				{
					r=1;
				}
			}
		}
		
		//determine if the move resulted in an extra turn
		if(c==max)
			return 1;
		//determine if the move resulted in a capture
			//int r2=(r+1)%2;	//(r==0) ? 1 : 0;
		if(s==0 && r==2)
		{
			int s2=board.getSeeds(1,c);
			int man=board.getSeeds(2,board.WIDTH-1);
			//add seeds to Mancala
			board.setSeeds(2,board.WIDTH-1, man+s2+1);
			//clear pits
			board.setSeeds(1,c, 0);
			board.setSeeds(2,c, 0);
			//set color
			board.setColor(1,c, "Green");
			return 2;
		}
		

		return -1;
        
        
        /**
        
        int row = 2;

        board.setSeeds(row,column,0);
        board.setColor(row,column,"Red");

        return -1;
        
        */
    }
}
