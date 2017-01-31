
**1st way !!
- you  need to change permission on storage file, for example in cmd (command line), you have to change the permission status on /etc/rc.local by writting: 
 "sudo chmod -R 777 /etc/rc.local" </br>

As soon as you do that you will be able to apply the "write and read" permission which will enable you to modify the " /etc/rc.local" file and write the following script.
</br>
Create script:</br>
My example for /etc/rc.local file:  </br>
" 
 # By default this script does nothing.</br>
 # need to enter sudo command</br>

sudo java -jar /home/pi/Desktop/SmartHouseServer/SmartHouseServer.jar</br>

</br>
exit 0</br>
"


if rc.local doesn't run enter on command line "chmod +x /etc/rc.local"

if this doesnt work clear 
sudo java -jar /home/pi/Desktop/SmartHouseServer/SmartHouseServer.jar</br>
from /etc/rc.local

**2nd way 
on command line enter
nano ~/.config/lxsession/LXDE-pi/autostart
and add at the end 
@lxterminal -e "/home/pi/Desktop/SmartHouseServer.sh"

then coppy [SmartHouseServer.sh ](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/SmartHouseServer.sh) on desktop folder as the above command orders


