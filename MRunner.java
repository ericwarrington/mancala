//Eric Warrington

import java.util.*;

public class MRunner
{
	/**
	 * The main program.
	 *
	 * @param	args	command line arguments
	 */
	public static void main(String[] args)
	{
		final int G=100;
		final int R=1000;
		
		int best=0, best2=0;
		int sum=0;
		ArrayList list=new ArrayList(R);
		ArrayList list2=new ArrayList(R/10);
		
		Mancala content=new Mancala(-1);
		
		for(int c=0; c<R; c++)
		{
			int pd=content.run(G, false);
			int score=G-pd;
			best=Math.max(best, score);
			best2=Math.max(best2, score);
			sum+=score;
			list.add(score);
			
			if(c%10==9)
			{
				list2.add(best2);
				best2=0;
			}
		}
		
		Collections.sort(list);
		Collections.sort(list2);
		
		System.out.println("================================");
		System.out.println("Number of times run: " + R);
		System.out.println("Number of games per run: " + G);
		System.out.println("Best score achieved: " + best);
		System.out.println("Average score: " + (double)sum/R);
		System.out.println("Median score: " + list.get(R/2));
		System.out.println("List of best scores: " + list2);
		System.out.println("List of scores: " + list);
		System.out.println("================================");
	}
}