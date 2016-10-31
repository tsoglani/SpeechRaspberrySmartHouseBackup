/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.synthesiser.Synthesiser;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import jaco.mp3.player.MP3Player;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import org.joda.time.DateTime;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;

/**
 *
 * @author tsoglani
 */
public class Jarvis {

    /**
     * @param args the command line arguments
     */
    private Microphone mic;
    private GSpeechDuplex duplex;
    public static final String name = "";
    private static Weather weather;
    private String output;
    private String bigOutput = "";
    private Process findMobileProcess;
    private Process speechProcess;
    private String musicFilesLocation = "/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/Music";
    public static final String GeoCiyyLocation = "/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/resources/location/GeoLiteCity.dat";
    private final String findMobileScr = "/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/resources/FindMobile.jar";
    private GSpeechResponseListener gSpeechListener;
    private Thread googleResponseThread;
    private int counter;
    private int sleepTime = 10;
    private final int totalWaitTime = 2000;
    //    private String respondText = "";
    private String lastProcessRespond = "";
    private MP3Player mp3Player;
    File musicFile ;

    Thread playerThread;

    // samples ::
    // 1 command per time not support multiple commands
    //in ten minutes turn kitchen lights off etc.
    //turn kitchen lights off 
    // how are you
    //hello-hi
    // search - what is ... // wiki search
    // play music-play sound ... next song .. previous song .. cancel
    // find mobile-find phone
    // how old are you
    // bye-goodbye-goodnight // want to close all the lights with this command
    // about
    // info - informations
    // weather
    // weather in ... (Athens)
    // weather in ... (London at UK)
    // what's your name
    // what's your mission-purpose
    // are you clever
    // is your boss clever
    // turn on-off all except ...
    // turn on-off all 
    // turn on-off all in n time except ...
    // turn on-off all in n time  
    // what day is today 
    //time now/current
    //date now/current
    //alarm in N time --- example alarm in 30 seconds
    //alarm at specific time --- example alarm at eight and ten //  alarm at half past ten //alarm at ten to ten
    // time in (City) px London/Athens
    // date in (City) px London/Athens
    // date and time in (City) px London/Athens
    //want to do 
    //implement raspberry and create timer methods
    //better speech voice
    //speech mobile commands send to processRespond function (mobile speech commands starts with "speech@@@" must implement it on raspberry side)
    // music festival searching
    //  public static void main(String[] args) {
    //    new SH();
    //  new Jarvis().run();
    //        new person_speech_recognition.PersonRecognition();
    //   }
    private SH sh;

