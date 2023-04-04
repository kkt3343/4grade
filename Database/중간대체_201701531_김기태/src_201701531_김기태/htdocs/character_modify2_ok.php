 <?php 
	$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
	$id = $_POST["id"];
	$charactername = $_POST["charactername"];
	$oldcharactername = $_POST["oldcharactername"];
	$hair = $_POST["hair"];
	$face = $_POST["face"];

	echo "<H2>캐릭터 수정 결과</H2><hr>";
	
	$sql1 = "SELECT count(*) as count from characters WHERE charactername LIKE '{$charactername}'";
	$ret1 = mysqli_query($con, $sql1);
	while ($row = mysqli_fetch_array($ret1)) {
		$count = intval($row['count']);
	}
	if ($count > 0) {
		echo "중복되거나, 기존에 사용한 이름입니다.<br>다른 이름을 사용해 주세요.";
		
		echo "<form action=\"character_modify.php\" method=\"post\">
		<input type=\"hidden\" name=\"id\" value= '{$id}'>
		<input type=\"submit\" value=\"캐릭터 수정 화면으로\">
		</form>";	
	}
	else {
		$sql2 = "UPDATE characters SET charactername = '{$charactername}', hair = '{$hair}', face = '{$face}' WHERE charactername = '{$oldcharactername}'";
		$ret2 = mysqli_query($con, $sql2);
		
		if($ret2) { 
			echo "캐릭터가 성공적으로 수정되었습니다.";
		}
		else { 
			echo "캐릭터 수정 실패"."<BR>";
			echo "실패 원인 :".mysqli_error($con);
		}
		
		echo "<form action=\"character_modify.php\" method=\"post\">
		<input type=\"hidden\" name=\"id\" value= '{$id}'>
		<input type=\"submit\" value=\"캐릭터 수정 화면으로\">
		</form>";	
	}
	mysqli_close($con);
?>