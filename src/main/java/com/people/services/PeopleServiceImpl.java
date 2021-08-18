package com.people.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.people.People;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;

@Service
public class PeopleServiceImpl implements PeopleService {

	private final static String ALL_USERS_URL = "https://bpdts-test-app.herokuapp.com/users";
	private final static String LONDON_USERS_URL = "https://bpdts-test-app.herokuapp.com/city/London/users";

	@Override
	public List<People> getLondonPeople() {
		return parse(getResponse(LONDON_USERS_URL));
	}

	@Override
	public List<People> getLondonPeopleByMiles(int miles) {
		return parse(getResponse(ALL_USERS_URL)).stream().filter(x -> filter(x, miles)).collect(Collectors.toList());
	}

	private String getResponse(String urlString) {
		HttpURLConnection connection = null;
		BufferedReader br = null;
		try {
			connection = (HttpURLConnection) new URL(urlString).openConnection();
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	// Parse the returned JSON data
	private List<People> parse(String responseBody) throws JSONException {

		List<People> peopleList = new ArrayList<>();
		JSONArray usersLondon = new JSONArray((responseBody));
		for (int i = 0; i < usersLondon.length(); i++) {
			JSONObject userLondon = usersLondon.getJSONObject(i);
			People user = new People();
			user.setId(userLondon.getInt("id"));
			user.setFirst_name(userLondon.getString("first_name"));
			user.setLast_name(userLondon.getString("last_name"));
			user.setEmail(userLondon.getString("email"));
			user.setIp_address(userLondon.getString("ip_address"));
			user.setLatitude(userLondon.getDouble("latitude"));
			user.setLongitude(userLondon.getDouble("longitude"));
			peopleList.add(user);

		}
		return peopleList;
	}

	private boolean filter(People people, int miles) {
		GeodesicData result = Geodesic.WGS84.Inverse(51.5074, 0.1278, people.getLatitude(), people.getLongitude());

		return (result.s12 / 1609.34) <= miles;
	}

}
