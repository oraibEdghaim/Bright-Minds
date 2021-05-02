package com.bm.financial.bussiness.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.bm.financial.bussiness.exceptions.UnauthorizedException;
import com.bm.financial.dao.DBManger;
import com.bm.financial.model.beans.LoginBean;

public class UserUtil {

	private static final String USER = "user";
	private static final String ADMIN = "admin";
	
	private static Map<String, String> users;
	private static Map<Role , List<Feature>> rolefeatures ;
	private static Map<Role,List<String>> roleUsers;
	static Logger logger = Logger.getLogger(UserUtil.class);
	
	static {
		// User management should be handled using database.
		initalizeAuthorizedUsers();
		initializeFeatureRoles();
		initializeRoleUsers();
	}
	
	private static void initalizeAuthorizedUsers() {
		
		users = new HashMap<String, String> ();
		users.put(ADMIN, ADMIN);
		users.put(USER, USER);
	}
	
	private static void initializeFeatureRoles() {
		
		rolefeatures = new HashMap<Role, List<Feature>>();
		
		List<Feature> adminFeatures = new ArrayList<Feature>();
		adminFeatures.add(Feature.SEARCH_BY_ACCOUNT_NUMBER);
		adminFeatures.add(Feature.SEARCH_BY_AMOUT);
		adminFeatures.add(Feature.SEARCH_BY_DATE);
		rolefeatures.put(Role.ADMIN, adminFeatures);
		
		List<Feature> userFeatures = new ArrayList<Feature>();
		userFeatures.add(Feature.SEARCH_BY_ACCOUNT_NUMBER);
		rolefeatures.put(Role.USER, userFeatures);
	}
	
	private static void initializeRoleUsers() {
		
		roleUsers = new HashMap<Role, List<String>>();
		
		List<String> userNames = new ArrayList<String>();
		userNames.add(USER);
		roleUsers.put(Role.USER, userNames);
		
		List<String> adminNames = new ArrayList<String>();
		adminNames.add(ADMIN);
		roleUsers.put(Role.ADMIN, adminNames);
	}
	
	public static void validateLogin(LoginBean login, HttpServletRequest request) throws UnauthorizedException {
		
		validateLoginAccount(login);
		validateLoginSession(login, request);
	}

	private static void validateLoginAccount(LoginBean login) throws UnauthorizedException {
		String password = users.get(login.getUsername());
		if (password == null || !password.equals(login.getPassword())){
			String err="Invalid credentials, Please try again !";
			logger.log(Priority.ERROR,err+ login.getUsername());
			throw new UnauthorizedException(err);
		}
	}

	private static void validateLoginSession(LoginBean login, HttpServletRequest request) throws UnauthorizedException {
		LoginBean userData = (LoginBean) request.getSession().getAttribute("userData");
        if (userData == null) {
	          request.getSession().setAttribute("userData", login);
	      }else if (userData!=null && !userData.equals(login)){
	    	  request.getSession().setAttribute("userData", login);
	      }
	      else{
	          throw new UnauthorizedException("You are already logged in from a different session. Please logout first.");
	      }
	}
    
	public static ArrayList<Role> getRoles(LoginBean login){
		
		String userName = login.getUsername();
		ArrayList<Role> roles = new ArrayList<Role>();
		
		for (Entry<Role, List<String>> entry : roleUsers.entrySet()) {
            if (entry.getValue().contains(userName)) {
            	roles.add(entry.getKey());
            }
        }
		return roles;
	}
	
	public static HashSet<Feature> getFeatures(List<Role> roles){
			
			HashSet<Feature> features = new HashSet<Feature>();
			
			for(Role role : roles) {
				if (!rolefeatures.containsKey(role)) {
					continue;
				}
				features.addAll(rolefeatures.get(role));
			}
			return features;
		}
}
