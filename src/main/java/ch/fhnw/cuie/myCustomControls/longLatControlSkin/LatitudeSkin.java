package ch.fhnw.cuie.myCustomControls.longLatControlSkin;

import ch.fhnw.cuie.myCustomControls.longLatControl.*;
import javafx.scene.control.SkinBase;

/**
 * Created by bettina on 09.01.17.
 */
public class LatitudeSkin extends SkinBase<LatitudeControl> {
    private static final int IMG_SIZE   = 12;
    private static final int IMG_OFFSET = 4;

    private static final String ANGLE_DOWN = "\uf107";
    private static final String ANGLE_UP   = "\uf106";

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected LatitudeSkin(LatitudeControl control) {
        super(control);
    }
}
