package edu.kit.informatik;

import java.util.HashMap;

class Part {
    private String id;
    private HashMap<String, Integer> children;

    /**
     * Default constructor of Part
     *
     * @param id       by-user-given id of Part
     * @param children If part is assembly, child Parts
     */
    public Part(String id, HashMap<String, Integer> children) {
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