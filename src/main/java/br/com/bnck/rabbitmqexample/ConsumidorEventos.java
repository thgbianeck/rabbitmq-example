package br.com.bnck.rabbitmqexample;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Criado utilizando IntelliJ IDEA.
 * Projeto: rabbitmq-example
 * Usuário: Thiago Bianeck (Bianeck)
 * Data: 15/02/2022
 * Hora: 23:33
 */
public class ConsumidorEventos {
    public static void main(String[] args) throws IOException, TimeoutException {
        final String EXCHANGE = "eventos";
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //Abrir conexão
        Connection connection = connectionFactory.newConnection();
        //Estabelecer um canal
        Channel channel = connection.createChannel();
        //Declarar exchange eventos
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.FANOUT);
        //Criar fila e associar a exchange eventos
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE, "");
        //Criar subscription a uma fila associada ao exchange "eventos
        channel.basicConsume(queueName, true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensagem recebida: " + messageBody);

                },
                (consumerTag) -> {
                    System.out.println("Consumidor" + consumerTag + " cancelado");
                }
        );

    }
}
