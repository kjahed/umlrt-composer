package ca.jahed.umlrt.composer.model;

import java.util.ArrayList;
import java.util.List;

public class UMLRTEnumeration extends UMLRTType {
    final List<String> literals = new ArrayList<>();

    public void addLiteral(String literal) {
        literals.add(literal);
    }

    public List<String> getLiterals() {
        return literals;
    }
}
