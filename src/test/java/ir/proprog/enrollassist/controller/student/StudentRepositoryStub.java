package ir.proprog.enrollassist.controller.student;

import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.domain.student.StudentNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

// stub
public class StudentRepositoryStub {
    private final List<Student> students = new ArrayList<Student>();

    public Student save(Student student) throws ExceptionList {
        this.students.add(student);
        return student;
    }

    public Iterable<Student> findAll() {
        return this.students;
    }

    public Optional<Student> findByStudentNumber(StudentNumber studentNumber) {
        Stream<Student> fetchedStudent = this.students.stream().filter(student -> studentNumber.equals(student.getStudentNumber()));
        return fetchedStudent.findAny();
    }

}
