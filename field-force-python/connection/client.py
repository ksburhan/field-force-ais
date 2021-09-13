import socket


class Client:
    # creates socket
    def __init__(self, ip, port):
        self.client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server = ip
        self.port = port
        self.addr = (self.server, self.port)

    # connects to server
    def connect(self):
        self.client.connect(self.addr)