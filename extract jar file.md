Extract on Jar file with BlueJ on Raspberry side. </br>
-Project-> Create Jar File.</br>
-then select SH as "Main Class" and include all pi4j libraries on " Include User Libraries"</br>
-Save it with name "SmartHouseServer" on Desktop (or you have to change path when you create [the script to Run the project On Startup](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/RunOnStartup.md)).</br>
- In case you face the following problem on jar exstraction :  Unsupported major.minor version 52.0</br>
you can download and install : "http://www.rpiblog.com/2014/03/installing-oracle-jdk-8-on-raspberry-pi.html"</br>


Extract on Jar file with BlueJ on Raspberry side. -Project-> Create Jar File. -then select SH as "Main Class" and include all pi4j libraries on " Include User Libraries", if there are duplicate libraries(.jar) add only one, DO NOT add lib with same title twice (possible duplicated libs 'pi4j-device.jar','pi4j-service.jar','pi4j-gpio-extension.jar','pi4j-core.jar'), try adding the first lib pair 'pi4j-device.jar','pi4j-service.jar', 'pi4j-gpio-extension.jar' and 'pi4j-core.jar' (if this doesn't work try the second lib pair, see if this is working by running it from cmd 'sudo java -jar file_name.jar' if this doesn't work you will see throwing an exception (about gpio_21)) https://github.com/tsoglani/Raspberry_SmartHouseServer/blob/master/images/12998194_10154109207358185_1366117902706159433_o.jpg
