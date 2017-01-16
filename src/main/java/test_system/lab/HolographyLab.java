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

                Complex v1 = new Complex(0, - 1 / (L * d));
                Complex v2 = new Complex(0, 2 * Math.PI / L).exp();
                Complex v3 = new Complex(0, Math.PI * a * a / (L * d) * (Math.pow((x - X / 2), 2) + Math.pow(y - Y / 2, 2))).exp();

                f[x][y] = v1.multiply(v2).multiply(v3);
            }
        }

        Complex[][] u_f = MathHelper.cfft(image);
        Complex[][] f_f = MathHelper.cfft(f);

        Complex[][] h_f = FunctionalHelper.multiplyMatrix(u_f, f_f);
    }

    public static void main(String[] args) {
        Map<String, String> data = new HashMap<>();
        data.put(LabService.FILE_DATA_KEY, "test.bmp");
        new HolographyLab().process(data);
    }
}
