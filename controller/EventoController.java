package controller;

import model.Evento;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventoController {
    private List<Evento> eventos;
    private static final String FILE_NAME = "events.data";

    public EventoController() {
        eventos = new ArrayList<>();
        carregarEventos();
    }

    public void adicionarEvento(Evento evento) {
        eventos.add(evento);
        salvarEventos();
    }

    public List<Evento> listarEventos() {
        eventos.sort(Comparator.comparing(Evento::getHorario));
        return eventos;
    }

    public List<Evento> eventosAtuais() {
        LocalDateTime agora = LocalDateTime.now();
        return eventos.stream()
                .filter(e -> e.getHorario().isBefore(agora.plusMinutes(1)) && e.getHorario().isAfter(agora.minusMinutes(60)))
                .toList();
    }

    public List<Evento> eventosPassados() {
        LocalDateTime agora = LocalDateTime.now();
        return eventos.stream().filter(e -> e.getHorario().isBefore(agora)).toList();
    }

    private void salvarEventos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(new ArrayList<>(eventos));
        } catch (IOException e) {
            System.out.println("Erro ao salvar eventos.");
        }
    }

    private void carregarEventos() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                eventos = (ArrayList<Evento>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar eventos.");
            }
        }
    }
}
