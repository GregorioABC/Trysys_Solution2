import java.util.Scanner;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

// Classe para gerenciar o histórico de ações dos pacientes usando uma pilha
class AcoesPaciente {
    private Stack<String> historico = new Stack<>();

    // Adiciona uma nova ação ao histórico
    public void adicionarAcao(String acao) {
        historico.push(acao);
        System.out.println("Ação adicionada: " + acao);
    }

    // Desfaz a última ação adicionada ao histórico (pop da pilha)
    public void desfazerUltimaAcao() {
        if (!historico.isEmpty()) {
            String acaoDesfeita = historico.pop();
            System.out.println("Ação desfeita: " + acaoDesfeita);
        } else {
            System.out.println("Nenhuma ação para desfazer.");
        }
    }

    // Exibe todas as ações armazenadas no histórico
    public void exibirHistorico() {
        System.out.println("Histórico de ações: " + historico);
    }
}

// Classe para gerenciar a fila de atendimento emergencial
class AtendimentoEmergencial {
    private Queue<String> filaPacientes = new LinkedList<>();

    // Adiciona um novo paciente à fila (enfileiramento)
    public void adicionarPaciente(String nomePaciente) {
        filaPacientes.add(nomePaciente);
        System.out.println("Paciente adicionado à fila: " + nomePaciente);
    }

    // Remove e atende o próximo paciente na fila (desenfileiramento)
    public void atenderPaciente() {
        if (!filaPacientes.isEmpty()) {
            String pacienteAtendido = filaPacientes.poll();
            System.out.println("Paciente atendido: " + pacienteAtendido);
        } else {
            System.out.println("Nenhum paciente na fila.");
        }
    }

    // Exibe todos os pacientes atualmente na fila
    public void exibirFila() {
        System.out.println("Fila de pacientes: " + filaPacientes);
    }
}

// Classe para representar um médico, com nome e especialidade
class Medico implements Comparable<Medico> {
    String nome;
    String especialidade;

    // Construtor para inicializar o médico
    public Medico(String nome, String especialidade) {
        this.nome = nome;
        this.especialidade = especialidade;
    }

    // Método para comparar médicos com base na especialidade (para ordenação na árvore)
    @Override
    public int compareTo(Medico outro) {
        return this.especialidade.compareTo(outro.especialidade);
    }

    // Representação do médico como string (para exibição)
    @Override
    public String toString() {
        return "Médico: " + nome + " - Especialidade: " + especialidade;
    }
}

// Nó da árvore binária para armazenar um médico
class No {
    Medico medico; // Dados do médico
    No esquerda; // Subárvore esquerda
    No direita; // Subárvore direita

    // Construtor para inicializar um nó com um médico
    public No(Medico medico) {
        this.medico = medico;
    }
}

// Classe para gerenciar a árvore binária de médicos
class ArvoreBinaria {
    private No raiz; // Raiz da árvore

    // Adiciona um médico à árvore binária de forma ordenada
    public void adicionar(Medico medico) {
        raiz = adicionarRecursivo(raiz, medico);
    }

    // Método recursivo para inserir o médico no local correto da árvore
    private No adicionarRecursivo(No atual, Medico medico) {
        if (atual == null) {
            return new No(medico); // Cria um novo nó se o local estiver vazio
        }

        if (medico.compareTo(atual.medico) < 0) {
            atual.esquerda = adicionarRecursivo(atual.esquerda, medico); // Insere na subárvore esquerda
        } else if (medico.compareTo(atual.medico) > 0) {
            atual.direita = adicionarRecursivo(atual.direita, medico); // Insere na subárvore direita
        }

        return atual;
    }

    // Busca e exibe todos os médicos com uma especialidade específica
    public void buscarPorEspecialidade(String especialidade) {
        System.out.println("Médicos na especialidade '" + especialidade + "':");
        buscarRecursivo(raiz, especialidade);
    }

    // Método recursivo para percorrer a árvore em busca da especialidade
    private void buscarRecursivo(No atual, String especialidade) {
        if (atual != null) {
            if (atual.medico.especialidade.equals(especialidade)) {
                System.out.println(atual.medico); // Exibe o médico encontrado
            }
            buscarRecursivo(atual.esquerda, especialidade); // Busca na subárvore esquerda
            buscarRecursivo(atual.direita, especialidade); // Busca na subárvore direita
        }
    }
}

// Classe principal que contém o menu interativo
public class ClinicaMedica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Instâncias das classes para gerenciar histórico, fila e médicos
        AcoesPaciente acoesPaciente = new AcoesPaciente();
        AtendimentoEmergencial atendimento = new AtendimentoEmergencial();
        ArvoreBinaria arvore = new ArvoreBinaria();

        // Loop principal do menu interativo
        while (true) {
            System.out.println("\n===== Clínica Médica =====");
            System.out.println("1. Gerenciar histórico de ações dos pacientes");
            System.out.println("2. Gerenciar fila de atendimento emergencial");
            System.out.println("3. Gerenciar médicos (árvore binária)");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    // Submenu para gerenciar o histórico de ações
                    System.out.println("\n-- Histórico de Ações dos Pacientes --");
                    System.out.println("1. Adicionar ação");
                    System.out.println("2. Desfazer última ação");
                    System.out.println("3. Exibir histórico");
                    System.out.print("Escolha uma opção: ");
                    int acaoOpcao = scanner.nextInt();
                    scanner.nextLine();

                    if (acaoOpcao == 1) {
                        System.out.print("Digite a ação do paciente: ");
                        String acao = scanner.nextLine();
                        acoesPaciente.adicionarAcao(acao);
                    } else if (acaoOpcao == 2) {
                        acoesPaciente.desfazerUltimaAcao();
                    } else if (acaoOpcao == 3) {
                        acoesPaciente.exibirHistorico();
                    }
                    break;

                case 2:
                    // Submenu para gerenciar a fila de atendimento emergencial
                    System.out.println("\n-- Fila de Atendimento Emergencial --");
                    System.out.println("1. Adicionar paciente");
                    System.out.println("2. Atender paciente");
                    System.out.println("3. Exibir fila");
                    System.out.print("Escolha uma opção: ");
                    int filaOpcao = scanner.nextInt();
                    scanner.nextLine();

                    if (filaOpcao == 1) {
                        System.out.print("Digite o nome do paciente: ");
                        String nomePaciente = scanner.nextLine();
                        atendimento.adicionarPaciente(nomePaciente);
                    } else if (filaOpcao == 2) {
                        atendimento.atenderPaciente();
                    } else if (filaOpcao == 3) {
                        atendimento.exibirFila();
                    }
                    break;

                case 3:
                    // Submenu para gerenciar os médicos na árvore binária
                    System.out.println("\n-- Gerenciamento de Médicos --");
                    System.out.println("1. Adicionar médico");
                    System.out.println("2. Buscar médicos por especialidade");
                    System.out.print("Escolha uma opção: ");
                    int medicoOpcao = scanner.nextInt();
                    scanner.nextLine();

                    if (medicoOpcao == 1) {
                        System.out.print("Digite o nome do médico: ");
                        String nomeMedico = scanner.nextLine();
                        System.out.print("Digite a especialidade do médico: ");
                        String especialidade = scanner.nextLine();
                        arvore.adicionar(new Medico(nomeMedico, especialidade));
                    } else if (medicoOpcao == 2) {
                        System.out.print("Digite a especialidade para buscar: ");
                        String especialidade = scanner.nextLine();
                        arvore.buscarPorEspecialidade(especialidade);
                    }
                    break;

                case 4:
                    // Encerra o programa
                    System.out.println("Encerrando o programa. Até mais!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}

