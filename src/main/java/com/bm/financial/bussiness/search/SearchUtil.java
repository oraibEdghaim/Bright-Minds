package com.bm.financial.bussiness.search;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.bm.financial.bussiness.exceptions.DBConnectionException;
import com.bm.financial.bussiness.exceptions.MSSQLException;
import com.bm.financial.bussiness.exceptions.UnauthorizedException;
import com.bm.financial.bussiness.exceptions.illegalParameterException;
import com.bm.financial.bussiness.user.Feature;
import com.bm.financial.bussiness.user.Role;
import com.bm.financial.bussiness.user.UserUtil;
import com.bm.financial.dao.DBManger;
import com.bm.financial.dao.StatementDao;
import com.bm.financial.model.beans.SearchParametersBean;
import com.bm.financial.model.beans.StatementBean;

public class SearchUtil {
	
	static Logger logger = Logger.getLogger(SearchUtil.class);
	
	public static void validateSearchParameters(SearchParametersBean parameters, List<Role>userRoles ) throws illegalParameterException, UnauthorizedException {
		
		
		StringBuilder errorMessage =new StringBuilder("");
		
		HashSet<Feature> features = UserUtil.getFeatures(userRoles);
		
		validateAccountNumberFilter(parameters,features, errorMessage);
		
		validateAmountFilter(parameters,features, errorMessage);
	
		validateDateFilter(parameters,features, errorMessage);
		
		if(!errorMessage.isEmpty()) {
			logger.log(Priority.ERROR,"Illegal input searching parameters");
			throw new illegalParameterException (errorMessage.toString());
		}
		
	}
	
	private static StringBuilder  validateAccountNumberFilter(SearchParametersBean parameters,HashSet<Feature> features, StringBuilder errorMessage) throws UnauthorizedException {
		
		String accountNumber = parameters.getAccountNumber();
		
		if(!features.contains(Feature.SEARCH_BY_ACCOUNT_NUMBER) && !accountNumber.isEmpty() ) {
			String err= "ACCESS DENIED, User does not have the access by the account number";
			logger.log(Priority.ERROR,err);
			throw new UnauthorizedException(err);
		}
		
		if(accountNumber==null || accountNumber.isEmpty() ) {
			errorMessage.append("Please fill the required account number field \n");
			
		}
		return errorMessage;
	}


	private static StringBuilder validateDateFilter(SearchParametersBean parameters,HashSet<Feature> features, StringBuilder errorMessage) throws UnauthorizedException {
		
		LocalDate fromDate = parameters.getFromDate();
		LocalDate toDate = parameters.getToDate();
		
		if(!features.contains(Feature.SEARCH_BY_DATE) && (fromDate != null || toDate != null) ) {
			String err = "ACCESS DENIED, User does not have the access by the date feature";
			logger.log(Priority.ERROR,err);
			throw new UnauthorizedException(err);
		}
		
		if(fromDate!=null && toDate!= null && (fromDate.isAfter(toDate))) {
			errorMessage.append("The Date filter is not valid, Kindly check the date range \n");
		}
		if(fromDate!=null && toDate== null || (fromDate==null && toDate!= null)) {
			errorMessage.append("The date filter is not valid, please fill the date range \n");
		}
		return errorMessage;
	}


	private static StringBuilder validateAmountFilter(SearchParametersBean parameters,HashSet<Feature> features, StringBuilder errorMessage) throws UnauthorizedException {
		
		Double fromAmount  = parameters.getFromAmount();
		Double toAmount  = parameters.getToAmount();
		
		if(!features.contains(Feature.SEARCH_BY_AMOUT) && (fromAmount != null || toAmount != null) ) {
			String err = "ACCESS DENIED, User does not have the access by the amount feature";
			logger.log(Priority.ERROR, err);
			throw new UnauthorizedException(err);
		}
		
		if(fromAmount!=null && toAmount !=null && (fromAmount > toAmount)) {
			errorMessage.append("The amount filter is not valid, Kindly check the amount range\n");
			
		}
		
		if((fromAmount!=null && toAmount== null) || (fromAmount== null  && toAmount!=null)) {
			errorMessage.append("The amount filter is not valid, please fill the amount range \n");
		}
		return errorMessage;
	}
	
	
	public static List<StatementBean> search (SearchParametersBean parameters) throws DBConnectionException, MSSQLException, IOException{
		
		DBManger manager = DBManger.getInstance();
		Connection conn = manager.getConnection();
		
		return StatementDao.getAccountStatementResult(conn,parameters);
	}

}
