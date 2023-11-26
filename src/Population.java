import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Collections.sort;

public class Population {
    final private int chromosomeSize;
    private HashMap<Integer, Individual> population;
    private HashMap<Integer, Integer> parents;
    private int populationSize;
    private double crossProb;
    private double mutationProb;
    private Function function;

    Population(int populationSize, int chromosomeSize, double mutationProb, double crossProb, Function function) {
        this.population = new HashMap<>();
        this.parents = new HashMap<>();
        this.chromosomeSize = chromosomeSize;
        this.populationSize = populationSize;
        this.crossProb = crossProb;
        this.function = function;

        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(chromosomeSize, mutationProb);
            individual.generateChromosome();
            population.put(i, individual);
        }
    }

    Population evolution() {
        Population duringEvoGeneration = new Population(populationSize, chromosomeSize, mutationProb, crossProb, function);
        Population newGeneration = new Population(populationSize, chromosomeSize, mutationProb, crossProb, function);
        generateUniquePairs();
        double randomProb;

        //1.Crossing
        for (Integer key : parents.keySet()) {
            randomProb = Math.random();
            int momIndex = key;
            int dadIndex = parents.get(key);
            Individual mom = population.get(momIndex).copy();
            Individual dad = population.get(dadIndex).copy();
            if (randomProb < crossProb) {
                crossing(mom, dad);
            }
            duringEvoGeneration.population.put(momIndex, mom);
            duringEvoGeneration.population.put(dadIndex, dad);
        }

        //2.Mutation
        for (Integer key : duringEvoGeneration.population.keySet()) {
            Individual individual = duringEvoGeneration.population.get(key).copy();
            individual.mutation();
            duringEvoGeneration.population.put(key, individual);
        }

        //3.Selection
        Map<Integer, Integer> indivProbMap = new HashMap<>();
        for (Integer key : duringEvoGeneration.population.keySet()) {
            int x = duringEvoGeneration.population.get(key).chromosomeToDigital();
            System.out.println("x: " + x);
            int prob = (function.fx(x));
                    //- fxMin() + 1)/ sumFxPositive();
            System.out.println("prob: " + prob);
            indivProbMap.put(key, prob);
        }


        for (int i = 0; i < populationSize; ) {
            double selectProb = Math.random();
            for (Integer key : indivProbMap.keySet()) {
                if (selectProb < indivProbMap.get(key)) {
                    newGeneration.population.put(i, duringEvoGeneration.population.get(key));
                    System.out.println("During evolution added, individual number: " + key);
                    i++;
                    break;
                }
                selectProb -= indivProbMap.get(key);
            }
        }
        //x = random()
        //for indiv, prob in indivProbMap {
        //   if x < prob {
        //        return indiv
        //    }
        //    x -= prob
        //}
        System.out.println(newGeneration.theBest());

        return newGeneration;
    }

    void crossing(Individual mom, Individual dad) {
        Random random = new Random();
        int chromosomeCut = random.nextInt(mom.chromosome.length - 1) + 1;
        for (int i = chromosomeCut; i < mom.chromosome.length; i++) {
            mom.chromosome[i] = dad.chromosome[i];
        }
        for (int i = 0; i < chromosomeCut; i++) {
            dad.chromosome[i] = mom.chromosome[i];
        }
    }

    void generateUniquePairs() {
        List<Integer> keys = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            keys.add(i);
        }

        Collections.shuffle(keys);

        for (int i = 0; i < keys.size(); i += 2) {
            int key = keys.get(i);
            int value = keys.get(i + 1);
            parents.put(key, value);
        }
    }

    int fxMin() {
        int Fx_min = 0;
        for (Integer key : population.keySet()) {
            int x = population.get(key).chromosomeToDigital();
            if (function.fx(x) < Fx_min) {
                Fx_min = function.fx(x);
            }
        }
        return Fx_min;
    }

    int sumFxPositive() {
        int sumFx = 0;
        for (Integer key : population.keySet()) {
            int x = population.get(key).chromosomeToDigital();
            sumFx += function.fx(x) - fxMin() + 1;
        }
        return sumFx;
    }

    String theBest() {
        double fx;
        double fx_best = 0;
        int x;
        int x_best = 0;
        for (Integer key : population.keySet()) {
            x = population.get(key).chromosomeToDigital();
            if (function.fx(x) > fx_best) {
                fx_best = function.fx(x);
                x_best = x;
            }
        }
        return "f(best)= " + fx_best + " best= " + x_best + "\n";
    }

    void printout() {
        for (Integer key : population.keySet()) {
            population.get(key).printChromosome();
            System.out.println(population.get(key).chromosomeToDigital());
        }
    }
}
