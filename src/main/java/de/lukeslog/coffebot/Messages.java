package de.lukeslog.coffebot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by lukas on 11.11.14.
 */
public class Messages
{
    static String[] startingmsg = {"And here we go.",
            "Los gehts.",
            "Wasser: Check. Pulver: Check. Strom: Check. Lets Go.",
            "Arbeit...",
            "Es wird Kaffee gemacht.",
            "ok... let's go.",
            "Everybody was Kaffee Machen... Irgendwas mit Expert Timing."};
    static String[] stopmsg = {"bye",
            "OK, I'm off.",
            "Shutting down",
            "This is the end, beautiful friend / This is the end, my only friend, the end",
            "Bye bye",
            "I'l be back. Pew. Pew. Alien Spaceship."};
    static String[] donemsg = {"I think your coffee is done now",
            "Ich koche hier seit 5 Minuten Kaffee, ich glaube, der ist fertig.",
            "Heißer, schwarzer Kaffee, Junge. Jetzt echt mal.",
            "olol. Kaffee. Ist. Fluktuation drölf",
            "5 Minuten sind genug. Du kannst deinen Kaffee abholen.",
            "Hello. Over Here. Waving. Got Coffee.",
            "Coffee: http://open.spotify.com/track/4MLpimiRmgeTHj5WJMghyg",
            "Hammer time! und mit Hammer meine ich Kaffee.",
            "My Coffee brings al the boys to the kitchen.",
            "Du hast das Spiel verloren. Aber es gibt Kaffee!",
            "Ich hab einen Kaffee, Ich hab, ich hab einen Kaffee."};
    static String[] longmsg ={"Ey, ich bin 15 minuten am Laufen, ich ruf hier nicht zum Scheiß an",
            "Duuuuuuuuuuude",
            "Ja WAS denn...",
            "Das grenzt schon an Kaffeeverschwendung hier...",
            "Ich hab immer noch Kaffee hier stehen... der wird nicht besser."};
    static String[] toolongmsg = {"40 minuten alter Kaffee ist nicht gut. Ich schalte mich mal lieber aus",
            "Warum bin ich eigentlich noch an, das ergiebt keinen Sinn!",
            "Ich glaube ich wurde vergessen. Uncool.",
            "If you leave me here you'll take away the biggest coffee of me\n" +
            "Ooh ooh ooh no baby please don't go",
            "I'm out. Thanks for forgetting me."};
    static String []  wantsomecoffee = {"Hey, do you feel like getting a coffee, maybe, sometime.",
            "You haven't had your caffeine in a while. I'm here. Waiting.",
            "If you feel like a coffee, you know where I am.",
            "I am bored. Make coffee.",
            "What do you want? Coffee! When do you want it? Now!",
            "@Allibert91 hat das Spiel verloren.",
            "@Dr_Ewes84 hat das Spiel verloren.",
            "Ich habe das Spiel verloren."};

    public static void parseForMessages()
    {
        try
        {
            URL oracle = new URL("http://www.github.com");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;
            String filetext = "";
            while ((inputLine = in.readLine()) != null)
            {
                //System.out.println(inputLine);
                //System.out.println();
                filetext = filetext + "\n" + inputLine;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
