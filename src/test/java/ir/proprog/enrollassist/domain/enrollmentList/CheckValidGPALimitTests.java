package ir.proprog.enrollassist.domain.enrollmentList;

import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.EnrollmentRules.EnrollmentRuleViolation;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.major.Major;
import ir.proprog.enrollassist.domain.program.Program;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.domain.studyRecord.StudyRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckValidGPALimitTests {
    @Test
    void checkValidGPALimit_minimumNumberOfCreditsIsNotMet_12() throws Exception {
        Student student = new Student("810198402", "Undergraduate");

        Course prog = new Course("7777777", "PROG", 4, "Undergraduate");
        Course economy = new Course("1111111", "ECO", 3, "Undergraduate");


        Section sectionMath2 = new Section(prog, "3", new ExamTime("2021-11-23T14:00", "2021-11-23T18:00"), Collections.singleton(new PresentationSchedule("Monday", "08:00", "10:00")));
        Section sectionEconomy = new Section(economy, "7", new ExamTime("2021-11-27T08:00", "2021-11-27T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:00", "11:00")));

        EnrollmentList correctEnrollmentList = new EnrollmentList("EnrollmentListTest", student);
        correctEnrollmentList.addSections(sectionEconomy, sectionMath2);

        List<EnrollmentRuleViolation> violations = correctEnrollmentList.checkValidGPALimit();
        assertEquals(1, violations.size());
        assertEquals("Minimum number of credits(12) is not met.", violations.get(0).toString());
    }


    @Test
    void checkValidGPALimit_whenGPAIs0AndNumberOfCreditsIsGreaterThan20AndTotalTakenCreditsIs0_maximumNumberOfCreditsExceeded() throws Exception {
        Student student = new Student("810198402", "Undergraduate");

        Course math1 = new Course("4444444", "MATH1", 3, "Undergraduate");
        Course phys1 = new Course("8888888", "PHYS1", 3, "Undergraduate");
        Course prog = new Course("7777777", "PROG", 4, "Undergraduate");
        Course phys2 = new Course("9999999", "PHYS2", 3, "Undergraduate").withPre(math1, phys1);
        Course ap = new Course("2222222", "AP", 3, "Undergraduate").withPre(prog);
        Course dm = new Course("3333333", "DM", 3, "Undergraduate").withPre(math1);
        Course economy = new Course("1111111", "ECO", 3, "Undergraduate");
        Course farsi = new Course("1212121", "FA", 2, "Undergraduate");
        Course akhlagh = new Course("1111110", "AKHLAGH", 2, "Undergraduate");


        Section sectionPhys1 = new Section(phys1, "1", new ExamTime("2021-11-21T08:00", "2021-11-21T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "08:00", "10:00")));
        Section sectionProg = new Section(farsi, "2", new ExamTime("2021-11-22T08:00", "2021-11-22T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "07:00", "08:00")));
        Section sectionMath2 = new Section(prog, "3", new ExamTime("2021-11-23T14:00", "2021-11-23T18:00"), Collections.singleton(new PresentationSchedule("Monday", "08:00", "10:00")));
        Section sectionPhys2 = new Section(phys2, "4", new ExamTime("2021-11-24T08:00", "2021-11-24T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionAp = new Section(ap, "5", new ExamTime("2021-11-25T08:00", "2021-11-25T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionDm = new Section(dm, "6", new ExamTime("2021-11-26T14:00", "2021-11-26T18:00"), Collections.singleton(new PresentationSchedule("Tuesday", "08:00", "10:00")));
        Section sectionEconomy = new Section(economy, "7", new ExamTime("2021-11-27T08:00", "2021-11-27T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:00", "11:00")));
        Section sectionAkhlagh = new Section(akhlagh, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));

        EnrollmentList correctEnrollmentList = new EnrollmentList("EnrollmentListTest", student);
        correctEnrollmentList.addSections(sectionAkhlagh, sectionDm, sectionEconomy, sectionMath2, sectionAp, sectionPhys2, sectionProg, sectionPhys1);

        List<EnrollmentRuleViolation> violations = correctEnrollmentList.checkValidGPALimit();
        assertEquals("Maximum number of credits(20) exceeded.", violations.get(0).toString());
        assertEquals(1, violations.size());
    }

    @Test
    void checkValidGPALimit_whenGPAIsLessThan12AndNumberOfCreditsIsGreaterThan14_maximumNumberOfCreditsExceeded() throws Exception {
        Student student = new Student("810198402", "Undergraduate");

        Course math1 = new Course("4444444", "MATH1", 3, "Undergraduate");
        Course phys1 = new Course("8888888", "PHYS1", 3, "Undergraduate");
        Course prog = new Course("7777777", "PROG", 4, "Undergraduate");
        Course math2 = new Course("6666666", "MATH2", 3, "Undergraduate").withPre(math1);
        Course phys2 = new Course("9999999", "PHYS2", 3, "Undergraduate").withPre(math1, phys1);
        Course ap = new Course("2222222", "AP", 3, "Undergraduate").withPre(prog);
        Course dm = new Course("3333333", "DM", 3, "Undergraduate").withPre(math1);
        Course economy = new Course("1111111", "ECO", 3, "Undergraduate");
        Course akhlagh = new Course("1111110", "AKHLAGH", 2, "Undergraduate");

        List<StudyRecord> studyRecords = new ArrayList<>();
        studyRecords.add(new StudyRecord("12341", math1, 11));

        for (StudyRecord studyRecord : studyRecords) {
            student.setGrade(studyRecord.getTerm(), studyRecord.getCourse(), studyRecord.getGrade().getGrade());
        }

        Section sectionMath2 = new Section(math2, "3", new ExamTime("2021-11-23T14:00", "2021-11-23T18:00"), Collections.singleton(new PresentationSchedule("Monday", "08:00", "10:00")));
        Section sectionPhys2 = new Section(phys2, "4", new ExamTime("2021-11-24T08:00", "2021-11-24T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionAp = new Section(ap, "5", new ExamTime("2021-11-25T08:00", "2021-11-25T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionDm = new Section(dm, "6", new ExamTime("2021-11-26T14:00", "2021-11-26T18:00"), Collections.singleton(new PresentationSchedule("Tuesday", "08:00", "10:00")));
        Section sectionEconomy = new Section(economy, "7", new ExamTime("2021-11-27T08:00", "2021-11-27T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:00", "11:00")));
        Section sectionAkhlagh = new Section(akhlagh, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));


        EnrollmentList correctEnrollmentList = new EnrollmentList("EnrollmentListTest", student);
        correctEnrollmentList.addSections(sectionAkhlagh, sectionDm, sectionEconomy, sectionMath2, sectionPhys2, sectionAp);
        List<EnrollmentRuleViolation> violations = correctEnrollmentList.checkValidGPALimit();
        assertEquals(1, violations.size());
        assertEquals("Maximum number of credits(14) exceeded.", violations.get(0).toString());
    }

    @Test
    void checkValidGPALimit_whenGPAIsLessThan17AndNumberOfCreditsIsGreaterThan20_maximumNumberOfCreditsExceeded() throws Exception {
        Student student = new Student("810198402", "Undergraduate");

        Course math1 = new Course("4444444", "MATH1", 3, "Undergraduate");
        Course phys1 = new Course("8888888", "PHYS1", 3, "Undergraduate");
        Course prog = new Course("7777777", "PROG", 4, "Undergraduate");
        Course phys2 = new Course("9999999", "PHYS2", 3, "Undergraduate").withPre(math1, phys1);
        Course ap = new Course("2222222", "AP", 3, "Undergraduate").withPre(prog);
        Course dm = new Course("3333333", "DM", 3, "Undergraduate").withPre(math1);
        Course economy = new Course("1111111", "ECO", 3, "Undergraduate");
        Course farsi = new Course("1212121", "FA", 2, "Undergraduate");
        Course akhlagh = new Course("1111110", "AKHLAGH", 2, "Undergraduate");


        Section sectionPhys1 = new Section(phys1, "1", new ExamTime("2021-11-21T08:00", "2021-11-21T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "08:00", "10:00")));
        Section sectionProg = new Section(farsi, "2", new ExamTime("2021-11-22T08:00", "2021-11-22T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "07:00", "08:00")));
        Section sectionMath2 = new Section(prog, "3", new ExamTime("2021-11-23T14:00", "2021-11-23T18:00"), Collections.singleton(new PresentationSchedule("Monday", "08:00", "10:00")));
        Section sectionPhys2 = new Section(phys2, "4", new ExamTime("2021-11-24T08:00", "2021-11-24T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionAp = new Section(ap, "5", new ExamTime("2021-11-25T08:00", "2021-11-25T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionDm = new Section(dm, "6", new ExamTime("2021-11-26T14:00", "2021-11-26T18:00"), Collections.singleton(new PresentationSchedule("Tuesday", "08:00", "10:00")));
        Section sectionEconomy = new Section(economy, "7", new ExamTime("2021-11-27T08:00", "2021-11-27T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:00", "11:00")));
        Section sectionAkhlagh = new Section(akhlagh, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));

        List<StudyRecord> studyRecords = new ArrayList<>();
        studyRecords.add(new StudyRecord("12341", math1, 16));

        for (StudyRecord studyRecord : studyRecords) {
            student.setGrade(studyRecord.getTerm(), studyRecord.getCourse(), studyRecord.getGrade().getGrade());
        }


        EnrollmentList correctEnrollmentList = new EnrollmentList("EnrollmentListTest", student);
        correctEnrollmentList.addSections(sectionAkhlagh, sectionDm, sectionEconomy, sectionMath2, sectionAp, sectionPhys2, sectionProg, sectionPhys1);

        List<EnrollmentRuleViolation> violations = correctEnrollmentList.checkValidGPALimit();
        assertEquals(1, violations.size());
        assertEquals("Maximum number of credits(20) exceeded.", violations.get(0).toString());
    }

    @Test
    void checkValidGPALimit_whenNumberOfCreditsIsGreaterThan24_maximumNumberOfCreditsExceeded() throws Exception {
        Student student = new Student("810198402", "Undergraduate");

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

        Major ce = new Major("8101", "CE", "Engineering");

        Program ceProgram = new Program(ce, "Undergraduate", 100, 100, "Major");
        ceProgram.addCourse(math1, math2, phys1, phys2, prog, ap, dm, economy, maaref, farsi, english, akhlagh, karafarini);

        student.addProgram(ceProgram);

        List<StudyRecord> studyRecords = new ArrayList<>();
        studyRecords.add(new StudyRecord("12341", math1, 20));

        for (StudyRecord studyRecord : studyRecords) {
            student.setGrade(studyRecord.getTerm(), studyRecord.getCourse(), studyRecord.getGrade().getGrade());
        }

        Section sectionMath1 = new Section(math1, "0", new ExamTime("2021-11-20T08:00", "2021-11-20T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "12:00", "14:00")));
        Section sectionPhys1 = new Section(phys1, "1", new ExamTime("2021-11-21T08:00", "2021-11-21T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "08:00", "10:00")));
        Section sectionProg = new Section(farsi, "2", new ExamTime("2021-11-22T08:00", "2021-11-22T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "07:00", "08:00")));
        Section sectionMath2 = new Section(prog, "3", new ExamTime("2021-11-23T14:00", "2021-11-23T18:00"), Collections.singleton(new PresentationSchedule("Monday", "08:00", "10:00")));
        Section sectionPhys2 = new Section(phys2, "4", new ExamTime("2021-11-24T08:00", "2021-11-24T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionAp = new Section(ap, "5", new ExamTime("2021-11-25T08:00", "2021-11-25T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionDm = new Section(dm, "6", new ExamTime("2021-11-26T14:00", "2021-11-26T18:00"), Collections.singleton(new PresentationSchedule("Tuesday", "08:00", "10:00")));
        Section sectionEconomy = new Section(economy, "7", new ExamTime("2021-11-27T08:00", "2021-11-27T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:00", "11:00")));
        Section sectionMaaref = new Section(maaref, "8", new ExamTime("2021-11-28T08:00", "2021-11-28T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:20", "13:00")));
        Section sectionFarsi = new Section(farsi, "8", new ExamTime("2021-11-29T08:00", "2021-11-29T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));
        Section sectionEnglish = new Section(english, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Wednesday", "15:00", "17:00")));
        Section sectionAkhlagh = new Section(akhlagh, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));
        Section sectionKarafarini = new Section(karafarini, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));


        EnrollmentList correctEnrollmentList = new EnrollmentList("EnrollmentListTest", student);
        correctEnrollmentList.addSections(sectionAkhlagh, sectionDm, sectionEconomy, sectionMath2, sectionEnglish, sectionFarsi, sectionMaaref,
                sectionKarafarini, sectionAp, sectionPhys2, sectionProg, sectionPhys1, sectionMath1);

        List<EnrollmentRuleViolation> violations = correctEnrollmentList.checkValidGPALimit();

        assertEquals(violations.size(), 1);
        assertEquals("Maximum number of credits(24) exceeded.", violations.get(0).toString());
    }

    @Test
    void checkValidGPALimit_NoViolation() throws Exception {
        Student student = new Student("810198402", "Undergraduate");

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

        Major ce = new Major("8101", "CE", "Engineering");

        Program ceProgram = new Program(ce, "Undergraduate", 100, 100, "Major");
        ceProgram.addCourse(math1, math2, phys1, phys2, prog, ap, dm, economy, maaref, farsi, english, akhlagh, karafarini);

        student.addProgram(ceProgram);

        List<StudyRecord> studyRecords = new ArrayList<>();
        studyRecords.add(new StudyRecord("12341", math1, 20));

        for (StudyRecord studyRecord : studyRecords) {
            student.setGrade(studyRecord.getTerm(), studyRecord.getCourse(), studyRecord.getGrade().getGrade());
        }

        Section sectionMath1 = new Section(math1, "0", new ExamTime("2021-11-20T08:00", "2021-11-20T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "12:00", "14:00")));
        Section sectionPhys1 = new Section(phys1, "1", new ExamTime("2021-11-21T08:00", "2021-11-21T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "08:00", "10:00")));
        Section sectionProg = new Section(farsi, "2", new ExamTime("2021-11-22T08:00", "2021-11-22T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "07:00", "08:00")));
        Section sectionMath2 = new Section(prog, "3", new ExamTime("2021-11-23T14:00", "2021-11-23T18:00"), Collections.singleton(new PresentationSchedule("Monday", "08:00", "10:00")));
        Section sectionPhys2 = new Section(phys2, "4", new ExamTime("2021-11-24T08:00", "2021-11-24T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionAp = new Section(ap, "5", new ExamTime("2021-11-25T08:00", "2021-11-25T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionDm = new Section(dm, "6", new ExamTime("2021-11-26T14:00", "2021-11-26T18:00"), Collections.singleton(new PresentationSchedule("Tuesday", "08:00", "10:00")));
        Section sectionEconomy = new Section(economy, "7", new ExamTime("2021-11-27T08:00", "2021-11-27T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:00", "11:00")));
        Section sectionMaaref = new Section(maaref, "8", new ExamTime("2021-11-28T08:00", "2021-11-28T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:20", "13:00")));
        Section sectionFarsi = new Section(farsi, "8", new ExamTime("2021-11-29T08:00", "2021-11-29T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));
        Section sectionEnglish = new Section(english, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Wednesday", "15:00", "17:00")));
        Section sectionAkhlagh = new Section(akhlagh, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));
        Section sectionKarafarini = new Section(karafarini, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));


        EnrollmentList correctEnrollmentList = new EnrollmentList("EnrollmentListTest", student);
        correctEnrollmentList.addSections(sectionAkhlagh, sectionDm, sectionEconomy, sectionMath2, sectionEnglish, sectionFarsi);

        List<EnrollmentRuleViolation> violations = correctEnrollmentList.checkValidGPALimit();

        assertEquals(violations.size(), 0);
    }

    @Test
    void checkValidGPALimit_whenGraduateLevelIsMasters_minimumNumberOfCreditsIsNotMet12() throws Exception {
        Student student = new Student("810198402", GraduateLevel.Undergraduate.toString());

        Course prog = new Course("7777777", "PROG", 4, "Undergraduate");
        Course economy = new Course("1111111", "ECO", 3, "Undergraduate");


        Section sectionMath2 = new Section(prog, "3", new ExamTime("2021-11-23T14:00", "2021-11-23T18:00"), Collections.singleton(new PresentationSchedule("Monday", "08:00", "10:00")));
        Section sectionEconomy = new Section(economy, "7", new ExamTime("2021-11-27T08:00", "2021-11-27T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:00", "11:00")));

        EnrollmentList correctEnrollmentList = new EnrollmentList("EnrollmentListTest", student);
        correctEnrollmentList.addSections(sectionEconomy, sectionMath2);

        List<EnrollmentRuleViolation> violations = correctEnrollmentList.checkValidGPALimit();
        assertEquals(1, violations.size());
        assertEquals("Minimum number of credits(12) is not met.", violations.get(0).toString());
    }

    @Test
    void checkValidGPALimit_whenGraduateLevelIsMasterAndNumberOfCreditsIsGreaterThan24_maximumNumberOfCreditsExceeded() throws Exception {
        Student student = new Student("810198402", GraduateLevel.Masters.toString());

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

        Section sectionMath1 = new Section(math1, "0", new ExamTime("2021-11-20T08:00", "2021-11-20T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "12:00", "14:00")));
        Section sectionPhys1 = new Section(phys1, "1", new ExamTime("2021-11-21T08:00", "2021-11-21T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "08:00", "10:00")));
        Section sectionProg = new Section(farsi, "2", new ExamTime("2021-11-22T08:00", "2021-11-22T10:00"), Collections.singleton(new PresentationSchedule("Sunday", "07:00", "08:00")));
        Section sectionMath2 = new Section(prog, "3", new ExamTime("2021-11-23T14:00", "2021-11-23T18:00"), Collections.singleton(new PresentationSchedule("Monday", "08:00", "10:00")));
        Section sectionPhys2 = new Section(phys2, "4", new ExamTime("2021-11-24T08:00", "2021-11-24T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionAp = new Section(ap, "5", new ExamTime("2021-11-25T08:00", "2021-11-25T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "08:00", "10:00")));
        Section sectionDm = new Section(dm, "6", new ExamTime("2021-11-26T14:00", "2021-11-26T18:00"), Collections.singleton(new PresentationSchedule("Tuesday", "08:00", "10:00")));
        Section sectionEconomy = new Section(economy, "7", new ExamTime("2021-11-27T08:00", "2021-11-27T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:00", "11:00")));
        Section sectionMaaref = new Section(maaref, "8", new ExamTime("2021-11-28T08:00", "2021-11-28T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "10:20", "13:00")));
        Section sectionFarsi = new Section(farsi, "8", new ExamTime("2021-11-29T08:00", "2021-11-29T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));
        Section sectionEnglish = new Section(english, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Wednesday", "15:00", "17:00")));
        Section sectionAkhlagh = new Section(akhlagh, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));
        Section sectionKarafarini = new Section(karafarini, "8", new ExamTime("2021-11-30T08:00", "2021-11-30T11:00"), Collections.singleton(new PresentationSchedule("Saturday", "15:00", "17:00")));


        EnrollmentList correctEnrollmentList = new EnrollmentList("EnrollmentListTest", student);
        correctEnrollmentList.addSections(sectionAkhlagh, sectionDm, sectionEconomy, sectionMath2, sectionEnglish, sectionFarsi, sectionMaaref,
                sectionKarafarini, sectionAp, sectionPhys2, sectionProg, sectionPhys1, sectionMath1);

        List<EnrollmentRuleViolation> violations = correctEnrollmentList.checkValidGPALimit();

        assertEquals(violations.size(), 1);
        assertEquals("Maximum number of credits(12) exceeded.", violations.get(0).toString());
    }
}
