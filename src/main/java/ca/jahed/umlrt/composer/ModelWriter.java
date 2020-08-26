package ca.jahed.umlrt.composer;

import ca.jahed.umlrt.composer.model.*;
import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.model.properties.*;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemCallEvent;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemProtocol;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemType;
import ca.jahed.umlrt.composer.model.sm.*;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.papyrusrt.codegen.cpp.profile.RTCppProperties.*;
import org.eclipse.papyrusrt.umlrt.profile.UMLRealTime.*;
import org.eclipse.papyrusrt.umlrt.profile.statemachine.UMLRTStateMachines.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.*;

public class ModelWriter extends CachedVisitor implements UMLRTVisitor, UMLRTPropertiesVisitor {
    final Resource resource;

    public ModelWriter(Resource resource) {
        this.resource = resource;
    }

    public static ModelWriter getWriter(Resource resource) {
        return new ModelWriter(resource);
    }

    public static Model write(Resource resource, UMLRTModel root) {
        ModelWriter writer = getWriter(resource);
        Model model = writer.visitModel(root);
        writer.resource.getContents().add(model);
        return model;
    }

    void postVisit(UMLRTElement element, EObject eObj) {
        element.setEObj(eObj);
        element.getProperties().forEach(this::visitProperties);

        if (UMLRTUtils.getRtClassifiers().contains(eObj.eClass()))
            resource.getContents().add(eObj);
    }

    @Override
    public Model visitModel(UMLRTModel model) {
        Model m = UMLFactory.eINSTANCE.createModel();
        cache(model, m);
        m.setName(model.getName());

        EAnnotation topAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
        topAnnotation.setSource("UMLRT_Default_top");
        topAnnotation.getDetails().put("top_name", model.getTop().getCapsule().getName());
        m.getEAnnotations().add(topAnnotation);

        EAnnotation langAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
        langAnnotation.setSource("http://www.eclipse.org/papyrus-rt/language/1.0.0");
        langAnnotation.getDetails().put("language", "umlrt-cpp");
        m.getEAnnotations().add(langAnnotation);

        m.applyProfile(UMLRTUtils.getProfile(UMLRTUtils.SysProfile.UMLRealTime));
        m.applyProfile(UMLRTUtils.getProfile(UMLRTUtils.SysProfile.UMLRTStateMachines));
        m.applyProfile(UMLRTUtils.getProfile(UMLRTUtils.SysProfile.RTCppProperties));

        traversePackage(model, m);
        postVisit(model, m);
        return m;
    }

    @Override
    public Package visitPackage(UMLRTPackage pkg) {
        if (visited(pkg)) return (Package) cached(pkg);

        Package p = UMLFactory.eINSTANCE.createPackage();
        cache(pkg, p);
        p.setName(pkg.getName());
        traversePackage(pkg, p);
        postVisit(pkg, p);
        return p;
    }

    private void traversePackage(UMLRTPackage pkg, Package p) {
        for (UMLRTPackage nested : pkg.getPackages())
            p.getPackagedElements().add(visitPackage(nested));
        for (UMLRTClass passiveClass : pkg.getPassiveClasses())
            p.getPackagedElements().add(visitClass(passiveClass));
        for (UMLRTCapsule capsule : pkg.getCapsules())
            p.getPackagedElements().add(visitCapsule(capsule).getBase_Class());
        for (UMLRTProtocol protocol : pkg.getProtocols())
            p.getPackagedElements().add(visitProtocol(protocol).getBase_Package());
        for (UMLRTEnumeration enumeration : pkg.getEnumerations())
            p.getPackagedElements().add(visitEnumeration(enumeration));
        for (UMLRTArtifact artifact : pkg.getArtifacts())
            p.getPackagedElements().add(visitArtifact(artifact));
    }

    @Override
    public Class visitClass(UMLRTClass passiveClass) {
        if (visited(passiveClass)) return (Class) cached(passiveClass);

        Class c = UMLFactory.eINSTANCE.createClass();
        cache(passiveClass, c);
        c.setName(passiveClass.getName());

        for (UMLRTAttribute attribute : passiveClass.getAttributes())
            c.getOwnedAttributes().add(visitAttribute(attribute));

        for (UMLRTOperation operation : passiveClass.getOperations()) {
            Operation o = visitOperation(operation);
            c.getOwnedOperations().add(o);
            if (!o.getMethods().isEmpty())
                c.getOwnedBehaviors().add(o.getMethods().get(0));
        }
        postVisit(passiveClass, c);
        return c;
    }

