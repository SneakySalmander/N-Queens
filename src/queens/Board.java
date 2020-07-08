package queens;

public class Board
{
	private double probability;
	private int number_of_queens;
	private int cost;
	private double maximum_fitness;
	private double fitness;
	private int[] layout;
	
	public Board()
	{
		/*
		 * Variables must be instantiated in this order.
		 * determineFitness() needs determineMaxFitness()
		 * determineProbability() needs determineFitness()
		 */
		this.number_of_queens = 25;
		this.setLayout(new int[this.number_of_queens]);
		determineMaxFitness();
		determineFitness();
		determineProbability();
	}//end Default Constructor
	
	public Board(int[] l, int nq)
	{
		/*
		 * Variables must be instantiated in this order.
		 * determineFitness() needs determineMaxFitness()
		 * determineProbability() needs determineFitness()
		 */
		this.number_of_queens = nq;
		this.setLayout(l);
		determineMaxFitness();
		determineFitness();
		determineProbability();
	}//end Constructor
	
	public void setLayout(int[] layout) 
	{
		this.layout = layout;
	}//end setLayout
	
	public int[] getLayout() 
	{
		return layout;
	}//end getLayout
	
	public void determineFitness()
	{
		int[] row_frequency = new int[this.number_of_queens];
		int[] main_diagonal_frequency = new int[(this.number_of_queens * 2)];
		int[] secondary_diagonal_frequency = new int[(this.number_of_queens * 2)];
		int horizontal_collisions = 0, main_diagonal_collisions = 0, secondary_diagonal_collisions = 0, row = 0;

		/*Use frequency of queens to sum up collisions (N * (N-1) / 2).
		 * This works because all queens on one row forms a complete graph.
		 * This means every queen is connected to every other queen.
		 * Therefore, the number of edges is the number of collisions.*/
		for (int i = 0; i < this.number_of_queens; i++)
		{
			row = this.layout[i];
			row_frequency[row]++;
			main_diagonal_frequency[row + i]++;
			secondary_diagonal_frequency[this.number_of_queens - row + i]++;
		}//end for
		for (int i = 0; i < this.number_of_queens * 2; i++)
		{
		    int queens_in_row = 0, queens_in_main_diag = 0, queens_in_sec_diag = 0;
		    if (i < this.number_of_queens)
		    {
		    	queens_in_row = row_frequency[i];  					//# of queens found in row
		    }//end if
		    queens_in_main_diag = main_diagonal_frequency[i];   	//# of queens found in main diagonal
		    queens_in_sec_diag = secondary_diagonal_frequency[i];	//# of queens found in secondary diagonal
		    horizontal_collisions += (queens_in_row * (queens_in_row - 1)) / 2;
		    main_diagonal_collisions += (queens_in_main_diag * (queens_in_main_diag - 1)) / 2;
		    secondary_diagonal_collisions += (queens_in_sec_diag * (queens_in_sec_diag - 1)) / 2;
		}//end for
		this.fitness  = this.maximum_fitness - (horizontal_collisions + main_diagonal_collisions + secondary_diagonal_collisions);
	}//end determineFitness
	
	public double getFitness()
	{
		return this.fitness;
	}//end getFitness
	
	public void determineMaxFitness()
	{
		this.maximum_fitness =  (this.number_of_queens * (this.number_of_queens - 1)) / 2;
	}//end determineMaxFitness
	
	public double getMaxFitness()
	{
		return this.maximum_fitness;
	}//end getMaxFitness
	
	//Function determines probability of survival of board
	public void determineProbability()
	{
		this.probability = this.getFitness() / this.getMaxFitness();
	}//end determineProbability
	
	public double getProbability()
	{
		return this.probability;
	}//end getProbability
	
	/* 
	 * Sets cost of N-Queens Algorithm (Varies by Method)
	 * E.g. Annealing Algorithm vs. Genetic Algorithm 
	 * 
	 * @Genetic Algorithm cost = population_size * number_of_generations
	 * @Annealing Algorithm cost = number_of_iterations
	 */
	public void setCost(int c)
	{
		this.cost = c;
	}//end setCost
	
	public int getCost()
	{
		return this.cost;
	}//end cost
}//end Board
