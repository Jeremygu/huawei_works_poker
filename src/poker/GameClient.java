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
			 * ����socket����ָ����������ip��ַ�ͷ����������Ķ˿ںš�
			 * �ͻ�����new��ʱ�򣬾ͷ������������󣬷������˾ͻ���д���
			 * �����������û�п���������ô��ʱ��ͻ��Ҳ�������������ͬʱ�׳��쳣: java.net.ConnectException��
			 */
			clientSocket = new Socket(serverIP, serverPort);
			// �������
			os = clientSocket.getOutputStream();
			// ��������
			is = clientSocket.getInputStream();
			// ��װ������
			dis = new DataInputStream(is);
			//��װ�����
			dos = new DataOutputStream(os);
			
			send(regMsg());
			
			String msg = receive();
			while (isGameOver(msg)) {
				
				//���Ʋ���
				
				msg = receive();
			}
			
			//�ر�������
			dis.close();
			//�ر������
			dos.close();
			//�ر�socket����
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
