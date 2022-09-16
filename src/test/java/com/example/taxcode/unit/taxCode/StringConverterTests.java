package com.example.taxcode.unit.taxCode;

import com.example.taxcode.application.taxcode.utils.StringConverterImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StringConverterTests {
    @InjectMocks
    private StringConverterImpl stringConverter;

    @ParameterizedTest()
    @MethodSource("com.example.taxcode.unit.taxCode.stream.StringConverterStreamData#stringConversionThrowException")
    @DisplayName("String conversion throw exception because doesn't match the regex")
    void stringConversionThrowException(String string) {
        assertThatThrownBy(() -> {
            stringConverter.convertStringInTaxCodeIntegerValues(string);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{index} => string={0}, values={1}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.StringConverterStreamData#stringConversion")
    @DisplayName("String conversion in integer values")
    void stringConversion(String string,int [] values) {
        var calculatedValues = stringConverter.convertStringInTaxCodeIntegerValues(string);

        assertEquals(values.length,calculatedValues.length);

        for(int i=0;i<calculatedValues.length;i++){
            assertEquals(values[i],calculatedValues[i]);
        }
    }
}
