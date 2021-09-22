package com.szusta.meduva.util;

import com.szusta.meduva.model.Undeletable;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;

import javax.persistence.EntityNotFoundException;

public class UndeletableWithNameUtils {

    public static <T extends Undeletable> boolean canBeSaved(UndeletableWithNameRepository<T> repo, String nameOfUndeletable) {

        if (repo.existsByName(nameOfUndeletable)) {
            T undeletable = repo.findByName(nameOfUndeletable)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found with name: " + nameOfUndeletable));
            return undeletable.isDeleted();
        } else {
            return true;
        }
    }

    public static <T extends Undeletable> void markAsDeleted(UndeletableRepository<T> repo, Long idOfUndeletable) {

        T undeletable = repo.findById(idOfUndeletable)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id : " + idOfUndeletable));

        undeletable.markAsDeleted();
        repo.save(undeletable);
    }
}
