package edu.kit.informatik;

class Error {
    private static final String REPLACEMENT_CHAR = "\\$p";

    private Type type;
    private String replacement;

    /**
     * Creates a new Error object of given type with optional replacement string
     *
     * @param type        The main cause of error
     * @param replacement The replacement text for the display message
     */
    Error(Type type, String replacement) {
        this.type = type;
        this.replacement = replacement;
    }

    /**
     * Creates a new Error object of given type with optional replacement string
     *
     * @param type The main cause of error
     */
    Error(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.message().replaceAll(REPLACEMENT_CHAR, replacement);
    }

    enum Type {
        /**
         * user didn't type in anything
         */
        NO_INPUT,
        /**
         * user entered an invalid text
         */
        CMD_NOT_VALID,
        /**
         * user gave invalid parameters
         */
        PARAM_NOT_VALID,
        /**
         * something went wrong
         */
        OTHER;

        private String message() {
            switch (this) {
                case NO_INPUT:
                    return "please enter a valid command!";
                case CMD_NOT_VALID:
                    return "the given text does not refer to a valid command.";
                case PARAM_NOT_VALID:
                    return "the parameters you entered for this command are not valid.";
                default:
                    return "something happened. No idea tbh";
            }
        }
    }
}