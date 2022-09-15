import org.apache.kafka.streams.processor.api
import org.apache.kafka.streams.processor.api.{Processor, ProcessorContext}
import org.apache.kafka.streams.state.KeyValueStore

class ConcatenateProcessor(keyValueStoreName: String) extends Processor[String, String, String, String] {

  var context : ProcessorContext[String, String] = null

  override def process(record: api.Record[String, String]): Unit = {
    val keyValueStore: KeyValueStore[String, String] = context.getStateStore(keyValueStoreName)
    println(record.key() + " : " + keyValueStore.get(record.key()))
  }

  override def init(context: ProcessorContext[String, String]): Unit = {
    this.context = context
    val keyValueStore: KeyValueStore[String, String] = context.getStateStore(keyValueStoreName)
    println(keyValueStore.approximateNumEntries())
  }

}