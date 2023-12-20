public class Main {
    public static void main(String[] args) {

        /* Prompt the user to enable or disable colors */

        //System.out.println("0: Disable colors  1: Enable colors");
        //SystemHelper.setColorEnabled(SystemHelper.scanIntRange(0, 1) == 1);
        SystemHelper.setColorEnabled(true);

        SystemHelper.println("\n   WELCOME TO BLUEJACK", SystemHelper.ANSI_BLUE_BOLD);
        SystemHelper.println("      by Umut Göler\n", SystemHelper.ANSI_WHITE_BOLD);

        /* Prompt the user to select game mode */
        System.out.println("0: Player VS CPU  1: Player VS Player  2: CPU VS CPU");
        int gameMode = SystemHelper.scanIntRange(0,2);
        switch (gameMode) {
            case 0:
                Game.init(false, true);
                System.out.println("Enter player name");
                Game.player1.setName(SystemHelper.scanString());
                Game.player2.setName("CPU");
                break;
            case 1:
                Game.init(false, false);
                System.out.println("Enter player 1 name");
                Game.player1.setName(SystemHelper.scanString());
                System.out.println("Enter player 2 name");
                Game.player2.setName(SystemHelper.scanString());
                break;
            case 2:
                Game.init(true, true);
                Game.player1.setName("CPU 1");
                Game.player2.setName("CPU 2");
        }

        Game.gameLoop();
    }
}
