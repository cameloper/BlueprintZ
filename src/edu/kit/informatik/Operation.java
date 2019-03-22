package edu.kit.informatik;

import java.util.HashMap;

final class Operation {
    private static final String COMMAND_PARAMETER_SEPARATOR = " ";

    private final Command command;
    private final String parameterString;

    private Operation(Command command, String parameterString) {
        this.command = command;
        this.parameterString = parameterString;
    }

    /**
     * Default builder for Operation
     *
     * @param input The input string from user
     * @return Result of the build
     */
    static Result<Operation> buildWith(String input) {
        String[] cmdComponents = input.split(COMMAND_PARAMETER_SEPARATOR);
        if (cmdComponents.length < 1)
            return new Result<>(null, new Error(Error.Type.NO_INPUT));

        Command cmd = Command.build(cmdComponents[0]);
        if (cmd == null)
            return new Result<>(null, new Error(Error.Type.CMD_NOT_VALID));

        String parameters = "";

        if (cmdComponents.length == 2)
            parameters = cmdComponents[1];

        return new Result<>(new Operation(cmd, parameters), null);
    }

    /**
     * Validates the parameter string with relevant regex literals
     *
     * @return true if parameters are valid, otherwise false
     */
    boolean validate() {
        return parameterString.matches(command.getRegex());
    }

    /**
     * Executes the process regarding current command
     *
     * @return Result with user-friendly description in it.
     */
    Result<String> execute() {
        switch (command) {
            case QUIT:
                BlueprintZ.isListening = false;
                return new Result<>(null, null);
            case ADD_ASSEMBLY:
                return addAssembly();
            case REMOVE_ASSEMBLY:
                return removeAssembly();
            case PRINT_ASSEMBLY:
                return printAssembly();
            case GET_ASSEMBLIES:
                return getAssemblies();
            case GET_COMPONENTS:
                return getComponents();
            default:
                return new Result<>(null, new Error(Error.Type.OTHER));
        }
    }

    private Result<String> addAssembly() {
        String[] parameters = parameterString.split("=");

        String id = parameters[0];

        try {
            HashMap<String, Integer> children = new HashMap<>();
            for (String s : parameters[1].split(";")) {
                String[] p = s.split(":");
                if (children.keySet().stream().anyMatch(sp -> sp.equals(p[1])))
                    return new Result<>(null, new Error(Error.Type.MULTIPLE_PART_REFERENCES, p[1]));
                children.put(p[1], Integer.parseInt(p[0]));
            }

            Result<Void> result = PartManager.MAIN.addAssemblyWith(id, children);
            if (result.isSuccessful()) {
                return new Result<>("OK", null);
            } else {
                return new Result<>(null, result.error);
            }

        } catch (NumberFormatException nEx) {
            return new Result<>(null, new Error(Error.Type.NUMBER_NOT_VALID));
        }
    }

    private Result<String> removeAssembly() {
        Result<Void> result = PartManager.MAIN.removeAssemblyWith(parameterString);

        if (result.isSuccessful()) {
            return new Result<>("OK", null);
        } else {
            return new Result<>(null, result.error);
        }
    }

    private Result<String> printAssembly() {
        return PartManager.MAIN.printAssemblyWith(parameterString);
    }

    private Result<String> getAssemblies() {
        return PartManager.MAIN.getAssembliesOf(parameterString);
    }

    private Result<String> getComponents() {
        return PartManager.MAIN.getComponentsOf(parameterString);
    }
}
