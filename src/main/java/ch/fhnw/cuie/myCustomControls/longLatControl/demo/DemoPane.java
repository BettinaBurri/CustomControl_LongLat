package ch.fhnw.cuie.myCustomControls.longLatControl.demo;

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
public class DemoPane extends BorderPane {
    private LongitudeControl customControl;

    private TextField   valueInputField;
    private Slider      valueSlider;

    // set min & max value
    private int minValue = -100;
    private int maxValue = 360;

    private ColorPicker colorPicker;

    public DemoPane() {
        initializeControls();
        layoutControls();
        addBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        customControl = new LongitudeControl();

        valueInputField = new TextField();
        valueInputField.setText("0.0");

        // limit input to numbers & check min/max value
        valueInputField.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("^[0-9, -]*\\.?[0-9]*$")
                        && Float.parseFloat(newValue)>=minValue
                        && Float.parseFloat(newValue)<=maxValue) {
                    double value = Float.parseFloat(newValue);
                } else {
                    valueInputField.setText(oldValue);
                }
            }
        });


        valueSlider = new Slider(0, 360, 0);
        valueSlider.setShowTickMarks(true);
        //valueSlider.setShowTickLabels(true);

        colorPicker = new ColorPicker();
    }

    private void layoutControls() {
        setCenter(customControl);
        VBox box = new VBox(10, new Label("Longitude"), valueInputField, valueSlider, colorPicker);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void addBindings() {
        Bindings.bindBidirectional(valueInputField.textProperty(), customControl.valueProperty(), new NumberStringConverter());
        valueSlider.valueProperty().bindBidirectional(customControl.valueProperty());

        colorPicker.valueProperty().bindBidirectional(customControl.baseColorProperty());
    }

}
