package com.openclassrooms.safetynetalertsP5.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	/** Endpoint Get /person **/
	@GetMapping("/Persons")
	public List<Person> getAllPersons() {
		logger.info("Get/Persons");

		List<Person> persons = personService.getAllPersons();
		if (persons != null) {
			logger.info("Get ok");
			return persons;
		} else {
			logger.error("Persons Not found");
			return null;
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
			return ResponseEntity.notFound().build();
		}

	}

	/**
	 * Endpoint POST /person Create = @PostMapping("/person")
	 */
	@PostMapping(value = "/Persons")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		Person addedPerson = personService.addPerson(person);
		if (Objects.isNull(addedPerson)) {
			logger.error("Person is not created");
			return ResponseEntity.noContent().build();
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{FirstName}/{LastName}")
				.buildAndExpand(addedPerson.getFirstName(), addedPerson.getLastName()).toUri();
		logger.info("Post OK");
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
			logger.info("Put OK");

			return ResponseEntity.ok(updatedPerson);
		} else {
			logger.error("No update");
			return ResponseEntity.notFound().build();
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
			logger.info("Successful deletion");
			return response;
		} else {
			logger.error("Deletion failed");
			ResponseEntity.notFound().build();
			response.put("deleted", Boolean.FALSE);
			return response;
		}

	}
}
