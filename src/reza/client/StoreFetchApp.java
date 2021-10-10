package reza.client;

import java.io.*;
import java.net.Socket;

public class StoreFetchApp {
    private static String PORT_SEPARATOR = ":";
    private static String IP = "127.0.0.1"; //default IP
    private static int PORT = 3000; //default PORT
    private static String ENCODING_METHOD = "base64"; //default encoding method
    private static String COMMAND_SEPARATOR = "-";
    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;


    public static void main(String[] args) {
        String encodedString;
        //create an instance of AppEncoder class
        AppEncoder encoder = new AppEncoder();
        //create an instance of AppDecoder
        AppDecoder decoder = new AppDecoder();
        //create an instance of Utility class to view help text
        Utility utility = new Utility();

        try{
            String command = args[0];

            switch (command){
                case "store":
                    File file = new File(args[1]);
                    if (args.length > 2){
                        String[] serverInfo = args[2].split(PORT_SEPARATOR);
                        //if server address is provided use that otherwise use the default IP and Port to connect
                        setIP(serverInfo[0]);
                        if (serverInfo.length > 1) setPORT(Integer.parseInt(serverInfo[1]));
                        //determine what encoding method to use
                        if(args.length > 3) setEncodingMethod(args[3]);
                    }

                    //read and encode the file
                    encodedString = encoder.encode(file, getEncodingMethod());
                    //check if the output string is empty or not
                    if (encodedString.equals("")) {
                        utility.displayHelp("Error Encoding File...!!!");
                        return;
                    }

                    //create connection with the server
                    if (!connect()) {
                        utility.displayHelp();
                        return;
                    }
                    //construct the request string
                    String request = command + COMMAND_SEPARATOR + encodedString;
                    //send the request string to the server
                    write(request);
                    //read response from the server
                    getResponse();
                    break;

                case "fetch":
                    //get filename
                    String fileName = args[1];
                    if(args.length > 2){
                        //if ip is provided use that otherwise use the default ip
                        String[] serverAddress = args[2].split(PORT_SEPARATOR);
                        setIP(serverAddress[0]);
                        //if port is provided use that otherwise use the default port
                        if (serverAddress.length > 1) setPORT(Integer.parseInt(serverAddress[1]));
                    }

                    //connect to the server
                    if (!connect()) {
                        utility.displayHelp();
                        return;
                    }
                    //send request
                    String fetchRequest = command + COMMAND_SEPARATOR + fileName;
                    write(fetchRequest);
                    //read response
                    String response = readResponse();
                    if (response.equals("")) return;
                    //decode and save the file
                    boolean decoded = decoder.decode(response);
                    String msg = "File saved successfully...";
                    if (!decoded){
                        msg = "Failed to decode and save file..!!";
                        System.out.println(msg);
                        write(msg);
                        return;
                    }
                    System.out.println(msg);
                    write(msg);
                    break;

                case "ls":
                    //get directory
                    String dir = args[1];
                    //choose to use the sever address
                    if(args.length > 2){
                        //if ip is provided use that otherwise use the default ip
                        String[] serverAddress = args[2].split(PORT_SEPARATOR);
                        setIP(serverAddress[0]);
                        //if port is provided use that otherwise use the default port
                        if (serverAddress.length > 1) setPORT(Integer.parseInt(serverAddress[1]));
                    }

                    //connect to the server
                    if (!connect()) {
                        utility.displayHelp();
                        return;
                    }
                    //send request
                    String listRequest = command + COMMAND_SEPARATOR + dir;
                    write(listRequest);
                    //read response
                    getResponse();
                    break;
                default:
                    utility.displayHelp("Invalid command...!!!");

            }

        } catch (ArrayIndexOutOfBoundsException e){
            utility.displayHelp();
        }
    }

    //establish network connection
    public static boolean connect(){
        try{
            socket = new Socket(getIP(), getPORT());
            System.out.println("Connected to " + socket.getLocalAddress() + " at port: " + socket.getPort());
            return true;
        } catch (IOException e){
            System.err.println("Error Connecting to the server...!!!");
            e.printStackTrace();
            return false;

        }
    }

    //write with the output stream
    public static void write(String data){
        try{
            System.out.println("Writing to server...");
            writer = new PrintWriter(socket.getOutputStream());
            writer.write(data);
            //line terminator
            writer.println();
            writer.flush();
        } catch (Exception e){
            System.err.println("Error writing to server...!!!");
            e.printStackTrace();
        }
    }

    //get response from the server
    public static void getResponse(){
        try {
            System.out.println("Reading from server....");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = reader.readLine();
            reader.close();
            System.out.println(message);

        } catch (IOException e){
            System.out.println("Error reading response from server...");
        }
    }

    //read response
    public static String readResponse(){
        String data = null;
        try {
            System.out.println("Reading from server....");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();
            String[] resArray = response.split(COMMAND_SEPARATOR);
            String status = resArray[0];
            data = resArray[1];

            if (status.equals("error")) {
                System.out.println(data);
                data = "";
            }


        } catch (IOException e){
            System.out.println("Error reading response from server...");
        }
        return data;
    }

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        StoreFetchApp.IP = IP;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int PORT) {
        StoreFetchApp.PORT = PORT;
    }

    public static String getEncodingMethod() {
        return ENCODING_METHOD;
    }

    public static void setEncodingMethod(String encodingMethod) {
        ENCODING_METHOD = encodingMethod;
    }
}
