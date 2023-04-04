 <?php 
	$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
	$id = $_POST["id"];
	$charactername = $_POST["charactername"];

	echo "<h2>{$id}님의 캐릭터 삭제하기</h2><hr>";
	
	$sql = "DELETE FROM characters WHERE charactername LIKE '{$charactername}'";
	$ret = mysqli_query($con, $sql);
	if($ret) { 
		echo "캐릭터 삭제 성공"."<BR>";
	}
	else { 
		echo "캐릭터 삭제 실패"."<BR>";
		echo "실패 원인:".mysqli_error($con);
	}
	
	echo "<form action=\"character_del.php\" method=\"post\">
	<input type=\"hidden\" name=\"id\" value= {$id}>
	<input type=\"submit\" value=\"이전으로\">
	</form>";
	
	mysqli_close($con);
?>