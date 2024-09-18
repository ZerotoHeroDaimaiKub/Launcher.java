package seproject.controller;

import seproject.model.crop.ResizableRectangle;
import seproject.model.edgedetector.detectors.EdgeDetector;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageController {

    @FXML
    private ImageView imageView;
    @FXML
    private ComboBox<String> algorithmChoice;
    @FXML
    private Label statusLabel;
    @FXML
    private BorderPane imagePane;
    @FXML
    public ScrollPane imageScrollPane;
    @FXML
    private Label dropArea;

    private ImageFileHandler imageFileHandler;
    private CropHandler cropHandler;
    private EdgeDetectionHandler edgeDetectionHandler;
    private ZoomHandler zoomHandler;
    private DragAndDropHandler dragAndDropHandler;

    private final Map<String, Class<? extends EdgeDetector>> edgeAlgorithms = new HashMap<>();


    @FXML
    private void initialize() {
//        edgeAlgorithms.put("Canny", model.edgedetector.detectors.CannyEdgeDetector.class);
//        edgeAlgorithms.put("Sobel", model.edgedetector.detectors.SobelEdgeDetector.class);
//        edgeAlgorithms.put("Laplacian", model.edgedetector.detectors.LaplacianEdgeDetector.class);
//        edgeAlgorithms.put("Prewitt", model.edgedetector.detectors.PrewittEdgeDetector.class);
//        edgeAlgorithms.put("Roberts Cross", model.edgedetector.detectors.RobertsCrossEdgeDetector.class);
//        edgeAlgorithms.put("Gaussian", model.edgedetector.detectors.GaussianEdgeDetector.class);

//        algorithmChoice.getItems().addAll(edgeAlgorithms.keySet());
//        algorithmChoice.setPromptText("Select Edge Detection Algorithm");

        imageFileHandler = new ImageFileHandler(imageView, statusLabel,imagePane,imageScrollPane);
        cropHandler = new CropHandler(imageView, imagePane, imageScrollPane);
        edgeDetectionHandler = new EdgeDetectionHandler(imageView, statusLabel,algorithmChoice,edgeAlgorithms);
        zoomHandler = new ZoomHandler(imageView, imageScrollPane);
        dragAndDropHandler = new DragAndDropHandler(imageView, statusLabel, dropArea,imagePane,imageScrollPane);
    }

    @FXML
    public void onChooseFile() {
        imageFileHandler.chooseFile();
    }

    @FXML
    public void onStartCrop() {
        cropHandler.startCrop();
    }

    @FXML
    public void onConfirmCrop() {
        cropHandler.confirmCrop();
    }

    @FXML
    public void onDetectEdges() {
        edgeDetectionHandler.detectEdges(algorithmChoice.getValue(), imageView.getImage());
    }

    @FXML
    public void onZoomIn() {
        zoomHandler.zoomIn();
    }

    @FXML
    public void onZoomOut() {
        zoomHandler.zoomOut();
    }

    @FXML
    public void onResetZoom() {
        zoomHandler.resetZoom();
    }
    @FXML
    public void onRevertToOriginal() {
        imageFileHandler.onRevertToOriginal();
    }
    @FXML
    public void onSaveImage() {
        imageFileHandler.saveImage(imageView.getImage());
    }


}