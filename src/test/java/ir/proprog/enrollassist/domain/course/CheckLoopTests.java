package ir.proprog.enrollassist.domain.course;

import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.controller.course.CourseMajorView;
import ir.proprog.enrollassist.repository.CourseRepository;
import ir.proprog.enrollassist.repository.ProgramRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Stream;


// behavior verification
public class CheckLoopTests {
    @MockBean
    private static CourseRepository courseRepository = Mockito.mock(CourseRepository.class);

    @MockBean
    private static ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);

    public AddCourseService addCourseService = new AddCourseService(courseRepository, programRepository);

    @Test
    @DisplayName("add course-checkLoop test, with loop error")
    void addCourse_checkLoop_whenCourseTitleIsNull_withError() throws Exception {
        try{
            Course math2_1 = new Course("6666666", "MATH2", 3, "Undergraduate");
            Course math1 = new Course("1111111", "MATH2", 3, "Undergraduate");
            Course math2 = new Course("6666666", "MATH2", 3, "Undergraduate").withPre(math1, math2_1);

            Set<Long> pre = new HashSet<Long>();
            pre.add(Long.valueOf("66666666"));

            Mockito.when(courseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(math2));
            Set<Long> prog = new HashSet<Long>();


            CourseMajorView courseMajorView = new CourseMajorView(math2, pre, prog);
            addCourseService.addCourse(courseMajorView);
        } catch (ExceptionList errors) {
            Stream<Exception> error = errors.getExceptions().stream().filter(
                    e -> Objects.equals(e.getMessage(), "MATH2 has made a loop in prerequisites.")
            );
            if(error.toList().size() == 0) {
                throw new Exception();
            }
        }
    }

    @Test
    @DisplayName("add course-checkLoop test, with loop error")
    void addCourse_checkLoop_whenCourseTitleIsNull_withSuccess() throws Exception {
        try{
            Course math1 = new Course("1111111", "MATH2", 3, "Undergraduate");
            Course math2 = new Course("6666666", "MATH2", 3, "Undergraduate").withPre(math1);

            Set<Long> pre = new HashSet<Long>();

            Mockito.when(courseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(math2));
            Set<Long> prog = new HashSet<Long>();


            CourseMajorView courseMajorView = new CourseMajorView(math2, pre, prog);
            addCourseService.addCourse(courseMajorView);
        } catch (ExceptionList errors) {
            Stream<Exception> error = errors.getExceptions().stream().filter(
                    e -> Objects.equals(e.getMessage(), "MATH2 has made a loop in prerequisites.")
            );
            if(error.toList().size() != 0) {
                throw new Exception();
            }
        }
    }
}
