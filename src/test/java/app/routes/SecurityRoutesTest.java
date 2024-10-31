package app.routes;

import app.PopulatorTestUtil;
import app.config.AppConfig;
import app.config.HibernateConfig;
import app.controllers.ISecurityController;
import app.controllers.SecurityController;
import app.dtos.UserDTO;
import app.entities.Role;
import app.entities.User;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class SecurityRoutesTest {

    private static PopulatorTestUtil populatorTestUtil;
    private static ISecurityController securityController;
    private static Javalin app;

    @BeforeAll
    static void beforeAll() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
        int port = 7070;

        populatorTestUtil = new PopulatorTestUtil(emf);
        securityController = SecurityController.getInstance(emf);
        app = AppConfig.startServer(port, emf);

        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api/v1";
        RestAssured.port = port;
    }

    @BeforeEach
    void setUp() {
        List<Role> roles = populatorTestUtil.createRoles();
        populatorTestUtil.persist(roles);

        List<User> users = populatorTestUtil.createUsers(roles);
        populatorTestUtil.persist(users);
    }

    @AfterEach
    void tearDown() {
        populatorTestUtil.cleanup(User.class);
        populatorTestUtil.cleanup(Role.class);
    }

    @AfterAll
    static void afterAll() {
        AppConfig.stopServer(app);
    }

    @Test
    void test() {
        given()
                .when()
                .get("/auth/test")
                .then()
                .statusCode(200)
                .body("message", is("Hello from open."));
    }

    @Test
    void login() {
        UserDTO userDTO = new UserDTO("User1", "1234");

        given()
                .body(userDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("username", is(userDTO.getUsername()))
                .body("token", is(notNullValue()));
    }

    @Test
    void register() {
        UserDTO userDTO = new UserDTO("User3", "1234");

        given()
                .body(userDTO)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201)
                .body("username", is(userDTO.getUsername()))
                .body("token", is(notNullValue()));
    }

    @Test
    void userDemo() {
        UserDTO userDTO = new UserDTO("User1", "1234", Set.of("user"));
        String token = securityController.createToken(userDTO);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/protected/user_demo")
                .then()
                .statusCode(200)
                .body("message", is("Hello from user protected."));
    }

    @Test
    void adminDemo() {
        UserDTO userDTO = new UserDTO("Admin1", "1234", Set.of("admin"));
        String token = securityController.createToken(userDTO);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/protected/admin_demo")
                .then()
                .statusCode(200)
                .body("message", is("Hello from admin protected."));
    }
}