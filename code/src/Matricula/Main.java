package Matricula;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String USERS_FILE = "code/usuarios.txt";
    private static final String DISCIPLINAS_FILE = "code/disciplinas.txt";
    private static final String CURRICULOS_FILE = "code/curriculos.txt";
    private static int nextId = 1;

    public static void main(String[] args) {
        List<Usuario> usuarios = loadUsersFromFile();
        if (!usuarios.isEmpty()) {
            nextId = usuarios.get(usuarios.size() - 1).getId() + 1;
        }

        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Centraliza a janela

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(frame, panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JFrame frame, JPanel panel) {
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

                Usuario loggedInUser = validateLogin(userName, password);
                if (loggedInUser != null) {
                    String message = String.format("Login bem-sucedido como %s!\nTipo: %s\nID: %d",
                            loggedInUser.getNome(), loggedInUser.getClass().getSimpleName(), loggedInUser.getId());

                    if (loggedInUser instanceof Aluno) {
                        Aluno aluno = (Aluno) loggedInUser;
                        message += String.format("\nNúmero de Matrícula: %d", aluno.getMatricula());
                        JOptionPane.showMessageDialog(null, message);

                        JFrame alunoFrame = new JFrame("Área do Aluno");
                        alunoFrame.setSize(400, 300);
                        alunoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        alunoFrame.setLocationRelativeTo(null);

                        JPanel alunoPanel = new JPanel();
                        alunoPanel.setLayout(null);

                        JButton matricularButton = new JButton("Matricular em Turma");
                        matricularButton.setBounds(10, 80, 200, 25);
                        alunoPanel.add(matricularButton);

                        matricularButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                showMatricularTurmaPage(aluno);
                            }
                        });

                        alunoFrame.add(alunoPanel);
                        alunoFrame.setVisible(true);
                    }

                    if (loggedInUser instanceof Secretaria) {
                        JOptionPane.showMessageDialog(null, message);

                        JFrame secretariaFrame = new JFrame("Área da Secretaria");
                        secretariaFrame.setSize(400, 300);
                        secretariaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        secretariaFrame.setLocationRelativeTo(null);

                        JPanel secretariaPanel = new JPanel();
                        secretariaPanel.setLayout(null);

                        JLabel secretariaInfoLabel = new JLabel("Informações da Secretaria:");
                        secretariaInfoLabel.setBounds(10, 10, 300, 25);
                        secretariaPanel.add(secretariaInfoLabel);

                        JButton addDisciplinaButton = new JButton("Adicionar Disciplina");
                        addDisciplinaButton.setBounds(10, 50, 200, 25);
                        secretariaPanel.add(addDisciplinaButton);

                        addDisciplinaButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Esconder a janela da secretaria e abrir a página de adicionar disciplinas
                                secretariaFrame.setVisible(false);
                                showAddDisciplinaPage();
                            }
                        });

                        JButton alterarCurriculoButton = new JButton("Alterar Currículo");
                        alterarCurriculoButton.setBounds(10, 90, 200, 25);
                        secretariaPanel.add(alterarCurriculoButton);

                        alterarCurriculoButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                showAlterarCurriculoPage();
                            }
                        });


                        secretariaFrame.add(secretariaPanel);
                        secretariaFrame.setVisible(true);
                    }
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

    private static void showMatricularTurmaPage(Aluno aluno) {
        JFrame matricularFrame = new JFrame("Matricular em Turma");
        matricularFrame.setSize(400, 300);
        matricularFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        matricularFrame.setLocationRelativeTo(null);

        JPanel matricularPanel = new JPanel();
        matricularPanel.setLayout(null);

        JLabel disciplinaLabel = new JLabel("Disciplina:");
        disciplinaLabel.setBounds(10, 20, 150, 25);
        matricularPanel.add(disciplinaLabel);

        JComboBox<Disciplina> disciplinaComboBox = new JComboBox<>(loadDisciplinasFromFile().toArray(new Disciplina[0]));
        disciplinaComboBox.setBounds(170, 20, 200, 25);
        matricularPanel.add(disciplinaComboBox);

        JLabel turmaLabel = new JLabel("Turma:");
        turmaLabel.setBounds(10, 60, 150, 25);
        matricularPanel.add(turmaLabel);

        JComboBox<Turma> turmaComboBox = new JComboBox<>();
        turmaComboBox.setBounds(170, 60, 200, 25);
        matricularPanel.add(turmaComboBox);

        JButton carregarTurmasButton = new JButton("Carregar Turmas");
        carregarTurmasButton.setBounds(10, 100, 150, 25);
        matricularPanel.add(carregarTurmasButton);

        carregarTurmasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Disciplina disciplinaSelecionada = (Disciplina) disciplinaComboBox.getSelectedItem();
                if (disciplinaSelecionada != null) {
                    turmaComboBox.removeAllItems();
                    List<Turma> turmas = loadTurmasFromFile();
                    for (Turma turma : turmas) {
                        if (turma.getDisciplina().equals(disciplinaSelecionada)) {
                            turmaComboBox.addItem(turma);
                        }
                    }
                }
            }
        });

        JButton matricularButton = new JButton("Matricular");
        matricularButton.setBounds(10, 140, 150, 25);
        matricularPanel.add(matricularButton);

        matricularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Turma turmaSelecionada = (Turma) turmaComboBox.getSelectedItem();
                if (turmaSelecionada != null && turmaSelecionada.adicionarAluno(aluno)) {
                    saveTurmasToFile(loadTurmasFromFile()); // Atualize o arquivo de turmas
                    JOptionPane.showMessageDialog(null, "Matriculado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível matricular.");
                }
            }
        });

        matricularFrame.add(matricularPanel);
        matricularFrame.setVisible(true);
    }

    private static List<Turma> loadTurmasFromFile() {
        List<Turma> turmas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("code/turmas.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int numero = Integer.parseInt(parts[0]);
                    int disciplinaCodigo = Integer.parseInt(parts[1]);
                    int professorId = Integer.parseInt(parts[2]);
                    Disciplina disciplina = findDisciplinaByCodigo(disciplinaCodigo);
                    Professor professor = findProfessorById(professorId);
                    Turma turma = new Turma(disciplina, professor, new ArrayList<>(), numero);
                    turmas.add(turma);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return turmas;
    }

    private static List<Disciplina> loadDisciplinasFromFile() {
        List<Disciplina> disciplinas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DISCIPLINAS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int codigo = Integer.parseInt(parts[0]);
                    String nome = parts[1];
                    int creditos = Integer.parseInt(parts[2]);
                    disciplinas.add(new Disciplina(codigo, nome, creditos));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return disciplinas;
    }


    private static void saveTurmasToFile(List<Turma> turmas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("code/turmas.txt"))) {
            for (Turma turma : turmas) {
                writer.write(turma.getNumero() + "," + turma.getDisciplina().getCodigo() + "," + turma.getProfessor().getId());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Disciplina findDisciplinaByCodigo(int codigo) {
        List<Disciplina> disciplinas = loadDisciplinasFromFile();
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getCodigo() == codigo) {
                return disciplina;
            }
        }
        return null;
    }

    private static Professor findProfessorById(int id) {
        List<Usuario> usuarios = loadUsersFromFile();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Professor && usuario.getId() == id) {
                return (Professor) usuario;
            }
        }
        return null;
    }



    private static void showAlterarCurriculoPage() {
        JFrame alterarCurriculoFrame = new JFrame("Alterar Currículo");
        alterarCurriculoFrame.setSize(400, 300);
        alterarCurriculoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        alterarCurriculoFrame.setLocationRelativeTo(null);

        JPanel alterarCurriculoPanel = new JPanel();
        alterarCurriculoPanel.setLayout(null);

        JLabel nomeCurriculoLabel = new JLabel("Nome do Currículo:");
        nomeCurriculoLabel.setBounds(10, 20, 150, 25);
        alterarCurriculoPanel.add(nomeCurriculoLabel);

        JTextField nomeCurriculoField = new JTextField(20);
        nomeCurriculoField.setBounds(170, 20, 200, 25);
        alterarCurriculoPanel.add(nomeCurriculoField);

        JLabel semestreLabel = new JLabel("Semestre:");
        semestreLabel.setBounds(10, 60, 150, 25);
        alterarCurriculoPanel.add(semestreLabel);

        JTextField semestreField = new JTextField(20);
        semestreField.setBounds(170, 60, 200, 25);
        alterarCurriculoPanel.add(semestreField);

        JLabel anoLabel = new JLabel("Ano:");
        anoLabel.setBounds(10, 100, 150, 25);
        alterarCurriculoPanel.add(anoLabel);

        JTextField anoField = new JTextField(20);
        anoField.setBounds(170, 100, 200, 25);
        alterarCurriculoPanel.add(anoField);

        JButton saveButton = new JButton("Salvar Alterações");
        saveButton.setBounds(10, 150, 150, 25);
        alterarCurriculoPanel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCurriculo = nomeCurriculoField.getText();
                String semestreText = semestreField.getText();
                String anoText = anoField.getText();

                if (!nomeCurriculo.isEmpty() && !semestreText.isEmpty() && !anoText.isEmpty()) {
                    try {
                        int semestre = Integer.parseInt(semestreText);
                        int ano = Integer.parseInt(anoText);

                        Curso curso = new Curso(nomeCurriculo, new ArrayList<>()); // Adapte conforme necessário
                        Curriculo curriculo = new Curriculo(semestre, ano, curso);

                        // Salvar as informações do currículo em curriculos.txt
                        saveCurriculoToFile(curriculo);

                        JOptionPane.showMessageDialog(null, "Currículo alterado com sucesso!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Semestre e Ano devem ser números inteiros.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente.");
                }
            }
        });

        alterarCurriculoFrame.add(alterarCurriculoPanel);
        alterarCurriculoFrame.setVisible(true);
    }

    private static void saveCurriculoToFile(Curriculo curriculo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CURRICULOS_FILE, true))) {
            // Adapte o formato para incluir o nome do curso se necessário
            writer.write(curriculo.getSemestre() + "," + curriculo.getAno());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void showAddDisciplinaPage() {
        JFrame addDisciplinaFrame = new JFrame("Adicionar Disciplina");
        addDisciplinaFrame.setSize(400, 300);
        addDisciplinaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addDisciplinaFrame.setLocationRelativeTo(null);

        JPanel addDisciplinaPanel = new JPanel();
        addDisciplinaPanel.setLayout(null);

        JLabel nomeDisciplinaLabel = new JLabel("Nome da Disciplina:");
        nomeDisciplinaLabel.setBounds(10, 20, 150, 25);
        addDisciplinaPanel.add(nomeDisciplinaLabel);

        JTextField nomeDisciplinaField = new JTextField(20);
        nomeDisciplinaField.setBounds(170, 20, 200, 25);
        addDisciplinaPanel.add(nomeDisciplinaField);

        JLabel codigoDisciplinaLabel = new JLabel("Código da Disciplina:");
        codigoDisciplinaLabel.setBounds(10, 60, 150, 25);
        addDisciplinaPanel.add(codigoDisciplinaLabel);

        JTextField codigoDisciplinaField = new JTextField(20);
        codigoDisciplinaField.setBounds(170, 60, 200, 25);
        addDisciplinaPanel.add(codigoDisciplinaField);

        JLabel numeroCreditosLabel = new JLabel("Número de Créditos:");
        numeroCreditosLabel.setBounds(10, 100, 150, 25);
        addDisciplinaPanel.add(numeroCreditosLabel);

        JTextField numeroCreditosField = new JTextField(20);
        numeroCreditosField.setBounds(170, 100, 200, 25);
        addDisciplinaPanel.add(numeroCreditosField);

        JButton saveButton = new JButton("Salvar Disciplina");
        saveButton.setBounds(10, 150, 150, 25);
        addDisciplinaPanel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeDisciplina = nomeDisciplinaField.getText();
                int codigoDisciplina = Integer.parseInt(codigoDisciplinaField.getText());
                int numeroCreditos = Integer.parseInt(numeroCreditosField.getText());

                if (!nomeDisciplina.isEmpty() && !codigoDisciplinaField.getText().isEmpty()
                        && !numeroCreditosField.getText().isEmpty()) {
                    Disciplina novaDisciplina = new Disciplina(codigoDisciplina, nomeDisciplina, numeroCreditos);
                    saveDisciplinaToFile(novaDisciplina);
                    JOptionPane.showMessageDialog(null, "Disciplina " + novaDisciplina.getNome() + " adicionada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente.");
                }
            }
        });

        addDisciplinaFrame.add(addDisciplinaPanel);
        addDisciplinaFrame.setVisible(true);
    }

    private static Usuario validateLogin(String userName, String password) {
        List<Usuario> usuarios = loadUsersFromFile();
        for (Usuario user : usuarios) {
            if (user.getNome().equals(userName) && user.getSenha().equals(password)) {
                return user;
            }
        }
        return null;
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

    private static void saveDisciplinaToFile(Disciplina disciplina) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DISCIPLINAS_FILE, true))) {
            writer.write(disciplina.getCodigo() + "," + disciplina.getNome() + "," + disciplina.getNumCreditos());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}