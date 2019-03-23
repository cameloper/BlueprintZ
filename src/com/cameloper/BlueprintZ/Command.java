package com.cameloper.BlueprintZ;

enum Command {
    /**
     * Adds a new assembly Part
     */
    ADD_ASSEMBLY("addAssembly"),
    /**
     * Removes the assembly part with given ID
     */
    REMOVE_ASSEMBLY("removeAssembly"),
    /**
     * Prints contents of a assembly Part
     */
    PRINT_ASSEMBLY("printAssembly"),
    /**
     * Lists all child-assemblies of an assembly
     */
    GET_ASSEMBLIES("getAssemblies"),
    /**
     * Lists all child-components of an assembly
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

    private final String rawValue;

    /**
     * Default constructor for Command
     *
     * @param input case value
     */
    Command(String input) {
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
     * Gives the obligatory regex literal for this command
     *
     * @return Regex literal
     */
    String getRegex() {
        String pnr = Main.Defaults.PART_NAME_REGEX;
        String ar = Main.Defaults.AMOUNT_REGEX;
        String naSL = Main.Defaults.NAME_AMOUNT_SEPARATOR;
        switch (this) {
            case ADD_PART:
                return pnr + "\\" + Main.Defaults.ADD_LITERAL + ar + naSL + pnr;
            case REMOVE_PART:
                return pnr + Main.Defaults.SUBTRACT_LITERAL + ar + naSL + pnr;
            case ADD_ASSEMBLY:
                return String.format("%s%s(%s%s%s%s)*(%s%s%s)",
                        pnr,
                        Main.Defaults.SETTER_LITERAL,
                        ar,
                        naSL,
                        pnr,
                        Main.Defaults.PART_SEPARATOR,
                        ar,
                        naSL,
                        pnr);
            case GET_ASSEMBLIES:
            case GET_COMPONENTS:
            case PRINT_ASSEMBLY:
            case REMOVE_ASSEMBLY:
                return pnr;
            default:
                return "";
        }
    }
}