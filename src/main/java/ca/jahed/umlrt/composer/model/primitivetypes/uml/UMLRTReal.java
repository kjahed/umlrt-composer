package ca.jahed.umlrt.composer.model.primitivetypes.uml;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTReal extends UMLRTPrimitiveType {
    private static final UMLRTReal instance = new UMLRTReal();

    private UMLRTReal() {
        setName("Real");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTReal get() {
        return instance;
    }
}