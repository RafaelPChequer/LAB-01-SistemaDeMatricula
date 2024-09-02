package Matricula;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String USERS_FILE = "code/usuarios.txt";
    private static int nextId = 1;

    public static void main(String[] args) {
        List<Usuario> usuarios = loadUsersFromFile();
        if (!usuarios.isEmpty()) {
            nextId = usuarios.get(usuarios.size() - 1).getId() + 1;
        }

        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Centralizar a janela

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Usuário:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JButton createUserButton = new JButton("Criar Usuário");
        createUserButton.setBounds(100, 80, 150, 25);
        panel.add(createUserButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userText.getText();
                String password = new String(passwordField.getPassword());

                if (validateLogin(userName, password)) {
                    JOptionPane.showMessageDialog(null, "Login bem-sucedido como " + userName + "!");
                } else {
                    JOptionPane.showMessageDialog(null, "Nome de usuário ou senha incorretos.");
                }
            }
        });

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userText.getText();
                String password = new String(passwordField.getPassword());

                if (userName.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha o nome de usuário e a senha.");
                    return;
                }

                String[] options = {"Aluno", "Professor", "Secretaria"};
                int choice = JOptionPane.showOptionDialog(null, "Selecione o tipo de conta", "Tipo de Conta",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (choice == JOptionPane.CLOSED_OPTION) {
                    return;
                }

                String userType = options[choice];

                if (createUser(userName, password, userType)) {
                    JOptionPane.showMessageDialog(null, "Usuário criado com sucesso como " + userType + "!");
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao criar usuário. O usuário já existe.");
                }
            }
        });
    }

    private static boolean validateLogin(String userName, String password) {
        List<Usuario> usuarios = loadUsersFromFile();
        for (Usuario user : usuarios) {
            if (user.getNome().equals(userName) && user.getSenha().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private static boolean createUser(String userName, String password, String userType) {
        List<Usuario> usuarios = loadUsersFromFile();
        for (Usuario user : usuarios) {
            if (user.getNome().equals(userName)) {
                return false;
            }
        }

        int userId = nextId++;

        Usuario newUser;
        switch (userType) {
            case "Aluno":
                newUser = new Aluno(userId, userName, password);
                break;
            case "Professor":
                newUser = new Professor(userId, userName, password);
                break;
            case "Secretaria":
                newUser = new Secretaria(userId, userName, password);
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + userType);
        }

        usuarios.add(newUser);
        saveUsersToFile(usuarios);
        return true;
    }

    private static List<Usuario> loadUsersFromFile() {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String userType = parts[3];
                    switch (userType) {
                        case "Aluno":
                            usuarios.add(new Aluno(id, parts[1], parts[2]));
                            break;
                        case "Professor":
                            usuarios.add(new Professor(id, parts[1], parts[2]));
                            break;
                        case "Secretaria":
                            usuarios.add(new Secretaria(id, parts[1], parts[2]));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    private static void saveUsersToFile(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Usuario user : usuarios) {
                String userType;
                if (user instanceof Aluno) {
                    userType = "Aluno";
                } else if (user instanceof Professor) {
                    userType = "Professor";
                } else {
                    userType = "Secretaria";
                }
                writer.write(user.getId() + "," + user.getNome() + "," + user.getSenha() + "," + userType);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
