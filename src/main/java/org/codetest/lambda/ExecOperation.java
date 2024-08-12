package org.codetest.lambda;

import java.util.*;
import java.util.function.BiFunction;

/**
 * this class acts as an intermediary between different stages of operations
 * and their specific implementations provided by OperationService
 *
 * @author Jude
 * @since 1.0
 */
public class ExecOperation {
    private static final OperationService SERVICE = new OperationService();
    private static final Map<String, BiFunction<String, Integer, Optional<Object>>> OPERATION_MAP = new HashMap<>();

    static {
        OPERATION_MAP.put("Stage1", SERVICE::removeOperation);
        OPERATION_MAP.put("Stage2", SERVICE::replaceOperation);
    }

    /**
     * retrieves the corresponding function from OPERATION_MAP based on the input stage
     *
     * @param stage           A string representing a stage
     * @param sourceString    string need to handle
     * @param repetitionCount consecutive occurrences
     * @return Optional
     */
    public Optional<Object> getResult(String stage, String sourceString, int repetitionCount) {
        BiFunction<String, Integer, Optional<Object>> result = OPERATION_MAP.get(stage);
        if (result != null) {
            return result.apply(sourceString, repetitionCount);
        }
        List<String> failedResult = new ArrayList<>();
        failedResult.add("no such stage");
        return Optional.of(failedResult);
    }
}
