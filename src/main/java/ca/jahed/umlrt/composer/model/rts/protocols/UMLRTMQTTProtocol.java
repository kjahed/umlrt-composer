package ca.jahed.umlrt.composer.model.rts.protocols;

import ca.jahed.umlrt.composer.model.rts.UMLRTSystemCallEvent;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemProtocol;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTMQTTProtocol extends UMLRTSystemProtocol {
    private static final UMLRTMQTTProtocol instance = new UMLRTMQTTProtocol();

    private UMLRTMQTTProtocol() {
        setName("MQTT");
        setEObj(UMLRTUtils.getProtocol(UMLRTUtils.SysProtocol.MQTT));
        addInputSignal(new UMLRTSystemCallEvent(UMLRTUtils.getEvent(
                UMLRTUtils.SysProtocol.MQTT, "received")));
    }

    public static UMLRTMQTTProtocol get() {
        return instance;
    }
}
