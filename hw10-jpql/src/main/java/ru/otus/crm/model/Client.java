package ru.otus.crm.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@NoArgsConstructor
@Data
public class Client implements Cloneable {
    @Id
    @SequenceGenerator(name = "idgen", initialValue = 1, allocationSize = 1, sequenceName = "client_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "client")
    private Address address;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "client", referencedColumnName = "id")
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name, this.address, this.phones);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
