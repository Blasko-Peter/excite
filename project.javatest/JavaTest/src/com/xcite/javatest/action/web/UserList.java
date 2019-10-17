package com.xcite.javatest.action.web;

import java.util.List;

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

	@Override
	public ProcessResult processRequest(ParameterMap parameterMap) throws Throwable {
		ProcessResult result = ProcessResult.createProcessDispatchResult("userList");

		ids = getDataList(databaseName, "id");
		emails = getDataList(databaseName, "email");
		activateds = getDataList(databaseName, "activated");
		createDates = getDataList(databaseName, "createDate");
		deleteds = getDataList(databaseName, "deleted");

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

}
