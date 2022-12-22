package ru.otus.crm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "phone")
@NoArgsConstructor
@Data
public class Phone {
    @Id
    @SequenceGenerator(name = "idgen", initialValue = 1, allocationSize = 1, sequenceName = "phone_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "id")
    private Long id;

    private String number;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}
