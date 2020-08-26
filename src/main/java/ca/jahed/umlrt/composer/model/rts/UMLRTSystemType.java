package ca.jahed.umlrt.composer.model.rts;

import ca.jahed.umlrt.composer.model.UMLRTClass;
import ca.jahed.umlrt.composer.model.rts.types.UMLRTCapsuleId;
import ca.jahed.umlrt.composer.model.rts.types.UMLRTMessage;
import ca.jahed.umlrt.composer.model.rts.types.UMLRTTimerId;
import ca.jahed.umlrt.composer.model.rts.types.UMLRTTimespec;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;
import org.eclipse.uml2.uml.Class;

public class UMLRTSystemType extends UMLRTClass {
    public static UMLRTSystemType get(Class cls) {
        if(UMLRTUtils.isMessage(cls))
            return UMLRTMessage.get();
        if(UMLRTUtils.isCapsuleId(cls))
            return UMLRTCapsuleId.get();
        if(UMLRTUtils.isTimerId(cls))
            return UMLRTTimerId.get();
        if(UMLRTUtils.isTimespec(cls))
            return UMLRTTimespec.get();
        throw new RuntimeException("Unexpected system type " + cls.getName());
    }
}
