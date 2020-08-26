package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTUnsignedInt extends UMLRTPrimitiveType {
    private static final UMLRTUnsignedInt instance = new UMLRTUnsignedInt();

    private UMLRTUnsignedInt() {
        setName("unsigned int");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTUnsignedInt get() {
        return instance;
    }
}