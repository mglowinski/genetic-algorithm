import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Algorithm {

    private static final double pc = 0.25;
    private static final double pm = 0.01;

    private static Random generator = new Random();

    static Population evolvePopulation(Population population) {
        Population newPopulation = SelectionCalculator.select(population);

        List<Integer> individualsIndexesToCrossover = getIndividualsIndexesToCrossover(newPopulation);

        while (individualsIndexesToCrossover.size() >= 2) {
            int number1 = generator.nextInt(individualsIndexesToCrossover.size());
            int number2 = generator.nextInt(individualsIndexesToCrossover.size());

            if (number1 != number2) {
                crossover(newPopulation.getIndividuals()[individualsIndexesToCrossover.get(number1)],
                        newPopulation.getIndividuals()[individualsIndexesToCrossover.get(number2)]);

                individualsIndexesToCrossover.remove(number1);

                if (number2 >= number1) {
                    individualsIndexesToCrossover.remove(number2 - 1);
                } else {
                    individualsIndexesToCrossover.remove(number2);
                }
            }
        }

        for (int i = 0; i < newPopulation.getPopulationSize(); i++) {
            double number = generator.nextDouble();

            if (number < pm) {
                mutation(newPopulation.getIndividuals()[i]);
            }
        }

        return newPopulation;
    }

    private static List<Integer> getIndividualsIndexesToCrossover(Population population) {
        List<Integer> individualsIndexesToCrossover = new ArrayList<>();

        for (int i = 0; i < population.getPopulationSize(); i++) {
            double number = generator.nextDouble();

            if (number < pc) {
                individualsIndexesToCrossover.add(i);
            }
        }

        return individualsIndexesToCrossover;
    }

    private static void crossover(Individual firstIndividual, Individual secondIndividual) {
        int random = generator.nextInt(10) + 1;

        for (int i = 0; i < firstIndividual.getGenes().length; i++) {
            if (i > random) {
                byte gene1 = firstIndividual.getGene(i);
                byte gene2 = secondIndividual.getGene(i);

                firstIndividual.setGene(i, gene2);
                secondIndividual.setGene(i, gene1);
            }
        }
    }

    private static void mutation(Individual individual) {
        int random = generator.nextInt(11);

        for (int i = 0; i < individual.getGenes().length; i++) {
            if (i == random) {
                byte gene = individual.getGene(i);

                if (gene == 0) {
                    gene = 1;
                } else if (gene == 1) {
                    gene = 0;
                }

                individual.setGene(i, gene);
            }
        }
    }

}