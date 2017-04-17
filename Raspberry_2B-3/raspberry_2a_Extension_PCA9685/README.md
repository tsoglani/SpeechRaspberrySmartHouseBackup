# Raspberry_SmartHouseServer


This file contains three project:</br>
-raspberry_in_and_out: 20 outputs 20 inputs ( if you want to use it with switches for example light switches ).</br>
-raspberry_out: only outputs and no input ( 40 out 0 in).</br>



In both project, you can send commands from : </br>
1)your touchscreen monitor connectet to raspberry pi</br>
2) computer</br>
3) android</br>
4)wearable-watch</br>
(if you refactor the SH.java file this would be able to work in any device not only raspberry) you can connect as many raspberry devices as you want but all of them must be conected to the same local network ( the device you will use to send the command must also be on same local network ).</br>

-you have to install the extension libraries, you can see [all command lines you should use](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Command_Line.md)</br>
Or if you want to see more try to see [this tutorial](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_2B-3/raspberry_2a_Extension_PCA9685/Installing%20Kernel%20Support%20) or [this](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_2B-3/raspberry_2a_Extension_PCA9685/pca9685%20libraries%20installation)
</br>
////or on command line type "sudo apt-get install python-smbus" and "sudo apt-get install i2c-tools"  </br>
////-you also have to enable i2c for </br>
////raspberry pi: on command line enter "sudo raspi-config" </br>
////select "Advanced Options"->"I2C"->"YES"</br>
////Optionally you can enable SPI option too</br>


imports libs for code refactoring: </br>
- imports all .jar from SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/libs/
 folder /// tools->preferences->Libraries->Add</br>
- restart bluej</br>

-You have to modify ["commands.txt" file](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/commands.txt) on "SpeechRaspberrySmartHouse" folder:</br>
but you have to be carefull with the syntax,</br>
with double comma (,,) the first text splited in comma in is sent to the client's device and is used for switching the buttons from the client's device, the other text splited with ",," is used to bind this command with others if you want to use speech funtions.</br>
after the commands there is a "@@" text, after this you put the output number (you can use multiple outputs using double commas , for example "Kitchen light,,kitchen lights@@0,,1,,2" ) 

-You have also to modify ["deviceName.txt" file](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/deviceName.txt) on "SpeechRaspberrySmartHouse" folder:
here you put the username if you want to connect remotely, you put your username after "username:" text on file.
example "username:home"</br>

</br>
Steps to test it:</br>
</br>
-(optionally if you want to use the code, you can also build it on a java IDE and use the extracted jar file) On ruspberry device download and install BlueJ, after that download this project, open it with BlueJ, and run it.</br>
-You can download [Computer Client](https://github.com/tsoglani/Java_SmartHouseClient/blob/master/SmartHouseClient/dist/SmartHouseClient.jar) file by pressing "View Raw"; it's a Free testing application for your computer device ( before you run it, make sure that your computer device is on same local network with your raspberry device ).</br>


info [Raspberry 3B GPIO's map ](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_2B-3/20160925_212252.jpg)


Enjoy...
