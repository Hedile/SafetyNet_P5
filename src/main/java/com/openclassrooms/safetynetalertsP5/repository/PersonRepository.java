package com.openclassrooms.safetynetalertsP5.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.model.Person;

@Repository
public class PersonRepository {

	@Autowired
	public SaveDataJSON saveDataJSON;

	public PersonRepository() {
		super();
	}

	private static final Logger logger = LogManager.getLogger("PersonRepository");

	/** Endpoint de POST /person **/
	public Person save(Person personToAdd) {

		Person person = findPersonByFirstNameAndLastName(personToAdd.getFirstName(), personToAdd.getLastName());
		if (person == null) {
			LoadDataJSON.listPersons.add(personToAdd);
			saveDataJSON.saveData();
			logger.info("Person created: {} {}", personToAdd.getFirstName(), personToAdd.getLastName());
			return personToAdd;

		} else {
			logger.error("A person already exists with firstname ={} and lastename = {} ", person.getFirstName(),
					person.getLastName());
			return null;

		}

	}

	/** Endpoint de PUT /person **/
	public Person updateByFirstNameAndLastName(Person personToUpdate) {
		Person person = findPersonByFirstNameAndLastName(personToUpdate.getFirstName(), personToUpdate.getLastName());
		if (person != null) {
			person.setAddress(personToUpdate.getAddress());
			person.setCity(personToUpdate.getCity());
			person.setZip(personToUpdate.getZip());
			person.setPhone(personToUpdate.getPhone());
			person.setEmail(personToUpdate.getEmail());
			saveDataJSON.saveData();
			logger.info("Person updated: {} {}", person.getFirstName(), person.getLastName());
			return person;
		} else {

			logger.error("Failed to find person {} {} ", personToUpdate.getFirstName(), personToUpdate.getLastName());
			return null;
		}

	}

	/** Endpoint de DELETE /person **/
	public Person deleteByFirstNameAndLastName(String fn, String ln) {

		Person personToDelete = findPersonByFirstNameAndLastName(fn, ln);
		if (personToDelete != null) {
			LoadDataJSON.listPersons.remove(personToDelete);
			saveDataJSON.saveData();
			logger.info("Person deleted: {}  {}", personToDelete.getFirstName(), personToDelete.getLastName());
			return personToDelete;
		} else {
			logger.error("Failed to find person: {}  {} ", fn, ln);
			return null;
		}
	}

	/** Find a person by firstname and lastname */
	public Person findPersonByFirstNameAndLastName(String fn, String ln) {
		for (Person p : LoadDataJSON.listPersons) {
			if (p.getFirstName().equals(fn) && p.getLastName().equals(ln)) {
				logger.info("Person found: {} {} ", fn, ln);
				return p;
			}
		}
		logger.error("Person not found :{} {} ", fn, ln);
		return null;
	}

	public List<Person> findAll() {
		return LoadDataJSON.listPersons;

	}

}
