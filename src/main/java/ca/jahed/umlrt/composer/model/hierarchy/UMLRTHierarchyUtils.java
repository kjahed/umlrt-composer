package ca.jahed.umlrt.composer.model.hierarchy;

import ca.jahed.umlrt.composer.model.*;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UMLRTHierarchyUtils {
    public static UMLRTSlot buildHierarchy(UMLRTModel model) {
        UMLRTSlot topSlot = new UMLRTSlot(model.getTop().getName(), model.getTop());
        List<UMLRTSlot> slots = buildHierarchy(topSlot);
        findNeighbors(slots);
        return topSlot;
    }

    static List<UMLRTSlot> buildHierarchy(UMLRTSlot slot) {
        List<UMLRTSlot> slots = new ArrayList<>();
        slots.add(slot);

        UMLRTCapsule capsule = slot.getPart().getCapsule();
        for (UMLRTCapsulePart childPart : capsule.getParts()) {
            for(int i=0; i<childPart.getReplication(); i++) {
                UMLRTSlot childSlot = new UMLRTSlot(slot.getName()
                        + "." + childPart.getName() + "[" + i + "]", childPart, slot);
                slot.addChild(childSlot);
                slots.addAll(buildHierarchy(childSlot));
            }
        }

        for(int i=0; i<slot.getChildren().size(); i++) {
            for(int j=i+1; j<slot.getChildren().size(); j++) {
                slot.getChildren().get(i).addSibling(slot.getChildren().get(j));
                slot.getChildren().get(j).addSibling(slot.getChildren().get(i));
            }
        }

        return slots;
    }

    static void findNeighbors(List<UMLRTSlot> slots) {
        findWiredNeighbors(slots);
        findNoneWiredNeighbors(slots);
    }

    static void findWiredNeighbors(List<UMLRTSlot> slots) {
        for (UMLRTSlot slot : slots) {
            List<UMLRTConnector> connectors = slot.getPart().getCapsule().getConnectors();
            for (UMLRTConnector connector : connectors) {
                UMLRTConnectorEnd end1 = connector.getEnd1();
                UMLRTConnectorEnd end2 = connector.getEnd2();

                if(end1.getPart() == null && end2.getPart() == null) {
                    slot.addConnection(end1.getPort(), end2.getPort(), slot);
                }
                else if(end1.getPart() == null) {
                    slot.getChildren().stream().filter(s -> s.getPart().equals(end2.getPart())).forEach(s -> {
                        slot.addConnection(end1.getPort(), end2.getPort(), s);
                        s.addConnection(end2.getPort(), end1.getPort(), slot);
                    });
                }
                else if(end2.getPart() == null) {
                    slot.getChildren().stream().filter(s -> s.getPart().equals(end1.getPart())).forEach(s -> {
                        slot.addConnection(end2.getPort(), end1.getPort(), s);
                        s.addConnection(end1.getPort(), end2.getPort(), slot);
                    });
                }
                else {
                    slot.getChildren().stream()
                            .filter(s1 -> s1.getPart().equals(end1.getPart()))
                            .forEach(s1 -> slot.getChildren().stream()
                                    .filter(s2 -> s2.getPart().equals(end2.getPart()))
                                    .forEach(s2 -> {
                                        s1.addConnection(end1.getPort(), end2.getPort(), s2);
                                        s2.addConnection(end2.getPort(), end1.getPort(), s1);
                                    }));
                }
            }
        }

        resolveRelays(slots);
    }

    static void resolveRelays(List<UMLRTSlot> slots) {
        for (UMLRTSlot slot : slots) {
            for (UMLRTPort port : slot.getConnections().keySet()) {
                if(port.isRelay())
                    continue;

                Set<Pair<UMLRTPort, UMLRTSlot>> resolvedConnections = new HashSet<>();
                for (Pair<UMLRTPort, UMLRTSlot> farEnd : slot.getConnections().get(port)) {
                    Set<Pair<UMLRTPort, UMLRTSlot>> visited = new HashSet<>();
                    visited.add(Pair.with(port, slot));
                    resolvedConnections.add(resolveFarEnd(farEnd, visited));
                }
                slot.getConnections().put(port, resolvedConnections);
            }
        }

        for(UMLRTSlot slot : slots) {
            slot.getConnections().keySet().stream().filter(UMLRTPort::isRelay)
                    .collect(Collectors.toSet()).forEach(port -> slot.getConnections().remove(port));
        }
    }

    static Pair<UMLRTPort, UMLRTSlot> resolveFarEnd(Pair<UMLRTPort, UMLRTSlot> farEnd,
                                             Set<Pair<UMLRTPort, UMLRTSlot>> visited) {
        visited.add(farEnd);
        UMLRTPort port = farEnd.getValue0();
        UMLRTSlot slot = farEnd.getValue1();

        if(!port.isRelay())
            return farEnd;

        Set<Pair<UMLRTPort, UMLRTSlot>> entries = slot.getConnections().get(port).stream()
                .filter(pair -> !visited.contains(pair)).collect(Collectors.toSet());
        assert entries.size() == 1;
        return resolveFarEnd(entries.iterator().next(), visited);
    }

    static void findNoneWiredNeighbors(List<UMLRTSlot> slots) {
        for (int i=0; i<slots.size(); i++) {
            for (UMLRTPort p1 : slots.get(i).getPart().getCapsule().getPorts()) {
                if(p1.isWired() || !p1.isBehavior())
                    continue;

                for(int j=i+1; j<slots.size(); j++) {
                    for (UMLRTPort p2 : slots.get(j).getPart().getCapsule().getPorts()) {
                        if (p2.isWired() || !p2.isBehavior())
                            continue;

                        if(p1.isService() != p2.isService()
                                && p1.isConjugated() != p2.isConjugated()
                                && (p1.getName().equals(p2.getName())
                                || p1.getRegistrationOverride().equals(p2.getRegistrationOverride()))
                                && p1.getProtocol().equals(p2.getProtocol())) {

                            slots.get(i).addConnection(p1, p2, slots.get(j));
                            slots.get(j).addConnection(p2, p1, slots.get(i));
                        }
                    }
                }
            }
        }
    }
}
