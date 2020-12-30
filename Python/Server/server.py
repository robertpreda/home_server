import time
import socket
import threading

HOST = "127.0.0.1"
FORMAT = 'utf-8'
HEADER = 64
PORT = 6969
DISCONNECT_MSG = "bye"

ADDR = (HOST, PORT)

def handle_client(conn, addr):
	print(f"New connection to: {addr}")

	connected = True
	while connected:
		msg_length = conn.recv(HEADER)
		if msg_length:
			print(msg_length.decode(FORMAT))
			msg_length = int(msg_length.decode(FORMAT))
			data = conn.recv(msg_length)
			print(data.decode(FORMAT))
			if data == DISCONNECT_MSG:
				conn.close()




def server():

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	print("Socket created")

	try:
	    s.bind(('', PORT))
	except socket.error as err:
	    print('Bind failed. Error Code : ' .format(err))

	s.listen()
	print("Server is Listening...")
	

	while True:
		conn, addr = s.accept()
		thread = threading.Thread(target=handle_client, args=(conn, addr))
		thread.start()


server_thread = threading.Thread(target=server)

if __name__ == '__main__':
	server_thread.start()

