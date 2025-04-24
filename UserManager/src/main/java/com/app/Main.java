package com.app;

import com.app.dao.UsuarioDAO;
import com.app.model.Usuario;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);
             UsuarioDAO dao = new UsuarioDAO()) {

            System.out.println("📌 Menu:");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Listar Usuários");
            System.out.println("3. Editar Usuário");
            System.out.println("4. Excluir Usuário");
            System.out.println("5. Login");
            System.out.print("Escolha uma opção: ");
            int opcao = sc.nextInt();
            sc.nextLine();  // Consumir a nova linha após o número

            switch (opcao) {
                case 1:
                    // Cadastrar novo usuário
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.print("Senha: ");
                    String senha = sc.nextLine();

                    Usuario novoUsuario = new Usuario(nome, email, senha);
                    dao.criarUsuario(novoUsuario);
                    System.out.println("✅ Usuário cadastrado com sucesso!");
                    break;

                case 2:
                    // Listar usuários
                    System.out.println("\n📄 Lista de usuários:");
                    for (Usuario u : dao.listarUsuarios()) {
                        System.out.println(u.getId() + ": " + u.getNome() + " (" + u.getEmail() + ")");
                    }
                    break;

                case 3:
                    // Editar usuário
                    System.out.print("Informe o ID do usuário a editar: ");
                    int idEditar = sc.nextInt();
                    sc.nextLine();  // Consumir a nova linha após o número

                    System.out.print("Novo Nome: ");
                    String nomeEditar = sc.nextLine();

                    System.out.print("Novo Email: ");
                    String emailEditar = sc.nextLine();

                    System.out.print("Nova Senha: ");
                    String senhaEditar = sc.nextLine();

                    Usuario usuarioEditar = new Usuario(nomeEditar, emailEditar, senhaEditar);
                    usuarioEditar.setId(idEditar);
                    dao.editarUsuario(usuarioEditar);
                    System.out.println("✅ Usuário editado com sucesso!");
                    break;

                case 4:
                    // Excluir usuário
                    System.out.print("Informe o ID do usuário a excluir: ");
                    int idExcluir = sc.nextInt();
                    dao.excluirUsuario(idExcluir);
                    System.out.println("✅ Usuário excluído com sucesso!");
                    break;

                case 5:
                    // Login
                    System.out.print("Informe o Email: ");
                    String loginEmail = sc.nextLine();

                    System.out.print("Informe a Senha: ");
                    String loginSenha = sc.nextLine();

                    if (dao.verificarSenha(loginEmail, loginSenha)) {
                        System.out.println("✅ Login bem-sucedido!");
                    } else {
                        System.out.println("❌ Email ou senha inválidos.");
                    }
                    break;

                default:
                    System.out.println("❌ Opção inválida.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
