package cs356;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class router1 extends Thread {																	//server 1
	private ServerSocket serverSocket;
	
	public router1 (int port) throws IOException {
		serverSocket = new ServerSocket(port);
		System.out.println("I am Router 1");
		System.out.println();
		serverSocket.setSoTimeout(60000);
	}

	public void run(){
		int[] router1cost = new int[] {1,0,1,99};														//array of its own cost to each router
		while(true){																					//will keep on running until terminated
			try {
				Socket server = serverSocket.accept();
				System.out.println("Received message from " + server.getRemoteSocketAddress());
				
				DataOutputStream out = new DataOutputStream(server.getOutputStream()); 					//sends its cost to router 0 as a String
				out.writeUTF("1" + Arrays.toString(router1cost));
				
				DataInputStream in = new DataInputStream(server.getInputStream());						//gets input from router 0
				String input = in.readUTF();
				String router0input = input.substring(2, input.length()-1);
				String[] router0array = router0input.split(", ");										//converts it into an array
				
				int[] router0cost = new int[router0array.length];								//converts the values of arrayCost to ints
				for (int i = 0; i < router0array.length; i ++){
					router1cost[i] = Integer.parseInt(router0array[i]);
				}
				
				/*System.out.println("Costs of router " + input.charAt(0) + ":");						//takes the array and prints out each value
				for (int i = 0; i < router0array.length; i++){
					if (router0array[i] == "99"){
						System.out.println("Router: " + i + " Cost: Infinity");
					}
					else{
						System.out.println("Router: " + i + " Cost: " + router0array[i]);
					}
				}
				System.out.println();*/
				
				int[] newCost = router1cost;													//creates a new array for the updated costs
				for (int i = 0; i < router0cost.length; i++){
					if (newCost[input.charAt(0)] + router0cost[i] < router0cost[i]){
						newCost[i] = newCost[input.charAt(0)] + router0cost[i];
					}
					else{
						continue;
					}
				}
				
				System.out.println("New updated costs:");										//takes the updated array and prints out each value
				for (int i = 0; i < newCost.length; i++){
					if (newCost[i] == 99){
						System.out.println("Router: " + i + " Cost: Infinity");
					}
					else{
						System.out.println("Router: " + i + " Cost: " + newCost[i]);
					}
				}
				System.out.println();
			} 
			catch(SocketTimeoutException s){															//times out if nothing has been received in 1 minute
				System.out.println("Socket timed out");
				break;
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
		
	public static void main(String args[]) throws IOException{ 
		int port = Integer.parseInt(args[0]);
		
		try {
			Thread t = new router1(port); 
			t.start();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}