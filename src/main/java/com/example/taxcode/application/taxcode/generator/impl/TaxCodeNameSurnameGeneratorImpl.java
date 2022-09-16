package com.example.taxcode.application.taxcode.generator.impl;

import com.example.taxcode.application.taxcode.generator.TaxCodeNameSurnameGenerator;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.example.taxcode.application.utils.ASCIICodeConstants.*;

@Component
public class TaxCodeNameSurnameGeneratorImpl implements TaxCodeNameSurnameGenerator {

    private static Set<Character> vowelSet;
    private static final int NAME_SURNAME_CODE_LENGTH = 3;

    private TaxCodeNameSurnameGeneratorImpl(){
        loadVowelSet();
    }

    @Override
    public String calculateSurnameCode(String surname) {
        if (!validate(surname)) {
            throw new IllegalArgumentException("surname is null or blank");
        }

        var surnameSanitized = removeWhiteSpaceAndToUpperCase(surname);
        var consonants = extractConsonants(surnameSanitized);

        if (consonants.length() >= NAME_SURNAME_CODE_LENGTH) {
            return consonants.substring(0, NAME_SURNAME_CODE_LENGTH);
        }

        var surnameCode = new StringBuilder(consonants);
        fillNameCodeOrSurnameCodeWithVowelsOrX(surnameCode, surnameSanitized);

        return surnameCode.toString();
    }

    @Override
    public String calculateNameCode(String name) {
        if (!validate(name)) {
            throw new IllegalArgumentException("name is null or blank");
        }

        var nameSanitized = removeWhiteSpaceAndToUpperCase(name);
        var consonants = extractConsonants(nameSanitized);
        var nameCode = new StringBuilder();

        if (consonants.length() >= 4) {
            nameCode.append(consonants.charAt(0));
            nameCode.append(consonants.charAt(2));
            nameCode.append(consonants.charAt(3));
        } else {
            nameCode.append(consonants);

            if (consonants.length() < 3) {
                fillNameCodeOrSurnameCodeWithVowelsOrX(nameCode, nameSanitized);
            }
        }


        return nameCode.toString();
    }

    private void fillNameCodeOrSurnameCodeWithVowelsOrX(StringBuilder stringBuilder, String string) {
        var vowels = extractVowels(string);
        var numberOfCharactersToFill = NAME_SURNAME_CODE_LENGTH - stringBuilder.length();

        if (vowels.length() >= numberOfCharactersToFill) {
            var vowelsToAppendToSurnameCode = vowels.substring(0, numberOfCharactersToFill);
            stringBuilder.append(vowelsToAppendToSurnameCode);
        } else {
            stringBuilder.append(vowels);
            stringBuilder.append("X".repeat(Math.max(0, NAME_SURNAME_CODE_LENGTH - stringBuilder.length())));
        }
    }

    private String removeWhiteSpaceAndToUpperCase(String string) {
        return string.replace(" ", "").toUpperCase();
    }


    private String extractConsonants(String string) {
        var consonants = new StringBuilder();

        for (char character : string.toCharArray()) {
            if (characterIsACapitalLetter(character) && !characterIsACapitalVowel(character)) {
                consonants.append(character);
            }
        }

        return consonants.toString();
    }


    private String extractVowels(String string) {
        var vowels = new StringBuilder();

        for (char character : string.toCharArray()) {
            if (characterIsACapitalVowel(character)) {
                vowels.append(character);
            }
        }

        return vowels.toString();
    }

    private boolean characterIsACapitalLetter(char character) {
        return character >= CAPITAL_LETTER_A && character <= CAPITAL_LETTER_Z;
    }

    private boolean characterIsACapitalVowel(char character) {
        return vowelSet.contains(character);
    }

    private boolean validate(String string) {
        return string != null && !string.isBlank();
    }

    private static void loadVowelSet() {
        vowelSet = new HashSet<>();
        vowelSet.add('A');
        vowelSet.add('E');
        vowelSet.add('I');
        vowelSet.add('O');
        vowelSet.add('U');
    }
}
