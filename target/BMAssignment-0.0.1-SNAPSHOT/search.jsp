<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@page import="java.util.ArrayList"%> 
    <%@page import="com.bm.financial.model.beans.StatementBean"%> 
    <%@page import="java.time.format.DateTimeFormatter"%> 
    <%@page import="java.time.LocalDate"%>
    
<style type="text/css"><%@include file="css/filterStyle.css" %></style>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Statements Banking View</title>
</head>
<body>
<%
if(request.getSession().getAttribute("user")== null) {
	response.sendRedirect("login.jsp");
	response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
}
%>

<h1 class="header">Statements Banking View </h1>
<form method="POST" action="search" class ="formContainer">
<div class="filters">

<div class="Item"> 
<label class="filterLabel">Account filter : </label>
<input type="number" name="accountNumber" placeholder ="Account Number" required>
</div>

<div class="Item"> 
<label class="filterLabel" >Amount filter : </label>
<input type="number" name="fromAmount" placeholder="From">
<input type="number" name="toAmount" placeholder="To" >
</div>

<div class="Item">
<label class="filterLabel">Date filter : </label>
<input class="fromDateFilter" type="date" name="fromDate" placeholder="From" >
<input type="date" name="toDate" placeholder="To">
</div>
          
<input type="submit" value="Search" />
<% if (request.getAttribute("ERROR")!=null && !((String)request.getAttribute("ERROR")).isEmpty() ){%>
<p class ="error"><%out.print((String)request.getAttribute("ERROR"));%></p>
 <%} %>
</div> 

<div class="result">

<% 
if( request.getAttribute("searchResult")!=null )
{ 
	ArrayList<StatementBean> result = (ArrayList<StatementBean>)request.getAttribute("searchResult");
	if(result.isEmpty()){
	%>
    <p>No Data to display </p>
    <% } 
	else {
   %>
<table>					
                   <tr>
						<th>Date</th>
						<th>Amount</th>	
					</tr>
					<%
					for(int i = 0 ;i <result.size();i++){
						StatementBean item = result.get(i); 
					%>
					<tr>
						<td>
							<%
					        out.print(item.getDateField()); 
							
							%>
						</td>

						<td>
							<%
							out.print(item.getAmount());
							%>
						</td>
						
					</tr>
					
					<% } %>
				</table>
<%  
   }   
}
%>
</div>
</form>
</body>
</html>