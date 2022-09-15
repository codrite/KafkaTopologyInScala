object Main {

  def main(args: Array[String]): Unit = {
    new KafkaPipeline("source", "kv", "target").initialize
  }

}
