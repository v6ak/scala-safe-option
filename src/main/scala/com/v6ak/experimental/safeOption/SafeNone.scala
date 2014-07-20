package com.v6ak.experimental.safeOption

object SafeNone {
	def unapply(v: SafeOption[_]) = v == SafeNone[AnyRef]
	def apply[T <: AnyRef] = new SafeOption[Null](null).asInstanceOf[SafeOption[T]]
}
