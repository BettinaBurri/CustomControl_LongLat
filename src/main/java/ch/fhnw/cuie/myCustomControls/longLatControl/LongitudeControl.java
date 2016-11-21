package ch.fhnw.cuie.myCustomControls.longLatControl;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

/**
 * @author Bettina Burri, Kathrin Koebel
 * 
 */
public class LongitudeControl extends Region {
    // needed for StyleableProperties
    private static final StyleablePropertyFactory<LongitudeControl> FACTORY = new StyleablePropertyFactory<>(Region.getClassCssMetaData());

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "style.css";

    private static final double ARTBOARD_WIDTH  = 300;
    private static final double ARTBOARD_HEIGHT = 300;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH  = 25;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double  VALUE_PATH_RADIUS = 125;
    private static final double  THUMB_RADIUS = 10;
    private static final Point2D VALUE_PATH_CENTER = new Point2D(ARTBOARD_WIDTH/2, ARTBOARD_HEIGHT/2);

    private static final double MAXIMUM_WIDTH = 800;
    private static final String FORMAT = "%.4f";

    // all parts
    private Text display;
    private Circle valuePath, valueThumb;
    private Arc valueArc;
    private Ellipse line0, line1, line2, line3, line4;
    private Rectangle valueTextBG;
    private Double minLongValue = new Double(0);
    private Double maxLongValue = new Double(360);



    // all properties
    private final StringProperty textValue = new SimpleStringProperty();
    private final DoubleProperty valueText = new SimpleDoubleProperty();
    private final DoubleProperty value    = new SimpleDoubleProperty();
    private final BooleanProperty animated      = new SimpleBooleanProperty(true);
    private DoubleProperty animatedValue  = new SimpleDoubleProperty();

    // Not needed properties ??
    private final BooleanProperty          timerIsRunning = new SimpleBooleanProperty(false);
    private final ObjectProperty<Duration> pulse          = new SimpleObjectProperty<>(Duration.seconds(1.0));

    //CSS stylable properties
    private static final CssMetaData<LongitudeControl, Color> BASE_COLOR_META_DATA = FACTORY.createColorCssMetaData("-base-color", s -> s.baseColor);

    private final StyleableObjectProperty<Color> baseColor = new SimpleStyleableObjectProperty<Color>(BASE_COLOR_META_DATA, this, "baseColor") {
        @Override
        protected void invalidated() {
            setStyle(BASE_COLOR_META_DATA.getProperty() + ": " + (getBaseColor()).toString().replace("0x", "#") + ";");
            applyCss();
        }
    };

    // all animations
    private final Timeline timeline = new Timeline();


    // all parts need to be children of the drawingPane
    private Pane drawingPane;

    private final AnimationTimer timer = new AnimationTimer() {
        private long lastTimerCall;

        @Override
        public void handle(long now) {
            if (now > lastTimerCall + (getPulse().toMillis() * 1_000_000L)) {
                performPeriodicTask();
                lastTimerCall = now;
            }
        }
    };

    public LongitudeControl() {
        initializeSelf();
        initializeParts();
        layoutParts();
        initializeAnimations();
        addEventHandlers();
        addValueChangedListeners();
        setupBindings();
    }

    private void initializeSelf() {
        addStyleSheets(this);
        getStyleClass().add(getStyleClassName());
    }


    private void initializeParts() {
        display = createCenteredText("display");

        // Globe Lonitude Lines
        line4 = new Ellipse(ARTBOARD_WIDTH/2, ARTBOARD_WIDTH/2, 100, 125);
        line4.getStyleClass().add("longitudeLine");
        line3 = new Ellipse(ARTBOARD_WIDTH/2, ARTBOARD_WIDTH/2, 75, 125);
        line3.getStyleClass().add("longitudeLine");
        line2 = new Ellipse(ARTBOARD_WIDTH/2, ARTBOARD_WIDTH/2, 50, 125);
        line2.getStyleClass().add("longitudeLine");
        line1 = new Ellipse(ARTBOARD_WIDTH/2, ARTBOARD_WIDTH/2, 25, 125);
        line1.getStyleClass().add("longitudeLine");
        line0 = new Ellipse(ARTBOARD_WIDTH/2, ARTBOARD_WIDTH/2, 0, 125);
        line0.getStyleClass().add("longitudeLine");

        // Globe value Label
        valueTextBG = new Rectangle(70, 105, 160, 80);
        valueTextBG.getStyleClass().add("valueTextBG");


        // Arc and Thumb for Value
        valuePath = new Circle(ARTBOARD_WIDTH/2, ARTBOARD_WIDTH/2, 125);
        valuePath.getStyleClass().add("valuePath");

        valueThumb = new Circle(VALUE_PATH_CENTER.getX(), VALUE_PATH_CENTER.getY(), THUMB_RADIUS);
        valueThumb.getStyleClass().add("valueThumb");

        valueArc = new Arc(ARTBOARD_WIDTH/2, ARTBOARD_WIDTH/2, 125, 125, 0, 0);
        valueArc.getStyleClass().add("valueArc");
        valueArc.setType(ArcType.OPEN);



        // always needed
        drawingPane = new Pane();
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.getStyleClass().add("drawingPane");
    }

    private void layoutParts() {
        // add all your parts here
        drawingPane.getChildren().addAll( line4, line3, line2, line1, line0, valueTextBG, display, valuePath, valueArc, valueThumb);

        getChildren().add(drawingPane);
    }

    private void initializeAnimations() {

    }

