package com.people.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.people.People;
import com.people.services.PeopleService;

@RestController
public class PeopleController {

	@Autowired
	private PeopleService peopleService;

	@RequestMapping("people")
	public Set<People> getLondonPeople() {
		Set<People> peopleSet = new HashSet<>();

		peopleSet.addAll(peopleService.getLondonPeople());
		peopleSet.addAll(peopleService.getLondonPeopleByMiles(50));

		return peopleSet;
	}

}
