package edu.kit.informatik;

public class Material implements Part {
    private String id;

    /**
     * Public etter of Id
     *
     * @return value of private variable Id
     */
    @Override
    public String getId() {
        return id;
    }
}
