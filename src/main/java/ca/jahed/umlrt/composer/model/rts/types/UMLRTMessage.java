package ca.jahed.umlrt.composer.model.rts.types;

import ca.jahed.umlrt.composer.model.rts.UMLRTSystemType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTMessage extends UMLRTSystemType {
    private static final UMLRTMessage instance = new UMLRTMessage();

    private UMLRTMessage() {
        setName("UMLRTMessage");
        setEObj(UMLRTUtils.getSystemClass(UMLRTUtils.SysClass.UMLRTMessage));
    }

    public static UMLRTMessage get() {
        return instance;
    }
}
