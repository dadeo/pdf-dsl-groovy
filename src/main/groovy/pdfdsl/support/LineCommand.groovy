package pdfdsl.support

class LineCommand extends InternalCommand {

  LineCommand() {
    defaults = [width:1]
  }

  def stampWith(DslWriter dslWriter) {
    dslWriter.withDirectContent(lingo.page) {cb, pageSize ->
      def attrs = new MapWrapper(lingo)
      cb.lineWidth = attrs.width
      cb.moveTo((float) attrs.at[0].value(pageSize, attrs), (float) attrs.at[1].value(pageSize, attrs))
      cb.lineTo((float) attrs.to[0].value(pageSize, attrs), (float) attrs.to[1].value(pageSize, attrs))
      cb.colorStroke = attrs.color
      cb.stroke()
    }
  }

}