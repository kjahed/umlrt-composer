package ca.jahed.umlrt.composer;

import ca.jahed.umlrt.composer.model.*;
import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.model.properties.*;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemCallEvent;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemProtocol;
import ca.jahed.umlrt.composer.model.rts.UMLRTSystemType;
import ca.jahed.umlrt.composer.model.sm.*;
import ca.jahed.umlrt.composer.utils.EMFUtils;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.papyrusrt.codegen.cpp.profile.RTCppProperties.*;
import org.eclipse.papyrusrt.umlrt.profile.UMLRealTime.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.*;

import java.util.*;
import java.util.stream.Collectors;

public class ModelReader extends CachedVisitor implements ModelVisitor {
    final Resource resource;
    final Map<EClassifier, List<EObject>> content = new HashMap<>();

    public ModelReader(Resource resource) {
        this.resource = resource;
        for (Resource res : resource.getResourceSet().getResources()) {
            TreeIterator<EObject> iterator = res.getAllContents();
            while(iterator.hasNext()) {
                EObject eObj = iterator.next();
                Collection<EObject> objects = content.computeIfAbsent(eObj.eClass(), k -> new ArrayList<>());
                objects.add(eObj);
            }
        }
    }

    public static ModelReader getReader(Resource resource) {
        return new ModelReader(resource);
    }

    public static UMLRTModel read(Resource resource) {
        return getReader(resource).read();
    }

    private UMLRTModel read() {
        return visitModel((Model) EMFUtils.getObjectByType(
                resource.getContents(), UMLPackage.Literals.MODEL));
    }

    void postVisit(EObject eObj, UMLRTElement element) {
        element.setEObj(eObj);
        EMFUtils.getReferencingObjectsByType(content,
                UMLRTUtils.getRtCppClassifiers(), eObj)
                .forEach(prop -> element.addProperties(visitProperties(prop)));
    }

    @Override
    public UMLRTModel visitModel(Model model) {
        String topName = "Top";
        EAnnotation topAnnotation = model.getEAnnotation("UMLRT_Default_top");
        if(topAnnotation != null && topAnnotation.getDetails().containsKey("top_name"))
            topName = topAnnotation.getDetails().get("top_name");
        assert topName != null;

        UMLRTModel umlrtModel = new UMLRTModel();
        umlrtModel.setName(model.getName());
        umlrtModel.loadPackage(visitPackage(model));
        umlrtModel.setTop(visitTop(topName));
        postVisit(model, umlrtModel);
        return umlrtModel;
    }

    public UMLRTCapsulePart visitTop(String topName) {
        Class topClass = (Class) EMFUtils.getObjectByType(content, UMLPackage.Literals.CLASS,
                Collections.singletonMap("name", topName));

        Capsule topCapsule = (Capsule) EMFUtils.getReferencingObjectByType(content,
                UMLRealTimePackage.Literals.CAPSULE, topClass);
        assert topCapsule != null;

        UMLRTAttribute umlrtAttribute = new UMLRTAttribute();
        umlrtAttribute.setName(topName);
        umlrtAttribute.setType(visitClass(topClass));
        umlrtAttribute.setReplication(1);

        UMLRTCapsulePart umlrtCapsulePart = new UMLRTCapsulePart();
        umlrtCapsulePart.setName(topName);
        umlrtCapsulePart.setAttribute(umlrtAttribute);
        umlrtCapsulePart.setPlugin(false);
        umlrtCapsulePart.setOptional(false);
        umlrtCapsulePart.setCapsule(visitCapsule(topCapsule));
        return umlrtCapsulePart;
    }

