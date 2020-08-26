package ca.jahed.umlrt.composer;

import ca.jahed.umlrt.composer.model.*;
import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.model.properties.*;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemCallEvent;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemProtocol;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemType;
import ca.jahed.umlrt.composer.model.sm.*;

public class ModelCopier extends CachedVisitor implements UMLRTVisitor, UMLRTPropertiesVisitor {
    CopyListener listener;

    public ModelCopier(CopyListener listener) {
        this.listener = listener;
    }

    public static ModelCopier getCopier(CopyListener listener) {
        return new ModelCopier(listener);
    }

    public static UMLRTModel copy(UMLRTModel element, CopyListener listener) {
        return getCopier(listener).visitModel(element);
    }

    public static UMLRTPackage copy(UMLRTPackage element, CopyListener listener) {
        return getCopier(listener).visitPackage(element);
    }

    public static UMLRTArtifact copy(UMLRTArtifact element, CopyListener listener) {
        return getCopier(listener).visitArtifact(element);
    }

    public static UMLRTClass copy(UMLRTClass element, CopyListener listener) {
        return getCopier(listener).visitClass(element);
    }

    public static UMLRTCapsule copy(UMLRTCapsule element, CopyListener listener) {
        return getCopier(listener).visitCapsule(element);
    }

    public static UMLRTEnumeration copy(UMLRTEnumeration element, CopyListener listener) {
        return getCopier(listener).visitEnumeration(element);
    }

    public static UMLRTCapsulePart copy(UMLRTCapsulePart element, CopyListener listener) {
        return getCopier(listener).visitCapsulePart(element);
    }

    public static UMLRTProtocol copy(UMLRTProtocol element, CopyListener listener) {
        return getCopier(listener).visitProtocol(element);
    }

    public static UMLRTStateMachine copy(UMLRTStateMachine element, CopyListener listener) {
        return getCopier(listener).visitStateMachine(element);
    }

    public static UMLRTPort copy(UMLRTPort element, CopyListener listener) {
        return getCopier(listener).visitPort(element);
    }

    public static UMLRTAttribute copy(UMLRTAttribute element, CopyListener listener) {
        return getCopier(listener).visitAttribute(element);
    }

    void postVisit(UMLRTElement original, UMLRTElement copy) {
        copy.setEObj(original.getEObj());
        original.getProperties().forEach(prop -> copy.addProperties(visitProperties(prop)));
        if (listener != null)
            listener.onCopy(original, copy);
    }

    @Override
    public UMLRTModel visitModel(UMLRTModel model) {
        UMLRTModel copy = new UMLRTModel();
        copy.setName(model.getName());
        copy.loadPackage(visitPackage(model));
        copy.setTop(visitCapsulePart(model.getTop()));
        return copy;
    }

    @Override
    public UMLRTPackage visitPackage(UMLRTPackage pkg) {
        if (visited(pkg)) return (UMLRTPackage) cached(pkg);

        UMLRTPackage copy = new UMLRTPackage();
        cache(pkg, copy);
        copy.setName(pkg.getName());

        for (UMLRTCapsule capsule : pkg.getCapsules())
            copy.addCapsule(visitCapsule(capsule));

        for (UMLRTEnumeration enumeration : pkg.getEnumerations())
            copy.addEnumeration(visitEnumeration(enumeration));

        for (UMLRTPackage nested : pkg.getPackages())
            copy.addPackage(visitPackage(nested));

        for (UMLRTProtocol protocol : pkg.getProtocols())
            copy.addProtocol(visitProtocol(protocol));

        for (UMLRTClass passiveClass : pkg.getPassiveClasses())
            copy.addPassiveClass(visitClass(passiveClass));

        for (UMLRTArtifact artifact : pkg.getArtifacts())
            copy.addArtifact(visitArtifact(artifact));
        postVisit(pkg, copy);
        return copy;
    }

    @Override
    public UMLRTClass visitClass(UMLRTClass passiveClass) {
        if (visited(passiveClass)) return (UMLRTClass) cached(passiveClass);

        UMLRTClass copy = new UMLRTClass();
        cache(passiveClass, copy);
        copy.setName(passiveClass.getName());
        for (UMLRTAttribute attribute : passiveClass.getAttributes())
            copy.addAttribute(visitAttribute(attribute));
        for (UMLRTOperation operation : passiveClass.getOperations())
            copy.addOperation(visitOperation(operation));
        postVisit(passiveClass, copy);
        return copy;
    }

