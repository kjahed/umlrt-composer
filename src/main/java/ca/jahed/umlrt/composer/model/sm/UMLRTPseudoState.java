package ca.jahed.umlrt.composer.model.sm;

public class UMLRTPseudoState extends UMLRTGenericState {
    Kind kind;

    public enum Kind {
        INITIAL,
        DEEP_HISTORY,
        SHALLOW_HISTORY,
        JOIN,
        FORK,
        JUNCTION,
        CHOICE,
        ENTRY_POINT,
        EXIT_POINT,
        TERMINATE
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }
}