    public Jarvis(SH sh) {
        this.sh = sh;
        musicFile = new File(musicFilesLocation);
        if(!musicFile.exists()){
            musicFile.mkdir();
        }
        dospeak("hello sir.");
        weather = new Weather();

        gSpeechListener = new GSpeechResponseListener() {
            //            String old_text = "";

            public void onResponse(GoogleResponse gr) {

                System.out.println(gr.getResponse());
                output=gr.getResponse();
                if (gr.getResponse() == null || gr.getResponse().replaceAll(" ", "").equalsIgnoreCase("")) {

                    if(sh.fr.speechTextLabel!=null)
                        bigOutput = sh.fr.speechTextLabel.getText();
                    if (bigOutput.contains("(")) {
                        if(sh.fr.speechTextLabel!=null)
                            bigOutput =sh.fr.speechTextLabel.getText();
                    }

                    bigOutput= bigOutput.replace(")", "").replace("( ", "");
                    // response.setText(bigOutput);

                    processRespond(bigOutput,false);

                    //                    System.out.println("output=" + output + "  ::prevOut=" + bigOutput);
                    //  bigOutput = "";
                    // output = "";
                    counter = 0;

                } else {

                    if (output.contains("(")) {
                        output = output.substring(0, output.indexOf(40));
                    }
                    if (!gr.getOtherPossibleResponses().isEmpty()) {
                        output = String.valueOf(output) + " (" + (String)gr.getOtherPossibleResponses().get(0) + ")";
                    }
                    System.out.println(output);
                    bigOutput=output;
                    if(sh.fr.speechTextLabel!=null){
                        sh.fr.speechTextLabel.setText("");
                        sh.fr.speechTextLabel.setText(output);

                    }
                    //  sh.fr.speechTextLabel.setText("( "+output+" )");
                    //  output = gr.getResponse();
                    //   if(sh.fr.speechTextLabel!=null)
                    //    sh.fr.speechTextLabel.setText("( "+output+" )");
                }

                
                
                if (googleResponseThread == null) {
                    googleResponseThread = new Thread() {
                        @Override
                        public void run() {
                            counter = totalWaitTime;

                            try {
                                while (counter > 0) {
                                    sleep(sleepTime);
                                    counter -= sleepTime;
                                }
                                if (output != null && !output.replaceAll(" ", "").equals("")) {
                                    output = "";
                                    if (bigOutput != null && !bigOutput.replaceAll(" ", "").equals("")) {
                                        if (!lastProcessRespond.equals(bigOutput)) {
                                            processRespond(bigOutput,false);

                                        }
                                        lastProcessRespond = "";
                                        bigOutput = "";
                                        //  deActivate();

                                        //    activate();
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            googleResponseThread = null;
                        }
                    };
                    googleResponseThread.start();
                } else {
                    counter = totalWaitTime;

                }

                //                if (gr != null && gr.getResponse() != null && !output.replaceAll(" ", "").equals("")) {
                //                    if (thread == null) {
                //                        thread = new Thread() {
                //                            @Override
                //                            public void run() {
                //                                try {
                //                                    sleep(1000);
                //                                    processRespond(output);
                //
                //                                } catch (InterruptedException ex) {
                //                                    Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
                //                                }
                //                                thread = null;
                //                            }
                //                        };
                //                        thread.start();
                //                    }
                ////                    processRespond(output);
                //                }
            }
        };

   

    }

    public void run() {
        //        System.out.println("getTimeIn:" + getTimeIn("Greece"));

        //        playAudio();
        //        findMobile();

        // boolean isCommandExcecuted = processRespond("kitchen lights off");
        //  System.out.println("command excecuted " + isCommandExcecuted);
try{
         mic = new Microphone(FLACFileWriter.FLAC);
        //  AudioFromat format=javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
        //mic = new Microphone(javax.sound.sampled.AudioFileFormat.Type.WAVE);
  
        }catch(Exception e){} 
        activate();
    }

    public void activate() {

        try {
  duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
              duplex.start();
            duplex.setLanguage("en");
            duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());

            // sh.fr.speechButton.setIcon(sh.fr.speechIcon);

        
            sh.fr.speechButton.setIcon(sh.fr.stop_speechIcon);

            // duplex.removeResponseListener(gSpeechListener);
            duplex.addResponseListener(gSpeechListener);

        } catch (Exception ex) {
            ex.printStackTrace();
            sh.fr.speechButton.setIcon(sh.fr.speechIcon);
        }catch (Error ex) {
            sh.fr.speechButton.setIcon(sh.fr.speechIcon);
            ex.printStackTrace();
        }

    }
    String result;
    protected String processRespond(String respond,boolean isFromPhone) {
        result=null;
        if(respond==null){
            if(sh.fr.speechTextLabel!=null )
                sh.fr.speechTextLabel.setText("");
            return null;}
        boolean hasCommandFound = false;
        try{
            if(!respond.startsWith(name)&&!isFromPhone){
                dospeak("you didn't say my name.");
                System.out.println("you didn't say my name fist.");
                return "you didn't say my name fist.";
            }else if(respond.startsWith(name+" ")){
                respond.substring((name+" ").length(), respond.length());
            }else if(respond.startsWith(name)){
                respond.substring((name).length(), respond.length());
            }

            lastProcessRespond = respond;
            if(sh.fr.speechTextLabel!=null)
                sh.fr.speechTextLabel.setText(respond);
            boolean foundCommand = false;
            System.out.println("respond::" + respond);

            if ( (respond.replaceAll(" ", "").contains("on")
                || respond.replaceAll(" ", "").contains("open")
                || respond.replaceAll(" ", "").contains("enable")
                || respond.replaceAll(" ", "").contains("activate"))
            && (respond.replaceAll(" ", "").contains("all")
                || respond.replaceAll(" ", "").contains("everything"))) {
                boolean found = false;

                if (respond.contains("except")) {
                    if (respond.contains("in ") || respond.contains("time")) {
                        try {
                            String timeContainsText = null;
                            if (respond.contains("in ")) {
                                timeContainsText = respond.split("in ")[1];
                            } else if (respond.contains("time")) {
                                timeContainsText = respond.split("time")[1];

                            }
                            if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                                dospeak("can you repeat the command sir?");
                                respond="@@speechOnly@@"+respond;
                                System.out.println("repeat command");
                                return null;
                            }

                            String[] wordsList = timeContainsText.split(" ");
                            for (String time : wordsList) {

                                //                            String time = timeContainsText.split(" ")[0];
                                String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                                int number = 0;
                                try {
                                    number = Integer.parseInt(time);
                                } catch (Exception e) {
                                    number = inNumerals(time);

                                }
                                if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                    continue;
                                }
                                found = true;
                                if (timeUnit.contains("min")) {
                                    if (number > 1) {
                                        timeUnit = "minutes";
                                    } else {
                                        timeUnit = "minute";
                                    }
                                } else if (timeUnit.contains("sec")) {
                                    if (number > 1) {
                                        timeUnit = "seconds";
                                    } else {
                                        timeUnit = "second";
                                    }
                                } else if (timeUnit.contains("hour")) {
                                    if (number > 1) {
                                        timeUnit = "hours";
                                    } else {
                                        timeUnit = "hour";
                                    }
                                } else if (number > 1) {
                                    timeUnit = "minutes";
                                } else {
                                    timeUnit = "minute";
                                }

                                String cmd = respond.split("except")[1];

                                for (int i = 0; i < sh.outputPowerCommands.length; i++) {
                                    ArrayList<String> lineCommands = sh.outputPowerCommands[i];
                                    for (String str : lineCommands) {
                                        if (cmd.contains(str)) {
                                            System.out.println("except");
                                            foundCommand = true;
                                            turnOnAllDeviceExeptInNTime(str, number, timeUnit);
                                            hasCommandFound = true;
                                            break;
                                        }
                                    }
                                }

                            }
                            if (!found) {
                                System.out.println("Can you repeat the command sir ?");

                                dospeak("Can you repeat the command sir ?");
                                respond="@@speechOnly@@"+respond;
                                return null;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        for (int i = 0; i < sh.outputPowerCommands.length; i++) {
                            ArrayList<String> lineCommands = sh.outputPowerCommands[i];
                            for (String str : lineCommands) {
                                if (respond.split("except")[1].contains(str)) {

                                    foundCommand = true;
                                    openAllExcept(str);
                                    respond="turn all on except "+str;
                                    hasCommandFound = true;
                                    break;
                                }
                            }
                        }

                    }

                } else if (respond.contains("in ") || respond.contains("time")) {

                    try {
                        String timeContainsText = null;
                        if (respond.contains("in ")) {
                            timeContainsText = respond.split("in ")[1];
                        } else if (respond.contains("time")) {
                            timeContainsText = respond.split("time")[1];

                        }
                        if (timeContainsText == null) {
                            return null;
                        }

                        String[] wordsList = timeContainsText.split(" ");
                        for (String time : wordsList) {

                            //                            String time = timeContainsText.split(" ")[0];
                            String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);

                            }
                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            }
                            found = true;
                            if (timeUnit.contains("min")) {
                                if (number > 1) {
                                    timeUnit = "minutes";
                                } else {
                                    timeUnit = "minute";
                                }
                            } else if (timeUnit.contains("sec")) {
                                if (number > 1) {
                                    timeUnit = "seconds";
                                } else {
                                    timeUnit = "second";
                                }
                            } else if (timeUnit.contains("hour")) {
                                if (number > 1) {
                                    timeUnit = "hours";
                                } else {
                                    timeUnit = "hour";
                                }
                            } else if (number > 1) {
                                timeUnit = "minutes";
                            } else {
                                timeUnit = "minute";
                            }
                            foundCommand = true;
                            hasCommandFound = true;
                            turnOnAllDeviceInNTime(number, timeUnit);
                            break;
                        }
                        if (!found) {
                            System.out.println("Can you repeat the command sir ?");

                            dospeak("Can you repeat the command sir ?");
                            respond="@@speechOnly@@"+respond;
                            return null;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    foundCommand = true;
                    hasCommandFound = true;
                    respond="turn all on";
                    openAll();
                }
                //            if (found) {
                //                dospeak("right away sir.");
                //            }
                //                lastProcessRespond = "";

            } else if ((respond.replaceAll(" ", "").contains("on")
                || respond.replaceAll(" ", "").contains("open")
                || respond.replaceAll(" ", "").contains("enable")
                || respond.replaceAll(" ", "").contains("activate"))) {
                boolean found = false;

                for (int i = 0; i < sh.outputPowerCommands.length; i++) {
                    ArrayList<String> lineCommands = sh.outputPowerCommands[i];

                    for (String outCommand : lineCommands) {
                        if (respond.contains(outCommand)) {
                            if (respond.contains("in ") || respond.contains("time")) {
                                try {
                                    String timeContainsText = null;
                                    if (respond.contains("in ")) {
                                        timeContainsText = respond.split("in ")[1];
                                    } else if (respond.contains("time")) {
                                        timeContainsText = respond.split("time")[1];

                                    }
                                    if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                                        dospeak("can you repeat the command sir?");
                                        respond="@@speechOnly@@"+respond;
                                        System.out.println("repeat command");
                                        return null;
                                    }

                                    String[] wordsList = timeContainsText.split(" ");
                                    for (String time : wordsList) {

                                        //                            String time = timeContainsText.split(" ")[0];
                                        String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                                        int number = 0;
                                        try {
                                            number = Integer.parseInt(time);
                                        } catch (Exception e) {
                                            number = inNumerals(time);

                                        }
                                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                            continue;
                                        }
                                        found = true;
                                        if (timeUnit.contains("hour")) {
                                            if (number > 1) {
                                                timeUnit = "hours";
                                            } else {
                                                timeUnit = "hour";
                                            }
                                        } else if (timeUnit.contains("min")) {
                                            if (number > 1) {
                                                timeUnit = "minutes";
                                            } else {
                                                timeUnit = "minute";
                                            }
                                        } else if (timeUnit.contains("sec")) {
                                            if (number > 1) {
                                                timeUnit = "seconds";
                                            } else {
                                                timeUnit = "second";
                                            }
                                        } else if (number > 1) {
                                            timeUnit = "minutes";
                                        } else {
                                            timeUnit = "minute";
                                        }
                                        foundCommand = true;
                                        hasCommandFound = true;
                                        turnOnDeviceInNTime(outCommand, number, timeUnit);
                                        break;
                                    }
                                    if (!found) {
                                        System.out.println("Can you repeat the command sir ?");

                                        dospeak("Can you repeat the command sir ?");
                                        respond="@@speechOnly@@"+respond;
                                        return null;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //                        foundCommand = true;
                                //                        hasCommandFound = true;

                                for (int k = 0; k < sh.outputPowerCommands.length; k++) {
                                    ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
                                    for (String str : lineCommands2) {
                                        if (outCommand.contains(str)) {

                                            foundCommand = true;
                                            turnOnDevice(str);
                                            respond="turn "+str+" on";
                                            hasCommandFound = true;
                                            break;
                                        }
                                    }
                                }

                                //                        turnOnDevice(outCommand);
                            }
                        }
                    }
                }
                //                lastProcessRespond = "";
            } else if ( (respond.replaceAll(" ", "").contains("off")
                || respond.replaceAll(" ", "").contains("close")
                || respond.replaceAll(" ", "").contains("disable")
                || respond.replaceAll(" ", "").contains("deactivate"))
            && (respond.replaceAll(" ", "").contains("all")
                || respond.replaceAll(" ", "").contains("everything"))) {

                if (respond.contains("except")) {
                    if (respond.contains("in ") || respond.contains("time")) {
                        try {
                            String timeContainsText = null;
                            if (respond.contains("in ")) {
                                timeContainsText = respond.split("in ")[1];
                            } else if (respond.contains("time")) {
                                timeContainsText = respond.split("time")[1];

                            }
                            if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                                dospeak("can you repeat the command sir?");
                                respond="@@speechOnly@@"+respond;
                                System.out.println("repeat command");
                                return null;
                            }

                            boolean found = false;
                            String[] wordsList = timeContainsText.split(" ");
                            for (String time : wordsList) {

                                //                            String time = timeContainsText.split(" ")[0];
                                String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                                int number = 0;
                                try {
                                    number = Integer.parseInt(time);
                                } catch (Exception e) {
                                    number = inNumerals(time);

                                }
                                if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                    continue;
                                }
                                found = true;
                                if (timeUnit.contains("min")) {
                                    if (number > 1) {
                                        timeUnit = "minutes";
                                    } else {
                                        timeUnit = "minute";
                                    }
                                } else if (timeUnit.contains("sec")) {
                                    if (number > 1) {
                                        timeUnit = "seconds";
                                    } else {
                                        timeUnit = "second";
                                    }
                                } else if (timeUnit.contains("hour")) {
                                    if (number > 1) {
                                        timeUnit = "hours";
                                    } else {
                                        timeUnit = "hour";
                                    }
                                } else if (number > 1) {
                                    timeUnit = "minutes";
                                } else {
                                    timeUnit = "minute";
                                }

                                String cmd = respond.split("except")[1];
                                //                            for (String str : SH.outputPowerCommands) {
                                //                                if (cmd.contains(str)) {
                                //                                    foundCommand = true;
                                //                                    turnOffAllDeviceExeptInNTime(str, number, timeUnit);
                                //                                    hasCommandFound = true;
                                //                                    break;
                                //                                }
                                //                            }

                                for (int k = 0; k < sh.outputPowerCommands.length; k++) {
                                    ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
                                    for (String str : lineCommands2) {
                                        if (cmd.contains(str)) {
                                            foundCommand = true;
                                            turnOffAllDeviceExeptInNTime(str, number, timeUnit);
                                            hasCommandFound = true;
                                            break;
                                        }
                                    }
                                }

                            }
                            if (!found) {

                                System.out.println("Can you repeat the command sir ?");
                                dospeak("Can you repeat the command sir ?");
                                respond="@@speechOnly@@"+respond;
                                return null;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //                    foundCommand = true;
                        //                    hasCommandFound = true;
                        //                    

                        for (int k = 0; k < sh.outputPowerCommands.length; k++) {
                            ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
                            for (String str : lineCommands2) {
                                if (respond.split("except")[1].contains(str)) {
                                    System.out.println("containsss");
                                    foundCommand = true;
                                    closeAllExcept(str);
                                    respond="turn all off except "+str;
                                    hasCommandFound = true;
                                    break;
                                }
                            }
                        }
                    }

                } else if (respond.contains("in ") || respond.contains("time")) {
                    try {
                        String timeContainsText = null;
                        if (respond.contains("in ")) {
                            timeContainsText = respond.split("in ")[1];
                        } else if (respond.contains("time")) {
                            timeContainsText = respond.split("time")[1];

                        }
                        if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                            dospeak("can you repeat the command sir?");
                            respond="@@speechOnly@@"+respond;
                            System.out.println("repeat command");
                            return null;
                        }

                        boolean found = false;
                        String[] wordsList = timeContainsText.split(" ");
                        for (String time : wordsList) {

                            //                            String time = timeContainsText.split(" ")[0];
                            String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);

                            }
                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            }
                            found = true;
                            if (timeUnit.contains("min")) {
                                if (number > 1) {
                                    timeUnit = "minutes";
                                } else {
                                    timeUnit = "minute";
                                }
                            } else if (timeUnit.contains("sec")) {
                                if (number > 1) {
                                    timeUnit = "seconds";
                                } else {
                                    timeUnit = "second";
                                }
                            } else if (timeUnit.contains("hour")) {
                                if (number > 1) {
                                    timeUnit = "hours";
                                } else {
                                    timeUnit = "hour";
                                }
                            } else if (number > 1) {
                                timeUnit = "minutes";
                            } else {
                                timeUnit = "minute";
                            }
                            foundCommand = true;
                            hasCommandFound = true;
                            turnOffAllDeviceInNTime(number, timeUnit);
                            break;
                        }
                        if (!found) {
                            System.out.println("Can you repeat the command sir ?");

                            dospeak("Can you repeat the command sir ?");
                            respond="@@speechOnly@@"+respond;
                            return null;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    foundCommand = true;
                    hasCommandFound = true;
                    closeAll();
                    respond="turn all off";
                }
                //            if (foundCommand) {
                //                
                //                dospeak("right away sir.");
                //            }
                //                respond = "";
                //                lastProcessRespond = "";

            } else if ((respond.replaceAll(" ", "").contains("off")
                || respond.replaceAll(" ", "").contains("close")
                || respond.replaceAll(" ", "").contains("disable")
                || respond.replaceAll(" ", "").contains("deactivate"))) {

                for (int k = 0; k < sh.outputPowerCommands.length; k++) {
                    ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];

                    for (String outCommand : lineCommands2) {
                        if (respond.contains(outCommand)) {
                            if (respond.contains("in ") || respond.contains("time")) {
                                try {
                                    String timeContainsText = null;
                                    if (respond.contains("in ")) {
                                        timeContainsText = respond.split("in ")[1];
                                    } else if (respond.contains("time")) {
                                        timeContainsText = respond.split("time")[1];

                                    }
                                    if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                                        dospeak("can you repeat the command sir?");
                                        respond="@@speechOnly@@"+respond;
                                        System.out.println("repeat command");
                                        return null;
                                    }

                                    boolean found = false;
                                    String[] wordsList = timeContainsText.split(" ");
                                    for (String time : wordsList) {

                                        //                            String time = timeContainsText.split(" ")[0];
                                        String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                                        int number = 0;
                                        try {
                                            number = Integer.parseInt(time);
                                        } catch (Exception e) {
                                            number = inNumerals(time);

                                        }
                                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                            continue;
                                        }
                                        found = true;
                                        if (timeUnit.contains("min")) {
                                            if (number > 1) {
                                                timeUnit = "minutes";
                                            } else {
                                                timeUnit = "minute";
                                            }
                                        } else if (timeUnit.contains("sec")) {
                                            if (number > 1) {
                                                timeUnit = "seconds";
                                            } else {
                                                timeUnit = "second";
                                            }
                                        } else if (timeUnit.contains("hour")) {
                                            if (number > 1) {
                                                timeUnit = "hours";
                                            } else {
                                                timeUnit = "hour";
                                            }
                                        } else if (number > 1) {
                                            timeUnit = "minutes";
                                        } else {
                                            timeUnit = "minute";
                                        }
                                        foundCommand = true;
                                        hasCommandFound = true;
                                        turnOffDeviceInNTime(outCommand, number, timeUnit);
                                        break;
                                    }
                                    if (!found) {
                                        System.out.println("Can you repeat the command sir ?");

                                        dospeak("Can you repeat the command sir ?");
                                        respond="@@speechOnly@@"+respond;
                                        return null;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //                        foundCommand = true;
                                //                        hasCommandFound = true;
                                //                        turnOffDevice(outCommand);

                                for (int i = 0; i < sh.outputPowerCommands.length; i++) {
                                    ArrayList<String> lineCommands = sh.outputPowerCommands[i];
                                    for (String str : lineCommands) {
                                        if (outCommand.contains(str)) {
                                            System.out.println("containsss");
                                            foundCommand = true;
                                            turnOffDevice(str);
                                            respond="turn "+str+" off";
                                            hasCommandFound = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //                respond = "";
                //                lastProcessRespond = "";
            }

            if ((respond.replaceAll(
                    " ", "").startsWith("whatis")
                || respond.replaceAll(" ", "").startsWith(  "whatis")
                || respond.replaceAll(" ", "").startsWith(  "what's")
                || respond.replaceAll(" ", "").startsWith( "what's")
            ) ||respond.replaceAll(" ", "").startsWith("searchfromwiki") 
            ||respond.replaceAll(" ", "").startsWith("wiki") 

            || respond.replaceAll(" ", "").startsWith("searchwiki")
            || respond.replaceAll(" ", "").startsWith("wikisearch")
            || respond.replaceAll(" ", "").startsWith("wikipediasearch")) {
                System.out.println("wikiii");
                String spStr = null;
                try {
                    if (respond.startsWith("search from wiki")) {
                        spStr = getFromWiki(respond.substring("search from wiki".length(),respond.length()));
                    }  else      if (respond.startsWith("search from wikipedia")) {
                        spStr = getFromWiki(respond.substring("search from wikipedia".length(),respond.length()));
                    } else    if (respond.startsWith("search wiki")) {
                        spStr = getFromWiki(respond.substring("search wiki".length(),respond.length()));
                    }  else    if (respond.startsWith("search wikipedia")) {
                        spStr = getFromWiki(respond.substring("search wikipedia".length(),respond.length()));
                    } else    if (respond.startsWith("wiki search")) {
                        spStr = getFromWiki(respond.substring("wiki search".length(),respond.length()));
                    } else    if (respond.startsWith("wikipedia search")) {
                        spStr = getFromWiki(respond.substring("wikipedia search".length(),respond.length()));
                    }else    if (respond.startsWith("wikipedia")) {
                        spStr = getFromWiki(respond.substring("wikipedia ".length(),respond.length()));
                    }else    if (respond.startsWith("wiki ")) {
                        spStr = getFromWiki(respond.substring("wiki ".length(),respond.length()));
                    }else if (respond.replaceAll(" ", "").contains("whatis")) {
                        spStr = getFromWiki(respond.split("what is")[1]);
                    } else if (respond.replaceAll(" ", "").contains("what's")) {
                        spStr = getFromWiki(respond.split("what's")[1]);
                    }
                    if (spStr != null) {
                        //                    System.out.println("getFromWiki:" + spStr);
                        result=   dospeak(spStr);
                        respond="@@speechOnly@@"+respond;
                        hasCommandFound = true;
                        foundCommand = true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //                    respond = "";
                //                    lastProcessRespond = "";
            }

            //        if (!foundCommand) {

            if (respond.replaceAll(
                " ", "").contains("weatherin" )&&respond.replaceAll(
                " ", "").contains("forecast" ) ) {
                respond.replace("forecast","");

                String spliString=null;
                if(respond.contains("weather in " )){
                    spliString="weather in ";
                }else{
                    spliString="weather in";
                }
                String[] list = respond.split(spliString);
                try {
                    if (list.length > 1) {
                        String curentCity = list[1];
                        String weatherInfo=null;
                        weatherInfo=new Info().getForcastWeather(curentCity);
                        System.out.println(curentCity+" forecast weather "+weatherInfo);
                        if(weatherInfo!=null){
                            result=  dospeak(weatherInfo);

                            respond="@@speechOnly@@"+respond;
                            hasCommandFound = true;
                        }else{
                            if (curentCity.length() > 1) {
                                if (curentCity.contains(" of ")) {
                                    String[] surentList = curentCity.split(" of ");
                                    curentCity = surentList[0];
                                    String countryCode = surentList[1];
                                    weatherInfo = weather.getForecastWeatherAtCity(curentCity);
                                    result= dospeak(weatherInfo);
                                    respond="@@speechOnly@@"+respond;
                                    hasCommandFound = true;
                                } else if (curentCity != null && !curentCity.equals("")) {
                                    System.out.println("curentCity" + curentCity);
                                    weatherInfo = weather.getForecastWeatherAtCity(curentCity);
                                    result= dospeak(weatherInfo);
                                    hasCommandFound = true;
                                    respond="@@speechOnly@@"+respond;
                                }
                            }
                        }}
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //            dospeak("wellcome sir");

                //            String weatherInfo = weather.getWeather(curentCity);
                //            dospeak(weatherInfo);

                lastProcessRespond = "";
            }    
            else if (respond.replaceAll(
                " ", "").contains("weatherin" ) ) {

                String spliString=null;
                if(respond.contains("weather in " )){
                    spliString="weather in ";
                }else{
                    spliString="weather in";
                }

                String[] list = respond.split(spliString);
                try {
                    if (list.length > 1) {
                        String curentCity = list[1];
                        System.out.println("weather in "+curentCity);
                        String wi=null;
                        wi=new Info().getWeather(curentCity);
                        System.out.println(curentCity+":curentCity  "+wi);
                        if(wi!=null){
                            result= dospeak(wi);
                            respond="@@speechOnly@@"+respond;
                            hasCommandFound = true;
                        }else{
                            if (curentCity.length() > 1) {
                                if (curentCity.contains(" of ")) {
                                    String[] surentList = curentCity.split(" of ");
                                    curentCity = surentList[0];
                                    String countryCode = surentList[1];
                                    String weatherInfo = weather.getWeather(curentCity, countryCode);
                                    result=   dospeak(weatherInfo);
                                    respond="@@speechOnly@@"+respond;
                                    hasCommandFound = true;
                                } else if (curentCity != null && !curentCity.equals("")) {
                                    System.out.println("curentCity" + curentCity);
                                    String weatherInfo = weather.getWeather(curentCity);
                                    result= dospeak(weatherInfo);
                                    respond="@@speechOnly@@"+respond;
                                    hasCommandFound = true;
                                }
                            }
                        }}
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //            dospeak("wellcome sir");

                //            String weatherInfo = weather.getWeather(curentCity);
                //            dospeak(weatherInfo);

                lastProcessRespond = "";
            } else if (( respond.replaceAll(" ", "").contains(  "weather"))
            &&respond.contains("now")) {

                String weatherInfo=null;
                if (respond.replaceAll(" ", "").contains("forecast")) {

                    weatherInfo=new Info().getForcastWeather("Ierapetra");
                    if(weatherInfo==null){

                        weatherInfo = weather.getForecastWeatherAtCity("Ierapetra");
                    }
                } else {

                    weatherInfo=new Info().getWeather("ierapetra");
                    System.out.println("weatherInfo "+weatherInfo);
                    if(weatherInfo==null){

                        weatherInfo = weather.getWeather("Ierapetra");
                        System.out.println("weatherInfo "+weatherInfo);
                    }

                }

                hasCommandFound = true;
                result=    dospeak(weatherInfo);
                respond="@@speechOnly@@"+respond;
                lastProcessRespond = "";
            } else if (respond.replaceAll(" ", "").contains("time")
            && respond.replaceAll(" ", "").contains("date")
            && respond.replaceAll(" ", "").contains("in")) {
                String[] words = respond.split(" ");
                for (String s : words) {
                    if (!s.equals("date") || !s.equals("in") || !s.equals("current") || !s.equals("now") || !s.equals("time")) {
                        String info =null;

                        info = getDateAndTimeIn(s);
                        if (info != null) {
                            result=    dospeak(info);
                            hasCommandFound = true;
                            respond="@@speechOnly@@"+respond;
                            break;
                        }
                    }
                }
            } else if ((respond.replaceAll(
                    " ", "").contains("date") || respond.replaceAll(
                    " ", "").contains(  "calendar")|| respond.replaceAll(
                    " ", "").contains("diary") || respond.replaceAll(
                    " ", "").contains("year" ) || respond.replaceAll(
                    " ", "").contains("month" )) && (respond.replaceAll(
                    " ", "").contains("curret") || respond.replaceAll(
                    " ", "").contains("get") || respond.replaceAll(
                    " ", "").contains("local") || respond.replaceAll(
                    " ", "").contains("now"))) {//diary 
                hasCommandFound = true;
                result=  dospeak(getDate());
                respond="@@speechOnly@@"+respond;
            } else if (respond.replaceAll(
                " ", "").contains("day" ) && respond.replaceAll(
                " ", "").contains(  "today")) {
                hasCommandFound = true;
                respond="@@speechOnly@@"+respond;
                result=  dospeak(getStringDate() + " sir.");
            } else if (respond.replaceAll(
                " ", "").contains("time") && (respond.replaceAll(
                    " ", "").contains("curret") || respond.replaceAll(
                    " ", "").contains("get") || respond.replaceAll(
                    " ", "").contains("local") || respond.replaceAll(
                    " ", "").contains("now"))) {
                hasCommandFound = true;
                result= dospeak(getCurentTime());
                respond="@@speechOnly@@"+respond;
            } else if ((respond.replaceAll(" ", "").contains( "timein") || respond.replaceAll(" ", "").contains("currenttime")
                || (respond.replaceAll(" ", "").contains("now")) && respond.replaceAll(" ", "").contains("time"))) {
                String[] words = respond.split(" ");
                System.out.println("current time");
                for (String s : words) {
                    if (!s.equals("time") && !s.equals("in")  &&  !s.equals("current")  &&  !s.equals("now")) {

                        String info=null;
                        info= getTimeIn(s, true);
                        if(info==null)
                            info= new Info(). getTime(s);
                        if (info != null||info.replaceAll(" ","").equals("")) {
                            result=    dospeak(info);
                            hasCommandFound = true;
                            respond="@@speechOnly@@"+respond;
                            break;
                        }

                        System.out.println(s+" info "+info);

                    }
                }
            } else if ((respond.replaceAll(" ", "").contains( "datein") || respond.replaceAll(" ", "").contains("currentdate")
                || (respond.replaceAll(" ", "").contains("now") && respond.replaceAll(" ", "").contains("date")))) {
                String[] words = respond.split(" ");
                for (String s : words) {
                    if (!s.equals("date") || !s.equals("in") || !s.equals("current") || !s.equals("now")) {
                        String info = getDateIN(s, true);
                        if (info != null) {
                            result=  dospeak(info);
                            respond="@@speechOnly@@"+respond;
                            hasCommandFound = true;
                        }
                    }
                }
            } else if (respond.replaceAll(
                " ", "").contains(  "alarm") ) {
                if (respond.contains("in ") || respond.contains("time")) {
                    try {
                        String timeContainsText = null;
                        if (respond.contains("in ")) {
                            timeContainsText = respond.split("in ")[1];

                        } else if (respond.contains("time")) {
                            timeContainsText = respond.split("time")[1];

                        }
                        if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                            dospeak("can you repeat the command sir?");
                            System.out.println("repeat command");
                            return null;
                        }

                        boolean found = false;
                        String[] wordsList = timeContainsText.split(" ");
                        for (String time : wordsList) {

                            //                            String time = timeContainsText.split(" ")[0];
                            String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);

                            }
                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            }
                            found = true;

                            System.out.println(timeUnit);
                            if (timeUnit.contains("min")) {
                                if (number > 1) {
                                    timeUnit = "minutes";
                                } else {
                                    timeUnit = "minute";
                                }
                            } else if (timeUnit.contains("sec") || timeUnit.contains("seconds")) {
                                if (number > 1) {
                                    timeUnit = "seconds";
                                } else {
                                    timeUnit = "second";
                                }
                            } else if (timeUnit.contains("hour")) {
                                if (number > 1) {
                                    timeUnit = "hours";
                                } else {
                                    timeUnit = "hour";
                                }
                            } else if (number > 1) {
                                timeUnit = "minutes";
                            } else {
                                timeUnit = "minute";
                            }

                            alarmInNTime(number, timeUnit, true);

                            hasCommandFound = true;   break;
                        }
                        if (!found) {
                            System.out.println("Can you repeat the command sir ?");

                            dospeak("Can you repeat the command sir ?");
                            return null;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (respond.contains("at ")) {
                    String timeContainsText = respond.split("at ")[1];
                    int hour = -1, min = -1;
                    String hourString, minuteString;
                    System.out.println("contain alarm at");

                    if (timeContainsText.contains(":") || timeContainsText.contains("and")) {

                        if (timeContainsText.contains(":")) {
                            hourString = timeContainsText.split(":")[0];
                            minuteString = timeContainsText.split(":")[1];
                        } else {
                            hourString = timeContainsText.split("and")[0];
                            minuteString = timeContainsText.split("and")[1];

                        }
                        for (String time : hourString.split(" ")) {

                            //                            String time = timeContainsText.split(" ")[0];
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);

                            }
                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            } else {

                                if (respond.contains("post meridiem") || respond.contains(" pm") || respond.contains("p.m.")) {
                                    if (number < 12) {
                                        number += 12;
                                    } else if (number == 12) {
                                        number = 0;
                                    }
                                }
                                hour = number;
                            }

                        }
                        for (String time : minuteString.split(" ")) {

                            //                            String time = timeContainsText.split(" ")[0];
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);
                            }
                            if (time.contains("quarter")) {
                                number = 15;
                            } else if (time.contains("half")) {
                                number = 30;
                            }

                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            } else {
                                min = number;
                            }

                        }

                    } else if (timeContainsText.replaceAll(" ", "").contains("oclock") || timeContainsText.replaceAll(" ", "").contains("o'clock")) {

                        hourString = timeContainsText.split("oclock")[0];
                        minuteString = "0";

                        for (String time : hourString.split(" ")) {

                            //                            String time = timeContainsText.split(" ")[0];
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);

                            }
                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            } else {

                                if (respond.contains("post meridiem") || respond.contains(" pm") || respond.contains("p.m.")) {
                                    if (number < 12) {
                                        number += 12;
                                    } else if (number == 12) {
                                        number = 0;
                                    }
                                }
                                hour = number;
                            }

                        }
                        min = 0;

                    } else if (timeContainsText.contains(" past ")) {

                        System.out.println("contain alarm past");

                        hourString = timeContainsText.split(" past ")[1];
                        minuteString = timeContainsText.split(" past ")[0];

                        for (String time : hourString.split(" ")) {

                            //                            String time = timeContainsText.split(" ")[0];
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);

                            }
                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            } else {

                                if (respond.contains("post meridiem") || respond.contains(" pm") || respond.contains("p.m.")) {
                                    if (number < 12) {
                                        number += 12;
                                    } else if (number == 12) {
                                        number = 0;
                                    }
                                }
                                hour = number;
                            }

                        }
                        for (String time : minuteString.split(" ")) {

                            //                            String time = timeContainsText.split(" ")[0];
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);
                            }
                            if (time.contains("quarter")) {
                                number = 15;
                            } else if (time.contains("half")) {
                                number = 30;
                            }

                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            } else {
                                min = number;
                            }

                        }

                    } else if (timeContainsText.contains(" to ")) {

                        System.out.println("contain alarm past");

                        hourString = timeContainsText.split(" to ")[1];
                        minuteString = timeContainsText.split(" to ")[0];

                        for (String time : hourString.split(" ")) {

                            //                            String time = timeContainsText.split(" ")[0];
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);

                            }
                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            } else {
                                number--;
                                if (respond.contains("post meridiem") || respond.contains(" pm") || respond.contains("p.m.")) {
                                    if (number < 12) {
                                        number += 12;
                                    } else if (number == 12) {
                                        number = 0;
                                    }
                                }
                                hour = number;
                            }
                            break;

                        }
                        for (String time : minuteString.split(" ")) {

                            //                            String time = timeContainsText.split(" ")[0];
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);
                            }
                            if (time.contains("quarter")) {
                                number = 15;
                            } else if (time.contains("half")) {
                                number = 30;
                            }

                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            } else {
                                min = 60 - number;

                            }
                            break;

                        }

                    }

                    System.out.println("hour " + hour);
                    System.out.println("min " + min);

                    if (hour != -1 && min != -1) {
                        hasCommandFound = true;
                        alarmAtTime(hour, min);
                    }

                    lastProcessRespond = "";
                }
            } else if ((respond.replaceAll(
                    " ", "").equalsIgnoreCase(  "hi") || respond.replaceAll(" ", "").equalsIgnoreCase(  "hello"))) {
                welcome();
                hasCommandFound = true;
                respond="@@speechOnly@@"+respond;
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                " ", "").contains( "aresokind")) {
                result=   dospeak("Thank you sir.");
                hasCommandFound = true;
                respond="@@speechOnly@@"+respond;
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                " ", "").contains(  "what") &&respond.replaceAll(
                " ", "").contains(  "your") && (respond.replaceAll(" ", "").contains("mission")
                || respond.replaceAll(" ", "").contains("purpose" ) || respond.replaceAll(" ", "").contains("aim" )
                || respond.replaceAll(" ", "").contains("aim" ) || respond.replaceAll(" ", "").contains("aim" ))) {
                result= dospeak("My purpose is to surve as good as I can my master.");
                hasCommandFound = true;
                respond="@@speechOnly@@"+respond;
                lastProcessRespond = "";
            }  else if ((respond.replaceAll(
                    " ", "").contains( "stop") || respond.replaceAll(" ", "").contains("cancel" ))&&(respond.replaceAll(
                    " ", "").contains( "alarm"))) {
                hasCommandFound = true;
                if(alarmProcess!=null&&alarmProcess.isAlive())
                    alarmProcess.destroy();
            }else if ((respond.replaceAll(
                    " ", "").contains( "stop") || respond.replaceAll(" ", "").contains("cancel" )|| respond.replaceAll(" ", "").contains("close" )|| respond.replaceAll(" ", "").contains("destroy" ))
            &&(respond.replaceAll(
                    " ", "").contains( "times") || respond.replaceAll(" ", "").contains("timers" )
            )) {
                hasCommandFound = true;
                stopAllTimers();
            }else if ((respond.replaceAll(
                    " ", "").contains(  "stop") || respond.replaceAll(" ", "").contains("cancel" ))) {
                if (respond.replaceAll(
                    " ", "").contains(  "stoptimer") ) {
                    stopAllTimers();
                    hasCommandFound = true;

                } else {
                    stopSpeak();
                    hasCommandFound = true;

                }
                lastProcessRespond = "";
            } else if ((respond.replaceAll(
                    " ", "").contains(  "bye") 
                || respond.replaceAll(" ", "").contains( "goodbye") 
                || respond.replaceAll(" ", "").contains(  "see you")) ) {

                turnEverythingOff();
                hasCommandFound = true;

                respond="@@speechOnly@@"+respond;
                lastProcessRespond = "";
            } else if ((respond.replaceAll(
                    " ", "").contains("find") || respond.replaceAll(" ", "").contains("whereis"))
            && (respond.replaceAll(" ", "").contains("phone") || respond.replaceAll(" ", "").contains("mobile"))) {
                System.out.println("findMobile search");
                findMobile();
                hasCommandFound = true;

                lastProcessRespond = "";
                result= dospeak("right away sir.");
            } else if ((respond.replaceAll(
                    " ", "").equalsIgnoreCase(  "about")) ){
                result=  dospeak("I am a smart house application, made by nikos Gaitanis, known as tsoglani!.My purpose is to surve my master.");
                respond="@@speechOnly@@"+respond;
                hasCommandFound = true;
                lastProcessRespond = "";
            } else if ((respond.replaceAll(
                    " ", "").equalsIgnoreCase(  "info") 
                || respond.replaceAll(" ", "").equalsIgnoreCase(  "information")) ) {
                result=    dospeak("I am authorized to turn on or off the electrical devices, and asnswear to some of your question master.");
                respond="@@speechOnly@@"+respond;
                hasCommandFound = true;
                lastProcessRespond = "";
            } else if ((respond.replaceAll(
                    " ", "").contains("clever") && respond.replaceAll(" ", "").contains("areyou"))) {
                result=   dospeak("I am as cleven as a mashine can be sir.");
                respond="@@speechOnly@@"+respond;
                hasCommandFound = true;
                lastProcessRespond = "";
            }  else if (respond.replaceAll(
                " ", "").contains("fuck") && (respond.replaceAll(" ", "").contains("you") ||respond.replaceAll(" ", "").contains("off"))) {
                result=     dospeak("I don't see that comming sir!");
                respond="@@speechOnly@@"+respond;
                hasCommandFound = true;
                lastProcessRespond = "";
            } else if ((respond.replaceAll(
                    " ", "").contains("clever") || respond.replaceAll(
                    " ", "").contains("smart") || respond.replaceAll(
                    " ", "").contains("intelligent") || respond.replaceAll(
                    " ", "").contains("ingenious") || respond.replaceAll(
                    " ", "").contains("brainy") || respond.replaceAll(
                    " ", "").contains("elegant") || respond.replaceAll(
                    " ", "").contains("smart"))
            && (respond.replaceAll(" ", "").contains("nick") || respond.replaceAll(" ", "").contains("nikos")
                || respond.replaceAll(" ", "").contains("tsoglani") || respond.replaceAll(" ", "").contains("master") || respond.replaceAll(" ", "").contains("boss"))) {
                result=   dospeak("Yes, he is very clever, I could also say he is a jenious.");
                respond="@@speechOnly@@"+respond;
                hasCommandFound = true;
                lastProcessRespond = "";
            } else if (respond.replaceAll(" ", "").contains( "howoldareyou")
            || respond.replaceAll(" ", "").contains("isyourage")) {
                result=  dospeak("I am a machine, I have no age, I am imortal.");
                respond="@@speechOnly@@"+respond;
                hasCommandFound = true;
                lastProcessRespond = "";
            } else if ((respond.replaceAll(
                    " ", "").contains("boss") || respond.replaceAll(" ", "").contains(  "nick") || respond.replaceAll(" ", "").contains(  "nikos")
                || respond.replaceAll(" ", "").contains(  "tsoglani") 
                || respond.replaceAll(" ", "").contains(  "master"))
            && (respond.replaceAll(" ", "").contains("pussy") || respond.replaceAll(" ", "").contains("gay") || respond.replaceAll(" ", "").contains("homo")
                || respond.replaceAll(" ", "").contains("stupid") || respond.replaceAll(" ", "").contains("motherfucker") || respond.replaceAll(" ", "").contains("suck")
                || respond.replaceAll(" ", "").contains("sucker") || respond.replaceAll(" ", "").contains("fool") || respond.replaceAll(" ", "").contains("simp")
                || respond.replaceAll(" ", "").contains("dupe") || respond.replaceAll(" ", "").contains("simpleton") || respond.replaceAll(" ", "").contains("moron")
                || respond.replaceAll(" ", "").contains("jackass") || respond.replaceAll(" ", "").contains("goof") || respond.replaceAll(" ", "").contains("slob")
                || respond.replaceAll(" ", "").contains("booby") || respond.replaceAll(" ", "").contains("duffer") || respond.replaceAll(" ", "").contains("idiot"))) {

                result=    dospeak("No he is not, beware your words for my master sir.");
                hasCommandFound = true;
                respond="@@speechOnly@@"+respond;
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                " ", "").equalsIgnoreCase("whatisyourname") || respond.replaceAll(" ", "").equalsIgnoreCase("what'syourname")|| respond.replaceAll(" ", "").equalsIgnoreCase("whatsyourname") || respond.replaceAll(" ", "").equalsIgnoreCase("whoareyou")) {
                if (name.replaceAll(" ", "").equals("")) {
                    result=     dospeak("I have no name sir.");
                } else {
                    result=    dospeak("My name is " + name + " sir.");
                }
                hasCommandFound = true;
                respond="@@speechOnly@@"+respond;
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                " ", "").contains("pause" ) || respond.replaceAll(
                " ", "").contains(  "pause")) {
                hasCommandFound = true;
                pauseAudio();
            } else if (respond.replaceAll(
                " ", "").contains("resume" ) || respond.replaceAll(
                " ", "").contains(  "resume")) {
                hasCommandFound = true;
                resumeAudio();
            } else if (respond.replaceAll(
                " ", "").contains("playaudio" )
            || respond.replaceAll(" ", "").contains(  "playaudio")
            || respond.replaceAll(" ", "").contains(  "playsound")
            || respond.replaceAll(" ", "").contains(  "playmusic")
            || respond.replaceAll(" ", "").contains( "playsong")) {

                result=      dospeak("right away sir.");
                playAudio();

                lastProcessRespond = "";
                hasCommandFound = true;
            } else if (respond.replaceAll(
                " ", "").contains("nextsong")
            || respond.replaceAll(" ", "").contains("next")) {
                nextSong();
                hasCommandFound = true;

            } else if (respond.replaceAll(
                " ", "").contains("previous")
            || respond.replaceAll(" ", "").contains("previoussong")) {
                previousSong();
                hasCommandFound = true;

                lastProcessRespond = "";
            } else if (respond.replaceAll( " ", "").contains(  "howareyou")){
                hasCommandFound = true;
                result=  dospeak("I am fine sir, thanks for asking. Would you like something else.");
                respond="@@speechOnly@@"+respond;
            } else if ((respond.replaceAll(
                    " ", "").contains("reboot" ) ||(respond.replaceAll(
                        " ", "").contains("restart" )))&& respond.replaceAll(" ", "").contains("now" )) {
                hasCommandFound = true;
                rebootDevice();
            } else if (!respond.contains(respond)) {
                hasCommandFound = true;
                respond += " " + respond;
            } //            }
        }catch(Exception e){                e.printStackTrace();}
        catch(Error e){                e.printStackTrace();}

        if(!hasCommandFound){
            if (respond.startsWith("search")) {
                respond=respond.substring("search ".length(),respond.length());
                result= new OnlineInfo().getInfoFromPage(respond);
            } 

            
            if(result==null||result.replaceAll(" ","").equals("")
            ||result.startsWith("Sorry, I don't")){
                result=  new ChatterBotExample().ask(respond);
                hasCommandFound=true;
                if(result!=null&&!result.replaceAll(" ","").equals(""))
                    respond="@@speechOnly@@"+respond;
                if(result==null||result.replaceAll(" ","").equals("")
                ||(result.contains("I don't understand"))
                ||(result.contains("I do not understand"))){

                    result= new OnlineInfo().getInfoFromPage(respond);
                    if(result!=null&&!result.replaceAll(" ","").equals(""))
                        respond="@@speechOnly@@"+respond;
                }
                if(result==null||result.replaceAll(" ","").equals("")
                ||result.startsWith("Sorry, I don't")){
                    hasCommandFound=false;
                }
            }else{
                hasCommandFound=true;
            }
            result=   dospeak(result);
        }

        if(hasCommandFound)

            return respond+"\n-"+result;
        else
            return null;
    }

    protected void findMobile() {
        try {

            new FindMobile(sh);
        } catch (Exception ex) {
            ex.printStackTrace();   }
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();

        String dateString = dateFormat.format(date).toString().split(" ")[0];

        String output = "";
        String[] numList = dateString.split("/");
        for (int i = numList.length - 1; i >= 0; i--) {
            if (i == 0) {
                output += " " + numList[i];
            } else if (i == 1) {
                try {
                    output += " of " + getMonthForInt(Integer.parseInt(numList[i]));
                } catch (Exception e) {
                    output += Integer.parseInt(numList[i]);

                }
            } else if (i == 2) {

                output += numList[i];
            }
        }
        System.out.println(output);
        return output;//2014/08/06 16:06:54
    }

    private String getStringDate() {

        Calendar cal = Calendar.getInstance();
        String dayOfWeek = getDayOfWeek(cal.DAY_OF_WEEK);
        return dayOfWeek;
    }

    private String getCurentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        return (dateFormat.format(date).toString().split(" ")[1]);//2014/08/06 16:06:54
    }

    boolean hasAlarm = false;
    Thread alarmThread;
    int timeCounter;

    void alarmAtTime(int hour, int min,int sec) {
        hour = hour % 24;
        min = min % 60;
        try {
            System.out.println("activate alarm");

            String fullTime = "";
            if (hour < 10) {
                fullTime += "0";
            }
            fullTime += hour + ":";
            if (min < 10) {
                fullTime += "0";
            }
            fullTime += min + ":00";

            Calendar cal = Calendar.getInstance();
            int curentHour = cal.get(cal.HOUR_OF_DAY);
            int curentMin = cal.get(cal.MINUTE);
            int curentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

            //          Calendar c = Calendar.getInstance(); 
            //            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //     //get current date time with Date()
            //     Date date = new Date();
            //     System.out.println(dateFormat.format(date));
            //    
            //     //get current date time with Calendar()
            //    dateFormat.format(cal.getTime());
            //c.setTime(date); 
            //c.add(Calendar.DATE, 1);
            //date = c.getTime();
            Date targetDateAlarm;
            DateTime usingTimeDate;
            String extraInfo = "today";
            if (curentHour > hour || curentHour == hour && curentMin >= min) {
                System.out.println("tomorrow date");
                extraInfo = "tomorrow";
                //                org.joda.time.DateTimeZone timeZone = org.joda.time.DateTimeZone.forID("America/Los_Angeles");
                DateTime now = new DateTime();
                usingTimeDate = now.plusDays(1);

                //                int year = tomorrowAsJUDate.getYear();
                //                int month = tomorrowAsJUDate.getMonth();
                //                int day = tomorrowAsJUDate.getDate();
                //                System.out.println(year + "/" + month + "/" + day);
                //                targetDateAlarm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(year + "/" + month + "/" + day + " " + fullTime);
                //                System.out.println(targetDateAlarm);
            } else {
                usingTimeDate = new DateTime();
                //                targetDateAlarm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(fullTime);
            }

            targetDateAlarm = usingTimeDate.toDate();
            targetDateAlarm.setHours(hour);
            targetDateAlarm.setMinutes(min);
            targetDateAlarm.setSeconds(sec);
            System.out.println(targetDateAlarm);
            DateTime now = new DateTime();

            long diffInMillis = (targetDateAlarm.getTime() - now.toDate().getTime()) / 1000;
            System.out.println(diffInMillis + " seconds");
            result=  dospeak("alarm will ring at " + hour + " and " + min + " o'clock " + extraInfo + " sir.");

            alarmInNTime((int) diffInMillis, "seconds", false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void alarmAtTime(int hour, int min) {
        hour = hour % 24;
        min = min % 60;
        try {
            System.out.println("activate alarm");

            String fullTime = "";
            if (hour < 10) {
                fullTime += "0";
            }
            fullTime += hour + ":";
            if (min < 10) {
                fullTime += "0";
            }
            fullTime += min + ":00";

            Calendar cal = Calendar.getInstance();
            int curentHour = cal.get(cal.HOUR_OF_DAY);
            int curentMin = cal.get(cal.MINUTE);
            int curentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

            //          Calendar c = Calendar.getInstance(); 
            //            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //     //get current date time with Date()
            //     Date date = new Date();
            //     System.out.println(dateFormat.format(date));
            //    
            //     //get current date time with Calendar()
            //    dateFormat.format(cal.getTime());
            //c.setTime(date); 
            //c.add(Calendar.DATE, 1);
            //date = c.getTime();
            Date targetDateAlarm;
            DateTime usingTimeDate;
            String extraInfo = "today";
            if (curentHour > hour || curentHour == hour && curentMin >= min) {
                System.out.println("tomorrow date");
                extraInfo = "tomorrow";
                //                org.joda.time.DateTimeZone timeZone = org.joda.time.DateTimeZone.forID("America/Los_Angeles");
                DateTime now = new DateTime();
                usingTimeDate = now.plusDays(1);

                //                int year = tomorrowAsJUDate.getYear();
                //                int month = tomorrowAsJUDate.getMonth();
                //                int day = tomorrowAsJUDate.getDate();
                //                System.out.println(year + "/" + month + "/" + day);
                //                targetDateAlarm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(year + "/" + month + "/" + day + " " + fullTime);
                //                System.out.println(targetDateAlarm);
            } else {
                usingTimeDate = new DateTime();
                //                targetDateAlarm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(fullTime);
            }

            targetDateAlarm = usingTimeDate.toDate();
            targetDateAlarm.setHours(hour);
            targetDateAlarm.setMinutes(min);
            targetDateAlarm.setSeconds(0);
            System.out.println(targetDateAlarm);
            DateTime now = new DateTime();

            long diffInMillis = (targetDateAlarm.getTime() - now.toDate().getTime()) / 1000;
            System.out.println(diffInMillis + " seconds");
            result=  dospeak("alarm will ring at " + hour + " and " + min + " o'clock " + extraInfo + " sir.");

            alarmInNTime((int) diffInMillis, "seconds", false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int prevTime=0;
    private void alarmInNTime(int time, String type, boolean isGoingToTalk) {
        if (isGoingToTalk) {
            result=    dospeak("alarm in " + time + " " + type);
        }
        System.out.println("alarm in ");
        if (type.startsWith("minute")) {
            time *= 60;
        } else if (type.startsWith("hour")) {
            time *= 60 * 60;
        }
        timeCounter = time;
        prevTime=timeCounter;
        if (alarmThread == null) {
            alarmThread = new Thread() {
                @Override
                public void run() {

                    hasAlarm = true;
                    updateAlarm(timeCounter);
                    while (hasAlarm) {
                        try {
                            sleep(1000);

                            timeCounter--;

                            System.out.println(timeCounter);

                            if (timeCounter <= 0) {
                                hasAlarm = false;
                                chooseRandomSong();
                                System.out.println("play");
                            }

                            if(prevTime!=(timeCounter+1)){
                                updateAlarm(timeCounter); 
                                prevTime=timeCounter;
                            }else
                                prevTime=timeCounter;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    alarmThread = null;
                    hasAlarm = false;
                    prevTime=0;
                    timeCounter=0;
                    sh.fr.alarmButton.setIcon(  sh.fr.alarmIcon);
                    sh.fr.alarmButton.setText( "");
                }

            };
            alarmThread.start();
        }

    }

    private void updateAlarm(int time){
        if(sh.fr.extraInfoPanel!=null&&sh.fr.addAlarmPanel!=null)
            sh.fr.extraInfoPanel.remove( sh.fr.addAlarmPanel);
        sh.fr.alarmButton.setIcon(null);
        sh.fr.findMobileButton.setIcon( sh.fr.findMobileIcon) ;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, time);
        int hour=calendar.getTime().getHours(),
        min=calendar.getTime().getMinutes(),
        sec=calendar.getTime().getSeconds();

        String hourTime="",minTime="",secTime="";
        if(hour<10){
            hourTime="0"+hour;
        }else{
            hourTime=""+hour;
        }
        if(min<10){

        }else{
            minTime=""+min;
        }
        if(sec<10){
            secTime="0"+sec;
        }else{
            secTime=""+sec;
        }
        String day=new SimpleDateFormat("EEEE").format(calendar.getTime());
        calendar.add(Calendar.SECOND, timeCounter);
        sh.fr.alarmButton.setText("<html>Alarm at :<br>"
            +day.substring(0,3)+" "+hourTime+":"+minTime+":"+secTime+"</html>");

    }

    Process alarmProcess;
    private void chooseRandomSong() {
        if (speechProcess != null&&speechProcess.isAlive()) {
            try{
                speechProcess.destroy();

            }catch(Exception e){
                e.printStackTrace();
            }}
        if (speakWProcess != null&&speakWProcess.isAlive()) {
            try{
                speakWProcess.destroy();

            }catch(Exception e){
                e.printStackTrace();
            }}
        if(alarmProcess!=null&&alarmProcess.isAlive()){
            alarmProcess.destroy();
        }

        String[] files = musicFile.list();
        boolean notFound = true;
        ArrayList<File> foundSongs = new ArrayList<File>();
        for (int i = 0; i < files.length; i++) {
            String title = files[i];
            File cf = new File(musicFilesLocation + "/" + title);
            if (cf.isFile()) {

                foundSongs.add(cf);
            }

        }
        long seed = System.nanoTime();
        Collections.shuffle(foundSongs, new Random(seed));
        //    mp3Player.addToPlayList(foundSongs.get(0));
        //  mp3Player.play();

        try{
            alarmProcess= Runtime.getRuntime().exec("play "+foundSongs.get(0).getAbsolutePath());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void stopAllTimers() {//// implement stop all timers in // delete the timers arraylist and send the data to all
        TimerCountdown.timers.removeAll(TimerCountdown.timers);
        sh.sendToAll("Timers:DeviceID:" + SH.DeviceID + DB.COMMAND_SPLIT_STRING + TimerCountdown.getAllTimers());
    }

    private void turnOnDevice(String device) {//// test
        System.out.println(device + " turned to on");
        if(!sh.fr.isSwitchModeSelected)
            sh.fr.manualSelected();
        sh.processCommandString(device + " on");
        sh.sendTheUpdates(device + " on");
        sh.sendToAll(device + " on");
        result=  dospeak(device + " turned to on");
    }

    private void turnOnDeviceInNTime(String device, int time, String timeUnit) {//// implement gpiopins
        System.out.println(device + " will turn on in " + time + " " + timeUnit + ".");
        result=  dospeak(device + " will turn on in " + time + " " + timeUnit + ".");
        if (timeUnit.startsWith("minute")) {
            time *= 60;
        } else if (timeUnit.startsWith("hour")) {
            time *= 60 * 60;
        }

        TimerCountdown timer = new TimerCountdown(sh, device + " on", Integer.toString(time), Long.toString(System.currentTimeMillis()));
        timer.start();
        sh.sendToAll("Timers:DeviceID:" + SH.DeviceID + DB.COMMAND_SPLIT_STRING + TimerCountdown.getAllTimers());
        new TimerView(sh.fr);
    }

    private void turnOffDeviceInNTime(String device, int time, String timeUnit) {//// implement gpiopins
        System.out.println(device + " will turn off in " + time + " " + timeUnit + ".");
        if (timeUnit.startsWith("minute")) {
            time *= 60;
        } else if (timeUnit.startsWith("hour")) {
            time *= 60 * 60;
        }
        if(!sh.fr.isTimerModeSelected)
            new TimerView(sh.fr);
        TimerCountdown timer = new TimerCountdown(sh, device + " off", Integer.toString(time), Long.toString(System.currentTimeMillis()));
        timer.start();
        sh.sendToAll("Timers:DeviceID:" + SH.DeviceID + DB.COMMAND_SPLIT_STRING + TimerCountdown.getAllTimers());
        result=   dospeak(device + " will turn off in " + time + " " + timeUnit + ".");
    }

    private void closeAll() {//// implement gpiopins
        System.out.println("close all");
        result=   dospeak("right away sir.");
        if(!sh.fr.isSwitchModeSelected)
            sh.fr.manualSelected();
        for (int k = 0; k < sh.outputPowerCommands.length; k++) {
            ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
            for (String str : lineCommands2) {
                sh.processCommandString(str + " off");

            }
        }

        String msg = sh.getAllCommandOutput();//"getput on@@@getoutt2 off";//
        if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
            sh.sendToAll("respondGetAllCommandsOutput" + msg);
        }

        msg = sh.getAllOutput();//"getput on@@@getoutt2 off";//
        System.out.println("msg = " + msg);
        if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
            sh.sendToAll("respondGetAllOutput" + msg);
        }

    }

    private void openAll() {//// implement gpiopin
        if(!sh.fr.isSwitchModeSelected)
            sh.fr.manualSelected();
        for (int k = 0; k < sh.outputPowerCommands.length; k++) {
            ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
            for (String str : lineCommands2) {
                sh.processCommandString(str + " on");

            }
        }

        String msg = sh.getAllCommandOutput();//"getput on@@@getoutt2 off";//
        if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
            sh.sendToAll("respondGetAllCommandsOutput" + msg);
        }

        msg = sh.getAllOutput();//"getput on@@@getoutt2 off";//
        System.out.println("msg = " + msg);
        if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
            sh.sendToAll("respondGetAllOutput" + msg);
        }

        System.out.println("open all");
        result= dospeak("right away sir.");

    }

    private void turnOffAllDeviceExeptInNTime(String deviceNotToClose, int time, String timeUnit) {//// implement gpiopins

        result=   dospeak("right away sir.");
        if(!sh.fr.isTimerModeSelected)
            new TimerView(sh.fr);
        if (timeUnit.startsWith("minute")) {
            time *= 60;
        } else if (timeUnit.startsWith("hour")) {
            time *= 60 * 60;
        }
        for (int k = 0; k < sh.outputPowerCommands.length; k++) {
            ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
            boolean isSame=false;
            for (String str : lineCommands2) {

                if (str.equals(deviceNotToClose)) {
                    isSame=true;
                    break;
                }
            }
            if(isSame){
                continue;
            }else{
                System.out.println(lineCommands2.get(0) + " off");
                TimerCountdown timer = new TimerCountdown(sh, lineCommands2.get(0) 
                        + " off", Integer.toString(time),
                        Long.toString(System.currentTimeMillis()));
                timer.start();
                System.out.println("close " + lineCommands2.get(0) );  

            }
        }
        sh.sendToAll("Timers:DeviceID:" + SH.DeviceID + DB.COMMAND_SPLIT_STRING + TimerCountdown.getAllTimers());
    }

    private void turnOnAllDeviceExeptInNTime(String deviceNotToClose, int time, String timeUnit) {//// implement gpiopins

        result=  dospeak("right away sir.");
        if(!sh.fr.isTimerModeSelected)
            new TimerView(sh.fr);
        if (timeUnit.startsWith("minute")) {
            time *= 60;
        } else if (timeUnit.startsWith("hour")) {
            time *= 60 * 60;
        }
        for (int k = 0; k < sh.outputPowerCommands.length; k++) {
            ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
            boolean isSame=false;
            for (String str : lineCommands2) {
                //                sh.processCommandString(str + " on");
                if (str.equals(deviceNotToClose)) {
                    isSame=true;
                    break;
                }
            }
            if(isSame){
                continue;
            }else{
                System.out.println(lineCommands2.get(0) + " on");
                TimerCountdown timer = new TimerCountdown(sh, lineCommands2.get(0) + " on", Integer.toString(time), Long.toString(System.currentTimeMillis()));
                timer.start();
                System.out.println("open " + lineCommands2.get(0) );  

            }
        }

        sh.sendToAll("Timers:DeviceID:" + SH.DeviceID + DB.COMMAND_SPLIT_STRING + TimerCountdown.getAllTimers());

    }

    private void turnOffAllDeviceInNTime(int time, String timeUnit) {//// implement gpiopins
        System.out.println("close all in " + time + " " + timeUnit);
        result=    dospeak("right away sir.");
        if (timeUnit.startsWith("minute")) {
            time *= 60;
        } else if (timeUnit.startsWith("hour")) {
            time *= 60 * 60;
        }

        for (int k = 0; k < sh.outputPowerCommands.length; k++) {
            ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];

            String str=lineCommands2.get(0);
            TimerCountdown timer = new TimerCountdown(sh, str + " off", Integer.toString(time), Long.toString(System.currentTimeMillis()));
            timer.start();

            //   }
        }
        sh.sendToAll("Timers:DeviceID:" + SH.DeviceID + DB.COMMAND_SPLIT_STRING + TimerCountdown.getAllTimers());
        if(!sh.fr.isTimerModeSelected)
            new TimerView(sh.fr);
    }

    private void turnOnAllDeviceInNTime(int time, String timeUnit) {//// implement gpiopins
        System.out.println("open all in " + time + " " + timeUnit);
        result=  dospeak("right away sir.");
        if (timeUnit.startsWith("minute")) {
            time *= 60;
        } else if (timeUnit.startsWith("hour")) {
            time *= 60 * 60;
        }
        if(!sh.fr.isTimerModeSelected)
            new TimerView(sh.fr);
        for (int k = 0; k < sh.outputPowerCommands.length; k++) {
            ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
            //   for (String str : lineCommands2) {
            //                sh.processCommandString(str + " on");
            String str=lineCommands2.get(0);
            TimerCountdown timer = new TimerCountdown(sh, str + " on", Integer.toString(time), Long.toString(System.currentTimeMillis()));
            timer.start();

            //   }
        }
        sh.sendToAll("Timers:DeviceID:" + SH.DeviceID + DB.COMMAND_SPLIT_STRING + TimerCountdown.getAllTimers());
    }

    private void closeAllExcept(String commandNotClose) {//// implement gpiopins

        result=   dospeak("right away sir.");
        if(!sh.fr.isSwitchModeSelected)
            sh.fr.manualSelected();
        for (int k = 0; k < sh.outputPowerCommands.length; k++) {
            boolean isSame=false;
            ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
            for (String str : lineCommands2) {

                if (str.equalsIgnoreCase(commandNotClose)) {
                    isSame=true;
                    break;

                }

            }
            if(isSame){
                continue;
            }else{
                sh.processCommandString(lineCommands2.get(0) + " off");}

        }

        String msg = sh.getAllCommandOutput();//"getput on@@@getoutt2 off";//
        if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
            sh.sendToAll("respondGetAllCommandsOutput" + msg);
        }

        msg = sh.getAllOutput();//"getput on@@@getoutt2 off";//
        System.out.println("msg = " + msg);
        if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
            sh.sendToAll("respondGetAllOutput" + msg);
        }

    }

    private void openAllExcept(String commandNotClose) {//// implement gpiopins
        if(!sh.fr.isSwitchModeSelected)
            sh.fr.manualSelected();
        result= dospeak("right away sir.");
        for (int k = 0; k < sh.outputPowerCommands.length; k++) {
            boolean isSame=false;
            ArrayList<String> lineCommands2 = sh.outputPowerCommands[k];
            for (String str : lineCommands2) {
                if (str.replaceAll(" ","").equalsIgnoreCase(commandNotClose.replaceAll(" ",""))) {
                    isSame=true;
                    break;
                }

            }
            if(isSame){
                System.out.println("sammeee" + commandNotClose);
                continue;
            }else{
                System.out.println("open all except " + commandNotClose);
                sh.processCommandString(lineCommands2.get(0) + " on");

            }

            String msg = sh.getAllCommandOutput();//"getput on@@@getoutt2 off";//
            if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
                sh.sendToAll("respondGetAllCommandsOutput" + msg);
            }

            msg = sh.getAllOutput();//"getput on@@@getoutt2 off";//
            System.out.println("msg = " + msg);
            if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
                sh.sendToAll("respondGetAllOutput" + msg);
            }
        }}

    private void turnOffDevice(String device) {//// implement gpiopins
        if(!sh.fr.isSwitchModeSelected)
            sh.fr.manualSelected();
        System.out.println(device + " turned to off");
        result=   dospeak(device + " turned to off");
        sh.processCommandString(device + " off");
        sh.sendTheUpdates(device + " off");
        sh.sendToAll(device + " off");

    }

    private void welcome() {//// implement gpiopins and turn livingroom lights on
        result=   dospeak("welcome sir");
        // turn all the lights off
    }

    private void turnEverythingOff() {
        result= dospeak("bye sir");
        closeAll();
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    private String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
            day = "Sunday";
            break;
            case 2:
            day = "Monday";
            break;
            case 3:
            day = "Tuesday";
            break;
            case 4:
            day = "Wednesday";
            break;
            case 5:
            day = "Thursday";
            break;
            case 6:
            day = "Friday";
            break;
            case 7:
            day = "Saturday";
            break;
        }
        return day;
    }

    private String getFromWiki(String searchingText) throws MalformedURLException, IOException {
        //https://en.wikipedia.org/w/api.php?action=opensearch&search=java-programming&limit=1&format=json
        String[] list = searchingText.split(" ");
        String searchingAddingString = "";
        for (int i = 0; i < list.length; i++) {
            searchingAddingString += list[i];
            if (i < list.length - 1) {
                searchingAddingString += "-";
            }
        }
        if (searchingAddingString.equals("")) {
            return null;
        }

        String urlString = "https://en.wikipedia.org/w/api.php?action=opensearch&search=" + searchingAddingString + "&limit=1&format=json";
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        String body = org.apache.commons.io.IOUtils.toString(in, encoding);
        System.out.println(body);
        //        body.replaceAll("\\[", "@@@");
        //        body.replaceAll("\\]", "@@@");

        String[] listBody = body.split("\\[\"");
        try {
            String output = listBody[3].substring(0, listBody[3].length() - 3);
            System.out.println(output);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //    private static final String VOICENAME_kevin = "kevin";
    //    Voice voice;
    //    VoiceManager voiceManager;
    private void stopSpeak() {
        if (speechProcess != null) {
            speechProcess.destroyForcibly();

        }
        if (speechProcess != null) {
            speechProcess.destroy();

        }

        if (mp3Player != null) {
            mp3Player.stop();
        }
        hasAlarm = false;
        if(alarmProcess!=null&&alarmProcess.isAlive()){
            alarmProcess.destroy();
        }
    }

    public void close() {
        try {
            stopSpeak();

            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void rebootDevice(){ try {
            Runtime.getRuntime().exec("reboot");
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
    Thread playThread;
    //sudo apt-get install libttspico-utils
    Process speakWProcess;
    public String dospeak(String speaktext) {
        try {

            if (speaktext == null||speaktext.replaceAll(" ","").equals("")) {
                speaktext = "Not available, I am sorry, would you like something else sir ?";
                return null;
            }

            speaktext = speaktext.replaceAll("\\(.*\\)", "");
            speaktext = speaktext.replaceAll("\\[.*\\]", "");
            if(sh.fr.speechTextLabel!=null){
                sh.fr.speechTextLabel.setText("<html>"+sh.fr.speechTextLabel.getText()+"<br>- <i>"+speaktext+"</i>"+"</html>");
            }
            if (speechProcess != null&&speechProcess.isAlive()) {
                try{
                    speechProcess.destroy();

                }catch(Exception e){
                    e.printStackTrace();
                }}
            if (speakWProcess != null&&speakWProcess.isAlive()) {
                try{
                    speakWProcess.destroy();

                }catch(Exception e){
                    e.printStackTrace();
                }}

            String extraCommand = "ven-us";
            ////sudo apt-get install espeak
            //           speechProcess = Runtime.getRuntime().exec(new String[]{"pico2wave -w lookdave.wav", '\"' + speaktext + '\"',"\" && aplay lookdave.wav\""});

            //            speechProcess = Runtime.getRuntime().exec(new String[]{"bash", "pico2wave -w tts.wav", speaktext});
            // try{
            //    googleStoreSpeech(speaktext);
            //    }catch(Exception e){}

            speechProcess = Runtime.getRuntime().exec(new String[]{"pico2wave", "-w", "/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/lookdave.wav", '\"' + speaktext + '\"'});

            playThread = new Thread() {
                @Override
                public void run() {
                    try {

                        while (speechProcess.isAlive()) {
                            Thread.sleep(50);
                        }

                        System.out.println("isAlive" + speechProcess.isAlive());

                        if (!speechProcess.isAlive()) {
                            speakWProcess = Runtime.getRuntime().exec("play /home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/lookdave.wav");
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Error ex) {
                        Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            };
            playThread.start();

            //Runtime.getRuntime().exec("pico2wave -w lookdave.wav \"Look Dave, I can see you're really upset about this.\" && aplay lookdave.wav");
            //                speechProcess = Runtime.getRuntime().exec(new String[]{"espeak", '\"' + speaktext + '\"'});
            //
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Error ex) {
            ex.printStackTrace();
        }
        return speaktext;
    }

    private void googleStoreSpeech(String text) throws IOException{
        String language="en-us";
        Synthesiser synth= new Synthesiser(language);
        InputStream is=synth.getMP3Data(text);
        FileOutputStream outStream=new FileOutputStream("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/lookdave.wav");
        int read=0;
        byte []bytes = new byte[8192];
        while((read=is.read(bytes))!=-1){
            outStream.write(bytes,0,read);
        }
    }

    public void deActivate() {
        sh.fr.speechButton.setIcon(sh.fr.speechIcon);

        if (gSpeechListener != null&&duplex!=null) {
            duplex.removeResponseListener(gSpeechListener);
            duplex.closeAll();
        }

        
        //System.gc();
        //        if (voice != null) {
        //            voice.deallocate();
        //        }
        //        voice = null;

    }
    private void pauseAudio() {
        if (mp3Player != null && !mp3Player.isPaused() && !mp3Player.isStopped()) {
            mp3Player.pause();
        }
    }

    private void resumeAudio() {
        if (mp3Player != null && !mp3Player.isPaused()) {
            mp3Player.play();
        }
    }

    private void playAudio() {

        try {

            //            File cf = new File(musicFilesLocation + "/" + musicFile.list()[musicCounterID++]);
            //            if (cf.isDirectory() || !cf.exists()) {
            //                playAudio();
            //            }
            //            String bip = cf.getAbsolutePath();
            //            System.out.println(bip);
            ////            AudioInputStream audioIn = AudioSystem.getAudioInputStream(cf);
            ////            Clip clip = AudioSystem.getClip();
            ////            clip.open(audioIn);
            ////            clip.start();
            if (mp3Player != null && !mp3Player.isStopped()) {
                mp3Player.stop();
            }

            mp3Player = new MP3Player();

            String[] files = musicFile.list();
            for (int i = 0; i < files.length; i++) {
                String title = files[i];
                File cf = new File(musicFilesLocation + "/" + title);
                if (cf.isFile()) {
                    System.out.println(cf.getAbsoluteFile());
                    mp3Player.addToPlayList(cf);
                }

            }
            mp3Player.play();

        } catch (Exception ex) {
            ex.printStackTrace();    }

    }

    private void previousSong() {
        if (mp3Player != null) {
            mp3Player.skipBackward();
        }

    }

    private void nextSong() {
        if (mp3Player != null) {
            mp3Player.skipForward();
        }

    }

    public int inNumerals(String inwords) {

        int wordnum = 0;
        String[] arrinwords = inwords.split(" ");
        int arrinwordsLength = arrinwords.length;
        if (inwords.equals("zero")) {
            return 0;
        }
        if (inwords.contains("thousand")) {
            int indexofthousand = inwords.indexOf("thousand");
            //System.out.println(indexofthousand);
            String beforethousand = inwords.substring(0, indexofthousand);
            //System.out.println(beforethousand);
            String[] arrbeforethousand = beforethousand.split(" ");
            int arrbeforethousandLength = arrbeforethousand.length;
            //System.out.println(arrbeforethousandLength);
            if (arrbeforethousandLength == 2) {
                wordnum = wordnum + 1000 * (wordtonum(arrbeforethousand[0]) + wordtonum(arrbeforethousand[1]));
                //System.out.println(wordnum);
            }
            if (arrbeforethousandLength == 1) {
                wordnum = wordnum + 1000 * (wordtonum(arrbeforethousand[0]));
                //System.out.println(wordnum);
            }

        }
        if (inwords.contains("hundred")) {
            int indexofhundred = inwords.indexOf("hundred");
            //System.out.println(indexofhundred);
            String beforehundred = inwords.substring(0, indexofhundred);

            //System.out.println(beforehundred);
            String[] arrbeforehundred = beforehundred.split(" ");
            int arrbeforehundredLength = arrbeforehundred.length;
            wordnum = wordnum + 100 * (wordtonum(arrbeforehundred[arrbeforehundredLength - 1]));
            String afterhundred = inwords.substring(indexofhundred + 8);//7 for 7 char of hundred and 1 space
            //System.out.println(afterhundred);
            String[] arrafterhundred = afterhundred.split(" ");
            int arrafterhundredLength = arrafterhundred.length;
            if (arrafterhundredLength == 1) {
                wordnum = wordnum + (wordtonum(arrafterhundred[0]));
            }
            if (arrafterhundredLength == 2) {
                wordnum = wordnum + (wordtonum(arrafterhundred[1]) + wordtonum(arrafterhundred[0]));
            }
            //System.out.println(wordnum);

        }
        if (!inwords.contains("thousand") && !inwords.contains("hundred")) {
            if (arrinwordsLength == 1) {
                wordnum = wordnum + (wordtonum(arrinwords[0]));
            }
            if (arrinwordsLength == 2) {
                wordnum = wordnum + (wordtonum(arrinwords[1]) + wordtonum(arrinwords[0]));
            }
            //System.out.println(wordnum);
        }

        return wordnum;
    }

    public int wordtonum(String word) {
        int num = 0;
        switch (word) {
            case "one":
            num = 1;
            break;
            case "two":
            num = 2;
            break;
            case "three":
            num = 3;
            break;
            case "four":
            num = 4;
            break;
            case "five":
            num = 5;
            break;
            case "six":
            num = 6;
            break;
            case "seven":
            num = 7;
            break;
            case "eight":
            num = 8;
            break;
            case "nine":
            num = 9;
            break;
            case "ten":
            num = 10;
            break;
            case "eleven":
            num = 11;
            break;
            case "twelve":
            num = 12;
            break;
            case "thirteen":
            num = 13;
            break;
            case "fourteen":
            num = 14;
            break;
            case "fifteen":
            num = 15;
            break;
            case "sixteen":
            num = 16;
            break;
            case "seventeen":
            num = 17;
            break;
            case "eighteen":
            num = 18;
            break;
            case "nineteen":
            num = 19;
            break;
            case "twenty":
            num = 20;
            break;
            case "thirty":
            num = 30;
            break;
            case "forty":
            num = 40;
            break;
            case "fifty":
            num = 50;
            break;
            case "sixty":
            num = 60;
            break;
            case "seventy":
            num = 70;
            break;
            case "eighty":
            num = 80;
            break;
            case "ninety":
            num = 90;
            break;
            case "hundred":
            num = 100;
            break;
            case "thousand":
            num = 1000;
            break;
            /*default: num = "Invalid month";
            break;*/
        }
        return num;
    }

    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    private String getDateAndTimeIn(String city) {
        city = capitalize(city);
        return "Date is " + getDateIN(city, false) + ", curent time is " + getTimeIn(city, false) + " sir.";
    }

    private String getTimeIn(String city, boolean extraInfo) {
        ZonedDateTime zoneDateTime = null;
        city = capitalize(city);
        for (int i = 0; i < CodeList.length; i++) {
            try {
                String code = CodeList[i];

                ZoneId zone = ZoneId.of(code + "/" + city);
                java.time.LocalDateTime ldt = java.time.LocalDateTime.now(zone);

                System.out.println("TimeZone : " + zone);

                //LocalDateTime + ZoneId = ZonedDateTime
                zoneDateTime = ldt.atZone(zone);
            } catch (Exception e) {
                //e.printStackTrace();
                continue;
            }
            break;
        }
        //        ZoneId newYokZoneId = ZoneId.of("America/New_York");
        //        System.out.println("TimeZone : " + newYokZoneId);
        //
        //        ZonedDateTime nyDateTime = zoneDateTime.withZoneSameInstant(newYokZoneId);
        //        System.out.println("Date (New York) : " + nyDateTime);
        //
        DateTimeFormatter format = DateTimeFormatter.ofPattern(TIME_FORMAT);
        //        System.out.println("\n---DateTimeFormatter---");
        //        System.out.println("Date (Singapore) : " + format.format(zoneDateTime));
        //        System.out.println("Date (New York) : " + format.format(nyDateTime));
        //        System.out.println("Date (Singapore) : " + );
        if (zoneDateTime == null || format == null) {
            return null;
        }
        String output = format.format(zoneDateTime);

        if (extraInfo) {
            output = "Time in " + city + " is " + output + " sir.";
        }
        System.out.println("Time in " + city + " is " + output);

        System.out.println(output);
        return output;
    }

    private String[] CodeList = {
            "Europe", "Asia", "Pacific", "America", "Atlantic", "Africa", "Antarctica", "Indian", "Australia", "Etc"
        };

    private String getDateIN(String city, boolean extraInfo) {

        city = capitalize(city);

        //       DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //        //get current date time with Date()
        ZonedDateTime zoneDateTime = null;
        for (int i = 0; i < CodeList.length; i++) {
            try {
                String code = CodeList[0];

                java.time.LocalDateTime ldt = java.time.LocalDateTime.now();
                ZoneId zone = ZoneId.of(code + "/" + city);
                System.out.println("TimeZone : " + zone);

                //LocalDateTime + ZoneId = ZonedDateTime
                zoneDateTime = ldt.atZone(zone);
                System.out.println("Date in " + city + " is " + zoneDateTime);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
        //        ZoneId newYokZoneId = ZoneId.of("America/New_York");
        //        System.out.println("TimeZone : " + newYokZoneId);
        //
        //        ZonedDateTime nyDateTime = zoneDateTime.withZoneSameInstant(newYokZoneId);
        //        System.out.println("Date (New York) : " + nyDateTime);
        //
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
        //        System.out.println("\n---DateTimeFormatter---");
        //        System.out.println("Date (Singapore) : " + format.format(zoneDateTime));
        //        System.out.println("Date (New York) : " + format.format(nyDateTime));
        //        System.out.println("Date (Singapore) : " + );
        if (zoneDateTime == null || format == null) {
            return null;
        }

        String dateString = format.format(zoneDateTime).split(" ")[0];

        String output = "";

        String[] numList = dateString.split("/");
        for (int i = numList.length - 1; i >= 0; i--) {
            if (i == 0) {
                output += " " + numList[i];
            } else if (i == 1) {
                try {
                    output += " of " + getMonthForInt(Integer.parseInt(numList[i]));
                } catch (Exception e) {
                    output += Integer.parseInt(numList[i]);

                }
            } else if (i == 2) {

                output += numList[i];
            }
        }
        System.out.println(output);

        if (extraInfo) {
            output = "Date in " + city + " is " + output + " sir.";
        }
        return output;
    }

    public String capitalize(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        string = string.toLowerCase();
        char c[] = string.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

}