    @Override
    public UMLRTArtifact visitArtifact(UMLRTArtifact artifact) {
        if (visited(artifact)) return (UMLRTArtifact) cached(artifact);

        UMLRTArtifact copy = new UMLRTArtifact();
        cache(artifact, copy);
        copy.setName(artifact.getName());
        copy.setFileName(artifact.getFileName());

        for (UMLRTAttribute attribute : artifact.getAttributes())
            copy.addAttribute(visitAttribute(attribute));
        for (UMLRTOperation operation : artifact.getOperations())
            copy.addOperation(visitOperation(operation));
        postVisit(artifact, copy);
        return copy;
    }

    @Override
    public UMLRTCapsule visitCapsule(UMLRTCapsule capsule) {
        if (visited(capsule)) return (UMLRTCapsule) cached(capsule);

        UMLRTCapsule copy = new UMLRTCapsule();
        cache(capsule, copy);
        copy.setName(capsule.getName());
        copy.setBaseClass(visitClass(capsule.getBaseClass()));

        if (capsule.getStateMachine() != null)
            copy.setStateMachine(visitStateMachine(capsule.getStateMachine()));
        for (UMLRTCapsulePart part : capsule.getParts())
            copy.addPart(visitCapsulePart(part));
        for (UMLRTPort port : capsule.getPorts())
            copy.addPort(visitPort(port));
        for (UMLRTConnector connector : capsule.getConnectors())
            copy.addConnector(visitConnector(connector));
        postVisit(capsule, copy);
        return copy;
    }

    @Override
    public UMLRTEnumeration visitEnumeration(UMLRTEnumeration enumeration) {
        if (visited(enumeration)) return (UMLRTEnumeration) cached(enumeration);

        UMLRTEnumeration copy = new UMLRTEnumeration();
        cache(enumeration, copy);
        copy.setName(enumeration.getName());
        copy.getLiterals().addAll(enumeration.getLiterals());
        postVisit(enumeration, copy);
        return copy;
    }

    @Override
    public UMLRTCapsulePart visitCapsulePart(UMLRTCapsulePart capsulePart) {
        if (visited(capsulePart)) return (UMLRTCapsulePart) cached(capsulePart);

        UMLRTCapsulePart copy = new UMLRTCapsulePart();
        cache(capsulePart, copy);
        copy.setName(capsulePart.getName());
        copy.setAttribute(visitAttribute(capsulePart.getAttribute()));
        copy.setCapsule(visitCapsule(capsulePart.getCapsule()));
        copy.setOptional(capsulePart.isOptional());
        copy.setPlugin(capsulePart.isPlugin());
        postVisit(capsulePart, copy);
        return copy;
    }

    @Override
    public UMLRTPort visitPort(UMLRTPort port) {
        if (visited(port)) return (UMLRTPort) cached(port);

        UMLRTPort copy = new UMLRTPort();
        cache(port, copy);
        copy.setName(port.getName());
        copy.setConjugated(port.isConjugated());
        copy.setBehavior(port.isBehavior());
        copy.setService(port.isService());
        copy.setWired(port.isWired());
        copy.setNotification(port.isNotification());
        copy.setPublish(port.isPublish());
        copy.setReplication(port.getReplication());
        copy.setRegistrationType(port.getRegistrationType());
        copy.setRegistrationOverride(port.getRegistrationOverride());
        copy.setProtocol(visitProtocol(port.getProtocol()));
        postVisit(port, copy);
        return copy;
    }

    @Override
    public UMLRTProtocol visitProtocol(UMLRTProtocol protocol) {
        if (visited(protocol)) return (UMLRTProtocol) cached(protocol);
        if (protocol instanceof UMLRTSystemProtocol) return protocol;

        UMLRTProtocol copy = new UMLRTProtocol();
        cache(protocol, copy);
        copy.setName(protocol.getName());
        for (UMLRTCallEvent inputSignal : protocol.getInputSignals())
            copy.addInputSignal(visitCallEvent(inputSignal));
        for (UMLRTCallEvent outputSignal : protocol.getOutputSignals())
            copy.addOutputSignal(visitCallEvent(outputSignal));
        postVisit(protocol, copy);
        return copy;
    }

