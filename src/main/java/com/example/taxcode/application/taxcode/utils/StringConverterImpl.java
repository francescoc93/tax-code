package com.example.taxcode.application.taxcode.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.example.taxcode.application.utils.ASCIICodeConstants.*;

@Component
public class StringConverterImpl implements StringConverter {
    private static HashMap<Character, Integer> characterOddValue;
    private static final String REGEX = "[A-Za-z0-9]+";
    private static final int NUMBER_0 = 48;

    private StringConverterImpl() {
        loadCharacterOddValues();
    }

    @Override
    public int[] convertStringInTaxCodeIntegerValues(String string) {
        if (!validateString(string)) {
            throw new IllegalArgumentException("The string \"" + string + "\" is not valid. The string must contains " +
                    "digits and letters");
        }

        return convertString(string.toUpperCase());
    }

    private boolean validateString(String string) {
        if (string == null) {
            return false;
        }

        return string.matches(REGEX);
    }


    private int[] convertString(String string) {
        var stringLength = string.length();
        var integerValues = new int[stringLength];

        var upperCaseString = string.toUpperCase();

        for (int i = 0; i < stringLength; i++) {
            var character = upperCaseString.charAt(i);
            integerValues[i] = (i + 1) % 2 == 0 ? convertEvenCharacter(character) : characterOddValue.get(character);
        }

        return integerValues;
    }

    private int convertEvenCharacter(char character) {
        if (character >= CAPITAL_LETTER_A && character <= CAPITAL_LETTER_Z) {
            return character - CAPITAL_LETTER_A;
        }

        return character - NUMBER_0;
    }


    private static void loadCharacterOddValues() {
        characterOddValue = new HashMap<>();

        characterOddValue.put('A', 1);
        characterOddValue.put('0', 1);
        characterOddValue.put('B', 0);
        characterOddValue.put('1', 0);
        characterOddValue.put('C', 5);
        characterOddValue.put('2', 5);
        characterOddValue.put('D', 7);
        characterOddValue.put('3', 7);
        characterOddValue.put('E', 9);
        characterOddValue.put('4', 9);
        characterOddValue.put('F', 13);
        characterOddValue.put('5', 13);
        characterOddValue.put('G', 15);
        characterOddValue.put('6', 15);
        characterOddValue.put('H', 17);
        characterOddValue.put('7', 17);
        characterOddValue.put('I', 19);
        characterOddValue.put('8', 19);
        characterOddValue.put('J', 21);
        characterOddValue.put('9', 21);
        characterOddValue.put('K', 2);
        characterOddValue.put('L', 4);
        characterOddValue.put('M', 18);
        characterOddValue.put('N', 20);
        characterOddValue.put('O', 11);
        characterOddValue.put('P', 3);
        characterOddValue.put('Q', 6);
        characterOddValue.put('R', 8);
        characterOddValue.put('S', 12);
        characterOddValue.put('T', 14);
        characterOddValue.put('U', 16);
        characterOddValue.put('V', 10);
        characterOddValue.put('W', 22);
        characterOddValue.put('X', 25);
        characterOddValue.put('Y', 24);
        characterOddValue.put('Z', 23);
    }
}
