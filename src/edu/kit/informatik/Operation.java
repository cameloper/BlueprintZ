package edu.kit.informatik;

class Operation {
    private static final String COMMAND_PARAMETER_SEPARATOR = " ";

    private Command command;
    private String parameterString;

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
            return new Result<>(new Error(Error.Type.NO_INPUT));

        Command cmd = Command.build(cmdComponents[0]);
        if (cmd == null)
            return new Result<>(new Error(Error.Type.CMD_NOT_VALID));

        String parameters = null;

        if (cmdComponents.length == 2)
            parameters = cmdComponents[1];

        return new Result<>(new Operation(cmd, parameters));
    }
}