    @Override
    public UMLRTType visitType(UMLRTType type) {
        if (type instanceof UMLRTPrimitiveType
                || type instanceof UMLRTSystemType) {
            cache(type, type);
            return type;
        }

        if (type instanceof UMLRTEnumeration)
            return visitEnumeration((UMLRTEnumeration) type);

        if (type instanceof UMLRTProtocol)
            return visitProtocol((UMLRTProtocol) type);

        assert type instanceof UMLRTClass;
        return visitClass((UMLRTClass) type);
    }

    @Override
    public UMLRTConnector visitConnector(UMLRTConnector connector) {
        if (visited(connector)) return (UMLRTConnector) cached(connector);

        UMLRTConnector copy = new UMLRTConnector();
        cache(connector, copy);
        copy.setName(connector.getName());
        copy.setEnd1(visitConnectorEnd(connector.getEnd1()));
        copy.setEnd2(visitConnectorEnd(connector.getEnd2()));
        postVisit(connector, copy);
        return copy;
    }

    @Override
    public UMLRTConnectorEnd visitConnectorEnd(UMLRTConnectorEnd connectorEnd) {
        if (visited(connectorEnd)) return (UMLRTConnectorEnd) cached(connectorEnd);

        UMLRTConnectorEnd copy = new UMLRTConnectorEnd();
        cache(connectorEnd, copy);
        copy.setPort(visitPort(connectorEnd.getPort()));
        if (connectorEnd.getPart() != null)
            copy.setPart(visitCapsulePart(connectorEnd.getPart()));
        postVisit(connectorEnd, copy);
        return copy;
    }

    @Override
    public UMLRTAttribute visitAttribute(UMLRTAttribute attribute) {
        if (visited(attribute)) return (UMLRTAttribute) cached(attribute);

        UMLRTAttribute copy = new UMLRTAttribute();
        cache(attribute, copy);
        copy.setName(attribute.getName());
        copy.setReplication(attribute.getReplication());
        copy.setType(visitType(attribute.getType()));
        postVisit(attribute, copy);
        return copy;
    }

    @Override
    public UMLRTParameter visitParameter(UMLRTParameter parameter) {
        if (visited(parameter)) return (UMLRTParameter) cached(parameter);

        UMLRTParameter copy = new UMLRTParameter();
        cache(parameter, copy);
        copy.setName(parameter.getName());
        copy.setReplication(parameter.getReplication());
        copy.setType(visitType(parameter.getType()));
        postVisit(parameter, copy);
        return copy;
    }

    @Override
    public UMLRTOperation visitOperation(UMLRTOperation operation) {
        if (visited(operation)) return (UMLRTOperation) cached(operation);

        UMLRTOperation copy = new UMLRTOperation();
        cache(operation, copy);
        copy.setName(operation.getName());

        for (UMLRTParameter parameter : operation.getParameters())
            copy.addParameter(visitParameter(parameter));

        if (operation.getReturnParam() != null)
            copy.setReturnParam(visitParameter(operation.getReturnParam()));

        if (operation.getCode() != null)
            copy.setCode(visitActionCode(operation.getCode()));
        postVisit(operation, copy);
        return copy;
    }

    @Override
    public UMLRTCallEvent visitCallEvent(UMLRTCallEvent callEvent) {
        if (visited(callEvent)) return (UMLRTCallEvent) cached(callEvent);
        if (callEvent instanceof UMLRTSystemCallEvent) return callEvent;

        UMLRTCallEvent copy = new UMLRTCallEvent();
        cache(callEvent, copy);
        copy.setOperation(visitOperation(callEvent.getOperation()));
        postVisit(callEvent, copy);
        return copy;
    }

    @Override
    public UMLRTStateMachine visitStateMachine(UMLRTStateMachine stateMachine) {
        if (visited(stateMachine)) return (UMLRTStateMachine) cached(stateMachine);

        UMLRTStateMachine copy = new UMLRTStateMachine();
        cache(stateMachine, copy);
        copy.setName(stateMachine.getName());
        if (stateMachine.getRegion() != null)
            copy.setRegion(visitRegion(stateMachine.getRegion()));
        postVisit(stateMachine, copy);
        return copy;
    }

