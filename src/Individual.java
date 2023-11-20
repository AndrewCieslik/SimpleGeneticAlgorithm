import java.util.Arrays;
import java.util.Random;

public class Individual {
    final boolean[] chromosome;
    double mutation_Prob = 0.07;

    Individual(int chromosomeSize) {
        this.chromosome = new boolean[chromosomeSize];
    }

    void generateChromosome() {
        Random random = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] = random.nextBoolean();
        }
    }

    void mutation() {
        double randomProbability;
        for (int i = 0; i < chromosome.length; i++) {
            randomProbability = Math.random();
            if (randomProbability < mutation_Prob) {
                chromosome[i] = !chromosome[i];
            }
        }
    }

    Individual copy() {
        Individual newInd = new Individual(chromosome.length);
        for (int i = 0; i < chromosome.length; i++) {
            newInd.chromosome[i] = chromosome[i];
        }
        return newInd;
    }

    void printChromosome() {
        System.out.println(Arrays.toString(chromosome));
    }

    int chromosomeToDigital() {
        int digital = 0;
        for (int i = 0; i < chromosome.length; i++) {
            if (chromosome[i]) {
                digital += Math.pow(2, i);
            }
        }
        return digital;
    }
}
