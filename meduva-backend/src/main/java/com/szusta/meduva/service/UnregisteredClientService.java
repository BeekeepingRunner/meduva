package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.UnregisteredClient;
import com.szusta.meduva.repository.UnregisteredClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnregisteredClientService {

    UnregisteredClientRepository clientRepository;

    @Autowired
    public UnregisteredClientService(UnregisteredClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public UnregisteredClient findById(Long id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("Unregistered client not found with id: " + id));
    }

    public UnregisteredClient save(UnregisteredClient client) {
        if (alreadyExists(client)) {
            throw new AlreadyExistsException("That client already exists");
        } else {
            return clientRepository.save(client);
        }
    }

    private boolean alreadyExists(UnregisteredClient client) {
        return clientRepository.existsByName(client.getName())
                && clientRepository.existsBySurname(client.getSurname())
                && clientRepository.existsByPhoneNumber(client.getPhoneNumber());
    }

    public List<UnregisteredClient> findAll() {
        return clientRepository.findAll();
    }

    public List<UnregisteredClient> findAllUndeleted() {
        return this.clientRepository.findAllUndeleted();
    }

    public List<UnregisteredClient> findAllDeleted() {
        return this.clientRepository.findAllDeleted();
    }

}
