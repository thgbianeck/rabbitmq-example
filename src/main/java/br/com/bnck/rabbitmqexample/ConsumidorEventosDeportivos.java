package br.com.bnck.rabbitmqexample;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Criado utilizando IntelliJ IDEA.
 * Projeto: rabbitmq-example
 * Usuário: Thiago Bianeck (Bianeck)
 * Data: 15/02/2022
 * Hora: 23:33
 */
public class ConsumidorEventosDeportivos {
    private static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //Abrir conexão
        Connection connection = connectionFactory.newConnection();
        //Estabelecer um canal
        Channel channel = connection.createChannel();
        //Declarar exchange evento-deportivos
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
        //Criar fila e associar a exchange evento-deportivos
        String queueName = channel.queueDeclare().getQueue();
        // Padrão routing-key -> pais.esporte.event
        // * -> identifica uma palavra
        // # -> identifica muitas palavras
        // eventos tenis -> *.tenis.*
        // eventos na espanha -> es.#
        // todos os eventos -> #

        System.out.println("Insira a routing-key");
        Scanner scanner = new Scanner(System.in);
        String routingKey = scanner.nextLine();

        channel.queueBind(queueName, EXCHANGE, routingKey);
        //Criar subscription a uma fila associada ao exchange "evento-deportivos
        channel.basicConsume(queueName, true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensagem recebida: " + messageBody);
                    System.out.println("Routing key: " + message.getEnvelope().getRoutingKey());

                },
                (consumerTag) -> {
                    System.out.println("Consumidor" + consumerTag + " cancelado");
                }
        );

    }
}
