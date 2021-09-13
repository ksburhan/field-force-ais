#pragma once
#include <string>

inline std::string playername = "behaviour";
inline int skill1 = 4;
inline int skill2 = 3;

inline char VALID_TARGETS[] = { '1', '2', '3', '4', '-' };

inline bool VERBOSE = false;

inline int GAME_MODE = 0;
inline int TIME_LIMIT = 3;
inline int OWN_PLAYER_ID = 0;
 
inline int HP = 150;
inline int SHIELD = 50;
 
inline int ATTACK_DAMAGE = 20;
inline int WALK_IN_PLAYER_DAMAGE = 20;
inline int PLAYER_WALKED_INTO_DAMAGE = 10;
 
inline int FIRE_DURATION_ON_MAP = 20;
inline int ON_FIRE_EFFECT_DURATION = 3;
inline int ON_FIRE_DAMAGE = 5;
 
inline int WALL_HP = 20;
inline int WALK_IN_WALL_DAMAGE = 20;
inline int WALL_TAKE_DAMAGE = 10;