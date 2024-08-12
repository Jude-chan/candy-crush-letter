package org.codetest.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for creating and executing an operation based on a given stage
 *
 * @author Jude
 * @since 1.0
 */
public class SolutionFactory {
    public static Optional<Object> solution(String str, String stage, int repetitionCount) {
        OperationStrategy strategy;
        if ("Stage1".equalsIgnoreCase(stage)) {
            strategy = new OperationRemove();
        } else if ("Stage2".equalsIgnoreCase(stage)){
            strategy = new OperationReplace();
        } else {
            List<String> failedResult = new ArrayList<>();
            failedResult.add("no such stage");
            return Optional.of(failedResult);
        }
        return strategy.doOperation(str, repetitionCount);
    }
}

