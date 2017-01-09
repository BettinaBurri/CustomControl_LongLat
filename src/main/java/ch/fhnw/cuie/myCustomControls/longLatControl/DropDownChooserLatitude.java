package ch.fhnw.cuie.myCustomControls.longLatControl;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Created by bettina on 09.01.17.
 */
public class DropDownChooserLatitude extends HBox {
    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "dropDownChooser.css";

    private TextField input;
    private static final String ANGLE_DOWN = "\uf107";
    private static final String ANGLE_UP   = "\uf106";

    private LatitudeControl customControl;


    public DropDownChooserLatitude(){
        //this.latitude = latitude;
        initializeSelf();
        initializeParts();
        layoutParts();
        setupBindings();
    }

    private void initializeSelf(){

        input = new TextField();
        getStyleClass().add("dropDownChooser");

        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getStylesheets().add(stylesheet);
    }

    private void initializeParts(){
        customControl = new LatitudeControl();
    }

    private void layoutParts(){
        getChildren().addAll(input, customControl);
    }

    private void setupBindings(){


    }
}
