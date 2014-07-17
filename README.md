In Scala, there is scala.Option class. This is a great class with some drawbacks. They are:

1. No `null` safety. Values `null` and `Some(null)` are valid for type `Option[String]`
2. Additional runtime overhead.

I've tried to overcome these drawbacks by this experiment.

However, some other limitations have appeared.

First, `SafeNone` is not `SafeOption[Nothing]`, but `SafeOption[Null]`. Together with some Scala type-system limitations, this makes hard to write methods like `map`. It seems to be impossible without ugly typecasts.

Second, only `AnyRef`s are supported. This is a design limitation.

Last but not least, `SafeNone.hashCode` throws `NullPointerException`. This problem is caused by Scala bug I've no workaround for. See https://issues.scala-lang.org/browse/SI-7401 and https://issues.scala-lang.org/browse/SI-7396 .

If you have some ideas how to overcome some of these problems, your ideas are welcome.