    @Override
    public Artifact visitArtifact(UMLRTArtifact artifact) {
        if (visited(artifact)) return (Artifact) cached(artifact);

        Artifact a = UMLFactory.eINSTANCE.createArtifact();
        cache(artifact, a);
        a.setName(artifact.getName());
        if (artifact.getFileName() != null)
            a.setFileName(artifact.getName());

        for (UMLRTAttribute attribute : artifact.getAttributes())
            a.getOwnedAttributes().add(visitAttribute(attribute));
        for (UMLRTOperation operation : artifact.getOperations())
            a.getOwnedOperations().add(visitOperation(operation));
        postVisit(artifact, a);
        return a;
    }

    @Override
    public Capsule visitCapsule(UMLRTCapsule capsule) {
        if (visited(capsule)) return (Capsule) cached(capsule);

        Capsule caps = UMLRealTimeFactory.eINSTANCE.createCapsule();
        cache(capsule, caps);

        Class c = visitClass(capsule.getBaseClass());
        caps.setBase_Class(c);

        for (UMLRTPort port : capsule.getPorts())
            c.getOwnedPorts().add(visitPort(port).getBase_Port());

        for (UMLRTCapsulePart part : capsule.getParts())
            c.getOwnedAttributes().add(visitCapsulePart(part).getBase_Property());

        if (capsule.getStateMachine() != null)
            c.getOwnedBehaviors().add(visitStateMachine(capsule.getStateMachine()).getBase_StateMachine());

        for (UMLRTConnector connector : capsule.getConnectors())
            c.getOwnedConnectors().add(visitConnector(connector).getBase_Connector());

        postVisit(capsule, caps);
        return caps;
    }

    @Override
    public Enumeration visitEnumeration(UMLRTEnumeration enumeration) {
        if (visited(enumeration)) return (Enumeration) cached(enumeration);

        Enumeration e = UMLFactory.eINSTANCE.createEnumeration();
        cache(enumeration, e);
        e.setName(enumeration.getName());

        for (String literal : enumeration.getLiterals()) {
            EnumerationLiteral l = UMLFactory.eINSTANCE.createEnumerationLiteral();
            l.setName(literal);
            e.getOwnedLiterals().add(l);
        }

        postVisit(enumeration, e);
        return e;
    }

    @Override
    public CapsulePart visitCapsulePart(UMLRTCapsulePart capsulePart) {
        if (visited(capsulePart)) return (CapsulePart) cached(capsulePart);

        CapsulePart c = UMLRealTimeFactory.eINSTANCE.createCapsulePart();
        cache(capsulePart, c);

        Property p = visitAttribute(capsulePart.getAttribute());
        p.setType(visitCapsule(capsulePart.getCapsule()).getBase_Class());
        c.setBase_Property(p);

        if (capsulePart.isOptional())
            p.setLower(0);
        if (capsulePart.isPlugin())
            p.setAggregation(AggregationKind.SHARED_LITERAL);
        postVisit(capsulePart, c);
        return c;
    }

    @Override
    public RTPort visitPort(UMLRTPort port) {
        if (visited(port)) return (RTPort) cached(port);

        RTPort rtp = UMLRealTimeFactory.eINSTANCE.createRTPort();
        cache(port, rtp);

        Port p = UMLFactory.eINSTANCE.createPort();
        p.setName(port.getName());
        p.setIsBehavior(port.isBehavior());
        p.setIsConjugated(port.isConjugated());
        p.setIsService(port.isService());
        p.setUpper(port.getReplication());
        p.setType((Type) visitProtocol(port.getProtocol())
                .getBase_Package().getPackagedElements().get(0));

        rtp.setBase_Port(p);
        rtp.setIsPublish(port.isPublish());
        rtp.setIsWired(port.isWired());
        rtp.setIsNotification(port.isNotification());
        rtp.setRegistration(PortRegistrationType.get(port.getRegistrationType().ordinal()));
        rtp.setRegistrationOverride(port.getRegistrationOverride());
        postVisit(port, rtp);
        return rtp;
    }

