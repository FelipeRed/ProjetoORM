import jakarta.persistence.*;
import java.util.Scanner;

@Entity
public class Garcom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int salario;
    private String cpf;
    @OneToMany
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    public Garcom(){

    }

    public Garcom(Scanner scan){
        System.out.print("Insira o nome do garcom: ");
        this.nome = scan.nextLine();

        System.out.printf("Insira o salario do Garcom [%s]:", this.nome);
        this.salario = scan.nextInt();
        // Limpar scan depois do nextInt;
        scan.nextLine();

        System.out.printf("Insira o cpf do Garcom [%s]: ", this.nome);
        this.cpf = scan.nextLine();
    }
}
