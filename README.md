ColludingApplications
=====================

Colluding applications means escalation of permission of one application by using the permission of other application. Here i have demonstrated the escalation attack by escalating the permissions of contact application through media player application(developed by myself), and thus establishing a medium to transfer contacts from one device to other device.

How to run these applications : 

You need two Android devices to perform this demonstartion. On one device you will run the MediaPlayer app and on other device you will run Server app. When you will start a video in media player, same time your server device will be detected and connected with the MediaPlayer app and contacts will be transfered to the Server app. 

Note : 
1. To run this demonstration successfully both the devices should me in the same sub network.

2. Following applications are for Android Platform. To use these applications you will have to make a small change in the "MediaPlayer/PingEveryOne/src/com/example/pingeveryone/MainActivity.java". In this file in the "Ping" class find for variable "mac" and replace it's value with ur wi-fi interface card mac address. Then compile it and get the executable.

