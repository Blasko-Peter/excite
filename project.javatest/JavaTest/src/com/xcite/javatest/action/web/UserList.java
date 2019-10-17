package com.xcite.javatest.action.web;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
	private List<String> activateds;
	private List<String> createDates;
	private List<String> deleteds;
	private List<Long> createDatesInNumber;

	@Override
	public ProcessResult processRequest(ParameterMap parameterMap) throws Throwable {
		ProcessResult result = ProcessResult.createProcessDispatchResult("userList");

		ids = getDataList(databaseName, "id");
		emails = getDataList(databaseName, "email");
		activateds = getDataList(databaseName, "activated");
		createDates = getDataList(databaseName, "createDate");
		deleteds = getDataList(databaseName, "deleted");

		createDatesInNumber = parseDate();
		System.out.println(createDatesInNumber);

		result.addData("content", getTable());

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

	private List<Long> parseDate() throws ParseException {
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

	private String getTable() {
		String allTable = "";
		String tableHead = "<table><tr><th>Id</th><th>Email</th><th>Activated</th><th>Date</th><th>Deleted</th></tr>";
		String tableBody = "";
		for (int i = 0; i < ids.size(); i++) {
			String tableBodyRow = "";
			tableBodyRow = "<tr><td>" + ids.get(i) + "</td><td>" + emails.get(i) + "</td><td>" + activateds.get(i) + "</td><td>" + createDates.get(i) + "</td><td>" + deleteds.get(i) + "</td></tr>";
			tableBody += tableBodyRow;
		}
		String tableFooter = "</table>";
		allTable = tableHead + tableBody + tableFooter;
		return allTable;
	}

}
