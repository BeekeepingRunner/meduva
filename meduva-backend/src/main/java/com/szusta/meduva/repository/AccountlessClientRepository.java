package com.szusta.meduva.repository;

import com.szusta.meduva.model.AccountlessClient;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountlessClientRepository extends UndeletableRepository<AccountlessClient> {

}
