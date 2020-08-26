package ca.jahed.umlrt.composer.model.properties;

public interface UMLRTPropertiesVisitor {

    Object visitArtifactProperties(UMLRTArtifactProperties artifactProperties);

    Object visitAttributeProperties(UMLRTAttributeProperties attributeProperties);

    Object visitCapsuleProperties(UMLRTCapsuleProperties capsuleProperties);

    Object visitEnumerationProperties(UMLRTEnumerationProperties enumerationProperties);

    Object visitOperationProperties(UMLRTOperationProperties operationProperties);

    Object visitParameterProperties(UMLRTParameterProperties parameterProperties);

    Object visitPassiveClassProperties(UMLRTPassiveClassProperties passiveClassProperties);

    Object visitTypeProperties(UMLRTTypeProperties typeProperties);
}
