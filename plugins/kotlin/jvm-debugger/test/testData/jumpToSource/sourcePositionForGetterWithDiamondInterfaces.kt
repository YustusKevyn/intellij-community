package sourcePositionForGetterWithDiamondInterfaces

interface I1 {
    val i1Int get() = 1
}

interface I2 : I1 {
    val i2Int get() = 2
}

interface I3 : I1 {
    val i3Int get() = 1
}

class TestClass : I2, I3 {

}

fun main() {
    val instance = TestClass()
    // JUMP TO SOURCE: instance.i3Int
    // JUMP TO SOURCE: instance.i2Int
    // JUMP TO SOURCE: instance.i1Int
    //Breakpoint!
    println("")
}