package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Biometry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Card card;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Lob
    private String base64;


    public Biometry(Card card, String base64) {
        this.card = card;
        this.base64 = base64;
    }

    public Biometry() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Biometry biometry = (Biometry) o;
        return card.equals(biometry.card) && base64.equals(biometry.base64);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, base64);
    }

    public Long getId() {
        return id;
    }

    public String getBase64() {
        return base64;
    }

    public Card getCard() {
        return card;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
