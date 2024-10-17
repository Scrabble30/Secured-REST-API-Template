package app.routes;

import app.controllers.SecurityController;
import app.dtos.HttpMessageDTO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SecurityRoutes {

    private final SecurityController securityController;

    public SecurityRoutes(EntityManagerFactory emf) {
        this.securityController = SecurityController.getInstance(emf);
    }

    public EndpointGroup getSecurityRoutes() {
        return () -> {
            path("/auth", () -> {
                get("/test", ctx -> ctx.json(new HttpMessageDTO(200, "Hello from open."), HttpMessageDTO.class));
                post("/login", securityController::login);
                post("/register", securityController::register);
            });
        };
    }

    public EndpointGroup getProtectedDemoRoutes() {
        return () -> {
            path("/protected", () -> {
                get("/user_demo", ctx -> ctx.json(new HttpMessageDTO(200, "Hello from user protected."), HttpMessageDTO.class));
                get("/admin_demo", ctx -> ctx.json(new HttpMessageDTO(200, "Hello from admin protected."), HttpMessageDTO.class));
            });
        };
    }
}
