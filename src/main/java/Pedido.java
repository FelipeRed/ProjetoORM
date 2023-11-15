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

    public Pedido() {

    }

    public Pedido(Prato prato, Mesa mesa) {
        this.prato = prato;
        this.mesa = mesa;
    }

    public Prato getPrato() {
        return prato;
    }
}
