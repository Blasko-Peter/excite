package com.xcite.javatest.action.standalone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UserStat {

	private static boolean isTest = true;

	private static int filterYear = 2015;

	public static void main(String[] args) {
		stat1();
		stat2();
		stat3();
		stat4();
	}

	private static void stat1() {
		int i, j, k, l;
		String[] userData = getFile("WebContent/data/users.txt").split("\n");
		String[] subscriptionData = getFile("WebContent/data/newslettersubs.txt").split("\n");

		Map<String, Integer> baseStat = new HashMap<String, Integer>();
		for (i = 1; i < userData.length; i++) {
			String[] temp = userData[i].split(",");

			int userId = Integer.parseInt(temp[0]);
			String firstName = temp[1];
			String lastName = temp[2];
			String email = temp[3];
			String regDate = temp[4];
			boolean subbed = false;
			int listId = 0;
			String subDate = "";
			for (i = 1; i < subscriptionData.length; i++) {
				temp = subscriptionData[i].split(",");
				if (userId == Integer.parseInt(temp[0])) {
					subbed = Boolean.parseBoolean(temp[1]);
					listId = Integer.parseInt(temp[2]);
					subDate = temp[3];
					break;
				}
			}

			String key = email.split("@")[1];
			Integer count = baseStat.get(key);
			if (count == null) {
				count = 0;
			}
			baseStat.put(key, count + 1);
			if (isTest) {
				if (i >= 10) {
					break;
				}
			} else {
				if (i >= 20) {
					break;
				}
			}
		}
		for (Entry<String, Integer> entry : baseStat.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}

	private static void stat2() {
		Map<String, Integer> baseStat = new HashMap<String, Integer>();
		String[] userData = getFile("WebContent/data/users.txt").split("\n");
		for (int i = 1; i < userData.length; i++) {
			String[] temp = userData[i].split(",");
			String key = temp[4].substring(0, 7);
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

	private static void stat3() {
		Map<String, Integer> baseStat = new HashMap<String, Integer>();
		String[] subscriptionData = getFile("WebContent/data/newslettersubs.txt").split("\n");
		for (int i = 1; i < subscriptionData.length; i++) {
			String[] temp = subscriptionData[i].split(",");
			if (temp[1].equals("true")) {
				String key = temp[2] + " " + temp[3].substring(0, 7);
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

	private static void stat4() {
		Map<String, Integer> baseStat = new HashMap<String, Integer>();
		String[] userData = getFile("WebContent/data/users.txt").split("\n");
		String[] subscriptionData = getFile("WebContent/data/newslettersubs.txt").split("\n");
		List<Integer> filteredUserData = new ArrayList<>();
		List<String[]> filteredSubscriptionData = new ArrayList<>();
		for (int i = 1; i < userData.length; i++) {
			String[] temp = userData[i].split(",");
			if (Integer.parseInt(temp[4].substring(0, 4)) > filterYear) {
				filteredUserData.add(Integer.parseInt(temp[0]));
			}
		}
		for (int j = 1; j < subscriptionData.length; j++) {
			String[] temp = subscriptionData[j].split(",");
			if (temp[1].equals("true")) {
				filteredSubscriptionData.add(temp);
			}
		}
		for (int userId : filteredUserData) {
			for (int k = 0; k < filteredSubscriptionData.size(); k++) {
				if (userId == Integer.parseInt(filteredSubscriptionData.get(k)[0])) {
					String key = filteredSubscriptionData.get(k)[2];
					Integer count = baseStat.get(key);
					if (count == null) {
						count = 0;
					}
					baseStat.put(key, count + 1);
				}
			}
		}
		for (Entry<String, Integer> entry : baseStat.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}

	private static String getFile(String path) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File(path)));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		return "";
	}
}
