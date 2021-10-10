package reza.client;

public class Utility {
    public void displayHelp(){
        System.out.println();
        System.out.println("Usage: java reza.client.StoreFetchApp [option] [source] [server-address : optional] [encoding-method : optional]");
        System.out.println("\toption: Available options: [store | fetch | ls]");
        System.out.println("\tsource: The file that needs to be sent or fetch. File must be provided with extension.");
        System.out.println("\tserver-address: provide server address and port. (optional) [default: localhost:3000]");
        System.out.println("\tencoding-method: values: base64, character (optional) [default: base64]");
        System.out.println("\t\tExpected syntax: <address>:<port>");
        System.out.println("\tIf Server address is not provided it connects to the default IP address and PORT.");
        System.out.println("\t\tDefault-server-address: 127.0.0.1:3000");
    }

    public void displayHelp(String message){
        System.out.println(message);
        System.out.println("Usage: java reza.client.StoreFetchApp [option] [source] [server-address : optional] [encoding-method : optional]");
        System.out.println("\toption: Available options: [store | fetch | ls]");
        System.out.println("\tsource: The file that needs to be sent or fetch. File must be provided with extension.");
        System.out.println("\tserver-address: provide server address and port. (optional) [default: localhost:3000]");
        System.out.println("\tencoding-method: values: base64, character (optional) [default: base64]");
        System.out.println("\t\tExpected syntax: <address>:<port>");
        System.out.println("\tIf Server address is not provided it connects to the default IP address and PORT.");
        System.out.println("\t\tDefault-server-address: 127.0.0.1:3000");
    }
}
