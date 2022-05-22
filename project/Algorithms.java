/*
 * Algorithms : A Java implementation of optimization algorithms: Blind Search,
 *            Iterated Local Search, Differential Evolution, Particle Swarm
 *            Optimization.
 * Author   : Ethan Krug
 * Email    : ethan.c.krug@gmail.com
 * Date     : May, 2022
 * 
 * Copyright (C) 2022 Ethan Krug
 */
package project;

import mt.MTRandom;

public class Algorithms {
    // The population of the algorithm
    private Population population;

    // The number of dimensions
    private int m;

    // The number of experiments/iterations
    private int n;

    // The solution vectors in the population
    private double[][] popMatrix;

    // The problem type of the algorithm
    private int problem;

    // The crossover method for the algorithm
    private int crosstype;

    // The solution vector of the algorithm
    private double[] solution;

    // Intialization of the fitness for the current algorithm
    private double fitness = Double.MAX_VALUE;

    // Random number generator
    private MTRandom r;

    /**
     * Constructor for IAlgs
     * 
     * @param algorithm - algorithm to run
     * @param pop       - population
     * @param problem   - problem type
     * @param crosstype - crossover type
     */
    public Algorithms(int algorithm, Population pop, int problem, int crosstype, int method, int index) {
        this.population = pop;
        this.m = pop.getPopulation()[0].length;
        this.n = pop.getPopulation().length;
        popMatrix = pop.getPopulation();
        this.problem = problem;
        this.crosstype = crosstype;
        this.r = new MTRandom(false);
        switch (algorithm) {
            case 1:
                this.solution = DE(method, popMatrix[0].length, popMatrix.length, 0.6, 0.9, 0.8, 100);
                break;
            case 2:
                Particle p = PSO(100, popMatrix.length, popMatrix[0].length, 0.8, 1.2);
                this.solution = p.solution;
                break;
            case 3:
                this.solution = blindSearch(n, pop.getPopulation()[index], fitness);
                break;
            case 4:
                this.solution = repeatedLocalSearch(pop.getPopulation()[index], null, null, false, n);
                break;
        }
    }

    /**
     * @return - solution vector of the algorithm
     */
    public double[] getSolution() {
        return solution;
    }

    /**
     * @return - the fitness of the solution vector
     */
    public double getFitness() {
        Problem p = new Problem(solution, problem);
        return p.getFitness();
    }

    /**
     * Returns the best solution found by the blind search algorithm
     * 
     * @param iterations - number of iterations
     * @param bestSol    - best solution found
     * @param fitness    - fitness of the best solution
     * @return - best solution found
     */
    public double[] blindSearch(int iterations, double[] bestSol, double fitness) {
        for (int i = 0; i < iterations; i++) {
            // Gets a random solution
            double[] arg = population.genRandomArray(m);

            // Evaluate the fitness of the random solution
            Problem prob = new Problem(arg, problem);

            // Get the fitness of the solution
            double fitnessNew = prob.getFitness();

            // If the fitness is better than the current best solution,
            // update the best solution
            if (fitnessNew < fitness) {
                fitness = fitnessNew;
                bestSol = arg;
            }
        }
        this.fitness = fitness;
        return bestSol;
    }

    /**
     * Returns the best solution found by the local search algorithm
     * 
     * @param initialSol - initial solution
     * @param bestSol    - best solution found
     * @param tau        - boolean variable to determine if the algorithm should
     *                   terminate
     * @return - best solution found
     */
    public double[] localSearch(double[] initialSol, double[] bestSol, boolean tau) {
        // Run algorithm until the solution doesn't improve
        while (tau) {
            tau = false;

            // Generate the neighborhood of the current solution
            double[][] neighborhood = population.genNeighborhood(n, m, initialSol);

            // Initialize best neighborhood fitness and solution
            double bestFitnessInNeighborhood = Double.MAX_VALUE;
            double[] bestSolutionInNeighborhood = new double[m];

            // Evaluate the fitness of the neighborhood
            for (int i = 0; i < neighborhood.length; i++) {
                Problem prob = new Problem(neighborhood[i], problem);
                double fitnessNew = prob.getFitness();

                // If the fitness is better than the current best solution in neighborhood,
                // update the best solution in neighborhood
                if (fitnessNew <= this.fitness) {
                    bestFitnessInNeighborhood = fitnessNew;
                    bestSolutionInNeighborhood = neighborhood[i];
                }
            }

            // If the best solution in the neighborhood is better than the current best
            // solution, update the best solution
            if (bestFitnessInNeighborhood < this.fitness) {
                this.fitness = bestFitnessInNeighborhood;
                bestSol = bestSolutionInNeighborhood;
                tau = true;
            }
        }
        return bestSol;
    }

