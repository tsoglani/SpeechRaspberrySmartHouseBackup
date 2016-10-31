
/**
 * Write a description of class Fr here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import javax.swing.text.StyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

public class Fr extends JFrame
{
    // instance variables - replace the example below with your own
    private JButton manual;
    private JButton timer;
    private JButton auto;
    protected boolean isSwitchModeSelected=false;
    protected boolean isTimerModeSelected=false;
    protected boolean isSheduleModeSelected=false;
    private String state="off";
    protected JButton home;
    protected SH sh;
    ArrayList <JButton> switcButtons;
    protected  SheduleView shv;
    ImageIcon speechIcon;
    ImageIcon stop_speechIcon;
    private JToggleButton toggle;
    JButton speechButton;
    JLabel speechTextLabel;
    JButton alarmButton;
    JButton findMobileButton;
    ImageIcon alarmIcon;
    JPanel extraInfoPanel;
    ImageIcon findMobileIcon,cancelOkIcon,cancelNot_okIcon;
    private ImageIcon findMobileAbcIcon;
    JPanel addAlarmPanel;
    /**
     * Constructor for objects of class Fr
     */
    double width,height;
    private String usinAlarmText;
    public Fr(SH sh)
    {
        this.sh=sh;
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        width=screenSize.getWidth();
        height=screenSize.getHeight();
        
   


    
//      StyleContext context = new StyleContext();
//     StyledDocument document = new DefaultStyledDocument(context);
// 
//     Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
// 
//     StyleConstants.setBackground(style, Color.blue);
// 
//     try {
//       document.insertString(document.getLength(), "java2s.com", style);
//                 speechTextLabel= new JTextPane(document);
//     } catch (Exception e) {
//       // TODO Auto-generated catch block
//           speechTextLabel= new JTextPane();
//       e.printStackTrace();
//     }
         speechTextLabel= new JLabel();


  //      SimpleAttributeSet attribs = new SimpleAttributeSet();
//StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
//speechTextLabel.setParagraphAttributes(attribs, true);


        ImageIcon homeIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/home.png");
        homeIcon=new ImageIcon(getScaledImage(homeIcon.getImage(),(int)(height/20), (int)(height/20)));

        alarmIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/alarm.png");
        alarmIcon=new ImageIcon(getScaledImage(alarmIcon.getImage(),(int)(height/10), (int)(height/10)));

        cancelOkIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/ok.png");
        cancelOkIcon=new ImageIcon(getScaledImage(cancelOkIcon.getImage(),(int)(height/10), (int)(height/10)));
        cancelNot_okIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/not_ok.png");
        cancelNot_okIcon=new ImageIcon(getScaledImage(cancelNot_okIcon.getImage(),(int)(height/10), (int)(height/10)));

        findMobileIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/find_mobile.png");
        findMobileIcon=new ImageIcon(getScaledImage(findMobileIcon.getImage(),(int)(height/10), (int)(height/10)));
        findMobileAbcIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/find_mobile_abc.png");
        findMobileAbcIcon=new ImageIcon(getScaledImage(findMobileAbcIcon.getImage(),(int)(height/10), (int)(height/10)));

        ImageIcon manualIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/manual.png");
        manualIcon=new ImageIcon(getScaledImage(manualIcon.getImage(),(int)(width/4), (int)(3*height/5)));
        speechIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/speech.png");
        speechIcon=new ImageIcon(getScaledImage(speechIcon.getImage(),(int)(height/20), (int)(height/10)));
        stop_speechIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/stop_speech.jpg");
        stop_speechIcon=new ImageIcon(getScaledImage(stop_speechIcon.getImage(),(int)(height/20), (int)(height/20)));
        speechButton= new JButton();
        alarmButton= new JButton();
        manual= new JButton(manualIcon);
        toggle=new JToggleButton("Commands Mode");
        ImageIcon timelIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/time.png");
        timelIcon=new ImageIcon(getScaledImage(timelIcon.getImage(),(int)(width/4), (int)(3*height/5)));
        timer= new JButton(timelIcon);
        findMobileButton= new JButton();
        addAlarmPanel= new JPanel();
        addAlarmPanel.setLayout(new BorderLayout());
        addAlarmPanel.add(new AlarmJPanel());
        ImageIcon automationIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/automation.png");
        automationIcon=new ImageIcon(getScaledImage(automationIcon.getImage(),(int)(width/4), (int)(3*height/5)));
        auto= new JButton(automationIcon);
        home= new JButton(homeIcon);
        home.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    mainMenu();
                    isSwitchModeSelected=false;
                    isTimerModeSelected=false;
                    isSheduleModeSelected=false;
                    shv=null;
                    toggle.setText("Commands Mode");
                }
            });
 toggle.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    if(!toggle.isSelected()){

                        toggle.setText("Commands Mode");
                        manualSelected();
                    }else{
                        toggle.setText("Output Mode");
                        manualSelected();

                    }
                }
            });


        timer.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new TimerView(Fr.this);
                }
            });

        auto.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    new SheduleView(Fr.this);
                }
            });
        alarmButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    if(alarmButton.getIcon()==null){ // is already activated
                        speechTextLabel.setText("Cancel alarm ?");
                        findMobileButton.setIcon(cancelOkIcon) ;
                        alarmButton.setIcon(cancelNot_okIcon);
                        usinAlarmText=alarmButton.getText();
                        alarmButton.setText("");
                    }else if(alarmButton.getIcon()==cancelNot_okIcon){
                        findMobileButton.setIcon(findMobileIcon) ;
                        speechTextLabel.setText("");
                        alarmButton.setText(usinAlarmText);
                        if(  sh.jarvis.hasAlarm)
                            alarmButton.setIcon(null);
                        else{
                            alarmButton.setIcon(alarmIcon);
                        }
                    }else if(alarmButton.getIcon()==alarmIcon){
                        if(sh.jarvis.alarmProcess!=null&&sh.jarvis.alarmProcess.isAlive()){
sh.jarvis.alarmProcess.destroy();
}else
                        showOrHideNewAlarm();
                    }

                }
            });

        findMobileButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    if(  findMobileButton.getIcon()==findMobileIcon ){
                        new Thread(){public void run(){
                                try{
                                    findMobileButton.setEnabled(false);
                                    sh.jarvis.findMobile();
                                    findMobileButton.setIcon(findMobileAbcIcon) ;
                                    sleep(4000);
                                    findMobileButton.setEnabled(true);
                                    findMobileButton.setIcon(findMobileIcon) ;

                                }catch(Exception e){
                                }
                            }}.start();}else if(findMobileButton.getIcon()==cancelOkIcon){

                        alarmButton.setIcon(alarmIcon);

                        speechTextLabel.setText("Alarm canceled.");
                        findMobileButton.setIcon(findMobileIcon) ;

                        sh.jarvis.hasAlarm=false;
                    }

                }
            });
        this.setSize((int)width,(int)height);
        mainMenu();
        setLocation(0,0);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);    
        setVisible(true);
    }

    private void  showOrHideNewAlarm(){
        int child=extraInfoPanel.getComponentCount();
        boolean contains=false;
        for(int i=0;i<child;i++){
            Component cmp=extraInfoPanel.getComponent(i);
            if(cmp==addAlarmPanel){
                contains=true;
                break;

            }
        }
        if(contains){
            extraInfoPanel.remove(addAlarmPanel);
        }else{
            extraInfoPanel.add(addAlarmPanel,BorderLayout.PAGE_END);
        }
        

        extraInfoPanel.repaint();
        extraInfoPanel.revalidate();
    }

    void mainMenu(){
        JPanel menuPanel= new JPanel();
        extraInfoPanel= new JPanel();

        extraInfoPanel.setLayout(new BorderLayout());
speechButton.setIcon(speechIcon);
        speechButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(speechButton.getIcon()==speechIcon){
                        sh.jarvis.activate();
                    }else if(speechButton.getIcon()==stop_speechIcon){
                        sh.jarvis.deActivate();
                    }
                }
            });

        JPanel lineBeginExtraPanel= new JPanel();
        lineBeginExtraPanel.setLayout(new GridLayout(1,2));

        lineBeginExtraPanel.add(alarmButton);
        alarmButton.setIcon(alarmIcon);

        speechTextLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        speechTextLabel.setBackground(Color.GRAY);
        speechTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
         speechTextLabel.setVerticalAlignment(SwingConstants.CENTER);
        speechTextLabel.setAutoscrolls(true);
        lineBeginExtraPanel.add(findMobileButton);
        extraInfoPanel.add(speechTextLabel);

        extraInfoPanel.add(speechButton,BorderLayout.LINE_END);
        findMobileButton.setIcon(findMobileIcon) ;
        extraInfoPanel.add(lineBeginExtraPanel,BorderLayout.LINE_START);
        setLayout(new BorderLayout());

        getContentPane().removeAll();
        menuPanel.setLayout(new GridLayout(1,3));
        menuPanel.add(manual);
        menuPanel.add(timer);
        menuPanel.add(auto);
        add(menuPanel);
        add(extraInfoPanel,BorderLayout.PAGE_END);
        manual.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    manualSelected();
                }
            });

        repaint();
        revalidate();
    }


    void manualSelected(){

        isTimerModeSelected=false;
        isSheduleModeSelected=false;
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        JPanel header=new JPanel();
        JPanel center=new JPanel();
        switcButtons=new <JButton> ArrayList();
        isSwitchModeSelected=true;
       
        header.setLayout(new BorderLayout());
        home.setSize((int)(height/20), (int)(height/20));
        header.add(home,BorderLayout.LINE_START);
        header.add(toggle,BorderLayout.LINE_END);
        add(header,BorderLayout.PAGE_START);

        ArrayList<String>[] usingList;
        String neededOutputs;
        if(toggle.getText().equals("Output Mode")){
            usingList=sh.outputCommands;
            neededOutputs=sh.getAllOutput();
        }else{
            usingList=sh.outputPowerCommands;
            neededOutputs=sh.getAllCommandOutput();
        }
        
        String [] outputs=neededOutputs.split("@@@");

        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length)+1,(int)Math.sqrt(usingList.length)+1));
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            JButton button = new JButton(list.get(0));
            switcButtons.add(button);
            center.add(button);
            button.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){

                        if(button.getBackground()==Color.GREEN){state="off";}else{
                            state="on";
                        }

                        String command =button.getText();
                        new Thread(){public void run(){
                                try {
                                    if(toggle.getText().equals("Output Mode")){
                                        sh.processLedString(command + " " + state);
                                        sh.sendToAll("switch "+command + " " + state);
                                        System.out.println("pressed:"+command + " " + state);
                                        sh.sendTheUpdates(command + " " + state);
                                    }else{
                                        sh.processCommandString(command + " " + state);
                                        sh.sendToAll("switch "+command + " " + state);
                                        System.out.println("pressed:"+command + " " + state);
                                        sh.sendTheUpdates(command + " " + state);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }}.start();

                    }
                });
            for(int j=0;j<outputs.length;j++){
                boolean isOn=false;
                String textNeededFromOutput=null;
                if(outputs[j].endsWith("on")){
                    textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"on".length());
                    isOn=true;

                }else if(outputs[j].endsWith("off")){
                    textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"off".length());
                    isOn=false;
                }
                if(textNeededFromOutput!=null&&textNeededFromOutput.equals(button.getText())){
                    if(isOn){
                        button.setBackground(Color.GREEN);
                    }else{
                        button.setBackground(Color.GRAY);
                    }
                }
            }
        }
        add(center);