    @Override
    public UMLRTPackage visitPackage(Package pkg) {
        if (visited(pkg)) return (UMLRTPackage) cached(pkg);

        UMLRTPackage umlrtPackage = new UMLRTPackage();
        cache(pkg, umlrtPackage);
        umlrtPackage.setName(pkg.getName());

        for (PackageableElement packagedElement : pkg.getPackagedElements()) {
            if(packagedElement instanceof Class) {
                Capsule capsule = (Capsule) EMFUtils.getReferencingObjectByType(content,
                        UMLRealTimePackage.Literals.CAPSULE, packagedElement);

                if(capsule != null)
                    umlrtPackage.addCapsule(visitCapsule(capsule));
                else
                    umlrtPackage.addPassiveClass(visitClass((Class) packagedElement));
            }
            else if(packagedElement instanceof Package) {
                ProtocolContainer protocolContainer = (ProtocolContainer) EMFUtils.getReferencingObjectByType(content,
                        UMLRealTimePackage.Literals.PROTOCOL_CONTAINER, packagedElement);

                if(protocolContainer != null)
                    umlrtPackage.addProtocol(visitProtocol(protocolContainer));
                else
                    umlrtPackage.addPackage(visitPackage((Package) packagedElement));
            }
            else if(packagedElement instanceof Enumeration) {
                umlrtPackage.addEnumeration(visitEnumeration((Enumeration) packagedElement));
            }
            else if(packagedElement instanceof Artifact) {
                umlrtPackage.addArtifact(visitArtifact((Artifact) packagedElement));
            }
        }

        postVisit(pkg, umlrtPackage);
        return umlrtPackage;
    }

    @Override
    public UMLRTCapsule visitCapsule(Capsule capsule) {
        if (visited(capsule)) return (UMLRTCapsule) cached(capsule);

        Class cls = capsule.getBase_Class();
        UMLRTCapsule umlrtCapsule = new UMLRTCapsule();
        cache(capsule, umlrtCapsule);
        umlrtCapsule.setName(cls.getName());
        umlrtCapsule.setBaseClass(visitClass(cls));

        for(Port port : cls.getOwnedPorts())
            umlrtCapsule.addPort(visitPort(port));

        for(Property property : cls.getOwnedAttributes()) {
            CapsulePart part = (CapsulePart) EMFUtils.getReferencingObjectByType(content,
                    UMLRealTimePackage.Literals.CAPSULE_PART, property);

            if(part != null)
                umlrtCapsule.addPart(visitCapsulePart(part));
        }

        for(Connector connector : cls.getOwnedConnectors())
            umlrtCapsule.addConnector(visitConnector(connector));

        List<Behavior> stateMachines = cls.getOwnedBehaviors().stream()
                .filter(b -> b instanceof StateMachine).collect(Collectors.toList());
        if(!stateMachines.isEmpty())
            umlrtCapsule.setStateMachine(visitStateMachine((StateMachine) stateMachines.get(0)));

        postVisit(capsule, umlrtCapsule);
        return umlrtCapsule;
    }

    @Override
    public UMLRTConnector visitConnector(Connector connector) {
        if (visited(connector)) return (UMLRTConnector) cached(connector);

        assert connector.getEnds().size() == 2;
        UMLRTConnector umlrtConnector = new UMLRTConnector();
        cache(connector, umlrtConnector);
        umlrtConnector.setName(connector.getName());
        umlrtConnector.setEnd1(visitConnectorEnd(connector.getEnds().get(0)));
        umlrtConnector.setEnd2(visitConnectorEnd(connector.getEnds().get(1)));
        postVisit(connector, umlrtConnector);
        return umlrtConnector;
    }

    @Override
    public UMLRTConnectorEnd visitConnectorEnd(ConnectorEnd connectorEnd) {
        if (visited(connectorEnd)) return (UMLRTConnectorEnd) cached(connectorEnd);

        UMLRTConnectorEnd umlrtConnectorEnd = new UMLRTConnectorEnd();
        cache(connectorEnd, umlrtConnectorEnd);
        umlrtConnectorEnd.setPort(visitPort((Port) connectorEnd.getRole()));

        if(connectorEnd.getPartWithPort() != null) {
            CapsulePart partWithPort = (CapsulePart) EMFUtils.getReferencingObjectByType(content,
                    UMLRealTimePackage.Literals.CAPSULE_PART, connectorEnd.getPartWithPort());
            assert partWithPort != null;
            umlrtConnectorEnd.setPart(visitCapsulePart(partWithPort));
        }
        postVisit(connectorEnd, umlrtConnectorEnd);
        return umlrtConnectorEnd;
    }

    @Override
    public UMLRTClass visitClass(Class cls) {
        if (visited(cls)) return (UMLRTClass) cached(cls);

        if(UMLRTUtils.isSystemClass(cls))
            return UMLRTSystemType.get(cls);

        UMLRTClass passiveClass = new UMLRTClass();
        cache(cls, passiveClass);
        passiveClass.setName(cls.getName());

        for(Property attr : cls.getOwnedAttributes()) {
            if(!cls.getOwnedPorts().contains(attr)
                    && EMFUtils.getReferencingObjectByType(content,
                    UMLRealTimePackage.Literals.CAPSULE, attr.getType()) == null)
                passiveClass.addAttribute(visitAttribute(attr));
        }

        for(Operation operation : cls.getOwnedOperations())
            passiveClass.addOperation(visitOperation(operation));
        postVisit(cls, passiveClass);
        return passiveClass;
    }

