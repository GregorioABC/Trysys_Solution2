import java.util.*;

public class SistemaEventos {

    class Evento {
        String nome;
        String data;
        String local;
        int capacidade;
        int prioridade; 

        public Evento(String nome, String data, String local, int capacidade) {
            this.nome = nome;
            this.data = data;
            this.local = local;
            this.capacidade = capacidade;
            this.prioridade = 0;
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

        public Evento consultarUltimoEvento() {
            if (!historico.isEmpty()) {
                return historico.peek();
            }
            System.out.println("Histórico vazio.");
            return null;
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
            evento.prioridade++;
            fila.offer(evento);
        }

        public Evento processarInscricao() {
            if (!fila.isEmpty()) {
                return fila.poll();
            }
            System.out.println("Nenhuma inscrição na fila.");
            return null;
        }

        public void imprimirFila() {
            if (fila.isEmpty()) {
                System.out.println("Nenhuma inscrição na fila.");
            } else {
                System.out.println("Fila de inscrições (ordem de prioridade):");
                for (Evento evento : fila) {
                    System.out.println("- Evento: " + evento.nome + ", Prioridade: " + evento.prioridade);
                }
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
                System.out.println("Participante: " + atual.participante.nome + ", Inscrição: " + atual.participante.numeroInscricao);
                imprimirEmOrdem(atual.direita);
            }
        }
    }

    public static void executar(Scanner scanner) {
        SistemaEventos sistema = new SistemaEventos();

        PilhaHistorico pilhaHistorico = sistema.new PilhaHistorico();
        FilaInscricoes filaInscricoes = sistema.new FilaInscricoes();
        ArvoreBinariaDeBusca arvoreParticipantes = sistema.new ArvoreBinariaDeBusca();

        int opcao;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Consultar evento (adicionar ao histórico)");
            System.out.println("2. Adicionar inscrição em evento");
            System.out.println("3. Processar inscrição prioritária");
            System.out.println("4. Adicionar participante à árvore");
            System.out.println("5. Buscar participante por número de inscrição");
            System.out.println("6. Imprimir histórico de eventos");
            System.out.println("7. Imprimir fila de inscrições");
            System.out.println("8. Imprimir participantes cadastrados");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o nome do evento: ");
                    String nomeEvento = scanner.nextLine();
                    Evento evento = sistema.new Evento(nomeEvento, "Data", "Local", 100);
                    pilhaHistorico.adicionarEvento(evento);
                    System.out.println("Evento adicionado ao histórico.");
                    break;

                case 2:
                    System.out.print("Digite o nome do evento: ");
                    String nomeInscricao = scanner.nextLine();
                    Evento eventoInscricao = sistema.new Evento(nomeInscricao, "Data", "Local", 100);
                    filaInscricoes.adicionarInscricao(eventoInscricao);
                    System.out.println("Inscrição adicionada à fila.");
                    break;

                case 3:
                    Evento eventoProcessado = filaInscricoes.processarInscricao();
                    if (eventoProcessado != null) {
                        System.out.println("Evento processado: " + eventoProcessado.nome);
                    }
                    break;

                case 4:
                    System.out.print("Digite o nome do participante: ");
                    String nomeParticipante = scanner.nextLine();
                    System.out.print("Digite o número de inscrição: ");
                    int numeroInscricao = scanner.nextInt();
                    scanner.nextLine();

                    Participante participante = sistema.new Participante(nomeParticipante, numeroInscricao);
                    arvoreParticipantes.adicionarParticipante(participante);
                    System.out.println("Participante adicionado à árvore.");
                    break;

                case 5:
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

                case 6:
                    pilhaHistorico.imprimirHistorico();
                    break;

                case 7:
                    filaInscricoes.imprimirFila();
                    break;

                case 8:
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
