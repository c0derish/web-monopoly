package webmonopoly.backend.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import webmonopoly.backend.boardmanagement.GameStarter;
import webmonopoly.backend.model.*;
import webmonopoly.backend.boardmanagement.MonopolyBoardServer;
import webmonopoly.backend.gamemanagement.MonopolyGame;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class MonopolyRestController {

    private HashMap<String, Integer> runningGameCodes;
    private HashMap<String, MonopolyGame> runningGames;
    private HashMap<String, MonopolyBoardServer> runningGameServers;
    private HashMap<String, Integer> bufferedPlayers;

    private static final int startingPortNumber = 6950;
    private static final String gameCodeAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @RequestMapping(value = "/board_request", method = GET)
    public @ResponseBody BoardResponse boardRequest() {
        // Generate new game code and port number
        int portNumber = startingPortNumber;
        while (runningGameCodes.containsValue(portNumber)) {
            portNumber++;
        }
        char[] gcAlphabet = gameCodeAlphabet.toCharArray();
        StringBuilder gameCodeBuilder = new StringBuilder();
        Random gcGen = new Random();
        for (int i = 0; i < 5; i++) {
            gameCodeBuilder.append(gcAlphabet[gcGen.nextInt(gcAlphabet.length)]);
        }
        while (runningGameCodes.containsKey(gameCodeBuilder.toString())) {
            gameCodeBuilder = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                gameCodeBuilder.append(gcAlphabet[gcGen.nextInt(gcAlphabet.length)]);
            }
        }
        String gameCode = gameCodeBuilder.toString();

        BoardResponse newBoard = new BoardResponse(gameCode, portNumber);

        // Start a new BoardServer at this port number
        MonopolyBoardServer mbs;
        try {
            mbs = new MonopolyBoardServer(gameCode, portNumber, () -> {
                runningGames.put(gameCode, new MonopolyGame(bufferedPlayers.get(gameCode),
                        jsonObject -> {
                            try {
                                runningGameServers.get(gameCode).sendJson(jsonObject);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }));
                bufferedPlayers.remove(gameCode);
                try {
                    runningGameServers.get(gameCode).sendJson(runningGames.get(gameCode).getBoardState());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Return game code and port number as BoardResponse obj
        runningGameCodes.put(gameCode, portNumber);
        runningGameServers.put(gameCode, mbs);
        bufferedPlayers.put(gameCode, 0);
        return newBoard;
    }

    @RequestMapping(value = "/connect_to_game/{gameCode}", method = GET)
    public @ResponseBody JoiningPlayerResponse joiningPlayerRequest(@PathVariable("gameCode") String gameCode) {
        int playerID = bufferedPlayers.get(gameCode);
        bufferedPlayers.put(gameCode, playerID + 1);

        return new JoiningPlayerResponse(playerID, MonopolyGame.playerDescriptors[playerID]);
    }

    @RequestMapping(value = "/game/{gameCode}/{playerID}", method = GET)
    public @ResponseBody PlayerState playerStateRequest(
            @PathVariable("gameCode") String gameCode, @PathVariable("playerID") Integer playerID) {
        try {
            MonopolyGame game = runningGames.get(gameCode);
            game.lockers[playerID].block();
            return game.exportPlayer(playerID);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @RequestMapping(value = "/game/{gameCode}/{playerID}/send_money", method = POST)
    public @ResponseBody ActionResponse sendMoney(
            @PathVariable("gameCode") String gameCode, @PathVariable("playerID") Integer playerID,
            @RequestBody SendMoneyRequest request) {
        // TODO: Implement
        return null;
    }

    @RequestMapping(value = "/game/<gameCode>/<playerID>/give_property", method = POST)
    public @ResponseBody ActionResponse giveProperty(
            @PathVariable("gameCode") String gameCode, @PathVariable("playerID") Integer playerID,
            @RequestBody GivePropertyRequest request) {
        // TODO: Implement
        return null;
    }

    @RequestMapping(value = "/game/<gameCode>/<playerID>/build", method = POST)
    public @ResponseBody ActionResponse build(
            @PathVariable("gameCode") String gameCode, @PathVariable("playerID") Integer playerID,
            @RequestBody PropertyRequest request) {
        // TODO: Implement
        return null;
    }

    @RequestMapping(value = "/game/<gameCode>/<playerID>/demolish", method = POST)
    public @ResponseBody ActionResponse demolish(
            @PathVariable("gameCode") String gameCode, @PathVariable("playerID") Integer playerID,
            @RequestBody PropertyRequest request) {
        // TODO: Implement
        return null;
    }

    @RequestMapping(value = "/game/<gameCode>/<playerID>/morgage", method = POST)
    public @ResponseBody ActionResponse morgage(
            @PathVariable("gameCode") String gameCode, @PathVariable("playerID") Integer playerID,
            @RequestBody PropertyRequest request) {
        // TODO: Implement
        return null;
    }

    @RequestMapping(value = "/game/<gameCode>/<playerID>/unmorgage", method = POST)
    public @ResponseBody ActionResponse unmorgage(
            @PathVariable("gameCode") String gameCode, @PathVariable("playerID") Integer playerID,
            @RequestBody PropertyRequest request) {
        // TODO: Implement
        return null;
    }

    @RequestMapping(value = "/game/<gameCode>/<playerID>/action", method = POST)
    public @ResponseBody ActionResponse action(
            @PathVariable("gameCode") String gameCode, @PathVariable("playerID") Integer playerID,
            @RequestBody ActionButtonRequest request) {
        // TODO: Implement
        // If "Roll Dice" then release the playerState now
        return null;
    }
}
