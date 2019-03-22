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
         * User didn't type in anything
         */
        NO_INPUT,
        /**
         * User entered an invalid text
         */
        CMD_NOT_VALID,
        /**
         * User gave invalid parameters
         */
        PARAM_NOT_VALID,
        /**
         * User gave multiple references to the same part
         */
        MULTIPLE_PART_REFERENCES,
        /**
         * Failed to cast a number parameter to Integer
         */
        NUMBER_NOT_VALID,
        /**
         * Given number is not in the correct range.
         */
        NUMBER_NOT_IN_RANGE,
        /**
         * And assembly with the given id already exists
         */
        ASSEMBLY_ALREADY_EXISTS,
        /**
         * The changes that have been made cause a cycle
         */
        NOT_ACYCLIC,
        /**
         * There is no part with given ID
         */
        PART_DOESNT_EXIST,
        /**
         * The part with given ID is an assembly
         */
        PART_IS_ASSEMBLY,
        /**
         * The part with given ID is a component
         */
        PART_IS_COMPONENT,
        /**
         * Something went wrong
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
                case MULTIPLE_PART_REFERENCES:
                    return "name of Part \"$p\" has been written more than once!";
                case NUMBER_NOT_VALID:
                    return "one or more of the given numbers are not in correct format.";
                case NUMBER_NOT_IN_RANGE:
                    return "at least one given number is not in the accepted range (1-1000).";
                case ASSEMBLY_ALREADY_EXISTS:
                    return "the assembly with id \"$p\" already exists.";
                case NOT_ACYCLIC:
                    return "such an alteration causes a cycle through Assembly $p and thus cannot be made.";
                case PART_DOESNT_EXIST:
                    return "there isn't any part with the given ID \"$p\".";
                case PART_IS_ASSEMBLY:
                    return "the part with given ID \"$p\" is an assembly.";
                case PART_IS_COMPONENT:
                    return "the part with given ID \"$p\" is a component.";
                default:
                    return "something happened. No idea tbh.";
            }
        }
    }
}