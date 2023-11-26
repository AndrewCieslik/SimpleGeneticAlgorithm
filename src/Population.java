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
    private double x_min;

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
            System.out.println("x: " + x);
            System.out.println(("fx: " + duringEvoGen.function.fx(x)));
            System.out.print("fxMin " + duringEvoGen.fxMin());
            System.out.println(", prob: " + duringEvoGen.countProb(x));
            System.out.print("SumFx: " + duringEvoGen.sumFx());
            System.out.println(" Key: " + key);

            indivProbMap.put(key, duringEvoGen.countProb(x));
            System.out.println("------------------------------------------");
        }
        System.out.println("fxMin: " + duringEvoGen.fxMin());

        for (Integer key : indivProbMap.keySet()) {
            System.out.println(key + " Indiv prob: " + indivProbMap.get(key));
        }

        for (int i = 0; i < populationSize; ) {
            double selectProb = Math.random();
            System.out.println("Random prob for : " + i + " " + selectProb);
            for (Integer key : indivProbMap.keySet()) {
                if (selectProb < indivProbMap.get(key)) {
                    newGeneration.population.put(i, duringEvoGen.population.get(key));
                    System.out.println("During evolution added, individual number: " + key);
                    i++;
                    break;
                }
                selectProb -= indivProbMap.get(key);
            }
            System.out.println("-----------------------------------------");
        }
        for (Integer key : newGeneration.population.keySet()) {
            System.out.println("New gen x: " + key + " Value: " + newGeneration.population.get(key).chromosomeToDigital());
        }
        double summmm = indivProbMap.get(0) + indivProbMap.get(1);
        System.out.println("Suma prawd: " + summmm);
        //x = random()
        //for indiv, prob in indivProbMap {
        //   if x < prob {
        //        return indiv
        //    }
        //    x -= prob
        //}
        return newGeneration;
    }

    double countProb(double x) {
        double fxPrim = function.fx(x) - fxMin() + 1;
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

    double fxMin() {
        x_min = population.get(0).chromosomeToDigital();
        double fx_min = function.fx(x_min);
        for (Integer key : population.keySet()) {
            double x = population.get(key).chromosomeToDigital();
            if (function.fx(x) < fx_min) {
                fx_min = function.fx(x);
                x_min = x;
            }
        }
        return fx_min;
    }

    double sumFx() {
        double sumFx = 0;
        for (Integer key : population.keySet()) {
            double x = population.get(key).chromosomeToDigital();
            double fxPrim = function.fx(x) - fxMin() + 1;
            sumFx += fxPrim;
        }
        return sumFx;
    }

    String theBest() {
        double fx;
        double fx_best = fxMin();
        double x;
        double x_best = x_min;
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
