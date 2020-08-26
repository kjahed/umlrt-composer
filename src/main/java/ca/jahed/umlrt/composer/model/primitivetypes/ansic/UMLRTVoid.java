package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTVoid extends UMLRTPrimitiveType {
    private static final UMLRTVoid instance = new UMLRTVoid();

    private UMLRTVoid() {
        setName("void");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTVoid get() {
        return instance;
    }
}