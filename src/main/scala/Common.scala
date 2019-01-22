
package  my_pkg  

import   chisel3._
import   chisel3.util.{MuxCase, PopCount, Mux1H}
import   collection.mutable.{LinkedHashMap}


class Link[A <: Data] (dType:A) extends Bundle {
  val data = Output (dType)
  override def cloneType = (new Link(dType)).asInstanceOf[this.type]
}

class Counter(size: Int) extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(size.W))
    val ena  = Input (Bool())
    val clr  = Input (Bool())
  })

  val tmp = RegInit (0.U(size.W))

  when (reset.asBool) {
    tmp := 0.U   
  }
  .otherwise {
    tmp := MuxCase(tmp, Array( io.clr -> 0.U, io.ena -> (tmp + 1.U)))
  }
  
  io.out := tmp
}

sealed class AddrDecoder[A <: Data, C[_] <: Iterable[_]] (baseSeq:C[A], sizeSeq:C[A], addrWidth:Int) extends Module {

  val bsize  = baseSeq.size 
  val ssize  = sizeSeq.size 
  require (bsize > 0, "Empty map passed")
  require (bsize == ssize, "Invalid initialization of AddrDecoder")
  
  val io = IO (new Bundle {
    val addr    = Input (UInt(addrWidth.W))      
    val en      = Input (Bool())  
    val sel     = Output(Vec(bsize,Bool()))  // $onehot0 selector
  })

  def inside():Bool = {
    import scala.math.Ordered._
    true.B
  }

  baseSeq zip io.sel foreach { case(base, port) =>
    
    when (inside()) {
      port := true.B
    }
    .otherwise {
      port := false.B
    }
    
  } 

  // $onehot0 output encoding check
  //assert (PopCount(io.sel) <= 1.U, "Invalid addr decoding")
}

