package org.codetest.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.codetest.common.Common.*;

/**
 * manipulating strings based on certain rules
 *
 * @author Jude
 * @since 1.0
 */
public class OperationService {
    public Optional<Object> removeOperation(String sourceString, int repetitionCount) {
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

    public Optional<Object> replaceOperation(String sourceString, int repetitionCount) {
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