    @Override
    public ProtocolContainer visitProtocol(UMLRTProtocol protocol) {
        if (visited(protocol)) return (ProtocolContainer) cached(protocol);

        if (protocol instanceof UMLRTSystemProtocol)
            return (ProtocolContainer) protocol.getEObj();

        ProtocolContainer rtc = UMLRealTimeFactory.eINSTANCE.createProtocolContainer();
        cache(protocol, rtc);

        Package p = UMLFactory.eINSTANCE.createPackage();
        p.setName(protocol.getName());
        rtc.setBase_Package(p);

        Collaboration c = UMLFactory.eINSTANCE.createCollaboration();
        c.setName(protocol.getName());
        p.getPackagedElements().add(c);

        Interface i1 = UMLFactory.eINSTANCE.createInterface();
        i1.setName(protocol.getName());
        p.getPackagedElements().add(i1);

        for (UMLRTCallEvent signal : protocol.getInputSignals())
            i1.getOwnedOperations().add(visitOperation(signal.getOperation()));

        Interface i2 = UMLFactory.eINSTANCE.createInterface();
        i2.setName(protocol.getName() + "~");
        p.getPackagedElements().add(i2);

        for (UMLRTCallEvent signal : protocol.getOutputSignals())
            i2.getOwnedOperations().add(visitOperation(signal.getOperation()));

        Interface i3 = UMLFactory.eINSTANCE.createInterface();
        i3.setName(protocol.getName() + "IO");
        p.getPackagedElements().add(i3);

        InterfaceRealization ir1 = UMLFactory.eINSTANCE.createInterfaceRealization();
        ir1.getClients().add(c);
        ir1.getSuppliers().add(i1);
        ir1.setContract(i1);
        c.getInterfaceRealizations().add(ir1);

        InterfaceRealization ir2 = UMLFactory.eINSTANCE.createInterfaceRealization();
        ir2.getClients().add(c);
        ir2.getSuppliers().add(i3);
        ir2.setContract(i3);
        c.getInterfaceRealizations().add(ir2);

        Usage u1 = UMLFactory.eINSTANCE.createUsage();
        u1.getClients().add(c);
        u1.getSuppliers().add(i2);
        p.getPackagedElements().add(u1);

        Usage u2 = UMLFactory.eINSTANCE.createUsage();
        u2.getClients().add(c);
        u2.getSuppliers().add(i3);
        p.getPackagedElements().add(u2);

        AnyReceiveEvent e1 = UMLFactory.eINSTANCE.createAnyReceiveEvent();
        e1.setName("*");
        p.getPackagedElements().add(e1);

        for (UMLRTCallEvent inputSignal : protocol.getInputSignals())
            p.getPackagedElements().add(visitCallEvent(inputSignal));
        for (UMLRTCallEvent outputSignal : protocol.getOutputSignals())
            p.getPackagedElements().add(visitCallEvent(outputSignal));

        Protocol rtp = UMLRealTimeFactory.eINSTANCE.createProtocol();
        rtp.setBase_Collaboration(c);
        resource.getContents().add(rtp);

        RTMessageSet rtm1 = UMLRealTimeFactory.eINSTANCE.createRTMessageSet();
        rtm1.setRtMsgKind(RTMessageKind.IN);
        rtm1.setBase_Interface(i1);
        resource.getContents().add(rtm1);

        RTMessageSet rtm2 = UMLRealTimeFactory.eINSTANCE.createRTMessageSet();
        rtm2.setRtMsgKind(RTMessageKind.OUT);
        rtm2.setBase_Interface(i2);
        resource.getContents().add(rtm2);

        RTMessageSet rtm3 = UMLRealTimeFactory.eINSTANCE.createRTMessageSet();
        rtm3.setRtMsgKind(RTMessageKind.IN_OUT);
        rtm3.setBase_Interface(i3);
        resource.getContents().add(rtm3);
        postVisit(protocol, rtc);
        return rtc;
    }

    @Override
    public Type visitType(UMLRTType type) {
        if (type instanceof UMLRTPrimitiveType)
            return UMLRTUtils.getPrimitiveType(type.getName());

        if (type instanceof UMLRTSystemType)
            return (Type) type.getEObj();

        if (type instanceof UMLRTEnumeration)
            return visitEnumeration((UMLRTEnumeration) type);

        if (type instanceof UMLRTClass)
            return visitClass((UMLRTClass) type);
        throw new RuntimeException("Unexpected type class " + type.getClass().getSimpleName());
    }