    @Override
    public UMLRTArtifact visitArtifact(Artifact artifact) {
        if (visited(artifact)) return (UMLRTArtifact) cached(artifact);

        UMLRTArtifact umlrtArtifact = new UMLRTArtifact();
        cache(artifact, umlrtArtifact);
        umlrtArtifact.setName(artifact.getName());
        umlrtArtifact.setFileName(artifact.getFileName());

        for (Property attr : artifact.getOwnedAttributes())
            umlrtArtifact.addAttribute(visitAttribute(attr));

        for(Operation operation : artifact.getOwnedOperations())
            umlrtArtifact.addOperation(visitOperation(operation));
        postVisit(artifact, umlrtArtifact);
        return umlrtArtifact;
    }

    @Override
    public UMLRTEnumeration visitEnumeration(Enumeration enumeration) {
        if (visited(enumeration)) return (UMLRTEnumeration) cached(enumeration);

        UMLRTEnumeration umlrtEnum = new UMLRTEnumeration();
        cache(enumeration, umlrtEnum);
        umlrtEnum.setName(enumeration.getName());
        for (EnumerationLiteral literal : enumeration.getOwnedLiterals())
            umlrtEnum.addLiteral(literal.getName());
        postVisit(enumeration, umlrtEnum);
        return umlrtEnum;
    }

    @Override
    public UMLRTCapsulePart visitCapsulePart(CapsulePart capsulePart) {
        if (visited(capsulePart)) return (UMLRTCapsulePart) cached(capsulePart);

        Property property = capsulePart.getBase_Property();
        Class cls = (Class) property.getType();

        Capsule capsule = (Capsule) EMFUtils.getReferencingObjectByType(content,
                UMLRealTimePackage.Literals.CAPSULE, cls);
        assert capsule != null;

        UMLRTCapsulePart umlrtCapsulePart = new UMLRTCapsulePart();
        cache(capsulePart, umlrtCapsulePart);

        umlrtCapsulePart.setAttribute(visitAttribute(property));
        umlrtCapsulePart.setCapsule(visitCapsule(capsule));
        umlrtCapsulePart.setOptional(property.getLower() == 0
                && property.getAggregation().equals(AggregationKind.COMPOSITE_LITERAL));
        umlrtCapsulePart.setPlugin(property.getLower() == 0
                && property.getAggregation().equals(AggregationKind.SHARED_LITERAL));
        postVisit(capsulePart, umlrtCapsulePart);
        return umlrtCapsulePart;
    }

    @Override
    public UMLRTAttribute visitAttribute(Property property) {
        if (visited(property)) return (UMLRTAttribute) cached(property);

        UMLRTAttribute umlrtAttribute = new UMLRTAttribute();
        cache(property, umlrtAttribute);
        umlrtAttribute.setName(property.getName());
        umlrtAttribute.setReplication(property.upperBound());
        umlrtAttribute.setType(visitType(property.getType()));
        postVisit(property, umlrtAttribute);
        return umlrtAttribute;
    }

    @Override
    public UMLRTOperation visitOperation(Operation operation) {
        if (visited(operation)) return (UMLRTOperation) cached(operation);

        UMLRTOperation umlrtOperation = new UMLRTOperation();
        cache(operation, umlrtOperation);
        umlrtOperation.setName(operation.getName());

        if(!operation.getMethods().isEmpty())
            umlrtOperation.setCode(visitOpaqueBehavior(((OpaqueBehavior)operation.getMethods().get(0))));

        for (Parameter parameter : operation.getOwnedParameters()) {
            if(parameter.getDirection() == ParameterDirectionKind.IN_LITERAL)
                umlrtOperation.addParameter(visitParameter(parameter));
            else if(parameter.getDirection() == ParameterDirectionKind.RETURN_LITERAL)
                umlrtOperation.setReturnParam(visitParameter(parameter));
        }
        postVisit(operation, umlrtOperation);
        return umlrtOperation;
    }

