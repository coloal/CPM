import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author davcem
 * @version 1.0
 * @since 2015-06-12
 * 
 * Represents an implementation of the Cellular Potts Model on a regular lattice, as defined by Glazier and Garner (1992)
 * 
 * <p>
 * The CPM is defined on a regular, square or hexagonal lattice, with a spin σ(x) ∈ Z+,0 defined on each lattice site x. * 
 * σ(x) ∈ Z+,0 --> Spin = sigma = cells (cell identification number)
 * σ=0 --> medium or the extracellular matrix (ecm)
 * 
 * Biological cells are represented as domains on the lattice with identical spin σ(x), where σ ∈ N can be seen as a cell identification number
 * Each cell and the medium is additionally marked with a label τ(σ) ∈ Z+, 0 to identify a biological cell type.
 * 
 * Important: The cell types are
 * 	- medium (ECM = extracellular matrix)
 * 	- light (cell-type modulo 2 == 0 == even)
 * 	- dark (cell-type modulo 2 == 1 == odd)
 */
public class CPMLatticeMio{

	/**CPM calculations */
	/**  Energy interaction for the ecm. */
    private double jEcm;

    /**  Energy interaction for even cells. */
    private double jLightCells;

    /**  Energy interaction for other cells. */
    private double jDarkCells;

    /**  Energy interaction for even with odd cells. */
    private double jDifferentCells;

    /** The lambda for area calculation. */
    private double lambdaArea;

    /**  The factor for the targetArea of light cells. */
    private double targetAreaFactorLight;

    /**  The factor for the targetArea of dark cells. */
    private double targetAreaFactorDark;

	/** relative proportion of dark and light cells */
	private int darkCellDecrease;

	/** The beta or temperature within the lattice */
	private double temperature;

	/** relative proportion of dark and light cells */
	private int ratioDarkToLightCells;
	/** end of CPMcalculations*/

	/** CPMLattice*/
	
	/** Number of rows of lattice */
	private int xMax;
	
	/** Number of columns of lattice */
	private int yMax;
	
	/** Number of monte-carlo-steps. */
	private int mcs;
	
	/** The amount of sub-steps per monte-carlo-step*/
	private int mcSubsteps;

	/** Maximum value of sigma (cells) including sigma=0=ECM */
	private int sigmaMax;
	
	/** The initial density for ECM (celltype == 0) to other cells (cellType != 0)*/
	private double initialMatrixDensity;
	
	/** Sigma Array which holds lattice sites */
	public int[][] sigma;
	
	/** Area array which holds the areas of different cells. */
	private int[] area;


	/**
	 * Instantiates a new CPM lattice.
	 *
	 * @param newXMax the x dimension of the lattice
	 * @param newYMax the y dimension of the lattice
	 * @param newMcs the amount of monte-carlo-steps
	 * @param newMcSubsteps the number of CPM substeps
	 * @param newSigmaMax the number of different cells
	 * @param newInitialMatrixDensity the density of the initial lattice's filling
	 * @param params the other adjustable params
	 */
	public CPMLatticeMio ( int newXMax, int newYMax, int newMcs, int newMcSubsteps, int newSigmaMax,double newInitialMatrixDensity,
						/*CPMcalculations*/
						double jEcm, double jLightCells,double jDarkCells, double jOtherCells, double lambdaArea,
						double targetAreaFactorLight, double targetAreaFactorDark, int darkCellDecrease, double temperature,
						int ratioDarkToLightCells)
	{

		xMax = newXMax;
		yMax = newYMax;
		mcs = newMcs;
		mcSubsteps = newMcSubsteps;

		/*+1 because ECM is not a cell*/
		sigmaMax = newSigmaMax + 1;
		
		initialMatrixDensity = newInitialMatrixDensity;
		
		sigma = new int[xMax][yMax];
		area = new int[sigmaMax];		

		/*CPMcalculations*/
		this.jEcm = jEcm;
		this.jLightCells = jLightCells;
		this.jDarkCells = jDarkCells;
		this.jDifferentCells = jOtherCells;
		this.lambdaArea = lambdaArea;
		this.targetAreaFactorLight = targetAreaFactorLight;
		this.targetAreaFactorDark = targetAreaFactorDark;
		this.darkCellDecrease = darkCellDecrease;
		this.temperature = temperature;
		this.ratioDarkToLightCells = ratioDarkToLightCells;
	}


