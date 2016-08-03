/*
 * Mancala.java
 *
 * Version:
 *    $Id$
 *
 * Revisions:
 *    &Log$
 *
 */

import java.util.*;
import java.awt.*;        
import java.awt.event.*;
import javax.swing.*;

/** 
 * This program is the driver for the game of Mancala. 
 *
 * Run the program as one of the following:
 *     java Mancala          (GUI with a default delay time of 1 second)
 *     java Mancala delay    (GUI with a delay of (delay) milliseconds)
 *     java Mancala 0        (GUI with human (Bottom) vs. machine (Top))
 *     java Mancala -delay   (No GUI - run program (delay) times)
 *
 * @author     Roxanne Canosa
 *
 */

public class Mancala extends JPanel {
       
    final static int TOP = 1;                // Whose turn it is
    final static int BOTTOM = 2;

    private Top top = new Top();             // The players
    private Bottom bottom = new Bottom();

    private static Board board;              // The game state
    private javax.swing.Timer timer, animationTimer;
    private static int delay;
    private static long startTime, stopTime, runTime = 0;
    private int turn;
    private boolean top_done = false; 
    private boolean bottom_done = false;
    private boolean extra_turn, capture;
    
    /**
     *  This constructor sets up the initial game configuration, 
     *  and starts the timer with a default delay of 1 second.
     */
    public Mancala() { this(1000); }

    /**
     *  This constructor sets up the initial game configuration, 
     *  and starts the timer with a user specified delay.
     *
     *  @param    delay    number of milliseconds between player moves
     */
    public Mancala(int delay) {

        // Initialize the game state
        initGame();
       
        // Run the game with GUI - computer vs. computer using a timer
        if (delay > 0) {
            setBackground(Color.YELLOW);
            timer = new javax.swing.Timer(delay, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        playerMove(true);
                        if (top_done || bottom_done)
                            timer.stop();
                        else
                            repaint();
                    }
                });
            
