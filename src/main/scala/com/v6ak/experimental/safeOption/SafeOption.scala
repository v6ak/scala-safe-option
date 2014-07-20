package com.v6ak.experimental.safeOption

/**
 * This class is very similar to [[scala.Option]], but this should be null-safe and more efficient.
 * That means, only SafeNone and SafeSome(someValue) are allowed. In [[scala.Option]], null, None,
 * Some(null) and Some(someValue) are allowed.
 *
 * This is very experimental!!! There are some drawbacks. See README.md for more details.
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
		fold[SafeOption[U]](SafeNone[U])(v => new SafeOption[U](f(v)))
		/*if(isDefined) new SafeOption[U](f(valueOrNull))
		else SafeNone.asInstanceOf[SafeOption[U]]*/
	}	// TODO: possibly handle null values*/

	def fold[U](ifEmpty: => U)(f: T => U): U = if(isDefined) f(valueOrNull) else ifEmpty

	def filter(f: T => Boolean): SafeOption[T] =
		if(isDefined && f(get)) this
		else SafeNone[T]

	def get = fold(throw new NoSuchElementException)(identity)

	def getOrNull = valueOrNull

	// FIXME: there is hashCode bug in Scala that I can't fix there

}

object SafeOption{
	def apply[T <: AnyRef](x: T) = new SafeOption(x)
}
