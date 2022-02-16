package br.com.bnck.rabbitmqexample;

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
 * Hora: 22:56
 */
public class ConsumidorSimple {

    public static void main(String[] args) throws IOException, TimeoutException {

        final String FILA = "primeira-fila";

        // Abrir conexão
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        // estabelecer um canal
        Channel channel = connection.createChannel();
        // declarar a fila "primeira-fila"
        channel.queueDeclare(FILA, false, false, false, null);
        // Criar subscribe para a fila "primeira-fila" usando o comando Basic.consume
        channel.basicConsume(FILA, true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensagem: " + messageBody);
                    System.out.println("Exchange: " + message.getEnvelope().getExchange());
                    System.out.println("Routing key: " + message.getEnvelope().getRoutingKey());
                    System.out.println("Delivery Tag: " + message.getEnvelope().getDeliveryTag());
                },
                (consumerTag) -> {
                    System.out.println("Consumidor" + consumerTag + " cancelado");
                }
        );
    }
}
