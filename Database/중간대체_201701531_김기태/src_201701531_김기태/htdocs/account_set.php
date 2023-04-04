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
	<title>계정 관리</title>
	<link rel="icon" href="mococo.png">
</head>
<body>
	<h2>계정 관리하기<br></h2><hr>
	
	<?php
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
		$sql1 = "select accounts.id, accounts.username, accounts.email, count(characters.id) as 'counts', accounts.account_date 
			from accounts LEFT OUTER JOIN characters
			ON characters.id = accounts.id
			group by accounts.id;";
		$ret1 = mysqli_query($con, $sql1);
		
		if($ret1){
			echo "<TABLE BORDER=1>";
			echo "<TR>";
			echo "<TH>계정</TH> <TH>이름</TH> <TH>이메일</TH> <TH>계정의 캐릭터 수</TH> <TH>가입일</TH> <TH>수정하기</TH> <TH>삭제하기</TH>";
			echo "</TR>";
			while($row = mysqli_fetch_array($ret1)) {
				echo "<TR>";
				echo "<TD>", $row['id'], "</TD>";
				echo "<TD>", $row['username'], "</TD>";
				echo "<TD>", $row['email'], "</TD>";
				echo "<TD>", $row['counts'], "개 </TD>";
				echo "<TD>", $row['account_date'], "</TD>";
				echo "<TD>", "<button onclick=\"ismodify(this)\">수정하기</button>", "</TD>";
				echo "<TD>", "<button onclick=\"isdelete(this)\">탈퇴하기</button>", "</TD>";
				echo "</TR>";
			}
		}
		else{
			echo "실패!!!", "<br>";
			echo "실패 원인 :", mysqli_error($con);
		}
		mysqli_close($con);

	?>
	
	<form action="login.html" method="post">
	<input type="submit" value="이전으로">
	</form>
	
	<form name="account_del_ok" action="account_del_ok.php" method="post">
	<input type="hidden" name="id" id="delid">
	<input type="hidden" name="pw" id="delpw">
	</form>
	
	<form name="account_modify_ok" action="account_modify.php" method="post">
	<input type="hidden" name="id" id="modiid">
	<input type="hidden" name="pw" id="modipw">
	</form>

	<script>				
		function isdelete(obj){
			var k = obj.parentNode.parentNode.childNodes;
			var k1 = obj.parentNode.parentNode;
			//alert(k[1].innerText);
			var isdelete_true = prompt("정말로 삭제하시겠습니까? 삭제 시 복구 불가능합니다.\n정말로 삭제를 원한다면 해당 계정의 비밀번호를 입력하세요.\n삭제를 원하는 계정명 : " + k[0].innerText);
			if (isdelete_true) {
				//alert(isdelete_true);
				document.getElementById("delid").value = String(k[0].innerText);
				document.getElementById("delpw").value = isdelete_true;
				document.account_del_ok.submit();
			}
			else {
				// 취소 버튼 클릭 시 동작
				alert("값이 비어있거나, 취소했습니다.");
			}
		}
		function ismodify(obj){
			var k = obj.parentNode.parentNode.childNodes;
			var k1 = obj.parentNode.parentNode;
			var ismodify_true = prompt("수정을 원하시면 해당 계정의 비밀번호를 입력하세요.\n수정을 원하는 계정명 : " + k[0].innerText);
			if (ismodify_true) {
				document.getElementById("modiid").value = String(k[0].innerText);
				document.getElementById("modipw").value = ismodify_true;
				document.account_modify_ok.submit();
			}
			else {
				// 취소 버튼 클릭 시 동작
				alert("값이 비어있거나, 취소했습니다.");
			}
		}
	</script>
	
</body>
</html>