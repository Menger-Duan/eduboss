<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>线程查看</title>
<style>
body {font-size:8pt;}
ol {line-height:18px;}
</style>
</head>
<body>
<strong>java.io.tmpdir:</strong>
<ul>
<li><%=System.getProperty("java.io.tmpdir")%></li>
</ul>
<br/>
<strong>Memory:</strong>
<ol>
<li>freeMemory=<%=Runtime.getRuntime().freeMemory()/(1024*1024)%>M</li>
    <li>totalMemory=<%=Runtime.getRuntime().totalMemory()/(1024*1024)%>M</li>
    <li>maxMemory=<%=Runtime.getRuntime().maxMemory()/(1024*1024)%>M</li>
</ol>
<br/>
<strong>Thread:</strong>
<table border="1">
<% int i=1; %>
<tr><td>序号</td><td>名字</td><td>状态</td> <td>类</td></tr>
<%for(Thread t : list_threads()){%>

<tr><td><%=i %></td><td><%=t.getName()%></td><td><%=t.getState()%></td></td> <td> <%=t.getClass().getName()%></td></tr>
<% i++; %>
<%}%>

</table>
<%!
public static java.util.List<Thread> list_threads(){
    int tc = Thread.activeCount();
    Thread[] ts = new Thread[tc];
    Thread.enumerate(ts);
    return java.util.Arrays.asList(ts);
}
%>
</body>
</html>