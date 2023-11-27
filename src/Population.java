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
    private int x_min;

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
        Population duringEvoGen = new Population(populationSize, chromosomeSize, mutationProb, crossProb, function);
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
            duringEvoGen.population.put(momIndex, mom);
            duringEvoGen.population.put(dadIndex, dad);
        }

        //2.Mutation
        for (Integer key : duringEvoGen.population.keySet()) {
            Individual individual = duringEvoGen.population.get(key).copy();
            individual.mutation();
            duringEvoGen.population.put(key, individual);
        }

        //3.Selection
        Map<Integer, Double> indivProbMap = new HashMap<>();
        for (Integer key : duringEvoGen.population.keySet()) {
            int x = duringEvoGen.population.get(key).chromosomeToDigital();
            indivProbMap.put(key, duringEvoGen.countProb(x));
        }

        for (int i = 0; i < populationSize; ) {
            double selectProb = Math.random();
            for (Integer key : indivProbMap.keySet()) {
                if (selectProb < indivProbMap.get(key)) {
                    newGeneration.population.put(i, duringEvoGen.population.get(key));
                    i++;
                    break;
                }
                selectProb -= indivProbMap.get(key);
            }
        }
        return newGeneration;
    }

    double countProb(int x) {
        int fxPrim = function.fx(x) - fxMin() + 1;
        return fxPrim / sumFx();
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
        x_min = population.get(0).chromosomeToDigital();
        int fx_min = function.fx(x_min);
        for (Integer key : population.keySet()) {
            int x = population.get(key).chromosomeToDigital();
            if (function.fx(x) < fx_min) {
                fx_min = function.fx(x);
                x_min = x;
            }
        }
        return fx_min;
    }

    double sumFx() {
        int sumFx = 0;
        for (Integer key : population.keySet()) {
            int x = population.get(key).chromosomeToDigital();
            int fxPrim = function.fx(x) - fxMin() + 1;
            sumFx += fxPrim;
        }
        return sumFx;
    }

    String theBest() {
        int fx;
        int fx_best = fxMin();
        int x;
        int x_best = x_min;
        for (Integer key : population.keySet()) {
            x = population.get(key).chromosomeToDigital();
            if (function.fx(x) > fx_best) {
                fx_best = function.fx(x);
                x_best = x;
            }
        }
        return "f(best)= " + fx_best + " best= " + x_best + "\n";
    }

    void printOut() {
        System.out.println("Generation:");
        for (int i = 0; i < populationSize; i++) {
            System.out.println(population.get(i).chromosomeToDigital());
        }
    }
}
