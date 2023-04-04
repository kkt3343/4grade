 <html>
 <body>
	 <?php 
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
		
		$id = $_POST["id"];
		$charactername = $_POST["charactername"];
		
		$money_multiple = $_POST["money_multiple"];

		$sql_getmoney = "Select * from characters where charactername LIKE '{$charactername}'";
		$ret_getmoney = mysqli_query($con, $sql_getmoney);
		
		$my_money = 0;
		$borrow_money = 0;
		while ($row = mysqli_fetch_array($ret_getmoney)) {
			$my_money = intval($row['money']);
			$borrow_money = intval($row['borrow_money']);
		}
		$my_money = $my_money + (50000 * intval($money_multiple));
		$borrow_money = $borrow_money + (50000 * intval($money_multiple));

		$sql_setmoney = "UPDATE characters SET money = '{$my_money}' WHERE charactername LIKE '{$charactername}'";
		$ret_setmoney = mysqli_query($con, $sql_setmoney);
		
		$sql_borrow_setmoney = "UPDATE characters SET borrow_money = '{$borrow_money}' WHERE charactername LIKE '{$charactername}'";
		$ret_borrow_setmoney = mysqli_query($con, $sql_borrow_setmoney);

		if($ret_setmoney && $ret_borrow_setmoney) {
			echo "<form name=\"go_inventory_success\" action=\"inventory.php\" method=\"post\">
			<input type=\"hidden\" name=\"id\" value= '{$id}'>
			<input type=\"hidden\" name=\"charactername\" value= '{$charactername}'>
			</form>";
			echo "<script>document.go_inventory_success.submit()</script>";
		}
		else { 
			echo "충전 실패"."<BR>";
			echo "실패 원인 :".mysqli_error($con);
		}

		echo "<form name=\"go_inventory_fail\" action=\"inventory.php\" method=\"post\">
			<input type=\"hidden\" name=\"id\" value= '{$id}'>
			<input type=\"hidden\" name=\"charactername\" value= '{$charactername}'>
			<input type=\"submit\" value=\"뒤로가기\">
			</form>";
		mysqli_close($con);
	?>
</body>
</html>