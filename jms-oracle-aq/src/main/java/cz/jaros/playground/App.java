package cz.jaros.playground;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jms.AQjmsAdtMessage;
import oracle.jms.AQjmsFactory;
import oracle.jms.AQjmsSession;
import oracle.xdb.XMLType;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.jms.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        try {
            var app = new App();
            var dataSource = app.createOracleDataSource();
            app.testJdbcConnection(dataSource);
            app.testTopic(dataSource);
        } catch (Exception e) {
            throw new RuntimeException("Ooops!", e);
        }
    }

    void testJdbcConnection(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println("JDBC driver version is: " + metaData.getDriverVersion());
        }
    }

    public void testTopic(DataSource dataSource) throws Exception {
        TopicConnectionFactory connectionFactory = AQjmsFactory.getTopicConnectionFactory(dataSource);
        TopicConnection topicConnection = connectionFactory.createTopicConnection();
//        try () {
            System.out.println("Topic connection: " + topicConnection);
            TopicSession session = topicConnection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);

            Map typeMap = ((AQjmsSession) session).getTypeMap();
//            typeMap.put("SYS.XMLTYPE", Class.forName("oracle.xdb.XMLType"));
//            typeMap.put("SYS.XMLTYPE", Class.forName("oracle.sql.AnyDataFactory"));
            typeMap.put("SYS.XMLTYPE", Class.forName("oracle.xdb.XMLTypeFactory"));

            Topic topic = ((AQjmsSession) session).getTopic("CUPI_ADM", "Q_CUPI_LCR");
            System.out.println("Topic: " + topic);

            topicConnection.start();

//            MessageConsumer consumer = session.createConsumer(topic);
//            Message message = consumer.receive();

            TopicSubscriber subscriber = session.createDurableSubscriber(topic, "CUPI_LCR_PROCESSOR");
            Message message = subscriber.receive(3000);
            System.out.println("Received message: " + message);
            var adtPayload = ((AQjmsAdtMessage) message).getAdtPayload();
            var payload = ((XMLType) adtPayload).getStringVal();
            System.out.println("Payload:\n" + payload);

//            AQjmsTopicReceiver receiver = ((AQjmsSession) session).createTopicReceiver(topic, "CUPI_LCR_PROCESSOR", null);
//            Message message = receiver.receive(5000);

//            Queue queue = ((AQjmsSession) session).getQueue("cupi_adm", "q_cupi_lcr");
//            try (MessageConsumer consumer = session.createConsumer(topic)) {
//                Message message = consumer.receive();
//                System.out.println("Received message: " + message);
//            }
//        }

//        var jmsTemplate = new JmsTemplate(connectionFactory);
//        var msg = jmsTemplate.receive("CUPI_ADM.Q_CUPI_LCR");
//        var msg = jmsTemplate.receive("IMWTNEW_T2.Q_PRODUCTS");
//
//        System.out.println("Message received: " + msg);
    }

//    public void testQueue() throws Exception {
//        var ds = createOracleDataSource();
//        QueueConnectionFactory connectionFactory = AQjmsFactory.getQueueConnectionFactory(ds);
//
//        try (QueueConnection queueConnection = connectionFactory.createQueueConnection()) {
//            System.out.println("Queue connection: " + queueConnection);
//            Session session = queueConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//            Queue queue = ((AQjmsSession) session).getQueue("cupi_adm", "q_cupi_lcr");
//            try (MessageConsumer consumer = session.createConsumer(queue)) {
//                Message message = consumer.receive();
//                System.out.println("Received message: " + message);
//            }
//        }

//        var jmsTemplate = new JmsTemplate(connectionFactory);
//        var msg = jmsTemplate.receive("CUPI_ADM.Q_CUPI_LCR");
//        var msg = jmsTemplate.receive("IMWTNEW_T2.Q_PRODUCTS");
//
//        System.out.println("Message received: " + msg);
//    }

    private DataSource createOracleDataSource() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
//        dataSource.setUser("it_osd");
//        dataSource.setPassword("tsnzEhrCJF9");
        dataSource.setUser("CUPI_USR");
        dataSource.setPassword("jVkacYzMH3nm");
        dataSource.setURL("jdbc:oracle:oci:@//webcluster-t.test:1521/IMWDBT");
//        dataSource.setURL("jdbc:oracle:thin:@webcluster-t.test:1521/IMWDBT");
        dataSource.setImplicitCachingEnabled(true);
        dataSource.setFastConnectionFailoverEnabled(true);
        return dataSource;
    }

    private DataSource createHikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setJdbcUrl("jdbc:oracle:thin:@webcluster-t.test:1521/IMWDBT");
        config.setUsername("CUPI_USR");
        config.setPassword("jVkacYzMH3nm");

        config.setMinimumIdle(1);
        config.setMaximumPoolSize(1);

        return new HikariDataSource(config);
    }

    private DataSource createBasicDataSource() {
        var ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        ds.setUrl("jdbc:oracle:thin:@webcluster-t.test:1521/IMWDBT");
        ds.setUsername("CUPI_USR");
        ds.setPassword("jVkacYzMH3nm");
        ds.setInitialSize(1);
        ds.setMaxTotal(1);
        return ds;
    }
}
