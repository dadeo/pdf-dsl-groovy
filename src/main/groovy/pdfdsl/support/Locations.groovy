package pdfdsl.support

import com.lowagie.text.Rectangle


class Locations {

  static def center = new Location({Rectangle rect, MapWrapper mapWrapper -> (rect.right - rect.left) / 2})
  static def right = new Location({Rectangle rect, MapWrapper mapWrapper -> rect.right })
  static def left = new Location({Rectangle rect, MapWrapper mapWrapper -> rect.left })

  static def top = new Location({Rectangle rect, MapWrapper mapWrapper -> rect.top })
  static def bottom = new Location({Rectangle rect, MapWrapper mapWrapper -> rect.bottom })
  static def middle = new Location({Rectangle rect, MapWrapper mapWrapper -> (rect.top - rect.bottom) / 2})

  static def fontSize = new Location({Rectangle rect, MapWrapper mapWrapper -> mapWrapper.fontSize })

}