public class Main {
    public static void main(String[] args) {

        //System.out.println("0: Disable colors  1: Enable colors");
        //SystemHelper.setColorEnabled(SystemHelper.scanIntRange(0, 1) == 1);
        SystemHelper.setColorEnabled(true);

        SystemHelper.println("\n   WELCOME TO BLUEJACK", SystemHelper.ANSI_BLUE_BOLD);
        SystemHelper.println("      by Umut Göler\n", SystemHelper.ANSI_WHITE_BOLD);

        Game.init();

        //todo: give option to play: player vs cpu, player vs player, cpu vs cpu

        System.out.println("Enter player name");
        Game.player1.name = SystemHelper.scanString();

        Game.gameLoop();
    }
}
