package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTInt16 extends UMLRTPrimitiveType {
    private static final UMLRTInt16 instance = new UMLRTInt16();

    private UMLRTInt16() {
        setName("int16_t");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTInt16 get() {
        return instance;
    }
}