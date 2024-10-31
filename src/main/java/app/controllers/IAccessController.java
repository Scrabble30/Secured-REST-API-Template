package app.controllers;

import io.javalin.http.Context;

public interface IAccessController {

    void handleAccess(Context ctx);
}
