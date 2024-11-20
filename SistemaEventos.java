import java.util.*;

public class SistemaEventos {

    class Evento {
        String nome;
        String data;
        String local;
        int capacidade;
        int prioridade;
        int inscricoesRealizadas;
        List<Participante> participantes;

        public Evento(String nome, String data, String local, int capacidade) {
            this.nome = nome;
            this.data = data;
            this.local = local;
            this.capacidade = capacidade;
            this.prioridade = 0;
            this.inscricoesRealizadas = 0;
            this.participantes = new ArrayList<>();
        }

        public boolean qtdInscricao() {
            return inscricoesRealizadas < capacidade;
        }

        public void adicionarParticipante(Participante participante) {
            if (qtdInscricao()) {
                participantes.add(participante);
                inscricoesRealizadas++;
                System.out.println("Participante " + participante.nome + " adicionado ao evento " + nome);
            } else {
                System.out.println("Evento " + nome + " já atingiu a capacidade máxima.");
            }
        }

        public void listarParticipantes() {
            if (participantes.isEmpty()) {
                System.out.println("Nenhum participante inscrito no evento " + nome + ".");
            } else {
                System.out.println("Participantes do evento " + nome + ":");
                for (Participante p : participantes) {
                    System.out.println("- " + p.nome + " (Inscrição: " + p.numeroInscricao + ")");
                }
            }
        }
    }

    class Participante {
        String nome;
        int numeroInscricao;

        public Participante(String nome, int numeroInscricao) {
            this.nome = nome;
            this.numeroInscricao = numeroInscricao;
        }
    }

    class PilhaHistorico {
        private Stack<Evento> historico = new Stack<>();

        public void adicionarEvento(Evento evento) {
            historico.push(evento);
        }

        public void imprimirHistorico() {
            if (historico.isEmpty()) {
                System.out.println("Histórico vazio.");
            } else {
                System.out.println("Histórico de eventos consultados:");
                for (Evento evento : historico) {
                    System.out.println("- " + evento.nome);
                }
            }
        }
    }

    class FilaInscricoes {
        private PriorityQueue<Evento> fila = new PriorityQueue<>(
            Comparator.comparingInt(e -> -e.prioridade)
        );

        public void adicionarInscricao(Evento evento) {
            if (evento.qtdInscricao()) {
                evento.inscricoesRealizadas++;
                evento.prioridade++;
                fila.offer(evento);
                System.out.println("Inscrição adicionada ao evento: " + evento.nome);
            } else {
                System.out.println("Evento " + evento.nome + " já atingiu a capacidade máxima.");
            }
        }

        public void mostrarEventoMaisInscritos() {
            if (fila.isEmpty()) {
                System.out.println("Nenhum evento com inscrições na fila.");
                return;
            }

            Evento maisInscritos = null;
            for (Evento evento : fila) {
                if (maisInscritos == null || evento.inscricoesRealizadas > maisInscritos.inscricoesRealizadas) {
                    maisInscritos = evento;
                }
            }

            if (maisInscritos != null) {
                System.out.println("Evento com mais inscritos: " + maisInscritos.nome +
                        " (Inscrições: " + maisInscritos.inscricoesRealizadas + ")");
            }
        }
    }

    class NodoParticipante {
        Participante participante;
        NodoParticipante esquerda, direita;

        public NodoParticipante(Participante participante) {
            this.participante = participante;
            this.esquerda = null;
            this.direita = null;
        }
    }

    class ArvoreBinariaDeBusca {
        private NodoParticipante raiz;

        public void adicionarParticipante(Participante participante) {
            raiz = adicionarRecursivo(raiz, participante);
        }

        private NodoParticipante adicionarRecursivo(NodoParticipante atual, Participante participante) {
            if (atual == null) {
                return new NodoParticipante(participante);
            }

            if (participante.numeroInscricao < atual.participante.numeroInscricao) {
                atual.esquerda = adicionarRecursivo(atual.esquerda, participante);
            } else if (participante.numeroInscricao > atual.participante.numeroInscricao) {
                atual.direita = adicionarRecursivo(atual.direita, participante);
            }

            return atual;
        }

        public Participante buscarParticipante(int numeroInscricao) {
            return buscarRecursivo(raiz, numeroInscricao);
        }

        private Participante buscarRecursivo(NodoParticipante atual, int numeroInscricao) {
            if (atual == null) {
                return null;
            }

            if (numeroInscricao < atual.participante.numeroInscricao) {
                return buscarRecursivo(atual.esquerda, numeroInscricao);
            } else if (numeroInscricao > atual.participante.numeroInscricao) {
                return buscarRecursivo(atual.direita, numeroInscricao);
            } else {
                return atual.participante;
            }
        }

