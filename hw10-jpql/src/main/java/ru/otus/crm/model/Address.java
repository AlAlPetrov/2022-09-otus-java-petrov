package ru.otus.crm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "address")
@NoArgsConstructor
@Data
public class Address {
    @Id
    @SequenceGenerator(name = "idgen", initialValue = 1, allocationSize = 1, sequenceName = "address_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "id")
    private Long id;

    private String street;


    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }
}
