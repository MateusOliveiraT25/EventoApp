package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento implements Comparable<Evento> {
    private String nome;
    private String endereco;
    private String cidade;
    private String categoria;
    private LocalDateTime horario;
    private String descricao;

    public Evento(String nome, String endereco, String cidade, String categoria, LocalDateTime horario, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean estaOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        return horario.isBefore(agora.plusMinutes(1)) && horario.isAfter(agora.minusMinutes(90));
    }

    public boolean jaOcorreu() {
        return horario.isBefore(LocalDateTime.now());
    }

    @Override
    public int compareTo(Evento outro) {
        return this.horario.compareTo(outro.horario);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "[" + categoria + "] " + nome +
                " - " + endereco + ", " + cidade +
                "\nHorário: " + horario.format(formatter) +
                "\nDescrição: " + descricao;
    }
}
