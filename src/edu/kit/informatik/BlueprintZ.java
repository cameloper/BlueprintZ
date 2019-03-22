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
        if (!operationResult.isSuccessful())
            Terminal.printError(operationResult.error.toString());
    }

    class Defaults {
        static final String PART_NAME_REGEX = "[a-zA-Z]+";
        static final String AMOUNT_REGEX = "(1000|([0-9]{1,3}))";
    }
}
