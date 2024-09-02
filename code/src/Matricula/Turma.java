package Matricula;

import java.util.List;

public class Turma {
    private Disciplina disciplina;
    private Professor professor;
    private List<Aluno> alunosMatriculados;
    private Status status;
    private int numero;

    private static final int MIN_ALUNOS = 3;
    private static final int MAX_ALUNOS = 60;

    public enum Status {
        ABERTO,
        FECHADO
    }

    public Turma(Disciplina disciplina, Professor professor, List<Aluno> alunosMatriculados, int numero) {
        this.disciplina = disciplina;
        this.professor = professor;
        this.alunosMatriculados = alunosMatriculados;
        this.status = Status.ABERTO;
        this.numero = numero;
    }

    public boolean adicionarAluno(Aluno aluno) {
        if(verificarVagas()){
            alunosMatriculados.add(aluno);
            return true;
        }
        return false;
    }

    public boolean removerAluno(Aluno aluno) {
        if (alunosMatriculados.contains(aluno)) {
            alunosMatriculados.remove(aluno);
            return true;
        }
        return false;
    }

    public boolean verificarVagas() {
        return alunosMatriculados.size() < MAX_ALUNOS;
    }

    public boolean verificarMinimoAlunos() {
        return alunosMatriculados.size() >= MIN_ALUNOS;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void listarAlunos() {
        if (alunosMatriculados == null || alunosMatriculados.isEmpty()) {
            System.out.println("Não há alunos nesta turma.");
            return;
        }

        System.out.println("Lista de alunos na turma " + disciplina.getNome() + ":");
        for (Aluno aluno : alunosMatriculados) {
            System.out.println(aluno.getNome());
        }
    }

    public Status getStatus() {
        return status;
    }

    public int getNumero() {
        return numero;
    }
}
