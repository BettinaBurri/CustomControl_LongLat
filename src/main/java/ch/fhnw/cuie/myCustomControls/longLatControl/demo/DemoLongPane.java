package ch.fhnw.cuie.myCustomControls.longLatControl.demo;

import ch.fhnw.cuie.myCustomControls.longLatControl.CompleteControlLongitude;
import ch.fhnw.cuie.myCustomControls.longLatControl.LongitudeControl;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * @author Dieter Holz, Bettina Burri, Kathrin Koebel
 */
public class DemoLongPane extends BorderPane {
    //private LongitudeControl customControl;
    private CompleteControlLongitude businessControl;

    private TextField   valueInputField;
    private Slider      valueSlider;

    private ColorPicker colorPicker;

    public DemoLongPane() {
        initializeControls();
        layoutControls();
        addBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        businessControl = new CompleteControlLongitude();

        valueInputField = new TextField();
        valueInputField.setText("0.0");

        // limit input to numbers & check min/max value
        valueInputField.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("^(-){0,1}[0-9]*\\.?[0-9]*$") && checkNumberRange(newValue, businessControl.getMinLongValue(), businessControl.getMaxLongValue())) {
                    double value = parseSignedDouble(newValue);
                } else {
                    valueInputField.setText(oldValue);
                }
            }
        });


        valueSlider = new Slider(businessControl.getMinLongValue(), businessControl.getMaxLongValue(), 0);
        valueSlider.setShowTickMarks(true);
        //valueSlider.setShowTickLabels(true);

        colorPicker = new ColorPicker();
    }

    private void layoutControls() {
        setCenter(businessControl);
        VBox box = new VBox(10, new Label("Longitude"), valueInputField, valueSlider /* , colorPicker */ );
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void addBindings() {
        Bindings.bindBidirectional(valueInputField.textProperty(), businessControl.longitudeValueProperty(), new NumberStringConverter());
        valueSlider.valueProperty().bindBidirectional(businessControl.longitudeValueProperty());
/*
        colorPicker.valueProperty().bindBidirectional(businessControl.baseColorProperty());*/
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
}
