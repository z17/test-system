package test_system.lab;

import org.apache.commons.math3.complex.Complex;
import test_system.lab.helper.BmpHelper;
import test_system.lab.helper.FunctionalHelper;
import test_system.lab.helper.MathHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class HolographyLab implements LabStrategy {
    public final static String FILE_KEY = "lab-file";
    public final static String L_HOLO_KEY = "holo-l";
    public final static String A_HOLO_KEY = "holo-a";
    public final static String D_HOLO_KEY = "holo-d";

    public final static String L_REPAIR_KEY = "repair-l";
    public final static String A_REPAIR_KEY = "repair-a";
    public final static String D_REPAIR_KEY = "repair-d";

    private Complex[][] hc = null;

    @Override
    public HolographyLabData process(final Map<String, String> data, final Path pathToOutFolder, final String outPrefixName) {

        Path inputFile = Paths.get(data.get(FILE_KEY));
        Integer[][] image = BmpHelper.readBmp(inputFile);
        double holoL = Double.valueOf(data.get(L_HOLO_KEY)) * Math.pow(10, -9);
        double holoA = Double.valueOf(data.get(A_HOLO_KEY)) * Math.pow(10, -6);
        double holoD = Double.valueOf(data.get(D_HOLO_KEY));

        double repairL = Double.valueOf(data.get(L_REPAIR_KEY)) * Math.pow(10, -9);
        double repairA = Double.valueOf(data.get(A_REPAIR_KEY)) * Math.pow(10, -6);
        double repairD = Double.valueOf(data.get(D_REPAIR_KEY));

        Double[][] holo = holoSynthesis(image, holoL, holoD, holoA);
        Double[][] restoredHolo = holoRestore(repairL, repairD, repairA);

        final Path holoFile = pathToOutFolder.resolve(outPrefixName + "holography.bmp");
        final Path holoRestoredFile = pathToOutFolder.resolve(outPrefixName + "restored-holography.bmp");

        BmpHelper.writeBmp(holoFile, holo);
        BmpHelper.writeBmp(holoRestoredFile, restoredHolo);
        final Double coefficient = countCorrelationCoefficient(restoredHolo, image);
        return new HolographyLabData(inputFile,
                Double.valueOf(data.get(L_HOLO_KEY)),
                Double.valueOf(data.get(A_HOLO_KEY)),
                Double.valueOf(data.get(D_HOLO_KEY)),
                Double.valueOf(data.get(L_REPAIR_KEY)),
                Double.valueOf(data.get(A_REPAIR_KEY)),
                Double.valueOf(data.get(D_REPAIR_KEY)),
                holoFile,
                holoRestoredFile,
                coefficient.isNaN() ? 0 : coefficient);
    }

    private Double[][] holoSynthesis(final Integer[][] image, double L, double d, double a) {
        int X = FunctionalHelper.cols(image);
        int Y = FunctionalHelper.rows(image);

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

        this.hc = hc;

        return normalize(hac, Y, X);
    }

    private Double[][] holoRestore(double L, double d, double a) {

        int X = FunctionalHelper.cols(hc);
        int Y = FunctionalHelper.rows(hc);

        int fullX = X * 2;
        int fullY = Y * 2;

        Complex[][] hb = new Complex[fullX][fullY];
        for (int i = 0; i <= 2 * X - 1; i++) {
            for (int j = 0; j <= 2 * Y - 1; j++) {
                hb[i][j] = new Complex(0);
            }
        }

        for (int i = Y / 2; i <= Y + Y / 2 - 1; i++) {
            for (int j = X / 2; j <= X + X / 2 - 1; j++) {
                hb[j][i] = hc[j - Y / 2][i - X / 2];
            }
        }

        Complex[][] W = new Complex[fullX][fullY];
        for (int y1 = 0; y1 < fullX; y1++) {
            for (int x1 = 0; x1 < fullY; x1++) {
                double v = (-Math.PI * a * a / (L * d)) * (Math.pow((x1 - 2 * X / 2), 2) + Math.pow((y1 - 2 * Y / 2), 2));
                W[y1][x1] = new Complex(0, v).exp();
            }
        }
        for (int y1 = 0; y1 < fullX; y1++) {
            for (int x1 = 0; x1 < fullY; x1++) {
                hb[y1][x1] = hb[y1][x1].multiply(W[y1][x1]);
            }
        }

        Complex[][] F = MathHelper.cfft(hb);

        Complex[][] FF = new Complex[fullX][fullY];
        for (int y1 = 0; y1 < fullX; y1++) {
            for (int x1 = 0; x1 < fullY; x1++) {
                Complex v1 = new Complex(0, 1 / L / d);
                Complex v2 = new Complex(0, -2 * Math.PI / L * d).exp();
                FF[y1][x1] = v1.multiply(v2).multiply(W[y1][x1]).multiply(F[y1][x1]);
            }
        }

        Double[][] FFDouble = new Double[fullX][fullY];

        for (int y1 = 0; y1 < fullX; y1++) {
            for (int x1 = 0; x1 < fullY; x1++) {
                FFDouble[y1][x1] = FF[y1][x1].abs();
            }
        }

        FFDouble = normalize(FFDouble, 2 * Y, 2 * X);

        return FFc(Y, X, FFDouble);
    }


    private Double[][] normalize(final Double[][] matrix, final int cols, final int rows) {
        Double[][] result = new Double[rows][cols];

        Double min = FunctionalHelper.min(matrix);
        Double max = FunctionalHelper.max(matrix);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = (matrix[i][j] - min) / (max - min) * 255;
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
            System.arraycopy(FF[j - 2 * Y / 4], 0, res[j], 0, 2 * X - 1 + 1);
        }

        for (int j = 0; j <= 2 * Y / 4 - 1; j++) {
            System.arraycopy(FF[j + 3 * 2 * Y / 4], 0, res[j], 0, 2 * X - 1 + 1);
        }

        for (int j = 0; j <= 2 * (Y - 1); j++) {
            System.arraycopy(res[j], 0, res1[j], 2 * X / 4, 2 * X - 2 * X / 4);
        }

        for (int j = 0; j <= 2 * Y - 1; j++) {
            System.arraycopy(res[j], 3 * 2 * X / 4, res1[j], 0, 2 * X / 4 - 1 + 1);
        }

        Double[][] result = new Double[Y][X];
        for (int i = 0; i < Y; i++) {
            System.arraycopy(res1[i], 0, result[i], 0, X);
        }

        return result;
    }

    private Double countCorrelationCoefficient(Double[][] first, Integer[][] second) {
        final int cols = FunctionalHelper.cols(first);
        final int rows = FunctionalHelper.rows(first);

        double meanIn = FunctionalHelper.mean(first);
        double meanOut = FunctionalHelper.mean(second);

        double sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double v = Math.abs(first[i][j] - meanIn) * Math.abs(second[i][j] - meanOut);
                sum += v;
            }
        }

        double stdevOne = MathHelper.stdev(first);
        double stdevTwo = MathHelper.stdev(second);

        return (double) 1 / cols / rows * sum / stdevOne / stdevTwo;
    }
}
