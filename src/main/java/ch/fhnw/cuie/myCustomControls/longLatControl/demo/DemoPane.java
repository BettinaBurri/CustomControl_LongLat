package ch.fhnw.cuie.myCustomControls.longLatControl.demo;

import ch.fhnw.cuie.myCustomControls.longLatControl.LongitudeControl;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;


/**
 * @author Dieter Holz
 */
public class DemoPane extends BorderPane {
    private LongitudeControl customControl;

    private TextField   valueField;
    private TextField   valueInputField;
    private Slider      valueSlider;
    private CheckBox    timerRunningBox;
    private Slider      pulseSlider;
    private ColorPicker colorPicker;

    public DemoPane() {
        initializeControls();
        layoutControls();
        addBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        customControl = new LongitudeControl();

        //valueField = new TextField();
        //valueField.setText("0.0");

        valueInputField = new TextField();
        valueInputField.setText("0.0");
        valueSlider = new Slider(0, 360, 0);
        //valueSlider.setShowTickLabels(true);
        valueSlider.setShowTickMarks(true);


        //timerRunningBox = new CheckBox("Timer running");
        //timerRunningBox.setSelected(false);

        //pulseSlider = new Slider(0.5, 2.0, 1.0);
        //pulseSlider.setShowTickLabels(true);
        //pulseSlider.setShowTickMarks(true);

        colorPicker = new ColorPicker();
    }

    private void layoutControls() {
        setCenter(customControl);
        VBox box = new VBox(10, new Label("Control Properties"), valueInputField, valueSlider, colorPicker);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void addBindings() {
        //customControl.textValueProperty().bindBidirectional(valueField.textProperty());

        //customControl.timerIsRunningProperty().bindBidirectional(timerRunningBox.selectedProperty());
        //customControl.pulseProperty().bind(Bindings.createObjectBinding(() -> Duration.seconds(pulseSlider.getValue()), pulseSlider.valueProperty()));

        Bindings.bindBidirectional(valueInputField.textProperty(), customControl.valueProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(valueInputField.textProperty(), customControl.valueDisplayProperty(), new NumberStringConverter());
        valueSlider.valueProperty().bindBidirectional(customControl.valueProperty());
        colorPicker.valueProperty().bindBidirectional(customControl.baseColorProperty());
    }

}
