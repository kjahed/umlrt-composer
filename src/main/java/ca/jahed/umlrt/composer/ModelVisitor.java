package ca.jahed.umlrt.composer;

import org.eclipse.papyrusrt.codegen.cpp.profile.RTCppProperties.*;
import org.eclipse.papyrusrt.umlrt.profile.UMLRealTime.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.*;

public interface ModelVisitor {
    Object visitModel(Model model);

    Object visitPackage(Package pkg);

    Object visitCapsule(Capsule capsule);

    Object visitConnector(Connector connector);

    Object visitConnectorEnd(ConnectorEnd connectorEnd);

    Object visitClass(Class cls);

    Object visitArtifact(Artifact artifact);

    Object visitEnumeration(Enumeration enumeration);

    Object visitCapsulePart(CapsulePart capsulePart);

    Object visitAttribute(Property property);

    Object visitOperation(Operation operation);

    Object visitParameter(Parameter parameter);

    Object visitPort(Port port);

    Object visitType(Type type);

    Object visitProtocol(ProtocolContainer protocol);

    Object visitCallEvent(CallEvent callEvent);

    Object visitStateMachine(StateMachine stateMachine);

    Object visitRegion(Region region);

    Object visitTransition(Transition transition);

    Object visitTrigger(Trigger trigger);

    Object visitVertex(Vertex vertex);

    Object visitOpaqueBehavior(OpaqueBehavior behavior);

    Object visitOpaqueExpression(OpaqueExpression expression);

    Object visitArtifactProperties(ArtifactProperties artifactProperties);

    Object visitAttributeProperties(AttributeProperties attributeProperties);

    Object visitCapsuleProperties(CapsuleProperties capsuleProperties);

    Object visitEnumerationProperties(EnumerationProperties enumerationProperties);

    Object visitOperationProperties(OperationProperties operationProperties);

    Object visitParameterProperties(ParameterProperties parameterProperties);

    Object visitPassiveClassProperties(PassiveClassProperties passiveClassProperties);

    Object visitTypeProperties(TypeProperties typeProperties);
}