        public void imprimirParticipantes() {
            imprimirEmOrdem(raiz);
        }

        private void imprimirEmOrdem(NodoParticipante atual) {
            if (atual != null) {
                imprimirEmOrdem(atual.esquerda);
                System.out.println("Participante: " + atual.participante.nome +
                        ", Inscrição: " + atual.participante.numeroInscricao);
                imprimirEmOrdem(atual.direita);
            }
        }
    }

    public static void executar(Scanner scanner) {
        SistemaEventos sistema = new SistemaEventos();

        PilhaHistorico pilhaHistorico = sistema.new PilhaHistorico();
        FilaInscricoes filaInscricoes = sistema.new FilaInscricoes();
        ArvoreBinariaDeBusca arvoreParticipantes = sistema.new ArvoreBinariaDeBusca();
        Map<String, Evento> eventosCriados = new HashMap<>();

        int opcao;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Criar evento");
            System.out.println("2. Adicionar participante a um evento");
            System.out.println("3. Mostrar evento com mais inscritos");
            System.out.println("4. Listar participantes de um evento");
            System.out.println("5. Adicionar participante ao sistema");
            System.out.println("6. Buscar participante por número de inscrição");
            System.out.println("7. Imprimir participantes cadastrados");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o nome do evento: ");
                    String nomeEvento = scanner.nextLine();
                    System.out.print("Digite a data do evento: ");
                    String dataEvento = scanner.nextLine();
                    System.out.print("Digite o local do evento: ");
                    String localEvento = scanner.nextLine();
                    System.out.print("Digite a capacidade máxima do evento: ");
                    int capacidadeEvento = scanner.nextInt();
                    scanner.nextLine();

                    Evento novoEvento = sistema.new Evento(nomeEvento, dataEvento, localEvento, capacidadeEvento);
                    eventosCriados.put(nomeEvento, novoEvento);
                    pilhaHistorico.adicionarEvento(novoEvento);
                    System.out.println("Evento criado com sucesso!");
                    break;

                case 2:
                    System.out.print("Digite o nome do evento: ");
                    String eventoParaAdicionar = scanner.nextLine();
                    if (!eventosCriados.containsKey(eventoParaAdicionar)) {
                        System.out.println("Evento não encontrado.");
                        break;
                    }

                    System.out.print("Digite o número de inscrição do participante: ");
                    int numeroInscricaoEvento = scanner.nextInt();
                    scanner.nextLine();

                    Participante participanteEvento = arvoreParticipantes.buscarParticipante(numeroInscricaoEvento);
                    if (participanteEvento == null) {
                        System.out.println("Participante não encontrado.");
                    } else {
                        Evento evento = eventosCriados.get(eventoParaAdicionar);
                        evento.adicionarParticipante(participanteEvento);
                    }
                    break;

                case 3:
                    filaInscricoes.mostrarEventoMaisInscritos();
                    break;

                case 4:
                    System.out.print("Digite o nome do evento: ");
                    String eventoParaListar = scanner.nextLine();
                    if (eventosCriados.containsKey(eventoParaListar)) {
                        eventosCriados.get(eventoParaListar).listarParticipantes();
                    } else {
                        System.out.println("Evento não encontrado.");
                    }
                    break;

                case 5:
                    System.out.print("Digite o nome do participante: ");
                    String nomeParticipante = scanner.nextLine();
                    System.out.print("Digite o número de inscrição: ");
                    int numeroInscricao = scanner.nextInt();
                    scanner.nextLine();

                    Participante participante = sistema.new Participante(nomeParticipante, numeroInscricao);
                    arvoreParticipantes.adicionarParticipante(participante);
                    System.out.println("Participante adicionado!");
                    break;

                case 6:
                    System.out.print("Digite o número de inscrição do participante: ");
                    int inscricaoBuscar = scanner.nextInt();
                    scanner.nextLine();

                    Participante participanteEncontrado = arvoreParticipantes.buscarParticipante(inscricaoBuscar);
                    if (participanteEncontrado != null) {
                        System.out.println("Participante encontrado: " + participanteEncontrado.nome);
                    } else {
                        System.out.println("Participante não encontrado.");
                    }
                    break;

                case 7:
                    arvoreParticipantes.imprimirParticipantes();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        executar(scanner);
        scanner.close();
    }
}
