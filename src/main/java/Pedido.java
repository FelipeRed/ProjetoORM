import jakarta.persistence.*;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "prato_id")
    private Prato prato;
    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    public Pedido() { }

    public Pedido(Prato prato, Mesa mesa) {
        this.prato = prato;
        this.mesa = mesa;
    }

    public Prato getPrato() {
        return prato;
    }

    public void apagar() {
        Main.limparTela();
        System.out.println("PEDIDO A SER EXCLUÍDO:");
        System.out.println(prato.getNome());

        int confirmacao = Main.inputInt("1- Sim \n2- Não \nCerteza que deseja cancelar esse pedido: ");
        if (confirmacao == 1) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Pedido pedido = em.find(Pedido.class, this.id);
            em.remove(pedido);
            em.getTransaction().commit();
            em.close();
            emf.close();
            Main.inputString("Pedido deletado do banco com sucesso!\nPressione ENTER para continuar.");
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
    }

    public void print() {
        System.out.println("ID: " + id);
        System.out.println("Pedido: " + prato.getNome() + " R$ " + prato.getPreco());
    }
}
