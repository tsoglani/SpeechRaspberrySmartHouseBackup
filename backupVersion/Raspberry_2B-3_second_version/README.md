# Raspberry_SmartHouseServer




This file contains two project:
-SmartHouseRaspberryServer: 13 outputs 13 inputs ( if you want to use it with switches for example light switches ).
 -SmartHouseRaspberryServerOnlyOutputs: only outputs and no input ( 26 out 0 in).

In both project, you can send commands from your computer, android, wearable-watch device to your raspberry device (if you refactor the SH.java file this would be able to work in any device not only raspberry) you can connect as many raspberry devices as you want but all of them must be conected to the same local network ( the device you will use to send the command must also be on same local network ).

----- Raspberry command line commands
(can also use -sudo -i to excecute on root)
-sudo apt-get update  ////--update
-sudo apt-get upgrade       ////--upgrade
-sudo apt-get install raspberrypi-ui-mods  // touch screen plugins   
-sudo apt-get install raspberrypi-net-mods  // touch screen plugins                
-curl -s get.pi4j.com | sudo bash    ////--update
-sudo apt-get install pi4j  /// update the raspberry libs
-sudo apt-get install libttspico-utils // speech
-sudo apt-get install sox // speech
-sudo apt-get install sox libsox-fmt-all// speech

imports libs:


- imports all libraries from  /opt/pi4j/lib                   /// tools->preferences->Libraries->Add
- imports all libs from project's folder /// tools->preferences->Libraries->Add
- restart bluej


inside the code you might have to modify:

-NumberOfBindingCommands : the number of commands you want to bind with one or more outputs.
-DeviceID : in case you want to use more than one raspberry (or any) devices in the same local network, each raspberry device must have a unique DeviceID. 
Example: if we have 4 raspberry devices connected to local network, each one MUST have a unique ID :
         the first Ruspberry device DeviceID will be 0, the second device's DeviceID will be 1
         the third will be 2 the fourth will be 3 ...    (this is important if you want to open each output seperately)
- initializePowerCommans() function (most important): in this function you activate addCommandsAndPorts function;
addCommandsAndPorts function has 3 parameters: 
-the first parameter is an id (inside the code it is "i" and used in switch cases, so it changes automatically) you don't have to change it, except if your Relay has more that 8 outputs.

-the second parameter is an array of a string-text, the first text in this array is sent to the client's device and is used for switching the buttons from the client's device. When you switch the button, the command is sent to raspberry server. One or more commands are bound to one or more raspberry outputs (see the third parameter paragraph below). This way you can set one or more raspberry outputs on and off at once, with one command. You have the option to put more than one commands in case you want to activate or deactivate the outputs with speech commands (you can also do it with speech command, by saying the command and then "on" or "off" word).

- the third parameter is an array of integers(one  or more), each integer represents one output in the raspberry device. The width of outputs in raspberry device is from 0 to 20, you can put one or more integers to activate or deactivate one or more output with one (or more) command (see parameter two).

Steps to run it:

-On ruspberry device download and install BlueJ, after that download this project, open it with BlueJ, and run it.

To be able to run it from computer device
-You can download this (https://github.com/tsoglani/Java_SmartHouseClient/blob/master/SmartHouseClient/dist/SmartHouseClient.jar) jar file by pressing "View Raw"; it's a Free testing application for your computer device ( before you run it, make sure that your computer device is on same local network with your raspberry device ).


How to run the application on raspberry startup :

Extract on Jar file with BlueJ on Raspberry side. 
-Project-> Create Jar File.
-then select SH as "Main Class" and include all non pi4j and pi4j libraries on " Include User Libraries", if there are duplicate 
libraries(.jar) add only one, DO NOT add lib with same title twice (possible duplicated libs 'pi4j-device.jar','pi4j-service.jar','pi4j-gpio-extension.jar','pi4j-core.jar'),
try adding the first lib pair 'pi4j-device.jar','pi4j-service.jar', 'pi4j-gpio-extension.jar' and 'pi4j-core.jar'
 (if this doesn't work try the second lib pair, see if this is working by running it from cmd 'sudo java -jar file_name.jar' if this doesn't work you will see throwing an exception (about gpio_21))
https://github.com/tsoglani/Raspberry_SmartHouseServer/blob/master/images/12998194_10154109207358185_1366117902706159433_o.jpg

-Save it with name "SmartHouseServer" on Desktop (or you have to change it on script-see below).
- In case you face the following problem on jar exstraction :  Unsupported major.minor version 52.0
you can download and install : "http://www.rpiblog.com/2014/03/installing-oracle-jdk-8-on-raspberry-pi.html"

- you  need to change permission on storage file, for example in cmd (command line), you have to change the permission status on /etc/rc.local by writting: 
 "sudo chmod -R 777 /etc/rc.local"

As soon as you do that you will be able to apply the "write and read" permission which will enable you to modify the " /etc/rc.local" file and write the following script.

Create script:
My example for /etc/rc.local file:  
" 
 # By default this script does nothing.
 # need to enter sudo command

sudo java -jar /home/pi/Desktop/SmartHouseServer/SmartHouseServer.jar


exit 0
"

finally a costume map to raspberry pi 3 (40 pin led)
->https://github.com/tsoglani/Raspberry_SmartHouseServer/blob/master/images/raspberry_pi_3_40_pin_led_map.jpg



Enjoy...
