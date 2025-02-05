package com.cameloper.BlueprintZ;

import java.util.HashMap;
import java.util.Map;

class Part {
    private final String id;
    private HashMap<String, Integer> children;

    /**
     * Creates a new {@link Part} object using the properties of given one
     *
     * @param origin The part object to be cloned
     */
    Part(Part origin) {
        this.id = origin.id;
        this.children = new HashMap<>();
        for (Map.Entry<String, Integer> child: origin.children.entrySet()) {
            this.children.put(child.getKey(), child.getValue());
        }
    }

    /**
     * Default constructor of Part
     *
     * @param id       by-user-given id of Part
     * @param children If part is assembly, child Parts
     */
    Part(String id, HashMap<String, Integer> children) {
        this.id = id;
        this.children = children;
    }

    /**
     * Getter of Id
     *
     * @return value of private variable Id
     */
    String getId() {
        return id;
    }

    /**
     * Getter of Children
     *
     * @return value of private variable Children
     */
    HashMap<String, Integer> getChildren() {
        return children;
    }

    /**
     * Adds the given ID with given amount in children.
     * If a child with the same ID already exists, increases the amount
     * by corresponding parameter.
     *
     * @param id ID of Part
     * @param amount How many of the given part should be added
     */
    void addChild(String id, Integer amount) {
        for (Map.Entry<String, Integer> child: children.entrySet()) {
            if (child.getKey().equals(id)) {
                child.setValue(child.getValue() + amount);
                return;
            }
        }

        children.put(id, amount);
    }

    /**
     * Calls the {@code addChild()} method for each child in given {@link HashMap}
     *
     * @param children Every child to be added
     */
    void addChildren(HashMap<String, Integer> children) {
        for (Map.Entry<String, Integer> child: children.entrySet()) {
            addChild(child.getKey(), child.getValue());
        }
    }

    /**
     * Removes the given id from children if there are enough
     *
     * @param id ID to remove
     * @param amount How many of the ID to remove
     * @return true if removal happened, otherwise false
     */
    boolean removeChild(String id, int amount) {
        if (children.keySet().stream().noneMatch(p -> p.equals(id)))
            return false;

        for (Map.Entry<String, Integer> child : children.entrySet()) {
            if (child.getKey().equals(id)) {
                int currentAmount = child.getValue();

                if (currentAmount < amount)
                    return false;
                else if (currentAmount == amount) {
                    children.remove(child.getKey());
                    return true;
                } else {
                    child.setValue(currentAmount - amount);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Replaces the current children with an empty HashMap
     */
    void removeAllChildren() {
        children = new HashMap<>();
    }

    /**
     * Gives whether the Part is assembly or component
     *
     * @return Assembly or component
     */
    Type getType() {
        if (children.isEmpty())
            return Type.COMPONENT;
        else
            return Type.ASSEMBLY;
    }

    enum Type {
        /**
         * Group Parts that include other Parts
         */
        ASSEMBLY,
        /**
         * End-Node parts without any children
         */
        COMPONENT
    }
}