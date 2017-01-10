package ch.fhnw.cuie.myCustomControls.longLatControl;

import javafx.scene.layout.StackPane;

/**
 * Created by kathrin on 10.01.17.
 */
public class DropDownChooserLongitude extends StackPane {
    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "dropDownChooser.css";

    private LongitudeControl customControl;


    public DropDownChooserLongitude(LongitudeControl longitudeControl){
        this.customControl = longitudeControl;
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

    public LongitudeControl getCustomControl() {
        return customControl;
    }

    public void setCustomControl(LongitudeControl customControl) {
        this.customControl = customControl;
    }
}
