package ca.jahed.umlrt.composer.model.rts.types;

import ca.jahed.umlrt.composer.model.rts.UMLRTSystemType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTTimerId extends UMLRTSystemType {
    private static final UMLRTTimerId instance = new UMLRTTimerId();

    private UMLRTTimerId() {
        setName("UMLRTTimerId");
        setEObj(UMLRTUtils.getSystemClass(UMLRTUtils.SysClass.UMLRTTimerId));
    }

    public static UMLRTTimerId get() {
        return instance;
    }
}
