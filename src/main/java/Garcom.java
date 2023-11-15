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


    /* =-=-=-=-=-=-=-=-= Ações com Banco de Dados =-=-=-=-=-=-=-=-= */

    public void salvar() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(this);
        em.getTransaction().commit();

        em.close();
        emf.close();

        Main.inputString("Garcom salvo com sucesso!\nPressione ENTER para continuar.");
    }

    public void atualizar() {
        Main.limparTela();
        int op = 0;
        while (op < 1 || op > 2) {
            op = Main.inputInt("" +
                    "\n1- Editar nome do Garcom           ->  ATUAL: " + this.nome +
                    "\n2- Editar salario do Garcom        ->  ATUAL: " + this.salario +
                    "\nInsira a opção desejada: ");

            switch (op) {
                case 1 -> this.setNome(Main.inputString("Insira o novo nome do garcom: "));
                case 2 -> this.setSalario(Main.inputInt("Insira o novo salário do garcom: "));
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

        Main.inputString("Garcom editado com sucesso!\nPressione ENTER para continuar.");
    }

    public void apagar() {
        Main.limparTela();
        System.out.println("GARÇOM A SER EXCLUÍDO:");
        this.print();
        int confirmacao = Main.inputInt("1- Sim \n2- Não \nCerteza que deseja excluir esse garcom: ");
        if (confirmacao == 1) {

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadePU");
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();

            Garcom garcom = em.find(Garcom.class, this.id);
            em.remove(garcom);

            em.getTransaction().commit();
            em.close();
            emf.close();
            Main.inputString("Garcom deletado do banco com sucesso!\nPressione ENTER para continuar.");

        }
        else {
            Main.inputString("Operação cancelada.\nPressione ENTER para continuar.");
        }
    }

    /* =-=-=-=-=-=-=-=-= SETTER'S =-=-=-=-=-=-=-=-= */
    public void setNome(String nome){
        this.nome = nome;
    }

    public void setSalario(int salario){
        this.salario = salario;
    }

    /* =-=-=-=-=-=-=-=-= GETTER'S =-=-=-=-=-=-=-=-= */
    public String getNome(){
        return this.nome;
    }

    /* =-=-=-=-=-=-=-=-= PRINT =-=-=-=-=-=-=-=-= */
    public void print() {
        System.out.println("Id: " + this.id);
        System.out.println("Nome: " + this.nome);
        System.out.printf("Salario: %d\n", this.salario);
        System.out.println("Cpf: " + this.cpf);
        System.out.println("---------------------------------");
    }

}
