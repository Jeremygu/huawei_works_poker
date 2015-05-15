package poker;

public class game {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GameClient gameClient = new GameClient(args[0],
				Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]),
				Integer.parseInt(args[4]), "player"+args[4]);
		GameStratege gameStratege = new EasyStrategy();
		gameClient.setStrategy(gameStratege);
		gameClient.runGame();
		
	}

}
