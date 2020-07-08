package queens;

public class Main 
{
	public static void main(String[] args)
	{
		double start = 0, end = 0;
		System.out.println("Welcome to the N-Queens Problem Solver!\n");
		
		
		double number_solved = 0.00, number_of_tests = 500.00, average_cost = 0, average_runtime = 0;
		
		/*
		System.out.println("Solving N-Queens using Genetic Algorithm:\n");
		for(int i = 0; i < number_of_tests; i++)
		{
			GeneticAlg test = new GeneticAlg();
			start = System.nanoTime();
			Board temp = test.solveGenetic();//No argument makes N = 25 Queens
			end = System.nanoTime();
			System.out.println("Time Elapsed (seconds): " + ((end - start) * 1e-9));
			
			if(temp.getFitness() == temp.getMaxFitness())
			{
				number_solved++;
				average_cost += temp.getCost();
				average_runtime += end - start;	
			}//end if
		}//end for
		System.out.println("\n\nTest Data for Genetic Algorithm Gathered Sucessfully.\n");
		System.out.println("Average Cost: " + (average_cost / number_of_tests));
		System.out.println("Average Runtime (seconds): " + ((average_runtime / number_of_tests) * 1e-9));
		System.out.println("Percentage Solved: " + ((number_solved /number_of_tests) * 100));
		*/
		
		//Resets Averages to 0 before calculating the averages for Simulated Annealing
		number_solved = 0.00; average_cost = 0; average_runtime = 0;
		
		System.out.println("\nSolving N-Queens using Simulated Annealing Algorithm:\n");
		for(int i = 0; i < number_of_tests; i++)
		{
			AnnealingAlg test = new AnnealingAlg();
			start = System.nanoTime();
			Board temp = test.solveAnnealing();//No argument makes N = 25 Queens
			end = System.nanoTime();
			System.out.println("Time Elapsed (seconds): " + ((end - start) * 1e-9));
			
			if(temp.getFitness() == temp.getMaxFitness())
			{
				number_solved++;
				average_cost += temp.getCost();
				average_runtime += end - start;	
			}//end if
		}//end for
		System.out.println("\n\nTest Data for Simulated Annealing Gathered Sucessfully.\n");
		System.out.println("Average Cost: " + (average_cost / number_of_tests));
		System.out.println("Average Runtime (seconds): " + (average_runtime / number_of_tests) * 1e-9);
		System.out.println("Percentage Solved: " + ((number_solved /number_of_tests) * 100));
		
	}//end main
}//end Main