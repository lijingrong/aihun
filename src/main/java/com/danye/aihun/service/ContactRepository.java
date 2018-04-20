package com.danye.aihun.service;

import com.danye.aihun.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact,String>{


}
