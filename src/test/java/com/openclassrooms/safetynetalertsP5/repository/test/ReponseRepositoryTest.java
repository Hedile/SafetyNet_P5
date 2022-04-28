package com.openclassrooms.safetynetalertsP5.repository.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.dto.Children;
import com.openclassrooms.safetynetalertsP5.dto.Family;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfo;
import com.openclassrooms.safetynetalertsP5.dto.PersonsByStationNumber;
import com.openclassrooms.safetynetalertsP5.dto.PersonsServedByFireStation;
import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.model.Person;
import com.openclassrooms.safetynetalertsP5.repository.ReponseRepository;

@WebMvcTest(ReponseRepository.class)
@AutoConfigureMockMvc
public class ReponseRepositoryTest {
	@Autowired
	private ReponseRepository reponseRepository;
	ArrayList<Person> persons = new ArrayList<Person>();
	Person person1 = new Person("Toto", "Bentiti", "st Germain", "paris", "97451", "0666777", "toto@email.com");
	Person person2 = new Person("leo", "Bentiti", "st Germain", "paris", "97451", "0666777", "leo@email.com");
	Person person3 = new Person("Jane ", "castex", "Chambourcy", "paris", "97451", "0666777", "jane@email.com");
	Person person4 = new Person("Jules", "Dupont", "Jean Médecin", "Nice", "06000", "2222222", "jean@hahoo.fr");

	ArrayList<FireStation> firestations = new ArrayList<FireStation>();
	FireStation firestation1 = new FireStation("st Germain", "5");
	FireStation firestation2 = new FireStation("Paris", "2");
	FireStation firestation3 = new FireStation("Chambourcy", "2");
	FireStation firestation4 = new FireStation("Chambord", "10");

	ArrayList<MedicalRecord> medicalrecords = new ArrayList<MedicalRecord>();
	List<String> medications = Arrays.asList("aznol:350mg", "doliprane:1000mg");
	List<String> allergies = Arrays.asList("nillacilan");
	MedicalRecord medicalrecord1 = new MedicalRecord("Toto", "Bentiti", "03/06/1984", medications, allergies);
	MedicalRecord medicalrecord2 = new MedicalRecord("leo", "Bentiti", "01/01/2015", medications, allergies);
	MedicalRecord medicalrecord3 = new MedicalRecord("Jane ", "castex", "02/06/1998", medications, allergies);
	MedicalRecord medicalrecord4 = new MedicalRecord("Jules", "Dupont", "01/01/1980", medications,
			allergies);

	@BeforeEach
	public void setup() {
		persons.add(person1);
		persons.add(person2);
		persons.add(person3);
		LoadDataJSON.listPersons = persons;

		firestations.add(firestation1);
		firestations.add(firestation2);
		firestations.add(firestation3);
		LoadDataJSON.listFirestations = firestations;

		medicalrecords.add(medicalrecord1);
		medicalrecords.add(medicalrecord2);
		medicalrecords.add(medicalrecord3);
		LoadDataJSON.listMedicalrecords = medicalrecords;

	}

	@AfterEach
	public void cleanUpEach() {

		LoadDataJSON.listPersons = null;
		LoadDataJSON.listFirestations = null;
		LoadDataJSON.listMedicalrecords = null;

	}

	/** La liste de personne par rapport à une adresse est trouvé */
	@Test
	public void testFindPersonsByAddress() throws Exception {
		List<Person> people = reponseRepository.findPersonsByAddress(person1.getAddress());
		assertEquals(2, people.size());
	}

	/** La liste de personne par rapport à une adresse n'est pas trouvé */
	@Test
	public void testFindPersonsByAddress_NotFound() throws Exception {
		List<Person> people = reponseRepository.findPersonsByAddress(person4.getAddress());
		assertEquals(0, people.size());
	}

	/** La liste des membres du foyer d'une personne est trouvé */
	@Test
	public void testFindFindFamilyMembers() throws Exception {
		List<Person> family = reponseRepository.findFamilyMembers(person1.getFirstName(), person1.getLastName());
		assertEquals(1, family.size());
	}

	/** La liste des membres du foyer d'une personne n'est pas trouvé */
	@Test
	public void testFindFindFamilyMembers_NotFound() throws Exception {
		List<Person> family = reponseRepository.findFamilyMembers(person3.getFirstName(), person3.getLastName());
		assertEquals(0, family.size());
	}

