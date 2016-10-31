import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
public class SheduleView extends JPanel
{ private JButton back;
    private Fr fr;
    private  Color []colors = {Color.white,Color.green,Color.red};
    private ArrayList   <MyJPanel> myPanels;
    private JButton addNewSchedule;
    public SheduleView(Fr fr)
    {
         fr.isSwitchModeSelected=false;
                    fr.isTimerModeSelected=false;

        this.fr=fr;
        setLayout(new BorderLayout());

        fr.isSheduleModeSelected=true;
        fr.shv=this;
        ImageIcon addSchedule=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/add_calentar.png");
        addSchedule=new ImageIcon(fr.getScaledImage(addSchedule.getImage(),(int)(fr.height/15), (int)(fr.height/15)));

        addNewSchedule = new JButton(addSchedule);

        addNewSchedule.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    fr.isSheduleModeSelected=false;
                    fr.shv=null;
                    new SelectNewShedule(fr);
                }
            });

        createGUI();
        new Thread(){
            public void run(){
                while(fr.isSheduleModeSelected)   {
                    try{

                        ArrayList<Shedule> shedules=fr.sh.db.getShedules();
                        if(shedules!=null)
                            update(shedules);
                        Thread.sleep(2000);
                    }catch(Exception e){}}
            }}.start();
    }
    ArrayList<SingleSheduleView> deletedSingleSheduleView = new ArrayList<SingleSheduleView>();

    protected void update(ArrayList<Shedule> sheduleList){
        if(sheduleList!=null){
            for(int i=0;i<sheduleList.size();i++){
                Shedule shedule=sheduleList.get(i);
                boolean containsInMyPanels=false;
                boolean isGoingToOpen;
                String timerFullCommand=shedule.getCommandText();

                if(myPanels!=null)
                    for(int j=0;j<myPanels.size();j++){
                        MyJPanel mp=myPanels.get(j);

                        if(mp.title.equals(timerFullCommand)){
                            System.out.println(timerFullCommand+":::"+mp.title);
                            /////for 
                            mp.update(shedule,sheduleList);

                        }
                    }
            }

            deletedSingleSheduleView.removeAll(deletedSingleSheduleView);
            for(int j=0;j<myPanels.size();j++){
                MyJPanel mp=myPanels.get(j);
                JPanel centerPanel=mp.centerPanel;
                for(int i=0;i<centerPanel.getComponentCount();i++){

                    boolean containsInSheduleList=false;
                    SingleSheduleView ssv=(SingleSheduleView)centerPanel.getComponent(i);

                    System.out.print("ssv.id:"+ssv.id);  
                    System.out.println();       
                    for(int k=0;k<sheduleList.size();k++){
                        Shedule shedule2=sheduleList.get(k);
                        System.out.print("   shedule2.id"+shedule2.getId());  
                        if(shedule2.getId()==ssv.id&&shedule2.getCommandText().equals(ssv.commandString)){
                            containsInSheduleList=true;
                            System.out.println("(shedule2.getId()==ssv.id");  
                            break;
                        }

                    }
                    if(!containsInSheduleList){
                        System.out.println("isGoingToDelete");
                        deletedSingleSheduleView.add(ssv);
                    }
                }

                for(int i=0;i<deletedSingleSheduleView.size();i++){
                    centerPanel.remove(deletedSingleSheduleView.get(i));}
                centerPanel.repaint();
                centerPanel.revalidate();
            }

        }
    }

    private void createGUI(){
        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);

        JPanel header=new JPanel();
        JPanel bottom=new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.add(addNewSchedule);
        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        //  header.add(back,BorderLayout.LINE_START);

        JPanel center=new JPanel();
        myPanels=new <MyJPanel> ArrayList();
        add(header,BorderLayout.PAGE_START);
        add(bottom,BorderLayout.PAGE_END);
        // add(bottom,BorderLayout.PAGE_END);
        ArrayList<String>[] usingList;
        String neededOutputs;
        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();
        String [] outputs=neededOutputs.split("@@@");
        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length)+1,(int)Math.sqrt(usingList.length)+1));
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0));
            myPanels.add(button);
            center.add(button);
            add(center);
        }

        repaint();
        revalidate();
        fr.repaint();
        fr.revalidate();

    }
    private class MyJPanel extends JPanel{
        int color_id=0;
        String title ;
        protected JPanel centerPanel= new JPanel();
        private JScrollPane scrollSpecific;
        public MyJPanel(String title){
            this.title=title;
            scrollSpecific = new JScrollPane(centerPanel);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel titleLabel= new JLabel(title);
            titleLabel.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
            JPanel titlePanel= new JPanel();
            titlePanel.add(titleLabel);
            setBackground(colors[color_id]);
            titlePanel.setBackground(colors[color_id]);
            add(titlePanel,BorderLayout.PAGE_START);
            //  add(centerPanel);
            add(scrollSpecific,BorderLayout.CENTER);
            centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            // example of use
            //   for(int j=0;j<10;j++){
            //     SingleSheduleView ssv=new SingleSheduleView();
            // centerPanel.add(ssv);
            // }
            //## end of example

        }

        protected void update(Shedule shedule,ArrayList<Shedule> sheduleList){
            boolean contains=false;
            SingleSheduleView usingSsv=null;

            for(int i=0;i<centerPanel.getComponentCount();i++){
                SingleSheduleView ssv=(SingleSheduleView)centerPanel.getComponent(i);

                if(ssv.id==shedule.getId()){
                    contains=true;
                    usingSsv=ssv;
                }
            }
            if(!contains){
                centerPanel.add(new SingleSheduleView(shedule));
            }else{
                if(usingSsv!=null){
                    usingSsv.updateAll(shedule);
                }

            }

            /// check for shedule in centerPanel id if not exist create
            /// if exist update text

        }

    } 
    class SingleSheduleView extends JPanel{

        private String activeDays, time, weeklyString, activeString,commandString;
        protected  int id;
        protected JPanel firstRow,secondRow;
        private JButton delete,edit;
        private JCheckBox isWeekly,isActive; 
        private JLabel [] days= new JLabel[7];
        private Shedule shedule;
        private JPanel centerPanel,header;
        protected JLabel timeLabel;
        private   ImageIcon deleteAmbIcon , deleteIcon;
        public SingleSheduleView(Shedule shedule){
            setLayout(new BorderLayout());
            activeDays= shedule.getActiveDays();
            weeklyString=  shedule.getIsWeekly();
            activeDays=  shedule.getActiveDays();
            activeString=  shedule.getIsActive();
            commandString=  shedule.getCommandText();
            centerPanel= new JPanel();
            header= new JPanel();
            id=shedule.getId();
            time=shedule.getTime();

            setBorder(BorderFactory.createLineBorder(Color.blue));
            firstRow= new JPanel();
            firstRow.setLayout (new GridLayout(1,7));
            secondRow= new JPanel();

            deleteAmbIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/delete_amb.png");
            deleteAmbIcon=new ImageIcon(fr.getScaledImage(deleteAmbIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));

            deleteIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/delete.png");
            deleteIcon=new ImageIcon(fr.getScaledImage(deleteIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
            delete = new JButton(deleteIcon);

            ImageIcon editIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/edit.png");
            editIcon=new ImageIcon(fr.getScaledImage(editIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
            edit = new JButton(editIcon);

            isWeekly= new JCheckBox("is Weekly");
            isActive= new JCheckBox("is Active");
            isWeekly.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(e.getStateChange() == ItemEvent.SELECTED){
                            fr.sh.db.updateSingleShedule(commandString,Integer.toString(id),DB.IS_WEEKLY+"true");
                        }else{
                            fr.sh.db.updateSingleShedule(commandString,Integer.toString(id),DB.IS_WEEKLY+"false");
                        }

                    }
                });
            isActive.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(e.getStateChange() == ItemEvent.SELECTED){
                            fr.sh.db.updateSingleShedule(commandString,Integer.toString(id),DB.IS_ACTIVE+"true");
                        }else{
                            fr.sh.db.updateSingleShedule(commandString,Integer.toString(id),DB.IS_ACTIVE+"false");
                        }

                    }
                });
            centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            centerPanel.add(firstRow);
            centerPanel.add(secondRow);
            timeLabel=new JLabel(time);

            Font f = timeLabel.getFont();
            f=f.deriveFont (30f);
            timeLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            header.add(timeLabel);
            add(centerPanel);
            add(header,BorderLayout.PAGE_START);
            for(int i=0;i<7;i++){
                JLabel dayButton=null;
                switch(i){
                    case 0:
                    dayButton= new JLabel("Su",SwingConstants.CENTER);
                    break;
                    case 1:
                    dayButton= new JLabel("Mo",SwingConstants.CENTER);
                    break;
                    case 2:
                    dayButton= new JLabel("Tu",SwingConstants.CENTER);
                    break;
                    case 3:
                    dayButton= new JLabel("We",SwingConstants.CENTER);
                    break;
                    case 4:
                    dayButton= new JLabel("Th",SwingConstants.CENTER);
                    break;
                    case 5:
                    dayButton= new JLabel("Fr",SwingConstants.CENTER);
                    break;
                    case 6:
                    dayButton= new JLabel("Sa",SwingConstants.CENTER);
                    break;    
                    default: 
                    dayButton= new JLabel("Uknown",SwingConstants.CENTER);

                }
                dayButton.setOpaque(true);
                dayButton.setBorder(BorderFactory.createLineBorder(Color.gray));
                days[i]=dayButton;
                firstRow.setBorder(BorderFactory.createLineBorder(Color.gray));
                firstRow.add(dayButton);

                dayButton.setBackground(colors[0]);

            }
            //   secondRow.setLayout(new BorderLayout());
            JPanel centerInsideSecondPanel= new JPanel();
            secondRow.add(delete,SwingConstants.CENTER);
            //    secondRow.add(new JLabel("    "),SwingConstants.CENTER);
            //   secondRow.add(new JLabel("    "),SwingConstants.CENTER);

            centerInsideSecondPanel.add(isActive,SwingConstants.CENTER);
            centerInsideSecondPanel.add(isWeekly,SwingConstants.CENTER);
            secondRow.add(centerInsideSecondPanel,SwingConstants.CENTER);
            // secondRow.add(new JLabel("    "),SwingConstants.CENTER);
            //             secondRow.add(new JLabel("    "),SwingConstants.CENTER);
            // secondRow.add(edit,SwingConstants.CENTER);
            add(edit,BorderLayout.LINE_START);
            add(delete,BorderLayout.LINE_END);
            edit.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        fr.isSheduleModeSelected=false;

                        fr.shv=null;
                        new EditSheduleView(fr,shedule);
                    }
                });
            delete.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){

                        new Thread(){
                            public void run(){
                                try{
                                    if(delete!=null){

                                        delete.setIcon(deleteAmbIcon);
                                        delete.setEnabled(false);
                                    }
                                    sleep(3000);
                                    if(delete!=null){
                                        delete.setIcon(deleteIcon);
                                        delete.setEnabled(true);
                                    }}catch(Exception e){}
                            }

                        }.start();

                        fr.sh.db.removeShedule(Integer.toString(id),commandString);
                        repaint();
                        revalidate();
                        centerPanel.repaint();
                        centerPanel.revalidate();
                    }
                });

            updateAll(shedule);

        }

        private void updateAll(Shedule shedule){
            updateAll(shedule.getActiveDays(), shedule.getIsWeekly(), shedule.getIsActive(), shedule.getTime());
            repaint();
            revalidate();
            centerPanel.repaint();
            centerPanel.revalidate();
        }

        private void updateAll(String activeDays,String weeklyString,String activeString,String time){
            updateDaysEnable(activeDays);
            updateWeekly(weeklyString);
            updatActive(activeString);
            updateTime(time);
        }

        private void updateAll(){
            updateDaysEnable();
            updateWeekly();
            updatActive();
            updateTime();
        }

        protected void updateDaysEnable(String activeDays){
            this.activeDays=activeDays;
            updateDaysEnable();
        }

        protected void updateDaysEnable(){

            if(activeDays.contains(Integer.toString( Calendar.SUNDAY)+" on")){
                days[0].setBackground(colors[1]);

            } else if(activeDays.contains(Integer.toString( Calendar.SUNDAY)+" off")){
                days[0].setBackground(colors[2]);

            } else {
                days[0].setBackground(colors[0]);

            }

            if(activeDays.contains(Integer.toString( Calendar.MONDAY)+" on")){
                days[1].setBackground(colors[1]);

            } else if(activeDays.contains(Integer.toString( Calendar.MONDAY)+" off")){
                days[1].setBackground(colors[2]);

            } else {
                days[1].setBackground(colors[0]);

            }

            if(activeDays.contains(Integer.toString( Calendar.TUESDAY)+" on")){
                days[2].setBackground(colors[1]);

            } else if(activeDays.contains(Integer.toString( Calendar.TUESDAY)+" off")){
                days[2].setBackground(colors[2]);

            } else {
                days[2].setBackground(colors[0]);

            }

            if(activeDays.contains(Integer.toString( Calendar.WEDNESDAY)+" on")){
                days[3].setBackground(colors[1]);

            } else if(activeDays.contains(Integer.toString( Calendar.WEDNESDAY)+" off")){
                days[3].setBackground(colors[2]);

            } else {
                days[3].setBackground(colors[0]);

            }

            if(activeDays.contains(Integer.toString( Calendar.THURSDAY)+" on")){
                days[4].setBackground(colors[1]);

            } else if(activeDays.contains(Integer.toString( Calendar.THURSDAY)+" off")){
                days[4].setBackground(colors[2]);

            } else {
                days[4].setBackground(colors[0]);

            }

            if(activeDays.contains(Integer.toString( Calendar.FRIDAY)+" on")){
                days[5].setBackground(colors[1]);

            } else if(activeDays.contains(Integer.toString( Calendar.FRIDAY)+" off")){
                days[5].setBackground(colors[2]);

            } else {
                days[5].setBackground(colors[0]);

            }
            if(activeDays.contains(Integer.toString( Calendar.SATURDAY)+" on")){
                days[6].setBackground(colors[1]);

            } else if(activeDays.contains(Integer.toString( Calendar.SATURDAY)+" off")){
                days[6].setBackground(colors[2]);

            } else {
                days[6].setBackground(colors[0]);

            }

            System.out.println(weeklyString);
            //setBackground(usingColor);
            //centerPanel.setBackground(usingColor);
            //firstRow.setBackground(usingColor);
            //secondRow.setBackground(usingColor);

        }

        protected void updateTime(String time){
            this.time=time;
            updateWeekly();
        }

        protected void updateTime(){
            timeLabel.setText(time);
        }

        protected void updateWeekly(String weeklyString){

            this.weeklyString=weeklyString;
            updateWeekly();
        }

        protected void updateWeekly(){
            if(weeklyString.equalsIgnoreCase("true")|| weeklyString.equalsIgnoreCase("enable")){
                isWeekly.setSelected(true);
            }else  if(weeklyString.equalsIgnoreCase("false")|| weeklyString.equalsIgnoreCase("disable")){
                isWeekly.setSelected(false);
            }
        }

        protected void updatActive(String activeString){

            this.activeString=activeString;
            updatActive();
        }

        protected void updatActive(){

            if(activeString.equalsIgnoreCase("true")|| activeString.equalsIgnoreCase("enable")){
                isActive.setSelected(true);
            }else  if(activeString.equalsIgnoreCase("false")|| activeString.equalsIgnoreCase("disable")){
                isActive.setSelected(false);
            }
        }

    }

}