    /**
     * Returns the best solution found by repeated local search algorithms
     * 
     * @param initialSol    - initial solution
     * @param bestGlobalSol - best overall solution found
     * @param bestIterSol   - best solution found in each iteration
     * @param tau           - boolean variable to determine if the algorithm should
     *                      terminate
     * @param iterations    - maximum number of iterations
     * @return - best solution found
     */
    public double[] repeatedLocalSearch(double[] initialSol, double[] bestGlobalSol, double[] bestIterSol, boolean tau,
            int iterations) {

        // Initialize best solutions
        bestGlobalSol = initialSol;
        bestIterSol = initialSol;
        tau = true;

        // Iteration counter
        int t = 1;

        // Run the algorithm until all iterations are complete
        while (t <= iterations) {
            // Get the local search solution for current iteration
            bestIterSol = localSearch(bestGlobalSol, bestIterSol, tau);

            // Get fitness of current iteration and global iteration
            Problem prob = new Problem(bestIterSol, problem);
            Problem probGlobal = new Problem(bestGlobalSol, problem);

            // If the fitness of the current iteration is better than the global
            // solution, update the global solution
            if (prob.getFitness() < probGlobal.getFitness()) {
                bestGlobalSol = bestIterSol;
            }
            t++;
            bestIterSol = population.genRandomArray(m);
        }
        return bestGlobalSol;
    }

    /**
     * Runs an experiment for the Differential Evolution algorithm
     * 
     * @param method      - mutation method
     * @param D           - Dimensions
     * @param NP          - Population size
     * @param CR          - Crossover rate
     * @param F           - Scaling factor
     * @param lambda      - Scaling factor
     * @param generations - Number of generations
     * @return - The best fitness vector
     */
    public double[] DE(int method, int D, int NP, double CR, double F, double lambda, int generations) {
        int generation = 0;
        while (generation < generations) {
            // Iterate over every solution in the population
            for (int i = 0; i < NP; i++) {

                // Getting the randomly selected vector indexes
                int r1 = i, r2 = i, r3 = i, r4 = i, r5 = i, jrand = r.nextInt(D);
                while (same(i, r1, r2, r3, r4, r5)) {
                    r1 = r.nextInt(NP);
                    r2 = r.nextInt(NP);
                    r3 = r.nextInt(NP);
                    r4 = r.nextInt(NP);
                    r5 = r.nextInt(NP);
                }

                // noisy vector
                double[] u = new double[D];

                // Mutation of noisy vector
                boolean crossed = false;
                while (!crossed) {
                    for (int k = 0; k < D; k++) {
                        if (r.nextDouble() < CR || k == jrand) {
                            u[k] = method(method, lambda, F, r1, r2, r3, r4, r5, k, i);
                            crossed = true;
                        } else {
                            u[k] = popMatrix[i][k];
                        }
                    }
                    if (crosstype == 2) {
                        crossed = true;
                    }
                }

                // Selection
                Problem p = new Problem(u, problem);
                Problem x = new Problem(popMatrix[i], problem);
                if (Math.abs(p.getFitness()) <= Math.abs(x.getFitness())) {
                    popMatrix[i] = u;
                }
            }
            generation++;
        }
        return popMatrix[bestSol()];
    }

    /**
     * 
     * @param i  - first value to compare
     * @param r1 - second value to compare
     * @param r2 - third value to compare
     * @param r3 - fourth value to compare
     * @param r4 - fifth value to compare
     * @param r5 - sixth value to compare
     * @return - a boolean value that says whether or not there are two or more
     *         inputs that are equal
     */
    private boolean same(int i, int r1, int r2, int r3, int r4, int r5) {
        return i == r1 || i == r2 || i == r3 || i == r4 || i == r5 || r1 == r2 || r1 == r3 || r1 == r4
                || r1 == r5 || r2 == r3 || r2 == r4 || r2 == r5 || r3 == r4 || r3 == r5 || r4 == r5;
    }

