<!DOCTYPE HTML>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
	<head>
		<meta charset="utf-8">
    
    	<title>Lista de tarefas</title>
    
    	<link href="static/css/bootstrap.min.css" rel="stylesheet">
    	<link href="static/css/bootstrap-theme.min.css" rel="stylesheet">
    	<link href="static/css/style.css" rel="stylesheet">
    	<script src="static/js/bootstrap.min.js"></script>
	</head>
	<body>
	
		<nav class="navbar navbar-inverse navbar-static-top" role="navigation">
	  		<div class="container-fluid">
	    		<div class="navbar-header">
	      			<a class="navbar-brand" href="#">Tarefas</a>
	    		</div>
	  		</div>
		</nav>
	
		<c:choose>
			<c:when test="${tarefas.size() == 0}">
				<div class="container" id="homeDiv">
				<div class="alert alert-success" role="alert">
				<b>Sem tarefas cadastradas.</b>
					Envie um post request para <i>localhost:8080/rest/tarefa</i>
				</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="container text-center" id="tarefasDiv">
					<br> 
					<div class="table-responsive">
						<table class="table table-striped table-bordered text-left">
							<thead>
								<tr>
									<th>Id</th>
									<th>Nome</th>
									<th>Descrição</th>
									<th>Data</th>
									<th>Finalizada</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="tarefa" items="${tarefas}">
									<tr>
										<td>${tarefa.id}</td>
										<td>${tarefa.titulo}</td>
										<td>${tarefa.descricao}</td>
										<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${tarefa.data}"/></td>
										<td>${tarefa.finalizada ? 'Sim' : 'Não'}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</c:otherwise>		
		</c:choose>
	</body>
</html>
