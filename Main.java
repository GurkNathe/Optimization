import project.Population;
import project.Algorithms;
import project.NEH;

import java.util.*;
import java.io.*;
import java.util.stream.LongStream;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Types of functions: minimization or scheduling.");
        String type = "";

        /**
         * Valid inputs: "minimization", "scheduling".
         */
        while (!type.equals("minimization") && !type.equals("scheduling")) {
            System.out.print("Enter the wanted optimization functions type: ");
            type = sc.nextLine();
        }

        int function = type == "minimization" ? 0 : 1;

        /**
         * Choose the algorithm type to use.
         * 0: minimization algorithms
         * 1: scheduling algorithms
         */
        switch (function) {
            case 0:
                minimizeDriver();
            case 1:
                scheduleDriver();
        }

        sc.close();
    }

    /**
     * Driver for the minimization functions.
     */
    public static void minimizeDriver() {
        try {
            /**
             * Reading from the input file and creating the output file
             * 
             * Input file format:
             * [algorithm] [DE method] [crossover type] [dimension] [population size]
             * [problem type] [range] [num experiments]
             */
            BufferedReader br = readFromFile("experiments.txt");
            BufferedWriter bw = makeOutFile();

            // Reads line from input file
            String line = br.readLine();

            // Main loop for conducting experiments
            fileLoop(line, br, bw);

            // Close input and output files when done
            bw.close();
            br.close();

        } catch (IOException e) {

            // If there is an error, print it
            e.printStackTrace();
        }
    }

    /**
     * Driver for the scheduling functions.
     */
    public static void scheduleDriver() {
        try {
            // Reading from the input file and creating the output file
            BufferedWriter bw = makeOutFile();
            // Loop for each algorithm (FSS and FSSB)
            for (int a = 0; a < 2; a++) {
                // Writing the header of the output file
                bw.write("Machines" + "," + "Jobs" + "," + "Makespan" + "," + "Time" + "," + "Results("
                        + (a == 0 ? "FSS" : "FSSB") + ")\n");
                // Loop to process all the input files
                for (int i = 1; i <= 120; i++) {
                    ArrayList<String> lines = readLines(i);

                    // Getting the number of machines and jobs from the first line
                    String[] parts = lines.get(0).split(" ");

                    // Number of machines
                    int m = Integer.parseInt(parts[0]);

                    // Number of jobs
                    int n = Integer.parseInt(parts[1]);

                    // Creating the processing times matrix
                    int[][] times = new int[m][n];
                    for (int j = 1; j < lines.size(); j++) {
                        String[] lineParts = lines.get(j).split(" ");
                        for (int k = 0; k < n; k++) {
                            times[j - 1][k] = Integer.parseInt(lineParts[k]);
                        }
                    }

                    // Run experiment for given inputs
                    scheduleLoop(times, m, n, a, bw);
                }
            }
            bw.close();
        } catch (IOException e) {
            // If there is an error, print it
            e.printStackTrace();
        }
    }

    /**
     * Runs the minimization experiments for the given line
     * 
     * @param line - line from input file
     * @param br   - BufferedReader for input file
     * @param bw   - BufferedWriter for output file
     * @exception IOException - if there is an error with the input or output files
     */
    public static void fileLoop(String line, BufferedReader br, BufferedWriter bw) {
        try {
            // Loop until end of input file
            while (line != null) {

                // Get experiment parameters
                String[] parts = line.split(" ");

                /**
                 * algorithm - algorithm to use (1 - DE, 2 - PSO)
                 * method - method to use (1 - DE/Best/1, 2 - DE/Rand/1, 3 - DE/Rand-To-Best/1,
                 * 4 - DE/Best/2, 5 - DE/Rand/2)
                 * crosstype - crossover type (1 - exponential, 2 - binomial)
                 * m - dimensions
                 * n - population size
                 * problem - problem type
                 * range - range of values
                 * numExperiments - number of experiments to run
                 */
                int algorithm = Integer.parseInt(parts[0]);
                int method = Integer.parseInt(parts[1]);
                int crosstype = Integer.parseInt(parts[2]);
                int m = Integer.parseInt(parts[3]);
                int n = Integer.parseInt(parts[4]);
                int problem = Integer.parseInt(parts[5]);
                double range = Double.parseDouble(parts[6]);
                int numExperiments = Integer.parseInt(parts[7]);

                // Initialize population
                Population pop = new Population(n, m, range);
                long[] times = new long[n];

                // Run the experiments
                experiment(numExperiments, pop, times, algorithm, problem, method, crosstype);

                // Sum time array for total time for the experiment
                long sum = LongStream.of(times).sum();

                // Save experiment in a CSV file
                writeFile(bw, problem, numExperiments, m, algorithm, range, sum, pop, method, crosstype);

                // Read next line from input file
                line = br.readLine();
            }
        } catch (IOException e) {

            // If there is an error, print it
            e.printStackTrace();
        }
    }

    /**
     * Runs [n] minimization experiments of problem type [problem], and stores
     * fitness values in the population
     * 
     * @param n         - number of experiments
     * @param pop       - population to store fitness values
     * @param times     - array to store time values
     * @param algorithm - algorithm to use
     * @param problem   - problem type
     * @param method    - method to use for mutation in DE
     * @param crosstype - crossover type for DE
     */
    public static void experiment(int n, Population pop, long[] times, int algorithm, int problem, int method,
            int crosstype) {
        for (int i = 0; i < n; i++) {
            long start = System.nanoTime();
            Algorithms alg = new Algorithms(algorithm, pop, problem, crosstype, method, i);
            System.out.println(i);
            times[i] = System.nanoTime() - start;
            pop.setFitness(i, alg.getFitness());
            pop.setSolution(i, alg.getSolution());
        }
    }

    /**
     * Runs the scheduling experiment for the given input file
     * 
     * @param times - the processing times matrix.
     * @param m     - the number of machines.
     * @param n     - the number of jobs.
     * @param alg   - the scheduling algorithm to use.
     * @param bw    - the BufferedWriter for the output file.
     */
    public static void scheduleLoop(int[][] times, int m, int n, int alg, BufferedWriter bw) {
        // Start timer
        long time = System.nanoTime();
        // Run the experiment
        NEH algos = new NEH(times, m, n, alg);
        // Stop timer
        time = System.nanoTime() - time;
        // Write the results to the output file
        writeResult(algos, bw, m, n, time);
    }

    /**
     * This method reads the lines from the input file and returns them as an
     * ArrayList (for scheduling)
     * 
     * @param i - the number of the input file
     * @return - the ArrayList of lines from the input file
     */
    public static ArrayList<String> readLines(int i) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader br = readFromFile("./project/Taillard_TestData/" + i + ".txt");
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Save minimization experiments to file
     * 
     * @param bw        - BufferedWriter to write to
     * @param problem   - Problem type
     * @param n         - Number of experiments
     * @param m         - Dimensions
     * @param range     - Range of values
     * @param sum       - Total time for the experiment
     * @param pop       - Population
     * @param method    - Method to use for mutation in DE
     * @param crosstype - Crossover type for DE
     * @exception IOException - if there is an error with the output file
     */
    public static void writeFile(BufferedWriter bw, int problem, int n, int m, int alg, double range, long sum,
            Population pop, int method, int crosstype) {
        try {
            // Gets the algorithm type
            String algType = (alg == 1) ? "" : "Partical Swarm Optimization";
            String meth = "";
            // Gets the method type for DE
            switch (method) {
                case 1:
                    meth = "DE/Best/1";
                    break;
                case 2:
                    meth = "DE/Rand/1";
                    break;
                case 3:
                    meth = "DE/Rand-To-Best/1";
                    break;
                case 4:
                    meth = "DE/Best/2";
                    break;
                case 5:
                    meth = "DE/Rand/2";
                    break;
                default:
                    break;
            }
            // Gets the crossover type for DE
            String cross = (crosstype == 1) ? "exp" : (crosstype == 2) ? "bin" : "";

            // Writes the summary of the experiments
            bw.write("Problem " + problem + " with " + n + " experiments of dimension " + m + " in range [-" + range
                    + " : " + range + "]"
                    + " using the " + algType + meth + cross + " algorithm that took " + +(double) sum / 1000000
                    + "milliseconds to run" + ",");

            // Writes the fitness values of the population
            for (int i = 0; i < n; i++) {
                bw.write(pop.getFitness()[i] + ",");
            }
            bw.write("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the results of the scheduling experiment to the CSV file
     * 
     * @param algos - the results of the experiment
     * @param bw    - the CSV file to write to
     * @param m     - the number of machines
     * @param n     - the number of jobs
     * @param time  - the time it took to run the experiment
     */
    public static void writeResult(NEH algos, BufferedWriter bw, int m, int n, long time) {
        // Prints the results to the output file
        try {
            // Writing # machines, # jobs, makespan, time, results
            bw.write(m + "," + n + "," + algos.makespan + "," + (double) time / 1000000 + ",");
            for (int i = 0; i < n; i++) {
                bw.write(algos.schedule[i] + 1 + ",");
            }
            bw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads input file with given filename and returns a buffered reader for the
     * input file
     * 
     * @param filename - name of the input file
     * @return buffered reader for the input file
     * @exception FileNotFoundException - if there is an error with the input file
     */
    public static BufferedReader readFromFile(String filename) {
        BufferedReader br = null;
        try {
            File experiments = new File(filename);
            br = new BufferedReader(new FileReader(experiments));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return br;
    }

    /**
     * Creates a CSV file to write the experiments to
     * 
     * @return BufferedWriter of that CSV file
     * @exception IOException - if there is an error with the output file
     */
    public static BufferedWriter makeOutFile() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("experiments" + System.currentTimeMillis() + ".csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bw;
    }

}
