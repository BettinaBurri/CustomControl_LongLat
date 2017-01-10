package ch.fhnw.cuie.myCustomControls.longLatControl;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.converter.NumberStringConverter;

import javax.xml.soap.Text;

/**
 * Created by bettina on 09.01.17.
 */
public class CompleteControlLatitude extends StackPane{

    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "style.css";

    private static final String ANGLE_DOWN = "\uf107";
    private static final String ANGLE_UP   = "\uf106";

    private static final double ARTBOARD_WIDTH  = 300;
    private static final double ARTBOARD_HEIGHT = 300;


    private Double minLatValue = new Double(-90);
    private Double maxLatValue = new Double(90);

    // all Parts
    private TextField textField;
    private Popup popup;
    private Pane dropDownChooser;
    private Button chooserButton;

    // Propertys
    private final DoubleProperty latitudeValue    = new SimpleDoubleProperty(40.0);
    private LatitudeControl customControl;

    public CompleteControlLatitude(){
        initializeSelf();
        initializeParts();
        layoutParts();
        addEventHandler();
        setupBindings();
    }

    private void initializeSelf(){
        addStyleSheets(this);
        getStyleClass().add(getStyleClassName());
    }

    private void initializeParts(){
        textField = new TextField();
        textField.setPrefWidth(250);

        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("^(-){0,1}[0-9]*\\.?[0-9]{0,4}") && checkNumberRange(newValue, getMinLatValue(), getMaxLatValue())) {
                    double latitudeValue = parseSignedDouble(newValue);
                } else {
                    textField.setText(oldValue);
                }
            }
        });

        customControl = new LatitudeControl();

        chooserButton = new Button(ANGLE_DOWN);
        chooserButton.getStyleClass().add("chooserButton");
        dropDownChooser = new DropDownChooserLatitude(customControl);

        popup = new Popup();
        popup.getContent().addAll(dropDownChooser);

    }

    private void layoutParts(){
        getChildren().addAll(textField, chooserButton);
        this.setAlignment(textField, Pos.CENTER_LEFT);
        this.setAlignment(chooserButton, Pos.CENTER_RIGHT);

    }

    private void addEventHandler(){
        chooserButton.setOnAction(event -> {
            if (popup.isShowing()) {
                popup.hide();
            } else {
                popup.show(textField.getScene().getWindow());
            }
        });

        popup.setOnHidden(event -> chooserButton.setText(ANGLE_DOWN));
        popup.setOnShown(event -> {
            chooserButton.setText(ANGLE_UP);
            Point2D location = textField.localToScreen(textField.getWidth() - dropDownChooser.getPrefWidth() - 3, textField.getHeight() -3);
            popup.setX(location.getX());
            popup.setY(location.getY());
        });
    }
    private void setupBindings(){
        //Bindings.bindBidirectional(dropDownChooser.getLatitudeControl(), latitudeValueProperty());
        //latitudeValueProperty().bindBidirectional();
        Bindings.bindBidirectional(textField.textProperty(), customControl.valueProperty(), new NumberStringConverter());
    }
    private boolean checkNumberRange(String value, Double minValue, Double maxValue){
        boolean positiveNumber = true;
        if (value.startsWith("-")) {
            value = value.substring(1);
            positiveNumber = false;
        }
        double doubleValue = Double.parseDouble(value);

        if(positiveNumber){
            return doubleValue>=minValue && doubleValue<=maxValue;
        }
        else {
            return -doubleValue>=minValue && -doubleValue<=maxValue;
        }
    }

    private double parseSignedDouble(String value){
        boolean positiveNumber = true;
        if (value.startsWith("-")) {
            value = value.substring(1);
            positiveNumber = false;
        }
        double doubleValue = Double.parseDouble(value);

        if(positiveNumber){
            return doubleValue;
        }
        else {
            return -doubleValue;
        }
    }

    private void addStyleSheets(Parent parent) {
        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        parent.getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        parent.getStylesheets().add(stylesheet);
    }

    private String getStyleClassName() {
        String className = this.getClass().getSimpleName();

        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    public double getLatitudeValue() {
        return latitudeValue.get();
    }

    public DoubleProperty latitudeValueProperty() {
        return latitudeValue;
    }

    public void setLatitudeValue(double latitudeValue) {
        this.latitudeValue.set(latitudeValue);
    }

    public Double getMinLatValue() {
        return minLatValue;
    }

    public void setMinLatValue(Double minLatValue) {
        this.minLatValue = minLatValue;
    }

    public Double getMaxLatValue() {
        return maxLatValue;
    }

    public void setMaxLatValue(Double maxLatValue) {
        this.maxLatValue = maxLatValue;
    }
}
