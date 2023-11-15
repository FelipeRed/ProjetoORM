import jakarta.persistence.*;

import java.util.Scanner;

@Entity
public class Prato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private float preco;
    private String ingredientes;

    public Prato() {

    }

    public Prato(Scanner scan) {
        System.out.print("Insira o nome do prato: ");
        this.nome = scan.nextLine();

        System.out.print("Insira o preço desse prato: ");
        this.preco = scan.nextFloat();
        // Limpar scan depois do nextFloat;
        scan.nextLine();

        System.out.print("Insira os ingredientes desse prato: ");
        this.ingredientes = scan.nextLine();
    }

    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void print() {
        System.out.println("ID: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Preço: " + preco);
        System.out.println("Ingredientes: " + ingredientes);
        System.out.println("---------------------------------");
    }

    public void atualizar() {
        Main.limparTela();
        int op = 0;
        while (op < 1 || op > 3) {
            op = Main.inputInt("" +
                    "\n1- Editar nome do prato           ->  ATUAL: " + nome +
                    "\n2- Editar preço do prato          ->  ATUAL: " + preco +
                    "\n3- Editar ingreditentes do prato  ->  ATUAL: " + ingredientes +
                    "\nInsira a opção desejada: ");

            switch (op) {
                case 1 -> this.setNome(Main.inputString("Insira o novo nome do prato: "));
                case 2 -> this.setPreco(Main.inputFloat("Insira o novo preço do prato: "));
                case 3 -> this.setIngredientes(Main.inputString("Insira os novos ingredientes do prato: "));
                default -> System.out.println("Opção inválida...");
            }
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(this);
        em.getTransaction().commit();
        em.close();
        emf.close();
        Main.inputString("Prato editado com sucesso!");
    }

    public void apagar() {
        Main.limparTela();
        System.out.println("PRATO A SER EXCLUÍDO:");
        this.print();
        int confirmacao = Main.inputInt("1- Sim \n2- Não \nCerteza que deseja excluir esse prato: ");
        if (confirmacao == 1) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Prato prato = em.find(Prato.class, this.id);
            em.remove(prato);
            em.getTransaction().commit();
            em.close();
            emf.close();
            Main.inputString("Prato deletado do banco com sucesso!\nPressione ENTER para continuar.");
        }
        else {
            Main.inputString("Operação cancelada.\nPressione ENTER para continuar.");
        }
    }

    public void salvar() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(this);
        em.getTransaction().commit();

        em.close();
        emf.close();
        Main.inputString("Prato criado no banco com sucesso!\nPressione ENTER para continuar.");
    }
}
