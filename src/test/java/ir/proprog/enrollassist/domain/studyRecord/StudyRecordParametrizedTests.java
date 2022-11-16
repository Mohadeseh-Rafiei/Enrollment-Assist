package ir.proprog.enrollassist.domain.studyRecord;


import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Stream;

public class StudyRecordParametrizedTests {
    @ParameterizedTest
    @MethodSource("undergraduateInputGenerator")
    void isPassed_undergraduate(StudyRecord value, boolean expected) {
        assertEquals(value.isPassed(GraduateLevel.Undergraduate), expected);
    }

    @ParameterizedTest
    @MethodSource("masterInputGenerator")
    void isPassed_master(StudyRecord value, boolean expected) {
        assertEquals(value.isPassed(GraduateLevel.Masters), expected);
    }

    @ParameterizedTest
    @MethodSource("phdInputGenerator")
    void isPassed_phd(StudyRecord value, boolean expected) {
        assertEquals(value.isPassed(GraduateLevel.Masters), expected);
    }

    public static Stream<Arguments> undergraduateInputGenerator() throws Exception {
        return Stream.of(
                Arguments.of(new StudyRecord("12343", new Course("2222222", "AP", 3, "Undergraduate"), 10), true),
                Arguments.of(new StudyRecord("12343", new Course("2222222", "AP", 3, "Undergraduate"), 11), true),
                Arguments.of(new StudyRecord("12343", new Course("2222222", "AP", 3, "Undergraduate"), 5), false)
        );
    }

    public static Stream<Arguments> masterInputGenerator() throws Exception {
        return Stream.of(
                Arguments.of(new StudyRecord("12343", new Course("2222222", "AP", 3, "Masters"), 12), true),
                Arguments.of(new StudyRecord("12343", new Course("2222222", "AP", 3, "Masters"), 14), true),
                Arguments.of(new StudyRecord("12343", new Course("2222222", "AP", 3, "Masters"), 5), false)
        );
    }

    public static Stream<Arguments> phdInputGenerator() throws Exception {
        return Stream.of(
                Arguments.of(new StudyRecord("12343", new Course("2222222", "AP", 3, "PHD"), 14), true),
                Arguments.of(new StudyRecord("12343", new Course("2222222", "AP", 3, "PHD"), 16), true),
                Arguments.of(new StudyRecord("12343", new Course("2222222", "AP", 3, "PHD"), 5), false)
        );
    }
}
