import org.apache.kafka.streams.processor.{PunctuationType, api}
import org.apache.kafka.streams.processor.api.{Processor, ProcessorContext}
import org.apache.kafka.streams.state.KeyValueStore

class CreateKeyValueStoreProcessor(keyValueStoreName: String) extends Processor[String, String, String, String] {

  var context : ProcessorContext[String, String] = null

  override def process(record: api.Record[String, String]): Unit = {
    val keyValueStateStore : KeyValueStore[String, String] = context.getStateStore(keyValueStoreName)
    println(record.key() + record.value())
    keyValueStateStore.put(record.key(), record.value())
  }

  override def init(context: ProcessorContext[String, String]): Unit = {
    this.context = context
    println("Context Initialized...")
  }

}