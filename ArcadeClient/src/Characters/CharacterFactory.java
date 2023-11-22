package Characters;

import java.io.BufferedWriter;

public class CharacterFactory {
	public static GameCharacter getCharacter(String _type, int _x, int _y, int _clientId, String _username, BufferedWriter _out) {
		GameCharacter player = null;
		if(_type.equals("Bazzi")) player = new Bazzi(_clientId,_x,_y,_username,_out);
		else if(_type.equals("Dao")) player = new Dao(_clientId,_x,_y,_username,_out);
		else if(_type.equals("Dizini")) player = new Dizini(_clientId,_x,_y,_username,_out);
		else if(_type.equals("Ethi")) player = new Ethi(_clientId,_x,_y,_username,_out);
		else if(_type.equals("Kephi")) player = new Kephi(_clientId,_x,_y,_username,_out);
		else if(_type.equals("Marid")) player = new Marid(_clientId,_x,_y,_username,_out);
		else if(_type.equals("Mos")) player = new Mos(_clientId,_x,_y,_username,_out);
		else if(_type.equals("Uni")) player = new Uni(_clientId,_x,_y,_username,_out);
		return player;
	}
}
