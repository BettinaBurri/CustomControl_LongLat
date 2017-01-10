package ch.fhnw.cuie.myCustomControls.longLatControl.demo;

import ch.fhnw.cuie.myCustomControls.longLatControl.CompleteControlLatitude;
import ch.fhnw.cuie.myCustomControls.longLatControl.DropDownChooserLatitude;
import ch.fhnw.cuie.myCustomControls.longLatControlSkin.LongitudeControl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * @author Dieter Holz
 */
public class DemoStarter extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Region rootPanel = new DropDownChooserLatitude();
        //Region rootPanel = new LongitudeControl();
        Region rootPanel = new CompleteControlLatitude();

        Scene scene = new Scene(rootPanel);

        primaryStage.setTitle("Simple Control Demo");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
