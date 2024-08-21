import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.algorithm.ShortestPath;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.weapon.Weapon;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final String SERVER_URL = "https://cf-server.jsclub.dev";
    private static final String GAME_ID = "181430";
    private static final String PLAYER_NAME = "player1";
    private static long lastCallTime = 0L;

    public Main() {
    }

    public static String randomMove() {
        String[] moves = new String[]{"u", "d", "l", "r"};
        return moves[(int)(Math.random() * (double)moves.length)];
    }

    public static void main(String[] args) throws IOException {
            final Hero hero = new Hero("181430", "player1");
            Emitter.Listener onMapUpdate = new Emitter.Listener() {
                public void call(Object... args) {
                    long currentTime = System.currentTimeMillis();
                    if (lastCallTime != 0L) {
                        long timeDifference = currentTime - lastCallTime;
                        System.out.println("Time between calls: " + timeDifference + " ms");
                    }

                    lastCallTime = currentTime;

                    try {
                        GameMap gameMap = hero.getGameMap();
                        gameMap.updateOnUpdateMap(args[0]);
                        System.out.println(gameMap);
                        Weapon gun = (Weapon)gameMap.getAllGun().get(0);
                        System.out.println(gun);
                        System.out.println(gameMap.getCurrentPlayer());
                        String path = ShortestPath.getShortestPath(gameMap, List.of(), gameMap.getCurrentPlayer(), gun, false);
                        System.out.println(path);
                        hero.move(path);
                    } catch (IOException var7) {
                        IOException e = var7;
                        throw new RuntimeException(e);
                    }
                }
            };

            hero.setOnMapUpdate(onMapUpdate);
            hero.start("https://cf-server.jsclub.dev");
        }
}