package com.szusta.meduva.repository.undeletable;

import com.szusta.meduva.model.common.Undeletable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface UndeletableRepository<T extends Undeletable> extends JpaRepository<T, Long> {

    @Query("select e from #{#entityName} as e where e.deleted = false")
    List<T> findAllUndeleted();

    @Query("select e from #{#entityName} as e where e.deleted = true")
    List<T> findAllDeleted();
}
