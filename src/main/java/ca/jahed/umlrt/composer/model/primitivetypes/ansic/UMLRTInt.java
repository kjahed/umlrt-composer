package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTInt extends UMLRTPrimitiveType {
    private static final UMLRTInt instance = new UMLRTInt();

    private UMLRTInt() {
        setName("int");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTInt get() {
        return instance;
    }
}