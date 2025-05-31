package view;

import model.Evento;
import model.Usuario;
import data.FileManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private List<Evento> eventos = new ArrayList<>();
    private List<Evento> participando = new ArrayList<>();
    private Usuario usuario;

    public void exibir() {
        eventos = FileManager.carregarEventos();
        System.out.println("\n=== Sistema de Eventos da Sua Cidade ===");
        cadastrarUsuario();

        int opcao;
        do {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Cadastrar evento");
            System.out.println("2. Listar eventos");
            System.out.println("3. Participar de evento");
            System.out.println("4. Cancelar participação");
            System.out.println("5. Meus eventos confirmados");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarEvento();
                case 2 -> listarEventos();
                case 3 -> participarEvento();
                case 4 -> cancelarParticipacao();
                case 5 -> listarEventosConfirmados();
                case 6 -> FileManager.salvarEventos(eventos);
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 6);
    }

    private void cadastrarUsuario() {
        System.out.println("\nCadastro do usuário:");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        usuario = new Usuario(nome, email, cidade);
    }

    private void cadastrarEvento() {
        System.out.println("\nCadastro de Evento:");
        System.out.print("Nome do evento: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        System.out.print("Categoria (festa, show, esportivo, etc.): ");
        String categoria = scanner.nextLine();

        LocalDateTime dataHora = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        while (dataHora == null) {
            System.out.print("Data e hora (formato: dd/MM/yyyy HH:mm): ");
            String entrada = scanner.nextLine();
            try {
                dataHora = LocalDateTime.parse(entrada, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Erro: você precisa informar **a data e a hora**! Exemplo: 25/11/2025 18:00");
            }
        }

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        Evento evento = new Evento(nome, endereco, cidade, categoria, dataHora, descricao);
        eventos.add(evento);
        System.out.println("Evento cadastrado com sucesso!");
    }

    private void listarEventos() {
        System.out.println("\nEventos cadastrados em " + usuario.getCidade() + ":");
        eventos.stream()
                .filter(e -> e.getCidade().equalsIgnoreCase(usuario.getCidade()))
                .sorted()
                .forEach(e -> {
                    System.out.println("----------------------------");
                    System.out.println(e);
                    if (e.estaOcorrendoAgora()) {
                        System.out.println(">> ACONTECENDO AGORA <<");
                    } else if (e.jaOcorreu()) {
                        System.out.println("(Evento já ocorreu)");
                    }
                });
    }

    private void participarEvento() {
        listarEventos();
        System.out.print("\nDigite o nome do evento para participar: ");
        String nomeEvento = scanner.nextLine();
        eventos.stream()
                .filter(e -> e.getNome().equalsIgnoreCase(nomeEvento))
                .findFirst()
                .ifPresentOrElse(
                        e -> {
                            if (!participando.contains(e)) {
                                participando.add(e);
                                System.out.println("Participação confirmada!");
                            } else {
                                System.out.println("Você já está participando deste evento.");
                            }
                        },
                        () -> System.out.println("Evento não encontrado.")
                );
    }

    private void cancelarParticipacao() {
        listarEventosConfirmados();
        System.out.print("\nDigite o nome do evento para cancelar a participação: ");
        String nomeEvento = scanner.nextLine();
        participando.removeIf(e -> e.getNome().equalsIgnoreCase(nomeEvento));
        System.out.println("Participação cancelada, se encontrada.");
    }

    private void listarEventosConfirmados() {
        System.out.println("\nEventos com participação confirmada:");
        if (participando.isEmpty()) {
            System.out.println("Você não confirmou presença em nenhum evento.");
        } else {
            participando.stream().sorted().forEach(e -> {
                System.out.println("----------------------------");
                System.out.println(e);
            });
        }
    }
}
