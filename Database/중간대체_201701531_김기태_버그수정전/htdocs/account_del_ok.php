 <?php 
	$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
	$id = $_POST["id"];
	$inputpw = $_POST["pw"];
	
	echo "<h2>계정 삭제</h2><hr>";

	$sql3 = "SELECT * FROM accounts WHERE id LIKE '{$id}'";
	$ret3 = mysqli_query($con, $sql3);

	$truepw = "";
	if($ret3){
		$istrue = mysqli_fetch_array($ret3);
		$truepw = $istrue['pw'];
		
		if($inputpw == $truepw){
			$sql2 = "DELETE FROM characters WHERE id LIKE '{$id}'";
			$ret2 = mysqli_query($con, $sql2);

			$sql = "DELETE FROM accounts WHERE pw LIKE '{$truepw}' and id LIKE '{$id}'";
			$ret = mysqli_query($con, $sql);
			if($ret && $ret2) { 
				echo "계정 삭제 성공"."<BR>";
			}
			else { 
				echo "계정 삭제 실패"."<BR>";
				echo "실패 원인:".mysqli_error($con);
			}
			
		}
		else{
			echo "비밀번호가 올바르지 않습니다.<br>";
		}
	}else{
		echo "계정 삭제 실패"."<BR>";
		echo "실패 원인:".mysqli_error($con);
	}
	echo "<form action=\"account_set.php\" method=\"post\">
	<input type=\"submit\" value=\"확인\">
	</form>";
	
	mysqli_close($con);
?>