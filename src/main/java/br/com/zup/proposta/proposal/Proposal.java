package br.com.zup.proposta.proposal;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String document;

    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProposalStatus status = ProposalStatus.PEDING;

    @Embedded
    private Address address;

    private String cardId;

    Proposal(@NotBlank String document, @NotBlank String email, @NotBlank String name, @NotNull @Positive BigDecimal salary, Address address) {
        this.document = document;
        this.email = email;
        this.name = name;
        this.salary = salary;
        this.address = address;
    }

    @Deprecated
    Proposal() {
    }

    public Long getId() {
        return id;
    }

    public String getDocument() {
        return document;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void updateStatus(ProposalStatus newStatus){
        this.status = newStatus;
    }

    public Address getAddress() {
        return address;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "Proposal{" +
                "id=" + id +
                ", document='" + document + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", status=" + status +
                ", address=" + address +
                ", cardId='" + cardId + '\'' +
                '}';
    }

    public ProposalStatus getStatus() {
        return this.status;
    }
}
