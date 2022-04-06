package com.openclassrooms.safetynetalertsP5.repository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.dto.Child;
import com.openclassrooms.safetynetalertsP5.dto.Children;
import com.openclassrooms.safetynetalertsP5.dto.Family;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfo;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfoGeneral;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfoMedical;
import com.openclassrooms.safetynetalertsP5.dto.PersonsByStationNumber;
import com.openclassrooms.safetynetalertsP5.dto.PersonsServedByFireStation;
import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.model.Person;

@Repository
public class ReponseRepository {

	/** Endpoint de firestation?stationNumber=<station_number **/
	public PersonsByStationNumber listPersonsByStationNumber(String station_number) {
		List<PersonInfoGeneral> listPersons = new ArrayList<PersonInfoGeneral>();
		int adultsNbr = 0;
		int childrenNbr = 0;
		List<String> address = addressFromNumberStation(station_number);
		for (Person p : LoadDataJSON.listPersons) {
			for (String add : address) {
				if (p.getAddress().equals(add)) {
					String fn = p.getFirstName();
					String ln = p.getLastName();
					String ad = p.getAddress();
					String phone = p.getPhone();
					PersonInfoGeneral person = new PersonInfoGeneral(fn, ln, ad, phone);
					listPersons.add(person);
					for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
						if (p.getFirstName().equals(m.getFirstName()) && p.getLastName().equals(m.getLastName())) {
							if (agePerson(m.getBirthdate()) <= 18)
								childrenNbr++;
							else if (agePerson(m.getBirthdate()) > 18)
								adultsNbr++;
						}
					}
				}
			}
		}
		PersonsByStationNumber personsByStationNumber = new PersonsByStationNumber(listPersons, childrenNbr, adultsNbr);

