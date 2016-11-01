-sudo apt-get install rpi-update
-sudo BRANCH=next rpi-update
-sudo apt-get install mpg123


#If you are using external usb sound card:


**On Raspberry jessie


-sudo nano /etc/asound.conf
and Enter 
"
pcm.!default {
type hw card 1

}
ctl.!default {
type hw card 1

}


"


**On Raspberry wheezy
-sudo nano /etc/mo/modprobe.d/alsa-base.conf
and change 
   #options snd-usb-audio index=0    (might be =-2)
  to  options snd-usb-audio index=0 



Finally

-reboot
