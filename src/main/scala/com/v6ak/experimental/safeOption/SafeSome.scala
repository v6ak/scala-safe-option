package com.v6ak.experimental.safeOption

object SafeSome{
	def apply[T <: AnyRef](x: T) = x match {
		case null => throw new NullPointerException
		case value => SafeOption(value)
	}
	def unapply[T <: AnyRef](option: SafeOption[T]): Option[T] = option.fold[Option[T]](None)(Some(_))
}
