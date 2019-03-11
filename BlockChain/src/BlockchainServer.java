import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
 
public class BlockchainServer {
    private Blockchain blockchain;
 
    public BlockchainServer() { blockchain = new Blockchain(); }
 
    // getters and setters
    public void setBlockchain(Blockchain blockchain) { this.blockchain = blockchain; }
    public Blockchain getBlockchain() { return blockchain; }
 
    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        int portNumber = Integer.parseInt(args[0]);
        BlockchainServer bcs = new BlockchainServer();
 
        // TODO: implement your code here.
        
        //Declare and Initialize server
        ServerSocket myServerSocket = null;
        try {
        	myServerSocket = new ServerSocket(portNumber);
        	
			//Accept Connections
        	while(true) {
        		Socket mySocket = myServerSocket.accept();
        		try {
        			//timeout maybe?
        			mySocket.setSoTimeout(1000);
        			bcs.serverHandler(mySocket.getInputStream(),mySocket.getOutputStream());
        		}
                catch (SocketTimeoutException e) {
                	System.out.println("errorsssss");
                }
			}
        }
	    catch (IOException e) {
	    	System.out.print(e);
	    }
    }
 
    public void serverHandler(InputStream clientInputStream, OutputStream clientOutputStream) {
        BufferedReader inputReader = new BufferedReader(
                new InputStreamReader(clientInputStream));
        PrintWriter outWriter = new PrintWriter(clientOutputStream, true);
 
        // TODO: implement your code here.
        class handle implements Runnable{
			
			public void run() {
			    String input = null;
			    while(true) {
		        	try {
						input = inputReader.readLine();
						if(input==null) {
							outWriter.close();
							inputReader.close();
							clientInputStream.close();
							clientOutputStream.close();
							break;
						}
			        	if(input.length()>1) {
							if(input.substring(0,2).matches("tx")) {
								int res = blockchain.addTransaction(input);
								if(res == 1 || res == 2) {
									outWriter.print("Accepted\n");
									outWriter.flush();
								}else {
									outWriter.print("Rejected\n");
									outWriter.flush();
								}
							}
							else if(input.matches("pb")) {
								outWriter.print(blockchain.toString()+"\n");
								outWriter.flush();
							}
 
							else if(input.matches("cc")) {
								outWriter.flush();
								outWriter.close();
								inputReader.close();
								clientInputStream.close();
								clientOutputStream.close();
								break;
							}
							else {
								outWriter.print("Error\n\n");
								outWriter.flush();
							}
						}
						else {
							outWriter.print("Error\n\n");
							outWriter.flush();
						}
					}
		        	
				 catch (IOException e) {
					// TODO Auto-generated catch block
						outWriter.close();
						try {
							inputReader.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							break;
						}
						break;
				}
			    }
			}
        }
        handle myHandle = new handle();
        Thread myThread = new Thread(myHandle);
        myThread.start();
        
 
    }
 
    // implement helper functions here if you need any.
}