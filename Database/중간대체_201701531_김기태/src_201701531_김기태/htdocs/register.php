<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>회원가입</title>
	<link rel="icon" href="mococo.png">
	<style>
		body {
			background-image: url('back.jpg');
			background-repeat: no-repeat;
		}
		div {
			background-color: rgba( 255, 255, 255, 0.7 );
			padding-top: 0px;
			padding-right: 30px;
			padding-bottom: 10px;
			padding-left: 80px;
		}
	</style>
	<script>
		function login_check(){
			var inputid = document.getElementById('formid').value;
			var inputpw = document.getElementById('formpw').value;
			var inputname = document.getElementById('formname').value;
			var inputemail = document.getElementById('formemail').value;
			if(inputid == "" || inputpw == "" || inputname == "" || inputemail == "" ){
				alert("모든 값을 채워주세요.");
			}else{
				document.register.submit();
			}
		}
	</script>
</head>
<body>
	<div><h2> 회원가입 </h2><hr></div>
	<div>
	<form name="register" action="register_ok.php" method="post">
	아이디 : <br><input type="text" name="id" id="formid"><br>
	비밀번호 : <br><input type="text" name="pw" id="formpw"><br>
	이름 : <br><input type="text" name="username" id="formname"><br>
	이메일 : <br><input type="text" name="email" id="formemail"><br>
	<input type="button" onclick="login_check()" value="회원가입">
	</form>
	</div>
	<div><button onClick="location.href='login.html'">취소</button></div>
</body>
</html>