


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
