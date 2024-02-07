package org.scoreboard;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUtils {
    private TestUtils() {
    }

    /**
     * Asserts throwing exception for all three cases that first, second or both arguments of a particular function
     * have the specified testing value.
     *
     * @param expectedType expected exception type
     * @param function tested function
     * @param testingValue tested value
     */
    public static void assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(Class<? extends Throwable> expectedType,
                                                                       BiConsumer<String, String> function, String testingValue) {
        assertThrows(expectedType, () -> function.accept(testingValue, "other"));
        assertThrows(expectedType, () -> function.accept("other", testingValue));
        assertThrows(expectedType, () -> function.accept(testingValue, testingValue));
    }
}