		return personsByStationNumber;
	}

	/** Endpoint de childAlert?address=<address> **/
	public Children childrenByAddress(String address) {

		List<Child> childrenByAddress = new ArrayList<Child>();
		List<Person> persons = findPersonsByAddress(address);
		for (Person p : persons) {
			String fn = p.getFirstName();
			String ln = p.getLastName();
			for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
				if (fn.equals(m.getFirstName()) && ln.equals(m.getLastName())) {
					int age = agePerson(m.getBirthdate());
					if (age <= 18) {
						List<Person> family = findFamilyMembers(fn, ln);
						Child child = new Child(fn, ln, age, family);
						childrenByAddress.add(child);
					}
				}
			}
		}
		Children children = new Children(childrenByAddress);
		return children;
	}

	/** Endpoint de phoneAlert?firestation=<firestation_number> **/
	public List<String> findPhoneNumberByStation(String stationNbr) {
		List<String> phoneAlert = new ArrayList<String>();
		List<Person> persons = findPersonsByNumberStation(stationNbr);
		for (Person p : persons) {
			String phone = p.getPhone();
			phoneAlert.add(phone);
		}
		return phoneAlert;
	}

	/** Endpoint de fire?address=<address> **/
	public PersonsServedByFireStation personInfoByAddress(String address) {

		String stationNumber = findStationNumberByAddress(address);
		List<PersonInfoMedical> listPersonInfo = findPersonInfoMedicalByAddress(address);
		PersonsServedByFireStation personsServedByFireStation = new PersonsServedByFireStation(listPersonInfo,
				stationNumber);
		return personsServedByFireStation;
	}

	/** Chercher une liste de personInfoMedical par rapport à une adresse */
	public List<PersonInfoMedical> findPersonInfoMedicalByAddress(String address) {
		List<Person> persons = findPersonsByAddress(address);
		List<PersonInfoMedical> listPersonInfo = new ArrayList<PersonInfoMedical>();
		for (Person p : persons) {
			String fn = p.getFirstName();
			String ln = p.getLastName();
			String phone = p.getPhone();
			for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
				if (fn.equals(m.getFirstName()) && ln.equals(m.getLastName())) {
					int age = agePerson(m.getBirthdate());
					List<String> medications = m.getMedications();
					List<String> allergies = m.getAllergies();
					PersonInfoMedical personinfoMedical = new PersonInfoMedical(fn, ln, phone, age, medications,
							allergies);
					listPersonInfo.add(personinfoMedical);
				}
			}
		}
		return listPersonInfo;
	}

	/** Endpoint de flood/stations?stations=<a list of station_number> */
	public List<Family> findFamiliesByStationNumbers(List<String> stationNbrs) {
		List<Family> families = new ArrayList<Family>();
		for (String nbr : stationNbrs) {
			List<String> address = addressFromNumberStation(nbr);
			for (String add : address) {

				List<PersonInfoMedical> persons = findPersonInfoMedicalByAddress(add);
				Family family = new Family(persons);
				families.add(family);
			}
		}
		return families;

	}

	/**
	 * Endpont de personInfo?firstName=<firstName>&lastName=<lastName>
	 * 
	 * @return
	 **/
	public List<PersonInfo> findPersonsInfoByName(String firstName, String lastName) {

		List<PersonInfo> listPersonInfo = new ArrayList<PersonInfo>();
		for (Person p : LoadDataJSON.listPersons) {
			if (p.getFirstName().equals(firstName) || p.getLastName().equals(lastName)) {
				String fn = p.getFirstName();
				String ln = p.getLastName();
				String ad = p.getAddress();
				String email = p.getEmail();
				for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
					if (fn.equals(m.getFirstName()) && ln.equals(m.getLastName())) {
						int age = agePerson(m.getBirthdate());
						List<String> medications = m.getMedications();
						List<String> allergies = m.getAllergies();
						PersonInfo personinfo = new PersonInfo(fn, ln, ad, email, age, medications, allergies);
						listPersonInfo.add(personinfo);
					}
				}
			}
		}
		return listPersonInfo;

	}

	/** Endpoint de communityEmail?city=<city> **/
	public List<String> findEmailsByCity(String city) {
		List<String> listEmails = new ArrayList<String>();
		for (Person p : LoadDataJSON.listPersons) {
			String email = p.getEmail();
			listEmails.add(email);
		}
		return listEmails;
	}

	/** calculer l'age d'une personne */
	private int agePerson(String birthDateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		int age = 0;
		LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);
		LocalDate currentDate = LocalDate.now();
		age = Period.between(birthDate, currentDate).getYears();
		return age;
	}

	/** Chercher une liste d'adresse par rapport au numéro de station */
	private List<String> addressFromNumberStation(String station_number) {
		List<String> addressFromNumberSt = new ArrayList<String>();
		for (FireStation f : LoadDataJSON.listFirestations) {
			if (f.getStation().equals(station_number)) {
				addressFromNumberSt.add(f.getAddress());
			}
		}
		return addressFromNumberSt;
	}

	/** Chercher le numéro de caserne en fonction de l'adresse */
	private String findStationNumberByAddress(String address) {
		String stationNumber = null;
		for (FireStation f : LoadDataJSON.listFirestations) {
			if (f.getAddress().equals(address)) {
				stationNumber = f.getStation();
			}
		}
		return stationNumber;
	}

	/** Chercher une liste de personne par rapport au numéro de station */
	private List<Person> findPersonsByNumberStation(String station_number) {
		List<Person> persons = new ArrayList<Person>();
		List<String> address = addressFromNumberStation(station_number);
		for (Person p : LoadDataJSON.listPersons) {
			for (String add : address) {
				if (p.getAddress().equals(add)) {
					persons.add(p);
				}
			}
		}
		return persons;
	}

	/** Chercher une liste de personne par rapport à une adresse */
	private List<Person> findPersonsByAddress(String address) {
		List<Person> persons = new ArrayList<Person>();
		for (Person p : LoadDataJSON.listPersons) {
			if (p.getAddress().equals(address)) {
				persons.add(p);
			}
		}
		return persons;
	}

	/** Chercher une liste des membres du foyer d'une personne */
	private List<Person> findFamilyMembers(String firstName, String lastName) {
		List<Person> family = new ArrayList<Person>();
		for (Person p : LoadDataJSON.listPersons) {
			if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)) {
				continue;
			}
			if (p.getFirstName().equals(firstName) || p.getLastName().equals(lastName)) {
				family.add(p);
			}
		}
		return family;
	}

}
