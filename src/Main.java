public class Main {
    public static void main(String[] args) {

        System.out.println("0: Disable colors  1: Enable colors");
        SystemHelper.setColorEnabled(SystemHelper.scanIntRange(0, 1) == 1);

        SystemHelper.println("   WELCOME TO BLUEJACK", SystemHelper.ANSI_BLUE_BOLD);

        Game.init();

        System.out.println("Enter player name");
        Game.user.name = SystemHelper.scanString();

        Game.gameLoop();
    }
}