header.repaint();
header.revalidate();
center.repaint();
center.revalidate();
getContentPane().repaint();
getContentPane().revalidate();
        repaint();
        revalidate();
    }

    protected void updateManual(){
        ArrayList<String>[] usingList;
        String neededOutputs;
        if(toggle.getText().equals("Output Mode")){
            usingList=sh.outputCommands;
            neededOutputs=sh.getAllOutput();
        }else{
            usingList=sh.outputPowerCommands;
            neededOutputs=sh.getAllCommandOutput();
        }
        String [] outputs=neededOutputs.split("@@@");

        for(int j=0;j<outputs.length;j++){
            System.out.println(outputs[j]);
            boolean isOn=false;
            String textNeededFromOutput=null;
            if(outputs[j].replaceAll(" ","").endsWith("on")){
                textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"on".length());
                isOn=true;

            }else if(outputs[j].endsWith("off")){

                textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"off".length());
                isOn=false;
            }
            JButton button=null;
            if(switcButtons!=null){
                for(int i=0;i<switcButtons.size();i++){

                    if(switcButtons.get(i)!=null&&switcButtons.get(i).getText().equals(textNeededFromOutput)){
                        button=switcButtons.get(i);

                    }
                }

                if(button!=null)
                    if(textNeededFromOutput!=null&&textNeededFromOutput.equals(button.getText())){

                        if(isOn){

                            button.setBackground(Color.GREEN);
                        }else{
                            button.setBackground(Color.GRAY);
                        }
                    }
            }
        }
    }

    protected void changeState(String message){
        boolean isOn=false;
        String textNeededFromOutput=null;
        if(message.endsWith("on")){
            textNeededFromOutput=message.substring(0,message.length()-1-"on".length());
            isOn=true;

        }else if(message.endsWith("off")){
            textNeededFromOutput=message.substring(0,message.length()-1-"off".length());
            isOn=false;
        }

        if(switcButtons!=null){
            for(int i=0;i<switcButtons.size();i++){
                System.out.println(switcButtons.get(i).getText()+":changeState:"+textNeededFromOutput);
                if(switcButtons.get(i).getText().equals(textNeededFromOutput)){
                    if(!isOn){
                        switcButtons.get(i).setBackground(Color.GRAY);
                    }else{
                        switcButtons.get(i).setBackground(Color.GREEN);
                    }

                }
            }}

    }

    protected Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    private class AlarmJPanel extends JPanel{
        int color_id=0;
        int sec=0,min=0,hours=0;
        private JPanel centerPanel= new JPanel();
        private JPanel hourPanel= new JPanel();
        private JPanel minPanel= new JPanel();
        private JPanel secPanel= new JPanel();
        private JSlider hourSlider,minSlider,secSlider;
        private JLabel hourLabel,minLabel,secLabel;
        private JLabel hourValues,minValues,secValues;
        public AlarmJPanel(){
            hourPanel.setLayout(new BoxLayout(hourPanel,BoxLayout.Y_AXIS));
            minPanel.setLayout(new BoxLayout(minPanel,BoxLayout.Y_AXIS));
            secPanel.setLayout(new BoxLayout(secPanel,BoxLayout.Y_AXIS));
            hourSlider= new JSlider(0,23);
            hourSlider.setValue(0);
            hourSlider.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent changeEvent) {
                        Object source = changeEvent.getSource();
                        JSlider theJSlider = (JSlider) source;
                        hours=theJSlider.getValue();

                        String hoursString="";
                        if(hours>9){
                            hoursString=Integer.toString(hours);
                        }else{
                            hoursString="0"+Integer.toString(hours);
                        }
                        hourValues.setText(hoursString);

                    }
                });

            minSlider= new JSlider(0,59);
            minSlider.setValue(0);
            minSlider.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent changeEvent) {
                        Object source = changeEvent.getSource();
                        JSlider theJSlider = (JSlider) source;
                        min=theJSlider.getValue();
                        String minString="";
                        if(min>9){
                            minString=Integer.toString(min);
                        }else{
                            minString="0"+Integer.toString(min);
                        }
                        minValues.setText(minString);

                    }
                });
            secSlider= new JSlider(0,59);
            secSlider.setValue(0);
            secSlider.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent changeEvent) {
                        Object source = changeEvent.getSource();
                        JSlider theJSlider = (JSlider) source;
                        sec=theJSlider.getValue();
                        String secString="";
                        if(sec>9){
                            secString=Integer.toString(sec);
                            System.out.println(sec);
                        }else{
                            secString="0"+Integer.toString(sec);
                        }
                        secValues.setText(secString);

                    }
                });
            hourLabel= new JLabel("Hours:");
            minLabel= new JLabel("Minutes:");
            secLabel= new JLabel("Seconds:");

            hourValues= new JLabel("00");
            minValues= new JLabel("00");
            secValues= new JLabel("00");
            hourPanel.add(hourLabel); 
            hourPanel.add(hourSlider);
            hourPanel.add(hourValues);
            minPanel.add(minLabel);
            minPanel.add(minSlider);
            minPanel.add(minValues);
            secPanel.add(secLabel);
            secPanel.add(secSlider);
            secPanel.add(secValues);
            // centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            centerPanel.setLayout(new GridLayout(1,3));
            centerPanel.add(hourPanel);
            centerPanel.add(minPanel);
            centerPanel.add(secPanel);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));

            
            JButton save= new JButton(),cancel=new JButton();
            save.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        try{
                            int hour=Integer.parseInt(hourValues.getText())
                            ,min=Integer.parseInt(minValues.getText())
                            ,sec=Integer.parseInt(secValues.getText());
                            sh.jarvis.alarmAtTime(hour, min,sec);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                        extraInfoPanel.remove(addAlarmPanel);
                        extraInfoPanel.repaint();
                        extraInfoPanel.revalidate();
                    }
                });
            cancel.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        extraInfoPanel.remove(addAlarmPanel);
                        extraInfoPanel.repaint();
                        extraInfoPanel.revalidate();
                    }
                });
            save.setIcon(cancelOkIcon);
            cancel.setIcon(cancelNot_okIcon);

            add(centerPanel,BorderLayout.CENTER);
            add(save,BorderLayout.LINE_END);
            add(cancel,BorderLayout.LINE_START);

        }

        private int getRemainingTimeToSeconds(){
            return hours*60*60+min*60+sec;
        }
    }
    
       class MyTextPane extends JTextPane {
        public MyTextPane() {
            super();

            setOpaque(false);

            // this is needed if using Nimbus L&F - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960
            setBackground(new Color(0,0,0,0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            // set background green - but can draw image here too
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            // uncomment the following to draw an image
            // Image img = ...;
            // g.drawImage(img, 0, 0, this);


            super.paintComponent(g);
        }
    }
}
