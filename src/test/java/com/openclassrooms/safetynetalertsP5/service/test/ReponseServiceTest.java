package com.openclassrooms.safetynetalertsP5.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.openclassrooms.safetynetalertsP5.dto.PersonInfoGeneral;
import com.openclassrooms.safetynetalertsP5.dto.PersonsByStationNumber;
import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.repository.ReponseRepository;
import com.openclassrooms.safetynetalertsP5.service.ReponseService;

@RunWith(SpringRunner.class)
@WebMvcTest(ReponseService.class)
@AutoConfigureMockMvc
public class ReponseServiceTest {
	@Autowired
	private ReponseService reponseService;
	@MockBean
	private ReponseRepository reponseRepository;
	FireStation firestation = new FireStation("St_Germain", "5");

	@Test
	public void testFindlistPersonsByStationNumber() throws Exception {
		PersonInfoGeneral person1 = new PersonInfoGeneral("Toto", "Bentiti", "st Germain", "0666777");
		PersonInfoGeneral person2 = new PersonInfoGeneral("leo", "mini", "st Germain", "0666777");
		PersonInfoGeneral person3 = new PersonInfoGeneral("Jane ", "Doe", "st Germain", "0666777");
		List<PersonInfoGeneral> persons = Arrays.asList(person1, person2, person3);
		PersonsByStationNumber people = new PersonsByStationNumber(persons, 1, 2);
		Mockito.when(reponseRepository.listPersonsByStationNumber(any(String.class))).thenReturn(people);
		PersonsByStationNumber listPerson = reponseService.getPersonsByStationNumber(firestation.getStation());
		assertNotNull(listPerson);
		assertEquals(listPerson, people);

		verify(reponseRepository).listPersonsByStationNumber(any(String.class));
	}
}
