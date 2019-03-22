package edu.kit.informatik;

import java.util.HashMap;

class Group implements Part {
    private String id;
    private HashMap<String, Integer> children;

    /**
     * Getter of Id
     *
     * @return value of private variable Id
     */
    @Override
    public String getId() {
        return id;
    }

}
