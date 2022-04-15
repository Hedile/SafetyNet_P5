package com.openclassrooms.safetynetalertsP5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.safetynetalertsP5.model.Person;
import com.openclassrooms.safetynetalertsP5.repository.PersonRepository;

@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;

	/** Endpoint de POST /person **/
	public Person addPerson(Person person) {
		return personRepository.save(person);

	}

	/** Endpoint de PUT /person **/
	public Person updatePerson(Person person) {
		return personRepository.updateByFirstNameAndLastName(person);

	}

	/** Endpoint de DELETE /person **/
	public Person deletePerson(String fn, String ln) {
		return personRepository.deleteByFirstNameAndLastName(fn, ln);

	}

	public List<Person> getAllPersons() {
		return personRepository.findAll();
	}

	public Person getPerson(String fn, String ln) {
		return personRepository.findPersonByFirstNameAndLastName(fn, ln);
	}

}
