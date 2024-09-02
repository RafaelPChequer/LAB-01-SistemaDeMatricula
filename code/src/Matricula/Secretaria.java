package Matricula;

import java.util.ArrayList;

public class Secretaria extends Usuario {

    public Secretaria(int id, String nome, String senha) {
        super(id, nome, senha);
    }

    public boolean realizarMatricula(Turma turma, Aluno aluno) {
        if (turma.adicionarAluno(aluno)) {
            notificarCobranca();
            return true;
        }
        return false;
    }

    public boolean cancelarMatricula(Turma turma, Aluno aluno) {
        return turma.removerAluno(aluno);
    }

    public void notificarCobranca() {
        System.out.println("Enviar cobrança!");
    }
    public void cadastrarDisciplina(int codigo, String nome, int numCreditos) {
        Disciplina novaDisciplina = new Disciplina(codigo, nome, numCreditos);
        SistemaMatricula.adicionarDisciplina(novaDisciplina);
        System.out.println("Disciplina cadastrada com sucesso: " + nome);
    }

    public void cadastrarTurma(Disciplina disciplina, Professor professor, int numero) {
        Turma novaTurma = new Turma(disciplina, professor, new ArrayList<>(), numero);
        SistemaMatricula.adicionarTurma(novaTurma);
        System.out.println("Turma cadastrada com sucesso: " + disciplina.getNome() + " - Turma " + numero);
    }
}
