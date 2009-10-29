package pdfdsl.support


class InternalCommand {
  def lingo
  def defaults = [:]

  void setLingo(lingo) {
    this.lingo = defaults + lingo
  }

  def stampWith(DslWriter dslWriter) {
    dslWriter.stamp(lingo)  
  }
}