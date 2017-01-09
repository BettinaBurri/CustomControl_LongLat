package ch.fhnw.cuie.myCustomControls.longLatControl;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;

import javax.xml.soap.Text;

/**
 * Created by bettina on 09.01.17.
 */
public class CompleteControlLatitude extends StackPane{

    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "style.css";

    private static final String ANGLE_DOWN = "\uf107";
    private static final String ANGLE_UP   = "\uf106";

    // all Parts
    private TextField textField;
    private Popup popup;
    private Pane dropDownChooser;
    private Button chooserButton;

    // Propertys
    private final DoubleProperty value    = new SimpleDoubleProperty();


    public CompleteControlLatitude(){
        initializeSelf();
        initializeParts();
        layoutParts();
        addEventHandler();
        setupBindings();
    }

    private void initializeSelf(){

    }

    private void initializeParts(){
        textField = new TextField();

        chooserButton = new Button(ANGLE_DOWN);
        chooserButton.getStyleClass().add("chooserButton");

        dropDownChooser = new DropDownChooserLatitude();

        popup = new Popup();
        popup.getContent().addAll(dropDownChooser);

    }

    private void layoutParts(){
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

    }
}
