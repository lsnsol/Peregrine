package com.peregrine;

// import javax.sound.midi.Receiver;

// import org.apache.kafka.clients.producer.internals.Sender;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
// import org.springframework.kafka.test.rule.KafkaEmbedded;
// import org.springframework.kafka.test.utils.ContainerTestUtils;
// import org.springframework.kafka.test.utils.KafkaTestUtils;

@SpringBootApplication
@EnableMongoRepositories("com.peregrine.repository")
@ComponentScan("com.peregrine.*")
public class PeregrineAppApplication {
	// private static String bootTopic = "Peregrine Boot";

	// @Autowired
	// private Sender sender;
	
	// @Autowired
	// private Receiver receiver;
	
	// @ClassRule
	// public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, bootTopic);
	
	// @BeforeClass
	// public static void setUpBeforeClass() throws Exception {
	// 	System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
	// }
	
	// @Test
	// public void testReceive() throws Exception {
	// 	sender.send(bootTopic, "Hello Boot!");
	// 	receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
	// 	assertThat(receiver.getLatch().getCount()).isEqualTo(0);
	// }

	public static void main(String[] args) {
		SpringApplication.run(PeregrineAppApplication.class, args);
	}

}
