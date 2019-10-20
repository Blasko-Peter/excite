package com.xcite.javatest.action.standalone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

public class UserStatJson {

	private static List<User> users;

	public static void main(String[] args) {
		loadUsersJson();
		seeUser(18995);
		seeUser(22895);
	}

	private static void loadUsersJson() {
		users = new ArrayList<>();
		String jsonText;
		try {
			jsonText = new String((Files.readAllBytes(Paths.get("WebContent/data/users.json"))));
			String jsonTextA = jsonText.substring(1, jsonText.length());
			String[] arrayJson = jsonTextA.split("}");
			String[] userArray = new String[arrayJson.length];
			for (int j = 0; j < arrayJson.length - 1; j++) {
				userArray[j] = arrayJson[j] + "}";
				int i = userArray[j].indexOf("{");
				String userText = userArray[j].substring(i);
				JSONObject jsonFile = new JSONObject(userText);
				Set<String> keys = jsonFile.keySet();
				Integer id = 0;
				String firstname = "";
				String lastname = "";
				String email = "na@na.na";
				String date = "";
				for (String key : keys) {
					if (key.equals("id")) {
						id = (Integer) jsonFile.get("id");
					} else if (key.equals("first_name")) {
						firstname = (String) jsonFile.get("first_name");
					} else if (key.equals("last_name")) {
						lastname = (String) jsonFile.get("last_name");
					} else if (key.equals("email")) {
						email = (String) jsonFile.get("email");
					} else if (key.equals("reg_date")) {
						date = (String) jsonFile.getString("reg_date");
					}
				}
				User newUser = new User(id, firstname, lastname, email, date);
				users.add(newUser);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void seeUser(int index) {
		User user = users.get(index);
		System.out.println(user.getId());
		System.out.println(user.getFirst_name());
		System.out.println(user.getLast_name());
		System.out.println(user.getEmail());
		System.out.println(user.getReg_date());
	}

}
