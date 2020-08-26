package ca.jahed.umlrt.composer.model.rts;

import ca.jahed.umlrt.composer.model.rts.protocols.*;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;
import ca.jahed.umlrt.composer.model.UMLRTProtocol;
import org.eclipse.papyrusrt.umlrt.profile.UMLRealTime.ProtocolContainer;

public abstract class UMLRTSystemProtocol extends UMLRTProtocol {
    public static UMLRTSystemProtocol get(ProtocolContainer protocol) {
        if(UMLRTUtils.isTiming(protocol))
            return UMLRTTimingProtocol.get();
        if(UMLRTUtils.isLog(protocol))
            return UMLRTLogProtocol.get();
        if(UMLRTUtils.isFrame(protocol))
            return UMLRTFrameProtocol.get();
        if(UMLRTUtils.isTCP(protocol))
            return UMLRTTCPProtocol.get();
        if(UMLRTUtils.isMQTT(protocol))
            return UMLRTMQTTProtocol.get();
        throw new RuntimeException("Unexpected system protocol " + protocol.getBase_Package().getName());
    }
}
