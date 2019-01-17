package my_pkg

import chisel3._
import chisel3.util.HasBlackBoxResource
import chisel3.core.{IntParam}
import Cfg._
import complex_pkg._


trait Ring[A] {
  def zero():A
  def one():A
  def +(a:A, b:A):A
}

object Ring {
  //val ev  = implicitly[Ring[A]]
  
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

  //implicit val ComplexRing:Ring[Complex[A,B]]  = new Ring[Complex[A,B]] {
  //  def zero(): Complex[A,B] = Complex.zero
  //  def one (): Complex[A,B] = Complex.one
  //  def + (a:Complex[A,B], b:Complex[A,B]) = Complex (a.re + b.re, a.im + b.im)
  //}
}

class Dummy[A <: Data:Ring] (dType: A) extends Module {
 
   val io = IO (new Bundle {
     
    val vld  = Input (Bool())
    val clr  = Input (Bool())
        
    val a    = Input (dType)
    val b    = Input (dType)

    val rdy  = Output (Bool())
    val res  = Output (dType)
       
   })
  
    val zero  = 0.U(dType.getWidth.W).asTypeOf(dType)

    io.rdy := true.B
    io.res := zero
    //io.res := io.a + io.b
      
}


// Verilog wrapper
class DspTop extends Module {

  val io = IO(new Bundle {

    val vld = Input(Bool())
    val clr = Input(Bool())
    val a   = Input(inType)
    val b   = Input(inType)

    val rdy = Output(Bool())
    val res = Output(outType)

  })

  val inst = Module(new Dummy(inType))

  inst.io <> io

}

