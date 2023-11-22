<?php
// Proses login
if(!empty($_POST['email']) && !empty($_POST['password'])) {
    $email = $_POST['email'];
    $password = $_POST['password'];
    $result = array();
    // Koneksi ke database
    $con = mysqli_connect("localhost","root","","ZenFemina");
    if ($con) {
        $sql = "SELECT * FROM users WHERE email = '".$email."'";
        $res = mysqli_query($con, $sql);
        if(mysqli_num_rows($res) != 0) {
            $row = mysqli_fetch_assoc ($res);
            if($email == $row ['email'] && $password == $row['password']){
                try {
                    $session_id = bin2hex(random_bytes(23));
                } catch (Exception $e) {
                    $session_id = bin2hex (uniqid($email, true));
                }
                $sqlUpdate = "UPDATE users set session_id = '" .$session_id."' WHERE email = '".$email."'";
                if (mysqli_query($con, $sqlUpdate)) {
                    $result = array("status" => "Sukses","message" => "Login Sukses",
                        "username" => $row['username'], "email" => $row['email'], "session_id" => $session_id);
               
                } else $result = array("status" => "failed", "message" => "Masuk gagal coba kembali");
            }else $result = array("status" => "failed", "message" => "Coba lagi dengan email dan password yang benar");
        }else $result = array("status" => "failed", "message" => "Coba lagi dengan email dan password yang benar");
    }else $result = array("status" => "failed", "message" => "Database koneksi gagal");
}else $result = array("status" => "failed", "message" => "Semua kolom wajib diisi");

echo json_encode($result, JSON_PRETTY_PRINT);

?>