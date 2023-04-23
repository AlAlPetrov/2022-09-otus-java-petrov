package ru.otus.domain;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
@Getter
@Setter
@NoArgsConstructor
public class Phone {
    @Id
    private Long id;
    @Nonnull
    private String number;

    public Phone(String number) {
        this(null, number);
    }

    @PersistenceCreator
    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}
