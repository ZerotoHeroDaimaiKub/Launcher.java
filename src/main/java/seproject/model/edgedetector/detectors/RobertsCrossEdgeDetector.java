package seproject.model.edgedetector.detectors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import seproject.model.edgedetector.imagederivatives.ConvolutionKernel;
import seproject.model.edgedetector.imagederivatives.ImageConvolution;
import seproject.model.edgedetector.util.Grayscale;
import seproject.model.edgedetector.util.Threshold;

public class RobertsCrossEdgeDetector implements EdgeDetector {

    private boolean[][] edges;
    private int rows;
    private int columns;
    private int threshold;

    // RobertsCross Kernels for X and Y direction
    private static final double[][] X_KERNEL = {
            {1, 0},
            {0, -1}
    };

    private static final double[][] Y_KERNEL = {
            {0, -1},
            {1, 0}
    };

    @Override
    public File detectEdges(File imageFile) throws IOException {
        // Step 1: Load the image and convert to grayscale
        BufferedImage originalImage = ImageIO.read(imageFile);
        int[][] pixels = Grayscale.imgToGrayPixels(originalImage);

        // Step 2: Apply Gaussian Smoothing (Blur) to reduce noise
        ImageConvolution gaussianConvolution = new ImageConvolution(pixels, ConvolutionKernel.GAUSSIAN_KERNEL);
        int[][] smoothedImage = gaussianConvolution.getConvolvedImage();

        // Step 3: Apply Roberts Cross Operator (Gradient Calculation) on the smoothed image
        ImageConvolution x_ic = new ImageConvolution(smoothedImage, X_KERNEL);
        ImageConvolution y_ic = new ImageConvolution(smoothedImage, Y_KERNEL);

        int[][] x_imageConvolution = x_ic.getConvolvedImage();
        int[][] y_imageConvolution = y_ic.getConvolvedImage();

        rows = x_imageConvolution.length;
        columns = x_imageConvolution[0].length;

        // Step 4: Compute gradient magnitude for edge detection
        int[][] mag = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                mag[i][j] = (int) Math.hypot(x_imageConvolution[i][j], y_imageConvolution[i][j]);
            }
        }

        // Step 5: Apply Thresholding to find the edges
        edges = new boolean[rows][columns];
        threshold = Threshold.calcThresholdEdges(mag); // Automatically calculate a threshold
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                edges[i][j] = mag[i][j] >= threshold;
            }
        }

        // Step 6: Create output image showing the detected edges
        BufferedImage edgeImage = Threshold.applyThresholdReversed(edges);
        File result = new File("roberts_cross_edge_result.png");
        ImageIO.write(edgeImage, "png", result);
        return result;
    }

    public boolean[][] getEdges() {
        return edges;
    }
}