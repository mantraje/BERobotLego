package Code;



import lejos.pc.comm.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Bluetooth {


    /*public static void main (String args[]) {
        //establish a connection...

        NXTConnector com;
        DataInputStream dataIn;
        DataOutputStream dataOut;

        boolean connected;

        com = new NXTConnector();
        connected = com.connectTo("Thorn","001653161388", NXTCommFactory.BLUETOOTH);
        dataIn = new DataInputStream(com.getInputStream());
        dataOut = new DataOutputStream(com.getOutputStream());

    }/*

    /*public static void main (String args[]) throws NXTCommException {


        NXTComm comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
        NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "Thorn", "001653161388");
        comm.open(nxtInfo);


    }*/


}