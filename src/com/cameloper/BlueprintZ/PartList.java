package com.cameloper.BlueprintZ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

class PartList {
    private final ArrayList<Part> parts;

    /**
     * Creates a new {@link PartList} object using the properties of given one
     *
     * @param origin The {@link PartList} to be cloned
     */
    PartList(PartList origin) {
        this.parts = new ArrayList<>();
        for (Part part : origin.parts) {
            this.parts.add(new Part(part));
        }
    }

    /**
     * Empty constructor for {@link PartList}
     */
    PartList() {
        this.parts = new ArrayList<>();
    }

    /**
     * Method to gather a Part object with given ID
     *
     * @param id ID of requested Part object
     * @return {@link Part} with given ID, if present
     */
    Part getPartWith(String id) {
        for (Part p : parts) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Adds the given part in PartList
     *
     * @param part Part to add
     */
    void addPart(Part part) {
        parts.add(part);
    }

    /**
     * Adds a new Part with the given ID in PartList
     *
     * @param id ID of new part
     */
    void addIfNotPresent(String id) {
        if (!contains(id)) {
            parts.add(new Part(id, new HashMap<>()));
        }
    }

    /**
     * Adds a new Part with each ID in the given set, if they
     * are not already present.
     *
     * @param ids IDs of the parts to add
     */
    void addAllIfNotPresent(Set<String> ids) {
        for (String id : ids) {
            addIfNotPresent(id);
        }
    }

    /**
     * Removes the given part from list.
     *
     * @param part Part to be removed
     */
    void removePart(Part part) {
        parts.remove(part);
    }

    /**
     * Says whether a part with the given ID is present or not
     *
     * @param id ID to check
     * @return true if such a part exist, otherwise false
     */
    boolean contains(String id) {
        for (Part p : parts) {
            if (p.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for an unwanted cycle through each part.
     *
     * @return ID of the part that contains one or more of its ancestors
     */
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
        for (String child : part.getChildren().keySet()) {
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

    /**
     * Removes each assembly without children
     * and each component without parents
     */
    void postRemovalCleanup() {
        cleanup();
        cleanup();
    }

    private void cleanup() {
        ArrayList<Part> partsToRemove = new ArrayList<>();

        for (Part part : parts) {
            if (part.getType() == Part.Type.ASSEMBLY && part.getChildren().isEmpty())
                partsToRemove.add(part);
            else if (part.getType() == Part.Type.COMPONENT && !partHasParents(part))
                partsToRemove.add(part);
        }

        for (Part part : partsToRemove) {
            parts.remove(part);
        }
    }

    /**
     * Searches through children of each part for the given ID
     *
     * @param  part Part to search for
     * @return true if any other part has the given ID as child
     */
    boolean partHasParents(Part part) {
        String id = part.getId();
        return parts.stream().anyMatch(p -> p.getChildren().keySet().stream().anyMatch(s -> s.equals(id)));
    }

    /**
     * Recursively goes through child parts and counts
     * amount of each part of the given type.
     *
     * @param part Starting part
     * @param type Type of parts that should be counted
     * @return Total count of each part with type
     */
    HashMap<Part, Integer> childrenOf(Part part, Part.Type type) {
        HashMap<Part, Integer> out = new HashMap<>();
        for (Map.Entry<String, Integer> entry : part.getChildren().entrySet()) {
            Part child = getPartWith(entry.getKey());
            if (child.getType() == type) {
                if (out.containsKey(child))
                    out.put(child, out.get(child) + entry.getValue());
                else
                    out.put(child, entry.getValue());
            }

            if (!child.getChildren().isEmpty())
                merge(out, childrenOf(child, type), entry.getValue());
        }

        return out;

    }

    private void merge(HashMap<Part, Integer> left,
                       HashMap<Part, Integer> right,
                       Integer multiplier) {
        for (Map.Entry<Part, Integer> entry : right.entrySet()) {
            Part key = entry.getKey();
            if (left.containsKey(key)) {
                left.put(key, left.get(key) + multiplier * entry.getValue());
                continue;
            }

            left.put(key, multiplier * entry.getValue());
        }
    }
}