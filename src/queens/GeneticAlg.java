package queens;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlg 
{
	private final int population_size = 100;			//Professor said 100 should be max
	private final int number_of_generations = 10000;	//Testing showed this worked best
	private final double mutation_rate = 0.80;			//Testing showed this worked best
	private int number_of_queens;
	
	public GeneticAlg()
	{
		this.number_of_queens = 25;
	}//end Default Constructor
	
	public GeneticAlg(int nq)
	{
		this.number_of_queens = nq;
	}//end Constructor
	
	//Fills a population with randomly created board states
	private ArrayList<Board> createPopulation()
	{
		ArrayList<Board> population = new ArrayList<Board>(this.population_size);
		for(int i = 0; i < this.population_size; i++)
		{
			population.add(createBoard());
		}
		return population;
	}//end createPopulation
	
	//Creates a randomly generated (and admissible) board state
	private Board createBoard()
	{
		Random rand = new Random();
		int[] layout = new int[this.number_of_queens];
		
		for(int i = 0; i < this.number_of_queens; i++)
		{
			layout[i] = rand.nextInt(this.number_of_queens);
		}
		Board board = new Board(layout, this.number_of_queens);
		return board;
	}//end createBoard
	
	private ArrayList<Board> createNextGeneration(ArrayList<Board> population)
	{
		ArrayList<Board> next_generation = new ArrayList<Board>(population.size());
		HashSet<Integer> already_chosen = new HashSet<Integer>();
		population.sort(new SurvivalSorter());
		///Performs Selection, Reproduce, Crossover, & Mutation
		for(int i = 0; i < population.size(); i++)
		{
			//Select to parents at random (Selection)
			Board parent_one = randomSelection(population, already_chosen);
			Board parent_two = randomSelection(population, already_chosen);
			already_chosen.clear();//Same parents can potentially be used again next iteration
			
			Board child = crossover(parent_one, parent_two);//Perform Crossover
			if(ThreadLocalRandom.current().nextFloat() <= this.mutation_rate)
			{
				child = mutate(child);//Perform mutation
			}//end if	
			//Add child to the new population
			next_generation.add(child);	
		}//end for
		return next_generation;
	}//end createNextGeneration

	//Creates a child from two board states with high probabilities of survival
	private Board crossover(Board parent_one, Board parent_two)
	{
		//This crossover was recommended by the Professor during lecture [1 to n - 3)
		int crossover_point = ThreadLocalRandom.current().nextInt(1, this.number_of_queens - 3);
		int[] new_layout = new int[this.number_of_queens];
		
		for(int i = 0; i < this.number_of_queens; i++)
		{
			if(i < crossover_point)//Portion from first parent
			{
				new_layout[i] = parent_one.getLayout()[i];
			}//end if
			else//Portion from second parent
			{
				new_layout[i] = parent_two.getLayout()[i];
			}//end else
		}//end for
		Board child = new Board(new_layout, this.number_of_queens);
		
		return child;
	}//end crossover
	
	//Randomly reassigns one of the values of the child board state with another random admissible value
	private Board mutate(Board child)
	{
		//Clone the passed in child into a new object mutated_child
		Board mutated_child = new Board(child.getLayout(), this.number_of_queens);
		int mutate_index = ThreadLocalRandom.current().nextInt(1, this.number_of_queens - 2);
		
		//Reassigns a single index from child in range of (1, N - 2) to a random permissible value
		mutated_child.getLayout()[mutate_index] = ThreadLocalRandom.current().nextInt(0, this.number_of_queens);
		//Recreate child with mutated value to recalculate fitness and probability
		mutated_child = new Board(mutated_child.getLayout(), this.number_of_queens);
		
		return mutated_child;
	}//end mutate
	
	/* Takes a population and sorts them by their probabilities of survival.
	 * Then, two nodes are selected at random from the % fittest to be used
	 * as the parents for crossover.*/
	private Board randomSelection(ArrayList<Board> population, HashSet<Integer> already_chosen)
	{
		double percent_of_fittest = 0.2;//In class, professor said 20% worked best
		int selected_index = 0;
		int minimum_selection_index = (int)(population.size() - population.size() * percent_of_fittest) - 1;
		
		//Ensures second parent is not the same as first parent
		do
		{
			selected_index = ThreadLocalRandom.current().nextInt(minimum_selection_index, population.size());
		}
		while(already_chosen.contains(selected_index));
		already_chosen.add(selected_index);//Places chosen parent in HashSet to avoid choosing them again
		
		return population.get(selected_index);
	}//end randomSelection
	
	/* 
	 * This is the driver function for the algorithm.
	 * It returns an object of type Board so that results
	 * and data can be displayed in main regarding the algorithm's
	 * solution and cost.
	 */
	public Board solveGenetic()
	{
		ArrayList<Board> population = createPopulation();
		//Board that yielded the highest fitness (Least attacking queen pairs)
		Board best_board = new Board();
		double population_max_fitness = 0, current_fitness = 0;
		int generation = 1;
		
		while(population_max_fitness < best_board.getMaxFitness() && generation < this.number_of_generations)
		{
			for(int i = 0; i < population.size(); i++)
			{
				current_fitness = population.get(i).getFitness();
				if(current_fitness > population_max_fitness)
				{
					population_max_fitness = current_fitness;
					best_board.setLayout(population.get(i).getLayout());
				}//end if
			}//end for
			
			//This line is commented while testing to keep console output clean
			//System.out.println("Max Fitness for Generation " + generation + ": "+ population_max_fitness);		
			
			//Immediately check condition
			if(population_max_fitness == best_board.getMaxFitness())
			{
				System.out.println("\nSolved in Generation #: " + generation);
				continue;
			}//end if
			population = createNextGeneration(population);		
			generation++;
		}//end while
		
		best_board = new Board(best_board.getLayout(), this.number_of_queens);
		/*
		 * Cost of Genetic Algorithm Solution is the generation # it solved it in
		 * multiplied by the population size of each generation.
		 * For example, at population 100, if the algorithm found a board with no
		 * attacking queen pairs in generation 27, the cost would be 2,700.
		 */
		best_board.setCost(generation * this.population_size);
		
		double solution_fitness = best_board.getFitness();
		//Displays Information Regarding Solution for Genetic Algorithm
		System.out.println("\nGeneration Limit " + generation + " Reached (or solution already found): " 
							+ "\nSolution Board: " + Arrays.toString(best_board.getLayout()));
		System.out.println("Solution's Fitness: " + solution_fitness);
		System.out.println("# Attacking Queen Pairs: " + (best_board.getMaxFitness() - solution_fitness));
		
		return best_board;
	}//end solveGenetic
	
	//Class is used to sort ArrayList by the probability of surival of each board
	private class SurvivalSorter implements Comparator<Board> 
    {
        @Override
        public int compare(Board b1, Board b2)
    	{
    		//Return -1 if first board has less survival chance than second board
            if (b1.getProbability() < b2.getProbability())
            {
                return -1;
            }//end if
            //Return 1 if first board has more survival chance than second board
            else if (b1.getProbability() > b2.getProbability())
            {
                return 1;
            }//end else if
    		//Return 0 when boards have equal survival chance
            else
    		{
    			return 0;
    		}//end else
        }//end compare
    }//end SurvivalSorter
}//end GeneticAlg