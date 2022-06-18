# Optimization

This is project that is used for testing optimization algorithms.

The currently implemented algorithms are:

- [Blind Search](https://en.wikipedia.org/wiki/Random_walk)
- [Repeated Local Search](https://en.wikipedia.org/wiki/Iterated_local_search)
- [Differential Evolution](https://en.wikipedia.org/wiki/Differential_evolution)
- [Particle Swarm Optimization](https://en.wikipedia.org/wiki/Particle_swarm_optimization)
- [Nawaz-Enscore-Ham (NEH)](https://www.hindawi.com/journals/jam/2020/7132469/alg2/)

# Running the project

Using the Java Virtual Machine (JVM), run the project from the Main file in this project to generate the desired output files.

In the terminal a prompt will appear. Either type "minimization" or "scheduling" for the desired algorithm types. The minimization algorithms are: Blind Search, Repeated Local Search, Differential Evolution, Particle Swarm Optimization. The scheduling algorithm is: NEH.

# Experiment File Formats

## Scheduling Algorithm File Format

Line 1: `[# of machines] [# of jobs]` <br/>
Following lines: processing times for each job for each machine. Each line represents a machine and the times for each job are separated by spaces.

Example: <br/>

<p style="text-align: center;">
5 4<br/>
5 9 9 4<br/>
9 3 4 8<br/>
8 10 5 8<br/>
10 1 8 7<br/>
1 8 6 2<br/>
</p>

The first line in the example says there are five machines (five lines after the first one) and four jobs (4 numbers per following line). The values in the following lines are the processing times for each job for each machine (e.g., on machine 1, job 1 has a processing time of 5).

Sample files can be found in the project folder in the Taillard_TestData folder.

## Minimization Algorithm File Format

`[algorithm] [DE method] [crossover type] [dimension] [population size] [problem type] [range] [num experiments]`

- The values for `[algorithm]` are 1 for DE, 2 for PSO, 3 for Blind Search, and 4 for Repeated Local Search.
- The values for `[DE method]` are 1 for DE/best/1, 2 for DE/rand/1, 3 for DE/rand-to-best/1, 4 for DE/best/2, and 5 for DE/rand/2.
- The values for `[crossover type]` are 1 for exponential crossover and 2 for binomial crossover.
- The value for `[dimension]` is the number of elements in each solution vector.
- The value for `[population size]` is the number of solution vectors in the population.
- The value for `[problem type]` is the objective function label: 1 - Schwefel, 2 - De Jong 1, 3 - Rosenbrock, 4 - Rastrigin, 5 - Griewank, 6 - Sine Envelope Sine Wave, 7 - Stretch V Sine Wave, 8 - Ackley One, 9 - Ackley Two, 10 - Egg Holder.
- The value for `[range]` is the range of initial values for each element in the solution vector.
- The value for `[num experiments]` is the number of experiments to run.

The experiments.txt file contains a list of all the experiments to run. The experiments are run in the order they appear in the file. The experiments.txt file should be in the same directory as the Main file to properly run the project.

# Output File Format

The output files are named according to the current system time. The output files are placed in the directory of the Main file.

## Scheduling Algorithms

Generates a CSV where each row is a different experiment. The first column is the number of machines, the second column is the number of jobs, the third column is the resulting makespan value, the fourth column is the run time for the experiment, the following columns in a row hold the resulting schedule for the experiment.

## Minimization Algorithms

Generates a CSV where each row is the resulting fitness values from each experiment. The first entry in each row has information on the algorithm run, how many experiments were run, and the time it took to run the experiments.

# Documentation

The documentation.pdf file contains the documentation for this project. The documentation was generated using Doxygen. A Doxygen config file is included if you want to configure the documentation to your liking.
