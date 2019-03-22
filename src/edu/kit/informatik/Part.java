package edu.kit.informatik;

import java.util.HashMap;

class Part {
    private String id;
    private HashMap<String, Integer> children;

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

    Type getType() {
        if (children.isEmpty())
            return Type.COMPONENT;
        else
            return Type.ASSEMBLY;
    }

    enum Type {
        ASSEMBLY,
        COMPONENT
    }
}
