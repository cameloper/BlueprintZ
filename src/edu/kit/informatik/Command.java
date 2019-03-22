package edu.kit.informatik;

enum Command {
    /**
     * Adds a new Group object
     */
    ADD_ASSEMBLY("addAssembly"),
    /**
     * Prints contents of a Group object
     */
    PRINT_ASSEMBLY("printAssembly"),
    /**
     * Lists all child-groups of a Group
     */
    GET_ASSEMBLIES("getAssemblies"),
    /**
     * Lists all materials of a Group
     */
    GET_COMPONENTS("getComponents"),
    /**
     * Adds a new part
     */
    ADD_PART("addPart"),
    /**
     * Removes a specific part
     */
    REMOVE_PART("removePart"),
    /**
     * Quits the app
     */
    QUIT("quit");

    private String rawValue;

    private Command(String input) {
        rawValue = input;
    }

    /**
     * Default builder for Command
     *
     * @param input raw input string
     * @return If exists, a command case with matching raw string
     */
    static Command build(String input) {
        for (Command cmd : Command.values()) {
            if (cmd.rawValue.equals(input))
                return cmd;
        }

        return null;
    }

    /**
     * Getter of RawValue
     *
     * @return value of private variable RawValue
     */
    String getRawValue() {
        return rawValue;
    }

    /**
     * Gives the obligatory regex literal for this command
     *
     * @return Regex literal
     */
    String getRegex() {
        String pnr = BlueprintZ.Defaults.PART_NAME_REGEX;
        String ar = BlueprintZ.Defaults.AMOUNT_REGEX;
        switch (this) {
            case ADD_PART:
                return pnr + "+" + ar + ":" + pnr;
            case REMOVE_PART:
                return pnr + "-" + ar + ":" + pnr;
            case ADD_ASSEMBLY:
                return String.format("%s=(%s:%s;)*(%s:%s)", pnr, ar, pnr, ar, pnr);
            case GET_ASSEMBLIES:
            case GET_COMPONENTS:
            case PRINT_ASSEMBLY:
                return pnr;
            default:
                return "";
        }
    }
}

