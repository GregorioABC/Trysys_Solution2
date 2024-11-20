import java.util.*;

public class SistemaRestaurante {
    private Mesa cabeca;
    private String[] cardapio = {"Hamburguer", "Pizza", "Salada", "Suco", "Refrigerante", "Rosquinha"};
    private Map<String, Integer> contagemItensPedidos = new HashMap<>();
    private Map<Integer, Integer> contagemReservasMesa = new HashMap<>(); // Contador de reservas por mesa

    // Pilha
    private Stack<String> historicoModificacoes = new Stack<>();
    
    // Fila
    private Queue<Pedido> filaPedidos = new LinkedList<>();
    
    // Árvore binária :)
    private ArvoreCardapio arvoreCardapio = new ArvoreCardapio();

    public SistemaRestaurante() {
        for (String item : cardapio) {
            contagemItensPedidos.put(item, 0);
            arvoreCardapio.adicionarItem(item, 10.0); // Adicionando preço fictício aos itens
        }
    }

    class ItemPedido {
        String descricao;
        int quantidade;
        double total;

        ItemPedido(String descricao, int quantidade, double total) {
            this.descricao = descricao;
            this.quantidade = quantidade;
            this.total = total;
        }
    }

    class Pedido {
        ItemPedido item;
        Pedido proximo;

        Pedido(ItemPedido item) {
            this.item = item;
            this.proximo = null;
        }
    }

    class Mesa {
        int numero;
        String cliente;
        String status;
        Pedido pedidos;
        Mesa proximo;

        Mesa(int numero, String cliente) {
            this.numero = numero;
            this.cliente = cliente;
            this.status = "ocupada";
            this.pedidos = null;
            this.proximo = null;

            // Atualiza a contagem de reservas
            contagemReservasMesa.put(numero, contagemReservasMesa.getOrDefault(numero, 0) + 1);
        }
    }

    // Pilha
    public void adicionarModificacao(String modificacao) {
        historicoModificacoes.push(modificacao);
    }

    public void desfazerModificacao() {
        if (!historicoModificacoes.isEmpty()) {
            System.out.println("Desfazendo modificação: " + historicoModificacoes.pop());
        } else {
            System.out.println("Nenhuma modificação para desfazer.");
        }
    }

    // Fila de pedidos
    public void adicionarPedidoNaFila(Pedido pedido) {
        filaPedidos.offer(pedido);
    }

    public void processarProximoPedido() {
        Pedido pedido = filaPedidos.poll();
        if (pedido != null) {
            System.out.println("Processando pedido: " + pedido.item.descricao);
        } else {
            System.out.println("Nenhum pedido na fila.");
        }
    }

    // Método para exibir o item
    public void exibirItemMaisRentavel() {
        double maiorTotal = 0;
        String itemMaisRentavel = "";
        
        for (Map.Entry<String, Integer> entry : contagemItensPedidos.entrySet()) {
            String item = entry.getKey();
            double totalVendas = entry.getValue() * 10.0; // Considerando preço fixo de 10.0 por item
            if (totalVendas > maiorTotal) {
                maiorTotal = totalVendas;
                itemMaisRentavel = item;
            }
        }
        System.out.println("Item mais rentável: " + itemMaisRentavel + " com total de R$ " + maiorTotal);
    }

    class ArvoreCardapio {
        NoCardapio raiz;

        class NoCardapio {
            String nome;
            double preco;
            NoCardapio esquerdo, direito;
            NoCardapio(String nome, double preco) {
                this.nome = nome;
                this.preco = preco;
                this.esquerdo = null;
                this.direito = null;
            }
        }

        public void adicionarItem(String nome, double preco) {
            // Easter egg:"Espaguete à la Godfather"
            if (nome.equalsIgnoreCase("Espaguete à la Godfather")) {
                System.out.println("Don Corleone: Vou fazer uma oferta que ele não poderá recusar...");
            }
            raiz = adicionarRec(raiz, nome, preco);
        }

        private NoCardapio adicionarRec(NoCardapio raiz, String nome, double preco) {
            if (raiz == null) {
                raiz = new NoCardapio(nome, preco);
                return raiz;
            }
            if (nome.compareTo(raiz.nome) < 0) {
                raiz.esquerdo = adicionarRec(raiz.esquerdo, nome, preco);
            } else if (nome.compareTo(raiz.nome) > 0) {
                raiz.direito = adicionarRec(raiz.direito, nome, preco);
            }
            return raiz;
        }

        public NoCardapio buscarItem(String nome) {
            if (nome.equalsIgnoreCase("Espaguete à la Godfather")) {
                System.out.println("Don Corleone: Vou fazer uma oferta que ele não poderá recusar...");
            }
            return buscarRec(raiz, nome);
        }

        private NoCardapio buscarRec(NoCardapio raiz, String nome) {
            if (raiz == null || raiz.nome.equalsIgnoreCase(nome)) {
                return raiz;
            }
            if (nome.compareTo(raiz.nome) < 0) {
                return buscarRec(raiz.esquerdo, nome);
            }
            return buscarRec(raiz.direito, nome);
        }
    }

