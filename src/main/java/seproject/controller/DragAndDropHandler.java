package seproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;

public class DragAndDropHandler {

    private final ImageView imageView;
    private final Label statusLabel;
    private final Label dropArea;
    private File selectedFile;
    private Image originalImage;

    private ZoomHandler zoomHandler;
    @FXML
    BorderPane imagePane;
    @FXML
    ScrollPane imageScrollPane;


    public DragAndDropHandler(ImageView imageView, Label statusLabel, Label dropArea,BorderPane imagePane,ScrollPane imageScrollPane) {
        this.imageView = imageView;
        this.statusLabel = statusLabel;
        this.dropArea = dropArea;
        this.imagePane = imagePane;
        this.imageScrollPane = imageScrollPane;
        this.zoomHandler = new ZoomHandler(imageView,imageScrollPane);
        setUpDragAndDrop();
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    private void setUpDragAndDrop() {
        dropArea.setOnDragOver(event -> {
            if (event.getGestureSource() != dropArea && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dropArea.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                selectedFile = db.getFiles().get(0);
                originalImage = new Image(selectedFile.toURI().toString());
                imageView.setImage(originalImage);
                statusLabel.setText("File loaded: " + selectedFile.getName());
                statusLabel.setStyle("-fx-text-fill: green;");

                zoomHandler.resetZoom();  // รีเซ็ตการซูมเมื่อโหลดภาพใหม่
                imagePane.minWidthProperty().bind(imageScrollPane.widthProperty());
                imagePane.minHeightProperty().bind(imageScrollPane.heightProperty());
            } else {
                statusLabel.setText("Drag and drop failed.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
            event.setDropCompleted(db.hasFiles());
            event.consume();
        });
    }

    // ฟังก์ชันเพื่อเปิด File Chooser กรณีที่ลากและวางไฟล์ไม่ได้
    public void chooseFileFallback() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            originalImage = new Image(selectedFile.toURI().toString());
            imageView.setImage(originalImage);
            statusLabel.setText("File loaded: " + selectedFile.getName());
            statusLabel.setStyle("-fx-text-fill: green;");
        } else {
            statusLabel.setText("File selection cancelled.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    public Image getOriginalImage() {
        return originalImage;
    }
}
