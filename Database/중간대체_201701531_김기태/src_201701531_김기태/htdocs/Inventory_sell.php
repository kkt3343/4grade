<!DOCTYPE html>
<head>
	<meta charset="UTF-8">
</head>
<body>
	<?php 
		$id = $_POST["id"];
		$charactername = $_POST["charactername"];
		$item_name = $_POST["item_name"];
		$item_price = INTVAL($_POST["item_price"]);
		
		$item_num = INTVAL($_POST["item_num"]);
	?>
	<h2>판매하기</h2><hr>
	
	<?php 
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
		
		$sql = "SELECT money FROM characters WHERE charactername = '{$charactername}'";
		$ret = mysqli_query($con, $sql);
		if($ret) {
			$character_money;
			while($row = mysqli_fetch_array($ret)){
				$character_money = INTVAL($row['money']);
			}
			
			$sql_delete = "DELETE FROM items WHERE item_num = '{$item_num}'";
			$ret_delete = mysqli_query($con, $sql_delete);
			
			$character_money = $character_money + $item_price;
			$sql_sell = "UPDATE characters SET money = '{$character_money}' WHERE charactername = '{$charactername}'";
			$ret_mymoney_fin = mysqli_query($con, $sql_sell);
			
			if($ret_mymoney_fin && $ret_delete){
				echo "판매성공"."<br>";
				echo $item_name."을 판매하였습니다.<br>판매 가격은".$item_price."입니다.<br>총 잔액은 ".$character_money."입니다.";
			}
			else { 
				echo "판매 실패"."<BR>";
				echo "실패 원인:".mysqli_error($con);
			}
		}
		mysqli_close($con);
	?>
	
	<form action="inventory.php" method="post">
	<input type="hidden" name="id" value= <?php echo $id ?>>
	<input type="hidden" name="charactername" value= <?php echo $charactername ?>>
	<input type="submit" value="완료">
	</form>
	
</body>
</html>