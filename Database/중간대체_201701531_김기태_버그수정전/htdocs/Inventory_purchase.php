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
	?>
	<h2>구매하기</h2><hr>
	
	<?php 
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
		
		$sql = "SELECT money FROM characters WHERE charactername = '{$charactername}'";
		$ret = mysqli_query($con, $sql);
		if($ret) {
			$character_money;
			while($row = mysqli_fetch_array($ret)){
				$character_money = INTVAL($row['money']);
			}
			if($character_money - $item_price >= 0){
				$sql_input = "INSERT INTO items VALUES(NULL, '{$item_name}','{$charactername}', '{$item_price}', '{$item_price}', 1)";
				$ret_input = mysqli_query($con, $sql_input);
				
				$character_money = $character_money - $item_price;
				$sql_purchase = "UPDATE characters SET money = '{$character_money}' WHERE charactername = '{$charactername}'";
				$ret_mymoney_fin = mysqli_query($con, $sql_purchase);
				if($ret_mymoney_fin && $ret_input){
					echo "구매성공"."<br>";
					echo $item_name."을 구매하였습니다.<br>가격은".$item_price."원 입니다.";
				}
				else { 
					echo "구매 실패"."<BR>";
					echo "실패 원인:".mysqli_error($con);
				}
			}
			else{
				echo "구매 실패"."<br>";
				echo "소지 금액이 부족합니다."."<br>";
			}
		}
		else { 
			echo "구매 실패"."<BR>";
			echo "실패 원인:".mysqli_error($con);
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