    @Override
    public UMLRTParameter visitParameter(Parameter parameter) {
        if (visited(parameter)) return (UMLRTParameter) cached(parameter);

        UMLRTParameter umlrtParameter = new UMLRTParameter();
        cache(parameter, umlrtParameter);
        umlrtParameter.setName(parameter.getName());
        umlrtParameter.setReplication(parameter.upperBound());
        umlrtParameter.setType(visitType(parameter.getType()));
        postVisit(parameter, umlrtParameter);
        return umlrtParameter;
    }

    @Override
    public UMLRTPort visitPort(Port port) {
        if (visited(port)) return (UMLRTPort) cached(port);

        RTPort rtPort = (RTPort) EMFUtils.getReferencingObjectByType(content,
                UMLRealTimePackage.Literals.RT_PORT, port);
        assert rtPort != null;

        Package pkg = port.getType().getPackage();
        ProtocolContainer protocol = (ProtocolContainer) EMFUtils.getReferencingObjectByType(content,
                UMLRealTimePackage.Literals.PROTOCOL_CONTAINER, pkg);
        assert protocol != null;

        UMLRTPort umlrtPort = new UMLRTPort();
        cache(port, umlrtPort);

        umlrtPort.setConjugated(port.isConjugated());
        umlrtPort.setBehavior(port.isBehavior());
        umlrtPort.setService(port.isService());
        umlrtPort.setWired(rtPort.isWired());
        umlrtPort.setNotification(rtPort.isNotification());
        umlrtPort.setPublish(rtPort.isPublish());
        umlrtPort.setReplication(port.upperBound());
        umlrtPort.setRegistrationType(UMLRTPort.RegistrationType.values()[rtPort.getRegistration().getValue()]);
        umlrtPort.setRegistrationOverride(rtPort.getRegistrationOverride());
        umlrtPort.setProtocol(visitProtocol(protocol));
        umlrtPort.setName(port.getName());
        postVisit(port, umlrtPort);
        return umlrtPort;
    }

    @Override
    public UMLRTProtocol visitProtocol(ProtocolContainer protocol) {
        if (visited(protocol)) return (UMLRTProtocol) cached(protocol);

        if(UMLRTUtils.isSystemProtocol(protocol))
            return UMLRTSystemProtocol.get(protocol);

        Package pkg = protocol.getBase_Package();
        UMLRTProtocol umlrtProtocol = new UMLRTProtocol();
        cache(protocol, umlrtProtocol);
        umlrtProtocol.setName(pkg.getName());

        Map<Operation, UMLRTCallEvent> operationCallEventMap = new HashMap<>();
        for (PackageableElement packagedElement : pkg.getPackagedElements()) {
            if(packagedElement instanceof CallEvent) {
                UMLRTCallEvent umlrtCallEvent = visitCallEvent((CallEvent) packagedElement);
                operationCallEventMap.put((Operation) umlrtCallEvent.getOperation().getEObj(), umlrtCallEvent);
            }
        }

        for (PackageableElement packagedElement : pkg.getPackagedElements()) {
            if(packagedElement instanceof Interface) {
                RTMessageSet messageSet = (RTMessageSet) EMFUtils.getReferencingObjectByType(content,
                        UMLRealTimePackage.Literals.RT_MESSAGE_SET, packagedElement);
                assert messageSet != null;

                for (Operation operation : ((Interface) packagedElement).getOwnedOperations()) {
                    if(RTMessageKind.IN.equals(messageSet.getRtMsgKind())) {
                        umlrtProtocol.addInputSignal(operationCallEventMap.get(operation));
                    } else if(RTMessageKind.OUT.equals(messageSet.getRtMsgKind())) {
                        umlrtProtocol.addOutputSignal(operationCallEventMap.get(operation));
                    } else {
                        umlrtProtocol.addInputSignal(operationCallEventMap.get(operation));
                        umlrtProtocol.addOutputSignal(operationCallEventMap.get(operation));
                    }
                }
            }
        }
        postVisit(protocol, umlrtProtocol);
        return umlrtProtocol;
    }

    @Override
    public UMLRTCallEvent visitCallEvent(CallEvent callEvent) {
        if (visited(callEvent)) return (UMLRTCallEvent) cached(callEvent);

        UMLRTCallEvent umlrtCallEvent = UMLRTUtils.isSystemCallEvent(callEvent)
                ? new UMLRTSystemCallEvent(callEvent)
                : new UMLRTCallEvent();
        cache(callEvent, umlrtCallEvent);

        if(!UMLRTUtils.isSystemCallEvent(callEvent))
            umlrtCallEvent.setOperation(visitOperation(callEvent.getOperation()));
        postVisit(callEvent, umlrtCallEvent);
        return umlrtCallEvent;
    }

