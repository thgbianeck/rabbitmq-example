package br.com.bnck.rabbitmqexample;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Criado utilizando IntelliJ IDEA.
 * Projeto: rabbitmq-example
 * Usuário: Thiago Bianeck (Bianeck)
 * Data: 15/02/2022
 * Hora: 23:24
 */
public class ProductorEventos {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        final String EXCHANGE = "eventos";

        ConnectionFactory connectionFactory = new ConnectionFactory();
        // Abrir conexão AMQ e estabelecer um canal
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {

            // Criar fanout exchange eventos
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.FANOUT);
            // Enviar mensagens para o fanout exchange eventos
            int count = 1;
            while (true) {
                final String message = "Evento " + count;
                System.out.println("Produzindo mensagem: " + message);
                channel.basicPublish(EXCHANGE, "", null, message.getBytes(StandardCharsets.UTF_8));
                Thread.sleep(1000);
                count++;
            }

        }

    }
}
