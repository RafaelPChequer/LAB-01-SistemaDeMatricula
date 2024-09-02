package Matricula;

public class Aluno extends Usuario {
    private int matricula;

    public Aluno(int id, String nome, String senha) {
        super(id, nome, senha);
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
}