package com.openclassrooms.safetynetalertsP5.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.openclassrooms.safetynetalertsP5.model.Person;

@Component
public class Child {
	private String firstName;
	private String lastName;
	private int age;
	private List<Person> childsFamily;

	public Child() {
	}

	public Child(String firstName, String lastName, int age, List<Person> family) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.childsFamily = family;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Person> getChildsFamily() {
		return childsFamily;
	}

	public void setChildsFamily(List<Person> childsFamily) {
		this.childsFamily = childsFamily;
	}

}
