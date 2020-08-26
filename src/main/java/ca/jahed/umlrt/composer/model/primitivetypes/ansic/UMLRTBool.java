package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTBool extends UMLRTPrimitiveType {
    private static final UMLRTBool instance = new UMLRTBool();

    private UMLRTBool() {
        setName("bool");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTBool get() {
        return instance;
    }
}