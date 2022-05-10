package com.openclassrooms.safetynetalertsP5;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.model.Person;

@Component
public class SaveDataJSON {

	JSONObject obj = new JSONObject();
	JSONArray jsonArrayPersons = new JSONArray();
	JSONArray jsonArrayFireStations = new JSONArray();
	JSONArray jsonArrayMedicalRecords = new JSONArray();

	public void saveData() {

		for (Person p : LoadDataJSON.listPersons) {
			JSONObject objPerson = new JSONObject();
			objPerson.put("firstName", p.getFirstName());
			objPerson.put("lastName", p.getLastName());
			objPerson.put("address", p.getAddress());
			objPerson.put("city", p.getCity());
			objPerson.put("zip", p.getZip());
			objPerson.put("phone", p.getPhone());
			objPerson.put("email", p.getEmail());
			jsonArrayPersons.add(objPerson);
			// System.out.println(objPerson);
		}
		for (FireStation f : LoadDataJSON.listFirestations) {
			JSONObject objFireStation = new JSONObject();
			objFireStation.put("address", f.getAddress());
			objFireStation.put("station", f.getStation());
			jsonArrayFireStations.add(objFireStation);
			// System.out.println(objFireStation);
		}
		for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
			JSONObject objMedicalRecord = new JSONObject();
			objMedicalRecord.put("firstName", m.getFirstName());
			objMedicalRecord.put("lastName", m.getLastName());
			objMedicalRecord.put("birthdate", m.getBirthdate());
			objMedicalRecord.put("medications", m.getMedications());
			objMedicalRecord.put("allergies", m.getAllergies());
			jsonArrayMedicalRecords.add(objMedicalRecord);
			// System.out.println(objMedicalRecord);
		}
		obj.put("persons", jsonArrayPersons);
		obj.put("firestations", jsonArrayFireStations);
		obj.put("medicalrecords", jsonArrayMedicalRecords);
		String fileName = "src/main/resources/data.json";
		File fw = new File(fileName);
		try {
			FileWriter file = new FileWriter(fw);

			file.write(obj.toJSONString());

			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
