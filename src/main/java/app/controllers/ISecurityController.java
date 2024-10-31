package app.controllers;

import app.dtos.UserDTO;
import io.javalin.http.Context;

public interface ISecurityController {

    void login(Context ctx);

    void register(Context ctx);

    UserDTO authenticate(Context ctx);

    String createToken(UserDTO userDTO);

    UserDTO validateToken(String token);
}
