package Matricula;

public class Disciplina {
    private int codigo;
    private String nome;
    private int numCreditos;

    // Construtor
    public Disciplina(int codigo, String nome, int numCreditos) {
        this.codigo = codigo;
        this.nome = nome;
        this.numCreditos = numCreditos;
    }

    public String getNome() {
        return nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getNumCreditos() {
        return numCreditos;
    }
}
