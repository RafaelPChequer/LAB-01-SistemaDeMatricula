package Matricula;

import java.io.*;
import java.util.*;

public class SistemaMatricula {

    private static Map<String, Usuario> usuarios = new HashMap<>();
    private static List<Curso> cursos = new ArrayList<>();
    private static List<Turma> turmas = new ArrayList<>();
    private static List<Disciplina> disciplinas = new ArrayList<>();
    private static Secretaria secretariaPadrao;

    public static void main(String[] args) {
        carregarDados();

        if (secretariaPadrao == null) {
            secretariaPadrao = new Secretaria(0, "Secretaria", "1234");
            usuarios.put("Secretaria", secretariaPadrao);
        }

        Scanner scanner = new Scanner(System.in);
        Usuario usuarioLogado = null;

        while (true) {
            if (usuarioLogado == null) {
                System.out.println("1. Login");
                System.out.println("2. Criar Conta");
                System.out.println("3. Sair");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();  // consumir newline

                switch (opcao) {
                    case 1:
                        usuarioLogado = login(scanner);
                        break;
                    case 2:
                        criarConta(scanner);
                        break;
                    case 3:
                        salvarDados();
                        System.out.println("Saindo...");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } else {
                if (usuarioLogado instanceof Aluno) {
                    menuAluno(scanner, (Aluno) usuarioLogado);
                } else if (usuarioLogado instanceof Secretaria) {
                    menuSecretaria(scanner, (Secretaria) usuarioLogado);
                } else if (usuarioLogado instanceof Professor) {
                    menuProfessor(scanner, (Professor) usuarioLogado);
                }

                usuarioLogado = null;  // Logout após cada ação
            }
        }
    }

    private static Usuario login(Scanner scanner) {
        System.out.print("Nome de usuário: ");
        String nome = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario usuario = usuarios.get(nome);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            System.out.println("Login bem-sucedido!");
            return usuario;
        } else {
            System.out.println("Nome de usuário ou senha inválidos.");
            return null;
        }
    }

    private static void criarConta(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        // Verificar se já existe um usuário com o mesmo nome
        if (usuarios.containsKey(nome)) {
            System.out.println("Já existe um usuário com esse nome. Por favor, escolha outro nome.");
            return;
        }

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.println("Tipo de usuário:");
        System.out.println("1. Aluno");
        System.out.println("2. Professor");
        System.out.println("3. Secretaria");
        System.out.print("Escolha uma opção: ");
        int tipo = scanner.nextInt();
        scanner.nextLine();  // consumir newline

        Usuario novoUsuario = null;

        switch (tipo) {
            case 1:
                novoUsuario = new Aluno(usuarios.size() + 1, nome, senha);
                break;
            case 2:
                novoUsuario = new Professor(usuarios.size() + 1, nome, senha);
                break;
            case 3:
                novoUsuario = new Secretaria(usuarios.size() + 1, nome, senha);
                break;
            default:
                System.out.println("Tipo de usuário inválido!");
                return;
        }

        usuarios.put(nome, novoUsuario);
        salvarUsuario(novoUsuario);
        System.out.println("Conta criada com sucesso!");
    }

