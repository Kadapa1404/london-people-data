package com.people.services;

import java.util.List;

import com.people.People;

public interface PeopleService {

	List<People> getLondonPeople();
	
	List<People> getLondonPeopleByMiles(int miles);
}
