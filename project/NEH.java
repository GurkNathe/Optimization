/*
 * Algorithms : A Java implementation of optimization algorithm: 
 *              Nawaz-Enscore-Ham (NEH) permutation flow shop 
 *              scheduling algorithm. Implements two makespan 
 *              algorithms: Flow Shop Scheduling (FSS), 
 *              Flow Shop Scheduling with Blocking (FSSB).
 * Author   : Ethan Krug
 * Email    : ethan.c.krug@gmail.com
 * Date     : June, 2022
 * 
 * Copyright (C) 2022 Ethan Krug
 */
package project;

public class NEH {
    // Hold the processing times of each job on each machine
    public int[][] matrix;

    // The schedule determined by the algorithm
    public int[] schedule;

    // The makespan of the schedule
    public int makespan;

    // The number of machines
    public int m;

    // The number of jobs
    public int n;

    /**
     * Constructor
     * 
     * @param times - the processing times of each job on each machine
     * @param m     - the number of machines
     * @param n     - the number of jobs
     * @param alg   - the algorithm to use
     */
    public NEH(int[][] times, int m, int n, int alg) {
        matrix = times;
        schedule = new int[n];
        this.m = m;
        this.n = n;
        makespan = runNEH(alg);
    }

    /**
     * The NEH algorithm to determine the schedule
     * 
     * @param alg - the algorithm to use
     * @return the makespan of the determined schedule
     */
    public int runNEH(int alg) {
        // Total processing time for each job
        int[] totals = new int[n];

        // Get the total processing time for each job
        for (int i = 0; i < n; i++) {
            totals[i] = 0;
            for (int j = 0; j < m; j++) {
                totals[i] += matrix[j][i];
            }
        }

        // Sort the jobs by total processing time in descending order
        int[] sorted = new int[n];
        for (int i = 0; i < n; i++) {
            sorted[i] = i;
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (totals[sorted[i]] < totals[sorted[j]]) {
                    int temp = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = temp;
                }
            }
        }

        // Transpose matrix for ease of use
        int[][] transMatrix = transposeMatrix(matrix);

        // Current number of jobs in the schedule
        int L = 2;

        // Initialize the schedule and the makespan
        schedule[0] = sorted[0];
        int currMake = -1;

        // Find the best schedule with the shortest makespan
        while (L < n + 1) {
            // Best schedule and makespan for the current L
            int[] best = new int[L];
            int bestScore = Integer.MAX_VALUE;

            // For every possible schedule of length L
            for (int i = 0; i < L; i++) {
                // Creating job order
                int[] order = new int[L];
                int newJob = sorted[L - 1];
                order[i] = newJob;
                for (int j = 0, k = 0; j < L; j++) {
                    if (i != j) {
                        order[j] = schedule[k];
                        k++;
                    }
                }

                // Calculating makespan
                int[][] tempMatrix = new int[L][m];
                for (int j = 0; j < L; j++) {
                    tempMatrix[j] = transMatrix[order[j]];
                }
                tempMatrix = transposeMatrix(tempMatrix);
                int makespan = alg == 0 ? FSS(tempMatrix, m, L) : FSSB(tempMatrix, m, L);

                // If the makespan is better than the best one, update the best
                if (makespan < bestScore) {
                    bestScore = makespan;
                    best = order;
                }
            }

            // Update the schedule
            for (int i = 0; i < L; i++) {
                schedule[i] = best[i];
            }

            // Update the best makespan for the current L
            currMake = bestScore;

            // Increase number of jobs in the schedule
            L += 1;
        }

        return currMake;
    }

    /**
     * This method transposes the given matrix
     * Function from:
     * https://stackoverflow.com/questions/26197466/transposing-a-matrix-from-a-2d-array
     * 
     * @param matrix - the matrix to be transposed
     * @return - the transposed matrix
     */
    public static int[][] transposeMatrix(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        int[][] transposedMatrix = new int[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                transposedMatrix[x][y] = matrix[y][x];
            }
        }

        return transposedMatrix;
    }

    /**
     * The FSS algorithm for the makespan problem.
     * 
     * @param times - the matrix of processing times
     * @param m     - the number of machines
     * @param n     - the number of jobs
     * @return the makespan value of the schedule
     */
    public int FSS(int[][] times, int m, int n) {
        int[][] makespan = new int[m][n];
        makespan[0][0] = times[0][0];

        // Processing job 1 on all machines
        for (int i = 1; i < m; i++) {
            makespan[i][0] = makespan[i - 1][0] + times[i][0];
        }

        // Processing all jobs on machine 1
        for (int j = 1; j < n; j++) {
            makespan[0][j] = makespan[0][j - 1] + times[0][j];
        }

        // Processing all jobs on all machines
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                makespan[i][j] = Math.max(makespan[i - 1][j], makespan[i][j - 1]) + times[i][j];
            }
        }

        return makespan[m - 1][n - 1];
    }

    /**
     * The FSSB algorithm for the makespan problem.
     * 
     * @param times - the matrix of processing times
     * @param m     - the number of machines
     * @param n     - the number of jobs
     * @return the makespan value of the schedule
     */
    public int FSSB(int[][] times, int m, int n) {
        int[][] makespan = new int[m][n];
        makespan[0][0] = times[0][0];

        // Processing job 1 on all machines
        for (int i = 1; i < m; i++) {
            makespan[i][0] = makespan[i - 1][0] + times[i][0];
        }

        // Finding departure times for all jobs on all machines
        for (int j = 1; j < n; j++) {
            for (int i = 1; i < m - 1; i++) {
                makespan[i][j] = Math.max(makespan[i - 1][j] + times[i][j], makespan[i + 1][j - 1]);
            }
            makespan[m - 1][j] = makespan[m - 2][j] + times[m - 1][j];
        }

        return makespan[m - 1][n - 1];
    }
}
