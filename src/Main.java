import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final int POP_SIZE = 50;

    public static void main(String[] args) {
        Population population = new Population(POP_SIZE, true);
        Map<Integer, ValuesPair> results = new HashMap<>();

        for (int i = 0; i < 150; i++) {
            population = Algorithm.evolvePopulation(population);
            Map<Double, Double> globalMax = SelectionCalculator.findGlobalMax(population);

            ValuesPair valuesPair = new ValuesPair(globalMax.keySet().stream().findFirst().get(),
                    globalMax.values().stream().findFirst().get());

            results.put(i, valuesPair);
        }

        Map.Entry<Integer, ValuesPair> maxEntry = null;
        int index = 0;
        int counter = 0;

        for (Map.Entry<Integer, ValuesPair> entry : results.entrySet()) {
            if (maxEntry == null || entry.getValue().getY().compareTo(maxEntry.getValue().getY()) > 0) {
                maxEntry = entry;
                index = counter;
            }
            counter++;
        }

        System.out.println("xo=" + maxEntry.getValue().getX());
        System.out.println("yo=" + maxEntry.getValue().getY());
        System.out.println("population number=" + index);

        results.values().forEach(valuesPair -> System.out.println(valuesPair.getX()));
        System.out.println();
        results.values().forEach(valuesPair -> System.out.println(valuesPair.getY()));
    }

}