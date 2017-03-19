package cs356;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class router0 {																		//client
	public static void main(String	args[]) throws IOException	{

		String serverName = args[0];														//takes the IP of the server
		int	port = Integer.parseInt(args[1]);												//port number of the server
		
		int[] router0cost = new int[] {0,1,3,7};											//array of its own cost to each router
		
		System.out.println("I am Router 0. My cost to each router is:");
		
		for (int i = 0; i < router0cost.length; i++){										//display own cost to each router
			if (router0cost[i] == 99){
				System.out.println("Router: " + i + " Cost: Infinity");
			}
			else{
				System.out.println("Router: " + i + " Cost: " + router0cost[i]);
			}
		}
		System.out.println();
		
		try	{
			Socket client =	new	Socket(serverName, port);

			System.out.println("Connected to " + client.getRemoteSocketAddress());

			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.writeUTF("0" + Arrays.toString(router0cost));								//sends its cost to router 1 as a String
			
			DataInputStream	in = new DataInputStream(client.getInputStream());
			String input = in.readUTF();													//gets input from router 1
			String router1input = input.substring(2, input.length()-1);
			String[] router1array = router1input.split(", ");								//converts it into an array
			
			int[] router1cost = new int[router1array.length];								//converts the values of arrayCost to ints
			for (int i = 0; i < router1array.length; i ++){
				router1cost[i] = Integer.parseInt(router1array[i]);
			}
				
			/*System.out.println("Costs of router " + input.charAt(0) + ":");				//takes the array and prints out each value
			for (int i = 0; i < router1array.length; i++){
				if (router1array[i] == "99"){
					System.out.println("Router: " + i + " Cost: Infinity");
				}
				else{
					System.out.println("Router: " + i + " Cost: " + router1array[i]);
				}
			}
			System.out.println();*/
			
			int[] newCost = router0cost;													//creates a new array for the updated costs
			for (int i = 0; i < router1cost.length; i++){
				if (newCost[input.charAt(0)] + router1cost[i] < router0cost[i]){
					newCost[i] = newCost[input.charAt(0)] + router1cost[i];
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
			
			client.close();																	//closes connection
		}
		
		catch (IOException e){
			e.printStackTrace();
		}
	}
}