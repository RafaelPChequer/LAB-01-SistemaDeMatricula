package Matricula;

import java.util.ArrayList;
import java.util.List;

public class Curriculo {
    private int semestre;
    private int ano;
    private Curso curso;
    private List<Turma> turmas;

    public Curriculo(int semestre, int ano, Curso curso) {
        this.semestre = semestre;
        this.ano = ano;
        this.curso = curso;
        this.turmas = new ArrayList<>();
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    // Adiciona uma turma ao currículo
    public void adicionarTurma(Turma turma) {
        turmas.add(turma);
    }

    // Remove uma turma do currículo
    public void removerTurma(Turma turma) {
        turmas.remove(turma);
    }

    // Desativa turmas com menos de 3 alunos
    public void desativarTurmasInativas() {
        for (Turma turma : turmas) {
            if (!turma.verificarMinimoAlunos()) {
                turma.setStatus(Turma.Status.FECHADO);
            }
        }
    }

    // Reativa turmas que tem 3 ou mais alunos
    public void reativarTurmas() {
        for (Turma turma : turmas) {
            if (turma.verificarVagas() && turma.getStatus() == Turma.Status.FECHADO) {
                turma.setStatus(Turma.Status.ABERTO);
            }
        }
    }
}
