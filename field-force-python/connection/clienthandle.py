def handle_gamemode(packet):
    gamemode = packet.read_int()
    timelimit = packet.read_int()
    own_id = packet.read_int()
    skill1 = -1
    skill2 = -1
    if gamemode == 1:
        skill1 = packet.read_int()
        skill2 = packet.read_int()
    packet.read_config()