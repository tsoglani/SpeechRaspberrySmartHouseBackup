# Raspberry_SmartHouseServer


This Folder contains Raspberry projects for remote smart house application (enable or dissable any electrical device you want remotely), you have to download this Folder, in this folder there are 6 projects (two for each raspberry device)  choose the project you want to use according to your Raspberry device and your needs.
You will be able to 

In this folder there are three folders:
 -Raspberry 1-2A
 -Raspberry 2B-3
 -RASPBERRY PI COMPUTE MODULE DEV KIT
open the folder you have to use according your raspberry device.

After this you will see two Folders, each one contains one Project, the projects you have to see are:
 -SmartHouseRaspberryServer: the outputs and the inputs have been split in half.
 -SmartHouseRaspberryServerOnlyOutputs: all pins have been used as ouptputs and you have no inputs ( for example: not recommended for light switching). 


***NOTE <br />
- Defauld raspbian browser might not let you download or clone the project from github, if that happened, install firefox browser "apt-get install iceweasel"  <br />
//// must excecuted after "sudo apt-get update" <br />
***OR<br />
You can also do it by paste on command line 
"git clone https://github.com/tsoglani/SpeechRaspberrySmartHouse.git /home/pi/Desktop/SpeechRaspberrySmartHouse"
<br/>
install speech function :  
sudo apt-get install libttspico0 libttspico-utils libttspico-data<br/>
sudo apt-get install sox<br/>
sudo apt-get install sox libsox-fmt-all<br/>



After you download and run the selected project, you would be able to control remotely your raspberry device with the applications below:


-Free (beta) application, works on any computer device:[Computer Client](https://github.com/tsoglani/Java_SmartHouseClient/blob/master/SmartHouseClient/dist/SmartHouseClient.jar).       
NOTE : need to have Java sdk to your computer device (if not, this is the link ->[Java_SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) )


///////// NOT YET<br />
-Paid (Android phone- Android wear(watch)) application: -<br />
Recommended : download free testing version before buy it to be sure that is working for you.<br />
/////////

Select the project you need.
In every "main" folder there is a read me file with instructions.


- the project must be on Desktop Folder else images will not display

Extra informations
- problem with sound on raspberry pi -->[on_sound_problems](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/import_audio.txt)

Good luck ;)

License: [GNU AGPLv3](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/LICENSE.txt)
