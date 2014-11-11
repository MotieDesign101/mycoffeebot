package de.lukeslog.coffebot;

import javax.xml.bind.JAXBContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lukas on 03.07.14.
 */
public class XMLParser {
    //get the values for the coffe machine
    //submit them to the records class
    public static double parseXMLFile() throws IOException {
            return readValueFromURL("http://192.168.1.107/addons/xmlapi/statelist.cgi");
    }

    private static double readValueFromURL(String url) throws IOException {
        URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        String filetext="";
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            //System.out.println();
            filetext=filetext+"\n"+inputLine;
        }
        filetext=filetext.replace(">", ">\n");
        filetext=filetext.replace("/>", "/>\n");
        //System.out.println(filetext);
        String[] lines = filetext.split("\n");
        String result="";
        for(String line : lines)
        {
            //System.out.println(line);

            if(line.contains("BidCos-RF.LEQ0150716:2.ENERGY_COUNTER"))
            {
                //System.out.println(line);
                String[] vv = line.split("'");
                String oldv="";
                for(String v : vv)
                {
                    if(oldv.equals(" value="))
                    {
                        result=v;
                    }
                    oldv=v;
                }
            }

        }
        in.close();
        //System.out.println(result);
        Double value = Double.parseDouble(result);
        return value;
    }

}
