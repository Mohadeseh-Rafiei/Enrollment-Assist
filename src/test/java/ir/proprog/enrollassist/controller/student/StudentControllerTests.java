package ir.proprog.enrollassist.controller.student;

import ir.proprog.enrollassist.controller.section.SectionView;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.major.Major;
import ir.proprog.enrollassist.domain.program.Program;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.domain.student.StudentNumber;
import ir.proprog.enrollassist.domain.user.User;
import ir.proprog.enrollassist.repository.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

import java.util.*;


public class StudentControllerTests {

    private static final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
    @MockBean
    private static CourseRepository courseRepository = Mockito.mock(CourseRepository.class);

    @MockBean
    private static SectionRepository sectionRepository = Mockito.mock(SectionRepository.class);

    @MockBean
    private static EnrollmentListRepository enrollmentListRepository = Mockito.mock(EnrollmentListRepository.class);

    @MockBean
    private static UserRepository userRepository = Mockito.mock(UserRepository.class);

   private static StudentController studentController = new StudentController(studentRepository, courseRepository, sectionRepository, enrollmentListRepository, userRepository);


    @BeforeEach
    void init() {
//        MockitoAnnotations.initMocks(this);
        reset(studentRepository);
        reset(sectionRepository);
    }

    @AfterAll
    static void tearDown() {
        studentController = null;
    }

    @Test
    @DisplayName("get all students, with success")
    public void getAll_withSuccess_getAllUsers() throws Exception {
        String studentNumber = "810199999";

//        Dummy
        String graduateLevel = "Undergraduate";

//        stub
        StudentRepositoryStub repository = new StudentRepositoryStub();

        when(studentRepository.findAll()).thenReturn(repository.findAll());

        Student student = new Student(studentNumber, graduateLevel);
        repository.save(student);
        Iterable<StudentView> studentViews = studentController.all();

//        State   verification
        assertEquals(1, studentViews.spliterator().estimateSize());

//        Behavior verification
        verify(studentRepository).findAll();
    }


    @Test
    @DisplayName("get one specific students, with success")
    public void one_withSuccess_getSpecificUser() throws Exception {
        String studentNumber = "810199999";

//        Dummy
        String graduateLevel = "Undergraduate";

        Student student = new Student(studentNumber, graduateLevel);

//        Stub
        StudentRepositoryStub repository = new StudentRepositoryStub();
        repository.save(student);
        when(studentRepository.findByStudentNumber(new StudentNumber(studentNumber))).thenReturn(repository.findByStudentNumber(new StudentNumber(studentNumber)));
        StudentView studentView = studentController.one(studentNumber);

//        State verification
        assertEquals(student.getStudentNumber(), studentView.getStudentNo());

//        Behavior Verification
        verify(studentRepository).findByStudentNumber(new StudentNumber(studentNumber));
    }


