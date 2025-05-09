package com.app.controller;

import com.app.dao.UsuarioDAO;
import com.app.model.Usuario;
import static spark.Spark.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class RotaController {

    public static void configurarRotas() {
        staticFiles.location("/public"); // CSS e JS
        get("/login", (req, res) -> {
            return new VelocityTemplateEngine().render(
                    new ModelAndView(null, "/templates/login.html")
            );
        });

        post("/login", (req, res) -> {
            String email = req.queryParams("email");
            String senha = req.queryParams("senha");

            try (UsuarioDAO dao = new UsuarioDAO()) {
                if (dao.verificarSenha(email, senha)) {
                    // Armazena na sessão
                    req.session(true).attribute("email", email);
                    res.redirect("/dashboard");
                } else {
                    res.status(401);
                    return "Email ou senha incorretos.";
                }
            }
            return null;
        });

        get("/dashboard", (req, res) -> {
            String email = req.session().attribute("email");
            if (email == null) {
                res.redirect("/login");
                return null;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("usuario", email);

            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "/templates/dashboard.html")
            );
        });

        post("/analise", (req, res) -> {
            String url = req.queryParams("url");

            // Chamar API Python Flask local
            try {
                String resultado = RequisicaoPython.analisarURL(url);
                return resultado;
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Erro na análise";
            }
        });
    }
}
