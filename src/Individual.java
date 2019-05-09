import java.util.Random;

public class Individual {

    private static int defaultGeneLength = 11;
    private static Random generator = new Random();

    private byte[] genes = new byte[defaultGeneLength];

    void generateIndividual() {
        for (int i = 0; i < genes.length; i++) {
            byte gene = (byte) Math.abs(generator.nextInt() % 2);
            genes[i] = gene;
        }
    }

    byte[] getGenes() {
        return genes;
    }

    byte getGene(int index) {
        return genes[index];
    }

    void setGene(int index, byte value) {
        genes[index] = value;
    }

    @Override
    public String toString() {
        StringBuilder geneString = new StringBuilder();
        for (int i = 0; i < genes.length; i++) {
            geneString.append(getGene(i));
        }
        return geneString.toString();
    }

}
