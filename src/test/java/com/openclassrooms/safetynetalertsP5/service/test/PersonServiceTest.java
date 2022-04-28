package com.openclassrooms.safetynetalertsP5.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassrooms.safetynetalertsP5.model.Person;
import com.openclassrooms.safetynetalertsP5.repository.PersonRepository;
import com.openclassrooms.safetynetalertsP5.service.PersonService;

@WebMvcTest(PersonService.class)
@AutoConfigureMockMvc
public class PersonServiceTest {
	@Autowired
	private PersonService personService;
	@MockBean
	private PersonRepository personRepository;
	private Person person;

	@BeforeEach
	public void setup() {
		person = new Person();
		person.setFirstName("Jules");
		person.setLastName("Dupont");
		person.setAddress("st Germain");
		person.setCity("paris");
		person.setZip("97451");
		person.setPhone("0666777");
		person.setEmail("jules@email.com");
	}

	@Test
	public void testFindAllPersons() throws Exception {
		List<Person> allPersons = Arrays.asList(person);
		Mockito.when(personRepository.findAll()).thenReturn(allPersons);
		List<Person> persons = personService.getAllPersons();
		assertNotNull(persons);
		assertEquals(persons, allPersons);
		assertEquals(persons.size(), allPersons.size());
		verify(personRepository).findAll();
	}

	@Test
	public void testFindPersonByFirstNameAndLastName() {

		Mockito.when(personRepository.findPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName()))
				.thenReturn((person));
		Person personFromDB = personService.getPerson(person.getFirstName(), person.getLastName());
		assertNotNull(personFromDB);
		assertEquals(personFromDB.getAddress(), (person.getAddress()));
		verify(personRepository).findPersonByFirstNameAndLastName(any(String.class), any(String.class));
	}

	@Test
	public void testSavePerson() throws Exception {
		Mockito.when(personRepository.save(any(Person.class))).thenReturn(person);
		Person personSaved = personService.addPerson(person);
		assertNotNull(personSaved);
		assertEquals(person.getFirstName(), personSaved.getFirstName());
		assertEquals(person.getLastName(), personSaved.getLastName());
		verify(personRepository).save(any(Person.class));
	}

	@Test
	public void testUpdatePerson() throws Exception {

		Mockito.when(personRepository.updateByFirstNameAndLastName(any(Person.class))).thenReturn(person);
		Person personFromDB = personService.updatePerson(person);
		assertNotNull(personFromDB);
		assertEquals("Jules", personFromDB.getFirstName());
		assertEquals("Dupont", personFromDB.getLastName());

	}

	@Test
	public void testDeletePerson() throws Exception {

		Mockito.when(personRepository.deleteByFirstNameAndLastName(any(String.class), any(String.class)))
				.thenReturn(person);
		Person personDeleted = personService.deletePerson(person.getFirstName(), person.getLastName());
		assertNotNull(personDeleted);
		assertEquals("Jules", personDeleted.getFirstName());
		assertEquals("Dupont", personDeleted.getLastName());
		verify(personRepository).deleteByFirstNameAndLastName(any(String.class), any(String.class));
	}
}
