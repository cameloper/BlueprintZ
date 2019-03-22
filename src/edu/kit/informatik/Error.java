package edu.kit.informatik;

class Error {
    private Type type;
    private String replacement;

    /**
     * Creates a new Error object of given type with optional replacement string
     * @param type The main cause of error
     * @param replacement If exists, the replacement text for the display message
     */
    Error(Type type, String replacement) {
        this.type = type;
        this.replacement = replacement;
    }

    enum Type {

    }
}
