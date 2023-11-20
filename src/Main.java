import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int populationSize = 18;
        int chromosomeSize = 8;
        int numOfIterations = 40;
        FileWriter writer = new FileWriter("results.txt");

        while (numOfIterations > 0) {
            int numOfGenerations = 8;
            Population population = new Population(populationSize, chromosomeSize);
            Population newGeneration = new Population(populationSize, chromosomeSize);
            while (numOfGenerations > 0) {
                population.printout();
                newGeneration = population.evolution();
                newGeneration.printout();
                population = newGeneration;
                numOfGenerations--;
            }
            writer.write(newGeneration.theBest());
            numOfIterations--;
        }
        writer.close();
    }
}