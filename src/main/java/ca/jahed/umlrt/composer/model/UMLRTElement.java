package ca.jahed.umlrt.composer.model;

import ca.jahed.umlrt.composer.model.properties.UMLRTProperties;
import ca.jahed.umlrt.composer.utils.NameUtils;
import org.eclipse.emf.ecore.EObject;

import java.util.ArrayList;
import java.util.List;

public abstract class UMLRTElement {
    final List<UMLRTProperties> properties = new ArrayList<>();
    transient EObject eObj;
    String name;

    public void addProperties(UMLRTProperties prop) {
        properties.add(prop);
    }

    public void setEObj(EObject eObj) {
        this.eObj = eObj;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        if(name == null)
            setName(NameUtils.randomString(eObj.eClass().getName() + "-", 5));
        return name;
    }

    public EObject getEObj() {
        return eObj;
    }

    public List<UMLRTProperties> getProperties() {
        return properties;
    }
}
