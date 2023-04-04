<!DOCTYPE html>
<head>
	<meta charset="UTF-8">
	<style>
	table, th, td {
		border: 1px solid black;
		border-collapse: collapse;
	}
	</style>
	<title>강화</title>
	<link rel="icon" href="mococo.png">
</head>
<body>
	<h2>강화하기</h2><hr>
	<?php 
		$id = $_POST["id"];
		$charactername = $_POST["charactername"];
		$item_num = $_POST["itemnum"];
		
		$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
		
		$sql = "SELECT * FROM items WHERE item_num = '{$item_num}'";
		
		$ret = mysqli_query($con, $sql);
		$row = mysqli_fetch_array($ret);
		
		$success_price = array(
			intval($row['item_original_price'])*0.05, //1
			intval($row['item_original_price'])*1.2 * 0.05, //2
			intval($row['item_original_price'])*2 * 0.05, //3
			intval($row['item_original_price'])*3 * 0.05, //4
			intval($row['item_original_price'])*4 * 0.05, //5
			intval($row['item_original_price'])*5 * 0.05, //6
			intval($row['item_original_price'])*6 * 0.05, //7
			intval($row['item_original_price'])*8 * 0.05, //8
			intval($row['item_original_price'])*10 * 0.05, //9
			0 //10
			);
	?>
	<table>
		<thead>
		</thead>
		<tbody>
		<tr>
		<td>
			<div>
			<table>
				<thead>
					<tr>
						<th colspan="4">강화 확률 표</th>
					</tr>
				</thead>
				<tbody border ="1">
					<th>강화수치</th>
					<th>확률</th>
					<th>강화비용</th>
					<th>1강 대비 가격 증가율</th>
					<tr>
						<td><span style="background-color:black; color: white;">1강</span>-><span style="background-color:brown; color: white;">2강</span></td>
						<td>100%</td>
						<td><?php echo strval($success_price[0])."원"?></td>
						<td>1.2배</td>
					</tr>
					<tr>
						<td><span style="background-color:brown; color: white;">2강</span>-><span style="background-color:brown; color: white;">3강</span></td>
						<td>81%</td>
						<td><?php echo strval($success_price[1])."원"?></td>
						<td>3배</td>
					</tr>
					<tr>
						<td><span style="background-color:brown; color: white;">3강</span>-><span style="background-color:brown; color: white;">4강</span></td>
						<td>64%</td>
						<td><?php echo strval($success_price[2])."원"?></td>
						<td>8배</td>
					</tr>
					<tr>
						<td><span style="background-color:brown; color: white;">4강</span>-><span style="background-color:silver; color: white;">5강</span></td>
						<td>50%</td>
						<td><?php echo strval($success_price[3])."원"?></td>
						<td>25배</td>
					</tr>
					<tr>
						<td><span style="background-color:silver; color: white;">5강</span>-><span style="background-color:silver; color: white;">6강</span></td>
						<td>26%</td>
						<td><?php echo strval($success_price[4])."원"?></td>
						<td>50배</td>
					</tr>
					<tr>
						<td><span style="background-color:silver; color: white;">6강</span>-><span style="background-color:silver; color: white;">7강</span></td>
						<td>15%</td>
						<td><?php echo strval($success_price[5])."원"?></td>
						<td>100배</td>
					</tr>
					<tr>
						<td><span style="background-color:silver; color: white;">7강</span>-><span style="background-color:gold; color: black;">8강</span></td>
						<td>7%</td>
						<td><?php echo strval($success_price[6])."원"?></td>
						<td>220배</td>
					</tr>
					<tr>
						<td><span style="background-color:gold; color: black;">8강</span>-><span style="background-color:gold; color: black;">9강</span></td>
						<td>4%</td>
						<td><?php echo strval($success_price[7])."원"?></td>
						<td>480배</td>
					</tr>
					<tr>
						<td><span style="background-color:gold; color: black;">9강</span>-><span style="background-color:gold; color: black;">10강</span></td>
						<td>2%</td>
						<td><?php echo strval($success_price[8])."원"?></td>
						<td>1000배</td>
					</tr>
				</tbody>
			</table>
			</div>
		</td>
		<td>
		</td>
		<td>
			<div>
			<table>
				<thead>
					<tr>
						<th colspan="11">강화 실패시 X강으로 복구될 확률표</th>
					</tr>
				</thead>
				<tbody border ="1">
					<th>강화수치</th>
					<th><span style="background-color:black; color: white;">1강</span></th>
					<th><span style="background-color:brown; color: white;">2강</span></th>
					<th><span style="background-color:brown; color: white;">3강</span></th>
					<th><span style="background-color:brown; color: white;">4강</span></th>
					<th><span style="background-color:silver; color: white;">5강</span></th>
					<th><span style="background-color:silver; color: white;">6강</span></th>
					<th><span style="background-color:silver; color: white;">7강</span></th>
					<th><span style="background-color:gold; color: black;">8강</span></th>
					<th><span style="background-color:gold; color: black;">9강</span></th>
					<th><span style="background-color:gold; color: black;">10강</span></th>

					<tr>
						<td><span style="background-color:black; color: white;">1강</span>-><span style="background-color:brown; color: white;">2강</span></td>
						<td>100%</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
					<tr>
						<td><span style="background-color:brown; color: white;">2강</span>-><span style="background-color:brown; color: white;">3강</span></td>
						<td>100%</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
					<tr>
						<td><span style="background-color:brown; color: white;">3강</span>-><span style="background-color:brown; color: white;">4강</span></td>
						<td>65%</td>
						<td>35%</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
					<tr>
						<td><span style="background-color:brown; color: white;">4강</span>-><span style="background-color:silver; color: white;">5강</span></td>
						<td>55%</td>
						<td>45%</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
					<tr>
						<td><span style="background-color:silver; color: white;">5강</span>-><span style="background-color:silver; color: white;">6강</span></td>
						<td>35%</td>
						<td>40%</td>
						<td>25%</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
					<tr>
						<td><span style="background-color:silver; color: white;">6강</span>-><span style="background-color:silver; color: white;">7강</span></td>
						<td>10%</td>
						<td>32%</td>
						<td>36%</td>
						<td>22%</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
					<tr>
						<td><span style="background-color:silver; color: white;">7강</span>-><span style="background-color:gold; color: black;">8강</span></td>
						<td>4%</td>
						<td>10%</td>
						<td>30%</td>
						<td>35%</td>
						<td>21%</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
					<tr>
						<td><span style="background-color:gold; color: black;">8강</span>-><span style="background-color:gold; color: black;">9강</span></td>
						<td>2%</td>
						<td>4%</td>
						<td>10%</td>
						<td>28%</td>
						<td>35%</td>
						<td>21%</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
					<tr>
						<td><span style="background-color:gold; color: black;">9강</span>-><span style="background-color:gold; color: black;">10강</span></td>
						<td>1%</td>
						<td>2%</td>
						<td>4%</td>
						<td>10%</td>
						<td>28%</td>
						<td>34%</td>
						<td>21%</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
				</tbody>
			</table>
			</div>
		</td>
		</tr>
		</tbody>
	</table>
		
	<?php	
		echo "<TABLE BORDER=1>";
		echo "<TR><THEAD><tr><th colspan=\"4\">강화</th></tr></THEAD>";
		echo "<TR><TH>아이템 이름</TH> <TH>아이템 주인</TH> <TH>가치</TH> <TH>강화수치</TH>";
		echo "</TR>";
		
		echo "<TR>";
		echo "<TD rowspan='2'>", $row['item_name'], "</TD>";
		echo "<TD>", $row['item_owner'], "</TD>";
		echo "<TD>", $row['item_price']."원", "</TD>";
		
		//강화 수치에 따라 색을 칠한다.
		$item_reinforce_num = "-";
		if(intval($row['item_reinforce'])==1){
			$item_reinforce_num = "<span style=\"background-color:black; color: white;\"> {$row['item_reinforce']}강</span>";
		}
		else if(intval($row['item_reinforce'])>=2 && intval($row['item_reinforce'])<=4){
			$item_reinforce_num = "<span style=\"background-color:brown; color: white;\"> {$row['item_reinforce']}강</span>";
		}
		else if(intval($row['item_reinforce'])>=5 && intval($row['item_reinforce'])<=7){
			$item_reinforce_num = "<span style=\"background-color:silver; color: white;\"> {$row['item_reinforce']}강</span>";
		}
		else{
			$item_reinforce_num = "<span style=\"background-color:gold; color: black;\"> {$row['item_reinforce']}강</span>";
		}
		
		
		echo "<TD>", "<div style=\"text-align: center;\"> {$item_reinforce_num} </div>","</TD>";
		echo "</TR>";
		

		//성공 실패 값이 있냐 없냐를 받아온다.
		$success_or_fail = "-";
		if(isset($_POST['success_or_fail'])){
			$success_or_fail = $_POST['success_or_fail'];
			if($success_or_fail == "성공"){
				$success_or_fail = "<span style=\"background-color:green; color: white;\">성공</span>";
			}
			else if($success_or_fail == "실패"){
				$success_or_fail = "<span style=\"background-color:red; color: white;\">실패</span>";
			}
			else if($success_or_fail == "reinforce10"){
				$success_or_fail = "<span style=\"background-color:gold; color: black;\">10강 이상 강화 불가</span>";
			}
			else{
				$success_or_fail = "<span style=\"background-color:gray; color: white;\">잔액 부족</span>";
			}
		}else{
			$success_or_fail = "<span>-</span>";
		}

		
		echo "<TR>";
		echo "<TD>", "성공 실패 여부", "</TD>";
		echo "<TD colspan='3'>", "<div style=\"text-align: center;\"> {$success_or_fail} </div>", "</TD>";
		echo "</TR>";
		
		echo "<TR>";
		$sql_owner = "SELECT * FROM characters WHERE charactername LIKE \"{$row['item_owner']}\"";
		$ret_onwer = mysqli_query($con, $sql_owner);
		$onwer_money = mysqli_fetch_array($ret_onwer);
		echo "<TD>", "보유 금액", "</TD>";
		echo "<TD>", "<div style=\"text-align: center;\"> {$onwer_money['money']}원 </div>", "</TD>";
		echo "<TD>", "강화비용", "</TD>";
		echo "<TD>", "<div style=\"text-align: center;\"> {$success_price[$row['item_reinforce']-1]}원 </div>" ,"</TD>";
		echo "</TR>";
		
		echo "<TR>";
		echo "<TD colspan='2'>
			<div style=\"text-align: center;\">
			<form action=\"inventory_reinforce_ok.php\" method=\"post\">
			<input type=\"hidden\" name=\"id\" value=$id>
			<input type=\"hidden\" name=\"charactername\" value= $charactername>
			<input type=\"hidden\" name=\"itemnum\" value= $item_num>
			<input type=\"submit\" value=\"강화하기\">
			</form></div></TD>";
			
		echo "<TD colspan='2'>
			<div style=\"text-align: center;\">
			<form action=\"inventory.php\" method=\"post\">
			<input type=\"hidden\" name=\"id\" value=$id>
			<input type=\"hidden\" name=\"charactername\" value= $charactername>
			<input type=\"submit\" value=\"그만하기\">
			</form></div></TD>";
		echo "</TR>";
		echo "</table>";
		
		mysqli_close($con);
	?>	
</body>
</html>