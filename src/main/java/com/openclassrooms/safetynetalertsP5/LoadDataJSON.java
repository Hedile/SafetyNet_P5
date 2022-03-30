package com.openclassrooms.safetynetalertsP5;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.model.Person;

@Component
public class LoadDataJSON {

	public static ArrayList<Person> listPersons = new ArrayList<Person>();
	public static List<FireStation> listFirestations = new ArrayList<FireStation>();
	public static List<MedicalRecord> listMedicalrecords = new ArrayList<MedicalRecord>();

	private static JSONObject loadJsonFile() {

		String url = "C:\\Users\\chedi\\Desktop\\openclassrooms\\SafetyNetAlerts\\src\\main\\resources\\data.json";
		try {

			JSONParser jsonParser = new JSONParser();
			Reader reader = new FileReader(url);

			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void loadData() {
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
		List<String> listMed = new ArrayList<String>();
		List<String> listAllergies = new ArrayList<String>();
		for (Object med : jsonArrayMedicalRecords) {
			JSONObject jsonMedicalRecord = (JSONObject) med;
			String fn = (String) jsonMedicalRecord.get("firstName");
			String ln = (String) jsonMedicalRecord.get("lastName");
			String bd = (String) jsonMedicalRecord.get("birthdate");
			JSONArray medications = (JSONArray) jsonMedicalRecord.get("medications");
			JSONArray allergies = (JSONArray) jsonMedicalRecord.get("allergies");
			for (Object m : medications) {
				listMed.add((String) m);
			}
			for (Object a : allergies) {
				listAllergies.add((String) a);
			}
			MedicalRecord medicalRecord = new MedicalRecord(fn, ln, bd, medications, allergies);
			listMedicalrecords.add(medicalRecord);

		}

	}

}