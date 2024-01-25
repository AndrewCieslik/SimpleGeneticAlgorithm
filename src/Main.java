import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int populationSize = 18;
        int numOfGenerations = 8;
        int chromosomeSize = 8;
        double mutationProb = 0.9;
        double crossProb = 0.9;
        int numOfIterations = 40;
        int a = 1;
        int b = -250;
        int c = 10000;

        FileWriter writer = new FileWriter("resultsIncreasing.txt");
        Function function = new Function(a, b, c);

        while (numOfIterations > 0) {
            int numOfGen = numOfGenerations;
            Population population = new Population(populationSize, chromosomeSize, mutationProb, crossProb, function);
            Population newGeneration = new Population(populationSize, chromosomeSize, mutationProb, crossProb, function);
            while (numOfGen > 0) {
                newGeneration = population.evolution();
                population = newGeneration;
                numOfGen--;
            }
            writer.write(newGeneration.theBest());
            System.out.println(newGeneration.theBest());
            numOfIterations--;
        }
        writer.close();

        numOfIterations = 40;
        a = -1;
        b = 250;
        c = -10000;
        writer = new FileWriter("resultsDecreasing.txt");
        function = new Function(a, b, c);

        while (numOfIterations > 0) {
            int numOfGen = numOfGenerations;
            Population population = new Population(populationSize, chromosomeSize, mutationProb, crossProb, function);
            Population newGeneration = new Population(populationSize, chromosomeSize, mutationProb, crossProb, function);
            while (numOfGen > 0) {
                newGeneration = population.evolution();
                population = newGeneration;
                numOfGen--;
            }
            writer.write(newGeneration.theBest());
            System.out.println(newGeneration.theBest());
            numOfIterations--;
        }
        writer.close();
    }
}