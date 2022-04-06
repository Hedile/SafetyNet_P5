package com.openclassrooms.safetynetalertsP5.dto;

import org.springframework.stereotype.Component;

@Component
public class PersonInfoGeneral {
	private String firstName;
	private String lastName;
	private String address;
	private String phone;

	public PersonInfoGeneral() {
		super();
	}

	/**
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param phone
	 */
	public PersonInfoGeneral(String firstName, String lastName, String address, String phone) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