    private UMLRTGenericState visitGenericState(UMLRTGenericState state) {
        if (state instanceof UMLRTPseudoState)
            return visitPseudoState((UMLRTPseudoState) state);
        if (state instanceof UMLRTCompositeState)
            return visitCompositeState((UMLRTCompositeState) state);
        return visitSimpleState((UMLRTState) state);
    }

    @Override
    public UMLRTState visitSimpleState(UMLRTState state) {
        if (visited(state)) return (UMLRTState) cached(state);

        UMLRTState copy = new UMLRTState();
        cache(state, copy);
        copy.setName(state.getName());
        if (state.getEntryCode() != null)
            copy.setEntryCode(visitActionCode(state.getEntryCode()));
        if (state.getExitCode() != null)
            copy.setExitCode(visitActionCode(state.getExitCode()));
        postVisit(state, copy);
        return copy;
    }

    @Override
    public UMLRTCompositeState visitCompositeState(UMLRTCompositeState state) {
        if (visited(state)) return (UMLRTCompositeState) cached(state);

        UMLRTCompositeState copy = new UMLRTCompositeState();
        cache(state, copy);
        copy.setName(state.getName());
        copy.setRegion(visitRegion(state.getRegion()));

        for (UMLRTPseudoState entryPoint : state.getEntryPoints())
            copy.addEntryPoint(visitPseudoState(entryPoint));
        for (UMLRTPseudoState exitPoint : state.getExitPoints())
            copy.addExitPoint(visitPseudoState(exitPoint));

        if (state.getEntryCode() != null)
            copy.setEntryCode(visitActionCode(state.getEntryCode()));
        if (state.getExitCode() != null)
            copy.setExitCode(visitActionCode(state.getExitCode()));
        postVisit(state, copy);
        return copy;
    }

    @Override
    public UMLRTPseudoState visitPseudoState(UMLRTPseudoState state) {
        if (visited(state)) return (UMLRTPseudoState) cached(state);

        UMLRTPseudoState copy = new UMLRTPseudoState();
        cache(state, copy);
        copy.setName(state.getName());
        copy.setKind(state.getKind());
        postVisit(state, copy);
        return copy;
    }

    @Override
    public UMLRTRegion visitRegion(UMLRTRegion region) {
        if (visited(region)) return (UMLRTRegion) cached(region);

        UMLRTRegion copy = new UMLRTRegion();
        cache(region, copy);
        copy.setName(region.getName());
        for (UMLRTGenericState state : region.getStates())
            copy.addState(visitGenericState(state));
        for (UMLRTTransition transition : region.getTransitions())
            copy.addTransition(visitTransition(transition));
        postVisit(region, copy);
        return copy;
    }

    @Override
    public UMLRTTransition visitTransition(UMLRTTransition transition) {
        if (visited(transition)) return (UMLRTTransition) cached(transition);

        UMLRTTransition copy = new UMLRTTransition();
        cache(transition, copy);

        copy.setName(transition.getName());
        copy.setSource(visitGenericState(transition.getSource()));
        copy.setTarget(visitGenericState(transition.getTarget()));

        if (transition.getAction() != null)
            copy.setAction(visitActionCode(transition.getAction()));
        if (transition.getGuard() != null)
            copy.setGuard(visitGuard(transition.getGuard()));
        for (UMLRTTrigger trigger : transition.getTriggers())
            copy.addTrigger(visitTrigger(trigger));
        postVisit(transition, copy);
        return copy;
    }

    @Override
    public UMLRTTrigger visitTrigger(UMLRTTrigger trigger) {
        if (visited(trigger)) return (UMLRTTrigger) cached(trigger);

        UMLRTTrigger copy = new UMLRTTrigger();
        cache(trigger, copy);
        copy.setName(trigger.getName());
        copy.setCallEvent(visitCallEvent(trigger.getCallEvent()));
        for (UMLRTPort port : trigger.getPorts())
            copy.addPort(visitPort(port));
        postVisit(trigger, copy);
        return copy;
    }

