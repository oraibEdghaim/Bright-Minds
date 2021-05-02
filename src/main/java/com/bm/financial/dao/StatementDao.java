package com.bm.financial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.bm.financial.bussiness.exceptions.MSSQLException;
import com.bm.financial.bussiness.user.UserUtil;
import com.bm.financial.model.beans.SearchParametersBean;
import com.bm.financial.model.beans.StatementBean;


public class StatementDao {
	
	static Logger logger = Logger.getLogger(StatementDao.class);

	public static List<StatementBean> getAccountStatementResult(Connection connection,SearchParametersBean parameters) throws MSSQLException {

		List statementBeans = new ArrayList<StatementBean>();
		StringBuilder query = new StringBuilder("select * from statement INNER JOIN account on account.id = statement.account_id where ");
		
		query.append(buildWhereClause(parameters));
		logger.log(Priority.INFO, "get account statement query : " + query);
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				StatementBean statementResult = new StatementBean();
				statementResult.setId(rs.getInt("ID"));
				statementResult.setAccountId(rs.getInt("account_id"));
				statementResult.setAmount(rs.getString("amount"));
				statementResult.setDateField(rs.getString("datefield"));
				statementBeans.add(statementResult);

		  } 
		}catch (SQLException e) {
			logger.log(Priority.ERROR, "MS SQL Exception :Error while executing get account statement query");
			throw new MSSQLException("MS SQL Exception :Error while executing MS query ");
		}
		Collections.sort(statementBeans);
		return statementBeans;

	}
	
	private static StringBuilder buildWhereClause(SearchParametersBean parameters) {
	
		StringBuilder where = new StringBuilder();
		boolean hasParams = false;
		
		if(hasDefinedValue(parameters.getAccountNumber()) && !parameters.getAccountNumber().isEmpty()) {
			where.append(" account_number = \"" + parameters.getAccountNumber() +"\" ");
		}
		if(hasDefinedValue(parameters.getFromAmount()) && hasDefinedValue(parameters.getToAmount())) {
			hasParams= true;
			where.append(" AND CLng(amount) between "+ parameters.getFromAmount() +" AND " +  parameters.getToAmount());
		}
		if(hasDefinedValue(parameters.getFromDate()) && hasDefinedValue(parameters.getToDate())) {
			hasParams= true;
			String fromDate = parameters.getFromDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			String toDate = parameters.getToDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			where.append(" AND DateValue(Replace (datefield, \".\",\"-\")) between DateValue("+"\"" + fromDate +  "\"" +") AND DateValue("+ "\""+toDate+ "\"" +")");
		}
		if (!hasParams) {	
			LocalDate currentDate = LocalDate.now();
			String threeMonthsAgoDate = currentDate.minusMonths(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			
			where.append(" AND DateValue(Replace (datefield, \".\",\"-\")) >= DateValue("+"\"" + threeMonthsAgoDate +  "\"" +")");
		}
		
		return where;
	}

	private static boolean hasDefinedValue(Object value) {
		if(value == null) {
			return false;
		}
		return true;
	}
	
}
