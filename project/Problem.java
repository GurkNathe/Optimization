/*
 * Problem : A Java implementation of ten mathematical functions typically 
 *           used in testing optimization algorithms.
 * Author   : Ethan Krug
 * Email    : ethan.c.krug@gmail.com
 * Date     : May, 2022
 * 
 * Copyright (C) 2022 Ethan Krug
 */
package project;

public class Problem {
    // Holds the generated values passed to the constructor
    private double[] values;

    // Holds the fitness value of values based on the problem type
    private double fitness;

    /**
     * Constructor for Problem
     * 
     * @param vector  - vector of values
     * @param probNum - problem type
     */
    public Problem(double[] vector, int probNum) {
        values = vector;
        switch (probNum) {
            case 1:
                fitness = schwefel();
                break;
            case 2:
                fitness = de_jong_1();
                break;
            case 3:
                fitness = rosenbrock();
                break;
            case 4:
                fitness = rastrigin();
                break;
            case 5:
                fitness = griewank();
                break;
            case 6:
                fitness = sine_envelope();
                break;
            case 7:
                fitness = sine_V();
                break;
            case 8:
                fitness = ackley_one();
                break;
            case 9:
                fitness = ackley_two();
                break;
            case 10:
                fitness = egg_holder();
                break;
        }
    }

    /**
     * Returns the fitness value of the problem
     * 
     * @return - returns the fitness value of the problem
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Returns the nth root of the input value
     * 
     * @param root  - the root to use
     * @param value - the value to use
     * @return - returns decimal form of the nth root of the input value
     */
    public double nthRoot(int root, double value) {
        return Math.pow(value, 1.0 / root);
    }

    /**
     * Returns the square of the input value
     * 
     * @param value - the value to use
     * @return - returns the decimal form of the square of the input value
     */
    public double square(double value) {
        return value * value;
    }

    /**
     * Returns the value of the Schwefel function using the values in the vector
     * 
     * @return - returns the fitness value of the Schwefel function
     */
    public double schwefel() {
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += -values[i] * Math.sin(Math.sqrt(Math.abs(values[i])));
        }
        return 418.9829 * values.length - sum;
    }

    /**
     * Returns the value of the De Jong 1 function using the values in the vector
     * 
     * @return - returns the fitness value of the De Jong 1 function
     */
    public double de_jong_1() {
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += square(values[i]);
        }
        return sum;
    }

    /**
     * Returns the value of the Rosenbrock function using the values in the vector
     * 
     * @return - returns the fitness value of the Rosenbrock function
     */
    public double rosenbrock() {
        double sum = 0;
        for (int i = 0; i < values.length - 1; i++) {
            sum += 100 * square((square(values[i]) - values[i + 1])) + square((1 - values[i]));
        }
        return sum;
    }

    /**
     * Returns the value of the Rastrigin function using the values in the vector
     * 
     * @return - returns the fitness value of the Rastrigin function
     */
    public double rastrigin() {
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += square(values[i]) - 10 * Math.cos(2 * Math.PI * values[i]);
        }
        return 10 * values.length + sum;
    }

    /**
     * Returns the value of the Griewank function using the values in the vector
     * 
     * @return - returns the fitness value of the Griewank function
     */
    public double griewank() {
        double sum = 0;
        double prod = 1;
        for (int i = 0; i < values.length; i++) {
            sum += square(values[i]);
            prod *= Math.cos(values[i] / Math.sqrt(i + 1));
        }
        return sum / 4000 - prod + 1;
    }

    /**
     * Returns the value of the Sine Envelope function using the values in the
     * vector
     * 
     * @return - returns the fitness value of the Sine Envelope function
     */
    public double sine_envelope() {
        double sum = 0;
        for (int i = 0; i < values.length - 1; i++) {
            double top = square(Math.sin(square(values[i]) + square(values[i + 1]) - 0.5));
            double bottom = square(1 + 0.001 * (square(values[i]) + square(values[i + 1])));
            sum += 0.5 + top / bottom;
        }
        return -sum;
    }

    /**
     * Returns the value of the Sine V function using the values in the vector
     * 
     * @return - returns the fitness value of the Sine V function
     */
    public double sine_V() {
        double sum = 0;
        for (int i = 0; i < values.length - 1; i++) {
            double first = nthRoot(4, square(values[i]) + square(values[i + 1]));
            double second = square(Math.sin(50 * nthRoot(10, square(values[i]) + square(values[i + 1]))));
            sum += first * second + 1;
        }
        return sum;
    }

    /**
     * Returns the value of the Ackley One function using the values in the vector
     * 
     * @return - returns the fitness value of the Ackley One function
     */
    public double ackley_one() {
        double sum = 0;
        for (int i = 0; i < values.length - 1; i++) {
            double first = Math.pow(Math.E, -0.2) * Math.sqrt(square(values[i]) + square(values[i + 1]));
            double second = 3 * (Math.cos(2 * values[i]) + Math.sin(2 * values[i + 1]));
            sum += first + second;
        }
        return sum;
    }

    /**
     * Returns the value of the Ackley Two function using the values in the vector
     * 
     * @return - returns the fitness value of the Ackley Two function
     */
    public double ackley_two() {
        double sum = 0;
        for (int i = 0; i < values.length - 1; i++) {
            double first = Math.pow(Math.E,
                    0.2 * Math.sqrt((square(values[i]) + square(values[i + 1])) / 2));
            double second = Math.pow(Math.E,
                    0.5 * (Math.cos(2 * Math.PI * values[i]) + Math.cos(2 * Math.PI * values[i + 1])));
            sum += 20 + Math.E - (20 / first) - second;
        }
        return sum;
    }

    /**
     * Returns the value of the Egg Holder function using the values in the vector
     * 
     * @return - returns the fitness value of the Egg Holder function
     */
    public double egg_holder() {
        double sum = 0;
        for (int i = 0; i < values.length - 1; i++) {
            double first = -values[i] * Math.sin(Math.sqrt(Math.abs(values[i] - values[i + 1] - 47)));
            double second = (values[i + 1] + 47) * Math.sin(Math.sqrt(Math.abs(values[i + 1] + 47 + values[i] / 2)));
            sum += first - second;
        }
        return sum;
    }

}
