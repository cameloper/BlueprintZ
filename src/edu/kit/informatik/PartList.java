package edu.kit.informatik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

class PartList {
    private final ArrayList<Part> parts;

    /**
     * Creates a new {@link PartList} object using the properties of given one
     *
     * @param origin The {@link PartList} to be cloned
     */
    PartList(PartList origin) {
        this.parts = new ArrayList<>();
        for (Part part: origin.parts) {
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
     * Adds a new Part with each ID in the given set, if they
     * are not already present.
     *
     * @param ids IDs of the parts to add
     */
    void addIfNotPresent(Set<String> ids) {
        for (String id: ids) {
            if (!hasPartWith(id)) {
                parts.add(new Part(id, new HashMap<>()));
            }
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
    boolean hasPartWith(String id) {
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

    /**
     * Searches through children of each part for the given ID
     *
     * @param id ID of part to search for
     * @return true if any other part has the given ID as child
     */
    boolean partHasParents(String id) {
        return parts.stream().anyMatch(p -> p.getChildren().keySet().stream().anyMatch(s -> s.equals(id)));
    }
}
