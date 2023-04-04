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
	<title>나의 인벤토리</title>
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
	<h2><?php echo $id; ?>님의 "<?php echo $charactername; ?>"의 인벤토리</h2><hr>
	
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
	
	<div>
	<form action="character.php" method="post">
	<input type="hidden" name="id" value= <?php echo $id ?>>
	<input type="submit" value="캐릭터 선택창으로">
	</form>
	</div>
	
	<div>
	현재 보유 금액: <?php $money = $row['money']; echo $money ?>원 <br>
	지금 까지 캐시 충전한 금액: <?php $borrow_money = $row['borrow_money']; echo $borrow_money ?>원 <br>
	<table>
		<thead>
			<tr>
				<th colspan="3">캐시 충전</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>
				<form action="inventory_cash.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id?>>
				<input type="hidden" name="charactername" value= <?php echo $charactername?>>
				<input type="hidden" name="money" value= <?php echo $money?>>
				<input type="hidden" name="money_multiple" value= "1">
				<input type="submit" value="5만원 충전">
				</form> 
				</td>
				<td>
				<form action="inventory_cash.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id?>>
				<input type="hidden" name="charactername" value= <?php echo $charactername?>>
				<input type="hidden" name="money" value= <?php echo $money?>>
				<input type="hidden" name="money_multiple" value= "2">
				<input type="submit" value="10만원 충전">
				</form> 
				</td>
				<td>
				<form action="inventory_cash.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id?>>
				<input type="hidden" name="charactername" value= <?php echo $charactername?>>
				<input type="hidden" name="money" value= <?php echo $money?>>
				<input type="hidden" name="money_multiple" value= "6">
				<input type="submit" value="30만원 충전">
				</form> 
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	
	<div>
	상점
	<table>
		<thead>
			<tr>
				<th colspan="4">상점</th>
			</tr>
		</thead>
		<tbody>
			<th>번호</th>
			<th>이름</th>
			<th>가격</th>
			<th>구매</th>
			<tr>
				<td>1.</td>
				<td>동검</td>
				<td>10000원</td>
				<td>
				<form action="Inventory_purchase.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id?>>
				<input type="hidden" name="charactername" value= <?php echo $charactername?>>
				<input type="hidden" name="item_name" value="동검">
				<input type="hidden" name="item_price" value="10000">
				<input type="submit" value="구매하기">
				</form> 
				</td>
			</tr>
			<tr>
				<td>2.</td>
				<td>은검</td>
				<td>30000원</td>
				<td>
				<form action="Inventory_purchase.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id?>>
				<input type="hidden" name="charactername" value= <?php echo $charactername?>>
				<input type="hidden" name="item_name" value="은검">
				<input type="hidden" name="item_price" value="30000">
				<input type="submit" value="구매하기">
				</form> 
				</td>
			</tr>
			<tr>
				<td>3.</td>
				<td>금검</td>
				<td>50000원</td>
				<td>
				<form action="Inventory_purchase.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id?>>
				<input type="hidden" name="charactername" value= <?php echo $charactername?>>
				<input type="hidden" name="item_name" value="금검">
				<input type="hidden" name="item_price" value="50000">
				<input type="submit" value="구매하기">
				</form> 
				</td>
			</tr>
			<tr>
				<td>4.</td>
				<td>다이아몬드검</td>
				<td>70000원</td>
				<td>
				<form action="Inventory_purchase.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id?>>
				<input type="hidden" name="charactername" value= <?php echo $charactername?>>
				<input type="hidden" name="item_name" value="다이아몬드검">
				<input type="hidden" name="item_price" value="70000">
				<input type="submit" value="구매하기">
				</form> 
				</td>
			</tr>
			<tr>
				<td>5.</td>
				<td>전설의검</td>
				<td>100000원</td>
				<td>
				<form action="Inventory_purchase.php" method="post">
				<input type="hidden" name="id" value= <?php echo $id?>>
				<input type="hidden" name="charactername" value= <?php echo $charactername?>>
				<input type="hidden" name="item_name" value="전설의검">
				<input type="hidden" name="item_price" value="100000">
				<input type="submit" value="구매하기">
				</form> 
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	
	<div>
	나의 인벤토리
	<?php
		echo "<TABLE BORDER=1>";
		echo "<TR>";
		echo "<TH>아이템 이름</TH> <TH>아이템 주인</TH> <TH>가치</TH> <TH>강화수치</TH> <TH>강화하기</TH> <TH>판매하기</TH>";
		echo "</TR>";
		
		$sql_inventory = "SELECT * FROM items WHERE item_owner LIKE '{$charactername}'";
		$ret_inventory = mysqli_query($con, $sql_inventory);
		while($row = mysqli_fetch_array($ret_inventory)) {
			echo "<TR>";
			echo "<TD>", $row['item_name'], "</TD>";
			echo "<TD>", $row['item_owner'], "</TD>";
			echo "<TD>", $row['item_price']."원", "</TD>";
			echo "<TD>", $row['item_reinforce']."강", "</TD>";
			echo "<TD>", 
			"<form action=\"inventory_reinforce.php\" method=\"post\">
			<input type=\"hidden\" name=\"id\" value= $id>
			<input type=\"hidden\" name=\"charactername\" value= $row[item_owner]>
			<input type=\"hidden\" name=\"itemnum\" value= $row[item_num]>
			<input type=\"submit\" value=\"강화하기\">
			</form>", "</TD>";
			echo "<TD>", 
			"<form action=\"inventory_sell.php\" method=\"post\">
			<input type=\"hidden\" name=\"id\" value= $id>
			<input type=\"hidden\" name=\"item_num\" value= $row[item_num]>
			<input type=\"hidden\" name=\"item_name\" value= $row[item_name]>
			<input type=\"hidden\" name=\"charactername\" value= $row[item_owner]>
			<input type=\"hidden\" name=\"item_price\" value= $row[item_price]>
			<input type=\"submit\" value=\"판매하기\">
			</form>", "</TD>";
			echo "</TR>";
		}
		echo "</table>";
		mysqli_close($con);
	?>
	</div>
</div>	
</body>
</html>