This implements some advanced Chisel3 examples in architecture, type inference and testing.

## Usage
In the project root folder, launch sbt:
> sbt

To run a MACC tester:
```
testOnly my_pkg.MaccTester -- -z Basic 
```

to get Verilog:
```
testOnly my_pkg.MaccTester -- -z veri 
```

To run a VRF tester:
```
testOnly my_pkg.VrfTester -- -z Basic
```

to get Verilog:
```
testOnly my_pkg.VrfTester -- -z veri 
```