    @Override
    public RTConnector visitConnector(UMLRTConnector connector) {
        if (visited(connector)) return (RTConnector) cached(connector);

        RTConnector rtc = UMLRealTimeFactory.eINSTANCE.createRTConnector();
        cache(connector, rtc);

        Connector c = UMLFactory.eINSTANCE.createConnector();
        c.setName(connector.getName());
        c.getEnds().add(visitConnectorEnd(connector.getEnd1()));
        c.getEnds().add(visitConnectorEnd(connector.getEnd2()));
        rtc.setBase_Connector(c);
        postVisit(connector, rtc);
        return rtc;
    }

    @Override
    public ConnectorEnd visitConnectorEnd(UMLRTConnectorEnd connectorEnd) {
        if (visited(connectorEnd)) return (ConnectorEnd) cached(connectorEnd);

        ConnectorEnd c = UMLFactory.eINSTANCE.createConnectorEnd();
        cache(connectorEnd, c);
        c.setRole(visitPort(connectorEnd.getPort()).getBase_Port());
        if (connectorEnd.getPart() != null)
            c.setPartWithPort(visitCapsulePart(connectorEnd.getPart()).getBase_Property());
        postVisit(connectorEnd, c);
        return c;
    }

    @Override
    public Property visitAttribute(UMLRTAttribute attribute) {
        if (visited(attribute)) return (Property) cached(attribute);

        Property p = UMLFactory.eINSTANCE.createProperty();
        cache(attribute, p);
        p.setName(attribute.getName());
        p.setType(visitType(attribute.getType()));
        p.setUpper(attribute.getReplication());
        postVisit(attribute, p);
        return p;
    }

    @Override
    public Parameter visitParameter(UMLRTParameter parameter) {
        if (visited(parameter)) return (Parameter) cached(parameter);

        Parameter p = UMLFactory.eINSTANCE.createParameter();
        cache(parameter, p);
        p.setName(parameter.getName());
        p.setType(visitType(parameter.getType()));
        p.setUpper(parameter.getReplication());
        p.setDirection(ParameterDirectionKind.IN_LITERAL);
        postVisit(parameter, p);
        return p;
    }

    @Override
    public Operation visitOperation(UMLRTOperation operation) {
        if (visited(operation)) return (Operation) cached(operation);

        Operation o = UMLFactory.eINSTANCE.createOperation();
        cache(operation, o);
        o.setName(operation.getName());

        for (UMLRTParameter parameter : operation.getParameters())
            o.getOwnedParameters().add(visitParameter(parameter));

        if (operation.getReturnParam() != null) {
            Parameter ret = visitParameter(operation.getReturnParam());
            ret.setDirection(ParameterDirectionKind.RETURN_LITERAL);
            o.getOwnedParameters().add(ret);
        }

        if (operation.getCode() != null)
            o.getMethods().add(visitActionCode(operation.getCode()));
        postVisit(operation, o);
        return o;
    }

    @Override
    public CallEvent visitCallEvent(UMLRTCallEvent callEvent) {
        if (visited(callEvent)) return (CallEvent) cached(callEvent);

        if (callEvent instanceof UMLRTSystemCallEvent)
            return (CallEvent) callEvent.getEObj();

        CallEvent c = UMLFactory.eINSTANCE.createCallEvent();
        cache(callEvent, c);
        c.setName(callEvent.getName());

        if (callEvent.getOperation() != null)
            c.setOperation(visitOperation(callEvent.getOperation()));
        postVisit(callEvent, c);
        return c;
    }

    @Override
    public RTStateMachine visitStateMachine(UMLRTStateMachine stateMachine) {
        if (visited(stateMachine)) return (RTStateMachine) cached(stateMachine);

        RTStateMachine rts = UMLRTStateMachinesFactory.eINSTANCE.createRTStateMachine();
        cache(stateMachine, rts);

        StateMachine s = UMLFactory.eINSTANCE.createStateMachine();
        s.setName(stateMachine.getName());
        rts.setBase_StateMachine(s);

        if (stateMachine.getRegion() != null)
            s.getRegions().add(visitRegion(stateMachine.getRegion()).getBase_Region());
        postVisit(stateMachine, rts);
        return rts;
    }

