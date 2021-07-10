PLAYERS_IN_GAME = []


class GameState:
    def __init__(self, gamefield, players, playerinturn, fires, walls, consumables):
        self.gamefield = gamefield
        self.players = players
        self.playerinturn = playerinturn
        self.fires = fires
        self.walls = walls
        self.consumables = consumables
