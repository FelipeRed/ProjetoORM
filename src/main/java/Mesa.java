import jakarta.persistence.*;

@Entity
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numMesa;
    private boolean ocupado;
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    public Mesa() {

    }

    public Mesa(Restaurante restaurante) {
        this.ocupado = false;
        this.restaurante = restaurante;
    }

    public Long getNumMesa() {
        return numMesa;
    }

    public boolean isOcupado() {
        return ocupado;
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

    public Restaurante getRestaurante() {
        return restaurante;
    }
}
