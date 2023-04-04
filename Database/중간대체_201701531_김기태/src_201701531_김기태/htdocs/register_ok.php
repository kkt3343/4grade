<html>
<head>
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
</head>
<body>
<div>
<?php 
	$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
	$id = $_POST["id"];
	$pw = $_POST["pw"];
	$username = $_POST["username"];
	$email = $_POST["email"];
	
	$sql1 = "SELECT count(*) as count from accounts WHERE id LIKE '{$id}'";
	$ret1 = mysqli_query($con, $sql1);
	while ($row = mysqli_fetch_array($ret1)) {
		$count = intval($row['count']);
	}
	if ($count > 0) {
		echo "<H2>회원가입결과</H2>";
		echo "중복 id 입니다.";
		echo "<br><button onClick=\"location.href='register.php'\">회원가입 화면으로</button>";
	}
	else {
		$sql2 = "INSERT INTO accounts VALUES('{$id}', '{$pw}', '{$username}', '{$email}', sysdate())";
		$ret2 = mysqli_query($con, $sql2);
		
		echo "<H2>회원가입결과</H2>";
		if($ret2) { 
			echo "회원가입을 축하합니다.";
		}
		else { 
			echo "회원가입에 실패하였습니다."."<BR>";
			echo "실패 원인 :".mysqli_error($con);
		}
		echo "<br><button onClick=\"location.href='login.html'\">로그인 화면으로</button>";
	}
	mysqli_close($con);
?>
</div>
</body>
</html>