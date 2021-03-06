package cz.pasekj.pia.fiveinarow.game.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.pasekj.pia.fiveinarow.data.entity.GameEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserInGameEntity;
import cz.pasekj.pia.fiveinarow.data.repository.GameRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserInGameRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.game.PlayerColor;
import cz.pasekj.pia.fiveinarow.game.services.NewGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * New game service implementation
 */
@Service
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class NewGameServiceImpl implements NewGameService {
    /** UserEntity DAO */
    private final UserRepository userRepository;
    /** GameEntity DAO */
    private final GameRepository gameRepository;
    /** UserInGame DAO */
    private final UserInGameRepository userInGameRepository;

    @Override
    public void createGame(String username1, String username2, int width, int height) {

        UUID newGameUUID = UUID.randomUUID();
        UserEntity user1 = userRepository.findByUsername(username1);
        UserEntity user2 = userRepository.findByUsername(username2);

        if (user1 == null || user2 == null) return;

        String whitePlayer;
        String blackPlayer;

        if(Math.random() > 0.5) {
            whitePlayer = user1.getEmail();
            blackPlayer = user2.getEmail();
        } else {
            whitePlayer = user2.getEmail();
            blackPlayer = user1.getEmail();
        }

        PlayerColor[][] board = new PlayerColor[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = PlayerColor.EMPTY;
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        GameEntity newGame;
        try {
            newGame = new GameEntity(
                    newGameUUID.toString(),
                    whitePlayer,
                    blackPlayer,
                    mapper.writeValueAsString(board)
                    );
        } catch (JsonProcessingException e) {
            return;
        }

        gameRepository.save(newGame);
        userInGameRepository.save(new UserInGameEntity(user1.getEmail(), newGameUUID.toString()));
        userInGameRepository.save(new UserInGameEntity(user2.getEmail(), newGameUUID.toString()));
    }

}
