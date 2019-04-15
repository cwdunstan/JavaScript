import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
 
public class BlockchainServerRunnable implements Runnable{
 
    private Socket clientSocket;
    private Blockchain blockchain;
 
    public BlockchainServerRunnable(Socket clientSocket, Blockchain blockchain) {
        // implement your code here
    	this.clientSocket = clientSocket;
    	this.blockchain = blockchain;
    }
 
    public void run() {
        // implement your code here
    	try{
    	 BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         PrintWriter outWriter = new PrintWriter(clientSocket.getOutputStream(), true);
		  while(true){  	
		   String input = null;
		   try{
			while((input = inputReader.readLine())!=null) {
						//adding transaction
						if(input.startsWith("tx")) {
							boolean res = blockchain.addTransaction(input);
							if(res) {
								outWriter.println("Accepted");
 
							}else {
								outWriter.println("Rejected");
 
							}
						}
						//Print blockchain
						else if(input.matches("ls")) {
							outWriter.println(blockchain.toString());
 
						}
						//Print blockchain
						else if(input.matches("pb")) {
							outWriter.println(blockchain.toString());
							
						
						}
						//Close connection
						else if(input.matches("cc")) {
							inputReader.close();
							outWriter.flush();
							outWriter.close();
							clientSocket.close();
							System.out.println("closed");
							return;
							
						}
						else {
							outWriter.print("Error\n");
							System.out.println(input);
							outWriter.flush();
					}
				}
		   }catch(Exception f){
				System.out.println("fatal");
		    	return;
		   }
    	}						        
    } catch(IOException e){
    	System.out.println("fatal");
    	return;
    }
    // implement any helper method here if you need any
}
}