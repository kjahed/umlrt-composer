package ca.jahed.umlrt.composer.model;

import ca.jahed.umlrt.composer.model.sm.*;

public interface UMLRTVisitor {

    Object visitModel(UMLRTModel model);

    Object visitPackage(UMLRTPackage pkg);

    Object visitClass(UMLRTClass passiveClass);

    Object visitCapsule(UMLRTCapsule capsule);

    Object visitArtifact(UMLRTArtifact artifact);

    Object visitEnumeration(UMLRTEnumeration enumeration);

    Object visitCapsulePart(UMLRTCapsulePart capsulePart);

    Object visitPort(UMLRTPort port);

    Object visitProtocol(UMLRTProtocol protocol);

    Object visitType(UMLRTType type);

    Object visitConnector(UMLRTConnector connector);

    Object visitConnectorEnd(UMLRTConnectorEnd connectorEnd);

    Object visitAttribute(UMLRTAttribute attribute);

    Object visitParameter(UMLRTParameter parameter);

    Object visitOperation(UMLRTOperation operation);

    Object visitCallEvent(UMLRTCallEvent callEvent);

    Object visitStateMachine(UMLRTStateMachine stateMachine);

    Object visitSimpleState(UMLRTState state);

    Object visitCompositeState(UMLRTCompositeState state);

    Object visitPseudoState(UMLRTPseudoState state);

    Object visitRegion(UMLRTRegion region);

    Object visitTransition(UMLRTTransition transition);

    Object visitTrigger(UMLRTTrigger trigger);

    Object visitActionCode(UMLRTActionCode actionCode);

    Object visitGuard(UMLRTActionCode actionCode);
}
