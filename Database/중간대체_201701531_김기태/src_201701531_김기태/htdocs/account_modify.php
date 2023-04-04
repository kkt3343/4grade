<!DOCTYPE html>
<head>
	<meta charset="UTF-8">
	<style>
		table,td {
			border: 1px solid #333;
		}
		thead,tfoot {
			background-color: #333;
			color: #fff;
		}
	</style>
</head>
<body>
	<h2>계정 수정<br></h2><hr>
	
	<?php
		$id = $_POST["id"];
		$inputpw = $_POST["pw"];
			
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
		$sql = "Select * from accounts WHERE id = '{$id}'";
		$ret = mysqli_query($con, $sql);
		
		$truepw = "";
		if($ret){
			$istrue = mysqli_fetch_array($ret);
			$truepw = $istrue['pw'];
			if($inputpw == $truepw){
				echo "수정을 원하시면 신규행의 빈값을 채워주세요.<br>";
				echo "<TABLE BORDER=1>";
				echo "<TR>";
				echo "<TH></TH> <TH>계정</TH> <TH>비밀번호</TH> <TH>이름</TH> <TH>이메일</TH> <TH>가입일</TH>";
				echo "</TR>";
				echo "<TR>";
				echo "<TD> 기존 </TD>";
				echo "<TD>", $istrue['id'], "</TD>";
				echo "<TD>", $istrue['pw'], "</TD>";
				echo "<TD>", $istrue['username'], "</TD>";
				echo "<TD>", $istrue['email'], "</TD>";
				echo "<TD>", $istrue['account_date'], "</TD>";
				echo "</TR>";
				
				echo "<TR>";
				echo "<TD> 신규 </TD>";
				echo "<TD>", $istrue['id'], "</TD>";
				echo "<TD> <input type=\"text\" id=\"newpw\"> </TD>";
				echo "<TD> <input type=\"text\" id=\"newusername\"> </TD>";
				echo "<TD> <input type=\"text\" id=\"newemail\"> </TD>";
				echo "<TD>", $istrue['account_date'], "</TD>";
				echo "</TR>";
				echo "</TABLE>";
				
				echo "<table>
				<tr style=\"text-align: center;\">
				<td width=\"300\">
				<form action=\"account_set.php\" method=\"post\">
				<input type=\"submit\" value=\"이전으로\">
				</form>
				</td>
				<td width=\"300\">
				<button onclick=\"ismodi()\">수정하기</button>
				</td>
				</tr>
				</table>";
			}
			else{
				echo "비밀번호가 올바르지 않습니다.<br>";
				echo "<form action=\"account_set.php\" method=\"post\">
				<input type=\"submit\" value=\"이전으로\">
				</form>";
			}
		}
		else{
			echo "실패!!!", "<br>";
			echo "실패 원인 :", mysqli_error($con);
		}
		mysqli_close($con);

	?>
	
	<form name="account_modify_ok" action="account_modify_ok.php" method="post">
	<input type="hidden" name="id" id="modiid">
	<input type="hidden" name="pw" id="modipw">
	<input type="hidden" name="username" id="modiusername">
	<input type="hidden" name="email" id="modiemail">
	</form>
	
	<script>
		var id = <?php echo "\"{$id}\"" ?>;
		function ismodi(){
			var pw = document.getElementById("newpw").value;
			var username = document.getElementById("newusername").value;
			var email = document.getElementById("newemail").value;

			if(pw==""||username==""||email==""){
				alert("모든 값을 채워주세요");
			}
			else{
				document.getElementById("modiid").value = id;
				document.getElementById("modipw").value = pw;
				document.getElementById("modiusername").value = username;
				document.getElementById("modiemail").value = email;
				document.account_modify_ok.submit();
			}
		}
	</script>
	
</body>
</html>