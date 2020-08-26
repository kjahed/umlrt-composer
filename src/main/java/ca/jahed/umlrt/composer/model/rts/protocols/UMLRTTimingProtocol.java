package ca.jahed.umlrt.composer.model.rts.protocols;

import ca.jahed.umlrt.composer.model.rts.UMLRTSystemCallEvent;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemProtocol;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTTimingProtocol extends UMLRTSystemProtocol {
    private static final UMLRTTimingProtocol instance = new UMLRTTimingProtocol();

    private UMLRTTimingProtocol() {
        setName("Timing");
        setEObj(UMLRTUtils.getProtocol(UMLRTUtils.SysProtocol.Timing));
        addInputSignal(new UMLRTSystemCallEvent(UMLRTUtils.getEvent(
                UMLRTUtils.SysProtocol.Timing, "timeout")));
    }

    public static UMLRTTimingProtocol get() {
        return instance;
    }
}
