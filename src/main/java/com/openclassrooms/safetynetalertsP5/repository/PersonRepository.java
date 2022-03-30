package com.openclassrooms.safetynetalertsP5.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.model.Person;

@Repository
public class PersonRepository {

	@Autowired
	public SaveDataJSON saveDataJSON;

	public Person findByFirstName(String fn) {
		for (Person p : LoadDataJSON.listPersons) {
			if (p.getFirstName().equals(fn)) {
				return p;
			}
		}
		return null;
	}

	public Person updateByFirstNameAndLastName(Person personToUpdate) {

		for (Person p : LoadDataJSON.listPersons) {
			if (p.getFirstName().equals(personToUpdate.getFirstName())
					&& p.getLastName().equals(personToUpdate.getLastName())) {
				p.setAddress(personToUpdate.getAddress());
				p.setCity(personToUpdate.getCity());
				p.setZip(personToUpdate.getZip());
				p.setPhone(personToUpdate.getPhone());
				p.setEmail(personToUpdate.getEmail());
				saveDataJSON.saveData();
				return p;
			}

		}

		return null;
	}

	public Person deleteByFirstNameAndLastName(String fn, String ln) {
		Person personToDelete = new Person();
		for (Person p : LoadDataJSON.listPersons) {
			if (p.getFirstName().equals(fn) && p.getLastName().equals(ln)) {
				personToDelete = p;
			}
		}
		LoadDataJSON.listPersons.remove(personToDelete);
		saveDataJSON.saveData();
		return personToDelete;

	}

	public List<Person> findAll() {
		return LoadDataJSON.listPersons;

	}

	public Person save(Person person) {
		LoadDataJSON.listPersons.add(person);
		saveDataJSON.saveData();
		return person;

	}

}
