package Matricula;

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
}
