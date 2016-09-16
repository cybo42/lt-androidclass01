<%@page import="com.ltree.kess.server.*, java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Expenses Server</title>
<link rel="StyleSheet" href="css/site.css" type="text/css" media="screen" ></link>
<style>
.error {color:red;}
</style>
</head>
<body>
<div id="wrapper">
<div id="header">
<h2>Expenses Synchronized with Server</h2>
<table>
<tr><th>Description</th><th>Amount</th><th>Date</th>
</tr>
<%
NumberFormat nf = NumberFormat.getCurrencyInstance();
DateFormat df = DateFormat.getDateInstance();
Expense[] expenses = ExpenseServer.readExpenses();
if(null != expenses){
	for(Expense expense : expenses){
%>
<tr><td class="descCol">
<% 
		out.print(expense.description);
%>
</td>
<td class="amountCol">
<%
		out.print(nf.format(expense.amount));
%>
</td>
<td class="dateCol">
<%
		out.print(df.format(expense.expenseDate));
%>
</td>
</tr>
<%
	}
} else {
	out.println("<h1 class='error'>No expenses found. Did your App send any?</h1>");
}
%>
</table>
<br /><br /><br /><br />
<h2>About the Expenses Server</h2>
<p>
	The Expenses server uses REST and JSON. The access URL's are:<br />
	PUT: http://localhost:8080/ExpensesServer/rest/ExpenseServer/putExpenses<br />
	
	GET: http://localhost:8080/ExpensesServer/rest/ExpenseServer/getExpenses<br />
	<br />
	Note: It only works with JSON and not XML payload
	
</p>
</div>
</div>
</body>
</html>