package test_system.lab;

import org.apache.commons.math3.complex.Complex;
import test_system.lab.helper.BmpHelper;
import test_system.lab.helper.FunctionalHelper;
import test_system.lab.helper.MathHelper;
import test_system.service.LabService;

import java.util.HashMap;
import java.util.Map;

public class HolographyLab implements LabStrategy {
    @Override
    public void process(Map<String, String> data) {

        String fileName = data.get(LabService.FILE_DATA_KEY);
        int[][] image = BmpHelper.readBmp(fileName);

        int X = FunctionalHelper.cols(image);
        int Y = FunctionalHelper.rows(image);

        double L = 632 * Math.pow(10, -9);
        double a = 6 * Math.pow(10, -6);
        double dc1 = Math.max(X, Y) * 2 * a * a / L;
        double d = 0.058;

        Complex[][] f = new Complex[X][Y];

        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {

                Complex v1 = new Complex(0, -1 / (L * d));
                Complex v2 = new Complex(0, 2 * Math.PI / L).exp();
                Complex v3 = new Complex(0, Math.PI * a * a / (L * d) * (Math.pow((x - X / 2), 2) + Math.pow(y - Y / 2, 2))).exp();

                f[x][y] = v1.multiply(v2).multiply(v3);
            }
        }

        Complex[][] u_f = MathHelper.cfft(image);
        Complex[][] f_f = MathHelper.cfft(f);

        Complex[][] h_f = FunctionalHelper.multiplyMatrix(u_f, f_f);
        Complex[][] h = MathHelper.iccft(h_f);
        Complex[][] hc = hc(X, Y, h);
        Double[][] hac = MathHelper.abs(hc);

        Double[][] hnc = hnc(hac);

        BmpHelper.writeBmp("out.bmp", hnc);
    }

    private Double[][] hnc(final Double[][] hac) {
        int cols = FunctionalHelper.cols(hac);
        int rows = FunctionalHelper.rows(hac);
        Double[][] result = new Double[rows][cols];

        Double min = FunctionalHelper.min(hac);
        Double max = FunctionalHelper.max(hac);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = (hac[i][j] - min) / (max - min) * 255;
            }
        }
        return result;
    }

    private Complex[][] hc(final int X, final int Y, Complex[][] h) {
        Complex[][] result = new Complex[Y][X];

        for (int j = 0; j <= Y / 2 - 1; j++) {
            System.arraycopy(h[j + Y / 2], 0, result[j], X / 2, X - X / 2);
        }

        for (int j = 0; j <= Y / 2 - 1; j++) {
            System.arraycopy(h[j + Y / 2], X / 2, result[j], 0, X / 2 - 1 + 1);
        }

        for (int j = Y / 2; j <= Y - 1; j++) {
            System.arraycopy(h[j - Y / 2], X / 2, result[j], 0, X / 2 - 1 + 1);
        }

        for (int j = Y / 2; j <= Y - 1; j++) {
            System.arraycopy(h[j - Y / 2], 0, result[j], X / 2, X - X / 2);
        }
        return result;

    }

    public static void main(String[] args) {
        Map<String, String> data = new HashMap<>();
        data.put(LabService.FILE_DATA_KEY, "test.bmp");
        new HolographyLab().process(data);
    }
}