    private void addEventHandlers() {
        valueThumb.setOnMouseDragged(event -> {
                setAnimatedValue((angle(VALUE_PATH_CENTER.getX(), VALUE_PATH_CENTER.getY(), event.getX(), event.getY())));
        });
    }

    private void addValueChangedListeners() {

        valueProperty().addListener((observable, oldValue, newValue) -> {
            // was tu ich wenn die Animation schon läuft?
            timeline.stop();

            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.millis(200),
                            new KeyValue(animatedValue, newValue)));

            timeline.play();
        });

        animatedValueProperty().addListener((observable, oldValue, newValue) -> {
            Point2D p = pointOnCircle(VALUE_PATH_CENTER.getX(), VALUE_PATH_CENTER.getY(), VALUE_PATH_RADIUS, newValue.doubleValue()+90);
            valueArc.setVisible(true);
            valueArc.setLength(getAngle(newValue));
            valueThumb.setCenterX(p.getX());
            valueThumb.setCenterY(p.getY());


        });

        // if you need the timer
        timerIsRunning.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                timer.start();
            } else {
                timer.stop();
            }
        });

        // always needed
        widthProperty().addListener((observable, oldValue, newValue) -> resize());
        heightProperty().addListener((observable, oldValue, newValue) -> resize());
    }

    private void checkBoundaries(Number newValue) {
        setTimerIsRunning(newValue.doubleValue() > getMaxLongValue() || newValue.doubleValue() < getMinLongValue());
    }

    private double getAngle(Number value) {
        return -(3.6 * getPercentage(value));
    }

    private double getPercentage(Number newValue) {
        double min = getMinLongValue();
        double max = getMaxLongValue();
        return Math.max(1.0, Math.min(100.0, (newValue.doubleValue() - min) / ((max - min) / 100.0)));
    }

    private void setupBindings() {
        display.textProperty().bind(textValueProperty());
        display.textProperty().bind(animatedValueProperty().asString(FORMAT));
    }

    private void performPeriodicTask() { valueArc.setVisible(!valueArc.isVisible());
    }


    // some useful helper-methods

    private Text createCenteredText(String styleClass) {
        return createCenteredText(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, styleClass);
    }

    private Text createCenteredText(double cx, double cy, String styleClass) {
        Text myText = new Text();
        myText.getStyleClass().add(styleClass);
        myText.setTextOrigin(VPos.CENTER);
        myText.setTextAlignment(TextAlignment.CENTER);

        double width = cx > ARTBOARD_WIDTH * 0.5 ? ((ARTBOARD_WIDTH - cx) * 2.0) : cx * 2.0;
        myText.setWrappingWidth(width);
        myText.setBoundsType(TextBoundsType.VISUAL);
        myText.setY(cy);

        return myText;
    }

    /*
     * angle in degrees, 0 is north
     */
    private double angle(double cx, double cy, double x, double y) {
        double deltaX = x - cx;
        double deltaY = y - cy;
        double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        double nx     = deltaX / radius;
        double ny     = deltaY / radius;
        double theta  = Math.toRadians(90) + Math.atan2(ny, nx);

        return Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) : Math.toDegrees((theta)) + 360.0;
    }

    /*
     * angle in degrees, 0 is north
     */
    private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
        return new Point2D(cX - (radius * Math.sin(Math.toRadians(angle - 180))),
                           cY + (radius * Math.cos(Math.toRadians(angle - 180))));
    }

    /*
     * Needed if you want to know what's defined in css during initialization
     */
    private void applyCss(Node node) {
        Group group = new Group(node);
        group.getStyleClass().add(getStyleClassName());
        addStyleSheets(group);
        new Scene(group);
        node.applyCss();
    }

    private void resize() {
        Insets padding         = getPadding();
        double availableWidth  = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH), MINIMUM_WIDTH);

        double scalingFactor = width / ARTBOARD_WIDTH;

        if (availableWidth > 0 && availableHeight > 0) {
            drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
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

    // compute sizes

    @Override
    protected double computeMinWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return ARTBOARD_HEIGHT + verticalPadding;
    }

    // getter and setter for all properties


    public double getValue() {
        return value.get();
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public String getTextValue() {
        return textValue.get();
    }

    public StringProperty textValueProperty() {
        return textValue;
    }

    public void setTextValue(String value) {
        this.textValue.set(value);
    }

    public Color getBaseColor() {
        return baseColor.get();
    }

    public StyleableObjectProperty<Color> baseColorProperty() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor.set(baseColor);
    }

    public boolean isTimerIsRunning() {
        return timerIsRunning.get();
    }

    public BooleanProperty timerIsRunningProperty() {
        return timerIsRunning;
    }

    public void setTimerIsRunning(boolean timerIsRunning) {
        this.timerIsRunning.set(timerIsRunning);
    }

    public Duration getPulse() {
        return pulse.get();
    }

    public ObjectProperty<Duration> pulseProperty() {
        return pulse;
    }

    public void setPulse(Duration pulse) {
        this.pulse.set(pulse);
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

    public void setMaxLongValue(Double maxLongValue) {
        this.maxLongValue = maxLongValue;
    }

    public double getValueText() {
        return valueText.get();
    }

    public DoubleProperty valueTextProperty() {
        return valueText;
    }

    public void setValueText(double valueText) {
        this.valueText.set(valueText);
    }

    public double getAnimatedValue() {
        return animatedValue.get();
    }

    public DoubleProperty animatedValueProperty() {
        return animatedValue;
    }

    public void setAnimatedValue(double animatedValue) {
        this.animatedValue.set(animatedValue);
    }

}
