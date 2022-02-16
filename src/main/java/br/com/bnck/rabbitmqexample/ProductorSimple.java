package br.com.bnck.rabbitmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * Criado utilizando IntelliJ IDEA.
 * Projeto: rabbitmq-example
 * Usuário: Thiago Bianeck (Bianeck)
 * Data: 15/02/2022
 * Hora: 21:45
 */
public class ProductorSimple {

    public static void main(String[] args) throws IOException, TimeoutException {
        final String message = "Hello everybody!";

        // Abrir Conexão AMQ e estabelecer canal
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (   Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel()){

            final String FILA = "primeira-fila";
            channel.queueDeclare(FILA, false, false, false, null);
            channel.basicPublish("", FILA, null, message.getBytes(StandardCharsets.UTF_8));

        }

    }
}
