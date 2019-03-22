package edu.kit.informatik;

enum Command {
    /**
     * Adds a new Group object
     */
    ADD_ASSEMBLY,
    /**
     * Prints contents of a Group object
     */
    PRINT_ASSEMBLY,
    /**
     * Lists all child-groups of a Group
     */
    GET_ASSEMBLIES,
    /**
     * Lists all materials of a Group
     */
    GET_COMPONENTS,
    /**
     * Adds a new part
     */
    ADD_PART,
    /**
     * Removes a specific part
     */
    REMOVE_PART,
    /**
     * Quits the app
     */
    QUIT
}
