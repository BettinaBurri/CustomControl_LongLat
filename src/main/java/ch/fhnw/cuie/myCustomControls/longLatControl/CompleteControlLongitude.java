package ch.fhnw.cuie.myCustomControls.longLatControl;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.converter.NumberStringConverter;

/**
 * Created by bettina & kathrin on 09.01.17.
 */
public class CompleteControlLongitude extends StackPane{

    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "style.css";

    private static final String ANGLE_DOWN = "\uf107";
    private static final String ANGLE_UP   = "\uf106";

    private Double minLongValue = new Double(-180);
    private Double maxLongValue = new Double(180);

    // all Parts
    private TextField textField;
    private Popup popup;
    private Pane dropDownChooser;
    private Button chooserButton;

    // Propertys
    private final DoubleProperty longitudeValue = new SimpleDoubleProperty();
    private LongitudeControl customControl;

    public CompleteControlLongitude(){
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
                if (newValue.matches("^(-){0,1}[0-9]*\\.?[0-9]{0,4}") && checkNumberRange(newValue, getMinLongValue(), getMaxLongValue())) {
                    double longlitudeValue = parseSignedDouble(newValue);
                } else {
                    textField.setText(oldValue);
                }
            }
        });

        customControl = new LongitudeControl();

        chooserButton = new Button(ANGLE_DOWN);
        chooserButton.getStyleClass().add("chooserButton");
        dropDownChooser = new DropDownChooserLongitude(customControl);

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
        Bindings.bindBidirectional(textField.textProperty(), customControl.valueProperty(), new NumberStringConverter());
        longitudeValueProperty().bindBidirectional(customControl.valueProperty());
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

    public double getLongitudeValue() {
        return longitudeValue.get();
    }

    public DoubleProperty longitudeValueProperty() {
        return longitudeValue;
    }

    public void setLongitudeValue(double longitudeValue) {
        this.longitudeValue.set(longitudeValue);
    }

    public Double getMinLongValue() {
        return minLongValue;
    }

    public void setMinLongValue(Double minLongValue) {
        this.minLongValue = minLongValue;
    }

    public Double getMaxLongValue() {
        return maxLongValue;
    }

    public void setMaxLongValue(Double maxLatValue) {
        this.maxLongValue = maxLongValue;
    }
}
