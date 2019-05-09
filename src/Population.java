import java.util.Arrays;

public class Population {

    private Individual[] individuals;

    Population(int populationSize, boolean initialise) {
        individuals = new Individual[populationSize];

        if (initialise) {
            for (int i = 0; i < individuals.length; i++) {
                Individual newIndividual = new Individual();
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
            }
        }
    }

    Individual getIndividual(int index) {
        return individuals[index];
    }

    void saveIndividual(int index, Individual individual) {
        individuals[index] = individual;
    }

    int getPopulationSize() {
        return individuals.length;
    }

    Individual[] getIndividuals() {
        return individuals;
    }

    @Override
    public String toString() {
        return Arrays.toString(individuals);
    }

}