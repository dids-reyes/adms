# Assistance Distribution Monitoring System
Mobile Application Development Project

:information_source: Copyright Disclaimer Under section 107 of the Copyright Act 1976, allowance is made for “fair use” for purposes such as criticism, comment, news reporting, teaching, scholarship, education and research. ... Non-profit, educational or personal use tips the balance in favor of fair use.

Images used in Development are not my property and are used for educational purposes only.

<hr/>

### Contents
- OTP Authentication
- Email Authentication
- Email Registration
- Pending Requests (this section is used by end-users, and to be accepted by administrators)
- Login Tracking (admin privileges)
- CRUD Operations (admin privileges)
- RTDB to JSON
- Broadcasting

<hr/>


Java Mail API jar files https://code.google.com/archive/p/javamail-android/downloads to be included in project.
![Alt Text](https://media.giphy.com/media/jODqcBqgZtBSevDRNR/giphy.gif)
<p>
  <img src="https://i.ibb.co/Yh3VvfX/received-278378680340374.webp" alt="received-278378680340374" width="290" height="530">
  <img src="https://i.ibb.co/3fmKK6k/received-250156306579707.webp" alt="received-250156306579707" width="290" height="530">
</p>

### Splashscreen
Splashscreen contains preloader and a delay timer to check whether the device mobile data or wireless connection is turned on.
It doesn't check whether the device has an access to internet. The app won't continue without turning on data or wireless, a no connection event will be triggered, turning on data or wireless connection will not turn off the event triggered and the app must be force closed or minimized to retry.

### Usage and Issues
Clone this repository to your local machine and you're good to go!

To resolve the "missing app valid identifier" at OTP Authentication:
- Create your own Realtime Database at https://firebase.google.com/
- Add your SHA-1 to your Database, Open Project Settings Scroll Down and find "add fingerprint"
- To add SHA-1, Open your Android Studio, Top Right click Gradle, click the Gradle icon, type `gradlew signingReport` or `gradle signingReport`
- Copy and Paste the SHA-1 that will appear from your Android Studio's Terminal to your Firebase "add fingerprint" field.

### Use Android Studio IDE for easy modification of the visual design.
