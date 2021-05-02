package com.bm.financial.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bm.financial.bussiness.exceptions.UnauthorizedException;
import com.bm.financial.bussiness.user.Role;
import com.bm.financial.bussiness.user.UserUtil;
import com.bm.financial.model.beans.LoginBean;

/**
 * Servlet implementation class Login
 */
//@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		
		
		LoginBean login = new LoginBean();
		login.setUsername(userName);
		login.setPassword(password);

		try {
			UserUtil.validateLogin(login,request);
			request.getSession().setAttribute("user", login.getUsername());
			List<Role> roles = UserUtil.getRoles(login);
			request.getSession().setAttribute("ROLE",roles);
			RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
			dispatcher.forward(request, response);
		} catch (UnauthorizedException e) {
			request.setAttribute("ERROR", e.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.include(request, response);
			
		}
	  
	}

}