    @Override
    public UMLRTActionCode visitActionCode(UMLRTActionCode actionCode) {
        if (visited(actionCode)) return (UMLRTActionCode) cached(actionCode);

        UMLRTActionCode copy = new UMLRTActionCode();
        cache(actionCode, copy);
        copy.setName(actionCode.getName());
        copy.setCode(actionCode.getCode());
        copy.setLanguage(actionCode.getLanguage());
        postVisit(actionCode, copy);
        return copy;
    }

    @Override
    public UMLRTActionCode visitGuard(UMLRTActionCode actionCode) {
        return visitActionCode(actionCode);
    }

    @Override
    public UMLRTArtifactProperties visitArtifactProperties(UMLRTArtifactProperties props) {
        if (visited(props)) return (UMLRTArtifactProperties) cached(props);

        UMLRTArtifactProperties copy = new UMLRTArtifactProperties();
        cache(props, copy);
        copy.setArtifact(visitArtifact(props.getArtifact()));
        copy.setIncludeFile(props.getIncludeFile());
        copy.setSourceFile(props.getSourceFile());
        postVisit(props, copy);
        return copy;
    }

    @Override
    public UMLRTAttributeProperties visitAttributeProperties(UMLRTAttributeProperties props) {
        if (visited(props)) return (UMLRTAttributeProperties) cached(props);

        UMLRTAttributeProperties copy = new UMLRTAttributeProperties();
        cache(props, copy);
        copy.setAttribute(visitAttribute(props.getAttribute()));
        copy.setInitialization(props.getInitialization());
        copy.setKind(props.getKind());
        copy.setSize(props.getSize());
        copy.setType(props.getType());
        copy.setPointsToConstType(props.isPointsToConstType());
        copy.setPointsToVolatileType(props.isPointsToVolatileType());
        copy.setPointsToType(props.isPointsToType());
        copy.setVolatile(props.isVolatile());
        postVisit(props, copy);
        return copy;
    }

    @Override
    public UMLRTCapsuleProperties visitCapsuleProperties(UMLRTCapsuleProperties props) {
        if (visited(props)) return (UMLRTCapsuleProperties) cached(props);

        UMLRTCapsuleProperties copy = new UMLRTCapsuleProperties();
        cache(props, copy);
        copy.setCapsule(visitCapsule(props.getCapsule()));
        copy.setHeaderPreface(props.getHeaderPreface());
        copy.setHeaderEnding(props.getHeaderEnding());
        copy.setImplementationPreface(props.getImplementationPreface());
        copy.setImplementationEnding(props.getImplementationEnding());
        copy.setPublicDeclarations(props.getPublicDeclarations());
        copy.setPrivateDeclarations(props.getPrivateDeclarations());
        copy.setProtectedDeclarations(props.getProtectedDeclarations());
        copy.setGenerateHeader(props.isGenerateHeader());
        copy.setGenerateImplementation(props.isGenerateImplementation());
        postVisit(props, copy);
        return copy;
    }

    @Override
    public UMLRTEnumerationProperties visitEnumerationProperties(UMLRTEnumerationProperties props) {
        if (visited(props)) return (UMLRTEnumerationProperties) cached(props);

        UMLRTEnumerationProperties copy = new UMLRTEnumerationProperties();
        cache(props, copy);
        copy.setEnumeration(visitEnumeration(props.getEnumeration()));
        copy.setHeaderPreface(props.getHeaderPreface());
        copy.setHeaderEnding(props.getHeaderEnding());
        copy.setImplementationPreface(props.getImplementationPreface());
        copy.setImplementationEnding(props.getImplementationEnding());
        copy.setGenerate(props.isGenerate());
        postVisit(props, copy);
        return copy;
    }

    @Override
    public UMLRTOperationProperties visitOperationProperties(UMLRTOperationProperties props) {
        if (visited(props)) return (UMLRTOperationProperties) cached(props);

        UMLRTOperationProperties copy = new UMLRTOperationProperties();
        cache(props, copy);
        copy.setOperation(visitOperation(props.getOperation()));
        copy.setKind(props.getKind());
        copy.setGenerateDefinition(props.isGenerateDefinition());
        copy.setInline(props.isInline());
        copy.setPolymorphic(props.isPolymorphic());
        postVisit(props, copy);
        return copy;
    }

