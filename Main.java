import project.Population;
import project.Algorithms;

import java.io.*;
import java.util.stream.LongStream;

public class Main {
    public static void main(String[] args) {
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
     * Runs the experiments for the given line
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
     * Reads input file with given filename and returns a buffered reader for the
     * input file
     * 
     * @param filename - name of the input file
     * @return buffered reader for the input file
     * @exception IOException - if there is an error with the input file
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

    /**
     * Runs [n] experiments of problem type [problem], and stores fitness values in
     * the population
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
     * Save experiments to file
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

}