    private static void menuAluno(Scanner scanner, Aluno aluno) {
        System.out.println("Menu do Aluno:");
        System.out.println("1. Solicitar Matrícula");
        System.out.println("2. Cancelar Matrícula");
        System.out.println("3. Logout");

        int opcao = scanner.nextInt();
        scanner.nextLine();  // consumir newline

        switch (opcao) {
            case 1:
                solicitarMatricula(scanner, aluno);
                break;
            case 2:
                cancelarMatricula(scanner, aluno);
                break;
            case 3:
                System.out.println("Logout bem-sucedido.");
                return;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private static void solicitarMatricula(Scanner scanner, Aluno aluno) {
        listarTurmas();
        System.out.print("Digite o número da turma para se matricular: ");
        int numeroTurma = scanner.nextInt();

        for (Turma turma : turmas) {
            if (turma.getNumero() == numeroTurma) {
                if (secretariaPadrao.realizarMatricula(turma, aluno)) {
                    System.out.println("Matrícula realizada com sucesso!");
                } else {
                    System.out.println("Não foi possível realizar a matrícula.");
                }
                return;
            }
        }
        System.out.println("Turma não encontrada.");
    }

    private static void cancelarMatricula(Scanner scanner, Aluno aluno) {
        System.out.print("Digite o número da turma para cancelar a matrícula: ");
        int numeroTurma = scanner.nextInt();

        for (Turma turma : turmas) {
            if (turma.getNumero() == numeroTurma) {
                if (secretariaPadrao.cancelarMatricula(turma, aluno)) {
                    System.out.println("Matrícula cancelada com sucesso!");
                } else {
                    System.out.println("Não foi possível cancelar a matrícula.");
                }
                return;
            }
        }
        System.out.println("Turma não encontrada.");
    }

    // Adicionar os métodos para gerenciar disciplinas e turmas
    public static void adicionarDisciplina(Disciplina disciplina) {
        disciplinas.add(disciplina);
    }

    public static void adicionarTurma(Turma turma) {
        turmas.add(turma);
    }

    private static void menuSecretaria(Scanner scanner, Secretaria secretaria) {
        System.out.println("Menu da Secretaria:");
        System.out.println("1. Realizar Matrícula");
        System.out.println("2. Cancelar Matrícula");
        System.out.println("3. Gerenciar Currículo");
        System.out.println("4. Cadastrar Disciplina");
        System.out.println("5. Cadastrar Turma");
        System.out.println("6. Logout");

        int opcao = scanner.nextInt();
        scanner.nextLine();  // consumir newline

        switch (opcao) {
            case 1:
                solicitarMatricula(scanner, null);  // Para qualquer aluno
                break;
            case 2:
                cancelarMatricula(scanner, null);  // Para qualquer aluno
                break;
            case 3:
                gerenciarCurriculo(scanner, secretaria);
                break;
            case 4:
                cadastrarDisciplina(scanner, secretaria);
                break;
            case 5:
                cadastrarTurma(scanner, secretaria);
                break;
            case 6:
                System.out.println("Logout bem-sucedido.");
                return;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private static void cadastrarDisciplina(Scanner scanner, Secretaria secretaria) {
        System.out.print("Código da disciplina: ");
        int codigo = scanner.nextInt();
        scanner.nextLine();  // Consumir newline

        System.out.print("Nome da disciplina: ");
        String nome = scanner.nextLine();

        System.out.print("Número de créditos: ");
        int numCreditos = scanner.nextInt();

        secretaria.cadastrarDisciplina(codigo, nome, numCreditos);
    }

    private static void cadastrarTurma(Scanner scanner, Secretaria secretaria) {
        if (disciplinas.isEmpty()) {
            System.out.println("Nenhuma disciplina cadastrada. Cadastre uma disciplina primeiro.");
            return;
        }

        System.out.println("Disciplinas disponíveis:");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i + 1) + ". " + disciplinas.get(i).getNome());
        }

        System.out.print("Selecione o número da disciplina para a turma: ");
        int numeroDisciplina = scanner.nextInt();
        scanner.nextLine();  // Consumir newline

        if (numeroDisciplina < 1 || numeroDisciplina > disciplinas.size()) {
            System.out.println("Opção inválida!");
            return;
        }

        Disciplina disciplina = disciplinas.get(numeroDisciplina - 1);

        System.out.print("Número da turma: ");
        int numeroTurma = scanner.nextInt();
        scanner.nextLine();  // Consumir newline

        System.out.print("Nome do professor: ");
        String nomeProfessor = scanner.nextLine();
        Professor professor = (Professor) usuarios.get(nomeProfessor);

        if (professor == null) {
            System.out.println("Professor não encontrado.");
            return;
        }

        secretaria.cadastrarTurma(disciplina, professor, numeroTurma);
    }

    private static void gerenciarCurriculo(Scanner scanner, Secretaria secretaria) {
        // Implementar as funcionalidades de gerenciar currículo (adicionar/remover disciplinas, etc.)
    }

    private static void menuProfessor(Scanner scanner, Professor professor) {
        System.out.println("Menu do Professor:");
        System.out.println("1. Listar Alunos da Turma");
        System.out.println("2. Logout");

        int opcao = scanner.nextInt();
        scanner.nextLine();  // consumir newline

        switch (opcao) {
            case 1:
                listarAlunosDaTurma(professor);
                break;
            case 2:
                System.out.println("Logout bem-sucedido.");
                return;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private static void listarTurmas() {
        System.out.println("Turmas disponíveis:");
        for (Turma turma : turmas) {
            System.out.println("Número: " + turma.getNumero() + " | Disciplina: " + turma.getDisciplina().getNome() + " | Professor: " + turma.getProfessor().getNome() + " | Status: " + turma.getStatus());
        }
    }

    private static void listarAlunosDaTurma(Professor professor) {
        for (Turma turma : turmas) {
            if (turma.getProfessor().equals(professor)) {
                turma.listarAlunos();
            }
        }
    }

    // Métodos para salvar e carregar dados em arquivos

    private static void salvarDados() {
        try {
            salvarUsuarios();
            salvarTurmas();
            // Adicionar outros dados conforme necessário
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void salvarUsuario(Usuario usuario) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(usuario.getClass().getSimpleName() + ".txt", true))) {
            bw.write(usuario.getId() + "," + usuario.getNome() + "," + usuario.getSenha());
            bw.newLine();
            System.out.println("Arquivo salvo em: " + new File(usuario.getClass().getSimpleName() + ".txt").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void salvarUsuarios() throws IOException {
        for (Usuario usuario : usuarios.values()) {
            salvarUsuario(usuario);
        }
    }

    private static void salvarTurmas() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Turma.txt"))) {
            for (Turma turma : turmas) {
                bw.write(turma.getNumero() + "," + turma.getDisciplina().getCodigo() + "," + turma.getProfessor().getId() + "," + turma.getStatus());
                bw.newLine();
            }
        }
    }

    private static void carregarDados() {
        carregarUsuarios("Aluno.txt", Aluno.class);
        carregarUsuarios("Professor.txt", Professor.class);
        carregarUsuarios("Secretaria.txt", Secretaria.class);

        secretariaPadrao = (Secretaria) usuarios.get("Secretaria");
        if (secretariaPadrao == null) {
            secretariaPadrao = new Secretaria(0, "Secretaria", "1234");
            usuarios.put("Secretaria", secretariaPadrao);
        }
    }

    private static void carregarUsuarios(String arquivo, Class<? extends Usuario> classe) {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                Usuario usuario = null;
                if (classe == Aluno.class) {
                    usuario = new Aluno(Integer.parseInt(dados[0]), dados[1], dados[2]);
                } else if (classe == Professor.class) {
                    usuario = new Professor(Integer.parseInt(dados[0]), dados[1], dados[2]);
                } else if (classe == Secretaria.class) {
                    usuario = new Secretaria(Integer.parseInt(dados[0]), dados[1], dados[2]);
                }
                usuarios.put(usuario.getNome(), usuario);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
