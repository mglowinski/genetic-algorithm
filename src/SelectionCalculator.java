import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

class SelectionCalculator {

    static Population select(Population population) {
        BigDecimal[] evals = new BigDecimal[population.getPopulationSize()];

        for (int i = 0; i < population.getIndividuals().length; i++) {
            evals[i] = calculateEval(population.getIndividual(i));
        }
        evals = normalize(evals);

        BigDecimal sumOfEvals = sumOfEvals(evals).setScale(3, RoundingMode.CEILING);
        BigDecimal[] probabilities = calculateProbabilities(evals, sumOfEvals);

        return prepareNewPopulation(population, probabilities);
    }

    private static Population prepareNewPopulation(Population population,
                                                   BigDecimal[] probabilities) {
        Population newPopulation = new Population(population.getPopulationSize(), false);

        for (int i = 0; i < population.getPopulationSize(); i++) {
            double number = Math.round(Math.random() * 1000d) / 1000d;

            BigDecimal sumA = BigDecimal.ZERO;
            BigDecimal sumB = BigDecimal.ZERO;
            BigDecimal maxDifference = BigDecimal.ZERO;
            int index = 0;

            for (int j = 1; j < probabilities.length; j++) {
                sumA = sumA.add(probabilities[j - 1]);
                sumB = sumB.add(probabilities[j]);

                if (number > sumA.doubleValue() && number <= sumB.doubleValue()) {
                    newPopulation.saveIndividual(i, population.getIndividual(j - 1));
                    break;
                }

                if (sumB.subtract(sumA).doubleValue() > maxDifference.doubleValue()) {
                    maxDifference = sumB.subtract(sumA);
                    index = j - 1;
                }
            }

            if (newPopulation.getIndividual(i) == null) {
                newPopulation.saveIndividual(i, population.getIndividual(index));
            }
        }

        return newPopulation;
    }

    static Map<Double, Double> findGlobalMax(Population population) {
        Map<Double, Double> map = new HashMap<>();
        for (int i = 0; i < population.getPopulationSize(); i++) {
            calculateMapEval(map, population.getIndividual(i));
        }
        return getMax(map);
    }

    private static Map<Double, Double> getMax(Map<Double, Double> map) {
        Map<Double, Double> maxValues = new HashMap<>();
        double maxYValue = Double.MIN_VALUE;
        double maxXValue = 0;

        for (Map.Entry<Double, Double> entry : map.entrySet()) {
            if (entry.getValue() > maxYValue) {
                maxYValue = entry.getValue();
                maxXValue = entry.getKey();
            }
        }

        maxValues.put(maxXValue, maxYValue);
        return maxValues;
    }

    private static void calculateMapEval(Map<Double, Double> map,
                                         Individual individual) {
        BigDecimal xValue = getXValue(individual.getGenes());
        BigDecimal yValue = getFunctionValue(xValue);
        map.put(xValue.doubleValue(), yValue.doubleValue());
    }

    private static BigDecimal calculateEval(Individual individual) {
        BigDecimal xValue = getXValue(individual.getGenes());
        return getFunctionValue(xValue);
    }

    private static BigDecimal getXValue(byte[] genes) {
        int xValue = binaryToDecimal(genes);
        return BigDecimal.valueOf(xValue)
                .multiply(BigDecimal.valueOf(2))
                .divide(BigDecimal.valueOf(2047), 3, BigDecimal.ROUND_CEILING)
                .add(BigDecimal.valueOf(0.5));
    }

    private static int binaryToDecimal(byte[] genes) {
        String binaryString = convertByteArrayToString(genes);
        return Integer.parseInt(binaryString, 2);
    }

    private static String convertByteArrayToString(byte[] genes) {
        StringBuilder builder = new StringBuilder();
        for (byte gene : genes) {
            builder.append(gene);
        }
        return builder.toString();
    }

    private static BigDecimal getFunctionValue(BigDecimal x) {
        BigDecimal numerator = BigDecimal.valueOf(exp(x.doubleValue()))
                .multiply(BigDecimal.valueOf(sin(10 * PI * x.doubleValue()))
                        .add(BigDecimal.ONE));
        return numerator.divide(x, 3, RoundingMode.CEILING);
    }

    private static BigDecimal[] calculateProbabilities(BigDecimal[] evals, BigDecimal sumOfEvals) {
        BigDecimal[] probabilities = new BigDecimal[evals.length + 1];
        probabilities[0] = BigDecimal.ZERO;

        for (int i = 1; i < probabilities.length; i++) {
            if (sumOfEvals.compareTo(BigDecimal.ZERO) == 0) {
                probabilities[i] = BigDecimal.ZERO;
            } else {
                probabilities[i] = evals[i - 1].divide(sumOfEvals, 3, RoundingMode.CEILING);
            }
        }

        return probabilities;
    }

    private static BigDecimal sumOfEvals(BigDecimal[] evals) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal eval : evals) {
            sum = sum.add(eval);
        }
        return sum;
    }

    private static BigDecimal[] normalize(BigDecimal[] evals) {
        BigDecimal minValue = evals[0];
        for (int i = 1; i < evals.length; i++) {
            if (evals[i].doubleValue() < minValue.doubleValue()) {
                minValue = evals[i];
            }
        }

        double absMinValue = abs(minValue.doubleValue());

        for (int i = 0; i < evals.length; i++) {
            evals[i] = evals[i].add(BigDecimal.valueOf(absMinValue));
        }

        return evals;
    }

}
