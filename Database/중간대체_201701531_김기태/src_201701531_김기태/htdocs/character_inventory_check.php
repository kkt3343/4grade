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
	<title>유저 인벤토리 검색</title>
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
</head>
<body>
<div>
	<?php 
		$id = $_POST["id"];
		$charactername = $_POST["charactername"];
	?>
	<?php 
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
		$sql = "SELECT * FROM characters WHERE charactername LIKE '{$charactername}'";
		$ret = mysqli_query($con, $sql);
		if($ret) {
			$row = mysqli_fetch_array($ret);
		}
		else { 
			echo "캐릭터 로딩 실패"."<BR>";
			echo "실패 원인:".mysqli_error($con);
		}
	?>
	
	<?php
		$sql_inventory_accounts = "SELECT id FROM accounts WHERE id = (SELECT id FROM characters WHERE charactername LIKE '{$charactername}')";
		$ret_inventory_accounts = mysqli_query($con, $sql_inventory_accounts);
		$row_inventory_accounts = mysqli_fetch_array($ret_inventory_accounts);
		
		if(!isset($row_inventory_accounts['id'])){
			echo "<h2>결과 없음</h2><hr>";
			echo "결과가 없습니다.";
		}
		else{
			echo "<h2>".$row_inventory_accounts['id']."님의 \"".$charactername."\"의 인벤토리</h2><hr>";
			echo $row_inventory_accounts['id']."님의 \"";
			echo $charactername."\"의 인벤토리";
			
			echo "<TABLE BORDER=1>";
			echo "<TR>";
			echo "<TH>아이템 이름</TH> <TH>아이템 주인</TH> <TH>가치</TH> <TH>강화수치</TH>";
			echo "</TR>";
			
			$sql_inventory = "SELECT * FROM items WHERE item_owner = '{$charactername}'";
			$ret_inventory = mysqli_query($con, $sql_inventory);
			while($row = mysqli_fetch_array($ret_inventory)) {
				echo "<TR>";
				echo "<TD>", $row['item_name'], "</TD>";
				echo "<TD>", $row['item_owner'], "</TD>";
				echo "<TD>", $row['item_price']."원", "</TD>";
				echo "<TD>", $row['item_reinforce']."강", "</TD>";
				echo "</TR>";
			}
			echo "</table>";
			mysqli_close($con);
		}
	?>
	
	<form action="character.php" method="post">
	<input type="hidden" name="id" value= <?php echo $id ?>>
	<input type="submit" value="캐릭터 선택창으로">
	</form>
</div>	
</body>
</html>