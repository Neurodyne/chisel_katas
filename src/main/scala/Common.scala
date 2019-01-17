
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

class AddrDecoder (addrWidth:Int, mmap:LinkedHashMap[UInt, (UInt, UInt)]) extends Module {
 
  val size  = mmap.size 
  require (size > 0, "Empty map passed")
  
  val io = IO (new Bundle {
    val addr    = Input (UInt(addrWidth.W))      
    val en      = Input (Bool())  
    val sel     = Output(Vec(size,Bool()))  // $onehot0 selector
  })

  // Curried function which accepts a tuple and an input addr
  def inside (range:(UInt,UInt))(addr:UInt):Bool = {
    addr >= range._1 && addr < range._1 + range._2
  }

  // MUX output
  //for (i <- 0 until size) {
  //  io.sel(i) := false.B
  //}

  // Loop thru the Memory Map, pair with index and evaluate logic value for io.sel
  //mmap.zipWithIndex foreach { case (entry,idx) =>

  //  when (io.en && inside(entry)(io.addr)) {
  //    io.sel(idx) := true.B

  //  } .otherwise {
  //    io.sel(idx) := false.B
  //  }
  //  
  //}    
  //for ((k,v) <- mmap) 
  //  if (inside(k,v)(io.addr))
  //    yield ()

  //io.sel := Mux1H ( (for((k,v) <- mmap ) yield inside ((k,v))(io.addr)) ) 
  //io.sel := Mux1H ( mmap map { case (k,v) => inside(k,v)(io.addr)}) 
  //for ((k,v) <- mmap) 
  //  if(inside(k,v)(io.addr))
  //    yield (io.sel(0) := true.B)
  //  else 
  //    yield (io.sel(0) := false.B)
 
  //for (i <- 0 until size)
  //  io.sel(i) := false.B
   
  //for ((k,v) <- mmap) {
  //  if (inside(v)(io.addr) == true.B)
  //    io.sel(k) := true.B
  //  else
  //    io.sel(k) := false.B
  //  
  //}
 
  //for ((k,v) <- mmap) {
  //  io.sel(k) :=  when ( inside(v)(io.addr) ) { true.B }
  //                .otherwise { false.B }
 
  for (i <- 0 until size)
    if ( inside(mmap(i.asUInt))(io.addr) == true.B )
      io.sel(i) :=  true.B 
    else 
      io.sel(i) := false.B
                    

  //io.sel map { p => 
  //  if ( for(k,v) <- mmap) inside()
  //}
  //
  //// $onehot0 output encoding check
  //assert (PopCount(io.sel) <= 1.U, "Invalid addr decoding")
}

