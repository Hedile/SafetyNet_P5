package com.openclassrooms.safetynetalertsP5.repository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger logger = LogManager.getLogger("ReponseRepository");

	public ReponseRepository() {
		super();

	}

	/** Endpoint de firestation?stationNumber=<station_number */
	public PersonsByStationNumber listPersonsByStationNumber(String station_number) {
		logger.debug("find peopole by station number :{}", station_number);
		List<PersonInfoGeneral> listPersons = new ArrayList<PersonInfoGeneral>();
		int adultsNbr = 0;
		int childrenNbr = 0;
		List<String> address = findAddressByNumberStation(station_number);
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
		logger.info("People found ");
		return personsByStationNumber;
	}

	/** Endpoint de childAlert?address=<address> */
	public Children childrenByAddress(String address) {
		logger.debug("find children by address :{}", address);
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
		logger.info("Children found ");
		return children;
	}

	/** Endpoint de phoneAlert?firestation=<firestation_number> */
	public List<String> findPhoneNumberByStation(String stationNbr) {
		logger.debug("find phone number by station  :{}", stationNbr);
		List<String> phoneAlert = new ArrayList<String>();
		List<Person> persons = findPersonsByNumberStation(stationNbr);
		for (Person p : persons) {
			String phone = p.getPhone();
			phoneAlert.add(phone);
		}
		logger.info("Phone number found ");
		return phoneAlert;
	}

	/** Endpoint de fire?address=<address> **/
	public PersonsServedByFireStation personInfoByAddress(String address) {
		logger.debug("find people Served By FireStation :{}", address);
		String stationNumber = findStationNumberByAddress(address);
		List<PersonInfoMedical> listPersonInfo = findPersonInfoMedicalByAddress(address);
		PersonsServedByFireStation personsServedByFireStation = new PersonsServedByFireStation(listPersonInfo,
				stationNumber);
		logger.info("people Served By FireStation found");
		return personsServedByFireStation;
	}

	/** Chercher une liste de personInfoMedical par rapport à une adresse */
	public List<PersonInfoMedical> findPersonInfoMedicalByAddress(String address) {
		logger.debug("find people with Info medical:{}", address);
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
		logger.info("people with Info medical found");
		return listPersonInfo;
	}

	/** Endpoint de flood/stations?stations=<a list of station_number> */
	public List<Family> findFamiliesByStationNumbers(List<String> stationNbrs) {
		logger.debug("find families by stationNumbers:{}", stationNbrs);
		List<Family> families = new ArrayList<Family>();
		for (String nbr : stationNbrs) {
			List<String> address = findAddressByNumberStation(nbr);
			for (String add : address) {

				List<PersonInfoMedical> persons = findPersonInfoMedicalByAddress(add);
				Family family = new Family(persons);
				families.add(family);
			}
		}
		logger.info("families found");
		return families;

	}

	/**
	 * Endpoint de personInfo?firstName=<firstName>&lastName=<lastName>
	 */
	public List<PersonInfo> findPersonsInfoByName(String firstName, String lastName) {
		logger.debug("find PersonsInfo by Name:{} {}", firstName, lastName);
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
		logger.info("PersonInfo found");
		return listPersonInfo;

	}

	/** Endpoint de communityEmail?city=<city> */
	public List<String> findEmailsByCity(String city) {
		logger.debug("find Emails by City {}", city);
		List<String> listEmails = new ArrayList<String>();
		for (Person p : LoadDataJSON.listPersons) {
			String email = p.getEmail();
			listEmails.add(email);
		}
		logger.info("Emails found ");
		return listEmails;
	}

	/** calculer l'age d'une personne */
	public int agePerson(String birthDateStr) {
		logger.debug("Calculate the age of person: {} ", birthDateStr);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		int age = 0;
		LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);
		LocalDate currentDate = LocalDate.now();
		age = Period.between(birthDate, currentDate).getYears();
		logger.info("Age= {} ", age);
		return age;
	}

	/** Chercher une liste d'adresse par rapport au numéro de station */
	public List<String> findAddressByNumberStation(String station_number) {
		logger.debug("find a list of addresses by this station number :{}", station_number);
		List<String> addressByNumberSt = new ArrayList<String>();
		for (FireStation f : LoadDataJSON.listFirestations) {
			if (f.getStation().equals(station_number)) {
				addressByNumberSt.add(f.getAddress());
			}
		}
		if (addressByNumberSt != null)
			logger.info("Addresses found corresponding to this station number = {}", station_number);
		return addressByNumberSt;

	}

	/** Chercher le numéro de caserne en fonction de l'adresse */
	public String findStationNumberByAddress(String address) {
		logger.debug("find a sattion number by address:{}", address);
		String stationNumber = null;
		for (FireStation f : LoadDataJSON.listFirestations) {
			if (f.getAddress().equals(address)) {
				stationNumber = f.getStation();
				logger.info("station number found = {}", stationNumber);
			}
		}
		logger.error("station number not found ");
		return stationNumber;
	}

	/** Chercher une liste de personne par rapport au numéro de station */
	public List<Person> findPersonsByNumberStation(String station_number) {
		logger.debug("find people by station number:{}", station_number);
		List<Person> persons = new ArrayList<Person>();
		List<String> address = findAddressByNumberStation(station_number);
		for (Person p : LoadDataJSON.listPersons) {
			for (String add : address) {
				if (p.getAddress().equals(add)) {
					persons.add(p);
					logger.info("People found ");
				}
			}
		}
		logger.error("People not found ");
		return persons;
	}

	/** Chercher une liste de personne par rapport à une adresse */
	public List<Person> findPersonsByAddress(String address) {
		logger.debug("find people by address:{}", address);
		List<Person> persons = new ArrayList<Person>();
		for (Person p : LoadDataJSON.listPersons) {
			if (p.getAddress().equals(address)) {
				persons.add(p);
				logger.info("People found ");
			}
		}
		logger.error("People not found ");
		return persons;
	}

	/** Chercher une liste des membres du foyer d'une personne */
	public List<Person> findFamilyMembers(String firstName, String lastName) {
		logger.debug("find the child's family members:{} {}", firstName, lastName);
		List<Person> family = new ArrayList<Person>();
		for (Person p : LoadDataJSON.listPersons) {
			if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)) {
				continue;
			}
			if (p.getFirstName().equals(firstName) || p.getLastName().equals(lastName)) {
				family.add(p);
				logger.info("Family found");
			}
		}
		logger.error("Family not found");
		return family;
	}

}
