<%@ page 	language="java" 
			contentType="text/html; charset=UTF-8"
    		pageEncoding="UTF-8" %>

<%@ taglib	prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  

<!DOCTYPE html>
<html>
		<head>
		  <meta charset="UTF-8">
		  <title>AdminLTE 2 | Log in</title>
		  <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		  <!-- Bootstrap 3.3.4 -->
		  <link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		  <!-- Font Awesome Icons -->
		  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
		  <!-- Theme style -->
		  <link href="resources/dist/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />
		  <!-- iCheck -->
		  <link href="resources/plugins/iCheck/square/blue.css" rel="stylesheet" type="text/css" />
	  
		</head>
		<body class="login-page">
		  <div class="login-box">
			<div class="login-logo">
			  <a href="#"><b>KDT-MULTICAMPUS</b></a>
			</div><!-- /.login-logo -->
			<div class="login-box-body">
			  <p class="login-box-msg">Sign in to start your session</p>
	  
      <!-- 변수 id와 pwd를 가지고 submit버튼을 누르면 action의 경로에 post로 매핑해게됨 -->
      <!-- 매핑은 UserController에서 받도록했음 -->
	  <form action="./user/login.multicampus" method="post">
		<div class="form-group has-feedback">
		  <input type="text" name="id" class="form-control" placeholder="USER ID"/>
		  <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
		</div>
		<div class="form-group has-feedback">
		  <input type="password" name="pwd" class="form-control" placeholder="Password"/>
		  <span class="glyphicon glyphicon-lock form-control-feedback"></span>
		</div>
		${loginFail}
		<div class="row">
		  <div class="col-xs-8">    
			<div class="checkbox icheck">
			  <label>
				<input type="checkbox" name="useCookie"> Remember Me
			  </label>
			</div>                        
		  </div><!-- /.col -->
		  <div class="col-xs-4">
			<button type="submit" class="btn btn-primary btn-block btn-flat">Sign In</button>
		  </div><!-- /.col -->
		</div>
	  </form>
	  		
	
			<a href="#">I forgot my password</a><br>
	  		<a href="/user/join.multicampus" class="text-center">Register a new membership</a>
	  
			</div><!-- /.login-box-body -->
		  </div><!-- /.login-box -->
	  
		  <!-- jQuery 2.1.4 -->
		  <script src="resources/plugins/jQuery/jQuery-2.1.4.min.js"></script>
		  <!-- Bootstrap 3.3.2 JS -->
		  <script src="resources/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
		  <!-- iCheck -->
		  <script src="resources/plugins/iCheck/icheck.min.js" type="text/javascript"></script>
		  <script>
			$(function () {
			  $('input').iCheck({
				checkboxClass: 'icheckbox_square-blue',
				radioClass: 'iradio_square-blue',
				increaseArea: '20%' // optional
			  });
			});
		  </script>
		</body>
	  </html>