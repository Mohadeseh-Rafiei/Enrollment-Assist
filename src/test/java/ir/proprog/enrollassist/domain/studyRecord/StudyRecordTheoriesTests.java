package ir.proprog.enrollassist.domain.studyRecord;

import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.course.Course;

import ir.proprog.enrollassist.domain.GraduateLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class StudyRecordTheoriesTests {
    @ParameterizedTest
    @ValueSource(doubles = {5, 1, 0, 9})
    void isPassed_false_undergraduateCourseGradeIsLessThanTen(double grade) throws ExceptionList {
        StudyRecord studyRecord = new StudyRecord("12343", new Course("2222222", "AP", 3, "Undergraduate"), grade);
        assertFalse(studyRecord.isPassed(GraduateLevel.Undergraduate));
    }

    @ParameterizedTest
    @ValueSource(doubles = {10, 11, 19, 20})
    void isPassed_true_undergraduateCourseGradeIsGreaterThanTen(double grade) throws ExceptionList {
        StudyRecord studyRecord = new StudyRecord("12343", new Course("2222222", "AP", 3, "Undergraduate"), grade);
        assertTrue(studyRecord.isPassed(GraduateLevel.Undergraduate));
    }

    @ParameterizedTest
    @ValueSource(doubles = {5, 1, 0, 9, 10, 11})
    void isPassed_false_mastersCourseGradeIsLessTwelve(double grade) throws ExceptionList {
        StudyRecord studyRecord = new StudyRecord("12343", new Course("2222222", "AP", 3, "Masters"), grade);
        assertFalse(studyRecord.isPassed(GraduateLevel.Masters));
    }

    @ParameterizedTest
    @ValueSource(doubles = {12, 19, 20})
    void isPassed_true_mastersCourseGradeIsGreaterThanTwelve(double grade) throws ExceptionList {
        StudyRecord studyRecord = new StudyRecord("12343", new Course("2222222", "AP", 3, "Masters"), grade);
        assertTrue(studyRecord.isPassed(GraduateLevel.Masters));
    }

    @ParameterizedTest
    @ValueSource(doubles = {5, 1, 0, 9, 10, 13})
    void isPassed_false_phdCourseGradeIsLessThanFourteen(double grade) throws ExceptionList {
        StudyRecord studyRecord = new StudyRecord("12343", new Course("2222222", "AP", 3, "PHD"), grade);
        assertFalse(studyRecord.isPassed(GraduateLevel.PHD));
    }

    @ParameterizedTest
    @ValueSource(doubles = {14, 16, 19, 20})
    void isPassed_true_phdCourseGradeIsGreaterThanFourteen(double grade) throws ExceptionList {
        StudyRecord studyRecord = new StudyRecord("12343", new Course("2222222", "AP", 3, "PHD"), grade);
        assertTrue(studyRecord.isPassed(GraduateLevel.PHD));
    }

}


