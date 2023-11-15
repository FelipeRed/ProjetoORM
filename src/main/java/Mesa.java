import jakarta.persistence.*;

@Entity
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numMesa;
    private boolean ocupado;
    @ManyToOne
    @JoinColumn(name = "garcom_id")
    private Garcom garcom;

    public Mesa() {}

    public Mesa(String a) {
        this.ocupado = false;
    }

    public Long getNumMesa() {
        return numMesa;
    }

    public Garcom getGarcom() {
        return garcom;
    }

    public void setGarcom(Garcom garcom) {
        this.garcom = garcom;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(this);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public void ocupar() {
        this.ocupado = true;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(this);
        em.getTransaction().commit();
        em.close();
        emf.close();

        Main.limparTela();
        Main.inputString("Mesa ocupada com sucesso! Pressione ENTER para continuar.");
    }

    public void liberar() {
        this.ocupado = false;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(this);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public void apagar() {
        Main.limparTela();
        System.out.println("MESA A SER EXCLUÍDA: Mesa " + numMesa);
        int confirmacao = Main.inputInt("1- Sim \n2- Não \nCerteza que deseja excluir essa mesa: ");
        if (confirmacao == 1) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Mesa mesa = em.find(Mesa.class, this.numMesa);
            em.remove(mesa);
            em.getTransaction().commit();
            em.close();
            emf.close();
            Main.inputString("Mesa deletada do banco com sucesso!\nPressione ENTER para continuar.");
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
}
