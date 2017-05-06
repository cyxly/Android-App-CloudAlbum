<?php
 
// Path to move uploaded files
//$target_path = "Upload/";
 
// array for final json respone
$response = array();
 
// getting server ip address
$server_ip = gethostbyname(gethostname());
 
// final file url that is being uploaded
//$file_upload_url = 'http://' . $server_ip . '/' . 'AndroidFileUpload' . '/' . $target_path;
//$file_upload_url = 'http://' . 'i.cs.hku.hk' . '/' . '~yxchen' . '/' . 'AndroidFileUpload' . '/' . $target_path;
 
if (isset($_FILES['image']['name'])) {
//    $target_path = $target_path . basename($_FILES['image']['name']);
 
    // reading other post parameters
	$target_path = isset($_POST['userName']) ? $_POST['userName'] : '';
    $email = isset($_POST['email']) ? $_POST['email'] : '';
    $website = isset($_POST['website']) ? $_POST['website'] : '';
 
    $response['file_name'] = basename($_FILES['image']['name']);
    $response['email'] = $server_ip;
    //$response['website'] = $website;
	if (!is_dir($target_path)) {
		mkdir($target_path);
		chmod($target_path, 0755);
	}
    $target_path = $target_path . basename($_FILES['image']['name']);
	$file_upload_url = 'http://' . 'i.cs.hku.hk' . '/' . '~yxchen' . '/' . 'AndroidFileUpload' . '/' . $target_path;
 
    try {
        // Throws exception incase file is not being moved
        if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {
            // make error flag true
            $response['error'] = true;
            $response['message'] = 'Could not upload the video!';
        }
		chmod($target_path, 0644);
		//chmod("Upload/". $_FILES['image']['name'], 0644);
        // File successfully uploaded
        $response['message'] = 'Video uploaded successfully!';
        $response['error'] = false;
        $response['file_path'] = $file_upload_url;
		
		$filename = isset($_POST['userName']) ? $_POST['userName'] : '';
		$filename = $filename . 'VideoUploaded';
		$doc = fopen($filename,"a");
		fwrite($doc, $file_upload_url."\n");
		fclose($doc);
		chmod($filename, 0644);
    } catch (Exception $e) {
        // Exception occurred. Make error flag true
        $response['error'] = true;
        $response['message'] = $e->getMessage();
    }
	
} else {
    // File parameter is missing
    $response['error'] = true;
    $response['message'] = 'Not received any file!F';
}
 
// Echo final json response to client
echo json_encode($response);
?>