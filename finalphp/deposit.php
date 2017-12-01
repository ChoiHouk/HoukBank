<?php

$name = $_POST['name'];
$deposit_amount = $_POST['balance'];
$money = (int)$deposit_amount;

include 'config.php';
$con = mysqli_connect($HostName, $HostUser, $HostPass, $DatabaseName);
mysqli_query($con, "set names utf8");


//check connection
if ($con->connect_error){
	die("connection failed: " .$con->connect_error);
}

$query = "UPDATE cust_info SET balance = balance + $money WHERE name = '$name';";
$query .= "INSERT INTO account_transaction (name, type, amount, date) VALUES ('$name', '입금', $money, addtime(current_timestamp, '09:00'))";
mysqli_set_charset($con, "utf8");


if($con->multi_query($query)===TRUE){
 
 	echo "Data successfully updated ...";
 }
 else{
 
 echo 'Error: Try Again';
 
 }
 $con -> close();

?>