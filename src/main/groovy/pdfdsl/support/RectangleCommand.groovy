package pdfdsl.support

import java.awt.Color


class RectangleCommand extends InternalCommand {
  private def defaults = [borderWidth: 1, borderColor: Color.BLACK, backgroundColor: Color.WHITE]

  void setLingo(lingo) {
    super.lingo = defaults + lingo
  }

  def stampWith(DslWriter dslWriter) {
    dslWriter.withDirectContent(lingo.page) {cb, pageSize ->
      def attrs = new MapWrapper(lingo)
//      cb.lineWidth = 4

      def plotRectangle = {
        cb.rectangle(
            (float) attrs.at[0].value(pageSize, attrs),
            (float) attrs.at[1].value(pageSize, attrs),
            (float) attrs.width,
            (float) -attrs.height
        )
      }

      plotRectangle()

      cb.colorFill = attrs.backgroundColor
      cb.fill()

      plotRectangle()

      cb.colorStroke = attrs.borderColor
      cb.stroke()

    }
  }

}