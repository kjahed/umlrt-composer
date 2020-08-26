package ca.jahed.umlrt.composer.model.properties;

import ca.jahed.umlrt.composer.model.UMLRTClass;

public class UMLRTPassiveClassProperties extends UMLRTProperties {
    transient UMLRTClass passiveClass;
    ClsKind kind;
    String headerPreface;
    String headerEnding;
    String implementationPreface;
    String implementationEnding;
    String publicDeclarations;
    String privateDeclarations;
    String protectedDeclarations;
    String implementationType;

    boolean generate;
    boolean generateHeader;
    boolean generateImplementation;
    boolean generateStateMachine;

    boolean generateAssignmentOperator;
    boolean generateEqualityOperator;
    boolean generateInequalityOperator;
    boolean generateInsertionOperator;
    boolean generateExtractionOperator;

    boolean generateCopyConstructor;
    boolean generateDefaultConstructor;
    boolean generateDestructor;

    public enum ClsKind {
        CLASS,
        STRUCT,
        UNION,
        TYPEDEF
    }

    public void setPassiveClass(UMLRTClass passiveClass) {
        this.passiveClass = passiveClass;
    }

    public void setKind(ClsKind kind) {
        this.kind = kind;
    }

    public void setHeaderPreface(String headerPreface) {
        this.headerPreface = headerPreface;
    }

    public void setHeaderEnding(String headerEnding) {
        this.headerEnding = headerEnding;
    }

    public void setImplementationPreface(String implementationPreface) {
        this.implementationPreface = implementationPreface;
    }

    public void setImplementationEnding(String implementationEnding) {
        this.implementationEnding = implementationEnding;
    }

    public void setPublicDeclarations(String publicDeclarations) {
        this.publicDeclarations = publicDeclarations;
    }

    public void setPrivateDeclarations(String privateDeclarations) {
        this.privateDeclarations = privateDeclarations;
    }

    public void setProtectedDeclarations(String protectedDeclarations) {
        this.protectedDeclarations = protectedDeclarations;
    }

    public void setImplementationType(String implementationType) {
        this.implementationType = implementationType;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public void setGenerateHeader(boolean generateHeader) {
        this.generateHeader = generateHeader;
    }

    public void setGenerateImplementation(boolean generateImplementation) {
        this.generateImplementation = generateImplementation;
    }

    public void setGenerateStateMachine(boolean generateStateMachine) {
        this.generateStateMachine = generateStateMachine;
    }

    public void setGenerateAssignmentOperator(boolean generateAssignmentOperator) {
        this.generateAssignmentOperator = generateAssignmentOperator;
    }

    public void setGenerateEqualityOperator(boolean generateEqualityOperator) {
        this.generateEqualityOperator = generateEqualityOperator;
    }

    public void setGenerateInequalityOperator(boolean generateInequalityOperator) {
        this.generateInequalityOperator = generateInequalityOperator;
    }

    public void setGenerateInsertionOperator(boolean generateInsertionOperator) {
        this.generateInsertionOperator = generateInsertionOperator;
    }

    public void setGenerateExtractionOperator(boolean generateExtractionOperator) {
        this.generateExtractionOperator = generateExtractionOperator;
    }

    public void setGenerateCopyConstructor(boolean generateCopyConstructor) {
        this.generateCopyConstructor = generateCopyConstructor;
    }

    public void setGenerateDefaultConstructor(boolean generateDefaultConstructor) {
        this.generateDefaultConstructor = generateDefaultConstructor;
    }

    public void setGenerateDestructor(boolean generateDestructor) {
        this.generateDestructor = generateDestructor;
    }

    public UMLRTClass getPassiveClass() {
        return passiveClass;
    }

    public ClsKind getKind() {
        return kind;
    }

    public String getHeaderPreface() {
        return headerPreface;
    }

    public String getHeaderEnding() {
        return headerEnding;
    }

    public String getImplementationPreface() {
        return implementationPreface;
    }

    public String getImplementationEnding() {
        return implementationEnding;
    }

    public String getPublicDeclarations() {
        return publicDeclarations;
    }

    public String getPrivateDeclarations() {
        return privateDeclarations;
    }

    public String getProtectedDeclarations() {
        return protectedDeclarations;
    }

    public String getImplementationType() {
        return implementationType;
    }

    public boolean isGenerate() {
        return generate;
    }

    public boolean isGenerateHeader() {
        return generateHeader;
    }

    public boolean isGenerateImplementation() {
        return generateImplementation;
    }

    public boolean isGenerateStateMachine() {
        return generateStateMachine;
    }

    public boolean isGenerateAssignmentOperator() {
        return generateAssignmentOperator;
    }

    public boolean isGenerateEqualityOperator() {
        return generateEqualityOperator;
    }

    public boolean isGenerateInequalityOperator() {
        return generateInequalityOperator;
    }

    public boolean isGenerateInsertionOperator() {
        return generateInsertionOperator;
    }

    public boolean isGenerateExtractionOperator() {
        return generateExtractionOperator;
    }

    public boolean isGenerateCopyConstructor() {
        return generateCopyConstructor;
    }

    public boolean isGenerateDefaultConstructor() {
        return generateDefaultConstructor;
    }

    public boolean isGenerateDestructor() {
        return generateDestructor;
    }
}
