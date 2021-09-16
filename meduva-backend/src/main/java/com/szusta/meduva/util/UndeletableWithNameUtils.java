package com.szusta.meduva.util;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Undeletable;
import com.szusta.meduva.repository.UndeletableWithNameRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

public class UndeletableWithNameUtils {

    public static <T extends Undeletable> boolean canBeSaved(UndeletableWithNameRepository<T> repo, String name) {

        if (repo.existsByName(name)) {
            T undeletable = repo.findByName(name)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found with name: " + name));
            return undeletable.isDeleted();
        } else {
            return true;
        }
    }

    public static <T extends Undeletable> void markAsDeleted(UndeletableWithNameRepository<T> repo, Long id) {

        T undeletable = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id : " + id));

        undeletable.markAsDeleted();
        repo.save(undeletable);
    }
}
