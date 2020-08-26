package ca.jahed.umlrt.composer.model.primitivetypes.uml;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTUnlimitedNatural extends UMLRTPrimitiveType {
    private static final UMLRTUnlimitedNatural instance = new UMLRTUnlimitedNatural();

    private UMLRTUnlimitedNatural() {
        setName("UnlimitedNatural");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTUnlimitedNatural get() {
        return instance;
    }
}