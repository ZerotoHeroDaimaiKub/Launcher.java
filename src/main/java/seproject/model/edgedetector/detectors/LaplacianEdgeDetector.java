package seproject.model.edgedetector.detectors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import seproject.model.edgedetector.util.Threshold;
import seproject.model.edgedetector.imagederivatives.ConvolutionKernel;
import seproject.model.edgedetector.imagederivatives.ImageConvolution;
import seproject.model.edgedetector.util.Grayscale;

public class LaplacianEdgeDetector implements EdgeDetector {

    private boolean[][] edges;  // ผลลัพธ์ขอบภาพ
    private int threshold;      // ค่าธรชโฮลด์สำหรับขอบ
    private double[][] kernel = { // Laplacian kernel
            {-1, -1, -1},
            {-1,  8, -1},
            {-1, -1, -1}
    };

    // คอนสตรัคเตอร์แบบไม่มีพารามิเตอร์
    public LaplacianEdgeDetector() {}

    // คอนสตรัคเตอร์ที่รับไฟล์ภาพ
    public LaplacianEdgeDetector(String filePath) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(filePath));
        int[][] grayImage = Grayscale.imgToGrayPixels(originalImage);
        findEdges(grayImage);
    }

    // คอนสตรัคเตอร์ที่รับภาพเป็น array
    public LaplacianEdgeDetector(int[][] image) {
        findEdges(image);
    }

    // ฟังก์ชันค้นหาขอบภาพ
    private void findEdges(int[][] image) {
        ImageConvolution gaussianConvolution = new ImageConvolution(image, ConvolutionKernel.GAUSSIAN_KERNEL);
        int[][] smoothedImage = gaussianConvolution.getConvolvedImage();

        ImageConvolution laplacianConvolution = new ImageConvolution(smoothedImage, kernel);
        int[][] convolvedImage = laplacianConvolution.getConvolvedImage();

        int rows = convolvedImage.length;
        int columns = convolvedImage[0].length;

        // คำนวณ threshold เพื่อใช้ในการหาขอบ
        threshold = Threshold.calcThresholdEdges(convolvedImage);

        // กำหนดขอบภาพใน array
        edges = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                edges[i][j] = Math.abs(convolvedImage[i][j]) > threshold;
            }
        }
    }

    // ฟังก์ชันคืนค่า edges
    public boolean[][] getEdges() {
        return edges;
    }

    // ฟังก์ชันคืนค่า threshold
    public int getThreshold() {
        return threshold;
    }

    // ฟังก์ชัน detectEdges ที่ใช้ใน ImageController
    @Override
    public File detectEdges(File imageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile);
        int[][] grayImage = Grayscale.imgToGrayPixels(originalImage);

        LaplacianEdgeDetector detector = new LaplacianEdgeDetector(grayImage);
        boolean[][] edges = detector.getEdges();

        BufferedImage edgeImage = Threshold.applyThresholdReversed(edges);
        File result = new File("laplacian_result.png");
        ImageIO.write(edgeImage, "png", result);
        return result;
    }
}