/*
 * Particle : A particle object implemented for the Particle Swarm Optimization
 *            algorithm.
 * Author   : Ethan Krug
 * Email    : ethan.c.krug@gmail.com
 * Date     : May, 2022
 * 
 * Copyright (C) 2022 Ethan Krug
 */
package project;

import mt.MTRandom;

public class Particle {
    // The current position of the particle
    public double[] solution;

    // The current velocity of the particle
    public double velocity;

    // The fitness of the particle
    public double fitness;

    // The best position the particle has ever achieved
    public Particle pBest;

    /**
     * Constructor for Particle
     * 
     * @param solution - solution vector
     * @param range    - range of values for the particle
     * @param problem  - problem type
     */
    public Particle(double[] solution, double range, int problem) {
        this.solution = solution;
        this.fitness = new Problem(solution, problem).getFitness();
        this.pBest = null;
        MTRandom r = new MTRandom();
        // Upper bound is [range] and lower bound is [-range], so 50% of
        // [U - L] = [range]
        this.velocity = r.nextDouble() * range;
    }

    /**
     * Sets the pBest particle
     * 
     * @param pBest - the new pBest particle
     */
    public void setPBest(Particle pBest) {
        this.pBest = pBest;
    }
}
