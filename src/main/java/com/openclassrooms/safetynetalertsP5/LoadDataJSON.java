package com.openclassrooms.safetynetalertsP5;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.model.Person;

@Component
public class LoadDataJSON {

	public static final ArrayList<Person> listPersons = new ArrayList<Person>();
	public static final List<FireStation> listFirestations = new ArrayList<FireStation>();
	public static final List<MedicalRecord> listMedicalrecords = new ArrayList<MedicalRecord>();
	private static final Logger logger = LogManager.getLogger("LoadDataJSON");

	public LoadDataJSON() {
		super();

	}

	private static JSONObject loadJsonFile() throws Exception {
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream("src\\main\\resources\\application.properties");
		try {
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
		String url = prop.getProperty("url");

		try {

			JSONParser jsonParser = new JSONParser();
			Reader reader = new FileReader(url);
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			logger.debug("loadJsonFile success");
			return jsonObject;
		} catch (Exception e) {

			logger.error("loadJsonFile has failed");
			e.printStackTrace();
		}

		return null;
	}

	public static void loadData() throws Exception {
		JSONObject jsonObject = loadJsonFile();
		JSONArray jsonArrayPersons = (JSONArray) jsonObject.get("persons");

		for (Object p : jsonArrayPersons) {
			JSONObject jsonPerson = (JSONObject) p;
			String fN = (String) jsonPerson.get("firstName");
			String lN = (String) jsonPerson.get("lastName");
			String add = (String) jsonPerson.get("address");
			String city = (String) jsonPerson.get("city");
			String zip = (String) jsonPerson.get("zip");
			String phone = (String) jsonPerson.get("phone");
			String email = (String) jsonPerson.get("email");
			Person person = new Person(fN, lN, add, city, zip, phone, email);
			listPersons.add(person);

		}

		JSONArray jsonArrayFirestations = (JSONArray) jsonObject.get("firestations");
		for (Object f : jsonArrayFirestations) {
			JSONObject jsonFirestation = (JSONObject) f;
			String add = (String) jsonFirestation.get("address");
			String stat = (String) jsonFirestation.get("station");
			FireStation fireStation = new FireStation(add, stat);
			listFirestations.add(fireStation);
		}

		JSONArray jsonArrayMedicalRecords = (JSONArray) jsonObject.get("medicalrecords");
		for (Object med : jsonArrayMedicalRecords) {

			JSONObject jsonMedicalRecord = (JSONObject) med;
			String fn = (String) jsonMedicalRecord.get("firstName");
			String ln = (String) jsonMedicalRecord.get("lastName");
			String bd = (String) jsonMedicalRecord.get("birthdate");
			JSONArray medications = (JSONArray) jsonMedicalRecord.get("medications");
			JSONArray allergies = (JSONArray) jsonMedicalRecord.get("allergies");
			List<String> listAllergies = new ArrayList<String>();
			List<String> listMed = new ArrayList<String>();
			for (Object m : medications) {
				listMed.add((String) m);
			}

			for (Object a : allergies) {
				listAllergies.add((String) a);
			}
			MedicalRecord medicalRecord = new MedicalRecord(fn, ln, bd, listMed, listAllergies);
			listMedicalrecords.add(medicalRecord);

		}

	}

}
