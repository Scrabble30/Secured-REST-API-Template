package app.daos;

import app.PopulatorTestUtil;
import app.config.HibernateConfig;
import app.entities.Role;
import app.entities.User;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class SecurityDAOTest {

    private static PopulatorTestUtil populatorTestUtil;
    private static ISecurityDAO securityDAO;

    @BeforeAll
    static void beforeAll() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();

        populatorTestUtil = new PopulatorTestUtil(emf);
        securityDAO = SecurityDAO.getInstance(emf);
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

    @Test
    void createUser() {
        String username = "User3";
        String password = "1234";

        User actual = securityDAO.createUser(username, password);

        assertThat(actual.getUsername(), is(username));
    }

    @Test
    void getVerifiedUser() {
        String username = "User1";
        String password = "1234";

        User actual = securityDAO.getVerifiedUser(username, password);

        assertThat(actual.getUsername(), is(username));
    }
}