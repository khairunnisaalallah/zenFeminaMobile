<?php
if (!empty($_POST['email']) && !empty($_POST['session_id'])){
$email = $_POST['email'];
$sesion_id = $_POST['session_id'];
$result = array();
$con = mysqli_connect("localhost", "root", "", "ZenFemina");
if($con){
    $sql="SELECT * FROM users WHERE email='".$email."' AND session_id='".$sesion_id."'";
    $res=mysqli_query($con, $sql);
    if (mysqli_num_rows($res)!=0){
        $row = mysqli_fetch_assoc($res);
        $result = array("status" => "Sukses","message" => "Data sukses tersambung",
        "username" => $row['username'], "email" => $row['email'], "session_id" => $row['session_id']);
        }else $result = array("status" => "failed", "message" => "Akses gagal");
    }else $result = array("status" => "failed", "message" => "Database koneksi gagal");
}else $result = array("status" => "failed", "message" => "Semua kolom wajib diisi");

echo json_encode($result, JSON_PRETTY_PRINT);
?>