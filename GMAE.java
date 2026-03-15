import java.util.Scanner;

//contains the main that will run the GMAE

public class GMAE {

    public static void main(String[] args){ 

        Scanner scanner = new Scanner(System.in);

        GuildQuest guildQuest = new GuildQuest();

        Player p1 = Player.choosePlayer(1, guildQuest.getUsers(), scanner);
        Player p2 = Player.choosePlayer(2, guildQuest.getUsers(), scanner);

        //add in mini adventures

        GameEngine start = new GameEngine();
        //start.runAdventure(_, p1, p2)
        
        scanner.close();
    }
}