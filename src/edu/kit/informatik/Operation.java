package edu.kit.informatik;

import java.util.HashMap;

final class Operation {

    private static final int INPUT_COMMAND_INDEX = 0;
    private static final int INPUT_PARAMETER_INDEX = 1;
    private static final int INPUT_ID_INDEX = 0;
    private static final int INPUT_NA_PAIR_INDEX = 1;
    private static final int NA_PAIR_NAME_INDEX = 1;
    private static final int NA_PAIR_AMOUNT_INDEX = 0;

    private static final String OK_STRING = "OK";
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
        String[] cmdComponents = input.split(BlueprintZ.Defaults.COMMAND_PARAMETER_SEPARATOR);
        if (cmdComponents.length < 1)
            return new Result<>(null, new Error(Error.Type.NO_INPUT));

        Command cmd = Command.build(cmdComponents[INPUT_COMMAND_INDEX]);
        if (cmd == null)
            return new Result<>(null, new Error(Error.Type.CMD_NOT_VALID));

        String parameters = "";

        if (cmdComponents.length == 2)
            parameters = cmdComponents[INPUT_PARAMETER_INDEX];

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
            case ADD_PART:
                return addPart();
            case REMOVE_PART:
                return removePart();
            default:
                return new Result<>(null, new Error(Error.Type.OTHER));
        }
    }

    private Result<String> addAssembly() {
        String[] parameters = parameterString.split(BlueprintZ.Defaults.SETTER_LITERAL);
        String id = parameters[INPUT_ID_INDEX];

        try {
            HashMap<String, Integer> children = new HashMap<>();
            for (String naPairString : parameters[INPUT_NA_PAIR_INDEX].split(BlueprintZ.Defaults.PART_SEPARATOR)) {
                String[] naPair = naPairString.split(BlueprintZ.Defaults.NAME_AMOUNT_SEPARATOR);
                String name = naPair[NA_PAIR_NAME_INDEX];
                if (children.keySet().stream().anyMatch(sp -> sp.equals(name)))
                    return new Result<>(null, new Error(Error.Type.MULTIPLE_PART_REFERENCES, name));
                Integer amount = Integer.parseInt(naPair[NA_PAIR_AMOUNT_INDEX]);
                if (amount < BlueprintZ.Defaults.MIN_AMOUNT)
                    return new Result<>(null, new Error(Error.Type.NUMBER_NOT_IN_RANGE, amount.toString()));
                children.put(name, amount);
            }

            Result<Void> result = PartManager.MAIN.addAssemblyWith(id, children);
            if (result.isSuccessful()) {
                return new Result<>(OK_STRING, null);
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
            return new Result<>(OK_STRING, null);
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

    private Result<String> addPart() {
        String[] parameters = parameterString.split("\\" + BlueprintZ.Defaults.ADD_LITERAL);
        String toId = parameters[INPUT_ID_INDEX];
        String[] naPair = parameters[INPUT_NA_PAIR_INDEX].split(BlueprintZ.Defaults.NAME_AMOUNT_SEPARATOR);

        String id = naPair[NA_PAIR_NAME_INDEX];
        String amountString = naPair[NA_PAIR_AMOUNT_INDEX];

        try {
            int amount = Integer.parseInt(amountString);
            if (amount < BlueprintZ.Defaults.MIN_AMOUNT)
                return new Result<>(null, new Error(Error.Type.NUMBER_NOT_IN_RANGE, Integer.toString(amount)));

            Result<Void> result = PartManager.MAIN.addPart(toId, id, amount);
            if (result.isSuccessful()) {
                return new Result<>(OK_STRING, null);
            } else {
                return new Result<>(null, result.error);
            }
        } catch (NumberFormatException ex) {
            return new Result<>(null, new Error(Error.Type.NUMBER_NOT_VALID));
        }
    }

    private Result<String> removePart() {
        String[] parameters = parameterString.split(BlueprintZ.Defaults.SUBTRACT_LITERAL);
        String fromId = parameters[INPUT_ID_INDEX];
        String[] naPair = parameters[INPUT_NA_PAIR_INDEX].split(BlueprintZ.Defaults.NAME_AMOUNT_SEPARATOR);

        String id = naPair[NA_PAIR_NAME_INDEX];
        String amountString = naPair[NA_PAIR_AMOUNT_INDEX];

        try {
            int amount = Integer.parseInt(amountString);
            if (amount < BlueprintZ.Defaults.MIN_AMOUNT)
                return new Result<>(null, new Error(Error.Type.NUMBER_NOT_IN_RANGE, Integer.toString(amount)));

            Result<Void> result = PartManager.MAIN.removePart(fromId, id, amount);
            if (result.isSuccessful()) {
                return new Result<>(OK_STRING, null);
            } else {
                return new Result<>(null, result.error);
            }
        } catch (NumberFormatException ex) {
            return new Result<>(null, new Error(Error.Type.NUMBER_NOT_VALID));
        }
    }
}
