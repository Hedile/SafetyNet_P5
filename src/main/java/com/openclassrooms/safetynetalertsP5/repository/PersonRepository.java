package com.openclassrooms.safetynetalertsP5.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.exceptions.PersonNotFoundException;
import com.openclassrooms.safetynetalertsP5.model.Person;

@Repository
public class PersonRepository {

	@Autowired
	public SaveDataJSON saveDataJSON;
	private static final Logger logger = LogManager.getLogger("PersonRepository");

	/** Endpoint de POST /person **/
	public Person save(Person personToAdd) {
		try {
			Person person = findPersonByFirstNameAndLastName(personToAdd.getFirstName(), personToAdd.getLastName());
			logger.error("A person already exists with firstname ={} and lastename = {} ", person.getFirstName(),
					person.getLastName());
			return null;
		} catch (PersonNotFoundException e) {

			LoadDataJSON.listPersons.add(personToAdd);
			saveDataJSON.saveData();
			logger.info("Person created: firstname ={} and lastename = {}", personToAdd.getFirstName(),
					personToAdd.getLastName());
			return personToAdd;
		}

	}

	/** Endpoint de PUT /person **/
	public Person updateByFirstNameAndLastName(Person personToUpdate) {
		try {
			Person person = findPersonByFirstNameAndLastName(personToUpdate.getFirstName(),
					personToUpdate.getLastName());
			person.setAddress(personToUpdate.getAddress());
			person.setCity(personToUpdate.getCity());
			person.setZip(personToUpdate.getZip());
			person.setPhone(personToUpdate.getPhone());
			person.setEmail(personToUpdate.getEmail());
			saveDataJSON.saveData();
			logger.info("Person updated: firstname ={} and lastename = {}", person.getFirstName(),
					person.getLastName());
			return person;
		} catch (PersonNotFoundException e) {

			logger.error("Failed to find person with firstname ={} and lastename = {} ", personToUpdate.getFirstName(),
					personToUpdate.getLastName());
			return null;
		}

	}

	/** Endpoint de DELETE /person **/
	public Person deleteByFirstNameAndLastName(String fn, String ln) {
		try {
			Person personToDelete = findPersonByFirstNameAndLastName(fn, ln);
			LoadDataJSON.listPersons.remove(personToDelete);
			saveDataJSON.saveData();
			logger.info("Person deleted: firstname ={} and lastename = {}", personToDelete.getFirstName(),
					personToDelete.getLastName());
			return personToDelete;
		} catch (PersonNotFoundException e) {
			logger.error("Failed to find person with firstname ={} and lastename = {} ", fn, ln);
			return null;
		}
	}

	/** Find a person by firstname and lastname */
	public Person findPersonByFirstNameAndLastName(String fn, String ln) {
		logger.debug("Find a person by firstname and lastname:{} {}", fn, ln);
		Person personFound = null;
		for (Person p : LoadDataJSON.listPersons) {
			if (p.getFirstName().equals(fn) && p.getLastName().equals(ln)) {
				personFound = p;
			}
		}
		if (personFound != null) {
			logger.info("Person found: firstname ={} and lastename = {} ", fn, ln);
			return personFound;
		} else {
			throw new PersonNotFoundException("Person not found with firstname= " + fn + " and lastName=" + ln);
		}

	}

	public List<Person> findAll() {
		logger.debug("Find persons ");
		return LoadDataJSON.listPersons;

	}

}