    @Override
    public UMLRTParameterProperties visitParameterProperties(UMLRTParameterProperties props) {
        if (visited(props)) return (UMLRTParameterProperties) cached(props);

        UMLRTParameterProperties copy = new UMLRTParameterProperties();
        cache(props, copy);
        copy.setParameter(visitParameter(props.getParameter()));
        copy.setType(props.getType());
        copy.setPointsToConst(props.isPointsToConst());
        copy.setPointsToVolatile(props.isPointsToVolatile());
        copy.setPointsToType(props.isPointsToType());
        postVisit(props, copy);
        return copy;
    }

    @Override
    public UMLRTPassiveClassProperties visitPassiveClassProperties(UMLRTPassiveClassProperties props) {
        if (visited(props)) return (UMLRTPassiveClassProperties) cached(props);

        UMLRTPassiveClassProperties copy = new UMLRTPassiveClassProperties();
        cache(props, copy);
        copy.setPassiveClass(visitClass(props.getPassiveClass()));
        copy.setKind(props.getKind());
        copy.setHeaderPreface(props.getHeaderPreface());
        copy.setHeaderEnding(props.getHeaderEnding());
        copy.setImplementationPreface(props.getImplementationPreface());
        copy.setImplementationEnding(props.getImplementationEnding());
        copy.setPublicDeclarations(props.getPublicDeclarations());
        copy.setPrivateDeclarations(props.getPrivateDeclarations());
        copy.setProtectedDeclarations(props.getProtectedDeclarations());
        copy.setImplementationType(props.getImplementationType());
        copy.setGenerate(props.isGenerate());
        copy.setGenerateHeader(props.isGenerateHeader());
        copy.setGenerateImplementation(props.isGenerateImplementation());
        copy.setGenerateStateMachine(props.isGenerateStateMachine());
        copy.setGenerateAssignmentOperator(props.isGenerateAssignmentOperator());
        copy.setGenerateEqualityOperator(props.isGenerateEqualityOperator());
        copy.setGenerateInequalityOperator(props.isGenerateInequalityOperator());
        copy.setGenerateInsertionOperator(props.isGenerateInsertionOperator());
        copy.setGenerateExtractionOperator(props.isGenerateExtractionOperator());
        copy.setGenerateCopyConstructor(props.isGenerateCopyConstructor());
        copy.setGenerateDefaultConstructor(props.isGenerateDefaultConstructor());
        copy.setGenerateDestructor(props.isGenerateDestructor());
        postVisit(props, copy);
        return copy;
    }

    @Override
    public UMLRTTypeProperties visitTypeProperties(UMLRTTypeProperties props) {
        if (visited(props)) return (UMLRTTypeProperties) cached(props);

        UMLRTTypeProperties copy = new UMLRTTypeProperties();
        cache(props, copy);
        copy.setName(props.getName());
        copy.setDefinitionFile(props.getDefinitionFile());
        postVisit(props, copy);
        return copy;
    }

    public UMLRTProperties visitProperties(UMLRTProperties properties) {
        if (properties instanceof UMLRTArtifactProperties)
            return visitArtifactProperties((UMLRTArtifactProperties) properties);
        if (properties instanceof UMLRTCapsuleProperties)
            return visitCapsuleProperties((UMLRTCapsuleProperties) properties);
        if (properties instanceof UMLRTEnumerationProperties)
            return visitEnumerationProperties((UMLRTEnumerationProperties) properties);
        if (properties instanceof UMLRTOperationProperties)
            return visitOperationProperties((UMLRTOperationProperties) properties);
        if (properties instanceof UMLRTParameterProperties)
            return visitParameterProperties((UMLRTParameterProperties) properties);
        if (properties instanceof UMLRTPassiveClassProperties)
            return visitPassiveClassProperties((UMLRTPassiveClassProperties) properties);
        if (properties instanceof UMLRTAttributeProperties)
            return visitAttributeProperties((UMLRTAttributeProperties) properties);
        if (properties instanceof UMLRTTypeProperties)
            return visitTypeProperties((UMLRTTypeProperties) properties);
        throw new RuntimeException("Unexpected properties class " + properties.getClass().getSimpleName());
    }

    public interface CopyListener {
        void onCopy(UMLRTElement original, UMLRTElement copy);
    }
}