    public RTState visitState(UMLRTState state) {
        if (state instanceof UMLRTCompositeState)
            return visitCompositeState((UMLRTCompositeState) state);
        return visitSimpleState(state);
    }

    @Override
    public RTState visitSimpleState(UMLRTState state) {
        if (visited(state)) return (RTState) cached(state);

        RTState rts = UMLRTStateMachinesFactory.eINSTANCE.createRTState();
        cache(state, rts);

        State s = UMLFactory.eINSTANCE.createState();
        s.setName(state.getName());
        rts.setBase_State(s);

        if (state.getEntryCode() != null)
            s.setEntry(visitActionCode(state.getEntryCode()));
        if (state.getExitCode() != null)
            s.setExit(visitActionCode(state.getExitCode()));
        postVisit(state, rts);
        return rts;
    }

    @Override
    public RTState visitCompositeState(UMLRTCompositeState state) {
        if (visited(state)) return (RTState) cached(state);

        RTState rts = UMLRTStateMachinesFactory.eINSTANCE.createRTState();
        cache(state, rts);

        State s = UMLFactory.eINSTANCE.createState();
        s.setName(state.getName());
        rts.setBase_State(s);

        if (state.getEntryCode() != null)
            s.setEntry(visitActionCode(state.getEntryCode()));
        if (state.getExitCode() != null)
            s.setExit(visitActionCode(state.getExitCode()));

        if (state.getRegion() != null)
            s.getRegions().add(visitRegion(state.getRegion()).getBase_Region());

        for (UMLRTPseudoState entryPoint : state.getEntryPoints())
            s.getConnectionPoints().add(visitPseudoState(entryPoint).getBase_Pseudostate());
        for (UMLRTPseudoState exitPoint : state.getExitPoints())
            s.getConnectionPoints().add(visitPseudoState(exitPoint).getBase_Pseudostate());
        postVisit(state, rts);
        return rts;
    }

    @Override
    public RTPseudostate visitPseudoState(UMLRTPseudoState state) {
        if (visited(state)) return (RTPseudostate) cached(state);

        RTPseudostate rts = UMLRTStateMachinesFactory.eINSTANCE.createRTPseudostate();
        cache(state, rts);

        Pseudostate s = UMLFactory.eINSTANCE.createPseudostate();
        s.setName(state.getName());
        s.setKind(PseudostateKind.get(state.getKind().ordinal()));
        rts.setBase_Pseudostate(s);
        postVisit(state, rts);
        return rts;
    }

    @Override
    public RTRegion visitRegion(UMLRTRegion region) {
        if (visited(region)) return (RTRegion) cached(region);

        RTRegion rtr = UMLRTStateMachinesFactory.eINSTANCE.createRTRegion();
        cache(region, rtr);

        Region r = UMLFactory.eINSTANCE.createRegion();
        r.setName(region.getName());
        rtr.setBase_Region(r);

        for (UMLRTGenericState state : region.getStates()) {
            if (state instanceof UMLRTPseudoState)
                r.getSubvertices().add(visitPseudoState((UMLRTPseudoState) state).getBase_Pseudostate());
            else
                r.getSubvertices().add(visitState((UMLRTState) state).getBase_State());
        }

        for (UMLRTTransition transition : region.getTransitions())
            r.getTransitions().add(visitTransition(transition));
        postVisit(region, rtr);
        return rtr;
    }

    @Override
    public Transition visitTransition(UMLRTTransition transition) {
        if (visited(transition)) return (Transition) cached(transition);

        Transition t = UMLFactory.eINSTANCE.createTransition();
        cache(transition, t);
        t.setName(transition.getName());

        if (transition.getSource() != null) {
            Vertex v = transition.getSource() instanceof UMLRTPseudoState
                    ? visitPseudoState((UMLRTPseudoState) transition.getSource()).getBase_Pseudostate()
                    : visitState((UMLRTState) transition.getSource()).getBase_State();
            t.setSource(v);
        }
        if (transition.getTarget() != null) {
            Vertex v = transition.getTarget() instanceof UMLRTPseudoState
                    ? visitPseudoState((UMLRTPseudoState) transition.getTarget()).getBase_Pseudostate()
                    : visitState((UMLRTState) transition.getTarget()).getBase_State();
            t.setTarget(v);
        }

        if (transition.getAction() != null)
            t.setEffect(visitActionCode(transition.getAction()));
        if (transition.getGuard() != null)
            t.setGuard(visitGuard(transition.getGuard()));

        for (UMLRTTrigger trigger : transition.getTriggers())
            t.getTriggers().add(visitTrigger(trigger));
        postVisit(transition, t);
        return t;
    }

