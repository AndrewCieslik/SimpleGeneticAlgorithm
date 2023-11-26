import java.util.Arrays;
import java.util.Random;

public class Individual {
    final boolean[] chromosome;
    private double mutationProb;

    Individual(int chromosomeSize, double mutationProb) {
        this.chromosome = new boolean[chromosomeSize];
        this.mutationProb = mutationProb;
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
            if (randomProbability < mutationProb) {
                chromosome[i] = !chromosome[i];
            }
        }
    }

    Individual copy() {
        Individual newInd = new Individual(chromosome.length, mutationProb);
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
