import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.state.{KeyValueStore, StoreBuilder, Stores}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}

import java.util.Properties

class KafkaPipeline(sourceTopic: String, kvTopic: String, targetTopic: String) {

  def topologyDefinition: Topology = {
    val stateStoreBuilder : StoreBuilder[KeyValueStore[String, String]] = Stores.keyValueStoreBuilder(Stores.inMemoryKeyValueStore("TEST"),
                                                                                                      Serdes.String(),
                                                                                                      Serdes.String())
    val topology = new Topology()

    topology.addSource("SOURCE_TOPIC", sourceTopic)
            .addSource("KV_STATE", kvTopic)
            .addProcessor("CONCATENATE_KEY_VALUE", () => new ConcatenateProcessor("TEST"), "KV_STATE")
            .addStateStore(stateStoreBuilder, "CONCATENATE_KEY_VALUE")
            .addSink("TARGET_TOPIC", targetTopic, "CONCATENATE_KEY_VALUE")
  }

  def initialize: Unit = {
    println(topologyDefinition.describe())
    val streams = new KafkaStreams(topologyDefinition, streamsConfig)
    streams.start()

    while(streams.state() != KafkaStreams.State.RUNNING)
      Thread.sleep(10)

    println("RUNNING")

    Thread.sleep(100000)
  }

  def streamsConfig: Properties = {
    val streamConfig = new Properties()

    streamConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "task-stream")
    streamConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    streamConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, classOf[Serdes.StringSerde])
    streamConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[Serdes.StringSerde])

    streamConfig
  }

}