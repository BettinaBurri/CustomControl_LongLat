package ch.fhnw.cuie.myCustomControls.longLatControl;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Created by bettina on 09.01.17.
 */
public class DropDownChooserLatitude extends StackPane {
    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "dropDownChooser.css";

    private LatitudeControl customControl;


    public DropDownChooserLatitude(LatitudeControl latitudeControl){
        this.customControl = latitudeControl;
        initializeSelf();
        initializeParts();
        layoutParts();
        setupBindings();
    }

    private void initializeSelf(){
        getStyleClass().add("dropDownChooser");

        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        getStylesheets().add(fonts);
        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getStylesheets().add(stylesheet);
    }

    private void initializeParts(){
    }

    private void layoutParts(){
        getChildren().addAll(customControl);
    }

    private void setupBindings(){


    }

    public LatitudeControl getCustomControl() {
        return customControl;
    }

    public void setCustomControl(LatitudeControl customControl) {
        this.customControl = customControl;
    }
}
