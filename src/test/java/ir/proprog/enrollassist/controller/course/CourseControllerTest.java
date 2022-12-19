package ir.proprog.enrollassist.controller.course;

import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static CourseRepository courseRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void getAll_restApiTest() throws Exception {
        List<Course> courses = new ArrayList<Course>();

        Course math2 = new Course("1111111", "MATH_2", 3, "Undergraduate");
        courses.add(math2);

        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/courses", String.class))
                .contains("[{\"courseNumber\":{\"courseNumber\":\"1111111\"},\"graduateLevel\":\"Undergraduate\"," +
                        "\"courseTitle\":\"MATH_2\",\"courseCredits\":3,\"prerequisites\":[]}]");
    }

    @Test
    void addNewCourse_restApiTest() throws Exception {
        Course math1 = new Course("1111111", "MATH2", 3, "Undergraduate");
        Course math2 = new Course("6666666", "MATH2", 3, "Undergraduate").withPre(math1);

        Set<Long> pre = new HashSet<Long>();

        Mockito.when(courseRepository.findById(null)).thenReturn(Optional.ofNullable(math2));

        Set<Long> prog = new HashSet<Long>();



        assert math2 != null;
        CourseMajorView courseMajorView = new CourseMajorView(math2, pre, prog);


        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/courses", courseMajorView,  String.class))
                .contains("{\"courseNumber\":{\"courseNumber\":\"6666666\"},\"graduateLevel\":\"Undergraduate\"," +
                        "\"courseTitle\":\"MATH2\",\"courseCredits\":3,\"prerequisites\":[]}");
    }

    @Test
    void one_restApiTest() throws Exception {
        Course math1 = new Course("1111111", "MATH2", 3, "Undergraduate");
        Course math2 = new Course("6666666", "MATH2", 3, "Undergraduate").withPre(math1);

        Set<Long> pre = new HashSet<Long>();

        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.ofNullable(math2));

        Set<Long> prog = new HashSet<Long>();

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/courses/" + 1,  String.class))
                .contains("{\"courseNumber\":{\"courseNumber\":\"6666666\"},\"graduateLevel\":\"Undergraduate\"," +
                        "\"courseTitle\":\"MATH2\",\"courseCredits\":3,\"prerequisites\":[null]}");
    }
}
