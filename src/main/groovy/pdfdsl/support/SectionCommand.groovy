package pdfdsl.support


class SectionCommand extends InternalCommand {
  private lines = []

  def line(map) {
    lines << map  
  }

  def stampWith(DslWriter dslWriter) {
    def coordinates = [at:lingo.at]
    lines.each {
      dslWriter.stamp(lingo + coordinates + it)
      coordinates = [at:[lingo.at[0], coordinates.at[1] - lingo.fontSize]]
    }
  }
  
}