package Matricula.disciplina;

import Matricula.aluno.Aluno;
import java.util.List;

public class Disciplina {
    private int codigo;
    private String nome;
    private int numCreditos;
    private int maxAlunos = 60;
    private List<Aluno> alunosMatriculados;

    public boolean adicionarAluno(Aluno aluno) {
        // Implementação da adição de aluno
        return false;
    }

    public boolean removerAluno(Aluno aluno) {
        // Implementação da remoção de aluno
        return false;
    }

    public boolean verificarVagas() {
        // Implementação da verificação de vagas
        return false;
    }

    public boolean verificarMinimoAlunos() {
        // Implementação da verificação do mínimo de alunos
        return false;
    }
}