import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        int acao = -1;
        while (acao != 0) {
            acao = menu();
            switch (acao) {
                case 1 -> ocuparMesa();
                case 2 -> fazerPedido();
                case 3 -> fecharMesa();
                case 4 -> verCardapio();
                case 0 -> System.out.println("Volte sempre!");
                default -> System.out.println("Insira uma opção válida.");
            }
        }
    }

    public static void ocuparMesa() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        Long idMesa = selecionarMesaPeloID(false);
        Mesa mesa = em.find(Mesa.class, idMesa);
        mesa.ocupar();

        em.close();
        emf.close();
    }

    public static void fazerPedido() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        Long idMesa = selecionarMesaPeloID(true);
        Mesa mesa = em.find(Mesa.class, idMesa);

        verCardapio();
        System.out.println("Exemplo: 1, 1, 4, 5");
        String pedidoStr = inputString("Insira o ID dos pratos que deseja pedir separados por vírgula: ");
        String[] pedidoSplit = pedidoStr.split(", ");

        for (String idStr : pedidoSplit) {
            Long idPrato = Long.parseLong(idStr.trim());  // Convertendo o ID do pedido de String para Long

            Prato prato = em.find(Prato.class, idPrato);
            Pedido pedido = new Pedido(prato, mesa);

            em.getTransaction().begin();
            em.persist(pedido);
            em.getTransaction().commit();
        }

        em.close();
        emf.close();
    }

    public static void fecharMesa() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        Long idMesa = selecionarMesaPeloID(true);
        Mesa mesa = em.find(Mesa.class, idMesa);

        // Pegando todos os pedidos das mesas
        Query query = em.createQuery("SELECT p FROM Pedido p WHERE p.mesa = :mesa");
        query.setParameter("mesa", mesa);
        List<Pedido> pedidos = query.getResultList();

        // Somando o valor total dos pedidos da mesa
        float total = 0;
        for (Pedido pedido : pedidos) {
            Prato prato = pedido.getPrato();
            total += prato.getPreco();
        }

        // Removendo os pedidos da mesa e liberando ela
        em.getTransaction().begin();
        for (Pedido pedido : pedidos) {
            em.remove(pedido);
        }
        em.getTransaction().commit();
        mesa.liberar();
        em.merge(mesa);

        em.close();
        emf.close();

        limparTela();
        Restaurante restaurante = em.find(Restaurante.class, 1);
        restaurante.aumentarFaturamento(total);
        inputString("Mesa fechada com sucesso!\n" +
                "VALOR TOTAL DOS PEDIDOS: R$" + total +
                "Pressione ENTER para continuar.");
    }

    public static void criarPrato() {
        limparTela();
        Prato prato = new Prato(new Scanner(System.in));
        prato.salvar();
    }

    public static void editarPrato() {
        Prato prato = selecionarPratoPeloID("editar");
        if (prato == null) {
            limparTela();
            System.out.println("Prato não encontrado...");
        }
        else {
            prato.atualizarPratoNoBanco();
        }
    }

    public static void deletarPrato() {
        Prato prato = selecionarPratoPeloID("deletar");
        if (prato == null) {
            System.out.println("Prato não encontrado...");
        }
        else {
            prato.apagarPratoDoBanco();
        }
    }

    public static void verCardapio() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("SELECT p FROM Prato p");
        List<Prato> cardapio = query.getResultList();

        if (cardapio.isEmpty()) {
            System.out.println("O cardápio está vazio.");
        }
        else {
            limparTela();
            System.out.println("---/--- CARDÁPIO ---/---");
            for (Prato prato : cardapio) {
                prato.print();
            }
        }

        em.close();
        emf.close();
    }

    public static Prato selecionarPratoPeloID(String acao) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        verCardapio();

        Long idEscolhido = inputLong("Insira o ID do prato que deseja " + acao + ": ");
        Prato prato = em.find(Prato.class, idEscolhido);  // Procurando o prato desejado

        em.close();
        emf.close();
        return prato;
    }

    public static Long selecionarMesaPeloID(boolean ocupadas) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        Query query;
        if (ocupadas) query = em.createQuery("SELECT m FROM Mesa m WHERE m.ocupado = true");
        else query = em.createQuery("SELECT m FROM Mesa m WHERE m.ocupado = false");

        List<Mesa> listaMesas = query.getResultList();

        if (listaMesas == null) {
            System.out.println("Nenhum resultado encontrado.");
        }
        else {
            limparTela();
            System.out.println("--- MESAS ---");
            for (Mesa m : listaMesas) {
                System.out.println("- Mesa " + m.getNumMesa());
            }
            em.close();
            emf.close();

            return inputLong("Insira o número da mesa que deseja selecionar: ");
        }
        return null;
    }

    public static int menu() {
        limparTela();
        return inputInt("" +
                "---/--- MENU USUÁRIO ---/---\n" +
                "1- Ocupar mesa;\n" +
                "2- Fazer um pedido;\n" +
                "3- Fechar mesa;\n" +
                "4- Ver cardápio;\n" +
                "0- Encerrar programa;\n" +
                "Insira a ação desejada: ");
    }

    public static int menuGerenciador(){
        limparTela();
        return inputInt("" +
                "---/--- MENU PRATO ---/---" +
                "\n3- Criar novo prato;" +
                "\n4- Editar um prato;" +
                "\n5- Deletar um prato;" +
                "\n7- Voltar" +
                "\nInsira a ação desejada: ");
    }

    public static int inputInt(String msg) {
        Scanner scan = new Scanner(System.in);
        System.out.print(msg);
        return scan.nextInt();
    }

    public static float inputFloat(String msg) {
        Scanner scan = new Scanner(System.in);
        System.out.print(msg);
        return scan.nextFloat();
    }

    public static Long inputLong(String msg) {
        Scanner scan = new Scanner(System.in);
        System.out.print(msg);
        return scan.nextLong();
    }

    public static String inputString(String msg) {
        Scanner scan = new Scanner(System.in);
        System.out.print(msg);
        return scan.nextLine();
    }

    public static void limparTela() {
        for (int i=0; i<50; i++) {
            System.out.println();
        }
    }
}
