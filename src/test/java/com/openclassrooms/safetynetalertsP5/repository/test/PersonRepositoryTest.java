package com.openclassrooms.safetynetalertsP5.repository.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.model.Person;
import com.openclassrooms.safetynetalertsP5.repository.PersonRepository;

@WebMvcTest(PersonRepository.class)
@AutoConfigureMockMvc
public class PersonRepositoryTest {
	@Autowired
	private PersonRepository personRepository;
	@MockBean
	private SaveDataJSON saveDataJSON;

	private ArrayList<Person> persons = new ArrayList<Person>();

	Person person1 = new Person("Toto", "Bentiti", "st Germain", "paris", "97451", "0666777", "toto@email.com");
	Person person2 = new Person("leo", "mini", "st Germain", "paris", "97451", "0666777", "leo@email.com");
	Person person3 = new Person("Jane ", "Doe", "st Germain", "paris", "97451", "0666777", "jane@email.com");
	Person personToUpdate = new Person("Toto", "Bentiti", "Jean Médecin", "Nice", "06000", "2222222", "jean@hahoo.fr");

	@BeforeEach
	public void setup() {
		persons.add(person1);
		persons.add(person2);
		LoadDataJSON.listPersons = persons;
	}

	@AfterEach
	public void cleanUpEach() {

		LoadDataJSON.listPersons = null;
	}

	@Test
	public void testFindAllPerson() throws Exception {

		List<Person> people = personRepository.findAll();
		assertEquals(2, persons.size());
		assertEquals(persons, people);

	}

	@Test
	public void testFindPersonByFirstNameAndLastName() throws Exception {
		Person p = personRepository.findPersonByFirstNameAndLastName(person2.getFirstName(), person2.getLastName());
		assertEquals(2, persons.size());
		assertEquals(person2.getFirstName(), p.getFirstName());
		assertThat(persons).contains(person2);

	}

	@Test
	public void testSavePerson() throws Exception {
		personRepository.save(person3);
		assertEquals(3, persons.size());
		assertEquals(persons.get(2).getFirstName(), person3.getFirstName());
		assertThat(persons).contains(person3);

	}

	@Test
	public void testSavePerson_Existing() throws Exception {
		personRepository.save(person1);
		assertEquals(2, persons.size());

	}

	@Test
	public void testUpdatePerson() throws Exception {

		personRepository.updateByFirstNameAndLastName(personToUpdate);
		assertEquals("Nice", persons.get(0).getCity());
		assertEquals("Jean Médecin", persons.get(0).getAddress());

	}

	@Test
	public void testUpdatePerson_NotFound() throws Exception {

		personRepository.updateByFirstNameAndLastName(person3);
		assertThat(persons).doesNotContain(person3);

	}

	@Test
	public void testDeletePerson() throws Exception {

		personRepository.deleteByFirstNameAndLastName(person1.getFirstName(), person1.getLastName());
		assertEquals(1, persons.size());
		assertThat(persons).doesNotContain(person1);
	}

	@Test
	public void testDeletePerson_NotFound() throws Exception {

		personRepository.deleteByFirstNameAndLastName(person3.getFirstName(), person3.getLastName());
		assertEquals(2, persons.size());
		assertThat(persons).doesNotContain(person3);

	}
}
