package com.openclassrooms.safetynetalertsP5.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

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

@RestController
public class PersonController {
	@Autowired
	private PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	/** Endpoint Get /person **/
	@GetMapping("/Persons")
	public List<Person> getAllPersons() {
		return personService.getAllPersons();
	}

	@GetMapping(value = "/Persons/{FirstName}")
	public Person getPersonByFirstName(@PathVariable("FirstName") String fn) {
		return personService.getPerson(fn);

	}

	/** Endpoint POST /person **/
	@PostMapping(value = "/Persons")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		Person addedPerson = personService.addPerson(person);
		if (Objects.isNull(addedPerson)) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{FirstName}/{LastName}")
				.buildAndExpand(addedPerson.getFirstName(), addedPerson.getLastName()).toUri();
		return ResponseEntity.created(location).build();

	}

	/** Endpoint PUT /person **/

	@PutMapping(value = "/Persons")
	public void updatePerson(@RequestBody Person p, @RequestParam("FirstName") String fn,
			@RequestParam("LastName") String ln) {
		personService.updatePerson(p);
	}

	/** Endpoint DELETE /person **/
	@DeleteMapping(value = "/Persons")
	public void deletePerson(@RequestParam("FirstName") String fn, @RequestParam("LastName") String ln) {
		personService.deletePerson(fn, ln);
	}

}
