package Matricula;

import Matricula.aluno.Aluno;
import Matricula.disciplina.Disciplina;
import Matricula.usuario.Usuario;

public class Secretaria extends Usuario {

    public boolean realizarMatricula(Aluno aluno, Disciplina disciplina) {
        // Implementação da realização de matrícula
        return false;
    }

    public boolean cancelarMatricula(Aluno aluno, Disciplina disciplina) {
        // Implementação do cancelamento de matrícula
        return false;
    }
}