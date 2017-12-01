<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
$name = $_POST['name'];
include 'config.php';
// Create connection
$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);
mysqli_query($conn, "set names utf8");
// Check connection
if ($conn->connect_error) {
     die("Connection failed: " . $conn->connect_error);
} 
 
$sql = "SELECT name, date, type, amount FROM account_transaction WHERE name = '$name'";
mysqli_set_charset($conn, "utf8");

$res = mysqli_query($conn, $sql);
 
if ($res->num_rows > 0) {
     // output data of each row
     while($row[] = $res->fetch_assoc()) {
     	$json = json_encode($row, JSON_UNESCAPED_UNICODE);
     }
     echo $json;
} else {
     echo "0 results";
}
mysqli_close($conn);
}
?>
