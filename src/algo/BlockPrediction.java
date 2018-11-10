package algo;

public class BlockPrediction {

    int T_R = 2;
    int T_B = 3;


    public int calculatePredictionTime(String[] input) {
        int carCount = input.length - 1;
        return 2 * (carCount - 1) * T_R + T_B;
    }
}
