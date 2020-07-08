package queens;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AnnealingAlg 
{
	private final double schedule = 0.99;
	private int number_of_queens;
	private double temperature;
	
	//Constructors
	public AnnealingAlg()
	{
		this.number_of_queens = 25;
		this.temperature = 100;
	}//end Default Constructor
	
	public AnnealingAlg(int nq, double t)
	{
		this.number_of_queens = nq;
		this.temperature = t;
	}//end Constructor
	
	private Board createBoard()
	{
		Random rand = new Random();
		int[] layout = new int[this.number_of_queens];
		
		for(int i = 0; i < this.number_of_queens; i++)
		{
			layout[i] = rand.nextInt(this.number_of_queens);
		}//end for
		Board board = new Board(layout, this.number_of_queens);
		return board;
	}//end createBoard
	
	/* 
	 * This is the driver function for the algorithm.
	 * It returns an object of type Board so that results
	 * and data can be displayed in main regarding the algorithm's
	 * solution and cost.
	 */
	public Board solveAnnealing()
	{
		double delta = 0;
		int random_index = 0, replacement_value = 0, iterations = 0;
		
		//Initializes a random initial state to be used by Simulated Annealing
		Board current = createBoard();
		
		while(this.temperature > 0 && current.getFitness() != current.getMaxFitness())
		{
			//Apply Schedule to Temperatue (T)
			this.temperature *= this.schedule;
		
			//Creates a successor board from the current board layout
			Board successor = new Board(current.getLayout().clone(), this.number_of_queens);
			
			//Generates succesor by reassigning a random index of current to a new random admissible value
			random_index = ThreadLocalRandom.current().nextInt(0, this.number_of_queens);
			replacement_value = ThreadLocalRandom.current().nextInt(0, this.number_of_queens);
			//Mutates a value from our cloned board state
			successor.getLayout()[random_index] = replacement_value;
			//Updates board "successor" with the mutated value and recalculates fitness
			successor = new Board(successor.getLayout(), this.number_of_queens);
			
			//This line is commented while testing to keep console output clean
			//System.out.println("Fitness:" + successor.getFitness() + " Temperature: " + this.temperature);	
			
			//Calculates the delta in fitness between successor and current
			delta = successor.getFitness() - current.getFitness();
			if(delta > 0)
			{
				current =  successor;
			}//end if
			else if(ThreadLocalRandom.current().nextDouble() <= Math.exp(delta / this.temperature))
			{
				current = successor;
			}//end else	
			iterations++;
		}//end while
		
		/*
		 * Cost of Simulated Annealing Solution is the iteration # it solved it in.
		 * For example, if the algorithm found a board with no attacking queen pairs 
		 * in iteration 27, the cost would be 27.
		 */
		current.setCost(iterations);
		System.out.println("\nSolution Cost: " + iterations);
		System.out.println("Solution Board: " + Arrays.toString(current.getLayout()));
		System.out.println("# Attacking Queen Pairs: " + (current.getMaxFitness() - current.getFitness()));
		
		return current;
	}//end solveAnnealing
}//end AnnealingAlg