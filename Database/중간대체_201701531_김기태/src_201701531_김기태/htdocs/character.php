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
	<title>캐릭터 선택 화면</title>
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
	var hair_num = 0;
	var face_num = 0;
	
	var hair_arr = ['라리엘','엘로디','시간의나라 엘리스','빅토리 라이더','시엘'];
	var face_arr = ['조용한','졸린','아잉','꼬맹이','헤헤'];
	
	function make_character(){
		hair_num = document.getElementById("hair_num").value;
		face_num = document.getElementById("face_num").value;
		
		document.getElementById("hair_num").innerText = String(hair_arr[hair_num])+" 헤어";
		document.getElementById("face_num").innerText = String(face_arr[face_num])+" 얼굴";
		document.getElementById("charimg").src = "char_img/"+String(hair_num)+"x"+String(face_num)+".png";
	}
	</script>
	
	<script>
		function changeSelect(){
			var target = document.getElementById("selectBox");
			if(target.options[target.selectedIndex].value == "캐릭터이름"){
				document.getElementById("select_type_").value = "1";
				document.select_type_form.submit();
			}else if(target.options[target.selectedIndex].value == "소지금"){
				document.getElementById("select_type_").value = "2";
				document.select_type_form.submit();
			}
			//alert('선택된 옵션 text 값=' + target.options[target.selectedIndex].text);
			//alert('선택된 옵션 value 값=' + target.options[target.selectedIndex].value);
		}
	</script>
	
</head>
<body onload="select_sort_type()">
	<form name="select_type_form" action="character.php" method="post">
	<input type="hidden" name="id" value= <?php $id = $_POST["id"]; echo $id?>>
	<input type="hidden" name="select_type" id="select_type_">
	</form>

<div>
	<h2><?php echo $id?>님의 캐릭터<br></h2><hr>
	
	<?php
		if(isset($_POST['select_type'])){
			$sort_select = $_POST['select_type'];
			if($sort_select == "1"){
				$sql = "Select * from characters where id = '{$id}' ORDER BY charactername ASC";
			}
			else if($sort_select == "2"){
				$sql = "Select * from characters where id = '{$id}' ORDER BY money DESC";
			}
		}
		else{
			$sql = "Select * from characters where id = '{$id}' ORDER BY charactername ASC";
			$sort_select = "1";
		}
	?>
	
	<script>
		function select_sort_type(){
			if(<?php echo $sort_select ?> == "1"){
				document.getElementById("sell").selected = true;
			}
			else if(<?php echo $sort_select ?> == "2"){
				document.getElementById("sel2").selected = true;
			}
		}
	</script>
	
	<table>
		<tr>
			<td colspan="4" style="text-align: center;">옵션</td>
			<td style="text-align: center;">캐릭터 정렬방식</td>
		</tr>
		<tr>
			<td>
				<form action="character_add.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id ?>>
				<input type="submit" value="캐릭터 생성">
				</form>
			</td>
			<td>
				<form action="character_modify.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id ?>>
				<input type="submit" value="캐릭터 수정">
				</form>
			</td>
			<td>
				<form action="character_del.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id ?>>
				<input type="submit" value="캐릭터 삭제">
				</form>
			</td>
			<td>
				<button onClick="location.href='login.html'">로그아웃</button>
			</td>
			<td style="text-align: center;">
			<select name="sort" id="selectBox" onchange="changeSelect()">
				<option value="캐릭터이름" id="sel1">캐릭터이름순</option>
				<option value="소지금" id="sel2">소지금순</option>
			</select>
			</td>
		</tr>
	</table>
	
	<?php
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
		$ret = mysqli_query($con, $sql);
		if($ret){
			$count = mysqli_num_rows($ret);
		}
		else{
			echo "캐릭터 로드 실패!!!", "<br>";
			echo "실패 원인 :", mysqli_error($con);
			exit();
		}
		
		$hair_arr = array('라리엘','엘로디','시간의나라 엘리스','빅토리 라이더','시엘');
		$face_arr = array('조용한','졸린','아잉','꼬맹이','헤헤');
		
		echo "<TABLE BORDER=1>";
		echo "<TR>";
		echo "<TH>계정</TH> <TH>캐릭터 이름</TH> <TH>헤어</TH> <TH>성형</TH> <TH>소지금</TH> <TH>접속하기</TH> <TH>캐릭터 이미지</TH>";
		echo "</TR>";
		while($row = mysqli_fetch_array($ret)) {
			echo "<TR>";
			echo "<TD>", $row['id'], "</TD>";
			echo "<TD>", $row['charactername'], "</TD>";
			echo "<TD>", "<p id=\"hair_num\">",$hair_arr[$row['hair']]," 헤어</p>", "</TD>";
			echo "<TD>", "<p id=\"face_num\">",$face_arr[$row['face']]," 얼굴</p>", "</TD>";
			echo "<TD>", $row['money']."원", "</TD>";
			echo "<TD>", 
			"<form action=\"inventory.php\" method=\"post\">
			<input type=\"hidden\" name=\"id\" value= $row[id]>
			<input type=\"hidden\" name=\"charactername\" value= $row[charactername]>
			<input type=\"submit\" value=\"접속하기\">
			</form>", "</TD>";
			echo "<TD><img src=char_img/".$row['hair']."x".$row['face'].".png></TD>";
			echo "</TR>";
		}
		echo "</table>";
	?>
	현재 DB에서 가장 비싼 아이템 TOP5<br>
	<?php
	$top_price_sql = "SELECT * FROM items ORDER BY item_price DESC;";
	$ret_price_sql = mysqli_query($con, $top_price_sql);
	
	$i = 0;
	echo "<TABLE BORDER=1>";
	echo "<TR>";
	echo "<TH>아이템 이름</TH> <TH>아이템 주인 이름</TH> <TH>아이템 가치</TH>";
	echo "</TR>";
	while($row = mysqli_fetch_array($ret_price_sql)) {
		echo "<TR>";
		echo "<TD>", $row['item_name'], "</TD>";
		echo "<TD>", $row['item_owner'], "</TD>";
		echo "<TD>", $row['item_price'], "원</TD>";
		echo "</TR>";
		if($i > 5){
			break;
		}
		$i = $i + 1;
	}
	echo "</table>";
	mysqli_close($con);
	?>
	
	<script>
		function login_check(){
			var inputid = document.getElementById('charactername_t').value;
			if(inputid == ""){
				alert("캐릭터명을 입력해주세요.");
			}else{
				document.getElementById('charactername_t').value = inputid;
				document.character_inventory_check.submit();
			}
		}
	</script>
	
	<br>
	<table>
	<tr>
	<th colspan="3">캐릭터가 보유하고 있는 아이템 보기</th>
	</tr>
	<tr>
	<td>캐릭터 이름</td>
	<td>
	<form name="character_inventory_check" action="character_inventory_check.php" method="post">
	<input type="hidden" name="id" value= <?php echo $id ?>>
	<input type="text" name="charactername", id="charactername_t">
	</form>
	</td>
	<td>
	<button onclick="login_check()">검색하기</button>
	</td>
	</tr>
	</table>
	
</div>
</body>
</html>