    @Override
    public UMLRTStateMachine visitStateMachine(StateMachine stateMachine) {
        if (visited(stateMachine)) return (UMLRTStateMachine) cached(stateMachine);

        UMLRTStateMachine umlrtStateMachine = new UMLRTStateMachine();
        cache(stateMachine, umlrtStateMachine);
        umlrtStateMachine.setName(stateMachine.getName());
        if(!stateMachine.getRegions().isEmpty())
            umlrtStateMachine.setRegion(visitRegion(stateMachine.getRegions().get(0)));
        postVisit(stateMachine, umlrtStateMachine);
        return umlrtStateMachine;
    }

    @Override
    public UMLRTRegion visitRegion(Region region) {
        if (visited(region)) return (UMLRTRegion) cached(region);

        UMLRTRegion umlrtRegion = new UMLRTRegion();
        cache(region, umlrtRegion);

        String name = region.getState() != null ? region.getState().getName() : region.getStateMachine().getName();
        umlrtRegion.setName(name);

        for(Transition transition : region.getTransitions())
            umlrtRegion.addTransition(visitTransition(transition));

        for(Vertex vertex : region.getSubvertices())
            umlrtRegion.addState(visitVertex(vertex));
        postVisit(region, umlrtRegion);
        return umlrtRegion;
    }

    @Override
    public UMLRTTransition visitTransition(Transition transition) {
        if (visited(transition)) return (UMLRTTransition) cached(transition);

        UMLRTTransition umlrtTransition = new UMLRTTransition();
        cache(transition, umlrtTransition);

        umlrtTransition.setName(transition.getName());
        umlrtTransition.setSource(visitVertex(transition.getSource()));
        umlrtTransition.setTarget(visitVertex(transition.getTarget()));

        umlrtTransition.getSource().addOutTransition(umlrtTransition);
        umlrtTransition.getTarget().addInTransition(umlrtTransition);

        if(transition.getEffect() != null)
            umlrtTransition.setAction(visitOpaqueBehavior((OpaqueBehavior) transition.getEffect()));

        if(transition.getGuard() != null) {
            assert transition.getGuard().getSpecification() != null;
            umlrtTransition.setGuard(visitOpaqueExpression((OpaqueExpression)
                    transition.getGuard().getSpecification()));
        }

        for(Trigger trigger : transition.getTriggers())
            umlrtTransition.addTrigger(visitTrigger(trigger));
        postVisit(transition, umlrtTransition);
        return umlrtTransition;
    }

    @Override
    public UMLRTTrigger visitTrigger(Trigger trigger) {
        if (visited(trigger)) return (UMLRTTrigger) cached(trigger);

        UMLRTTrigger umlrtTrigger = new UMLRTTrigger();
        cache(trigger, umlrtTrigger);

        umlrtTrigger.setName(trigger.getName());
        umlrtTrigger.setCallEvent(visitCallEvent((CallEvent) trigger.getEvent()));

        for(Port port : trigger.getPorts())
            umlrtTrigger.addPort(visitPort(port));
        postVisit(trigger, umlrtTrigger);
        return umlrtTrigger;
    }

    @Override
    public UMLRTActionCode visitOpaqueBehavior(OpaqueBehavior behavior) {
        if (visited(behavior)) return (UMLRTActionCode) cached(behavior);

        assert !behavior.getBodies().isEmpty();
        UMLRTActionCode actionCode = new UMLRTActionCode();
        cache(behavior, actionCode);

        actionCode.setName(behavior.getName());
        actionCode.setCode(behavior.getBodies().get(0));

        if(!behavior.getLanguages().isEmpty())
            actionCode.setLanguage(behavior.getLanguages().get(0));
        postVisit(behavior, actionCode);
        return actionCode;
    }

    @Override
    public UMLRTActionCode visitOpaqueExpression(OpaqueExpression expression) {
        if (visited(expression)) return (UMLRTActionCode) cached(expression);

        assert !expression.getBodies().isEmpty();
        UMLRTActionCode actionCode = new UMLRTActionCode();
        cache(expression, actionCode);

        actionCode.setName(expression.getName());
        actionCode.setCode(expression.getBodies().get(0));
        if(!expression.getLanguages().isEmpty())
            actionCode.setLanguage(expression.getLanguages().get(0));
        postVisit(expression, actionCode);
        return actionCode;
    }

