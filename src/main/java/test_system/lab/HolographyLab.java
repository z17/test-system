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


        // todo: use another name, dont re-use
        h = hc;

        Complex[][] hb = new Complex[2 * X][2 * Y];
        for (int i = 0; i <= 2 * X - 1; i++) {
            for (int j = 0; j <= 2 * Y - 1; j++) {
                hb[i][j] = new Complex(0);
            }
        }

        for (int i = Y / 2; i <= Y + Y / 2 - 1; i++) {
            for (int j = X / 2; j <= X + X / 2 - 1; j++) {
                hb[j][i] = h[j - Y / 2][i - X / 2];
            }
        }

        Complex[][] W = new Complex[2 * X][2 * Y];
        for (int y1 = 0; y1 <= 2 * X - 1; y1++) {
            for (int x1 = 0; x1 <= 2 * Y - 1; x1++) {
                double v = (-Math.PI * a * a / (L * d)) * (Math.pow((x1 - 2 * X / 2), 2) + Math.pow((y1 - 2 * Y / 2), 2));
                W[y1][x1] = new Complex(0, v).exp();
            }
        }
        for (int y1 = 0; y1 <= 2 * X - 1; y1++) {
            for (int x1 = 0; x1 <= 2 * Y - 1; x1++) {
                hb[y1][x1] = hb[y1][x1].multiply(W[y1][x1]);
            }
        }

        Complex[][] F = MathHelper.cfft(hb);

        Complex[][] FF = new Complex[2 * X][2 * Y];
        for (int y1 = 0; y1 <= 2 * X - 1; y1++) {
            for (int x1 = 0; x1 <= 2 * Y - 1; x1++) {
                Complex v1 = new Complex(0, 1 / L / d);
                Complex v2 = new Complex(0, -2 * Math.PI / L * d).exp();
                FF[y1][x1] = v1.multiply(v2).multiply(W[y1][x1]).multiply(F[y1][x1]);
            }
        }

        Double[][] FFDouble = new Double[2 * X][2 * Y];

        for (int y1 = 0; y1 <= 2 * X - 1; y1++) {
            for (int x1 = 0; x1 <= 2 * Y - 1; x1++) {
                FFDouble[y1][x1] = FF[y1][x1].abs();
            }
        }

        Double min = FunctionalHelper.min(FFDouble);
        Double max = FunctionalHelper.max(FFDouble);
        for (int y1 = 0; y1 <= 2 * X - 1; y1++) {
            for (int x1 = 0; x1 <= 2 * Y - 1; x1++) {
                FFDouble[y1][x1] = (FFDouble[y1][x1] - min) / (max - min) * 255;
            }
        }

        Double[][] doubles = FFc(Y, X, FFDouble);

        Double[][]result = new Double[Y][X];
        for (int i = 0; i < Y; i++) {
            for(int j = 0; j < X; j++) {
                result[i][j] = doubles[i][j];
            }
        }

        BmpHelper.writeBmp("qwdqwsdvsdv.bmp", result);

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

    private Double[][] FFc(final int Y, final int X, Double[][] FF) {
        Double[][] res = new Double[2 * Y][2 * X];
        Double[][] res1 = new Double[2 * Y][2 * X];
        for (int j = 2 * Y / 4; j <= 2 * Y - 1; j++) {
            for (int i = 0; i <= 2 * X - 1; i++) {
                res[j][i] = FF[j - 2 * Y / 4][i];
            }
        }

        for (int j = 0; j <= 2 * Y / 4 - 1; j++) {
            for (int i = 0; i <= 2 * X - 1; i++) {
                res[j][i] = FF[j + 3 * 2 * Y / 4][i];
            }
        }

        for (int j = 0; j <= 2 * (Y - 1); j++) {
            for (int i = 2 * X / 4; i <= 2 * X - 1; i++) {
                res1[j][i] = res[j][i - 2 * X / 4];
            }
        }

        for (int j = 0; j <= 2 * Y - 1; j++) {
            for (int i = 0; i <= 2 * X / 4 - 1; i++) {
                res1[j][i] = res[j][i + 3 * 2 * X / 4];
            }
        }

        return res1;
    }

    public static void main(String[] args) {
        Map<String, String> data = new HashMap<>();
        data.put(LabService.FILE_DATA_KEY, "test.bmp");
        new HolographyLab().process(data);
    }
}
