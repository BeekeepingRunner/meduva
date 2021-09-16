package com.szusta.meduva.repository;

import com.szusta.meduva.model.Undeletable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface UndeletableWithNameRepository<T extends Undeletable> extends UndeletableRepository<T> {

    boolean existsByName(String name);
    Optional<T> findByName(String name);
}
