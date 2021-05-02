package com.bm.financial.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bm.financial.bussiness.exceptions.DBConnectionException;
import com.bm.financial.bussiness.exceptions.MSSQLException;
import com.bm.financial.bussiness.exceptions.UnauthorizedException;
import com.bm.financial.bussiness.exceptions.illegalParameterException;
import com.bm.financial.bussiness.search.SearchUtil;
import com.bm.financial.bussiness.user.Role;
import com.bm.financial.model.beans.SearchParametersBean;
import com.bm.financial.model.beans.StatementBean;

/**
 * Servlet implementation class Search
 */
//@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String searchAction = request.getParameter("Search");
		String logOutAction = request.getParameter("LogOut");
		
		if(logOutAction != null) {
			request.getSession().invalidate();
			RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.include(request, response);
		}else if (searchAction != null) {
		SearchParametersBean params = setSearchParamtersBean(request);
		List<StatementBean> result = new ArrayList<StatementBean>();
		List<Role> userRoles = (List<Role>) request.getSession().getAttribute("ROLE");

		if (doesUserHaveSession(request, response)) {
			try {
				SearchUtil.validateSearchParameters(params, userRoles);
				result = SearchUtil.search(params);
			} catch (illegalParameterException error) {
				request.setAttribute("ERROR", error.getMessage());
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} catch (DBConnectionException | MSSQLException error) {
				request.setAttribute("ERROR", error.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (UnauthorizedException error) {
				request.setAttribute("ERROR", error.getMessage());
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} finally {
				request.setAttribute("searchResult", result);
				RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
				dispatcher.include(request, response);
			}

		}
		}
	}

	private boolean doesUserHaveSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("user")== null) {
			response.sendRedirect("login.jsp");
			return false;
		}
		return true;
	}

	private SearchParametersBean setSearchParamtersBean(HttpServletRequest request) {
		Map <String,String[]> paramtersMap =  request.getParameterMap();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		SearchParametersBean params = new SearchParametersBean();
		params.setAccountNumber(paramtersMap.get("accountNumber")[0]);

		LocalDate fromDate = paramtersMap.get("fromDate")[0].isEmpty() ? null :  LocalDate.parse(paramtersMap.get("fromDate")[0], formatter);
		LocalDate toDate = paramtersMap.get("toDate")[0].isEmpty() ? null : LocalDate.parse(paramtersMap.get("toDate")[0], formatter);
		 
		params.setFromDate(fromDate);
		params.setToDate(toDate);
		
		Double fromAmount = paramtersMap.get("fromAmount")[0].isEmpty() ? null : Double.parseDouble(paramtersMap.get("fromAmount")[0]);
		Double toAmount =  paramtersMap.get("toAmount")[0].isEmpty() ? null :  Double.parseDouble(paramtersMap.get("toAmount")[0]);
		
		params.setFromAmount(fromAmount);
		params.setToAmount(toAmount);
		return params;
	}

}
