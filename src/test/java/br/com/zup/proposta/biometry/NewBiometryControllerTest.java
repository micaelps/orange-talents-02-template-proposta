package br.com.zup.proposta.biometry;

import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.card.CardResponse;
import br.com.zup.proposta.proposal.*;
import br.com.zup.proposta.utils.Requester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NewBiometryControllerTest {

    Card card;

    @Autowired
    AllBiometrics allBiometrics;

    @Autowired
    Requester requester;

    @PersistenceContext
    EntityManager manager;

    @BeforeEach
    void setup(){
        Proposal proposal = new Proposal("70810536455", "email@email.com.br", "name", BigDecimal.valueOf(1000),new Address("rua", 222, "45080"));
        card = new Card("123321", "holder", LocalDateTime.now(), BigDecimal.valueOf(10000), proposal);
        manager.persist(proposal);
        manager.persist(card);
    }

    @Test
    @DisplayName("Should create new biometry and return 200")
    void add_new_biometry() throws Exception {
        String biometry = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4";
        NewBiometryRequest request = new NewBiometryRequest(biometry);

        manager = Mockito.mock(EntityManager.class);
        Mockito.when(manager.find(Card.class, 1L)).thenReturn(card);
        ResultActions post = requester.post("/api/biometrics/1", request).andExpect(status().isCreated());

        Iterable<Biometry> all = allBiometrics.findAll();
        List<Biometry> collect = StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
        Biometry savedBiometry = collect.get(0);

        post.andExpect(redirectedUrlPattern("http://*/api/biometrics/" + savedBiometry.getId()));

        Assertions.assertEquals(collect.size(), 1);
        Assertions.assertEquals(savedBiometry.getBase64(), biometry);
    }


    @Test
    @DisplayName("Should not create new invalid biometry")
    void add_new_invalid_biometry() throws Exception {
        NewBiometryRequest request = new NewBiometryRequest(null);
        requester.post("/api/biometrics/1", request).andExpect(status().isBadRequest());
        Iterable<Biometry> all = allBiometrics.findAll();
        List<Biometry> collect = StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
        Assertions.assertEquals(collect.size(), 0);
    }

    @Test
    @DisplayName("Should not create new biometry with invalid card")
    void add_new_biometry_invalid_card() throws Exception {
        String biometry = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4";
        NewBiometryRequest request = new NewBiometryRequest(biometry);
        requester.post("/api/biometrics/"+Long.MAX_VALUE, request).andExpect(status().isNotFound());
        Iterable<Biometry> all = allBiometrics.findAll();
        List<Biometry> collect = StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
        Assertions.assertEquals(collect.size(), 0);
    }



    @DisplayName("oxe")
    @Test
    void get_biometry() throws Exception {
        String biometry = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4";
        NewBiometryRequest request = new NewBiometryRequest(biometry);
        Biometry savedBiometry = allBiometrics.save(request.toModel(card));
        NewBiometryResponse response = NewBiometryResponse.of(savedBiometry, CardResponse.of(savedBiometry.getCard()));
        requester.get("/api/biometrics/{id}", savedBiometry.getId())
                 .andExpect(status().isOk())
                 .andExpect(content().json(requester.toJson(response)));
    }






}