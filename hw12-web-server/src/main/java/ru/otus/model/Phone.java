package ru.otus.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "phone")
@Setter
@NoArgsConstructor
@Data
public class Phone {
    @Id
    @SequenceGenerator(name = "idgen", initialValue = 1, allocationSize = 1, sequenceName = "phone_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    private String number;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}
