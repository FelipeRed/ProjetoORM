import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cnpj;
    private int numClientes;
    private float faturamento;

    public Restaurante() {

    }

    public Restaurante(String nome, String cnpj, int numClientes) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.numClientes = numClientes;
        this.faturamento = 0;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public int getNumClientes() {
        return numClientes;
    }

    public float getFaturamento() {
        return faturamento;
    }

    public void aumentarFaturamento(Float faturamento) {
        this.faturamento += faturamento;
    }
}
