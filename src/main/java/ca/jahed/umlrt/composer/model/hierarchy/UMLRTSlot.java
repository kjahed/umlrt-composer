package ca.jahed.umlrt.composer.model.hierarchy;

import ca.jahed.umlrt.composer.model.UMLRTCapsulePart;
import ca.jahed.umlrt.composer.model.UMLRTPort;
import org.javatuples.Pair;

import java.util.*;

public class UMLRTSlot {
    final String name;
    final UMLRTCapsulePart part;
    final List<UMLRTSlot> children = new ArrayList<>();
    final List<UMLRTSlot> siblings = new ArrayList<>();
    final Map<UMLRTPort, Set<Pair<UMLRTPort, UMLRTSlot>>> connections = new HashMap<>();
    UMLRTSlot parent;

    public UMLRTSlot(String name, UMLRTCapsulePart part) {
        this.name = name;
        this.part = part;
    }

    public UMLRTSlot(String name, UMLRTCapsulePart part, UMLRTSlot parent) {
        this(name, part);
        this.parent = parent;
    }

    public void addChild(UMLRTSlot slot) {
        children.add(slot);
    }

    public void addSibling(UMLRTSlot slot) {
        siblings.add(slot);
    }

    public void addConnection(UMLRTPort srcPort, UMLRTPort destPort, UMLRTSlot destSlot) {
        Set<Pair<UMLRTPort, UMLRTSlot>> slots = this.connections.computeIfAbsent(srcPort, k -> new HashSet<>());
        slots.add(Pair.with(destPort, destSlot));
    }

    public String getName() {
        return name;
    }

    public UMLRTCapsulePart getPart() {
        return part;
    }

    public UMLRTSlot getParent() {
        return parent;
    }

    public List<UMLRTSlot> getChildren() {
        return children;
    }

    public List<UMLRTSlot> getSiblings() {
        return siblings;
    }

    public Map<UMLRTPort, Set<Pair<UMLRTPort, UMLRTSlot>>> getConnections() {
        return connections;
    }
}