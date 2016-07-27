/**
 * Board.java
 *
 * Version:
 *     $Id:$
 *
 * Revisions:
 *     $Log:$
 *
 */

/**
 *
 * This class represents the state of the game board in the game of Mancala.
 *
 * @author Roxanne Canosa
 */

class Board {

    final static int HEIGHT = 3;
    final static int WIDTH = 8;
    final static int OFFBOARD = -1;        
    final static int INIT_SEEDS = 4;
    final static int INIT_SCORE = 0;   

    private int column = 0;        // The preferred column of this board

    private Pit board[][] = new Pit[HEIGHT][WIDTH];

    /**
     *  Constructor for an initial board 
     */
    public Board () {

        for (int i=0; i < HEIGHT; i++)
            for (int j=0; j < WIDTH; j++) {
                board[i][j] = new Pit("Black",0);
                if ( (i==0) || (j==0) || (j==WIDTH-1) )
                    board[i][j].setSeeds(OFFBOARD);
                else
                    board[i][j].setSeeds(INIT_SEEDS);

                if ( ((i==1) && (j==0)) || ((i==2) && (j==WIDTH-1)) )
                    board[i][j].setSeeds(INIT_SCORE);
            }
    }

    /**
     *  Constructor for a specific board 
     */
    public Board(Board other) {

        for (int i=0; i < HEIGHT; i++) 
            for (int j=0; j < WIDTH; j++) 
                board[i][j] = new Pit(other.getColor(i,j),other.getSeeds(i,j));
    }

    /**
     *  Mutator method to change the number of seeds in a pit 
     *
     *  @param    row     The row number, 1 or 2
     *  @param    column  The column number, 1 - 6
     *  @param    seeds   The number of seeds in the pit
     */
    public void setSeeds(int row, int column, int seeds) {
        board[row][column].setSeeds(seeds);
    }

    /**
     *  Accessor method to get the number of seeds in a pit 
     *
     *  @param    row     The row number, 1 or 2
     *  @param    column  The column number, 1 - 6
     *
     *  @return           The number of seeds at that row, column
     */
    public int getSeeds(int row, int column) {
        return board[row][column].getSeeds();
    }

    /**
     *  Mutator method to change the color of a pit
     *
     *  @param    row     The row number, 1 or 2
     *  @param    column  The column number, 1 - 6
     *  @param    color   The color of the pit
     */
    public void setColor(int row, int column, String color) {
        board[row][column].setColor(color);
    }
    
    /**
     *  Accessor method to get the color of a pit
     *
     *  @param    row     The row number, 1 or 2
     *  @param    column  The column number, 1 - 6
     *
     *  @return           The color of the pit at that row, column
     */
    public String getColor(int row, int column) {
        return board[row][column].getColor();
    }
    
    /**
     *  Mutator method to indicate the column tested for this board
     *
     *  @param    column  The column number, 1 - 6
     *  
     */
    public void setColumn(int c) {
        column = c;
    }
    
    /**
     *  Accessor method to find the column tested for this board
     *
     *  @return    column  The column number, 1 - 6
     */
    public int getColumn() {
        return column;
    }
       
    /**
     *  Check if the selected pit is greater than 0
     *  @param     row      The row number, 1 or 2
     *  @param     column   The column number, 1 - 6
     *
     *  @return             True if pit is valid (you define)
     */
    public boolean isPitValid(int row, int column) {
         
        return board[row][column].isValid();
    }

    /**
     *  Set the selected pit to valid or not
     *  @param     row      The row number, 1 or 2
     *  @param     column   The column number, 1 - 6
     *  @param     val      True if the pit is valid (you define)
     *
     */
    public void setPitValid(int row, int column, boolean val) {
        board[row][column].setValid(val);
    }

    /**
     *  Print the board to standard output. 
     *
     *  @param    String    Top or Bottom player
     *
     */
    public void printBoard(String player) {
        int seeds;

        System.out.println("Board before " + player + " moved: ");
        
        for (int i=1; i < HEIGHT; i++) {
            for (int j=0; j < WIDTH; j++) {
                seeds = getSeeds(i,j);
                if ( (seeds > 9) || (seeds == OFFBOARD) )
                    System.out.print(seeds + " | ");
                else
                    System.out.print(" " + seeds + " | ");
            }
            System.out.println();
        }
        System.out.println();
    }
            
    
}