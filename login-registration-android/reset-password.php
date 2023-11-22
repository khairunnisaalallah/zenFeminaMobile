<?php
if (!empty($_POST['email'])){
    $email = $_POST['email'];
    $con = mysqli_connect("localhost", "root", "", "ZenFemina");
    if($con){
        try{
        $otp = random_int(100000, 999999);
        }catch (Exception $e){
            $otp = rand(100000, 999999);
        }
        $sql = "UPDATE users SET reset_password_otp"
    }else echo "Database koneksi gagal";
}else echo "Semua kolom wajib diisi";