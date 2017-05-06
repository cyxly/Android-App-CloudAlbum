# Android-App-CloudAlbum
This is an app, users can upload their videos to server and view them online.


Register and Log in
Before you use the app to upload videos or watch them, you should register first. Here we already
supply a user name “tcl” and password “1234” for testing. You can register and then test or use our
user name and password to test. If you want to register first, remember to click “REGISTER” to
register and then click “SIGN IN”.


Techniques:
This Web-based Video Album App can record a short video from device camera and then upload
the recorded video to a web server. (Notice: because our web server is using free storage provided
by CS home, and it’s only 80M, which is very limited. So the video taken by mobile phone is
using low quality format, when you do the test, don’t surprise that the image quality is poor.)

Remember to connect the Internet, or the app won’t work.

This app contains 4 pages including log in page, home page, uploading page and watch-videos
page. The log in page can verify the user, only registered users can log in. So when you do the test,
please register first. After log in, then you go to the home page. The home page contains to button,
the upper one is “Record Video”, when you click it, the app starts record the video. If you click
cross to cancel upload, the video won’t be uploaded and it will return to home page. And if you
click “tick” button, then it will be uploaded to the web server automatically. And the uploading
page will show a progress bar to show the progress. Also in the uploading page, there is a video
view up to the progress bar to preview the video. Remember to connect the Internet! If the video is
uploaded successfully, the server will return a “Video uploaded successfully” message and it will
show an alert dialog to inform the user that the video uploaded successfully. If unsuccessfully, the
alert dialog would show an error message “Could not upload the video!”. And the alert dialog
contains an “OK” button, after you click it, it goes to the watch-videos page. This page contains
all videos you have uploaded. User do not have the right to view the videos which have uploaded
by others. All videos which the certain user have been uploaded are listed in a list, each of them
has a cover which is the first frame of the video. It helps you to recognize them easily. And it is
listed in time line, the newly uploaded would list in the bottom. When you click a certain a video,
the app will broadcast this video by the video player provided by your own mobile phone.

The other button “Video Uploaded” in home page is used to go to the watch-videos page if you
want to watch videos you have uploaded. If you haven’t uploaded any video, it would toast a
message “You haven’t uploaded any video!” rather than go to the watch-videos page.

Different user has his own web server space. If you register successfully, after you upload the
video, the server would allocate a directory to the user based on his user name. And in the user’s
device storage, the app would make a directory called “Android File Upload” under “Picture”
directory of the system. And the videos would in “Android File Upload” directory. There is a file
called “VideoUploaded” to record all the videos’ URL in the server which are uploaded
successfully. And before go to the watch-videos page, the app would fetch videos in the server
first and get their fist frame to use them as covers of videos. So it may take a little time if videos
are large. Because the video only takes a few seconds, so it won’t be a problem. When you click
the cover of the video, it would call the device’s system video player and play the video. And it
would be full screen. After finishing playing video, it would exit full screen and you can switch to
play another videos.


Methods:
We use PHP & MySQL to build a web server response system for storing user name and their
password, and use MD5 to encode the password.

As for the web sever, we use a php “fileUpload.php” to receive videos and allocate directory for
users to store their videos.

When record videos, we use “MediaStore.ACTION_VIDEO_CAPTURE” to call the device
camera to record videos. And we set the video quality by using the function of MediaStore:
“intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5);” to set a lower quality of videos.
If you want a high quality image, then set “0.5” to “1”. However it would cost a lot of storage.
Because CS home only provides us 80M storage, we have to use the lower quality image. Also the
high quality image would demand a higher network speed, or it doesn’t work well too.

After recording the video, it would call the function “launchUploadActivity()” to upload the video
automatically. In the function “launchUploadActivity()”, it would pass the video’s path to
“UploadActivity” to find the path.

When uploading the video, there would be progress bar to indicate the progress. We override the
function “protected void onProgressUpdate(Integer... progress)” to change to progress bar.
When listing videos using their first frame.the android application use MediaMetadataRetriever
class to get the video and use getFrameAtTime() method to catch the bitmap picture and display it
at relative position.We catch the first screen of the video to display,so it will spend little time to
download it and user feels its user-friendly.

The layout of the listing videos is LinearLayout, the orientation is vertical.Because there may be
many videos so it is impossible to display all in the screen at one time.As a result, We put the
LinearLayout in a ScrollView and you can slide the screen to view all.


Features:
1.The app can upload videos automatically to the server after recording.
2.When you go to watch-videos page, you can recognize videos easily because each of them has a
cover.
3.When you click one of the covers, the relative video will broadcast using the video player
provided by users’ own mobile phones.
4.Before you use the app, you should register, and each user has his own directory, it won’t mix up.
And to protect users’ privacy, we use “MD5” Method to encode the password, so no other people
can know the original password characters.
5.Once you have uploaded a video, both the sever and the device have a copy, even the user delete
it locally, the video still can still be accessed through URL of the video.
