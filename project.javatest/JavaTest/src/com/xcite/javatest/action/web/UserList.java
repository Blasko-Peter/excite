package com.xcite.javatest.action.web;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

		if (parameterMap.get("type") == null) {
			List<Integer> sortIndexOfElements = sortedElementsByDate();
			result.addData("content", getTable(sortIndexOfElements));
		} else {
			if (parameterMap.get("type").equals("update")) {
				Object[] fields = new Object[12];
				fields[0] = "id";
				fields[1] = Integer.parseInt(parameterMap.get("id"));
				fields[2] = "email";
				fields[3] = parameterMap.get("email");
				fields[4] = "password";
				fields[5] = parameterMap.get("password");
				fields[6] = "activated";
				fields[7] = Boolean.parseBoolean(parameterMap.get("activated"));
				fields[8] = "createDate";
				fields[9] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getNewDateForm(parameterMap.get("createDate")));
				fields[10] = "deleted";
				fields[11] = Boolean.parseBoolean(parameterMap.get("deleted"));

				DataBase.update(databaseName, Integer.parseInt(parameterMap.get("id")), fields);

				String newPage = "<a href='/userlist'><h1>Success Update</h1></a>";
				result.addData("content", newPage);
			} else if (parameterMap.get("type").equals("delete")) {

				Object[] fields = new Object[2];
				fields[0] = "deleted";
				fields[1] = Boolean.parseBoolean("true");

				DataBase.update(databaseName, Integer.parseInt(parameterMap.get("id")), fields);

			} else if (parameterMap.get("type").equals("search")) {

				List<Integer> indexOfEmails = searchEmailsByKeyWord(parameterMap.get("word"));
				result.addData("content", getTable(indexOfEmails));

			} else {
				String newEditForm = getEditForm(Integer.parseInt(parameterMap.get("type")));
				result.addData("content", newEditForm);
			}
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

	private String getTable(List<Integer> indexes) {
		String allTable = "";
		String tableHead = "<form action='/userlist?type=search' method='POST'>Search:<br><input type='text' name='word'><br><input type='submit' value='Submit'></form><table><thead><tr><th>Id</th><th>Email</th><th>Password</th><th>Activated</th><th>Date</th><th>Deleted</th><th>Edit</th><th>Delete</th></tr></thead><tbody id='tbody'>";
		String tableBody = "";
		for (Integer index : indexes) {
			String tableBodyRow = "";
			tableBodyRow = "<tr id='row" + ids.get(index) + "'><td>" + ids.get(index) + "</td><td>" + emails.get(index) + "</td><td>" + passwords.get(index) + "</td><td>" + activateds.get(index) + "</td><td>" + createDates.get(index) + "</td><td>"
					+ deleteds.get(index) + "</td><td><a href='/userlist?type=" + index + "'><i style='font-size:24px' class='far'>&#xf328;</i></a></td><td><button id='" + ids.get(index) + "' onclick='deleteRow(this.id)'>Delete</button></td></tr>";
			tableBody += tableBodyRow;
		}
		String tableFooter = "</tbody></table>";
		allTable = tableHead + tableBody + tableFooter;
		return allTable;
	}

	private String getEditForm(int index) {
		String editForm = "<form action='/userlist?type=update' method='POST'>id<br><input type='hidden' name='id' value='" + ids.get(index) + "'><br>email<br><input type='text' name='email' value='" + emails.get(index)
				+ "'><br>password<br><input type='text' name='password' value='" + passwords.get(index) + "'><br>activated<br><input type='text' name='activated' value='" + activateds.get(index)
				+ "'><br>date<br><input type='text' name='createDate' value='" + createDates.get(index) + "'><br>deleted<br><input type='text' name='deleted' value='" + deleteds.get(index) + "'><br><br><input type='submit' value='Submit'></form>";
		return editForm;
	}

	private String getNewDateForm(String oldDateForm) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
		ZonedDateTime zdt = ZonedDateTime.parse(oldDateForm, format);
		long newLong = zdt.toInstant().toEpochMilli();
		Date date = new Date(newLong);
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateText = df2.format(date);
		return dateText;
	}

	private List<Integer> searchEmailsByKeyWord(String keyword) {
		List<Integer> indexOfEmails = new ArrayList<>();
		Set<Integer> indexs = new HashSet<>();
		String[] keywords = keyword.split(" ");
		for (int j = 0; j < keywords.length; j++) {
			for (int i = 0; i < emails.size(); i++) {
				if (emails.get(i).contains(keywords[j])) {
					indexs.add(i);
				}
			}
		}
		for (Integer index : indexs) {
			indexOfEmails.add(index);
		}
		return indexOfEmails;
	}

}
