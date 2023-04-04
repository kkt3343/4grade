<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>캐릭터 생성</title>
	<link rel="icon" href="mococo.png">
	<style>
		table,td {
			border: 1px solid #333;
		}
		thead,tfoot {
			background-color: #333;
			color: #fff;
		}
	</style>
	
	<script>
	var hair_num = 0;
	var face_num = 0;
	
	var hair_arr = ['라리엘','엘로디','시간의나라 엘리스','빅토리 라이더','시엘'];
	var face_arr = ['조용한','졸린','아잉','꼬맹이','헤헤'];
	
	function hair_num_add(){
		if(hair_num < 4){
			hair_num = hair_num + 1;
		}else{
			hair_num = hair_num;
		}
	    //document.getElementById("hair_num").innerText = String(hair_num);
		make_character();
	}
	function hair_num_diff(){
	    if(hair_num > 0){
			hair_num = hair_num - 1;
		}else{
			hair_num = hair_num;
		}
	    //document.getElementById("hair_num").innerText = String(hair_num);
		make_character();
	}
	function face_num_add(){
		if(face_num < 4){
			face_num = face_num + 1;
		}else{
			face_num = face_num;
		}
	    //document.getElementById("face_num").innerText = String(face_num);
		make_character();
	}
	function face_num_diff(){
	    if(face_num > 0){
			face_num = face_num - 1;
		}else{
			face_num = face_num;
		}
	    //document.getElementById("face_num").innerText = String(face_num);
		make_character();
	}
	
	function character_create(){
		var character_name = document.getElementById("character_name").value;
		//캐릭터 명 빈값 방지
		if(character_name == ""){
			alert("캐릭터 명을 입력하세요.");
		}
		else{
			document.getElementById("charactername").value = character_name;
			document.getElementById("create_hair").value = String(hair_num);
			document.getElementById("create_face").value = String(face_num);
			document.character.submit();
		}
	}
	
	function add_cancel(){
		document.add_cancel_.submit();
	}
	
	function make_character(){
		document.getElementById("hair_num").innerText = String(hair_arr[hair_num])+" 헤어";
		document.getElementById("face_num").innerText = String(face_arr[face_num])+" 얼굴";
		document.getElementById("charimg").src = "char_img/"+String(hair_num)+"x"+String(face_num)+".png";
	}
	</script>
</head>
<body>
	<?php $id = $_POST["id"]; ?>

	<h2> 캐릭터 생성 </h2><hr>

	<table>
		<thead>
			<tr>
				<th colspan="4">캐릭터 생성</th>
				<th colspan="1">나의 캐릭터</th>
			</tr>
		</thead>
		<tbody>
			<tr height="50">
				<td>헤어</td>
				<td><div style="text-align: center;"><button onClick="hair_num_diff()"> <- </button></div></td>
				<td width="150"><div style="text-align: center;" id="hair_num">라리엘 헤어</div></td>
				<td><div style="text-align: center;"><button onClick="hair_num_add()"> -> </button></div></td>
				<td rowspan="3"><div style="text-align: center;"><img id="charimg" src="char_img/0x0.png"></img></div></td>
			</tr>
			<tr height="50">
				<td>성형</td>
				<td><div style="text-align: center;"><button onClick="face_num_diff()"> <- </button></div></td>
				<td width="150"><div style="text-align: center;" id="face_num">조용한 얼굴</div></td>
				<td><div style="text-align: center;"><button onClick="face_num_add()"> -> </button></div></td>
			</tr>
			<tr height="50">
				<td>캐릭터이름</td>
				<td colspan="3"><input type="text" id="character_name"></td>
			</tr>
			
			<tr>
				<td colspan="2">
					<div style="text-align: center;"><button onClick="character_create()">캐릭터 생성</button></div>
				</td>
				<td colspan="3">
					<div style="text-align: center;"><button onClick="add_cancel()">취소</button></div>
				</td>
			</tr>
		</tbody>
	</table>
		
	<form name="character" action="character_add_ok.php" method="post">
		<input type="hidden" name="id" value= <?php echo $id ?>>
		<input type="hidden" name="charactername" id="charactername">
		<input type="hidden" name="hair" id="create_hair">
		<input type="hidden" name="face" id="create_face">
	</form>
	
	<form name="add_cancel_" action="character.php" method="post">
	<input type="hidden" name="id" value= <?php echo $id ?>>
	</form>
	
</body>
</html>