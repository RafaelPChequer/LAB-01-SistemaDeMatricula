package Matricula;

import java.util.*;

public class Curriculo {
    private int semestre;
    private int ano;
    private List<Turma> turmas;

    public boolean adicionarTurma(Turma turma) {
        turmas.add(turma);
        return true;
    }

    public void atualizarStatusTurmas(List<Turma> turmas) {
        for (Turma turma : turmas) {
            if (!turma.verificarMinimoAlunos()) {
                turma.setStatus(Turma.Status.FECHADO);
            }
        }
    }

    public Map<String, List<Turma>> listarTurmasPorDisciplina() {
        Map<String, List<Turma>> turmasPorDisciplina = new HashMap<>();

        for (Turma turma : turmas) {
            String nomeDisciplina = turma.getDisciplina().getNome();
            turmasPorDisciplina
                    .computeIfAbsent(nomeDisciplina, k -> new ArrayList<>())
                    .add(turma);
        }

        return turmasPorDisciplina;
    }

    public void listarTurmasPorProfessor(Professor professor) {
        Scanner scanner = new Scanner(System.in);
        List<Turma> turmasDoProfessor = new ArrayList<>();

        // Filtra as turmas do professor
        for (Turma turma : turmas) {
            if (turma.getProfessor().equals(professor)) {
                turmasDoProfessor.add(turma);
            }
        }

        if (turmasDoProfessor.isEmpty()) {
            System.out.println("Este professor não tem turmas.");
            return;
        }

        System.out.println("Turmas do professor " + professor.getNome() + ":");
        for (int i = 0; i < turmasDoProfessor.size(); i++) {
            System.out.println((i + 1) + ". " + turmasDoProfessor.get(i).getDisciplina().getNome());
        }

        System.out.print("Escolha o número da turma para listar os alunos: ");
        int escolha = scanner.nextInt();

        if (escolha > 0 && escolha <= turmasDoProfessor.size()) {
            Turma turmaEscolhida = turmasDoProfessor.get(escolha - 1);
            System.out.println("Alunos da turma " + turmaEscolhida.getDisciplina().getNome() + ":");
            turmaEscolhida.listarAlunos();
        } else {
            System.out.println("Opção inválida.");
        }
    }
}