    @Override
    public UMLRTGenericState visitVertex(Vertex vertex) {
        if (visited(vertex)) return (UMLRTGenericState) cached(vertex);

        if(vertex instanceof Pseudostate)
            return visitPseudoState((Pseudostate) vertex);
        return visitState((State) vertex);
    }

    @Override
    public UMLRTType visitType(Type type) {
        if(type instanceof Enumeration)
            return visitEnumeration((Enumeration) type);

        if(type instanceof Collaboration)
            return visitProtocol((ProtocolContainer) EMFUtils.getReferencingObjectByType(content,
                    UMLRealTimePackage.Literals.PROTOCOL_CONTAINER, type.getPackage()));

        if(type instanceof Class)
            return visitClass((Class) type);

        if(type instanceof PrimitiveType)
            return UMLRTPrimitiveType.get(type);
        throw new RuntimeException("Unexpected type " + type.eClass().getName());
    }

    public UMLRTState visitState(State state) {
        UMLRTState umlrtState = state.getRegions().isEmpty()
                ? new UMLRTState()
                : visitCompositeState(state);
        cache(state, umlrtState);
        umlrtState.setName(state.getName());

        if(state.getEntry() != null)
            umlrtState.setEntryCode(visitOpaqueBehavior((OpaqueBehavior) state.getEntry()));

        if(state.getExit() != null)
            umlrtState.setExitCode(visitOpaqueBehavior((OpaqueBehavior) state.getExit()));
        postVisit(state, umlrtState);
        return umlrtState;
    }

    public UMLRTCompositeState visitCompositeState(State state) {
        assert !state.getRegions().isEmpty();
        UMLRTCompositeState compositeState = new UMLRTCompositeState();
        cache(state, compositeState);
        compositeState.setRegion(visitRegion(state.getRegions().get(0)));

        for (Pseudostate connectionPoint : state.getConnectionPoints()) {
            if(connectionPoint.getKind() == PseudostateKind.ENTRY_POINT_LITERAL)
                compositeState.addEntryPoint((UMLRTPseudoState) visitVertex(connectionPoint));
            else if(connectionPoint.getKind() == PseudostateKind.EXIT_POINT_LITERAL)
                compositeState.addExitPoint((UMLRTPseudoState) visitVertex(connectionPoint));
        }
        postVisit(state, compositeState);
        return compositeState;
    }

    public UMLRTPseudoState visitPseudoState(Pseudostate state) {
        UMLRTPseudoState pseudoState = new UMLRTPseudoState();
        cache(state, pseudoState);
        pseudoState.setName(state.getName());
        pseudoState.setKind(UMLRTPseudoState.Kind.values()[state.getKind().getValue()]);
        postVisit(state, pseudoState);
        return pseudoState;
    }

    @Override
    public UMLRTArtifactProperties visitArtifactProperties(ArtifactProperties props) {
        if (visited(props)) return (UMLRTArtifactProperties) cached(props);

        UMLRTArtifactProperties umlrtArtifactProperties = new UMLRTArtifactProperties();
        cache(props, umlrtArtifactProperties);
        umlrtArtifactProperties.setArtifact(visitArtifact(props.getBase_Artifact()));
        umlrtArtifactProperties.setIncludeFile(props.getIncludeFile());
        umlrtArtifactProperties.setSourceFile(props.getSourceFile());
        postVisit(props, umlrtArtifactProperties);
        return umlrtArtifactProperties;
    }

    @Override
    public UMLRTAttributeProperties visitAttributeProperties(AttributeProperties props) {
        if (visited(props)) return (UMLRTAttributeProperties) cached(props);

        UMLRTAttributeProperties umlrtAttributeProperties = new UMLRTAttributeProperties();
        cache(props, umlrtAttributeProperties);
        umlrtAttributeProperties.setAttribute(visitAttribute(props.getBase_Property()));
        umlrtAttributeProperties.setInitialization(UMLRTAttributeProperties
                .InitKind.values()[props.getInitialization().getValue()]);
        umlrtAttributeProperties.setKind(UMLRTAttributeProperties
                .Kind.values()[props.getKind().getValue()]);
        umlrtAttributeProperties.setSize(props.getSize());
        umlrtAttributeProperties.setType(props.getType());
        umlrtAttributeProperties.setPointsToConstType(props.isPointsToConstType());
        umlrtAttributeProperties.setPointsToVolatileType(props.isPointsToVolatileType());
        umlrtAttributeProperties.setPointsToType(props.isPointsToType());
        umlrtAttributeProperties.setVolatile(props.isVolatile());
        postVisit(props, umlrtAttributeProperties);
        return umlrtAttributeProperties;
    }

