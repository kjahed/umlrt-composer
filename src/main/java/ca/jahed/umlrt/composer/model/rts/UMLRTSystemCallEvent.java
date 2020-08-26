package ca.jahed.umlrt.composer.model.rts;

import ca.jahed.umlrt.composer.model.UMLRTCallEvent;
import org.eclipse.uml2.uml.CallEvent;

public class UMLRTSystemCallEvent extends UMLRTCallEvent {
    public UMLRTSystemCallEvent(CallEvent event) {
        setName(event.getName());
        setEObj(event);
    }
}
