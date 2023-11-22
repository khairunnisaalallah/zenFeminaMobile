<?php
if (!empty($_POST['email']) && !empty($_POST['session_id'])){
$email = $_POST['email'];
$sesion_id = $_POST['session_id'];
$con = mysqli_connect("localhost", "root", "", "ZenFemina");
if($con) {
    $sql="SELECT * FROM users WHERE email='".$email."' AND session_id='".$sesion_id."'";
    $res=mysqli_query($con, $sql);
    if (mysqli_num_rows($res)!=0){
        $row = mysqli_fetch_assoc($res);
        $sqlUpdate = "UPDATE users SET session_id = ' ' WHERE email = '" . $email . "'";
        if (mysqli_query($con, $sqlUpdate)){
            echo "Sukses";
        }else echo "Logout gagal";
    }else echo "Akses gagal";
}else echo "Database koneksi gagal";
}else echo "Semua kolom wajib diisi";
?>