    @Override
    public UMLRTCapsuleProperties visitCapsuleProperties(CapsuleProperties props) {
        if (visited(props)) return (UMLRTCapsuleProperties) cached(props);

        Capsule capsule = (Capsule) EMFUtils.getReferencingObjectByType(content,
                UMLRealTimePackage.Literals.CAPSULE, props.getBase_Class());
        assert capsule != null;

        UMLRTCapsuleProperties umlrtCapsuleProperties = new UMLRTCapsuleProperties();
        cache(props, umlrtCapsuleProperties);
        umlrtCapsuleProperties.setCapsule(visitCapsule(capsule));
        umlrtCapsuleProperties.setHeaderPreface(props.getHeaderPreface());
        umlrtCapsuleProperties.setHeaderEnding(props.getHeaderEnding());
        umlrtCapsuleProperties.setImplementationPreface(props.getImplementationPreface());
        umlrtCapsuleProperties.setImplementationEnding(props.getImplementationEnding());
        umlrtCapsuleProperties.setPublicDeclarations(props.getPublicDeclarations());
        umlrtCapsuleProperties.setPrivateDeclarations(props.getPrivateDeclarations());
        umlrtCapsuleProperties.setProtectedDeclarations(props.getProtectedDeclarations());
        umlrtCapsuleProperties.setGenerateHeader(props.isGenerateHeader());
        umlrtCapsuleProperties.setGenerateImplementation(props.isGenerateImplementation());
        postVisit(props, umlrtCapsuleProperties);
        return umlrtCapsuleProperties;
    }

    @Override
    public UMLRTEnumerationProperties visitEnumerationProperties(EnumerationProperties props) {
        if (visited(props)) return (UMLRTEnumerationProperties) cached(props);

        UMLRTEnumerationProperties umlrtEnumerationProperties = new UMLRTEnumerationProperties();
        cache(props, umlrtEnumerationProperties);
        umlrtEnumerationProperties.setEnumeration(visitEnumeration(props.getBase_Enumeration()));
        umlrtEnumerationProperties.setHeaderPreface(props.getHeaderPreface());
        umlrtEnumerationProperties.setHeaderEnding(props.getHeaderEnding());
        umlrtEnumerationProperties.setImplementationPreface(props.getImplementationPreface());
        umlrtEnumerationProperties.setImplementationEnding(props.getImplementationEnding());
        umlrtEnumerationProperties.setGenerate(props.isGenerate());
        postVisit(props, umlrtEnumerationProperties);
        return umlrtEnumerationProperties;
    }

    @Override
    public UMLRTOperationProperties visitOperationProperties(OperationProperties props) {
        if (visited(props)) return (UMLRTOperationProperties) cached(props);

        UMLRTOperationProperties umlrtOperationProperties = new UMLRTOperationProperties();
        cache(props, umlrtOperationProperties);
        umlrtOperationProperties.setOperation(visitOperation(props.getBase_Operation()));
        umlrtOperationProperties.setKind(UMLRTOperationProperties.OpKind.values()[props.getKind().getValue()]);
        umlrtOperationProperties.setGenerateDefinition(props.isGenerateDefinition());
        umlrtOperationProperties.setInline(props.isInline());
        umlrtOperationProperties.setPolymorphic(props.isPolymorphic());
        postVisit(props, umlrtOperationProperties);
        return umlrtOperationProperties;
    }

    @Override
    public UMLRTParameterProperties visitParameterProperties(ParameterProperties props) {
        if (visited(props)) return (UMLRTParameterProperties) cached(props);

        UMLRTParameterProperties umlrtParameterProperties = new UMLRTParameterProperties();
        cache(props, umlrtParameterProperties);
        umlrtParameterProperties.setParameter(visitParameter(props.getBase_Parameter()));
        umlrtParameterProperties.setType(props.getType());
        umlrtParameterProperties.setPointsToConst(props.isPointsToConst());
        umlrtParameterProperties.setPointsToVolatile(props.isPointsToVolatile());
        umlrtParameterProperties.setPointsToType(props.isPointsToType());
        postVisit(props, umlrtParameterProperties);
        return umlrtParameterProperties;
    }

