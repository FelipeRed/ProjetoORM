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
                case 3 -> cancelarPedido();
                case 4 -> fecharMesa();
                case 5 -> verCardapio();
                case 6 -> criarGarcom();
                case 7 -> editarGarcom();
                case 8 -> deletarGarcom();
                case 9 -> criarMesa();
                case 10 -> mudarGarcomMesa();
                case 11 -> deletarMesa();
                case 12 -> criarPrato();
                case 13 -> editarPrato();
                case 14 -> deletarPrato();
                case 0 -> System.out.println("Volte sempre!");
                default -> System.out.println("Insira uma opção válida.");
            }
        }
    }



    /* =-=-=-=-=-=-=-=-=-=-=-= SELECT'S =-=-=-=-=-=-=-=-=-=-=-= */
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
    public static void verGarcons(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT g FROM Garcom g");
        List<Garcom> garcons = query.getResultList();
        if (garcons.isEmpty()) {
            System.out.println("Não há garçons cadastrados!");
        }
        else {
            limparTela();
            System.out.println("---/--- Lista de Garçons ---/---");
            for (Garcom garcom : garcons) {
                garcom.print();
            }
        }
        em.close();
        emf.close();
    }
    public static Prato selecionarPratoPeloId(String acao) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        verCardapio();

        Long idEscolhido = inputLong("Insira o ID do prato que deseja " + acao + ": ");
        Prato prato = em.find(Prato.class, idEscolhido);  // Procurando o prato desejado

        em.close();
        emf.close();
        return prato;
    }
    public static Mesa selecionarMesaPeloId(String tipo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        Query query;
        if (tipo.equals("ocupadas")) query = em.createQuery("SELECT m FROM Mesa m WHERE m.ocupado = true");
        else if (tipo.equals("livres")) query = em.createQuery("SELECT m FROM Mesa m WHERE m.ocupado = false");
        else query = em.createQuery("SELECT m FROM Mesa m");

        List<Mesa> listaMesas = query.getResultList();

        if (listaMesas != null) {
            limparTela();
            System.out.println("--- MESAS ---");
            for (Mesa m : listaMesas) {
                System.out.println("- Mesa " + m.getNumMesa());
            }
            em.close();
            emf.close();

            Long idMesa = inputLong("Insira o número da mesa que deseja selecionar: ");
            return em.find(Mesa.class, idMesa);
        }
        return null;
    }
    public static Garcom selecionarGarcomPeloId(String id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        Garcom garcom = em.find(Garcom.class, id);

        em.close();
        emf.close();

        return garcom;
    }
    public static Pedido selecionarPedidoPeloId(String acao) {
        Mesa mesa = selecionarMesaPeloId("ocupadas");

        if (mesa != null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
            EntityManager em = emf.createEntityManager();

            Query query = em.createQuery("SELECT p FROM Pedido p WHERE p.mesa_id = :numMesa");
            query.setParameter("numMesa", mesa.getNumMesa());

            List<Pedido> pedidos = query.getResultList();

            if (pedidos.isEmpty()) {
                inputString("A mesa não possui pedidos. Pressione ENTER para continuar.");
                return null;
            }
            else {
                limparTela();
                System.out.println("---/--- PEDIDOS MESA " + mesa.getNumMesa() + " ---/---");
                for (Pedido p : pedidos) {
                    p.print();
                }
            }
            Long idEscolhido = inputLong("Insira o ID do pedido que deseja " + acao + ": ");
            Pedido pedido = em.find(Pedido.class, idEscolhido);

            em.close();
            emf.close();
            return pedido;
        }
        inputString("Mesa não encontrada. Pressione ENTER para continuar.");
        return null;
    }


    /* =-=-=-=-=-=-=-=-=-=-=-= PRINT'S =-=-=-=-=-=-=-=-=-=-=-= */
    public static void limparTela() {
        for (int i=0; i<50; i++) {
            System.out.println();
        }
    }
    public static int menu() {
        limparTela();
        System.out.println("""
                ---/--- USO NORMAL ---/---
                1  - Ocupar mesa;
                2  - Fazer um pedido;
                3  - Cancelar um pedido;
                4  - Fechar mesa;
                5  - Ver cardápio;

                """);

        System.out.println("""
                ---/--- USO DE ADMIN ---/---
                 6 - Adicionar Garçom;
                 7 - Atualizar Garçom;
                 8 - Apagar Gaçom;
                 9 - Adicionar Mesa;
                10 - Atualizar Mesa;
                11 - Apagar Mesa;
                12 - Adicionar Prato
                13 - Atualizar Prato;
                14 - Apagar Prato;

                 0 - Para sair""");

        return inputInt("\nInsira a ação desejada: ");
    }


    /* =-=-=-=-=-=-=-=-=-=-=-= INPUT'S =-=-=-=-=-=-=-=-=-=-=-= */
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


    /* =-=-=-=-=-=-=-=-=-=-=-= CRUD PEDIDO =-=-=-=-=-=-=-=-=-=-=-= */
    public static void fazerPedido() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        Mesa mesa = selecionarMesaPeloId("ocupadas");

        verCardapio();
        System.out.println("Exemplo: 1, 1, 4, 5");
        String pedidoStr = inputString("Insira o ID dos pratos que deseja pedir separados por vírgula: ");
        String[] pedidoSplit = pedidoStr.split(", ");

        for (String idStr : pedidoSplit) {
            Long idPrato = Long.parseLong(idStr.trim());  // Convertendo o ID do pedido de String para Long

            Prato prato = em.find(Prato.class, idPrato);
            Pedido pedido = new Pedido(prato, mesa);
            pedido.salvar();
        }

        em.close();
        emf.close();
    }
    public static void cancelarPedido() {
        Pedido pedido = selecionarPedidoPeloId("cancelar");
        if (pedido != null) pedido.apagar();
        else inputString("Pedido não encontrada.");
    }


    /* =-=-=-=-=-=-=-=-=-=-=-= CRUD MESA =-=-=-=-=-=-=-=-=-=-=-= */
    public static void criarMesa(){
        limparTela();
        Mesa mesa = new Mesa("");
        mesa.salvar();
    }
    public static void deletarMesa() {
        Mesa mesa = selecionarMesaPeloId("todas");
        if (mesa != null) mesa.apagar();
        else inputString("Mesa não encontrada.");
    }
    public static void ocuparMesa() {
        Mesa mesa = selecionarMesaPeloId("livres");
        if (mesa == null) inputString("Mesa não encontrada.\nPressione ENTER para continuar.");
        else mesa.ocupar();
    }
    public static void mudarGarcomMesa() {
        Mesa mesa = selecionarMesaPeloId("todas");

        if (mesa != null) {
            verGarcons();
            System.out.println("Garçom atual responsável por essa mesa: " + mesa.getGarcom().getNome());
            String id = inputString("Insira o ID do garcom que deseja alocar nesta mesa: ");
            Garcom garcom = selecionarGarcomPeloId(id);

            if (garcom != null) {
                mesa.setGarcom(garcom);
                limparTela();
                inputString("Operação realizada com sucesso.\nPressione ENTER para continuar.");
                return;
            }
            limparTela();
            inputString("Garçom não encontrado.\nPressione ENTER para continuar.");
        }

        limparTela();
        inputString("Garçom não encontrado.\nPressione ENTER para continuar.");
    }
    public static void fecharMesa() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        Mesa mesa = selecionarMesaPeloId("ocupadas");

        if (mesa != null) {
            // Pegando todos os pedidos da mesa
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
            inputString("Mesa fechada com sucesso!\n" +
                    "VALOR TOTAL DOS PEDIDOS: R$" + total +
                    "Pressione ENTER para continuar.");
        }
        else {
            inputString("Mesa não encontrada.");
        }
    }


    /* =-=-=-=-=-=-=-=-=-=-=-= CRUD PRATO =-=-=-=-=-=-=-=-=-=-=-= */
    public static void criarPrato() {
        limparTela();
        Prato prato = new Prato(new Scanner(System.in));
        prato.salvar();
    }
    public static void editarPrato() {
        Prato prato = selecionarPratoPeloId("editar");
        if (prato == null) {
            limparTela();
            System.out.println("Prato não encontrado...");
        }
        else {
            prato.atualizar();
        }
    }
    public static void deletarPrato() {
        Prato prato = selecionarPratoPeloId("deletar");
        if (prato == null) {
            System.out.println("Prato não encontrado...");
        }
        else {
            prato.apagar();
        }
    }


    /* =-=-=-=-=-=-=-=-=-=-=-= CRUD GARÇOM =-=-=-=-=-=-=-=-=-=-=-= */
    public static void criarGarcom(){
        limparTela();
        Garcom garcom = new Garcom(new Scanner(System.in));
        garcom.salvar();
    }
    public static void editarGarcom() {
        verGarcons();
        String id = inputString("Insira o ID do garcom que deseja editar: ");
        Garcom garcom = selecionarGarcomPeloId(id);

        if (garcom == null) {
            limparTela();
            System.out.println("Prato não encontrado...");
        } else {
            garcom.atualizar();
        }
    }
    public static void deletarGarcom() {
        verGarcons();
        String id = inputString("Insira o ID do garcom que deseja remover: ");
        Garcom garcom = selecionarGarcomPeloId(id);

        if (garcom == null) {
            limparTela();
            System.out.println("Garcom não encontrado...");
        } else {
            garcom.apagar();
        }
    }
}
