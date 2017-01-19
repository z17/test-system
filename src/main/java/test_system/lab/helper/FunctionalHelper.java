package test_system.lab.helper;

import org.apache.commons.math3.complex.Complex;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public final class FunctionalHelper {
    public static Complex[][] transformIntMatrixToComplex(Integer[][] matrix) {
        int cols = cols(matrix);
        int rows = rows(matrix);

        Complex[][] result = new Complex[rows][cols];
        for(int i = 0; i < rows; i++) {
            for (int j  = 0; j < cols; j++) {
                result[i][j] = new Complex(matrix[i][j], 0);
            }
        }
        return result;
    }

    public static <T> int cols(T[][] matrix) {
        long count = Arrays.stream(matrix).map(array -> array.length).distinct().count();
        if (count > 1) {
            throw new IllegalArgumentException("Not a matrix");
        }
        return matrix[0].length;
    }

    public static <T> int rows(T[][] matrix) {
        return matrix.length;
    }

    public static Complex[][] multiplyMatrix(Complex[][] a, Complex[][] b) {
        Complex[][] result = new Complex[cols(a)][rows(a)];
        for (int i = 0; i < a.length; i++) {
            for (int k = 0; k < a[i].length; k++) {
                result[i][k] = a[i][k].multiply(b[i][k]);
            }
        }
        return result;
    }

    public static Double min(Double[][] hac) {
        return Stream.of(hac).flatMap(Stream::of).mapToDouble(v -> v).min().orElse(0);
    }

    public static Double max(Double[][] hac) {
        return Stream.of(hac).flatMap(Stream::of).mapToDouble(v -> v).max().orElse(0);
    }

    public static <T extends Number> double mean(T[][] matrix) {
        double sum = 0;
        int count = 0;
        for (T[] aMatrix : matrix) {
            for (T anAMatrix : aMatrix) {
                sum += anAMatrix.doubleValue();
                count++;
            }
        }
        return sum / count;
    }
}
