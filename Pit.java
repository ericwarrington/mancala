/**
 * Pit.java
 *
 * Version:
 *     $Id:$
 *
 * Revisions:
 *     $Log:$
 *
 */

import java.util.*;

/**
 *
 * This class represents a pit in the game of Mancala.
 *
 * @author Roxanne Canosa
 */

class Pit {

    private String color;     // The color of pit for display
    private int seeds;        // The number of seeds in the pit
    private boolean valid;    // True if the pit is valid (you define)
   
    public Pit (String c, int s) {
	color = c;
	seeds = s;
        valid = true;
    }

    public void setColor(String c) {
        color = c;
    }

    public String getColor() {
        return color;
    }

    public void setSeeds(int s) {
        seeds = s;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setValid(boolean val) {
        valid = val;
    }

    public boolean isValid() {
        return valid;
    }
}