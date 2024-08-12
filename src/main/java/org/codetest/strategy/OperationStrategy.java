package org.codetest.strategy;

import java.util.Optional;

public interface OperationStrategy {
    /**
     * provides a way to handle strings with a certain repetition count
     *
     * @param sourceString    string need to handle
     * @param repetitionCount consecutive occurrences
     * @return Optional
     */
    Optional<Object> doOperation(String sourceString, int repetitionCount);
}
