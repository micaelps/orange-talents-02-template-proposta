package br.com.zup.proposta.newproposal;
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
    @Column(nullable = false)
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

    Proposal(@NotBlank String document, @NotBlank String email, @NotBlank String name, @NotNull @Positive BigDecimal salary) {
        this.document = document;
        this.email = email;
        this.name = name;
        this.salary = salary;
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
}
