package com.github.egorklimov.repository;

import com.github.egorklimov.data.transaction.Transaction;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;

import java.util.Collections;
import java.util.function.Predicate;

public interface CommonCRUDTestSuite<T> {

    T valueToPersist();
    String updateQuery();
    Predicate<T> postUpdateCondition();

    @SneakyThrows
    default void run(PanacheRepository<T> repository, Transaction transaction) {
        Assertions.assertDoesNotThrow(
                () -> transaction.execute(repository::deleteAll)
        );

        T valueToPersist = valueToPersist();
        Assertions.assertDoesNotThrow(
                () -> transaction.execute(() -> repository.persist(valueToPersist))
        );
        Assertions.assertEquals(
                Collections.singletonList(valueToPersist),
                repository.listAll(),
                "Persisted value not found"
        );

        int affected = transaction.execute(() -> repository.update(updateQuery()));

        Assertions.assertEquals(
                1,
                affected,
                "Failed to update value"
        );

        transaction.execute(() ->
            repository.listAll().forEach(
                    v -> Assertions.assertTrue(
                            postUpdateCondition().test(v),
                            "Post update condition failed"
                    )
            )
        );

        // Check transaction
        Assertions.assertDoesNotThrow(
                () -> transaction.execute(() -> {
                    repository.deleteAll();
                    Assertions.assertTrue(
                            repository.listAll().isEmpty(),
                            "Not all records deleted"
                    );
                })
        );
    }
}
