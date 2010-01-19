package pdfdsl.support

class DelegateWrapper {
  private delegate
  def overrides = [:]

  DelegateWrapper(delegate) {
    this.delegate = delegate
  }

  def propertyMissing(String name) {
    if(overrides[name]) {
      overrides[name]()
    } else {
      delegate[name]
    }
  }

  def methodMissing(String name, args) {
    if(overrides[name]) {
      overrides[name](*args)
    } else {
      delegate."$name"(*args)
    }
  }
}