	/** La liste d'adresse par rapport au numéro de station est trouvé */
	@Test
	public void testFindAddressByNumberStation() throws Exception {
		List<String> address = reponseRepository.findAddressByNumberStation(firestation1.getStation());
		assertEquals(1, address.size());
	}

	/** La liste d'adresse par rapport au numéro de station n'est pas trouvé */
	@Test
	public void testFindAddressByNumberStation_NotFound() throws Exception {
		List<String> address = reponseRepository.findAddressByNumberStation(firestation4.getStation());
		assertEquals(0, address.size());
	}

	/** le numéro de caserne en fonction de l'adresse est trouvé */
	@Test
	public void testFindStationNumberByAddress() throws Exception {
		String stationNumber = reponseRepository.findStationNumberByAddress(firestation1.getAddress());
		assertEquals(firestation1.getStation(), stationNumber);
	}

	/** le numéro de caserne en fonction de l'adresse n'est pas trouvé */
	@Test
	public void testFindStationNumberByAddress_NotFound() throws Exception {
		String stationNumber = reponseRepository.findStationNumberByAddress(firestation4.getAddress());
		assertEquals(null, stationNumber);
	}

	/** La liste de personne par rapport au numéro de station est trouvé */
	@Test
	public void testFindPersonsByNumberStation() throws Exception {
		List<Person> people = reponseRepository.findPersonsByNumberStation(firestation1.getStation());
		assertEquals(2, people.size());
	}

	/** La liste de personne par rapport au numéro de station n'est pas trouvé */
	@Test
	public void testFindPersonsByNumberStation_Notfound() throws Exception {
		List<Person> people = reponseRepository.findPersonsByNumberStation(firestation4.getStation());
		assertEquals(0, people.size());
	}

	/** calculer l'age d'une personne */
	@Test
	public void testCalculatorAgePerson() throws Exception {
		int age = reponseRepository.agePerson(medicalrecord1.getBirthdate());
		assertEquals(38, age);
	}

	/** Test Endpoint de communityEmail?city=<city> **/
	@Test
	public void testFindEmailsByCity() throws Exception {
		List<String> emails = reponseRepository.findEmailsByCity("paris");
		assertEquals(3, emails.size());
		assertThat(emails).contains(person1.getEmail());
	}

	/** Test Endpoint de personInfo?firstName=<firstName>&lastName=<lastName> */
	@Test
	public void testFindPersonsInfoByName() throws Exception {

		List<PersonInfo> peopleInfo = reponseRepository.findPersonsInfoByName(person1.getFirstName(),
				person1.getLastName());
		assertEquals(2, peopleInfo.size());

	}

	/** Test Endpoint de flood/stations?stations=<a list of station_number> */
	@Test
	public void testFindFamiliesByStationNumbers() throws Exception {
		List<String> stationNbrs = Arrays.asList("5", "2");
		List<Family> families = reponseRepository.findFamiliesByStationNumbers(stationNbrs);
		assertEquals(3, families.size());

	}

	/** Test Endpoint de fire?address=<address> **/
	@Test
	public void testFindPersonsServedByFireStation() throws Exception {
		PersonsServedByFireStation personsServedByFireStation = reponseRepository.personInfoByAddress("st Germain");

		assertEquals("5", personsServedByFireStation.getStationNumber());
		assertEquals("Toto", personsServedByFireStation.getPersonsServedByFireStation().get(0).getFirstName());

	}

	/** Test Endpoint de phoneAlert?firestation=<firestation_number> */
	@Test
	public void testFindPhoneNumberByStation() throws Exception {
		List<String> phoneAlert = reponseRepository.findPhoneNumberByStation("5");
		assertEquals(2, phoneAlert.size());
		assertEquals("0666777", phoneAlert.get(0));

	}

	/** Endpoint de childAlert?address=<address> */
	@Test
	public void testFindChildrenByAddress() throws Exception {
		Children children = reponseRepository.childrenByAddress("st Germain");
		assertEquals(person2.getLastName(), children.getChidren().get(0).getLastName());

	}

	/** Test Endpoint de firestation?stationNumber=<station_number */
	@Test
	public void testFindListPersonsByStationNumber() throws Exception {
		PersonsByStationNumber personsByStationNumber = reponseRepository.listPersonsByStationNumber("5");
		assertEquals(1, personsByStationNumber.getAdultsNumber());
		assertEquals(1, personsByStationNumber.getChildrenNumber());

	}
}