    public void adicionarMesa(int numero, String cliente) {
        Mesa novaMesa = new Mesa(numero, cliente);
        if (cabeca == null) {
            cabeca = novaMesa;
            cabeca.proximo = cabeca;
        } else {
            Mesa atual = cabeca;
            while (atual.proximo != cabeca) {
                atual = atual.proximo;
            }
            atual.proximo = novaMesa;
            novaMesa.proximo = cabeca;
        }
        System.out.println("Mesa " + numero + " adicionada para o cliente " + cliente + ".");
    }

    public void exibirCardapio() {
        System.out.println("Cardápio:");
        for (String item : cardapio) {
            System.out.println("- " + item);
        }
    }

    public boolean itemValido(String descricao) {
        for (String item : cardapio) {
            if (item.equalsIgnoreCase(descricao)) {
                return true;
            }
        }
        return false;
    }

    public void adicionarPedido(int numeroMesa, String descricao, int quantidade, double total) {
        if (!itemValido(descricao)) {
            System.out.println("Item fora do cardápio: " + descricao);
            return;
        }

        Mesa mesa = buscarMesa(numeroMesa);
        if (mesa != null && mesa.status.equals("ocupada")) {
            ItemPedido novoItem = new ItemPedido(descricao, quantidade, total);
            Pedido novoPedido = new Pedido(novoItem);
            novoPedido.proximo = mesa.pedidos;
            mesa.pedidos = novoPedido;

            contagemItensPedidos.put(descricao, contagemItensPedidos.get(descricao) + quantidade);

            if (descricao.equalsIgnoreCase("rosquinha") && quantidade >= 2) {
                System.out.println("Um pedaço de rosquinha alegra muito. Dois pedaços causam tristeza.");
            }

            //fila de pedido
            adicionarPedidoNaFila(novoPedido);

            // Armazenando modificação
            adicionarModificacao("Pedido adicionado: " + descricao + " para mesa " + numeroMesa);

            System.out.println("Pedido adicionado na mesa " + numeroMesa + ".");
        } else {
            System.out.println("Mesa não encontrada ou está livre.");
        }
    }

    public void exibirItensMaisPedidos() {
        System.out.println("Itens mais pedidos:");
        contagemItensPedidos.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> {
                    if (entry.getValue() > 0) {
                        System.out.println(entry.getKey() + ": " + entry.getValue() + " pedidos");
                    }
                });
    }

    public void fecharConta(int numeroMesa) {
        Mesa mesa = buscarMesa(numeroMesa);
        if (mesa != null && mesa.status.equals("ocupada")) {
            double totalConta = 0;
            Pedido atual = mesa.pedidos;
            while (atual != null) {
                totalConta += atual.item.total;
                atual = atual.proximo;
            }
            System.out.println("Total a pagar na mesa " + numeroMesa + ": R$ " + totalConta);
            mesa.status = "livre";
            mesa.pedidos = null;
        } else {
            System.out.println("Mesa não encontrada ou já está livre.");
        }
    }

    public Mesa buscarMesa(int numero) {
        Mesa mesa = cabeca;
        do {
            if (mesa.numero == numero) {
                return mesa;
            }
            mesa = mesa.proximo;
        } while (mesa != cabeca);
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaRestaurante sistema = new SistemaRestaurante();

        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Adicionar mesa");
            System.out.println("2 - Adicionar pedido");
            System.out.println("3 - Fechar conta");
            System.out.println("4 - Exibir cardápio");
            System.out.println("5 - Exibir itens mais pedidos");
            System.out.println("6 - Exibir item mais rentável");
            System.out.println("7 - Processar próximo pedido");
            System.out.println("8 - Desfazer modificação");
            System.out.println("9 - Sair");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // consumir a nova linha

            if (opcao == 1) {
                System.out.print("Número da mesa: ");
                int numeroMesa = scanner.nextInt();
                scanner.nextLine(); // consumir a nova linha
                System.out.print("Nome do cliente: ");
                String cliente = scanner.nextLine();
                sistema.adicionarMesa(numeroMesa, cliente);
            } else if (opcao == 2) {
                System.out.print("Número da mesa: ");
                int numeroMesa = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Descrição do pedido: ");
                String descricao = scanner.nextLine();
                System.out.print("Quantidade: ");
                int quantidade = scanner.nextInt();
                scanner.nextLine(); 
                System.out.print("Total: ");
                double total = scanner.nextDouble();
                sistema.adicionarPedido(numeroMesa, descricao, quantidade, total);
            } else if (opcao == 3) {
                System.out.print("Número da mesa: ");
                int numeroMesa = scanner.nextInt();
                scanner.nextLine(); 
                sistema.fecharConta(numeroMesa);
            } else if (opcao == 4) {
                sistema.exibirCardapio();
            } else if (opcao == 5) {
                sistema.exibirItensMaisPedidos();
            } else if (opcao == 6) {
                sistema.exibirItemMaisRentavel();
            } else if (opcao == 7) {
                sistema.processarProximoPedido();
            } else if (opcao == 8) {
                sistema.desfazerModificacao();
            } else if (opcao == 9) {
                break;
            } else {
                System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }
}
