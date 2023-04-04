 <?php 
	$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
	$id = $_POST["id"];
	$pw = $_POST["pw"];
	$username = $_POST["username"];
	$email = $_POST["email"];

	$sql1 = "SELECT * from accounts WHERE id LIKE '{$id}'";
	$ret1 = mysqli_query($con, $sql1);
	$row = mysqli_fetch_array($ret1);
	
	echo "<H2>계정 수정</H2><hr>";
	
	$sql2 = "UPDATE accounts SET pw = '{$pw}', username = '{$username}', email = '{$email}' WHERE id LIKE '{$id}'";
	$ret2 = mysqli_query($con, $sql2);
	
	if($ret1 && $ret2) { 
		echo "계정정보가 수정되었습니다.";
	}
	else { 
		echo "수정 실패"."<BR>";
		echo "실패 원인 :".mysqli_error($con);
	}
		
	echo "<form action=\"account_set.php\" method=\"post\">
	<input type=\"submit\" value=\"확인\">
	</form>";	
	mysqli_close($con);
?>