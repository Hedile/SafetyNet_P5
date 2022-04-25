package com.openclassrooms.safetynetalertsP5.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.openclassrooms.safetynetalertsP5.exceptions.NotFoundException;
import com.openclassrooms.safetynetalertsP5.model.Person;
import com.openclassrooms.safetynetalertsP5.service.PersonService;

/**
 * Person controller.
 */
@RestController
public class PersonController {
	@Autowired
	private PersonService personService;

	private static final Logger logger = LogManager.getLogger("PersonController");

	public PersonController() {
		super();
	}

	/** Endpoint Get /person **/
	@GetMapping("/Persons")
	public ResponseEntity<List<Person>> getAllPersons() {
		logger.info("Get/Persons");

		List<Person> persons = personService.getAllPersons();
		if (persons != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(persons);
		} else {
			logger.error("Persons Not found");
			throw new NotFoundException("Persons Not found");
		}
	}

	@GetMapping(value = "/Persons/{FirstName}/{LastName}")
	public ResponseEntity<Person> getPersonByFirstName(@PathVariable("FirstName") String fn,
			@PathVariable("LastName") String ln) {
		Person person = personService.getPerson(fn, ln);
		if (person != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(person);
		} else {
			logger.error("Person Not found");
			throw new NotFoundException("Person with firstname= " + fn + " and lastName=" + ln + " does not exist.");
		}

	}

	/**
	 * Endpoint POST /person Create = @PostMapping("/person")
	 */
	@PostMapping(value = "/Persons")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {

		Person addedPerson = personService.addPerson(person);
		if (addedPerson == null) {
			logger.error("Failed create person");
			return ResponseEntity.noContent().build();
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{FirstName}/{LastName}")
				.buildAndExpand(addedPerson.getFirstName(), addedPerson.getLastName()).toUri();
		logger.info("The person is created successfully");
		return ResponseEntity.created(location).build();

	}

	/**
	 * Endpoint PUT /person Update = @PutMapping("/person")
	 * 
	 * @return
	 **/

	@PutMapping(value = "/Persons")
	public ResponseEntity<Person> updatePerson(@RequestBody Person p, @RequestParam("FirstName") String fn,
			@RequestParam("LastName") String ln) {

		Person updatedPerson = personService.updatePerson(p);
		if (updatedPerson != null) {
			logger.info("Person is updated successfully");

			return ResponseEntity.ok(updatedPerson);
		} else {
			logger.error("Failed update person ");
			throw new NotFoundException("Person with firstname= " + fn + " and lastName=" + ln + " does not exist.");

		}

	}

	/**
	 * Endpoint DELETE /person Delete = @DeleteMapping("/person")
	 **/
	@DeleteMapping(value = "/Persons")
	public Map<String, Boolean> deletePerson(@RequestParam("FirstName") String fn,
			@RequestParam("LastName") String ln) {
		Person deletedPerson = personService.deletePerson(fn, ln);
		Map<String, Boolean> response = new HashMap<>();
		if (deletedPerson != null) {
			response.put("deleted", Boolean.TRUE);
			logger.info("Person is deleted successfully");
			return response;
		} else {
			logger.error("Failed delete person ");
			throw new NotFoundException("Person with firstname= " + fn + " and lastName=" + ln + " does not exist.");

		}
	}
}
