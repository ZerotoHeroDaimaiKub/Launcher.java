package seproject.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seproject.model.edgedetector.detectors.EdgeDetector;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EdgeDetectionHandler {

    private final ImageView imageView;
    private ComboBox<String> algorithmChoice;
    private Map<String, Class<? extends EdgeDetector>> edgeAlgorithms = new HashMap<>();
    private final Label statusLabel;

    private Image originalImage;
    private Image croppedImage;
    private Image processedImage;
    public EdgeDetectionHandler(ImageView imageView, Label statusLabel, ComboBox<String> algorithmChoice,Map<String, Class<? extends EdgeDetector>> edgeAlgorithms) {
        this.imageView = imageView;
        this.statusLabel = statusLabel;
        this.algorithmChoice = algorithmChoice;
        this.edgeAlgorithms = edgeAlgorithms;
        initializeEdgeAlgorithms();
    }

    private void initializeEdgeAlgorithms() {
        edgeAlgorithms.put("Canny", seproject.model.edgedetector.detectors.CannyEdgeDetector.class);
        edgeAlgorithms.put("Sobel", seproject.model.edgedetector.detectors.SobelEdgeDetector.class);
        edgeAlgorithms.put("Laplacian", seproject.model.edgedetector.detectors.LaplacianEdgeDetector.class);
        edgeAlgorithms.put("Prewitt", seproject.model.edgedetector.detectors.PrewittEdgeDetector.class);
        edgeAlgorithms.put("Roberts Cross", seproject.model.edgedetector.detectors.RobertsCrossEdgeDetector.class);
        edgeAlgorithms.put("Gaussian", seproject.model.edgedetector.detectors.GaussianEdgeDetector.class);

        algorithmChoice.getItems().addAll(edgeAlgorithms.keySet());
        algorithmChoice.setPromptText("Select Edge Detection Algorithm");
    }

    public void detectEdges(String selectedAlgorithm, Image imageToProcess) {
        if (imageToProcess == null) {
            statusLabel.setText("Please load or crop an image first.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (selectedAlgorithm == null) {
            statusLabel.setText("Please select an edge detection algorithm.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            Class<? extends EdgeDetector> detectorClass = edgeAlgorithms.get(selectedAlgorithm);
            EdgeDetector detector = detectorClass.getDeclaredConstructor().newInstance();

            File tempImageFile = new File("temp_image.png");
            ImageIO.write(SwingFXUtils.fromFXImage(imageToProcess, null), "png", tempImageFile);
            File resultFile = detector.detectEdges(tempImageFile);
            Image processedImage = new Image(resultFile.toURI().toString());
            imageView.setImage(processedImage);
            statusLabel.setText("Edge detection completed.");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Edge detection failed.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    public void onDetectEdges() {
        // ตรวจสอบว่ามีภาพถูกโหลดหรือครอบแล้วหรือไม่
        if (croppedImage == null && originalImage == null) {
            statusLabel.setText("Please load or crop an image first.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // เลือกภาพที่ถูกครอบหรือภาพต้นฉบับ
        Image imageToProcess = (croppedImage != null) ? croppedImage : originalImage;

        // ตรวจสอบว่ามีการเลือกอัลกอริธึมการตรวจจับขอบหรือไม่
        if (algorithmChoice.getValue() == null) {
            statusLabel.setText("Please select an edge detection algorithm.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            // เรียกใช้อัลกอริธึมที่เลือกสำหรับการตรวจจับขอบ
            Class<? extends EdgeDetector> detectorClass = edgeAlgorithms.get(algorithmChoice.getValue());
            EdgeDetector detector = detectorClass.getDeclaredConstructor().newInstance();

            // แปลงภาพเป็นไฟล์ชั่วคราวเพื่อนำไปประมวลผล
            File tempImageFile = new File("temp_image.png");
            ImageIO.write(SwingFXUtils.fromFXImage(imageToProcess, null), "png", tempImageFile);

            // เรียกใช้การตรวจจับขอบจากไฟล์ภาพ
            File resultFile = detector.detectEdges(tempImageFile);
            processedImage = new Image(resultFile.toURI().toString());

            // แสดงภาพที่ถูกประมวลผลบน ImageView
            imageView.setImage(processedImage);
            statusLabel.setText("Edge detection completed.");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Edge detection failed.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    public void setCroppedImage(Image croppedImage) {
        this.croppedImage = croppedImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    public Image getProcessedImage() {
        return processedImage;
    }

}

