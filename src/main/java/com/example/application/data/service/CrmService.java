package com.example.application.data.service;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Status;
import com.example.application.data.entity.Contact;
import com.example.application.data.repository.CompanyRepository;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.StatusRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CrmService {
    private final ContactRepository contactRepository;
    private StatusRepository statusRepository;
    private CompanyRepository companyRepository;

    public CrmService(ContactRepository contactRepository,CompanyRepository companyRepository , StatusRepository statusRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;

    }
    public List<Contact> findAllContacts(String filterText)
    {
        if(filterText == null || filterText.isEmpty())
        {
            return contactRepository.findAll();
        }else{
            return contactRepository.search(filterText);
        }
        
    }
    public long CountContacts()
    {
        return contactRepository.count();

    }
    public void deleteContact(Contact contact)
    {
        contactRepository.delete(contact);;

    }
    public void SaveContact(Contact contact)
    {
        if(contact == null )
        {
        System.err.println("Contact is null");
        return;
        }
        contactRepository.save(contact);
    }
    public List<Company> findAllCompanies(){
    return companyRepository.findAll();
    }
    public List<Status> FindAllStatuses(){
        return statusRepository.findAll();
    }
    
}