import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

class pilha {
    private Stack<String> pilhaEventos;

    public pilha() {
        pilhaEventos = new Stack<>();
    }

    public void consultarEvento(String evento) {
        pilhaEventos.push(evento);
        System.out.println("Evento Consultado: " + evento);
    }

    public void UltimoEvento() {
        if (!pilhaEventos.isEmpty()) {
            String evento = pilhaEventos.pop();
            System.out.println("Voltando para o último evento: " + evento);
        } else {
            System.out.println("Sem eventos no histórico.");
        }
    }

    public void limparHistorico() {
        pilhaEventos.clear();
        System.out.println("Histórico de eventos limpo.");
    }

    public void removerEvento(String evento) {
        if (pilhaEventos.remove(evento)) {
            System.out.println("Evento \"" + evento + "\" removido do histórico.");
        } else {
            System.out.println("Evento \"" + evento + "\" não encontrado no histórico.");
        }
    }
}

class InscricaoPrioritaria implements Comparable<InscricaoPrioritaria> {
    String nomeParticipante;
    int prioridade;

    public InscricaoPrioritaria(String nomeParticipante, int prioridade) {
        this.nomeParticipante = nomeParticipante;
        this.prioridade = prioridade;
    }

    @Override
    public int compareTo(InscricaoPrioritaria o) {
        return Integer.compare(o.prioridade, this.prioridade); // Prioridade maior vem primeiro
    }
}

class fila {
    private PriorityQueue<InscricaoPrioritaria> filaInscricao;

    public fila() {
        filaInscricao = new PriorityQueue<>();
    }

    public void inscreverParticipante(String nome, int prioridade) {
        filaInscricao.add(new InscricaoPrioritaria(nome, prioridade));
        System.out.println(nome + " inscrito com prioridade " + prioridade);
    }

    public void alocarInscricao() {
        if (!filaInscricao.isEmpty()) {
            InscricaoPrioritaria inscricao = filaInscricao.poll();
            System.out.println("Inscrição alocada para: " + inscricao.nomeParticipante);
        } else {
            System.out.println("Nenhum participante na fila.");
        }
    }

    public void cancelarInscricao(String nomeParticipante) {
        filaInscricao.removeIf(inscricao -> inscricao.nomeParticipante.equals(nomeParticipante));
        System.out.println("Inscrição de " + nomeParticipante + " cancelada.");
    }

    private static final int LIMITE_MAXIMO = 100;

    public boolean filaCheia() {
        return filaInscricao.size() >= LIMITE_MAXIMO;
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

class ArvoreBinariaBusca {
    private class No {
        Participante participante;
        No esquerda, direita;

        public No(Participante participante) {
            this.participante = participante;
            esquerda = direita = null;
        }
    }

    private No raiz;

    public ArvoreBinariaBusca() {
        raiz = null;
    }

    public void inserir(Participante participante) {
        raiz = inserirRecursivo(raiz, participante);
    }

    private No inserirRecursivo(No raiz, Participante participante) {
        if (raiz == null) {
            raiz = new No(participante);
            return raiz;
        }
        if (participante.numeroInscricao < raiz.participante.numeroInscricao) {
            raiz.esquerda = inserirRecursivo(raiz.esquerda, participante);
        } else if (participante.numeroInscricao > raiz.participante.numeroInscricao) {
            raiz.direita = inserirRecursivo(raiz.direita, participante);
        }
        return raiz;
    }

    public Participante buscarPorNumeroInscricao(int numeroInscricao) {
        return buscarRecursivo(raiz, numeroInscricao);
    }

    private Participante buscarRecursivo(No raiz, int numeroInscricao) {
        if (raiz == null || raiz.participante.numeroInscricao == numeroInscricao) {
            return raiz == null ? null : raiz.participante;
        }
        return numeroInscricao < raiz.participante.numeroInscricao
            ? buscarRecursivo(raiz.esquerda, numeroInscricao)
            : buscarRecursivo(raiz.direita, numeroInscricao);
    }

    public Participante buscarPorNome(String nome) {
        return buscarPorNomeRecursivo(raiz, nome);
    }

    private Participante buscarPorNomeRecursivo(No raiz, String nome) {
        if (raiz == null || raiz.participante.nome.equals(nome)) {
            return raiz == null ? null : raiz.participante;
        }
        Participante resultadoEsquerda = buscarPorNomeRecursivo(raiz.esquerda, nome);
        if (resultadoEsquerda != null) return resultadoEsquerda;
        return buscarPorNomeRecursivo(raiz.direita, nome);
    }
}

public class SistemaEventos {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        fila filaInscricoes = new fila();
        pilha historicoEventos = new pilha();
        ArvoreBinariaBusca arvoreParticipantes = new ArvoreBinariaBusca();

        
        System.out.println("Sistema de Eventos Iniciado!");

        
        filaInscricoes.inscreverParticipante("Alice", 3);
        filaInscricoes.inscreverParticipante("Bob", 5);
        filaInscricoes.inscreverParticipante("Charlie", 1);

        
        filaInscricoes.alocarInscricao();
        filaInscricoes.alocarInscricao();

        
        historicoEventos.consultarEvento("Evento A");
        historicoEventos.consultarEvento("Evento B");
        historicoEventos.consultarEvento("Evento C");

        
        historicoEventos.UltimoEvento();

        
        historicoEventos.limparHistorico();

        
        arvoreParticipantes.inserir(new Participante("Alice", 1001));
        arvoreParticipantes.inserir(new Participante("Bob", 1002));
        arvoreParticipantes.inserir(new Participante("Charlie", 1003));

        
        Participante participante = arvoreParticipantes.buscarPorNumeroInscricao(1002);
        if (participante != null) {
            System.out.println("Participante encontrado: " + participante.nome);
        } else {
            System.out.println("Participante não encontrado.");
        }

        
        participante = arvoreParticipantes.buscarPorNome("Charlie");
        if (participante != null) {
            System.out.println("Participante encontrado: " + participante.nome);
        } else {
            System.out.println("Participante não encontrado.");
        }

        
        filaInscricoes.cancelarInscricao("Alice");

        
        if (filaInscricoes.filaCheia()) {
            System.out.println("A fila de inscrições está cheia.");
        } else {
            System.out.println("Há espaço na fila de inscrições.");
        }

        scanner.close();
    }
}
