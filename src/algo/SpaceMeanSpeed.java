package algo;

public class SpaceMeanSpeed {

    public int calculateCost(String[] input) {
        int length = Integer.valueOf(input[input.length - 1]);
        int carCount = input.length - 1;

        double temp = 0;
        for (int i = 0; i < input.length - 1; i++) {
            System.out.println(Integer.valueOf(input[i]));
            temp += 1 / Double.valueOf(input[i]);
        }
        return (int) (length / (carCount / temp));
    }
}
