/*
 * Top.java
 *
 * Version:
 *	$Id$
 *
 * Revisions:
 *	&Log$
 *
 */

import java.util.*;
import java.lang.reflect.*;

/** 
 * This class represents a player who knows how to play the game of Mancala. 
 *
 * @author Eric Warrington
 *
 */

public class Top
{
	private int turn=1, numFirsts=0;
	
	public Top()
	{
		turn=1;
	}
	
	/** 
	 *  This method calls the appropriate strategy.
	 *
	 *  @param	board	the current state of the game
	 *
	 *  @return	the selected column of the board	 
	 */
	public int strategy(Board board)
	{
		return strategy1(board);
		//return randStrategy(board);
	}

	/**
	 *  Take a turn using a random strategy.
	 *
	 *  @param	board   the current state of the game
	 *
	 *  @return		the selected column of the board  
	 */
	public int randStrategy(Board board)
	{
		int row = 1, column = 1;
		boolean valid = false;
		
		while(!valid)
		{
			column = (int)(Math.random()*(board.WIDTH)) + 1;
			if((column>0) && (column<board.WIDTH-1) && (board.getSeeds(row,column)!=0))
				valid = true;
		}
		
		return column;
	}

	/**
	 *  Take a turn using another strategy. 
	 *
	 *  @param	board   the current state of the game
	 *
	 *  @return		   the selected column of the board
	 */
	public int strategy1(Board board)
	{
		int choice=-1;
		
		//STRATEGY:
		//turn	col	notes
		//1		1
		//2,1	4	(again)
		//2,2	1	(again)
		//2,3	5	(cap)
		//3		rand
		
		//test if it's turn 1
		if(turn==1 || isFirstTurn(board))
		{
			//System.out.println("First Turn " + numFirsts++);
			turn=1;
			/*
			try
			{
				Field field = Math.class.getDeclaredField("randomNumberGenerator");
		        field.setAccessible(true);
		        Random r=new Random();
		        r.setSeed(50);
		        field.set(null, r);
			}
			catch(Exception e) {}
			//*/
		}
		//System.out.println("turn " + turn);
		
		switch(turn)
		{
			case 1:
				choice=1;
				break;	
			case 2:			//2,1
				for(int i=4; i<board.WIDTH-1; i++)
				{
					if(board.getSeeds(1,i)==i)
					{
						choice=i;
						break;
					}
				}
				break;	
			case 3:			//2,2
				choice=1;
				break;	
			case 4:			//2,3
				for(int i=4; i<board.WIDTH-1; i++)
					if(board.getSeeds(1,i)==i-1)
						choice=i;
				break;	
		}
		
		turn++;
		
		if(choice == -1)
			choice=pitChoice(board);
		
		if(choice == -1)
			choice=randStrategy(board);
			
		//System.out.println(choice);
		return choice;
		
		
		//int row = 1;
		//return -1;
	}

	/**
	 *  What happens when a player makes a move.
	 *
	 *  @param	board	state of the game before move (changed here)
	 *  @param	column   selected column of board 
	 *
	 *  @return			1 if extra turn, 2 if capture, -1 otherwise
	 */
	public int move(Board board, int c)
	{
		//default capabilities
		int r = 1;
		int min=0, max=6;
		board.setColor(r, c, "Green");
		
		//the orig # seeds in selected pit
		int seeds=board.getSeeds(r,c);
		//set the seeds in the selected pit to 0
		board.setSeeds(r, c, 0);
		
		int s=-1;
		
		//TODO fix bound cases
		//if(c==max) c-=1;
		
		//increment the seeds in the other pits to the appropriate amount according to the rules of the game
		for(int i=0; i<seeds; i++)
		{
			//counter-clockwise
			int dir=(r==1) ? -1 : 1;
			
			//if(c!=max) 
				c+=dir;
			s=board.getSeeds(r,c);
			board.setSeeds(r, c, s+1);
			
			//change color
			board.setColor(r, c, "Green");
			//System.out.println("r,c = " + r + "," + c);
			
			//if(c==max) c+=dir;
			
			//switch rows?
			if((i+1)<seeds)
			{
				if(c==min && r==1)
				{
					r=2;
				}
				else if(c==max && r==2)
				{
					r=1;
					c++;	//if we continue, accomodate for dir calcs
				}
			}
		}
		
		//determine if the move resulted in an extra turn
		if(c==min)
			return 1;
		//determine if the move resulted in a capture
			//int r2=(r+1)%2;	//(r==0) ? 1 : 0;
		if(s==0 && r==1)
		{
			int s2=board.getSeeds(2,c);
			int man=board.getSeeds(1,0);
			//add seeds to Mancala
			board.setSeeds(1,0, man+s2+1);
			//clear pits
			board.setSeeds(1,c, 0);
			board.setSeeds(2,c, 0);
			//set color
			board.setColor(2,c, "Red");
			return 2;
		}
		

		return -1;
	}
	
	
	private boolean isFirstTurn(Board b)
	{
		//boolean first=true;
		
		//if Top has scored, it's not the first turn
		if(b.getSeeds(1,0)>=1)
			return false;
		
		//for(int c=1; c<3; c++)	//board rows start @ index 1 and end @ 2
		
		int s=-1;
		int min=4;
		
		//Top's row will be increasing with a min of 4 
		for(int i=1; i<b.WIDTH-1; i++)	//skip first and last columns
		{
			s=b.getSeeds(1,i);
			if(s<min)
				return false;
			min=s;
		}
		
		return true;
	}
	
	private int pitChoice(Board b)
	{
		int i=xtraTurn(1, b);
		int max=(i == -1) ? b.WIDTH-1 : i;
		for(int c=1; c<max; c++)
		{
			//check how many stones are cap'd
			int caps=isCap(c, b);
			if(caps != -1)
			{
				//check how many stones are cap'd
				//int caps=b.getSeeds(1,c)+1;
				
				//if there are no huge clumps, always return first cap
				if(caps>2) return c;
				if(i==-1)	//no shot at xtra turn & less than 2 stones are cap'd
				{
					int a=hugeClumps(b);
					if(a==-1)	//no clumps
						return c;
					else
						return a;
				}
				//else: only one stone cap'd BUT there's an extra turn option
			}
		}
		
		int a=hugeClumps(b);
		if(i==-1)	//no xtra; possible clumps
			return a;
		else		//extra turn
			return i;
	}
	
	//return the first index that gives an extra turn
	private int xtraTurn(int min, Board b)
	{
		for(int i=min; i<b.WIDTH-1; i++)
			if(b.getSeeds(1,i)==i)
				return i;
		return -1;
	}
	
	private int isCap(int c, Board b)
	{
		int s=b.getSeeds(1,c);
		int x=b.WIDTH-2;
		int z=2*x+1;
		
		if(s>=c)
			s-=c+x+1;
		while(s>z)
		{
			s-=z;
		}
		
		if(s<=0 || s>=x) return -1;
		
		int y=x-s;
		if(b.getSeeds(1,y)==0)
			return b.getSeeds(2,y);
		return -1;
	}
	
	private int hugeClumps(Board b)
	{
		//check for huge clumps
		final int huge=7;
		for(int a=1; a<b.WIDTH-1; a++)
		{
			if(b.getSeeds(1,a)>huge)
				return a;
		}
		
		return -1;
	}
}
