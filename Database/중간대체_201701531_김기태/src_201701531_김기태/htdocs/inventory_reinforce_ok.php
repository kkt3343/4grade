 <?php 
	$id = $_POST["id"];
	$charactername = $_POST["charactername"];
	$item_num = $_POST["itemnum"];
		
	$success_rate = array(0, 100, 81, 64, 50, 26, 15, 7, 4, 2);
	
	$success_price_rate = array(1, 1.2, 3, 8, 25, 50, 100, 220, 480, 1000);
	$fail = array(
		array(100, 0, 0, 0, 0, 0, 0, 0, 0, 0), //0->1(필요는 없음)
		array(100, 0, 0, 0, 0, 0, 0, 0, 0, 0), //1->2
		array(100, 0, 0, 0, 0, 0, 0, 0, 0, 0), //2->3
		array(65, 35, 0, 0, 0, 0, 0, 0, 0, 0), //3->4
		array(55, 45, 0, 0, 0, 0, 0, 0, 0, 0), //4->5
		array(35, 40, 25, 0, 0, 0, 0, 0, 0, 0), //5->6
		array(10, 32, 36, 22, 0, 0, 0, 0, 0, 0), //6->7
		array(4, 10, 30, 35, 21, 0, 0, 0, 0, 0), //7->8
		array(2, 4, 10, 28, 35, 21, 0, 0, 0, 0), //8->9
		array(1, 2, 4, 10, 28, 34, 21, 0, 0, 0) //9->10
	);
		
	$con = mysqli_connect("localhost", "id201701531", "pw201701531", "mygames") or die("MySQL 접속 실패!!");
	
	$sql_get_item = "SELECT * FROM items WHERE item_num = $item_num";
	$ret_get_item = mysqli_query($con, $sql_get_item);
	$my_reinforce_item = mysqli_fetch_array($ret_get_item);
	
	//강화비용 계산
	$success_price = array(
			intval($my_reinforce_item['item_original_price'])*0.05, //1
			intval($my_reinforce_item['item_original_price'])*1.2 * 0.05, //2
			intval($my_reinforce_item['item_original_price'])*2 * 0.05, //3
			intval($my_reinforce_item['item_original_price'])*3 * 0.05, //4
			intval($my_reinforce_item['item_original_price'])*4 * 0.05, //5
			intval($my_reinforce_item['item_original_price'])*5 * 0.05, //6
			intval($my_reinforce_item['item_original_price'])*6 * 0.05, //7
			intval($my_reinforce_item['item_original_price'])*8 * 0.05, //8
			intval($my_reinforce_item['item_original_price'])*10 * 0.05 //9 (10강은 강화가 없기 때문에 필요가 없다.)
	);
	
	//난수 발생
	$random_rum = mt_rand(1, 100);
	echo "1~100뽑기결과:";
	echo $random_rum;
	$item_reinforce = $my_reinforce_item['item_reinforce'];
	
	//10강 이상이면 취소시킨다.
	if(intval($item_reinforce) >= 10){
		echo "최대강화는 10강까지 입니다.";
		echo "<form name=\"fail\" action=\"inventory_reinforce.php\" method=\"post\">
			<input type=\"hidden\" name=\"id\" value= echo $id>
			<input type=\"hidden\" name=\"charactername\" value= $charactername>
			<input type=\"hidden\" name=\"itemnum\" value= $item_num>
			<input type=\"hidden\" name=\"success_or_fail\" value= \"reinforce10\">
			<input type=\"submit\" value=\"확인\">
			</form>";
		echo "<script>document.fail.submit()</script>";
	}else{ //그외에는 강화를 실행
		//보유금액 차감
		$sql_get_money = "SELECT * FROM characters WHERE charactername = \"{$charactername}\"";
		$ret_get_money = mysqli_query($con, $sql_get_money);
		$my_money_tmp = mysqli_fetch_array($ret_get_money);
		$my_money_ = $my_money_tmp['money'];
		$my_money_ = $my_money_ - $success_price[$my_reinforce_item['item_reinforce'] - 1];
		
		//잔액이 있을 때
		if(intval($my_money_) >= 0){
			$sql_money_update = "UPDATE characters SET money = $my_money_ WHERE charactername = \"{$charactername}\"";
			$ret_money_update = mysqli_query($con, $sql_money_update);
			
			$sql_set_item = "";
			if($random_rum <= $success_rate[$my_reinforce_item['item_reinforce']]){ //성공
				//아이템의 가치를 증가시킨다.
				$moneytmp = $my_reinforce_item['item_price'] * ($success_price_rate[$my_reinforce_item['item_reinforce']] / $success_price_rate[$my_reinforce_item['item_reinforce'] - 1]);	
				$sql_price_upgrade = "UPDATE items SET item_price = $moneytmp WHERE item_num = $item_num";
				$ret_price_upgrade = mysqli_query($con, $sql_price_upgrade);
				
				
				$item_reinforce = $item_reinforce + 1;
				$sql_set_item = "UPDATE items SET item_reinforce = $item_reinforce WHERE item_num = $item_num";
				$ret_set_item = mysqli_query($con, $sql_set_item);
				if($ret_set_item){
					echo "success";
				}else{
					echo "ERROR";
				}
				
				echo "<form name=\"success\" action=\"inventory_reinforce.php\" method=\"post\">
					<input type=\"hidden\" name=\"id\" value=$id>
					<input type=\"hidden\" name=\"charactername\" value= $charactername>
					<input type=\"hidden\" name=\"itemnum\" value= $item_num>
					<input type=\"hidden\" name=\"success_or_fail\" value= \"성공\">
					<input type=\"submit\" value=\"확인\">
					</form>";
				
				echo "<script>document.success.submit()</script>";
				
			}else{ //실패
				//확률에 따라 아이템의 가치를 낮춘다.
				//강화 실패에 따른 난수 발생으로 몇강으로 돌릴 지 정한다.
				$fail_random_rum = mt_rand(1, 100);
				
				echo "<br>";
				echo "하락뽑기 1~100뽑기결과:";
				echo $fail_random_rum;
				echo "<br>";
				
				//현재 강화수치를 가져온다.
				$item_fail_section = $fail[$my_reinforce_item['item_reinforce']];

				echo "<br>";
				for($i = 0; $i < 9; $i = $i + 1){
					$item_fail_section[$i + 1] = $item_fail_section[$i + 1] + $item_fail_section[$i];
					echo $item_fail_section[$i]."<br>";
				}
				
				$fail_level_arr = $fail[intval($item_reinforce) - 1];
				if($fail_random_rum > 0 && $fail_random_rum <= $item_fail_section[0]){
					echo "my section 1";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[0];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 1, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
					
				}
				else if($fail_random_rum > $item_fail_section[0] && $fail_random_rum <= $item_fail_section[1]){
					echo "my section 2";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[1];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 2, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
				}
				else if($fail_random_rum > $item_fail_section[1] && $fail_random_rum <= $item_fail_section[2]){
					echo "my section 3";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[2];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 3, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
				}
				else if($fail_random_rum > $item_fail_section[2] && $fail_random_rum <= $item_fail_section[3]){
					echo "my section 4";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[3];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 4, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
				}
				else if($fail_random_rum > $item_fail_section[3] && $fail_random_rum <= $item_fail_section[4]){
					echo "my section 5";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[4];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 5, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
				}
				else if($fail_random_rum > $item_fail_section[4] && $fail_random_rum <= $item_fail_section[5]){
					echo "my section 6";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[5];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 6, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
				}
				else if($fail_random_rum > $item_fail_section[5] && $fail_random_rum <= $item_fail_section[6]){
					echo "my section 7";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[6];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 7, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
				}
				else if($fail_random_rum > $item_fail_section[6] && $fail_random_rum <= $item_fail_section[7]){
					echo "my section 8";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[7];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 8, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
				}
				else if($fail_random_rum > $item_fail_section[7] && $fail_random_rum <= $item_fail_section[8]){
					echo "my section 9";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[8];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 9, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
				}
				else if($fail_random_rum > $item_fail_section[8] && $fail_random_rum <= $item_fail_section[9]){
					echo "my section 10";
					$down_price = $my_reinforce_item['item_original_price'] * $success_price_rate[9];
					$down_my_item_sql = "UPDATE items SET item_reinforce = 10, item_price = $down_price WHERE item_num = $item_num";
					mysqli_query($con, $down_my_item_sql);
				}
				else{
				}
				
				echo "<br>fail";
				echo "<form name=\"fail\" action=\"inventory_reinforce.php\" method=\"post\">
					<input type=\"hidden\" name=\"id\" value=$id>
					<input type=\"hidden\" name=\"charactername\" value= $charactername>
					<input type=\"hidden\" name=\"itemnum\" value= $item_num>
					<input type=\"hidden\" name=\"success_or_fail\" value= \"실패\">
					<input type=\"submit\" value=\"확인\">
					</form>";
				echo "<script>document.fail.submit()</script>";
			}
		}
		else{ //잔액 부족
			echo "not enough money";
				echo "<form name=\"fail\" action=\"inventory_reinforce.php\" method=\"post\">
					<input type=\"hidden\" name=\"id\" value=$id>
					<input type=\"hidden\" name=\"charactername\" value= $charactername>
					<input type=\"hidden\" name=\"itemnum\" value= $item_num>
					<input type=\"hidden\" name=\"success_or_fail\" value= \"잔액 부족\">
					<input type=\"submit\" value=\"확인\">
					</form>";
				echo "<script>document.fail.submit()</script>";
		}
	}
	mysqli_close($con);
?>