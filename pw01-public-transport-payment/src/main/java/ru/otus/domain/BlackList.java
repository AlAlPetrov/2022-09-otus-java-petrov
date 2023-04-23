package ru.otus.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

@Table("blacklist")
@Getter
public class BlackList {
    @Id
    private final Long id;

    @NonNull
    @Column("account")
    private final Long accountId;

    @NonNull
    private final String reason;

    public static BlackList fromRequest(BlackListRequest blackListRequest) {
        return new BlackList(blackListRequest.accountId(),
                blackListRequest.reason());
    }

    @PersistenceCreator
    protected BlackList(Long id, @NonNull Long accountId, @NonNull String reason) {
        this.id = id;
        this.accountId = accountId;
        this.reason = reason;
    }

    public BlackList(@NonNull Long accountId, @NonNull String reason) {
        this(null, accountId, reason);
    }

    @Override
    public String toString() {
        return "TariffType{" +
                "id=" + id +
                ", account='" + accountId +
                '}';
    }
}