            // Create the Start and Stop buttons
            JButton start = new JButton("Start"); 
            start.setBounds(10,20,80,25); 
            add(start);
            start.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        timer.start();
                    }
                });

            JButton stop = new JButton("Stop"); 
            stop.setBounds(10,80,80,25); 
            add(stop);
            stop.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        timer.stop();
                    }
                });
        }

        // Run the game with GUI - human vs. computer. 
        // The computer plays the top row and the human plays the bottom row.
        if (delay == 0) {
            setBackground(Color.YELLOW);
            addMouseListener( new MouseAdapter() {
                    public void mousePressed(MouseEvent evt) {
                        // Find out which pit was clicked
                        int x = evt.getX();
                        int y = evt.getY();
                        int screenWidth = getWidth();
                        int screenHeight = getHeight();
                        int column = (x * board.WIDTH) / screenWidth;
                        int row = (y * board.HEIGHT) / screenHeight;

                        if ((row != 2) || (column == 0) || (column == 7))  
                            System.out.println("Invalid location. Try again.");
                        else {
                            bottomMove(column,true);
                            repaint(); 
                        }  
                    }
                });

            JButton topsTurn = new JButton("Top Takes a Turn"); 
            topsTurn.setBounds(10,80,80,25); 
            add(topsTurn);
            topsTurn.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        topMove(true);
                        repaint();
                    }
                });
        }
    }
	
	
	//return the number of points deducted
	public int run(int games, boolean print)
	{
		// Keep track of how many wins each player has and scores
		int top_won=0, bottom_won=0, ties = 0;
		
		// Start timing how long it takes to play "delay" games
		startTime = new Date().getTime();
		
		// Play a bunch of games!
		for (int times=0; times < games; times++) {
			
			initGame();
			int topScore=0, bottomScore=0;
			top_done = false;
			bottom_done = false;
			
			while (!top_done && !bottom_done) {
				playerMove(false);
				
				// Check if either player can make a move
				top_done = true;
				bottom_done = true;
				
				// Top player
				for (int i=1; i < board.WIDTH-1; i++)
					if ( board.getSeeds(1,i) != 0 )
						top_done = false;
				
				// Bottom player
				for (int i=1; i < board.WIDTH-1; i++)
					if ( board.getSeeds(2,i) != 0 )
						bottom_done = false;
				
				if (top_done || bottom_done) {
					topScore = board.getSeeds(1,0);
					bottomScore = board.getSeeds(2,board.WIDTH-1);
					
					if (topScore > bottomScore) top_won++;
					else if (bottomScore > topScore) bottom_won++;
					else ties++;
				}
			}
		}
		
		stopTime = new Date().getTime();
		runTime = (stopTime - startTime);
		if(print)
		{
			System.out.println("===========================");
			System.out.println("Total number of games = " + games);
			System.out.println("Top won " + top_won + " times");
			System.out.println("Bottom won " + bottom_won + " times");
			System.out.println("Number of tied games = " + ties);
			System.out.print("\nRuntime for " + -delay + " games = ");
			
			System.out.println(runTime + " milliseconds");
			System.out.println("===========================");
			
			int points_deducted = bottom_won + ties;
			System.out.println("\nPoints deducted = " + points_deducted);
		}
		
		
		return bottom_won + ties;
	}
	
	
	
	
	
	
	/**
	 *  Initialize the game state
     *
     */
    public void initGame() {

        board = new Board();              // Create a new game board

        double chance = Math.random();  // Uncomment these lines to
        if (chance > 0.5)               // alternate who goes first
            turn = TOP;
        else
            turn = BOTTOM;
    }

    /**
     *  A player makes a move when the Timer goes off. Bottom goes
     *  first, and then Bottom and Top take turns.
     *
     *  @param gui    true if the game GUI is to be displayed
     */
    public void playerMove(boolean gui) {
		
        if (turn == BOTTOM) {
            bottomMove(0,gui);
            if (extra_turn) turn = BOTTOM;
            else turn = TOP;
        }
        else {
            topMove(gui);
            if (extra_turn) turn = TOP;
            else turn = BOTTOM;
        }
    }
    
    /**
     *  Bottom takes a turn.
     *  
     *  @param mouse       1 if mouse is used to click on a square, else 0
     *  @param gui         true if the game is to be displayed
     *
     */
    public void bottomMove(int mouse, boolean gui) {
       
        int row=2, option=0, column;
        extra_turn = false;
        capture = false;
        
        if (gui) board.printBoard("Bottom");
        
        // Check if Bottom can move anywhere
        bottom_done = true;
        for (int i=1; i < board.WIDTH-1; i++)
            if ( board.getSeeds(row,i) != 0 ) 
                bottom_done=false;

        if (!bottom_done) {
            // Pick a column - using either AI strategy or human (with mouse)
            if (mouse == 0)              
                column = bottom.strategy(board);
            else          
                column = mouse;
            option = bottom.move(board,column);
            if (option == 1) extra_turn = true;
            if (option == 2) capture = true;
        }
    } 

    /**
     *  Top takes a turn.
     *
     *  @param gui         true if the game board is to be displayed
     *
     */
    public void topMove(boolean gui) {

        int row=1, option=0, column;
        extra_turn = false;
        capture = false;

        if (gui) board.printBoard("Top");
        
        // Check if Top can move anywhere
        top_done = true;
        for (int i=1; i < board.WIDTH-1; i++)
            if ( board.getSeeds(row,i) != 0 ) 
                top_done=false;

        if (!top_done) {
            // Pick a column - always use AI strategy
            column = top.strategy(board); 
            option = top.move(board,column);
            if (option == 1) extra_turn = true;
            if (option == 2) capture = true;
        }
    }

   /**
    *  Draw the board and the current state of the game. 
    *
    *  @param    g    the graphics context of the game
    */
   public void paintComponent(Graphics g) {
      
       super.paintComponent(g);  // Fill panel with background color
       Font big = new Font("SansSerif", Font.BOLD, 48);
       Font small = new Font("SansSerif", Font.BOLD, 18);
       
       int width = getWidth();
       int height = getHeight();
       int xoff = width / board.WIDTH;
       int yoff = height / board.HEIGHT;

       int bottomScore = 0;                     
       int topScore = 0; 

       g.setFont(small);
       g.setColor(Color.GREEN);
       g.drawString("TOP", 10, yoff);
       g.setColor(Color.RED);
       g.drawString("BOTTOM", width-xoff, yoff);

       g.setColor(Color.BLACK);
       g.setFont(big);
       // Draw the horizontal lines on the board
       for (int i=0; i <= board.HEIGHT; i++)
           if (i==2)
               g.drawLine(xoff, i*yoff, width-xoff, i*yoff);
           else
               g.drawLine(0, i*yoff, width, i*yoff);
       
       // Draw the vertical lines on the board
       for (int i=0; i <= board.WIDTH; i++) 
           if ((i==0) || (i == board.WIDTH))
               g.drawLine(i*xoff, 0, i*xoff, height);
           else
               g.drawLine(i*xoff, yoff, i*xoff, height); 

       // Draw number of seeds in each pit
       for (int i=0; i < board.HEIGHT; i++)         
           for (int j=0; j < board.WIDTH; j++)
               if (board.getSeeds(i,j) != board.OFFBOARD) {
                   if ( (board.getColor(i,j)).equals("Red") )
                       g.setColor(Color.RED);
                   else if ( (board.getColor(i,j)).equals("Green") )
                       g.setColor(Color.GREEN);
                   else
                       g.setColor(Color.BLACK);
                   g.drawString(((Integer)(board.getSeeds(i,j))).toString(),
                                (j*xoff+30),(i*yoff+65));
                   board.setColor(i,j,"Black");
               }
 
       g.setColor(Color.BLUE);
       g.setFont(small);
        
       topScore = board.getSeeds(1,0);
       bottomScore = board.getSeeds(2,board.WIDTH-1);
      
       // Check if either player has won the game
       top_done = true;
       for (int i=1; i < board.WIDTH-1; i++) 
           if ( board.getSeeds(1,i) != 0 )
               top_done=false;

       bottom_done = true;
       for (int i=1; i < board.WIDTH-1; i++) 
           if ( board.getSeeds(2,i) != 0 )
               bottom_done=false;
               
       if ( top_done || bottom_done ) {
           topScore = 0;
           bottomScore = 0;
           topScore += board.getSeeds(1,0);
           bottomScore += board.getSeeds(2,board.WIDTH-1);
           if (topScore > bottomScore)
               g.drawString("Top won with " + topScore, 312, 60);
           else if (bottomScore > topScore)
               g.drawString("Bottom won with " + bottomScore, 300, 60);
           else g.drawString("Tied game", 330, 60);
       }
       else {  
           if (extra_turn) 
               g.drawString("EXTRA TURN!!!", 312, 60);
           else if (capture)
               g.drawString("CAPTURE!!!", 312, 60);
           else {
               if (topScore > bottomScore)
                   g.drawString("Top is winning with " + topScore, 260, 60);
               else if (bottomScore > topScore)
                   g.drawString("Bottom is winning with "+bottomScore,250,60);
               else g.drawString("Currently tied", 312, 60);
           }
       }
   }

    /**
     * The main program.
     *
     * @param    args    command line arguments (ignored)
     */
    public static void main(String [] args) {

        Mancala content;

        if (args.length > 1) {
            System.out.println("Usage: java Mancala delayTime");
            System.exit(0);
        }

        if (args.length == 1) {   
            try {
                delay = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                System.out.println("Command line arg must be an integer");
                System.exit(0);
            }
            content = new Mancala(delay);
            if (delay >= 0) {
                JFrame window = new JFrame("Mancala Game");
                window.setContentPane(content);
                window.setSize(770,300);
                window.setLocation(100,100);
                window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                window.setVisible(true);
            }
			else	// Run the game without the GUI - as many times as specified in delay.
			{
				content.run(-delay, true);
			}
        }
        else {
            content = new Mancala();
            JFrame window = new JFrame("Mancala Game");
            window.setContentPane(content);
            window.setSize(770,300);
            window.setLocation(100,100);
            window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            window.setVisible(true);
        }
    }
}  // Mancala