import pika

user_pwd = pika.PlainCredentials('user', 'pwd')

connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost', credentials=user_pwd))
channel = connection.channel()

routing_key='queue'
#channel.queue_declare(queue=routing_key)

import time
millis = int(round(time.time() * 1000))

body='aaaa'


channel.basic_publish(exchange='',
                      routing_key=routing_key,
                      body=body)
print(" [x] Sent " + body)
connection.close()