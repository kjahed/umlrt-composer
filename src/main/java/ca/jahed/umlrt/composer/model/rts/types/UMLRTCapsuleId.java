package ca.jahed.umlrt.composer.model.rts.types;

import ca.jahed.umlrt.composer.model.rts.UMLRTSystemType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTCapsuleId extends UMLRTSystemType {
    private static final UMLRTCapsuleId instance = new UMLRTCapsuleId();

    private UMLRTCapsuleId() {
        setName("UMLRTCapsuleId");
        setEObj(UMLRTUtils.getSystemClass(UMLRTUtils.SysClass.UMLRTCapsuleId));
    }

    public static UMLRTCapsuleId get() {
        return instance;
    }
}