    @Override
    public Trigger visitTrigger(UMLRTTrigger trigger) {
        if (visited(trigger)) return (Trigger) cached(trigger);

        Trigger t = UMLFactory.eINSTANCE.createTrigger();
        cache(trigger, t);
        t.setName(trigger.getName());

        for (UMLRTPort port : trigger.getPorts())
            t.getPorts().add(visitPort(port).getBase_Port());

        if (trigger.getCallEvent() != null)
            t.setEvent(visitCallEvent(trigger.getCallEvent()));
        postVisit(trigger, t);
        return t;
    }

    @Override
    public Behavior visitActionCode(UMLRTActionCode actionCode) {
        if (visited(actionCode)) return (Behavior) cached(actionCode);

        OpaqueBehavior o = UMLFactory.eINSTANCE.createOpaqueBehavior();
        cache(actionCode, o);
        o.setName(actionCode.getName());
        o.getBodies().add(actionCode.getCode());
        if (actionCode.getLanguage() != null)
            o.getLanguages().add(actionCode.getLanguage());
        postVisit(actionCode, o);
        return o;
    }

    @Override
    public Constraint visitGuard(UMLRTActionCode actionCode) {
        if (visited(actionCode)) return (Constraint) cached(actionCode);

        Constraint c = UMLFactory.eINSTANCE.createConstraint();
        cache(actionCode, c);

        OpaqueExpression o = UMLFactory.eINSTANCE.createOpaqueExpression();
        o.setName(actionCode.getName());
        o.getBodies().add(actionCode.getCode());
        c.setSpecification(o);

        if (actionCode.getLanguage() != null)
            o.getLanguages().add(actionCode.getLanguage());
        postVisit(actionCode, c);
        return c;
    }

    @Override
    public ArtifactProperties visitArtifactProperties(UMLRTArtifactProperties props) {
        if (visited(props)) return (ArtifactProperties) cached(props);

        ArtifactProperties a = RTCppPropertiesFactory.eINSTANCE.createArtifactProperties();
        cache(props, a);
        a.setBase_Artifact(visitArtifact(props.getArtifact()));
        a.setIncludeFile(props.getIncludeFile());
        a.setSourceFile(props.getSourceFile());
        postVisit(props, a);
        return a;
    }

    @Override
    public AttributeProperties visitAttributeProperties(UMLRTAttributeProperties props) {
        if (visited(props)) return (AttributeProperties) cached(props);

        AttributeProperties a = RTCppPropertiesFactory.eINSTANCE.createAttributeProperties();
        cache(props, a);
        a.setBase_Property(visitAttribute(props.getAttribute()));
        a.setInitialization(InitializationKind.get(props.getInitialization().ordinal()));
        a.setKind(AttributeKind.get(props.getKind().ordinal()));
        a.setSize(props.getSize());
        a.setType(props.getType());
        a.setVolatile(props.isVolatile());
        a.setPointsToConstType(props.isPointsToConstType());
        a.setPointsToType(props.isPointsToType());
        a.setPointsToVolatileType(props.isPointsToVolatileType());
        postVisit(props, a);
        return a;
    }

    @Override
    public CapsuleProperties visitCapsuleProperties(UMLRTCapsuleProperties props) {
        if (visited(props)) return (CapsuleProperties) cached(props);

        CapsuleProperties c = RTCppPropertiesFactory.eINSTANCE.createCapsuleProperties();
        cache(props, c);
        c.setBase_Class(visitClass(props.getCapsule().getBaseClass()));
        c.setHeaderEnding(props.getHeaderEnding());
        c.setHeaderPreface(props.getHeaderPreface());
        c.setImplementationPreface(props.getImplementationPreface());
        c.setImplementationEnding(props.getImplementationEnding());
        c.setPublicDeclarations(props.getPublicDeclarations());
        c.setPrivateDeclarations(props.getPrivateDeclarations());
        c.setProtectedDeclarations(props.getProtectedDeclarations());
        c.setGenerateHeader(props.isGenerateHeader());
        c.setGenerateImplementation(props.isGenerateImplementation());
        postVisit(props, c);
        return c;
    }

