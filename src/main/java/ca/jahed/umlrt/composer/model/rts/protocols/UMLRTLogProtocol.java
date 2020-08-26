package ca.jahed.umlrt.composer.model.rts.protocols;

import ca.jahed.umlrt.composer.model.rts.UMLRTSystemProtocol;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTLogProtocol extends UMLRTSystemProtocol {
    private static final UMLRTLogProtocol instance = new UMLRTLogProtocol();

    private UMLRTLogProtocol() {
        setName("Log");
        setEObj(UMLRTUtils.getProtocol(UMLRTUtils.SysProtocol.Log));
    }

    public static UMLRTLogProtocol get() {
        return instance;
    }
}
