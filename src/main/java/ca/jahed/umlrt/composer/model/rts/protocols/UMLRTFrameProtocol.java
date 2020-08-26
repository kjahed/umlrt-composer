package ca.jahed.umlrt.composer.model.rts.protocols;

import ca.jahed.umlrt.composer.model.rts.UMLRTSystemProtocol;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTFrameProtocol extends UMLRTSystemProtocol {
    private static final UMLRTFrameProtocol instance = new UMLRTFrameProtocol();

    private UMLRTFrameProtocol() {
        setName("Frame");
        setEObj(UMLRTUtils.getProtocol(UMLRTUtils.SysProtocol.Frame));
    }

    public static UMLRTFrameProtocol get() {
        return instance;
    }
}
