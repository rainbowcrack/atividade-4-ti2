package com.app.dao;

import com.app.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class UsuarioDAO implements AutoCloseable {
    private Connection conn;

    public UsuarioDAO() throws SQLException, IOException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IOException("db.properties not found in classpath");
            }
            props.load(input);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        conn = DriverManager.getConnection(url, user, password);
    }

    // Criptografa a senha antes de armazenar
    public void criarUsuario(Usuario usuario) throws SQLException {
        String hashedSenha = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());

        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, hashedSenha);
            stmt.executeUpdate();
        }
    }

    // Método para verificar senha durante login
    public boolean verificarSenha(String email, String senha) throws SQLException {
        String sql = "SELECT senha FROM usuarios WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedSenha = rs.getString("senha");
                return BCrypt.checkpw(senha, hashedSenha);  // Verifica a senha
            }
        }
        return false;
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha")); 
                lista.add(u);
            }
        }
        return lista;
    }

    // Metodo para editar o usuário
    public void editarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt())); // Atualiza para senha criptografada
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
        }
    }

    // Metodo para excluir usuario
    public void excluirUsuario(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
