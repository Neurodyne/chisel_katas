// This package contains an example of a Type Class, which implements some funtionality
// All other data dypes, including Chisel types, should join this typeclass thru implicit conversions

package ring_pkg  

import chisel3._
import chisel3.core.{FixedPoint => FP}
import complex_pkg._

// Generic Ring trait. Implements common operations
trait Ring[A] {
  def zero():A
  def one():A
  def +(a:A, b:A):A
}

// Apply methods and concrete implicit implementations
object Ring {
 
//--------------------------------------------------------------------------------------------
// Simple Types
//--------------------------------------------------------------------------------------------

  // UInt
  implicit val UIntRing:Ring[UInt]  = new Ring[UInt] {

    def zero(): UInt = 0.U
    def one (): UInt = 1.U
    def + (a:UInt, b:UInt) = a + b

  }
  
  // SInt
  implicit val SIntRing:Ring[SInt]  = new Ring[SInt] {

    def zero(): SInt = 0.S
    def one (): SInt = 1.S
    def + (a:SInt, b:SInt) = a + b

  }

  // Alias types for FixedPoint
  val FPZero  = 0.0.F(0.BP)
  val FPOne   = 1.0.F(0.BP)
    
  // FixedPoint
  implicit val FPRing:Ring[FP]  = new Ring[FP] {

    def zero(): FP = FPZero
    def one() : FP = FPOne
    def + (a:FP, b:FP) = a + b

  }
 
//--------------------------------------------------------------------------------------------
// Complex Types
//--------------------------------------------------------------------------------------------

  implicit val ComplexUIntRing:Ring[Complex[UInt, UInt]]  = new Ring[Complex[UInt, UInt]] {
    def zero(): Complex[UInt, UInt] = Complex.wire (0.U,0.U)
    def one (): Complex[UInt, UInt] = Complex.wire (1.U,0.U)
    def + (a:Complex[UInt, UInt], b:Complex[UInt, UInt]) = Complex.wire (a.re + b.re, a.im + b.im)
  }
  
  implicit val ComplexSIntRing:Ring[Complex[SInt, SInt]]  = new Ring[Complex[SInt, SInt]] {
    def zero(): Complex[SInt, SInt] = Complex.wire (0.S,0.S)
    def one (): Complex[SInt, SInt] = Complex.wire (1.S,0.S)
    def + (a:Complex[SInt, SInt], b:Complex[SInt, SInt]) = Complex.wire (a.re + b.re, a.im + b.im)
  }
 
  type ComplexFP = Complex[FP, FP]

  implicit val ComplexFPRing:Ring[ComplexFP]  = new Ring[ComplexFP]{
    def zero(): ComplexFP = Complex.wire (FPZero, FPZero)
    def one (): ComplexFP = Complex.wire (FPZero, FPOne)
    def + (a:ComplexFP, b:ComplexFP) = Complex.wire (a.re + b.re, a.im + b.im)
  }

}
