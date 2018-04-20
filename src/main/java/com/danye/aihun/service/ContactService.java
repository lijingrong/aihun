package com.danye.aihun.service;

import com.danye.aihun.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }
}
