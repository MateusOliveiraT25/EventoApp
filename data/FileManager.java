package data;

import model.Evento;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "events.data";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void salvarEventos(List<Evento> eventos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Evento e : eventos) {
                writer.write(String.format("%s;%s;%s;%s;%s;%s\n",
                        e.getNome(),
                        e.getEndereco(),
                        e.getCidade(),
                        e.getCategoria(),
                        e.getHorario().format(formatter),
                        e.getDescricao()
                ));
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar eventos: " + e.getMessage());
        }
    }

    public static List<Evento> carregarEventos() {
        List<Evento> eventos = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Arquivo events.data criado.");
            } catch (IOException e) {
                System.out.println("Erro ao criar arquivo: " + e.getMessage());
            }
            return eventos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 6) {
                    String nome = partes[0];
                    String endereco = partes[1];
                    String cidade = partes[2];
                    String categoria = partes[3];
                    LocalDateTime horario;
                    try {
                        horario = LocalDateTime.parse(partes[4], formatter);
                    } catch (DateTimeParseException e) {
                        horario = LocalDateTime.parse(partes[4], isoFormatter);
                    }
                    String descricao = partes[5];

                    eventos.add(new Evento(nome, endereco, cidade, categoria, horario, descricao));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar eventos: " + e.getMessage());
        }

        return eventos;
    }
}