	/**
	 * Returns a random number within the given range (between min and max (max = inclusive)
	 *
	 * @param min the minimum range
	 * @param max the maximum range
	 * @return the random number within range
	 */
	private int getRandom(int min, int max){
		
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

	/**
	 * Return a random even number within the given range.
	 * See also getRandom()
	 *
	 * @param min the minimum range
	 * @param max the maximum range
     * @return the random even number within range
     */
	private int getRandomEven(int min, int max){

		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		if(randomNum % 2 == 1) { // check if odd
			if(randomNum >= max) {
				randomNum = randomNum - 1;
			}
			else {
				randomNum = randomNum +1;
			}
		}
		return randomNum;
	}

	/**
	 * Return a random odd number within the given range.
	 * See also getRandom()
	 *
	 * @param min the minimum range
	 * @param max the maximum range
	 * @return the random odd number within range
	 */
	private int getRandomOdd(int min, int max){

		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		if(randomNum % 2 == 0) { // check if even
			if(randomNum <= max) {
				randomNum = randomNum - 1;
			}
			else {
				randomNum = randomNum +1;
			}
		}
		return randomNum;
	}
	
	/**
	 * Procedure is used to initialize the lattice.
	 * <p>
	 * @see #initialMatrixDensity is the density of cells to ecm in the lattice.
	 * @see #sigmaMax defines the number of cells to be randomly populated in lattice.
	 */
	public void initializeLattice() {
		
		int i, j = 0;
		
		//at the beginning the area for ECM is xMax * yMax (because all lattice sites are of cell 0 (=ECM)
		area[0] = xMax * yMax;
		
		int sumCellArea = 0;
		int counter = 1;
		int ratioDarkLight = 1;
		ratioDarkLight = ratioDarkToLightCells;
		
		//as long as the sum of cell areas of "normal cells" (!= ECM) is lower than
		//the area of the lattice * initialMatrixDensity create new random lattice sites != ECM
		while(sumCellArea < xMax * yMax * initialMatrixDensity){
			
			i=getRandom(0, xMax-1);
			j=getRandom(0, yMax-1);
			
			//if we override a cell, we have to update the area of this cell
			area[sigma[i][j]]--;
			
			//if we override an already filled cell, we have to reduce the sumCellArea
			if(sigma[i][j] > 0){
				
				sumCellArea--;
			
			}

			// adjust relative proportion between light and dark cells by a specified ratio
			if (counter % ratioDarkLight == 0) {
				sigma[i][j] = getRandomOdd(1, sigmaMax-1);
			}
			else {
				sigma[i][j] = getRandomEven(1, sigmaMax - 1);
			}

			
			//for the new cell we have to update the area also
			area[sigma[i][j]]++;
			sumCellArea++;
			counter++;
			
		}
		
	}
	
	/**
	 * Computes the CPM according to the parameters of the lattice.
	 */
	public void computeCPM() {

		/*Variables for randomness according to neighbors*/
		int randomX, randomY;
		
		/*The cell within mcs step and one possible neighbor*/
		int cell, cellNeighbour;

		int neighbourX, neighbourY;
		
		double energyAdhesion, newEnergyAdhesion, energyArea, newEnergyArea, deltaH, prob;
		
		prob = 0.0;
		
		/*Main loop over the number of monte-carlo-steps*/
		for(int step=1; step <= mcs; step++){
			
			for(int substeps = 1; substeps <= mcSubsteps; substeps++){
				
				/*Generate random numbers for random lattice site.*/

				randomX = ThreadLocalRandom.current().nextInt(0, xMax);
				randomY = ThreadLocalRandom.current().nextInt(0, yMax);
				cell = sigma[randomX][randomY];
				
				/*Looking for possible neighbors: Define the ranges for neighbors*/
				int rangXMin = (randomX - 1 < 0) ? randomX : randomX-1;
				int rangYMin = (randomY - 1 < 0) ? randomY : randomY-1;
				int rangXMax = (randomX + 1 >= xMax) ? randomX : randomX+1;
				int rangYMax = (randomY + 1 >= yMax) ? randomY : randomY+1;

				

				/*Neighbor must be different from actual cell so getRandoms until different*/
				do{
					
					neighbourX = ThreadLocalRandom.current().nextInt(rangXMin, rangXMax+1);
					neighbourY = ThreadLocalRandom.current().nextInt(rangYMin, rangYMax+1);
					
				}while(randomX == neighbourX && randomY == neighbourY);
				
				cellNeighbour = sigma[neighbourX][neighbourY];
	
				energyAdhesion = 0;
				newEnergyAdhesion = 0;

				/*
				 * Within this loop we calculate the adhesion differences for
				 * the cells: We look at the neighbors of cell to calculate the energy
				 * values for this configuration
				 * than we look at the energy configuration of the neighbors of 
				 * cellNeighbour (as if the copy would be successful)
				 */
				for (int i = rangXMin; i <= rangXMax; i++) {

					for (int j = rangYMin; j <= rangYMax; j++) {

						/* make sure we are not on actual cell */
						if (!(i == randomX && j == randomY)) {
							
							/*Only calculate if we have different cells --> Kronecker Delta (1-1 = 0 --> multiplication with 0)
							 * Kronecker delta, δ(σ, σ ) = 1 if σ = σ and δ(σ, σ) = 0 if σ != σ , ensures that only the 
							 * surface sites between different cells contribute to the adhesion energy*/
							if(cell != sigma[i][j]){
								
								
								if(cell == 0 || sigma[i][j] == 0){//ECM
									
									energyAdhesion += jEcm;
								
								}/*else if(cell == sigma[i][j]){//Same cells, which cells?
									
									energyAdhesion += (cell % 2 == 0) ? jLightCells : jDarkCells;
								
								}*/else{//different cells
									
									energyAdhesion += jDifferentCells;
								}
								
								//energyAdhesion += getEnergyAdhesionForCells(cell, sigma[i][j]);
									
							}
							/*Only calculate if we have different cell types --> Kronecker Delta (1-1 = 0 --> multiplication with 0)*/
							if(cellNeighbour != sigma[i][j]){//cells are identical so don't calculate
								
								
								if(cellNeighbour == 0 || sigma[i][j] == 0){//ECM
									
									newEnergyAdhesion += jEcm;
								
								}/*else if(cellNeighbour == sigma[i][j]){//Same cellNeighbours, which cellNeighbours? 
									
									newEnergyAdhesion += (cellNeighbour % 2 == 0) ? jLightCells : jDarkCells;
								
								}*/else{//different cells
									
									newEnergyAdhesion += jDifferentCells;
								}
								
								//newEnergyAdhesion += getEnergyAdhesionForCells(cellNeighbour, sigma[i][j]);
							
							}
						}		
					}
				}

				energyArea = 0;
				newEnergyArea = 0;

				//if cell is of type ECM then the Area calculation is suppressed
				if (cell > 0) {
					double targetAreaFactor = 0.0;

					targetAreaFactor =  (cell % 2 == 0) ? targetAreaFactorLight : targetAreaFactorDark;

					energyArea = lambdaArea *(Math.pow((area[cell] - (xMax * yMax * targetAreaFactor) ), 2));
				}

				//if cell neighbor is of type ECM then the Area calculation is suppressed
				if (cellNeighbour > 0) {

					double targetAreaFactor = 0.0;

					targetAreaFactor =  (cellNeighbour % 2 == 0) ? targetAreaFactorLight : targetAreaFactorDark;

					newEnergyArea = lambdaArea * (Math.pow((area[cellNeighbour] - (xMax * yMax * targetAreaFactor) ),2));

				}

				/* The overall deltaH = AFTER-BEFORE */
				deltaH = (newEnergyAdhesion + newEnergyArea) - (energyAdhesion + energyArea);

				// spin-copy for a temperature > 0 is accepted
				//  with prob = 1.0 if it would decrease the value of globally Hamiltonian
				//  or with Boltzmann probability if it would increase the value of Hamiltonian
				if (temperature > 0) {
					
					if (deltaH > 0) {

						prob = Math.exp(-deltaH / temperature); // Boltzmann

					} else if (deltaH <= 0) {

						prob = 1.0;

					}
					
				} else if (temperature == 0) {
					
					if (deltaH > 0) {

						prob = 0;

					} else if (deltaH == 0) {

						prob = 0.5;

					} else if (deltaH < 0) {

						prob = 1.0;

					}
				}

				//only copy if probability is high enough, and if cellNeighbour to copy is not an 
				//lattice site of type ECM (ECM should not grow)
				if (prob >= ThreadLocalRandom.current().nextDouble() ) {

					if(cellNeighbour > 0){
						if ((cell % 2 == 1) && (darkCellDecrease == 1)) {
							/* do not copy if dark cells are configured to not decrease in mass */
						}
						else {
							/* actual copy */
							sigma[randomX][randomY] = cellNeighbour;
							area[cell]--;
							area[cellNeighbour]++;
						}
					}
				} 
			}
		}
	}
		
	/**
	 * Calculates the energyAdhesion for the given cells.
	 * 
	 * @param cell1 - The cell
	 * @param cell2 - The neighbor of cell
	 * 
	 * @return the energyAdhesion between given cells
	 */
	public double getEnergyAdhesionForCells(int cell1, int cell2){
	
		if(cell1 == 0 || cell2 == 0){//ECM
			
			return jEcm;
		
		}else if(cell1 == cell2){//Same cells, which cells?
			
			return (cell1 % 2 == 0) ? jLightCells : jDarkCells;
		
		}else{//different cells
			
			return jDifferentCells;
		}
	}
	

	


	/**
	 * Function returns the targetArea for given cell
	 * 
	 * @param int cell - the cell to obtain the targetArea for
	 * 
	 * @return targetArea - the targetArea for cell (depends if cell is light or dark cell)
	 * */
	public double getTargetAreaForCell(int cell){
		
		double targetAreaFactor = 0.0;
		
		targetAreaFactor =  (cell % 2 == 0) ? targetAreaFactorLight 
				: targetAreaFactorDark;
		
		return xMax * yMax * targetAreaFactor;
		
	}
	
	/**
	 * @return the xMax
	 */
	public int getXMax() {
		return xMax;
	}

	/**
	 * @return the yMax
	 */
	public int getYMax() {
		return yMax;
	}
	
	/**
	 * Gets the mcsMax.
	 *
	 * @return the mcsMax
	 */
	public int getMcs() {
		return mcs;
	}

	/**
	 * @return the mcsSteps
	 */
	public int getMcSubsteps() {
		return mcSubsteps;
	}
	
	/**
	 * Gets the sigmaMax.
	 *
	 * @return the sigmaMax
	 */
	public int getSigmaMax() {
		return sigmaMax;
	}

	/**
	 * Gets the initialMatrixDensity.
	 * 
	 * @return the initialMatrixDensity
	 */
	public double getInitialMatrixDensity() {
		return initialMatrixDensity;
	}

	/**
	 * Gets the sigma.
	 *
	 * @return the sigma
	 */
	public int[][] getSigma() {
		return sigma;
	}

	/**
	 * Gets the area.
	 *
	 * @return the area
	 */
	public int[] getArea() {
		return area;
	}
	
	public static void main(String[] args) {
		CPMLatticeMio lattice = new CPMLatticeMio(50,50,50,50,2,0.9 
											/*CPMcalculations*/,20,15,2,10,0.05,0.25,0.25,5/*en que se diferencia esto del ratioDarkToLightCells*/,20,5);
		lattice.initializeLattice();
		lattice.computeCPM();
		int[][] lat = lattice.getSigma();

		for(int i=0 ; i < lattice.getXMax(); ++i)
		{
			for(int j=0 ; j < lattice.getYMax(); ++j)
				System.out.print(lat[i][j]+" ");
			System.out.println("\n");
		}
	}
}