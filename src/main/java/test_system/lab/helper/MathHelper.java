package test_system.lab.helper;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public final class MathHelper {
    public static Complex[] cfft(final double[] array) {
        final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.UNITARY);
        return transformer.transform(array, TransformType.INVERSE);
    }

    public static Complex[][] cfft(final int[][] matrix) {
        Complex[][] m = FunctionalHelper.transformIntMatrixToComplex(matrix);
        return  cfft(m);
    }

    public static Complex[][] cfft(final Complex[][] matrix) {
        final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.UNITARY);
        return  (Complex[][]) transformer.mdfft(matrix, TransformType.INVERSE);
    }

    public static Complex[] iccft(final double[] array) {
        final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.UNITARY);
        return transformer.transform(array, TransformType.FORWARD);
    }

    public static Complex[][] icfft(final int[][] matrix) {
        Complex[][] m = FunctionalHelper.transformIntMatrixToComplex(matrix);

        final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.UNITARY);
        return  (Complex[][]) transformer.mdfft(m, TransformType.FORWARD);
    }
}
