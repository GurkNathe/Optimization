/*
 * Population : Population object implemented as a control structure to assist 
 *              in running the optimization algorithms.
 * Author   : Ethan Krug
 * Email    : ethan.c.krug@gmail.com
 * Date     : May, 2022
 * 
 * Copyright (C) 2022 Ethan Krug
 */
package project;

import mt.MTRandom;

public class Population {

    // Holds the generated values for each experiment
    private double[][] population;

    // Holds the fitness values of each experiment
    private double[] fitness;

    // Holds the solution vectors
    private double[][] solutions;

    // Range of possible values
    private double range;

    /**
     * Constructor for the Population class.
     * 
     * @param n     - number of experiments
     * @param m     - number of dimensions
     * @param range - range of the values selected <strong>[-range, range]</strong>
     */
    public Population(int n, int m, double range) {
        this.range = range;
        fitness = new double[n];
        solutions = new double[n][m];
        population = genRandomMatrix(n, m);
    }

    /**
     * Returns the fitness vector
     * 
     * @return - fitnesses of the population
     */
    public double[] getFitness() {
        return fitness;
    }

    /**
     * Sets the value of the fitness at the index
     * 
     * @param i       - index
     * @param fitness - fitness value
     */
    public void setFitness(int i, double fitness) {
        this.fitness[i] = fitness;
    }

    /**
     * Sets the values of the solution vector at the index
     * 
     * @param i   - index
     * @param sol - solution vector
     */
    public void setSolution(int i, double[] sol) {
        solutions[i] = sol;
    }

    /**
     * Returns the population matrix
     * 
     * @return - population matrix
     */
    public double[][] getPopulation() {
        return population;
    }

    /**
     * Returns the solutions matrix
     * 
     * @return - solutions matrix
     */
    public double[][] getSolution() {
        return solutions;
    }

    /**
     * Sets the population matrix to the given matrix
     * 
     * @param population - given population matrix
     */
    public void setPopulation(double[][] population) {
        this.population = population;
    }

    /**
     * Returns the range of the initial population values
     * 
     * @return - range
     */
    public double getRange() {
        return range;
    }

    /**
     * Creates an n x m matrix initialized to pseudo-random values
     * 
     * @param n - number of experiments
     * @param m - number of dimensions
     * @return - n x m matrix
     */
    public double[][] genRandomMatrix(int n, int m) {
        double[][] matrix = new double[n][m];
        MTRandom r = new MTRandom(false);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = r.nextDouble() * (range - (-range)) + (-range);
            }
        }
        return matrix;
    }

    /**
     * Returns an array of random values within the range
     * 
     * @param m - dimension of the array
     * @return - array of random values
     */
    public double[] genRandomArray(int m) {
        double[] array = new double[m];
        MTRandom r = new MTRandom(false);
        for (int i = 0; i < m; i++) {
            array[i] = r.nextDouble() * (range - (-range)) + (-range);
        }
        return array;
    }

    /**
     * Returns a neighbor of the given solution vector
     * 
     * @param n        - number of experiments
     * @param m        - number of dimensions
     * @param solution - solution vector
     * @return - the neighborhood of the given solution vector
     */
    public double[][] genNeighborhood(int n, int m, double[] solution) {
        double[][] neighborhood = new double[n][m];
        MTRandom r = new MTRandom(false);

        // Fill a neighborhood with pseudo-random values based off of the solution
        // vector within a given range
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double value = solution[j] + r.nextDouble() * (range - (-range)) + (-range);
                if (value > range) {
                    value = range;
                } else if (value < -range) {
                    value = -range;
                }

                neighborhood[i][j] = value;
            }
        }
        return neighborhood;
    }
}