    @Override
    public UMLRTPassiveClassProperties visitPassiveClassProperties(PassiveClassProperties props) {
        if (visited(props)) return (UMLRTPassiveClassProperties) cached(props);

        UMLRTPassiveClassProperties umlrtPassiveClassProperties = new UMLRTPassiveClassProperties();
        cache(props, umlrtPassiveClassProperties);
        umlrtPassiveClassProperties.setPassiveClass(visitClass(props.getBase_Class()));
        umlrtPassiveClassProperties.setKind(UMLRTPassiveClassProperties.ClsKind.values()[props.getKind().getValue()]);
        umlrtPassiveClassProperties.setHeaderPreface(props.getHeaderPreface());
        umlrtPassiveClassProperties.setHeaderEnding(props.getHeaderEnding());
        umlrtPassiveClassProperties.setImplementationPreface(props.getImplementationPreface());
        umlrtPassiveClassProperties.setImplementationEnding(props.getImplementationEnding());
        umlrtPassiveClassProperties.setPublicDeclarations(props.getPublicDeclarations());
        umlrtPassiveClassProperties.setPrivateDeclarations(props.getPrivateDeclarations());
        umlrtPassiveClassProperties.setProtectedDeclarations(props.getProtectedDeclarations());
        umlrtPassiveClassProperties.setImplementationType(props.getImplementationType());
        umlrtPassiveClassProperties.setGenerate(props.isGenerate());
        umlrtPassiveClassProperties.setGenerateHeader(props.isGenerateHeader());
        umlrtPassiveClassProperties.setGenerateImplementation(props.isGenerateImplementation());
        umlrtPassiveClassProperties.setGenerateStateMachine(props.isGenerateStateMachine());
        umlrtPassiveClassProperties.setGenerateAssignmentOperator(props.isGenerateAssignmentOperator());
        umlrtPassiveClassProperties.setGenerateEqualityOperator(props.isGenerateEqualityOperator());
        umlrtPassiveClassProperties.setGenerateInequalityOperator(props.isGenerateInequalityOperator());
        umlrtPassiveClassProperties.setGenerateInsertionOperator(props.isGenerateInsertionOperator());
        umlrtPassiveClassProperties.setGenerateExtractionOperator(props.isGenerateExtractionOperator());
        umlrtPassiveClassProperties.setGenerateCopyConstructor(props.isGenerateCopyConstructor());
        umlrtPassiveClassProperties.setGenerateDefaultConstructor(props.isGenerateDefaultConstructor());
        umlrtPassiveClassProperties.setGenerateDestructor(props.isGenerateDestructor());
        postVisit(props, umlrtPassiveClassProperties);
        return umlrtPassiveClassProperties;
    }

    @Override
    public UMLRTTypeProperties visitTypeProperties(TypeProperties props) {
        if (visited(props)) return (UMLRTTypeProperties) cached(props);

        UMLRTTypeProperties umlrtTypeProperties = new UMLRTTypeProperties();
        cache(props, umlrtTypeProperties);
        umlrtTypeProperties.setName(props.getName());
        umlrtTypeProperties.setDefinitionFile(props.getDefinitionFile());
        postVisit(props, umlrtTypeProperties);
        return umlrtTypeProperties;
    }

    public UMLRTProperties visitProperties(EObject properties) {
        if(properties instanceof ArtifactProperties)
            return visitArtifactProperties((ArtifactProperties) properties);
        if(properties instanceof CapsuleProperties)
            return visitCapsuleProperties((CapsuleProperties) properties);
        if(properties instanceof EnumerationProperties)
            return visitEnumerationProperties((EnumerationProperties) properties);
        if(properties instanceof OperationProperties)
            return visitOperationProperties((OperationProperties) properties);
        if(properties instanceof ParameterProperties)
            return visitParameterProperties((ParameterProperties) properties);
        if(properties instanceof PassiveClassProperties)
            return visitPassiveClassProperties((PassiveClassProperties) properties);
        if(properties instanceof AttributeProperties)
            return visitAttributeProperties((AttributeProperties) properties);
        if(properties instanceof TypeProperties)
            return visitTypeProperties((TypeProperties) properties);
        throw new RuntimeException("Unexpected properties class "+properties.eClass().getName());
    }
}
