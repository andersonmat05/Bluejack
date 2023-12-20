public class Main {
    public static void main(String[] args) {

        //System.out.println("0: Disable colors  1: Enable colors");
        //SystemHelper.setColorEnabled(SystemHelper.scanIntRange(0, 1) == 1);
        SystemHelper.setColorEnabled(true);

        SystemHelper.println("\n   WELCOME TO BLUEJACK", SystemHelper.ANSI_BLUE_BOLD);
        SystemHelper.println("      by Umut Göler\n", SystemHelper.ANSI_WHITE_BOLD);

        System.out.println("0: Player VS CPU  1: Player VS Player  2: CPU VS CPU");
        int gameMode = SystemHelper.scanIntRange(0,2);
        switch (gameMode) {
            case 0:
                Game.init(false, true);
                break;
            case 1:
                Game.init(false, false);
                break;
            case 2:
                Game.init(true, true);
        }

        //System.out.println("Enter player name");
        //Game.player1.name = SystemHelper.scanString();

        Game.gameLoop();
    }
}
