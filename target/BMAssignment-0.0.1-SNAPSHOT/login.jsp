<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <style type="text/css"><%@include file="css/loginStyle.css" %></style>
  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Form</title>
</head>
<body>

<div class="container">
    <div class="logo">Welcome to Online Banking</div>
    <div class="login-item">
     <form action="login" method="post" class="form form-login">
        <div class="form-field">
          <label class="user" for="login-username"><span class="hidden">Username</span></label>
          <input id="login-username" type="text" name="username" class="form-input" placeholder="Username" required>
        </div>

        <div class="form-field">
          <label class="lock" for="login-password"><span class="hidden">Password</span></label>
          <input id="login-password" type="password" name="password"class="form-input" placeholder="Password" required>
        </div>

        <div class="form-field">
          <input type="submit" value="Log in">
        </div>
        
     <% if( request.getAttribute("ERROR")!=null && !((String)request.getAttribute("ERROR")).isEmpty()){ %>
    <div>Invalid credentials, Please try again !</div>
    <% } %>
    
      </form>
    </div>
</div>
</body>