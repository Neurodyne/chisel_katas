// This package contains an example of a Type Class, which implements some funtionality
// All other data dypes, including Chisel types, should join this typeclass thru implicit conversions

package ring_pkg  

import chisel3._
import chisel3.core.{FixedPoint}
import complex_pkg._

// Generic Ring trai
trait Ring[A] {
  def zero():A
  def one():A
  def +(a:A, b:A):A
}

// Apply methods and concrete implicit implementations
object Ring {
  
  implicit val UIntRing:Ring[UInt]  = new Ring[UInt] {

    def zero(): UInt = 0.U
    def one (): UInt = 1.U
    def + (a:UInt, b:UInt) = a + b

  }
  
  implicit val SIntRing:Ring[SInt]  = new Ring[SInt] {

    def zero(): SInt = 0.S
    def one (): SInt = 1.S
    def + (a:SInt, b:SInt) = a + b

  }
  
  implicit val FPRing:Ring[FixedPoint]  = new Ring[FixedPoint] {

    def zero(): FixedPoint = 0.0.F(0.BP)
    def one() : FixedPoint = 1.0.F(0.BP)
    def + (a:FixedPoint, b:FixedPoint) = a + b

  }
  
  implicit val ComplexUIntRing:Ring[Complex[UInt, UInt]]  = new Ring[Complex[UInt, UInt]] {
    def zero(): Complex[UInt, UInt] = Complex.zero
    def one (): Complex[UInt, UInt] = Complex.one
    def + (a:Complex[UInt, UInt], b:Complex[UInt, UInt]) = Complex (a.re + b.re, a.im + b.im)
  }
}
