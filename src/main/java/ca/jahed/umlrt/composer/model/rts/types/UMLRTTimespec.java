package ca.jahed.umlrt.composer.model.rts.types;

import ca.jahed.umlrt.composer.model.rts.UMLRTSystemType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTTimespec extends UMLRTSystemType {
    private static final UMLRTTimespec instance = new UMLRTTimespec();

    private UMLRTTimespec() {
        setName("UMLRTTimespec");
        setEObj(UMLRTUtils.getSystemClass(UMLRTUtils.SysClass.UMLRTTimespec));
    }

    public static UMLRTTimespec get() {
        return instance;
    }
}
