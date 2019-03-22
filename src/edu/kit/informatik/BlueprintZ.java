package edu.kit.informatik;

public class BlueprintZ {

    /**
     * Decides whether the program should continue listening for commands
     */
    static boolean isListening = true;

    /**
     * The method that will be called with terminal arguments
     *
     * @param args System arguments
     */
    public static void main(String[] args) {
        loop();

    }

    private static void loop() {
        while (isListening) {
            go(Terminal.readLine());
        }
    }

    private static void go(String input) {
        Result<Operation> operationResult = Operation.buildWith(input);
        if (!operationResult.isSuccessful()) {
            Terminal.printError(operationResult.error.toString());
            return;
        }

        Operation operation = operationResult.value;
        if (!operation.validate()) {
            Terminal.printError(new Error(Error.Type.PARAM_NOT_VALID).toString());
            return;
        }

        Result<String> executionResult = operation.execute();
        if (executionResult.isSuccessful()) {
            Terminal.printLine(executionResult.value);
        } else {
            Terminal.printError(executionResult.error.toString());
        }
    }

    class Defaults {
        /**
         * Regex rules for part name inputs
         */
        static final String PART_NAME_REGEX = "[a-zA-Z]+";
        /**
         * Regex rules for amount inputs
         */
        static final String AMOUNT_REGEX = "(1000|([0-9]{1,3}))";
    }
}
