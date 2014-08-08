import java.awt.Point;


/**
 * CVRPData.java
 *
 * This class provides most of the information in fruitybun-data.vrp
 * (and what this class does not provide you do not need).
 * This class provides "get methods" to access the demand and distance 
 * information and it does some parameter checking to make sure
 * that nodes passed to the get methods are within the valid range.
 *
 * It does NOT provide direct access to node coordinates and instead only
 * computes the Euclidean distance between them. However, it would be easy
 * to add a get method to access coordinates, although you would have to
 * decide how to return a coordinate: in an array or some kind of coordinate 
 * class.
 *
 * This class is an alternative to reading fruitybun-data.vrp. This class
 * is more convenient if you want to work in java but it encodes
 * the data as arrays so it specifies only the fruitybun CVRP instance.
 * In contrast, code that can read fruitybun-data.vrp should be able to
 * read other .vrp files.
 *
 * @author Tim Kovacs
 * @version 9/9/2011
 * 
 * @author Sreekanth Jujare
 * @version 8/11/2012
 */


class Solution
{
	int[] route;
	double cost;
}

public class CVRPData {

    /** The capacity that all vehicles in fruitybun-data.vrp have. */
    public static final int VEHICLE_CAPACITY = 220;

    /** The number of nodes in the fruitybun CVRP i.e. the depot and the customers */
    public static final int NUM_NODES = 76;

    /** Return the demand for a given node. */
    public static int getDemand(int node) {
	if (!nodeIsValid(node)) {
	    System.err.println("Error: demand for node " + node + 
			       " was requested from getDemand() but only nodes 1.." + NUM_NODES + " exist");
	    System.exit(-1);
	}	    
	return demand[node];
    }
        
