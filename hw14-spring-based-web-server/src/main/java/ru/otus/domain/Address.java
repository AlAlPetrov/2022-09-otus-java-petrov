package ru.otus.domain;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Nonnull
    private String street;

    @PersistenceCreator
    public Address(String street) {
        this.street = street;
    }
}
