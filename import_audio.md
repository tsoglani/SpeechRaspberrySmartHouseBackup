


#If you are using external usb sound card:


it can be with 2 ways:


way no :  1)</br>
-right click on volum icon (right on top)</br>
-select USB PnP Sound Device </br>
-reboot
</br>
way no :  2)</br>
On Raspberry jessie</br>


-sudo nano /etc/asound.conf
and Enter 


if is not empty .. clear all and enter this.
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
