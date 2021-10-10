package reza.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class AppServer {
    int PORT = 3000;
    String COMMAND_SEPARATOR = "-";
    String OPTION_SEPARATOR = " ";
    String ENCODING_METHOD = "base64"; //default encoding method


    public static void main(String[] args) {
        new AppServer().listen();
    }

    public void listen(){

        try{
            //create serversocket
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Listening to port " + PORT);

            while (true){
                //accept connections
                Socket clientSocket = serverSocket.accept();
                //create a new thread with the connection
                Thread thread = new Thread(new ClientHandler(clientSocket));
                //run the thread
                thread.start();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }



    public class ClientHandler implements Runnable{
        BufferedReader reader;
        PrintWriter writer;
        Socket socket;

        String encodedString = "";
        AppDecoder decoder = new AppDecoder();
        AppEncoder encoder = new AppEncoder();

        //constructor
        public ClientHandler(Socket clientSocket){
            socket = clientSocket;
        }

        @Override
        public void run() {
            try{
                //read string from the client
                System.out.println("Reading Data.....");
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String request = reader.readLine();
                //reader.close();
                //separate command and options
                String[] reqBody = request.split(COMMAND_SEPARATOR);
                String command = reqBody[0];
                String options = reqBody[1];

                //determine what to do
                switch (command){
                    case "store":
                        System.out.println("Decoding...");

                        encodedString = options;
                        //decode the string and save local
                        boolean decoded = decoder.decode(encodedString);
                        //check if decoding fails
                        if (!decoded) {
                            sendResponse("Decoding Failed...!!!");
                            return;
                        }
                        //send success response
                        sendResponse("Decoding Successful...");
                    break;

                    case "fetch":
                        String[] optionsArry = options.split(OPTION_SEPARATOR);
                        //get filename
                        File file = new File(optionsArry[0]);
                        //if file doesn't exists send error response
                        if (!file.exists()){
                            sendResponse("error-File/Directory Not Found!!");
                            return;
                        }
                        //if encoding method is provided use that
                        if (optionsArry.length > 1) setENCODING_METHOD(optionsArry[1]);
                        //encode the file
                        String encodedFile = encoder.encode(file, ENCODING_METHOD);
                        //send encoded string
                        sendResponse( "success-" + encodedFile);
                        //get decoding status
                        getResponse();
                        break;

                    case "ls":
                        File dir = new File(options);
                        //check if the directory exists
                        if (!dir.exists()){
                            sendResponse("Directory doesn't exists!!!");
                            return;
                        }
                        //get the list of files and directories
                        String[] list = dir.list();
                        if (list.length < 1){
                            sendResponse("Empty Directory...");
                            return;
                        }

                        //send directory list
                        sendResponse(Arrays.toString(list));
                        break;
                    default:
                        System.out.println("Invalid command provided...");
                        sendResponse("error-Invalid command provided. Expecting store/fetch, got " + command);
                }


            } catch (IOException e){
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }

        }

        public void sendResponse(String message){
            try {
                writer = new PrintWriter(socket.getOutputStream());
                writer.println(message);
                writer.flush();
                System.out.println("Response sent to client...");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void getResponse(){
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = reader.readLine();
                reader.close();
                System.out.println(message);

            } catch (IOException e){
                System.out.println("Error reading response...");
            }
        }
    }


    public void setENCODING_METHOD(String ENCODING_METHOD) {
        this.ENCODING_METHOD = ENCODING_METHOD;
    }
}
