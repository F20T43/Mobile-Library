package com.stl.mobilelibrary;


public interface Validatable {
    /**
     * Determines if the current fields are valid
     * @return true if valid, false otherwise
     */
    boolean isValid();

    /**
     * Get the error message
     * @return the error message
     */
    String getErrorMessage();
}
