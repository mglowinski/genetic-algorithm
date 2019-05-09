import java.util.Map;

public class Main {

    private static final int POP_SIZE = 50;

    public static void main(String[] args) {
        Population population = new Population(POP_SIZE, true);

        for (int i = 0; i < 1000; i++) {
            population = Algorithm.evolvePopulation(population);
            System.out.println(population.toString());
        }

        Map<Double, Double> globalMax = SelectionCalculator.findGlobalMax(population);
        globalMax.keySet().stream().findFirst().ifPresent(xo -> System.out.println("xo=" + xo));
        globalMax.values().stream().findFirst().ifPresent(yo -> System.out.println("yo=" + yo));
    }

}