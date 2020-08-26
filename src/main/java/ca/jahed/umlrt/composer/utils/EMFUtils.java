package ca.jahed.umlrt.composer.utils;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import java.util.*;

public class EMFUtils {

    public static EObject getObjectByType(List<EObject> objects,
                                          EClassifier type) {

        return getObjectByType(Collections.singletonMap(type, objects),
                Collections.singletonList(type), Collections.emptyMap());
    }

    public static EObject getObjectByType(List<EObject> objects,
                                          EClassifier type,
                                          Map<String, Object> attrs) {

        return getObjectByType(Collections.singletonMap(type, objects),
                Collections.singletonList(type), attrs);
    }

    public static EObject getObjectByType(Map<EClassifier, List<EObject>> objects,
                                          EClassifier type,
                                          Map<String, Object> attrs) {

        return getObjectByType(objects, Collections.singletonList(type), attrs);
    }

    public static EObject getObjectByType(Map<EClassifier, List<EObject>> objects,
                                          Collection<EClassifier> types,
                                          Map<String, Object> attrs) {

        List<EObject> eObjs = getObjectsByType(objects, types, attrs);
        if(!eObjs.isEmpty())
            return eObjs.get(0);
        return null;
    }

    public static List<EObject> getObjectsByType(Map<EClassifier, List<EObject>> objects,
                                                 Collection<EClassifier> types) {

        return getObjectsByType(objects, types, Collections.emptyMap());
    }

    public static List<EObject> getObjectsByType(Map<EClassifier, List<EObject>> objects,
                                                 Collection<EClassifier> types,
                                                 Map<String, Object> attrs) {

        Set<EObject> candidates = getCandidates(objects, types);
        List<EObject> found = new ArrayList<>();

        out: for (EObject eObj : candidates) {
            int matchedAttrs = 0;

            for (EAttribute eAttr : eObj.eClass().getEAllAttributes()) {
                if(attrs.containsKey(eAttr.getName())) {
                    if(!attrs.get(eAttr.getName()).equals(eObj.eGet(eAttr)))
                        continue out;
                    matchedAttrs++;
                }
            }

            if(matchedAttrs == attrs.size())
                found.add(eObj);
        }
        return found;
    }

    public static EObject getReferencingObjectByType(List<EObject> objects,
                                                     EClassifier type,
                                                     EObject target) {

        return getReferencingObjectByType(Collections.singletonMap(type, objects), type, target);
    }

    public static EObject getReferencingObjectByType(Map<EClassifier, List<EObject>> objects,
                                                     EClassifier type,
                                                     EObject target) {

        return getReferencingObjectByType(objects, Collections.singletonList(type), target);
    }

    public static EObject getReferencingObjectByType(Map<EClassifier, List<EObject>> objects,
                                                     Collection<EClassifier> types,
                                                     EObject target) {

        List<EObject> eObjs = getReferencingObjectsByType(objects, types, target);
        if(!eObjs.isEmpty())
            return eObjs.get(0);
        return null;
    }

    public static List<EObject> getReferencingObjectsByType(Map<EClassifier, List<EObject>> objects,
                                                            Collection<EClassifier> types,
                                                            EObject target) {
        Set<EObject> candidates = getCandidates(objects, types);
        List<EObject> found = new ArrayList<>();

        for (EObject eObj : candidates) {
            for (EReference eRef : eObj.eClass().getEAllReferences()) {
                if(target.equals(eObj.eGet(eRef))) {
                    found.add(eObj);
                    break;
                }
            }
        }
        return found;
    }

    private static Set<EObject> getCandidates(Map<EClassifier, List<EObject>> objects,
                                              Collection<EClassifier> types) {
        Set<EObject> candidates = new HashSet<>();
        for (EClassifier classifier : types) {
            objects.getOrDefault(classifier, Collections.emptyList()).stream()
                    .filter(eObj -> eObj.eClass().equals(classifier)).forEach(candidates::add);
        }
        return candidates;
    }
}
