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
     * New Result builder with value and error
     *
     * @param value result value of a particular operation
     * @param error the cause of failure, if present
     */
    Result(T value, Error error) {
        this.value = value;
        this.error = error;

        this.type = value == null && error != null ? Type.FAILURE : Type.SUCCESS;
    }

    /**
     * Boolean method whether the operation type is success or failure
     *
     * @return true if successful, otherwise false
     */
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