    @Override
    public EnumerationProperties visitEnumerationProperties(UMLRTEnumerationProperties props) {
        if (visited(props)) return (EnumerationProperties) cached(props);

        EnumerationProperties e = RTCppPropertiesFactory.eINSTANCE.createEnumerationProperties();
        cache(props, e);
        e.setBase_Enumeration(visitEnumeration(props.getEnumeration()));
        e.setHeaderPreface(props.getHeaderPreface());
        e.setHeaderEnding(props.getHeaderEnding());
        e.setImplementationPreface(props.getImplementationPreface());
        e.setImplementationEnding(props.getImplementationEnding());
        e.setGenerate(props.isGenerate());
        postVisit(props, e);
        return e;
    }

    @Override
    public OperationProperties visitOperationProperties(UMLRTOperationProperties props) {
        if (visited(props)) return (OperationProperties) cached(props);

        OperationProperties o = RTCppPropertiesFactory.eINSTANCE.createOperationProperties();
        cache(props, o);
        o.setBase_Operation(visitOperation(props.getOperation()));
        o.setGenerateDefinition(props.isGenerateDefinition());
        o.setInline(props.isInline());
        o.setPolymorphic(props.isPolymorphic());
        o.setKind(OperationKind.get(props.getKind().ordinal()));
        postVisit(props, o);
        return o;
    }

    @Override
    public ParameterProperties visitParameterProperties(UMLRTParameterProperties props) {
        if (visited(props)) return (ParameterProperties) cached(props);

        ParameterProperties p = RTCppPropertiesFactory.eINSTANCE.createParameterProperties();
        cache(props, p);
        p.setBase_Parameter(visitParameter(props.getParameter()));
        p.setType(props.getType());
        p.setPointsToConst(props.isPointsToConst());
        p.setPointsToType(props.isPointsToType());
        p.setPointsToVolatile(props.isPointsToVolatile());
        postVisit(props, p);
        return p;
    }

    @Override
    public PassiveClassProperties visitPassiveClassProperties(UMLRTPassiveClassProperties props) {
        if (visited(props)) return (PassiveClassProperties) cached(props);

        PassiveClassProperties p = RTCppPropertiesFactory.eINSTANCE.createPassiveClassProperties();
        cache(props, p);
        p.setBase_Class(visitClass(props.getPassiveClass()));
        p.setHeaderEnding(props.getHeaderEnding());
        p.setHeaderPreface(props.getHeaderPreface());
        p.setImplementationPreface(props.getImplementationPreface());
        p.setImplementationEnding(props.getImplementationEnding());
        p.setPublicDeclarations(props.getPublicDeclarations());
        p.setPrivateDeclarations(props.getPrivateDeclarations());
        p.setProtectedDeclarations(props.getProtectedDeclarations());
        p.setGenerateHeader(props.isGenerateHeader());
        p.setGenerateImplementation(props.isGenerateImplementation());
        p.setImplementationType(props.getImplementationType());
        p.setKind(ClassKind.get(props.getKind().ordinal()));
        p.setGenerate(props.isGenerate());
        p.setGenerateCopyConstructor(props.isGenerateCopyConstructor());
        p.setGenerateDefaultConstructor(props.isGenerateDefaultConstructor());
        p.setGenerateDestructor(props.isGenerateDestructor());
        p.setGenerateEqualityOperator(props.isGenerateEqualityOperator());
        p.setGenerateExtractionOperator(props.isGenerateExtractionOperator());
        p.setGenerateInequalityOperator(props.isGenerateInequalityOperator());
        p.setGenerateInsertionOperator(props.isGenerateInsertionOperator());
        p.setGenerateAssignmentOperator(props.isGenerateAssignmentOperator());
        p.setGenerateStateMachine(props.isGenerateStateMachine());
        postVisit(props, p);
        return p;
    }

    @Override
    public TypeProperties visitTypeProperties(UMLRTTypeProperties props) {
        if (visited(props)) return (TypeProperties) cached(props);

        TypeProperties t = RTCppPropertiesFactory.eINSTANCE.createTypeProperties();
        cache(props, t);
        t.setDefinitionFile(props.getDefinitionFile());
        t.setName(props.getName());
        postVisit(props, t);
        return t;
    }

    public EObject visitProperties(UMLRTProperties properties) {
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
}
