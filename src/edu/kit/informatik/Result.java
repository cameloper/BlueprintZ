package edu.kit.informatik;

class Result<T> {
    /**
     * Value ({@linkplain T} if the result is a success)
     */
    final T value;
    /**
     * Error if the result is a failure
     */
    final Error error;
    /**
     * Whether the result is successful or not
     */
    private final Type type;

    /**
     * New Result builder with only a value.
     * Use if result is a success
     *
     * @param value result value of a particular operation
     */
    Result(T value) {
        this.value = value;
        this.error = null;
        this.type = Type.SUCCESS;
    }

    /**
     * New Result builder with only an error
     * Use if result is a failure
     *
     * @param error error that caused an operation to fail
     */
    Result(Error error) {
        this.value = null;
        this.error = error;
        this.type = Type.FAILURE;
    }

    boolean isSuccessful() {
        return type == Type.SUCCESS;
    }

    enum Type {
        /**
         * Case where everything went smooth
         */
        SUCCESS,
        /**
         * Case when something unexpected took place
         */
        FAILURE
    }
}
