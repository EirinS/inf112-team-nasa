package db;

import register.Player;

class Queries {

    private static String tableName = "public.highscores";

    static String listPlayers() {
        return "SELECT * FROM " + tableName;
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

    static String updatePlayer(String playerName, int rating, int wins, int losses, int draws) {
        return String.format(
                "UPDATE %s SET rating='%d', wins='%d', losses='%d', draws='%d' WHERE name='%s'",
                tableName,
                rating,
                wins,
                losses,
                draws,
                playerName
        );
    }
}
