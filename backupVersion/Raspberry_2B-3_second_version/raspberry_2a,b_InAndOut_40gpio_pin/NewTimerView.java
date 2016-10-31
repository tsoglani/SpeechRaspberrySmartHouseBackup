
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NewTimerView extends JPanel{
    private Fr fr;
    private JButton back,save;
    private  Color []colors = {Color.gray,Color.green,Color.red};
    public NewTimerView(Fr fr) 
    {
        setLayout(new BorderLayout());
        this.fr=fr;
        ImageIcon backTime=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/back.png");
        backTime=new ImageIcon(fr.getScaledImage(backTime.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        back = new JButton(backTime);
        ImageIcon saveIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/save.png");
        saveIcon=new ImageIcon(fr.getScaledImage(saveIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        back = new JButton(backTime);
        save = new JButton(saveIcon);
        save.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    saveFunction();
                    new TimerView(fr); 
                }
            });
        back.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new TimerView(fr);
                }
            });
        createGUI();

    }

    ArrayList   <MyJPanel> myPanels;
    private void createGUI(){
        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);

        JPanel header=new JPanel();
        JPanel bottom=new JPanel();
        bottom.setLayout(new BorderLayout());

        bottom.add(save);

        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        header.add(back,BorderLayout.LINE_START);

        JPanel center=new JPanel();
        myPanels=new <MyJPanel> ArrayList();
        add(header,BorderLayout.PAGE_START);
        add(bottom,BorderLayout.PAGE_END);
        ArrayList<String>[] usingList;
        String neededOutputs;
        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();
        String [] outputs=neededOutputs.split("@@@");
                center.setLayout(new GridLayout((int)Math.sqrt(usingList.length)+1,(int)Math.sqrt(usingList.length)+1));
   add(center);
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0));
            myPanels.add(button);
            center.add(button);
         
        }

        repaint();
        revalidate();
        fr.repaint();
        fr.revalidate();
    }

    private class MyJPanel extends JPanel{
        int color_id=0;
        String title ;
        int sec=0,min=0,hours=0;
        private JPanel centerPanel= new JPanel();
        private JPanel hourPanel= new JPanel();
        private JPanel minPanel= new JPanel();
        private JPanel secPanel= new JPanel();
        private JSlider hourSlider,minSlider,secSlider;
        private JLabel hourLabel,minLabel,secLabel;
        private JLabel hourValues,minValues,secValues;
        public MyJPanel(String title){
            this.title=title;
            hourSlider= new JSlider(0,23);
            hourSlider.setValue(0);
            hourSlider.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent changeEvent) {
                        Object source = changeEvent.getSource();
                        JSlider theJSlider = (JSlider) source;
                        hours=theJSlider.getValue();
                        hourValues.setText(Integer.toString(hours));

                    }
                });

            minSlider= new JSlider(0,59);
            minSlider.setValue(0);
            minSlider.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent changeEvent) {
                        Object source = changeEvent.getSource();
                        JSlider theJSlider = (JSlider) source;
                        min=theJSlider.getValue();
                        minValues.setText(Integer.toString(min));

                    }
                });
            secSlider= new JSlider(0,59);
            secSlider.setValue(0);
            secSlider.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent changeEvent) {
                        Object source = changeEvent.getSource();
                        JSlider theJSlider = (JSlider) source;
                        sec=theJSlider.getValue();
                        secValues.setText(Integer.toString(sec));

                    }
                });
            hourLabel= new JLabel("Hours:");
            minLabel= new JLabel("Minutes:");
            secLabel= new JLabel("Seconds:");

            hourValues= new JLabel("0");
            minValues= new JLabel("0");
            secValues= new JLabel("0");
            hourPanel.add(hourLabel); 
            hourPanel.add(hourSlider);
            hourPanel.add(hourValues);
            minPanel.add(minLabel);
            minPanel.add(minSlider);
            minPanel.add(minValues);
            secPanel.add(secLabel);
            secPanel.add(secSlider);
            secPanel.add(secValues);

            centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            centerPanel.add(hourPanel);
            centerPanel.add(minPanel);
            centerPanel.add(secPanel);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel titleLabel= new JLabel(title);
            JPanel titlePanel= new JPanel();
            titlePanel.add(titleLabel);
            setBackground(colors[color_id]);

            hourPanel.setBackground(colors[color_id]);
            minPanel.setBackground(colors[color_id]);
            secPanel.setBackground(colors[color_id]);
            titlePanel.setBackground(colors[color_id]);

            hourLabel.setBackground(Color.white);
            minLabel.setBackground(Color.white);
            secLabel.setBackground(Color.white);
            hourValues.setBackground(Color.white);
            minValues.setBackground(Color.white);
            secValues.setBackground(Color.white);

            add(titlePanel,BorderLayout.PAGE_START);
            add(centerPanel);
            addMouseListener(new MouseAdapter() { 
                    public void mousePressed(MouseEvent me) { 
                        color_id++;
                        if(color_id>2){
                            color_id=0;
                        }
                        setBackground(colors[color_id]);
                        //centerPanel.setBackground(colors[color_id]);
                        hourPanel.setBackground(colors[color_id]);
                        minPanel.setBackground(colors[color_id]);
                        secPanel.setBackground(colors[color_id]);
                        titlePanel.setBackground(colors[color_id]);
                    } 
                }); 

            centerPanel. addMouseListener(new MouseAdapter() { 
                    public void mousePressed(MouseEvent me) { 
                        color_id++;
                        if(color_id>2){
                            color_id=0;
                        }
                        setBackground(colors[color_id]);
                        //centerPanel.setBackground(colors[color_id]);
                        hourPanel.setBackground(colors[color_id]);
                        minPanel.setBackground(colors[color_id]);
                        secPanel.setBackground(colors[color_id]);
                        titlePanel.setBackground(colors[color_id]);
                    } 
                }); 
        }

        private int getRemainingTimeToSeconds(){
            return hours*60*60+min*60+sec;
        }
    }

    private void saveFunction(){
        for (int i=0;i<myPanels.size();i++){
            MyJPanel mp=myPanels.get(i);
            if(mp.getBackground()!=colors[0]){
                String mode=" off";
                if(mp.getBackground()==colors[1]){
                    mode=" on";
                }
                TimerCountdown timerCountdown=  new TimerCountdown(fr.sh,mp.title+mode,Integer.toString(mp.getRemainingTimeToSeconds()),Long.toString(getTimeStamps()));
                timerCountdown.start();

                new Thread(){
                    public void run(){
                        try{
                            sleep(1000);
                            fr.sh.sendToAll("Timers:DeviceID:"+ fr.sh.DeviceID+ DB.COMMAND_SPLIT_STRING+TimerCountdown.getAllTimers());
                        }catch (Exception e){}
                    }

                }.start();

                System.out.println("Timers:DeviceID:"+ fr.sh.DeviceID+ DB.COMMAND_SPLIT_STRING+TimerCountdown.getAllTimers() );
            }
        }
    }

    private long getTimeStamps(){
        return  System.currentTimeMillis();
    }

}