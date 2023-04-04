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
	<title>캐릭터 수정하기</title>
	<link rel="icon" href="mococo.png">
</head>
<body>
	<h2><?php $id = $_POST["id"]; echo $id?>님의 캐릭터 수정하기<br></h2><hr>
	
	<?php
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
		$sql = "Select * from characters where id LIKE '{$id}'";
		
		$ret = mysqli_query($con, $sql);
		if($ret){
			$count = mysqli_num_rows($ret);
		}
		else{
			echo "userTBL 데이터 검색 실패!!!", "<br>";
			echo "실패 원인 :", mysqli_error($con);
			exit();
		}
		
		$hair_arr = array('라리엘','엘로디','시간의나라 엘리스','빅토리 라이더','시엘');
		$face_arr = array('조용한','졸린','아잉','꼬맹이','헤헤');
		
		echo "<TABLE BORDER=1>";
		echo "<TR>";
		echo "<TH>계정</TH> <TH>캐릭터 이름</TH> <TH>헤어</TH> <TH>성형</TH> <TH>수정하기</TH> <TH>캐릭터 이미지</TH>";
		echo "</TR>";
		while($row = mysqli_fetch_array($ret)) {
			echo "<TR>";
			echo "<TD>", $row['id'], "</TD>";
			echo "<TD>", $row['charactername'], "</TD>";
			echo "<TD>", "<div id=\"hair_num\">",$hair_arr[$row['hair']]," 헤어</div>", "</TD>";
			echo "<TD>", "<div id=\"face_num\">",$face_arr[$row['face']]," 얼굴</div>", "</TD>";
			echo "<TD>", "<button onclick=\"ismodify(this)\">수정하기</button>", "</TD>";
			echo "<TD><img src=char_img/".$row['hair']."x".$row['face'].".png></TD>";
			echo "</TR>";
		}
		mysqli_close($con);

	?>
	
	<form action="character.php" method="post">
	<input type="hidden" name="id" value= <?php echo $id ?>>
	<input type="submit" value="이전으로">
	</form>
	
	<form name="character_modify2" action="character_modify2.php" method="post">
	<input type="hidden" name="id" value= <?php echo $id ?>>
	<input type="hidden" name="charactername", id="charactername">
	</form>

	<script>				
		function ismodify(obj){
			var k = obj.parentNode.parentNode.childNodes;
			document.getElementById("charactername").value = String(k[1].innerText);
			document.character_modify2.submit();
		}
	</script>
	
</body>
</html>