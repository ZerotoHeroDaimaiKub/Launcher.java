package seproject.model.edgedetector.imagederivatives;

public class ConvolutionKernel {

    // Default 5x5 Gaussian kernel with sigma = 1.4
    public static final double[][] GAUSSIAN_KERNEL = {{2/159.0, 4/159.0 , 5/159.0 , 4/159.0 , 2/159.0},
            {4/159.0, 9/159.0 , 12/159.0, 9/159.0 , 4/159.0},
            {5/159.0, 12/159.0, 15/159.0, 12/159.0, 5/159.0},
            {4/159.0, 9/159.0 , 12/159.0, 9/159.0 , 4/159.0},
            {2/159.0, 4/159.0 , 5/159.0 , 4/159.0 , 2/159.0}};

    // Larger Gaussian Kernel for better noise reduction
    public static final double[][] GAUSSIAN_KERNEL_LARGE = {
            {1/256.0, 4/256.0, 6/256.0, 4/256.0, 1/256.0},
            {4/256.0, 16/256.0, 24/256.0, 16/256.0, 4/256.0},
            {6/256.0, 24/256.0, 36/256.0, 24/256.0, 6/256.0},
            {4/256.0, 16/256.0, 24/256.0, 16/256.0, 4/256.0},
            {1/256.0, 4/256.0, 6/256.0, 4/256.0, 1/256.0}
    };

    /**
     * Generates a 1D averaging kernel with user-defined dimensions
     */
    public static double[] averagingKernel(int r) {
        double[] kernel = new double[r];
        double entry = 1.0 / r;

        for (int i = 0; i < r; i++)
            kernel[i] = entry;

        return kernel;
    }

    /**
     * Generates a 2D averaging kernel with user-defined dimensions
     */
    public static double[][] averagingKernel(int r, int c) {
        double[][] kernel = new double[r][c];
        double entry = 1.0 / (r * c);

        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                kernel[i][j] = entry;

        return kernel;
    }

    /**
     * Generates a 2D Gaussian kernel with user-defined size and sigma
     *
     * @param size the size of the kernel (odd number, e.g. 3, 5, 7)
     * @param sigma the standard deviation of the Gaussian distribution
     * @return a 2D Gaussian kernel
     */
    public static double[][] generateGaussianKernel(int size, double sigma) {
        double[][] kernel = new double[size][size];
        int halfSize = size / 2;
        double sum = 0.0;

        // Calculate the Gaussian kernel values
        for (int i = -halfSize; i <= halfSize; i++) {
            for (int j = -halfSize; j <= halfSize; j++) {
                kernel[i + halfSize][j + halfSize] = (1 / (2 * Math.PI * sigma * sigma)) *
                        Math.exp(-(i * i + j * j) / (2 * sigma * sigma));
                sum += kernel[i + halfSize][j + halfSize];
            }
        }

        // Normalize the kernel so that the sum of all elements is 1
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                kernel[i][j] /= sum;
            }
        }

        return kernel;
    }
}
