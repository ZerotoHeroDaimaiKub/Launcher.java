package seproject.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFileHandler {

    private final ImageView imageView;
    private Label statusLabel;
    private File selectedFile;
    private Image originalImage;
    private Image croppedImage;
    private Image processedImage;
    private String originalFilename;
    private ZoomHandler zoomHandler;
    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    private BorderPane imagePane;

    public ImageFileHandler(ImageView imageView, Label statusLabel,BorderPane imagePane,ScrollPane imageScrollPane) {
        this.imageView = imageView;
        this.statusLabel = statusLabel;
        this.imagePane = imagePane;
        this.imageScrollPane = imageScrollPane;
        this.zoomHandler = new ZoomHandler(imageView,imageScrollPane);
    }

    // ตั้งค่า originalImage
    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    // ตั้งค่า croppedImage
    public void setCroppedImage(Image croppedImage) {
        this.croppedImage = croppedImage;
    }

    // ตั้งค่า processedImage
    public void setProcessedImage(Image processedImage) {
        this.processedImage = processedImage;
    }

    // โหลดไฟล์ภาพ
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            originalImage = new Image(selectedFile.toURI().toString());
            imageView.setImage(originalImage);
            originalFilename = selectedFile.getName();
            statusLabel.setText("File loaded: " + originalFilename);

            zoomHandler.resetZoom();  // รีเซ็ตการซูมเมื่อโหลดภาพใหม่
            imagePane.minWidthProperty().bind(imageScrollPane.widthProperty());
            imagePane.minHeightProperty().bind(imageScrollPane.heightProperty());
        } else {
            statusLabel.setText("File selection cancelled.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // บันทึกภาพ
    public void saveImage(Image imageToSave) {
        if (imageToSave == null) {
            statusLabel.setText("No image to save.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        if (originalFilename != null && !originalFilename.isEmpty()) {
            String fileNameWithoutExtension = generateNewFileName(selectedFile);
            fileChooser.setInitialFileName(fileNameWithoutExtension);
        }
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File saveFile = fileChooser.showSaveDialog(new Stage());

        if (saveFile != null) {
            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageToSave, null);
                ImageIO.write(bufferedImage, "png", saveFile);
                statusLabel.setText("Image saved successfully.");
                statusLabel.setStyle("-fx-text-fill: green;");
            } catch (IOException e) {
                e.printStackTrace();
                statusLabel.setText("Failed to save image.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    // คืนค่าเป็นภาพต้นฉบับ
    @FXML
    public void onRevertToOriginal() {
        if (originalImage != null) {
            imageView.setImage(originalImage);  // คืนค่าเป็นภาพต้นฉบับ
            croppedImage = null;  // ล้างข้อมูลภาพที่ถูกครอบ
            processedImage = null;  // ล้างข้อมูลภาพที่ถูกประมวลผล
            statusLabel.setText("Reverted to original image.");
            statusLabel.setStyle("-fx-text-fill: green;");
        } else {
            statusLabel.setText("No original image to revert to.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // สร้างชื่อไฟล์ใหม่เมื่อทำการบันทึก
    private String generateNewFileName(File originalFile) {
        String originalFileName = originalFile.getName();
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.')); // ชื่อไฟล์ไม่รวมสกุลไฟล์
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.')); // นามสกุลไฟล์

        String newFileName = baseName + "1" + extension;
        int counter = 1;

        while (new File(originalFile.getParent(), newFileName).exists()) {
            counter++;
            newFileName = baseName + counter + extension;
        }

        return newFileName;
    }

    // Getter สำหรับภาพต้นฉบับและภาพที่ถูกครอบ/ประมวลผล
    public Image getOriginalImage() {
        return originalImage;
    }

    public Image getCroppedImage() {
        return croppedImage;
    }

    public Image getProcessedImage() {
        return processedImage;
    }
}
