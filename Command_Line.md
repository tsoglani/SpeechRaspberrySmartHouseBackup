
1)java -version // to see if java is installed, if not enter the following commands<br>
  sudo add-apt-repository ppa:webupd8team/java (optional you might already have it)</br>
   sudo apt-get update
 sudo apt-get install oracle-java8-installer(install java 8)</br>
 sudo apt-get install oracle-java8-set-default(set java 8 default) </br>
 
2)if you not used it before  "sudo apt-get update" </br>

3)sudo apt-get install libttspico0 libttspico-utils libttspico-data<br>
4)sudo apt-get install sox libsox-fmt-all </br>
5)apt-get install iceweasel (install firefox, defauld raspbian browser might not let you download/clone the project---optional) </br>
or</br>
 You can also download it by paste on command line "git clone https://github.com/tsoglani/SpeechRaspberrySmartHouse.git /home/pi/Desktop/SpeechRaspberrySmartHouse"
 <br>
6)curl -s get.pi4j.com | sudo bash</br>
7)sudo apt-get install mpg123</br>
8)sudo apt-get install alsa-utils</br>
9)sudo apt-get install lame</br>
10)sudo apt-get install raspberrypi-ui-mods</br>
11)sudo apt-get install raspberrypi-net-mods</br>
12)sudo apt-get install pi4j</br>
13)sudo apt-get upgrade </br>
14)sudo apt-get dist-upgrade</br>


optional: if you want to use PCA9685 gpio expancion </br>
sudo raspi-config</br>
1) Internationalisation Options  ->Change Timezone -> select timezone</br>
2) Advanced Options  ->SPI -> yes</br>
2) Advanced Options  ->I2C -> yes</br>
3)sudo apt-get install i2c-tools</br>
4)sudo apt-get install python-smbus</br>

