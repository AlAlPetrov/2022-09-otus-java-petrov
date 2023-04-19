package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

@Table("tariffs")
public class Tariff {
    @Id
    private final Long id;

    @NonNull
    private final Long type;

    @NonNull
    private final String name;

    @NonNull
    private final Long price;

    @NonNull
    private final Long initialValue;

    @PersistenceCreator
    protected Tariff(Long id,
                     @NonNull Long type,
                     @NonNull String name,
                     @NonNull Long price,
                     @NonNull Long initialValue) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.price = price;
        this.initialValue = initialValue;
    }

    public Tariff(@NonNull Long type,
                  @NonNull String name,
                  @NonNull Long price,
                  @NonNull Long initialValue) {
        this(null, type, name, price, initialValue);
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public Long getPrice() {
        return price;
    }

    @NonNull
    public Long getInitialValue() {
        return initialValue;
    }

    @NonNull
    public Long getType() { return type; }

    @Override
    public String toString() {
        return "Tariff{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }
}
