package com.xcite.javatest.action.standalone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

public class UserStatJson {

	private static List<User> users;
	private static List<Newslettersub> newslettersubs;
	private static boolean isTest = true;
	private static int filterYear = 2015;

	public static void main(String[] args) {
		loadUsersJson();
		loadNewslettersubJson();
		stat1J();
		stat2J();
		stat3J();
		stat4J();
	}

	private static void stat1J() {
		int i;
		Map<String, Integer> baseStat = new HashMap<String, Integer>();
		for (i = 0; i < users.size(); i++) {
			User newUser = users.get(i);
			int userId = newUser.getId();
			for (i = 0; i < newslettersubs.size(); i++) {
				Newslettersub newNewslettersub = newslettersubs.get(i);
				if (userId == newNewslettersub.getId()) {
					break;
				}
			}
			String key = newUser.getEmail().split("@")[1];
			Integer count = baseStat.get(key);
			if (count == null) {
				count = 0;
			}
			baseStat.put(key, count + 1);
			if (isTest) {
				if (i >= 9) {
					break;
				}
			} else {
				if (i >= 19) {
					break;
				}
			}
		}
		for (Entry<String, Integer> entry : baseStat.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}

	private static void stat2J() {
		Map<String, Integer> baseStat = new HashMap<String, Integer>();
		for (User user : users) {
			String key = user.getReg_date().substring(0, 7);
			Integer count = baseStat.get(key);
			if (count == null) {
				count = 0;
			}
			baseStat.put(key, count + 1);
		}
		for (Entry<String, Integer> entry : baseStat.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}

	private static void stat3J() {
		Map<String, Integer> baseStat = new HashMap<String, Integer>();
		for (Newslettersub newslettersub : newslettersubs) {
			if (newslettersub.getSubscribed().equals(true)) {
				String key = newslettersub.getListid() + " " + newslettersub.getCreatedate().substring(0, 7);
				Integer count = baseStat.get(key);
				if (count == null) {
					count = 0;
				}
				baseStat.put(key, count + 1);
			}
		}
		for (Entry<String, Integer> entry : baseStat.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}

	private static void stat4J() {
		Map<String, Integer> baseStat = new HashMap<String, Integer>();
		for (User user : users) {
			if (Integer.parseInt(user.getReg_date().substring(0, 4)) > filterYear) {
				for (Newslettersub newslettersub : newslettersubs) {
					if (user.getId() == newslettersub.getId() && newslettersub.getSubscribed().equals(true)) {
						String key = String.valueOf(newslettersub.getListid());
						Integer count = baseStat.get(key);
						if (count == null) {
							count = 0;
						}
						baseStat.put(key, count + 1);
					}
				}
			}
		}
		for (Entry<String, Integer> entry : baseStat.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
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

	private static void loadNewslettersubJson() {
		newslettersubs = new ArrayList<>();
		String jsonText;
		try {
			jsonText = new String((Files.readAllBytes(Paths.get("WebContent/data/newslettersubs.json"))));
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
				Boolean subscribed = false;
				Integer listid = 0;
				String createdate = "0000-00-00 00:00:00";
				for (String key : keys) {
					if (key.equals("id")) {
						id = (Integer) jsonFile.get("id");
					} else if (key.equals("subscribed")) {
						subscribed = (Boolean) jsonFile.get("subscribed");
					} else if (key.equals("listid")) {
						listid = (Integer) jsonFile.get("listid");
					} else if (key.equals("createdate")) {
						createdate = (String) jsonFile.get("createdate");
					}
				}
				Newslettersub newNewslettersub = new Newslettersub(id, subscribed, listid, createdate);
				newslettersubs.add(newNewslettersub);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
