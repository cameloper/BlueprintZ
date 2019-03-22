package edu.kit.informatik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PartList {
    private ArrayList<Part> parts;

    PartList(PartList origin) {
        this.parts = new ArrayList<>();
        for (Part part: origin.parts) {
            this.parts.add(new Part(part));
        }
    }

    PartList() {
        this.parts = new ArrayList<>();
    }

    Part getPartWith(String id) {
        for (Part p : parts) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    void addPart(Part part) {
        parts.add(part);
    }

    void addIfNotPresent(Set<String> ids) {
        for (String id: ids) {
            if (!hasPartWith(id)) {
                parts.add(new Part(id, new HashMap<>()));
            }
        }
    }

    boolean hasPartWith(String id) {
        for (Part p : parts) {
            if (p.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    String cycleRoot() {
        for (Part p : parts) {
            String result = partIsFreeOf(p, "");
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private String partIsFreeOf(Part part, String parents) {
        for (String child: part.getChildren().keySet()) {
            if (parents.contains(child)) {
                return child;
            }

            String nParents = parents + "," + child;
            String result = partIsFreeOf(getPartWith(child), nParents);
            if (result != null)
                return result;
        }

        return null;
    }
}
