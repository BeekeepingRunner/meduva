package com.szusta.meduva.repository;

import com.szusta.meduva.model.Deletable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeletableRepository<T extends Deletable> extends JpaRepository<T, Long> {

    @Query("select d from Deletable d where d.deleted = false")
    List<T> findAllUndeleted();
}
