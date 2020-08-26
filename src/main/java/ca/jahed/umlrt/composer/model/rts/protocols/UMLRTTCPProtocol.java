package ca.jahed.umlrt.composer.model.rts.protocols;

import ca.jahed.umlrt.composer.model.rts.UMLRTSystemCallEvent;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemProtocol;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTTCPProtocol extends UMLRTSystemProtocol {
    private static final UMLRTTCPProtocol instance = new UMLRTTCPProtocol();

    private UMLRTTCPProtocol() {
        setName("TCP");
        setEObj(UMLRTUtils.getProtocol(UMLRTUtils.SysProtocol.TCP));
        addInputSignal(new UMLRTSystemCallEvent(UMLRTUtils.getEvent(
                UMLRTUtils.SysProtocol.TCP, "received")));
    }

    public static UMLRTTCPProtocol get() {
        return instance;
    }
}
