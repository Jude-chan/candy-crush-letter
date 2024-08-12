package org.codetest.common;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Common class
 *
 * @author Jude
 * @since 1.0
 */
public class Common {
    /**
     * find and return a List of characters from the given sourceString which appear more than
     * or equal to repetitionCount times
     *
     * @param sourceString    string need to handle
     * @param repetitionCount consecutive occurrences
     * @return List
     */
    public static List<Character> getDuplicatesChar(String sourceString, int repetitionCount) {
        // Construct two stacks, where the first stack stores characters
        // and the second stack stores the frequency of each character.
        List<Character> list = new LinkedList<>();
        if (sourceString == null || sourceString.length() < 3) {
            return list;
        }
        Deque<Character> stack1 = new ArrayDeque<>();
        Deque<Integer> stack2 = new ArrayDeque<>();
        for (char ch : sourceString.toCharArray()) {
            // checks if this is either the first different character or if the stack is empty
            if (stack1.isEmpty() || stack1.peekLast() != ch) {
                stack1.addLast(ch);
                stack2.addLast(1);
            } else {
                // current character's frequency increased by 1
                stack2.addLast(stack2.pollLast() + 1);
            }
            // checks if the current character's frequency equals repetitionCount, add to list
            if (stack2.peekLast() == repetitionCount) {
                list.add(stack1.removeLast());
                stack2.removeLast();
            }
        }
        return list;
    }

    /**
     * replaces a specified character in a string at a given index with another character,
     * and repeats this replacement for the specified number of times
     *
     * @param sourceString    string need to handle
     * @param character       character need to replace
     * @param index           character index in string
     * @param repetitionCount consecutive occurrences
     * @return string after replace
     */
    public static String replace(String sourceString, Character character, int index, int repetitionCount) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sourceString, 0, index).append((char) (character - 1))
                .append(sourceString.substring(index + repetitionCount));
        return stringBuilder.toString();
    }

    /**
     * removes a specified number of characters from a given string at a particular index
     *
     * @param sourceString    string need to handle
     * @param character       character need to replace
     * @param index           character index in string
     * @param repetitionCount consecutive occurrences
     * @return string after remove
     */
    public static String remove(String sourceString, Character character, int index, int repetitionCount) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sourceString, 0, index)
                .append(sourceString.substring(index + repetitionCount));
        return stringBuilder.toString();
    }

    public static void validate(String sourceString, List<Character> characterList, List<String> result) {
        if (sourceString == null || sourceString.length() < 3 || characterList.isEmpty()) {
            result.add(sourceString);
        }
    }
}
