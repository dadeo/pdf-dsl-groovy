package pdfdsl.support


class InternalCommand {
  def lingo

  def stampWith(DslWriter dslWriter) {
    dslWriter.stamp(lingo)  
  }
}