package com.xcite.javatest.action.web;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.xcite.core.interfaces.IWebAction;
import com.xcite.core.servlet.ParameterMap;
import com.xcite.core.servlet.ProcessResult;
import com.xcite.core.utils.DataBase;

public class UserList extends IWebAction {

	private static final String databaseName = "xjfw.account";
	private List<String> ids;
	private List<String> emails;
	private List<String> passwords;
	private List<String> activateds;
	private List<String> createDates;
	private List<String> deleteds;
	private List<Long> createDatesInNumber;
	private List<Integer> sortIndexOfElements;

	@Override
	public ProcessResult processRequest(ParameterMap parameterMap) throws Throwable {
		ProcessResult result = ProcessResult.createProcessDispatchResult("userList");

		ids = getDataList(databaseName, "id");
		emails = getDataList(databaseName, "email");
		passwords = getDataList(databaseName, "password");
		activateds = getDataList(databaseName, "activated");
		createDates = getDataList(databaseName, "createDate");
		deleteds = getDataList(databaseName, "deleted");
		createDatesInNumber = convertStringToLong();
		sortIndexOfElements = sortedElementsByDate();

		if (parameterMap.get("index") == null) {
			result.addData("content", getTable());
		} else {
			String newEditForm = getEditForm(Integer.parseInt(parameterMap.get("index")));
			result.addData("content", newEditForm);
		}

		return result;
	}

	@Override
	public String getAuthenticaionClassname() {
		return null;
	}

	private List<String> getDataList(String database, String field) {
		List<String> dataList = DataBase.selectList(database, field);
		return dataList;
	}

	private List<Long> convertStringToLong() throws ParseException {
		List<Long> dataList = new ArrayList<>();
		for (int i = 0; i < createDates.size(); i++) {
			String input = createDates.get(i);
			DateTimeFormatter format = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
			ZonedDateTime zdt = ZonedDateTime.parse(input, format);
			long newLong = zdt.toInstant().toEpochMilli();
			dataList.add(newLong);
		}
		return dataList;
	}

	private List<Integer> sortedElementsByDate() {
		List<Integer> sortedIndexList = new ArrayList<>();
		List<Long> newCreateDatesInNumber = new ArrayList<>();
		for (Long newNumber : createDatesInNumber) {
			newCreateDatesInNumber.add(newNumber);
		}
		Collections.sort(newCreateDatesInNumber, Collections.reverseOrder());
		for (Long number : newCreateDatesInNumber) {
			for (int i = 0; i < createDatesInNumber.size(); i++) {
				if (createDatesInNumber.get(i) == number) {
					sortedIndexList.add(i);
				}
			}
		}
		return sortedIndexList;
	}

	private String getTable() {
		String allTable = "";
		String tableHead = "<table><tr><th>Id</th><th>Email</th><th>Password</th><th>Activated</th><th>Date</th><th>Deleted</th><th>Edit</th></tr>";
		String tableBody = "";
		for (Integer index : sortIndexOfElements) {
			String tableBodyRow = "";
			tableBodyRow = "<tr><td>" + ids.get(index) + "</td><td>" + emails.get(index) + "</td><td>" + passwords.get(index) + "</td><td>" + activateds.get(index) + "</td><td>" + createDates.get(index) + "</td><td>" + deleteds.get(index)
					+ "</td><td><a href='/userlist?index=" + index + "'><i style='font-size:24px' class='far'>&#xf328;</i></a></td></tr>";
			tableBody += tableBodyRow;
		}
		String tableFooter = "</table>";
		allTable = tableHead + tableBody + tableFooter;
		return allTable;
	}

	private String getEditForm(int index) {
		String editForm = "<form action=''>id<br><input type='text' name='id' value='" + ids.get(index) + "'><br>email<br><input type='text' name='email' value='" + emails.get(index) + "'><br>password<br><input type='text' name='password' value='"
				+ passwords.get(index) + "'><br>activated<br><input type='text' name='activated' value='" + activateds.get(index) + "'><br>date<br><input type='text' name='createDate' value='" + createDates.get(index)
				+ "'><br>deleted<br><input type='text' name='deleted' value='" + deleteds.get(index) + "'><br><br><input type='submit' value='Submit'></form>";
		return editForm;
	}

}