    /** Return the Euclidean distance between the two given nodes */
    public static double getDistance(int node1, int node2) {
	if (!nodeIsValid(node1)) {
	    System.err.println("Error: distance for node " + node1 + 
			       " was requested from getDistance() but only nodes 1.." + NUM_NODES + " exist");
	    System.exit(-1);
	}	    
	
	if (!nodeIsValid(node2)) {
	    System.err.println("Error: distance for node " + node2 + 
			       " was requested from getDistance() but only nodes 1.." + NUM_NODES + " exist");
	    System.exit(-1);
	}	    

	int x1 = coords[node1][X_COORDINATE];
	int y1 = coords[node1][Y_COORDINATE];
	int x2 = coords[node2][X_COORDINATE];
	int y2 = coords[node2][Y_COORDINATE];

	// compute Euclidean distance
	return Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));

    // For example, the distance between 33,44 and 44,13 is:
    // 33-44 = -11 and 11^2 = 121
    // 44-13 = 31 and 31^2 = 961
    // 121 + 961 = 1082
    // The square root of 1082 = 32.893768...
    // A simpler example is the distance between nodes 54 and 36, which have coordinates 55,57 and 55,50 and distance 7.0
    }    

    /** Return true if the given node is within the valid range (1..NUM_NODES), false otherwise */
    private static boolean nodeIsValid(int node) {
	if (node < 0 || node > NUM_NODES-1)
	    return false;
	else
	    return true;
    }
    
    private static final int X_COORDINATE = 0; // x-axis coordinate is dimension 0 in coords[][]
    private static final int Y_COORDINATE = 1; // y-axis coordinate is dimension 1 in coords[][]

    // 2-dimensional array with the coordinates of each node in fruitybun-data.vrp. 
    // coords[0] is a dummy entry which is not used. Node 1 is at coords[1], node 2 is at coords[2] and so on.
    // A more Object-Oriented solution would have been to create coordinate objects but that may be overkill.
    private static int [][] coords = new int[][] 
	{
	 {40, 40}, // the coordinates of node 0 (the depot)
	 {22, 22}, // the coordinates of node 1 ...
	 {36, 26},
	 {21, 45},
	 {45, 35},
	 {55, 20},
	 {33, 34},
	 {50, 50},
	 {55, 45},
	 {26, 59}, // node 11
	 {40, 66},
	 {55, 65},
	 {35, 51},
	 {62, 35},
	 {62, 57},
	 {62, 24},
	 {21, 36},
	 {33, 44},
	 {9, 56},
	 {62, 48}, // node 21
	 {66, 14},
	 {44, 13},
	 {26, 13},
	 {11, 28},
	 {7, 43},
	 {17, 64},
	 {41, 46},
	 {55, 34},
	 {35, 16},
	 {52, 26}, // node 31
	 {43, 26},
	 {31, 76},
	 {22, 53},
	 {26, 29},
	 {50, 40},
	 {55, 50},
	 {54, 10},
	 {60, 15},
	 {47, 66},
	 {30, 60}, // node 41
	 {30, 50},
	 {12, 17},
	 {15, 14},
	 {16, 19},
	 {21, 48},
	 {50, 30},
	 {51, 42},
	 {50, 15},
	 {48, 21},
	 {12, 38}, // node 51
	 {15, 56},
	 {29, 39},
	 {54, 38},
	 {55, 57},
	 {67, 41},
	 {10, 70},
	 {6, 25},
	 {65, 27},
	 {40, 60},
	 {70, 64}, // node 61
	 {64, 4},
	 {36, 6},
	 {30, 20},
	 {20, 30},
	 {15, 5},
	 {50, 70},
	 {57, 72},
	 {45, 42},
	 {38, 33},
	 {50, 4},  // node 71
	 {66, 8},
	 {59, 5},
	 {35, 60},
	 {27, 24},
	 {40, 20},
	 {40, 37}};// node 75

    private static int[] demand = new int[]
	{
	 0,  // node 1
	 18, // node 2
	 26, // node 3
	 11, // node 4
	 30, // node 5
	 21, // node 6
	 19, // node 7
	 15, // node 8
	 16, // node 9
	 29, // node 10
	 26, // node 11
	 37, // node 12
	 16, // node 13
	 12, // node 14
	 31, // node 15
	 8,  // node 16
	 19, // node 17
	 20, // node 18
	 13, // node 19
	 15, // node 20
	 22, // node 21
	 28, // node 22
	 12, // node 23
	 6,  // node 24
	 27, // node 25
	 14, // node 26
	 18, // node 27
	 17, // node 28
	 29, // node 29
	 13, // node 30
	 22, // node 31
	 25, // node 32
	 28, // node 33
	 27, // node 34
	 19, // node 35
	 10, // node 36
	 12, // node 37
	 14, // node 38
	 24, // node 39
	 16, // node 40
	 33, // node 41
	 15, // node 42
	 11, // node 43
	 18, // node 44
	 17, // node 45
	 21, // node 46
	 27, // node 47
	 19, // node 48
	 20, // node 49
	 5,  // node 50
	 22, // node 51
	 12, // node 52
	 19, // node 53
	 22, // node 54
	 16, // node 55
	 7,  // node 56
	 26, // node 57
	 14, // node 58
	 21, // node 59
	 24, // node 60
	 13, // node 61
	 15, // node 62
	 18, // node 63
	 11, // node 64
	 28, // node 65
	 9,  // node 66
	 37, // node 67
	 30, // node 68
	 10, // node 69
	 8,  // node 70
	 11, // node 71
	 3,  // node 72
	 1,  // node 73
	 6,  // node 74
	 10, // node 75
	 20};// node 76    

    // Return the absolute co-ordinates of any node
    public static Point getLocation(int node) {
      if (!nodeIsValid(node)) {
        System.err.println("Error: Request for location of non-existent node " + node + ".");
        System.exit(-1);
      }

      return new Point(coords[node][X_COORDINATE], coords[node][Y_COORDINATE]);
    }
    
	private static double[][] distanceMat = new double[76][76];
	private static double[][] tmp = new double[76][76];
	private static int[][] nearestDistMat = new int[76][76];
	private static int[][] solutions = new int[76][76];
	private static int REP_INIT = 2500;
	private static int BONUS = 0;
	private static double COOLING_FACTOR = 0.95;
	private static double INIT_TEMP = 1;
	private static final int twoOptOpNr = 8;
	private static int exploreOp = 4;
	private static boolean PRUNE = false;
	private static int PRUNE1 = 16;
	private static int PRUNE2 = 16;
	private static boolean debug = false;
	
	private static void calDistMat()
	{
		for(int i = 0; i < NUM_NODES; i++)
		{
			for(int j = i; j < NUM_NODES; j++)
			{
				// Diagonal elements
				if (i == j)
				{
					distanceMat[i][j] = 0;
					// copy
					tmp[i][j] = 0;
				}
				else
				{
					distanceMat[i][j] = getDistance(i,j);
					distanceMat[j][i] = distanceMat[i][j];
					// copy
					tmp[i][j] = getDistance(i,j);
					tmp[j][i] = tmp[i][j];					
				}
			}
		}
	}
	
	private static int partition(double realDist[], int points[], int left, int right)
	{
	      int i = left, j = right;
	      double tmp;
	      int tmp1;
	      double pivot = realDist[(left + right) / 2];
	     
	      while (i <= j) {
	            while (realDist[i] < pivot)
	                  i++;
	            while (realDist[j] > pivot)
	                  j--;
	            if (i <= j) {
	                  tmp = realDist[i];
	                  realDist[i] = realDist[j];
	                  realDist[j] = tmp;

	                  tmp1 = points[i];
	                  points[i] = points[j];
	                  points[j] = tmp1;
	                  
	                  i++;
	                  j--;
	            }
	      };
	     
	      return i;
	}
	 
	private static  void quickSort(double realDist[], int points[], int left, int right) {
	      int index = partition(realDist, points, left, right);
	      if (left < index - 1)
	            quickSort(realDist, points, left, index - 1);
	      if (index < right)
	            quickSort(realDist, points, index, right);
	}
	
	private static void calNearDist()
	{	
		for(int i = 0; i < NUM_NODES; i++)
		{
			for (int j = 0; j < NUM_NODES; j++)
			{
				nearestDistMat[i][j] = j;
			}
			quickSort(tmp[i], nearestDistMat[i], 0, 75);
		}
	}
	
	public static void printSolution(Solution sol)
	{
		int capacity = 0;
		int demand = 0;
		
		System.out.printf("login sj12845\n");
		
		System.out.printf("cost %.3f\n", sol.cost);
		
		System.out.printf("1");
		for (int i = 1; i < NUM_NODES; i++)
		{
			demand = getDemand(sol.route[i]);
			if (capacity + demand <= VEHICLE_CAPACITY)
			{
				System.out.printf("->%d", sol.route[i]+1);
				capacity += demand;
			}
			else
			{
				System.out.printf("->1\n");
				System.out.printf("1->%d", sol.route[i]+1);
				capacity = demand;
			}
			
			if (i == NUM_NODES - 1) // Last stop
			{
				System.out.printf("->1");
			}
		}		
	}
	
	public static double fitness(int solution[], boolean display)
	{
		int bestP = PRUNE1;
		double cost;
		double bestCost = 99999.0;
		
		for (int p = PRUNE1; p <= PRUNE2; p++)
		{

			cost = fitnessP(solution, false, p);
			if (debug)
				System.out.printf("\nfitness :: p %d cost %.9f", p, cost);
			if(cost < bestCost)
			{
				bestP = p;
				bestCost = cost;
			}

			if (!PRUNE)
			   break;
			//System.out.printf("\n p %d cost %f", p, cost);
		}
		
		if (display)
			fitnessP(solution, true, bestP);
	
		//System.out.printf("\nbestP %d bestCost %f\n", bestP, bestCost);
		return bestCost;
	}
			
	public static double fitnessP(int solution[], boolean display, int prune)
	{
		int capacity = 0;
		int demand = 0;
		double distance = 0;
		boolean distanceAdded = true;
		int count = 0;
		
		if (display)
		{
			System.out.printf("login sj12845\n");
			System.out.printf("cost %.3f\n", fitnessP(solution, false, prune));
			System.out.printf("1");
		}
		
		distance = distanceMat[0][solution[1]];
		count++;
		

		for (int i = 1; i < NUM_NODES; i++)
		{
			demand = getDemand(solution[i]);
			if ((capacity + demand <= VEHICLE_CAPACITY) && (PRUNE ? (count < prune) : true)) // Prune
			{
				if (display)
					System.out.printf("->%d", solution[i]+1);
				
				if (!distanceAdded)
				{
					distance += distanceMat[solution[i-1]][solution[i]];
					count++;

				}
				else
					distanceAdded = false;
				capacity += demand;
			}
			else
			{
				if (display)
				{	
					System.out.printf("->1\n");
					System.out.printf("1->%d", solution[i]+1);
				}
				capacity = demand;

				distance += distanceMat[solution[i-1]][0];

				distance += distanceMat[0][solution[i]];
				count = 1;
				

				distanceAdded = false;
			}
			
			if (i == NUM_NODES - 1) // Last stop
			{
				distance += distanceMat[solution[i]][0];
				if (display)
					System.out.printf("->1");
			}
		}
		
		return distance;
	}
			
	private static int checkSol(int tmp[])
	{
		for (int t =1; t < NUM_NODES; t++)
		{
			boolean found = false;
			for (int v =1; v < NUM_NODES; v++)
			{
				if (tmp[v] == t)
				{
					found = true;
				}
			}
			if (!found)
				return t;
		}
		
		return 0;
	}
	
	private static void applyOperators(int tmp[], int neighbour[][], int i, int index, boolean explore)
	{
		int j;
		int k;
		
		/*
		 * A : [1,i] , B : [i+1,NUM_NODES-1]
		 */
		
		/*
		 * OP 1: F(A)+B
		 */
		for (k = 1, j = i; (k <= i); k++, j--)
		{
			neighbour[index][k] = tmp[j];
		}
		for (k = i+1; (k <= NUM_NODES-1); k++)
		{
			neighbour[index][k] = tmp[k];
		}
		index++;
		
		/*
		 * OP 2: A+F(B)
		 */
		for (k = 1; (k <= i) ; k++)
		{
			neighbour[index][k] = tmp[k];
		}
		for (k = i+1, j = NUM_NODES-1; (k <= NUM_NODES-1); k++, j--)
		{
			neighbour[index][k] = tmp[j];
		}
		index++;
	
		/*
		 * OP 3: F(A)+F(B)
		 */
		for (k = 1, j = i; (k <= i); k++, j--)
		{
			neighbour[index][k] = tmp[j];
		}
		for (k = i+1, j = NUM_NODES-1; (k <= NUM_NODES-1); k++, j--)
		{
			neighbour[index][k] = tmp[j];
		}
		index++;
		
		/*
		 * OP 4: B+A
		 */
		for (k = 1, j = i+1; (k <= NUM_NODES-1-i) ; k++, j++)
		{
			neighbour[index][k] = tmp[j];
		}
		for (k = NUM_NODES-i, j = 1; (k <= NUM_NODES-1) ; k++, j++)
		{
			neighbour[index][k] = tmp[j];
		}
		index++;
	
		/*
		 * OP 5: B+F(A)
		 */
		for (k = 1, j = i+1; (k <= NUM_NODES-1-i) ; k++, j++)
		{
			neighbour[index][k] = tmp[j];
		}
		for (k = NUM_NODES-i, j = i; (k <= NUM_NODES-1) ; k++, j--)
		{
			neighbour[index][k] = tmp[j];
		}
		index++;
		
		/*
		 * OP 6: F(B)+F(A)
		 */
		for (k = 1, j = NUM_NODES-1; (k <= NUM_NODES-1-i) ; k++, j--)
		{
			neighbour[index][k] = tmp[j];
		}
		for (k = NUM_NODES-i, j = i; (k <= NUM_NODES-1) ; k++, j--)
		{
			neighbour[index][k] = tmp[j];
		}
		index++;
		
		/*
		 * OP 7: F(B)+A
		 */	
		for (k = 1, j = NUM_NODES-1; (k <= NUM_NODES-1-i) ; k++, j--)
		{
			neighbour[index][k] = tmp[j];
		}
		for (k = NUM_NODES-i, j = 1; (k <= NUM_NODES-1) ; k++, j++)
		{
			neighbour[index][k] = tmp[j];
		}
		index++;
	
		/*
		 * OP 8: Swap positions
		 */
		for (k = 1; k <= NUM_NODES-1 ; k++)
		{
			if (k == i)
			{
				neighbour[index][k] = tmp[k+1];
				k++;
				neighbour[index][k] = tmp[k-1];
			}
			else
				neighbour[index][k] = tmp[k];
		}
		
		if (explore)
		{
			/*
			 * Create extra random solutions with swap operator
			 */
			int rnd = i+1;
			int tempVal;
			//int rndUsed[] = new int[NUM_NODES];
			// TODO: Add more solutions here up to 75/2 = 37
			for (j = index+1; j < exploreOp+twoOptOpNr ; j++)
			{
				for (k = 1; k <= NUM_NODES-1 ; k++)
				{
					neighbour[j][k] = tmp[k];
				}
				
				/*do
				{
					rnd = (int)(Math.random()*(double)(NUM_NODES-1));
				} while((rnd == i) || (rnd == i+1) || rnd < 1 || rndUsed[j] != 0);
				*/
				rnd = (rnd+1)%NUM_NODES;
				if (rnd == 0)
					rnd = 1;
				// Swap now!
				tempVal = neighbour[j][i];
				neighbour[j][i] = neighbour[j][rnd];
				neighbour[j][rnd] = tempVal;
				//rndUsed[j] = 1;
			}
		}
	}
	
	private static Solution SAHillClimber2Opt(Solution sol, double temp, boolean explore) throws InterruptedException
	{
		int solSize = twoOptOpNr;
		int[][] neighbour;
        double tmpValue;
        double currTemp = temp;
        Solution tmpSol = new Solution();
        Solution minSol = new Solution();
        
        if (explore)
        {
        	// Generate extra samples
        	solSize += exploreOp;
        }
        
        neighbour = new int[solSize][NUM_NODES];
        
		tmpSol.route = sol.route.clone();
		tmpSol.cost = sol.cost;
		
		// Prepare a copy!
		minSol.route = sol.route.clone();
		minSol.cost = sol.cost;
		
		do 
		{
			int i = ((explore ? (int)(Math.random()*(double)(5)) : 1));
			
			if (i == 0)
				i = 1;
			
			for (i = i; i < NUM_NODES-1; i++)
			{
				applyOperators(tmpSol.route, neighbour, i, 0, explore);
						
				if (debug)
				for (int t =0; t < solSize; t++)
				{
					int missing = 0;
					if (debug)
					if ((missing = checkSol(neighbour[t])) != 0)
					{
						System.err.printf("\nBUG!!!!!!!!!!!!!! missing %d  opNr %d i %d \n ############### \n orig", missing, t, i);
						for (int v =1; v < NUM_NODES; v++)
						{
							System.err.printf("%d ", tmpSol.route[v]);
						}
						System.err.println("\n after operator:");
						for (int v =1; v < NUM_NODES; v++)
						{
							System.err.printf("%d ", neighbour[t][v]);
						}
						
						Thread.sleep(20000);
					}
				}

				int j;			
				for (j = (explore ? (int)(Math.random()*(double)(5)) : 0); j < solSize; j++)
				{	
					tmpValue = fitness(neighbour[j], false);
					if (debug)
					System.out.printf("\ni %d j %d tmpValue %.9f minSol.cost %.9f currTemp %.9f Math.exp(-(tmpValue-minSol.cost)/currTemp) %.9f ##",i, j, tmpValue, minSol.cost, currTemp, Math.exp(-(tmpValue-minSol.cost)/currTemp));

					if (tmpValue < minSol.cost)
					{
						if (debug)
						System.out.printf("  (1)  ");
						minSol.cost = tmpValue;
						minSol.route = neighbour[j].clone();
						
						tmpSol.route = minSol.route.clone();
						tmpSol.cost = minSol.cost;
						break;
					}			
					else if (tmpValue == minSol.cost)
					{
						if (debug)
						System.out.printf("  (2)  ");
						// dummy
					}
					else if (Math.random() < Math.exp(-(tmpValue-minSol.cost)/currTemp)) //Boltzman distribution
					{
						if (debug)
						System.out.printf("  (3)  ");
						tmpSol.route = neighbour[j].clone();
						tmpSol.cost = tmpValue;
						currTemp *=COOLING_FACTOR;
						break;
					}
					if (debug)
					Thread.sleep(200);
				}

				//System.out.printf("tmpSol.cost %f currTemp %f", tmpSol.cost, currTemp);
				if (j != solSize)
				{
					// new solution found, start with i = 1
					i = ((explore ? (int)(Math.random()*(double)(5)) : 0));
					continue;
				}
			}
			
			if (i == NUM_NODES-1)
			{
				// Search exhausted
				break;
			}
			
		} while(true);	
		
		return minSol;
	}
	
	
    public static void main(String[] args) throws InterruptedException
    {		
    	Solution newSol = new Solution();
		Solution tmp;
		int timeInMins;
		boolean exit = false;
		
		newSol.cost = 99999.0;
		
		timeInMins = Integer.parseInt("55");// TODO : change to args[0]
		if (timeInMins < 1 || timeInMins > 58)
			timeInMins = 58; // default
		
    	/*
    	 * Record Runtime
    	 */
    	long startTime = System.nanoTime();
    	long expRunTime = startTime + (long)timeInMins*(long)60*(long)1000000000-(long)5*(long)1000000000; //Grace period : 5 seconds
    	
    	/*
    	 * Prepare distance matrix
    	 */
    	calDistMat();

		/*
		 * Prepare nearest distance matrix
		 */
		calNearDist();
				
		/*
		 * Prepare solution
		 */
		for(int i = 0; i < NUM_NODES; i++)
		{
			solutions[i][0] = i; 
			for(int j = 1; j < NUM_NODES; j++)
			{
				int index = 1;
				int elem;
				while (true)
				{
					int k;
					elem = nearestDistMat[solutions[i][j-1]][index];
					
					// if the point is depot, ignore!!
					if (elem == 0) 
					{
						index++;
						continue;
					}
					
					for (k = 1; k < j; k++)
					{ 
						if (elem == solutions[i][k])
						{
							index++;
							break;
						}
					}
					
					if (k == j)
					{
						solutions[i][j] = elem;
						break;
					}
				} 	
			}
		}
		
		/* For 1 min,
		 * No bonus here !!
		 * And I'm not in a mood for wide exploration ;)
		 */
		REP_INIT = 2500;
		BONUS = 0;
		INIT_TEMP = 1;
		exploreOp = 36;
		for(int i = 0; i < NUM_NODES; i++)
		{
			/* I need a quick result :)
			 * Choosing optimistic solutions
			 */
			if (i==0 || i==3 || i==31)
			{
				System.out.printf("\n i %d\n", i);
				
				int rep = REP_INIT;
				Solution sol = new Solution();
				sol.route = solutions[i].clone();
				sol.cost = fitness(solutions[i], false);
				
				do
				{
					tmp = SAHillClimber2Opt(sol, INIT_TEMP, false);
				
					if (tmp.cost < newSol.cost)
					{
						newSol = tmp;
						System.out.printf("\n Main : New cost : %f\n", newSol.cost);
						// bonus
						rep += BONUS;
					}
				} while(rep-- > 0 && System.nanoTime() < expRunTime);
				
				if (System.nanoTime() > expRunTime) 
				{
					exit = true;
					break;
				}
			}
		}
		
		if (System.nanoTime() + (long)15*(long)1000000000 > expRunTime)
		{
			/*
			 * Less than 20 seconds, time to quit !!
			 */
			exit = true;
		}

		REP_INIT = 700;
		BONUS = 1000;
		INIT_TEMP = 10; 
		exploreOp = 32;
		PRUNE = false;
		PRUNE2 = 16;
		PRUNE1 = 11;
		COOLING_FACTOR = 0.95;
		debug = false;
		if (!exit)
		{
			double temp = 1;
			do
			{
				int reps = REP_INIT;
				do
				{
					tmp = SAHillClimber2Opt(newSol, temp, true);
					if (tmp.cost < newSol.cost)
					{
						newSol = tmp;
						System.out.printf("\nMain 2: New cost : %f Time %d secs \n", newSol.cost, (System.nanoTime()-startTime)/1000000000);
						reps+= REP_INIT;
					}
					reps--;
				} while(reps > 0);
				
				temp++;
				if (temp == 4*INIT_TEMP)
					temp = 1;
				System.out.printf("temp %f\n", temp);
			} while(System.nanoTime() < expRunTime);
		}
		
				
		/*
		 * Print Solution
		 */
		fitness(newSol.route, true);

		
    	long endTime = System.nanoTime();
    	long secs = (endTime - startTime) / 1000000000;
    	long mins = secs / 60;
    	secs = secs % 60 ;
    	System.out.println("\nTime taken : "+mins+ " mins "+secs+" secs"); 
    }
}