    /**
     * Used for getting the value to insert in the noisy vector
     * 
     * @param method - the method to be used to get new value at mutation
     * @param lambda - scaling factor
     * @param F      - scaling factor
     * @param r1     - randomly selected index
     * @param r2     - randomly selected index
     * @param r3     - randomly selected index
     * @param r4     - randomly selected index
     * @param r5     - randomly selected index
     * @param k      - index of the element to be mutated
     * @param i      - index of the current element
     * @return - the mutated value or NaN if the method is not implemented
     */
    private double method(int method, double lambda, double F, int r1, int r2, int r3, int r4, int r5, int k, int i) {
        switch (method) {
            case 1: // DE/best/1
                return popMatrix[bestSol()][k] + F * (popMatrix[r1][k] - popMatrix[r2][k]);
            case 2: // DE/rand/1
                return popMatrix[r1][k] + F * (popMatrix[r2][k] - popMatrix[r3][k]);
            case 3: // DE/rand-to-best/1
                return popMatrix[i][k] + lambda * (popMatrix[bestSol()][k] - popMatrix[i][k])
                        + F * (popMatrix[r1][k] - popMatrix[r2][k]);
            case 4: // DE/best/2
                return popMatrix[bestSol()][k]
                        + F * (popMatrix[r1][k] + popMatrix[r2][k] - popMatrix[r3][k] - popMatrix[r4][k]);
            case 5: // DE/rand/2
                return popMatrix[r5][k]
                        + F * (popMatrix[r1][k] + popMatrix[r2][k] - popMatrix[r3][k] - popMatrix[r4][k]);
            default: // Invalid method
                return Double.NaN;
        }
    }

    /**
     * Returns the index of the best solution in the population
     * 
     * @return - the index of the best solution
     */
    private int bestSol() {
        int best = 0;
        for (int i = 0; i < popMatrix.length; i++) {
            Problem p = new Problem(popMatrix[i], problem);
            Problem b = new Problem(popMatrix[best], problem);
            if (Math.abs(p.getFitness()) <= Math.abs(b.getFitness())) {
                best = i;
            }
        }
        return best;
    }

    /**
     * Runs an experiment for the Particle Swarm Optimization algorithm
     * 
     * @param iterations   - Number of iterations
     * @param numParticles - Number of particles
     * @param dimensions   - Number of dimensions
     * @param c1           - cognitive factor
     * @param c2           - social factor
     * @return - The best fitness particle
     */
    public Particle PSO(int iterations, int numParticles, int dimensions, double c1, double c2) {
        // Get range of values
        double range = population.getRange();

        // Initialize the particles
        Particle[] particles = new Particle[numParticles];
        for (int i = 0; i < numParticles; i++) {
            particles[i] = new Particle(popMatrix[i], range, problem);
            particles[i].setPBest(particles[i]);
        }

        // Initialize the best particle
        Particle gBest = particles[0];
        for (int i = 1; i < numParticles; i++) {
            if (Math.abs(particles[i].fitness) < gBest.fitness) {
                gBest = particles[i];
            }
        }

        // Runs the main part of the PSO algorithm
        for (int t = 0; t < iterations; t++) {
            // Update the states of every particle in the swarm
            for (int j = 0; j < numParticles; j++) {
                // Update the velocity and position of the particles
                for (int k = 0; k < dimensions; k++) {
                    // Calculate the new velocity
                    double addVel = particles[j].velocity
                            + c1 * r.nextDouble() * (particles[j].pBest.solution[k] - particles[j].solution[k])
                            + c2 * r.nextDouble() * (gBest.solution[k] - particles[j].solution[k]);
                    // Add the new velocity to the current position
                    particles[j].solution[k] += addVel;
                }

                // Update the pBest and fitness of the particle
                Problem p = new Problem(particles[j].solution, problem);
                if (Math.abs(p.getFitness()) < Math.abs(particles[j].fitness)) {
                    particles[j].pBest = particles[j];
                }
                particles[j].fitness = p.getFitness();

                // Update the gBest
                if (Math.abs(particles[j].fitness) < gBest.fitness) {
                    gBest = particles[j];
                }
            }
        }
        return gBest;
    }
}
