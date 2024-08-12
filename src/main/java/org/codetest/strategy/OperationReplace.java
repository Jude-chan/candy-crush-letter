package org.codetest.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.codetest.common.Common.*;

/**
 * implementation of the OperationStrategy interface
 * to replace duplicate characters from a string based on their occurrence count
 *
 * @author Jude
 * @since 1.0
 */
public class OperationReplace implements OperationStrategy {
    @Override
    public Optional<Object> doOperation(String sourceString, int repetitionCount) {
        List<Character> characterList = getDuplicatesChar(sourceString, repetitionCount);
        List<String> result = new ArrayList<>();
        validate(sourceString, characterList, result);
        for (Character character : characterList) {
            int index = sourceString.indexOf(String.valueOf(character));
            if (character == 'a') {
                sourceString = remove(sourceString, character, index, repetitionCount);
            } else {
                sourceString = replace(sourceString, character, index, repetitionCount);
            }
            result.add(sourceString);
        }
        return Optional.of(result);
    }
}
