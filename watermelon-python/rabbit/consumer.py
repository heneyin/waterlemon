import pika

#user_pwd = pika.PlainCredentials('user', 'pass')

connection = pika.BlockingConnection(pika.ConnectionParameters(
    host='localhost', port=5672))
channel = connection.channel()

#channel.queue_declare(queue='qqqqq')

def callback(ch, method, properties, body):
    print(" [x] Received %r" % body)

channel.basic_consume("system-diagnosis-log", callback, consumer_tag="tcccctt")
print(' [*] Waiting for messages. To exit press CTRL+C')
channel.start_consuming()
