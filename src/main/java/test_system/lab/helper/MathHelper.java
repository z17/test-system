package test_system.lab.helper;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.stream.DoubleStream;

public final class MathHelper {
    public static Complex[][] cfft(final Integer[][] matrix) {
        Complex[][] m = FunctionalHelper.transformIntMatrixToComplex(matrix);
        return  cfft(m);
    }

    public static Complex[][] cfft(final Complex[][] matrix) {
        final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.UNITARY);
        return  (Complex[][]) transformer.mdfft(matrix, TransformType.INVERSE);
    }

    public static Complex[][] iccft(final Complex[][] matrix) {
        final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.UNITARY);
        return  (Complex[][]) transformer.mdfft(matrix, TransformType.FORWARD);
    }

    public static Double[][] abs(final Complex[][] matrix) {
        int cols = FunctionalHelper.cols(matrix);
        int rows = FunctionalHelper.rows(matrix);

        Double[][] result = new Double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                try {
                    result[i][j] = matrix[i][j].abs();
                } catch (Exception e) {
                    System.out.println();
                }
            }
        }
        return result;
    }
}
