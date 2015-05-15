package poker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Formatter;

public class GameClient {

	private InetAddress serverIP;
	private int serverPort;
	private InetAddress myIP;
	private int myPort;
	private int playerID;
	private String playerName;
	
	private Socket clientSocket;
	private OutputStream os;
	private InputStream is;
	private DataOutputStream dos;
	private DataInputStream dis;
	private Formatter fomatter;
	
	private GameStratege myStratege = null;
	
	public GameClient(String serverIPString, int serverPort, String myIPString, int myPort, int playerID, String playerName) {
		
		try {
			this.serverIP = InetAddress.getByName(serverIPString);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.serverPort = serverPort;
		try {
			this.myIP = InetAddress.getByName(myIPString);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.myPort = myPort;
		this.playerID = playerID;
		this.playerName = playerName;
		
		this.fomatter = new Formatter();
		
	}
	
	public void setStrategy(GameStratege gameStratege) {
		this.myStratege = gameStratege;
	}
	
	public void runGame() {
		
		try {
			
			if (this.myStratege == null) return;
			
			/*
			 * 创建socket对象，指定服务器的ip地址和服务器监听的端口号。
			 * 客户端在new的时候，就发出了连接请求，服务器端就会进行处理。
			 * 如果服务器端没有开启服务，那么这时候就会找不到服务器，并同时抛出异常: java.net.ConnectException。
			 */
			clientSocket = new Socket(serverIP, serverPort);
			// 打开输出流
			os = clientSocket.getOutputStream();
			// 打开输入流
			is = clientSocket.getInputStream();
			// 封装输入流
			dis = new DataInputStream(is);
			//封装输出流
			dos = new DataOutputStream(os);
			
			send(regMsg());
			
			String msg = receive();
			while (isGameOver(msg)) {
				
				//出牌策略
				
				msg = receive();
			}
			
			//关闭输入流
			dis.close();
			//关闭输出流
			dos.close();
			//关闭socket对象
			clientSocket.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void send(String msg) {
		try {
			dos.writeBytes(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String receive() {
		
		byte[] bytes = null;
		
		try {
			dis.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new String(bytes);
	}
	
	private String regMsg() {

		return fomatter.format(
				"reg: %d %s \n", this.playerID, this.playerName
				).toString();
		
	}
	
	private boolean isGameOver(String msg) {
		
		if (msg.equals("game-over \n")) return true;
		else return false;
		
	}
}
