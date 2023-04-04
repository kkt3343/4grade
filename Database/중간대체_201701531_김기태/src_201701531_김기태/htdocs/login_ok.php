<!DOCTYPE html>
<head>
	<meta charset="UTF-8">
</head>
<body>
	<?php
		$id = $_POST["id"];
		$pw = $_POST["pw"];
		
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");

		$sql = "SELECT * from accounts WHERE id LIKE '{$id}'";
		$ret = mysqli_query($con, $sql);
		$row = mysqli_fetch_array($ret);
	?>

	<?php
		echo "<H2>로그인</H2><hr>";
		if ($row) {
			$mypw = $row['pw'];
			if($mypw == $pw){
	?>
				<form name="login" action="character.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id ?>><br>
				<input type="hidden" name="pw" value= <?php echo $pw ?>><br>
				</form>
				<script>
					document.login.submit();
				</script>
	<?php			
			}
			else{
				echo "비밀번호가 일치하지 않습니다.";
				echo "<br><button onClick=\"location.href='login.html'\">이전으로</button>";
			}
		}
		else {
			echo "아이디가 존재하지 않습니다.";
			echo "<br><button onClick=\"location.href='login.html'\">이전으로</button>";
		}
		mysqli_close($con);
	?>
</body>
</html>