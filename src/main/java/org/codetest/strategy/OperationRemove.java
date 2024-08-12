package org.codetest.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.codetest.common.Common.*;

/**
 * implementation of the OperationStrategy interface
 * to remove duplicate characters from a string based on their occurrence count
 *
 * @author Jude
 * @since 1.0
 */
public class OperationRemove implements OperationStrategy {
    @Override
    public Optional<Object> doOperation(String sourceString, int repetitionCount) {
        List<String> result = new ArrayList<>();
        List<Character> characterList = getDuplicatesChar(sourceString, repetitionCount);
        validate(sourceString, characterList, result);
        for (Character character : characterList) {
            int index = sourceString.indexOf(String.valueOf(character));
            sourceString = remove(sourceString, character, index, repetitionCount);
            result.add(sourceString);
        }
        return Optional.of(result);
    }
}
