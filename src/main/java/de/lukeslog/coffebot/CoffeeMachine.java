package de.lukeslog.coffebot;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lukas on 04.07.14.
 */
public class CoffeeMachine implements Runnable
{


    private double energycouter=0;
    private double counteratlastsecond=-1;
    private int currentrunntime=0;
    private int currentdowntime=0;
    private double energythisuptime=0;
    private static final int STATE_ON = 1;
    private static final int STATE_OFF = 0;
    private int state = STATE_OFF;
    private int laststate = STATE_OFF;
    public static final double PRICE_PER_KILOWAT_HOUR = 26.76;

    Thread d;

    public CoffeeMachine()
    {
        //MyTwitter.tweet("Stating");
        d = new Thread(this);
        d.run();
    }

    @Override
    public void run()
    {
        while(true)
        {
            Messages.parseForMessages();
            DateTime dt = new DateTime();
            System.out.println(dt.getHourOfDay()+":"+dt.getMinuteOfHour());
            if(counteratlastsecond==-1)
                try {
                    System.out.println("counterlastsecond is -1 so we update to the current value");
                    counteratlastsecond = getCurrentValue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else
            {
                try
                {
                    double value = getCurrentValue();
                    if(value!=counteratlastsecond)
                    {
                        double change = value-counteratlastsecond;
                        if(change<0)
                        {
                            change=0;
                        }
                        energycouter=energycouter+change;
                        counteratlastsecond = value;
                        System.out.println("the change is: "+change);
                        if(change>0.2)
                        {
                            currentrunntime++;
                            currentdowntime=0;
                            energythisuptime=energythisuptime+change;
                            laststate=state;
                            state=STATE_ON;

                        }
                        else
                        {
                            currentrunntime=0;
                            currentdowntime++;
                            laststate=state;
                            state=STATE_OFF;
                        }
                    }
                    else
                    {
                        laststate=state;
                        currentrunntime=0;
                        currentdowntime++;
                        state=STATE_OFF;
                    }
                    System.out.println(value);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            checkForActions();
            if(laststate==STATE_ON && state == STATE_OFF)
            {
                energythisuptime=0;
            }
            sleep();
        }

    }

    private void checkForActions() {
        System.out.println("current run time = "+currentrunntime);
        System.out.println("current down time = "+currentdowntime);
        DateTime dt = new DateTime();
        String tweettext="";
        if(laststate==STATE_OFF && state == STATE_ON)
        {
            int high = Messages.startingmsg.length;
            int x = (int) (Math.random() * high);
            //x = x-1;
            tweettext=Messages.startingmsg[x];
            //MyTwitter.tweet(startingmsg[x]+" ("+timeAsHourMinuteString(dt)+")");
        }
        else if(laststate==STATE_ON && state == STATE_OFF)
        {
            int high = Messages.stopmsg.length;
            int x = (int) (Math.random() * high);
            //x = x-1;
            tweettext = Messages.stopmsg[x];
            //MyTwitter.tweet(stopmsg[x]+" ("+timeAsHourMinuteString(dt)+")");
        }
        else if(currentdowntime==1440)
        {
            tweettext = "Seit 24 Stunden aus. Was ist da los?";
            //MyTwitter.tweet("Seit 24 Stunden aus. Was ist da los?"+" ("+timeAsHourMinuteString(dt)+")");
        }
        else if(currentrunntime==5)
        {
            int high = Messages.donemsg.length;
            int x = (int) (Math.random() * high);
            //x = x-1;
            tweettext = "@TVLuke "+Messages.donemsg[x];
            //MyTwitter.tweet("@TVLuke "+donemsg[x]+" ("+timeAsHourMinuteString(dt)+")");
        }
        else if(currentrunntime==15)
        {
            int high = Messages.longmsg.length;
            int x = (int) (Math.random() * high);
            //x = x-1;
            tweettext = "@TVLuke "+Messages.longmsg[x];
            //MyTwitter.tweet("@TVLuke "+longmsg[x]+" ("+timeAsHourMinuteString(dt)+")");
        }
        else if(currentrunntime==40)
        {
            int high = Messages.toolongmsg.length;
            int x = (int) (Math.random() * high);
            //x = x-1;
            tweettext = "@TVLuke "+Messages.toolongmsg[x];
            //MyTwitter.tweet("@TVLuke "+toolongmsg[x]+" ("+timeAsHourMinuteString(dt)+")");
            turnOff();
        }
        else if(dt.getHourOfDay()==0 && dt.getMinuteOfHour()==0)
        {
            DateTime dt2 = dt.minusDays(1);
            if(energycouter<0) {
                tweettext = "Heute, am " + dt2.getDayOfMonth() + ". " + dt2.getMonthOfYear() + ", habe ich " + energycouter + " Wh (" + energyInCent(energycouter) + " ct) verbraucht.";
            }
            energycouter=0;
        }
        else if(currentdowntime>60 && dt.getHourOfDay()>6 && dt.getHourOfDay()<16)
        {
            int x = (int) (Math.random() * 10000);
            if(x == 1)
            {
                int high = Messages.wantsomecoffee.length;
                x= (int) (Math.random() * high);
                if(Messages.wantsomecoffee[x].startsWith("@"))
                {
                    tweettext = Messages.wantsomecoffee[x]  ;
                }
                else
                {
                    tweettext = "@TVLuke " + Messages.wantsomecoffee[x];
                }
            }
        }
        if(!tweettext.equals(""))
        {
            tweettext = tweettext.replace("[energythisuptime]", ""+energythisuptime+"");
            MyTwitter.tweet(tweettext + " (" + timeAsHourMinuteString(dt) + ")");
        }
    }

    private double energyInCent(double energy)
    {
        double ct=0.0;
        if(energy>=0)
        {
            ct = (energy / 1000.0) * PRICE_PER_KILOWAT_HOUR;
            if (ct > 0.01)
            {
                ct = round(ct, 2);
            }
        }
        return ct;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private String timeAsHourMinuteString(DateTime dt)
    {
        int m = dt.getMinuteOfHour();
        int h = dt.getHourOfDay();
        return String.format("%02d", h)+":"+String.format("%02d", m);
    }

    private void turnOff()
    {
        try
        {
            URL oracle = new URL("http://192.168.1.242/control?cmd=set_state_actuator&number=3&function=2&page=control.html");
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sleep()
    {
        try
        {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private double getCurrentValue() throws IOException {
            return XMLParser.parseXMLFile();
    }
}
