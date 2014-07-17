package com.v6ak.experimental.safeOption

/**
 * This class is very similar to [[scala.Option]], but this should be null-safe and more efficient.
 * That means, only SafeNone and SafeSome(someValue) are allowed. In [[scala.Option]], null, None,
 * Some(null) and Some(someValue) are allowed.
 *
 * This is very experimental!!! There are some drawbacks.
 *
 * First, its hashCode implementation is broken for SafeNone. This is a Scala bug. See https://issues.scala-lang.org/browse/SI-7401 and https://issues.scala-lang.org/browse/SI-7396
 * Second, only AnyRefs are supported.
 * Third, the code is a bit hacky at the moment. I use SafeOption[Null] instead of SafeOption[Nothing], which is a hack I don't like and does not work well.
 * Fourth, there are some important methods (e.g. map) missing. I was not able to implement some of them.
 * @param valueOrNull
 * @tparam T
 */
class SafeOption[+T <: AnyRef](val valueOrNull: T) extends AnyVal{

	def isDefined: Boolean = valueOrNull != null

	def isEmpty: Boolean = !isDefined

	override def toString: String =
		if(isDefined) s"SafeSome($valueOrNull)"
		else "SafeNone"

	def map[U <: AnyRef](f: T=>U): SafeOption[U] = {
		//val x: SafeOption[Int] = SafeNone
		fold[SafeOption[U]](SafeNone.asInstanceOf[SafeOption[U]])(v => new SafeOption[U](f(v)))
		/*if(isDefined) new SafeOption[U](f(valueOrNull))
		else SafeNone.asInstanceOf[SafeOption[U]]*/
	}	// TODO: possibly handle null values*/

	def fold[U](ifEmpty: => U)(f: T => U): U = if(isDefined) f(valueOrNull) else ifEmpty

	/*def filter(f: T => Boolean): SafeOption[T] =
		if(isDefined && f(get)) this
		else SafeNone*/

	def get = fold(throw new NoSuchElementException)(identity)

	def getOrNull = valueOrNull

	// FIXME: there is hashCode bug in Scala that I can't fix there

}

object SafeOption{
	def apply[T <: AnyRef](x: T) = new SafeOption(x)
}

object SafeSome{
	def apply[T <: AnyRef](x: T) = x match {
		case null => throw new NullPointerException
		case value => SafeOption(value)
	}
	def unapply[T <: AnyRef](option: SafeOption[T]): Option[T] = option.fold[Option[T]](None)(Some(_))
}

object `package` {

	val SafeNone = new SafeOption[Null](null)//.asInstanceOf[SafeOption[Nothing]] <-- This ugly hack makes it even more broken

}