    @Test
    @DisplayName("add student, with success")
    public void addStudent_withSuccess_addNewStudent() throws Exception {
        String studentNumber = "810199999";
        String userId = "124";

//        Dummy
        String graduateLevel = "Undergraduate";
        String name = "mohadese";

        Student student = new Student(studentNumber, graduateLevel);
        User user = new User(name, userId);

        StudentRepositoryStub sRepository = new StudentRepositoryStub();

        when(studentRepository.save(student)).thenReturn(sRepository.save(student));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        StudentView studentView = new StudentView(student);
        studentView.setUserId(user.getUserId());
        StudentView addedStudentView = studentController.addStudent(studentView);

//        State verification
        assertEquals(new StudentNumber(studentNumber) ,addedStudentView.getStudentNo());

//        Behavior verification
        verify(userRepository).findByUserId(userId);
    }

// State Verification
    @Test
    @DisplayName("get all students, with User with id: 123 was not found error")
    public void addStudent_withError_addNewStudent() throws Exception {
        String studentNumber = "810199999";
        String userId = "123";

//        Dummy
        String graduateLevel = "Undergraduate";
        String name = "mohadese";

        try {
            Student student = new Student(studentNumber, graduateLevel);
            User user = new User(name, userId);

            StudentRepositoryStub sRepository = new StudentRepositoryStub();

            when(studentRepository.save(student)).thenReturn(sRepository.save(student));

            StudentView studentView = new StudentView(student);
            studentView.setUserId(user.getUserId());

            studentController.addStudent(studentView);
        } catch (ResponseStatusException error) {
            if(!(Objects.equals(error.getReason(), "User with id: 123 was not found."))){
                throw new Exception();
            }
        }

    }

//    Behavior verification
    @Test
    @DisplayName("find takeable sections my major, with success")
    public void findTakeableSectionsByMajor_withSuccess_findTakeableSectionForStudent() throws Exception {
//        Dummies
        Course math1 = new Course("4444444", "MATH1", 3, "Undergraduate");
        Course phys1 = new Course("8888888", "PHYS1", 3, "Undergraduate");
        Course prog = new Course("7777777", "PROG", 4, "Undergraduate");
        Course math2 = new Course("6666666", "MATH2", 3, "Undergraduate").withPre(math1);
        Course phys2 = new Course("9999999", "PHYS2", 3, "Undergraduate").withPre(math1, phys1);
        Course ap = new Course("2222222", "AP", 3, "Undergraduate").withPre(prog);
        Course dm = new Course("3333333", "DM", 3, "Undergraduate").withPre(math1);
        Course economy = new Course("1111111", "ECO", 3, "Undergraduate");
        Course maaref = new Course("5555555", "MAAREF", 2, "Undergraduate");
        Course farsi = new Course("1212121", "FA", 2, "Undergraduate");
        Course english = new Course("1010101", "EN", 2, "Undergraduate");
        Course akhlagh = new Course("1111110", "AKHLAGH", 2, "Undergraduate");
        Course karafarini = new Course("1313131", "KAR", 3, "Undergraduate");
        courseRepository.saveAll(List.of(math1, phys1, prog, math2, phys2, ap, dm, economy, maaref, farsi, english, akhlagh, karafarini));

        Major ce = new Major("8101", "CE", "Engineering");

        Program ceProgram = new Program(ce, "Undergraduate", 140, 140, "Major");
        ceProgram.addCourse(math1, math2, phys1, phys2);


        Student mahsa = new Student("810199999", "Undergraduate")
                .setGrade("11112", math1, 10)
                .setGrade("11112", phys1, 12)
                .setGrade("11112", prog, 16.3)
                .setGrade("11112", farsi, 18.5)
                .setGrade("11112", akhlagh, 15);
        mahsa.addProgram(ceProgram);

        ExamTime exam0 = new ExamTime("2021-07-10T09:00", "2021-07-10T11:00");
        ExamTime exam1 = new ExamTime("2021-07-11T09:00", "2021-07-11T11:00");
        ExamTime exam2 = new ExamTime("2021-07-12T09:00", "2021-07-12T11:00");
        ExamTime exam3 = new ExamTime("2021-07-13T09:00", "2021-07-13T11:00");
        ExamTime exam4 = new ExamTime("2021-07-14T09:00", "2021-07-14T11:00");
        ExamTime exam5 = new ExamTime("2021-07-15T09:00", "2021-07-15T11:00");
        ExamTime exam6 = new ExamTime("2021-07-16T09:00", "2021-07-16T11:00");
        ExamTime exam7 = new ExamTime("2021-07-17T09:00", "2021-07-17T11:00");

        Section math1_1 = new Section(math1, "01", exam0, Collections.emptySet()); sectionRepository.save(math1_1);
        Section phys1_1 = new Section(phys1, "01", exam1, Collections.emptySet()); sectionRepository.save(phys1_1);
        Section math2_1 = new Section(math2, "01", exam2, Collections.emptySet()); sectionRepository.save(math2_1);
        Section math2_2 = new Section(math2, "02", exam3, Collections.emptySet()); sectionRepository.save(math2_2);
        Section phys2_1 = new Section(phys2, "01", exam4, Collections.emptySet()); sectionRepository.save(phys2_1);
        Section phys2_2 = new Section(phys2, "02", exam5, Collections.emptySet()); sectionRepository.save(phys2_2);
        Section ap_1 = new Section(ap, "01", exam6, Collections.emptySet()); sectionRepository.save(ap_1);
        Section dm_1 = new Section(dm, "01", exam7, Collections.emptySet()); sectionRepository.save(dm_1);
        Section akhlagh_1 = new Section(akhlagh, "01" ,exam0, Collections.emptySet()); sectionRepository.save(akhlagh_1);
        Section english_1 = new Section(english, "01", exam1, Collections.emptySet()); sectionRepository.save(english_1);


        Section[] sections = {math1_1, phys1_1, math2_1, math2_2, phys2_1, phys2_2, ap_1, dm_1, akhlagh_1, english_1};
        Iterable<Section> allSections = Arrays.asList(sections);

//        Mock
        Mockito.when(sectionRepository.findAll()).thenReturn(allSections);
        Mockito.when(studentRepository.findByStudentNumber(new StudentNumber("810199999"))).thenReturn(Optional.of(mahsa));

        Iterable<SectionView> sectionViews = studentController.findTakeableSectionsByMajor("810199999");
        assertEquals(4, sectionViews.spliterator().estimateSize());

        verify(sectionRepository).findAll();
    }
}
