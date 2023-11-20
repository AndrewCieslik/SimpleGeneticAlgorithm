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


    Population(int populationSize, int chromosomeSize) {
        this.population = new HashMap<>();
        this.parents = new HashMap<>();
        this.chromosomeSize = chromosomeSize;
        this.populationSize = populationSize;

        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(chromosomeSize);
            individual.generateChromosome();
            population.put(i, individual);
        }
    }

    Population evolution() {
        Population duringEvoGeneration = new Population(populationSize, chromosomeSize);
        Population newGeneration = new Population(populationSize, chromosomeSize);
        printout();

        generateUniquePairs();
        printoutParents();
        double randomProb;

        //1.Crossing
        for (Integer key : parents.keySet()) {
            randomProb = Math.random();
            int momIndex = key;
            int dadIndex = parents.get(key);
            System.out.println("Parents: " + key + " + " + parents.get(key) + " crossing probability : " + randomProb);

            Individual mom = population.get(momIndex).copy();
            Individual dad = population.get(dadIndex).copy();

            if (randomProb > 0.4) {
                System.out.println("Crossing started: Mom " + momIndex + " + Dad " + dadIndex);
                crossing(mom, dad);
                System.out.println("Crossing completed.");
            }
            duringEvoGeneration.population.put(momIndex, mom);
            duringEvoGeneration.population.put(dadIndex, dad);
        }

        //2.Mutation
        for (Integer key : duringEvoGeneration.population.keySet()) {
            randomProb = Math.random();
            Individual individual = duringEvoGeneration.population.get(key).copy();
            individual.mutation();
            System.out.println("Mutation of " + key + " completed.");
            duringEvoGeneration.population.put(key, individual);
        }

        //3.Selection
        double sumFx = 0;
        for (Integer key : duringEvoGeneration.population.keySet()) {
            double x = duringEvoGeneration.population.get(key).chromosomeToDigital();
            sumFx += Function.fxPositive(x);
        }

        Map<Integer, Double> indivProbMap = new HashMap<>();
        for (Integer key : duringEvoGeneration.population.keySet()) {
            double x = duringEvoGeneration.population.get(key).chromosomeToDigital();
            double prob = Function.fxPositive(x) / sumFx;
            indivProbMap.put(key, prob);
        }

        double sumProb = 0;
        for (Integer key : indivProbMap.keySet()) {
            sumProb += indivProbMap.get(key);
            System.out.println("Individual: " + key + "   " + "his probability: " + indivProbMap.get(key));
        }

        for (int i = 0; i < populationSize; ) {
            double selectProb = Math.random();
            System.out.println("Selection probability: " + selectProb);

            List<Integer> keys = new ArrayList<>(indivProbMap.keySet());
            Collections.shuffle(keys);

            for (Integer key : keys) {
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
        System.out.println("Chromosome cut: " + chromosomeCut);
        System.out.println("Before:");
        mom.printChromosome();
        dad.printChromosome();
        for (int i = chromosomeCut; i < mom.chromosome.length; i++) {
            mom.chromosome[i] = dad.chromosome[i];
        }
        for (int i = 0; i < chromosomeCut; i++) {
            dad.chromosome[i] = mom.chromosome[i];
        }
        System.out.println("After crossing:");
        mom.printChromosome();
        dad.printChromosome();
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

    void printout() {
        for (Integer key : population.keySet()) {
            population.get(key).printChromosome();
            System.out.println(population.get(key).chromosomeToDigital());
        }
    }

    void printoutParents() {
        for (Map.Entry<Integer, Integer> entry : parents.entrySet()) {
            System.out.println("Mom(Key): " + entry.getKey() + ", Dad(Value): " + entry.getValue());
        }
    }

    String theBest() {
        double fx;
        double fx_best = 0;
        int x;
        int x_best = 0;

        for (Integer key : population.keySet()) {
            x = population.get(key).chromosomeToDigital();
            fx = Function.fx(x);
            if (fx > fx_best) {
                fx_best = fx;
                x_best = x;
            }
        }
        return "f(best)= " + fx_best + " best= " + x_best + "\n";
    }
}
