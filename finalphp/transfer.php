<?php

$trans_name = $_POST['trans_name'];
$receive_name = $_POST['receive_name'];
$transfer_amount = $_POST['balance'];
$money = (int)$transfer_amount;

include 'config.php';
$con = mysqli_connect($HostName, $HostUser, $HostPass, $DatabaseName);
mysqli_query($con, "set names utf8");

//check connection
if ($con->connect_error){
	die("connection falied: " .$con->connect_error);
}

$query = "UPDATE cust_info SET balance = balance + $money WHERE name = '$receive_name';";
$query .= "UPDATE cust_info SET balance = balance - $money WHERE name = '$trans_name';";
$query .= "INSERT INTO account_transaction (name, type, amount, date) values ('$trans_name', '송금',-$money, addtime(current_timestamp, '09:00'));";
$query .= "INSERT INTO account_transaction (name, type, amount, date) values ('$receive_name', '입금',$money, addtime(current_timestamp, '09:00'))";
mysqli_set_charset($con, "utf8");

if($con->multi_query($query)===TRUE){
echo "data successfully updated...";
}
else{
echo "Could not update data";
}
 $con -> close();

?>
