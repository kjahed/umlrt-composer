package ca.jahed.umlrt.composer.utils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrusrt.codegen.cpp.profile.RTCppProperties.RTCppPropertiesPackage;
import org.eclipse.papyrusrt.umlrt.profile.UMLRealTime.ProtocolContainer;
import org.eclipse.papyrusrt.umlrt.profile.UMLRealTime.UMLRealTimePackage;
import org.eclipse.papyrusrt.umlrt.profile.statemachine.UMLRTStateMachines.UMLRTStateMachinesPackage;
import org.eclipse.papyrusrt.umlrt.system.profile.systemelements.SystemClass;
import org.eclipse.papyrusrt.umlrt.system.profile.systemelements.SystemElementsPackage;
import org.eclipse.papyrusrt.umlrt.system.profile.systemelements.SystemProtocol;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UMLRTUtils {
    static final Map<String , URL> PATHMAP = new HashMap<String , URL>() {{
        put("pathmap://UMLRTRTSLIB/UMLRT-RTS.uml",
                getClass().getClassLoader().getResource("UMLRT-RTS.uml"));
        put("pathmap://UML_RT_PROFILE/uml-rt.profile.uml",
                getClass().getClassLoader().getResource("uml-rt.profile.uml"));
        put("pathmap://UML_RT_PROFILE/UMLRealTimeSM-addendum.profile.uml",
                getClass().getClassLoader().getResource("UMLRealTimeSM-addendum.profile.uml"));
        put("pathmap://UMLRT_CPP/RTCppProperties.profile.uml",
                getClass().getClassLoader().getResource("RTCppProperties.profile.uml"));
        put("pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml",
                getClass().getClassLoader().getResource("UMLPrimitiveTypes.library.uml"));
        put("pathmap://PapyrusC_Cpp_LIBRARIES/AnsiCLibrary.uml",
                getClass().getClassLoader().getResource("AnsiCLibrary.uml"));
    }};

    public enum SysProtocol {
        Log,
        Timing,
        Frame,
        TCP,
        MQTT,
        UMLRTBaseCommProtocol
    }

    public enum SysClass {
        UMLRTCapsuleId,
        UMLRTMessage,
        UMLRTTimerId,
        UMLRTTimespec
    }

    public enum SysProfile {
        UMLRealTime,
        UMLRTStateMachines,
        RTCppProperties
    }

    private static final Map<String, Profile> profiles = new HashMap<>();
    private static final Map<String, ProtocolContainer> sysProtocols = new HashMap<>();
    private static final Map<String, Class> sysClasses = new HashMap<>();
    private static final Map<ProtocolContainer, Map<String, CallEvent>> sysProtocolEventsMap = new HashMap<>();
    private static final Map<String, PrimitiveType> sysTypes = new HashMap<>();

    private final static List<EClassifier> rtClassifiers = new ArrayList<>();
    private final static List<EClassifier> rtCppClassifiers = new ArrayList<>();

    static {
        try {
            for (Field field : UMLRealTimePackage.Literals.class.getDeclaredFields())
                if(field.getType().equals(EClass.class))
                    rtClassifiers.add((EClassifier) field.get(null));

            for (Field field : UMLRTStateMachinesPackage.Literals.class.getDeclaredFields())
                if(field.getType().equals(EClass.class))
                    rtClassifiers.add((EClassifier) field.get(null));

            for (Field field : RTCppPropertiesPackage.Literals.class.getDeclaredFields())
                if(field.getType().equals(EClass.class))
                    rtCppClassifiers.add((EClassifier) field.get(null));
            rtClassifiers.addAll(rtCppClassifiers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UMLRTUtils() {}

    public static void init(ResourceSet set) {
        set.getPackageRegistry().put(UMLRealTimePackage.eNS_URI, UMLRealTimePackage.eINSTANCE);
        set.getPackageRegistry().put(UMLRTStateMachinesPackage.eNS_URI, UMLRTStateMachinesPackage.eINSTANCE);
        set.getPackageRegistry().put(RTCppPropertiesPackage.eNS_URI, RTCppPropertiesPackage.eINSTANCE);
        set.getPackageRegistry().put(SystemElementsPackage.eNS_URI, SystemElementsPackage.eINSTANCE);

        PATHMAP.forEach((pathmap, location) -> set.getURIConverter().getURIMap().put(URI.createURI(pathmap),
                URI.createURI(location.toString())));

        PATHMAP.keySet().forEach(pathmap -> set.getResource(URI.createURI(pathmap), true));

        loadProfiles(set);
        loadSysProtocols(set);
        loadSysClasses(set);
        loadSysTypes(set);
    }

    private static void loadProfiles(ResourceSet set) {
        set.getResources().forEach(resource -> {
            EcoreUtil.getObjectsByType(resource.getContents(), UMLPackage.Literals.PROFILE).forEach(p -> {
                Profile profile = (Profile) p;
                profiles.put(profile.getName(), profile);
            });
        });
    }

    private static void loadSysProtocols(ResourceSet set) {
        set.getResources().forEach(resource -> {
            EcoreUtil.getObjectsByType(resource.getContents(),
                    SystemElementsPackage.Literals.SYSTEM_PROTOCOL).forEach(p -> {
                SystemProtocol systemProtocol = (SystemProtocol) p;
                Collaboration collaboration = systemProtocol.getBase_Collaboration();
                Package pkg = collaboration.getPackage();

                ProtocolContainer protocol = (ProtocolContainer)
                        EMFUtils.getReferencingObjectByType(resource.getContents(),
                                UMLRealTimePackage.Literals.PROTOCOL_CONTAINER, pkg);

                UMLRTUtils.sysProtocols.put(pkg.getName(), protocol);

                Map<String, CallEvent> callEvents = new HashMap<>();
                sysProtocolEventsMap.put(protocol, callEvents);
                for (PackageableElement packagedElement : pkg.getPackagedElements()) {
                    if(packagedElement instanceof CallEvent) {
                        CallEvent event = ((CallEvent) packagedElement);
                        Operation operation = event.getOperation();
                        if(operation != null)
                            callEvents.put(operation.getName(), event);
                    }
                }
            });
        });
    }

    private static void loadSysClasses(ResourceSet set) {
        set.getResources().forEach(resource -> {
            EcoreUtil.getObjectsByType(resource.getContents(),
                    SystemElementsPackage.Literals.SYSTEM_CLASS).forEach(c -> {
                        Class cls = ((SystemClass) c).getBase_Class();
                        sysClasses.put(cls.getName(), cls);
            });
        });
    }

    private static void loadSysTypes(ResourceSet set) {
        set.getResources().forEach(resource -> {
            EcoreUtil.getObjectsByType(resource.getContents(), UMLPackage.Literals.MODEL).forEach(m -> {
                Model model = (Model) m;
                EcoreUtil.getObjectsByType(model.getPackagedElements(), UMLPackage.Literals.PRIMITIVE_TYPE).forEach(t -> {
                    PrimitiveType type = (PrimitiveType) t;
                    sysTypes.put(type.getName(), type);
                });
            });
        });
    }

    public static List<EClassifier> getRtClassifiers() {
        return rtClassifiers;
    }

    public static List<EClassifier> getRtCppClassifiers() {
        return rtCppClassifiers;
    }

    public static Profile getProfile(SysProfile profile) {
        return profiles.get(profile.toString());
    }

    public static ProtocolContainer getProtocol(SysProtocol protocol) {
        return sysProtocols.get(protocol.toString());
    }

    public static CallEvent getEvent(SysProtocol protocol, String eventName) {
        return sysProtocolEventsMap.get(getProtocol(protocol)).get(eventName);
    }

    public static boolean isSystemProtocol(ProtocolContainer protocol) {
        return sysProtocols.containsValue(protocol);
    }

    public static boolean isSystemClass(Class cls) {
        return sysClasses.containsValue(cls);
    }

    public static boolean isSystemCallEvent(CallEvent callEvent) {
        for (Map<String, CallEvent> events : sysProtocolEventsMap.values()) {
            if(events.containsValue(callEvent))
                return true;
        }
        return false;
    }

    public static boolean isTiming(ProtocolContainer protocol) {
        return getProtocol(SysProtocol.Timing).equals(protocol);
    }

    public static boolean isLog(ProtocolContainer protocol) {
        return getProtocol(SysProtocol.Log).equals(protocol);
    }

    public static boolean isFrame(ProtocolContainer protocol) {
        return getProtocol(SysProtocol.Frame).equals(protocol);
    }

    public static boolean isTCP(ProtocolContainer protocol) {
        return getProtocol(SysProtocol.TCP).equals(protocol);
    }

    public static boolean isMQTT(ProtocolContainer protocol) {
        return getProtocol(SysProtocol.MQTT).equals(protocol);
    }

    public static PrimitiveType getPrimitiveType(String name) {
        return sysTypes.get(name);
    }

    public static Class getSystemClass(SysClass sysClass) {
        return sysClasses.get(sysClass.toString());
    }

    public static boolean isMessage(Class cls) {
        return getSystemClass(SysClass.UMLRTMessage).equals(cls);
    }

    public static boolean isCapsuleId(Class cls) {
        return getSystemClass(SysClass.UMLRTCapsuleId).equals(cls);
    }

    public static boolean isTimerId(Class cls) {
        return getSystemClass(SysClass.UMLRTTimerId).equals(cls);
    }

    public static boolean isTimespec(Class cls) {
        return getSystemClass(SysClass.UMLRTTimespec).equals(cls);
    }
}
