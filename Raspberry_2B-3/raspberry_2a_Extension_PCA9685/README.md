# Raspberry_SmartHouseServer


This file contains three project:
-raspberry_in_and_out: 20 outputs 20 inputs ( if you want to use it with switches for example light switches ).
-raspberry_out: only outputs and no input ( 40 out 0 in).



In both project, you can send commands from : 
1)your touchscreen monitor connectet to raspberry pi
2) computer
3) android
4)wearable-watch
(if you refactor the SH.java file this would be able to work in any device not only raspberry) you can connect as many raspberry devices as you want but all of them must be conected to the same local network ( the device you will use to send the command must also be on same local network ).

-you have to install the extension libraries, you can do it using
[this tutorial](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_2B-3/raspberry_2a_Extension_PCA9685/Installing%20Kernel%20Support%20) or [this](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_2B-3/raspberry_2a_Extension_PCA9685/pca9685%20libraries%20installation)
or on command line type "sudo apt-get install python-smbus" and "sudo apt-get install i2c-tools"  

</br>-you also have to enable i2c for 
raspberry pi: on command line enter "sudo raspi-config" 
select "Advanced Options"->"I2C"->"YES"
Optionally you can enable SPI option too


imports libs:

- imports all .jar from SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/libs/
 folder /// tools->preferences->Libraries->Add
- restart bluej

inside the code you might have to modify:


-DeviceID : in case you want to use more than one raspberry (or any) devices in the same local network, each raspberry device must have a unique DeviceID. 
Example: if we have 4 raspberry devices connected to local network, each one MUST have a unique ID :
         the first Ruspberry device DeviceID will be 0, the second device's DeviceID will be 1
         the third will be 2 the fourth will be 3 ...    (this is important if you want to open each output seperately)
-int NumberOfBindingCommands : the number of commands you want to bind with one or more outputs on initializePowerCommans() function.
- initializePowerCommans() function (most important): in this function you activate addCommandsAndPorts function;

**addCommandsAndPorts function has 3 parameters: 
the first parameter is an id (inside the code it is "i" and used in switch cases, so it changes automatically) you don't have to change it, except if your Relay has more that NumberOfBindingCommands outputs.

**the second parameter is an array of a string-text, the first text in this array is sent to the client's device and is used for switching the buttons from the client's device. When you switch the button, the command is sent to raspberry server. One or more commands are bound to one or more raspberry outputs (see the third parameter paragraph below). This way you can set one or more raspberry outputs on and off at once, with one command. You have the option to put more than one commands in case you want to activate or deactivate the outputs with speech commands (you can also do it with speech command, by saying the command and then "on" or "off" word).

** the third parameter is an array of integers(one  or more), each integer represents one output in the raspberry device. The width of outputs in raspberry device is from 0 to 20, you can put one or more integers to activate or deactivate one or more output with one (or more) command (see parameter two).

Steps to run it:

-On ruspberry device download and install BlueJ, after that download this project, open it with BlueJ, and run it.
-You can download [Computer Client](https://github.com/tsoglani/Java_SmartHouseClient/blob/master/SmartHouseClient/dist/SmartHouseClient.jar) file by pressing "View Raw"; it's a Free testing application for your computer device ( before you run it, make sure that your computer device is on same local network with your raspberry device ).


How to run the application on raspberry startup :

first [Extract jar file](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_1_Version/extract%20jar%20file)  </br>
then [Run project On Startup](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_1_Version/RunOnStartup.txt)

info [Raspberry 3B GPIO's map ](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_2B-3/20160925_212252.jpg)


Enjoy...
