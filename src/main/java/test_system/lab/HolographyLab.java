package test_system.lab;

import test_system.lab.helper.BmpHelper;
import test_system.service.LabService;

import java.util.Map;

public class HolographyLab implements LabStrategy {
    @Override
    public void process(Map<String, String> data) {

        String fileName = data.get(LabService.FILE_DATA_KEY);
        int[][] image = BmpHelper.readBmp(fileName);

        int X = BmpHelper.cols(image);
        int Y = BmpHelper.rows(image);

        double L = 632 * Math.pow(10, -9);
        double a = 6 * Math.pow(10, -6);
        double dc1 = Math.max(X, Y) * 2 * a * a / L;
        double d = 0.058;

        int[][] f = new int[X-1][Y-1];
//
//        for (int x = 0; x < X; x++) {
//            for (int y = 0; y < Y; y++) {
//                f[x][y] = 1 /
//            }
//        }
    }

    private void cfft(int[][] matrix) {

    }
}
