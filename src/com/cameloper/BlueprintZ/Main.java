package com.cameloper.BlueprintZ;

import edu.kit.informatik.Terminal;

public class Main {

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
            printError(operationResult.error);
            return;
        }

        Operation operation = operationResult.value;
        if (!operation.validate()) {
            printError(new Error(Error.Type.PARAM_NOT_VALID));
            return;
        }

        Result<String> executionResult = operation.execute();
        if (executionResult.isSuccessful()) {
            printLine(executionResult.value);
        } else {
            printError(executionResult.error);
        }
    }

    private static void printLine(Object obj) {
        if (obj != null)
            Terminal.printLine(obj);
    }

    private static void printError(Error error) {
        if (error != null)
            Terminal.printError(error.toString());
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
        /**
         * String literal to use when separating commands and parameters
         */
        static final String COMMAND_PARAMETER_SEPARATOR = " ";
        /**
         * String literal to use when separating names and amounts
         */
        static final String NAME_AMOUNT_SEPARATOR = ":";
        /**
         * String literal to use when separating parts
         */
        static final String PART_SEPARATOR = ";";
        /**
         * String literal to use when setting an assembly
         */
        static final String SETTER_LITERAL = "=";
        /**
         * String literal to use when adding to an assembly
         */
        static final String ADD_LITERAL = "+";
        /**
         * String literal to use when subtracting from an assembly
         */
        static final String SUBTRACT_LITERAL = "-";
        /**
         * Minimum value of an amount variable
         */
        static final int MIN_AMOUNT = 1;
    }
}
