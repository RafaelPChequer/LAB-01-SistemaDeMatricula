package Matricula;

import Matricula.disciplina.Disciplina;
import Matricula.usuario.Usuario;
import java.util.List;
public class Aluno extends Usuario {
    private int matricula;
    private List<Disciplina> disciplinasObrigatorias;
    private List<Disciplina> disciplinasOptativas;

    public boolean matricularEmDisciplina(Disciplina disciplina) {
        // Implementação da matrícula em disciplina
        return false;
    }

    public boolean cancelarMatricula(Disciplina disciplina) {
        // Implementação do cancelamento de matrícula
        return false;
    }
}