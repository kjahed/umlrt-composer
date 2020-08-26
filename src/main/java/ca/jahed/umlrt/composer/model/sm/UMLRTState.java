package ca.jahed.umlrt.composer.model.sm;

public class UMLRTState extends UMLRTGenericState {
    UMLRTActionCode entryCode;
    UMLRTActionCode exitCode;

    public void setEntryCode(UMLRTActionCode entryCode) {
        this.entryCode = entryCode;
    }

    public void setExitCode(UMLRTActionCode exitCode) {
        this.exitCode = exitCode;
    }

    public UMLRTActionCode getEntryCode() {
        return entryCode;
    }

    public UMLRTActionCode getExitCode() {
        return exitCode;
    }
}
