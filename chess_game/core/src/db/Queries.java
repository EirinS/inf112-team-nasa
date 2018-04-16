package db;

class Queries {

    private static String tableName = "public.highscores";

    static String listPlayers() {
        return "SELECT * FROM " + tableName + " ORDER BY rating DESC";
    }

    static String registerPlayer(Player player) {
        return String.format(
                "INSERT INTO %s (name, rating, wins, losses, draws) VALUES ('%s', '%d', '%d', '%d', '%d')",
                tableName,
                player.getName(),
                player.getRating(),
                player.getWins(),
                player.getLosses(),
                player.getDraws()
        );
    }

    static String getPlayer(String playerName) {
        return String.format(
                "SELECT * FROM %s WHERE name='%s'",
                tableName,
                playerName
        );
    }

    static String updatePlayer(Player player, int rating, int win_lose_draw) {
        int wins = player.getWins() + (win_lose_draw == 1 ? 1 : 0);
        int losses = player.getLosses() + (win_lose_draw == 2 ? 1 : 0);
        int draws = player.getDraws() + (win_lose_draw == 3 ? 1 : 0);
        return String.format(
                "UPDATE %s SET rating='%d', wins='%d', losses='%d', draws='%d' WHERE name='%s'",
                tableName,
                rating,
                wins,
                losses,
                draws,
                player.getName()
        );
    }
}
