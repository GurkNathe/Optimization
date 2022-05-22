# Optimization

This is project that is used for testing optimization algorithms.

The currently implemented algorithms are:

- [Blind Search](https://en.wikipedia.org/wiki/Random_walk)
- [Repeated Local Search](https://en.wikipedia.org/wiki/Iterated_local_search)
- [Differential Evolution](https://en.wikipedia.org/wiki/Differential_evolution)
- [Particle Swarm Optimization](https://en.wikipedia.org/wiki/Particle_swarm_optimization)

# Running the project

Using the Java Virtual Machine (JVM), run the project from the Main file in this project to generate the desired output files.

# Experiment File Format

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

Generates a CSV where each row is the resulting fitness values from each experiment. The first entry in each row has information on the algorithm run, how many experiments were run, and the time it took to run the experiments.

The output files are named according to the current system time. The output files are placed in the directory of the Main file.

# Documentation

The documentation.pdf file contains the documentation for this project. The html folder contains index.html which is used to create the web version of the documentation that has interactive features for viewing the documentation. The documentation was generated using Doxygen.
