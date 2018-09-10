# Swift技术交流小结

本次技术交流主要讲解了Swift的语法风格：简洁，安全，快速。讲解内容都在PPT上了，现将交流过程中提到的点做以总结。

### 闭包
Swift中的闭包类似于OC中的Block，其用法如下：

### 基本用法

Swift中Array的sort方法实现了Closure，我们来看下：

```Swift
var clients = ["Pestov", "Buenaventura", "Sreeram", "Babbage"]

clients.sort({(a: String, b: String) -> Bool in 
return a < b }) 

println(clients)
// [Babbage, Buenaventura, Pestov, Sreeram]
```   

这样就实现了数组中的元素排序。
但是基于`Swift`强大的类型推断功能，我们可以将其简化为：

```Swift
clients.sort({ a, b in
return a < b
})
```  

因为这个`Closure`是有返回值的，所以编译器可以再次推断，所以我们可以这样写

```Swift
clients.sort({ a, b in a < b })
```  

编译器还可以推断出其参数值，所以，我们这里可以写成

```Swift
clients.sort({$0 < $1})
```  

因为我们还有尾随闭包，所以我们可以进一步简化

```Swift
clients.sort{$0 < $1}
```  

### Functional Programming 

我们有很多函数式编程的高阶函数可以供调用：

```Swift
let result = words.filter{ $0.hasSuffix("gry")}.map{$0.uppercaseString}
```  

这样我们就可以找到所有以`gry`结尾的单词，并且将其转化为大写字母。如果这时结果是

```Swift
ANGRY
HUNGRY
```  

我们还可以调用`reduce`方法将其和成一个字符串
```Swift
let reducedResult = result.reduce("HULK"){"\($0) \($1)"}
```   

这时结果如下：
```Swift
HULK ANGRY HUNGRY
```  

### 函数值

比高可以传递一个函数，例如：

```Swift
 numbers.map {
        println($0)
} 
numbers.map(println)    // 可以将一个函数传递过去


var indexes = NSMutableIndexSet()
numbers.map {
    indexes.addIndex($0)
} 

numbers.map (indexes.addIndex) // 可以将一个Method传过去
```  

### 闭包是一个ARC对象

我们可以声明一个Closure属性：

```Swift
var onTempratureChange: (Int) -> Void = {}
func logTemperatureDifferences(initial: Int) {
    var prev = initial
    onTemperatureChange = { next in
        println("Changed \(next - prev)°F")
prev = next 
} 
```  

因为function也是closure，那么我们可以这样写：

```Swift
func logTemperatureDifferences(initial: Int) {
    var prev = initial
    func log(next: Int) {
        println("Changed \(next - prev)°F")
prev = next } 
    onTemperatureChange = log
```  

### 闭包的循环引用问题

和OC中的Block一样，Swift中也会出现循环引用的问题，我们来看看怎样解决：

```Swift
class TemperatureNotifier {
    var onChange: (Int) -> Void = {}
    var currentTemp = 72
    init() {
        onChange = { temp in
currentTemp = temp    } // error: requires explicit 'self' 
  } 
} 
```    

如果出现上面的循环引用问题，编译器会直接报错的，所以我们可以用上文提到的`unowned`来解决。我们可以将init()方法用下面的来取代：

```Swift
init() { 
    unowned let uSelf = self
    onChange = { temp in
      uSelf.currentTemp = temp
    }
```   

但是这样写还会出现一个问题，就是如果别处有一份逻辑一样的代码，某个人不注意拷贝过来了忘记将`self`改成uSelf，或者这个方法很长，写到下面的是忘记了将`self`改成uSelf，那么就会出现内存泄漏的问题。为了解决这个问题Swift中提出了下面的优雅做法：

```Swift
init() { onChange = {[unowned self] temp in 
self.currentTemp = temp 
} } 
```     

## Pattern Matching

`switch`中可以有范围，字符串和数字，并且Enum中可以关联属性，比如：
```Swift
// case中含有范围
func describe(value: Int) { switch value { 
case 0...4: println("a few") 
case 5...12: println("a lot") 
      default:
        println("a ton")
} } 
// case中

enum TrainStatus {
    case OnTime
    case Delayed(Int)
}
```  

使用的时候如下：
```Swift
switch trainStatus {
  case .OnTime:
} 
  println("on time")
case .Delayed(let minutes)
                        :
println("delayed by \(minutes) minutes")
```   

我们可以对这个`delay`做各种各样的匹配：
```Swift
switch trainStatus {
  case .OnTime:
    println("on time")
  case .Delayed(1):
    println("nearly on time")
  case .Delayed(2...10):
    println("almost on time, I swear")
  case .Delayed(_):
    println("it'll get here when it's ready")
``` 

### Pattern Compose

也就是说Pattern可以组合出现，一个Pattern中可以包含其它的Pattern，比如对上文的`TrainStatus`再做以Pattern Compose：

```Swift
enum VacationStatus {
    case Traveling(TrainStatus)
    case Relaxing(daysLeft: Int)
} 

switch vacationStatus {
  case .Traveling(.OnTime):
    tweet("Train's on time! Can't wait to get there!")
  case .Traveling(.Delayed(1...15)):
    tweet("Train is delayed.")
  case .Traveling(.Delayed(_)):
    tweet("OMG when will this train ride end #railfail")
  default:
  print("relaxing")
```   

### Type Pattern

`Pattern`不仅仅可以作用于`Enum`，还可以作用于动态的类型，如：`Class`

```Swift
func tuneUp(car: Car) {
    switch car {
      case let formulaOne as FormulaOne:
        formulaOne.enterPit()
      case let raceCar as RaceCar:
        if raceCar.hasTurbo { raceCar.tuneTurbo() }
        fallthrough
      default:
        car.checkOil()
        car.pumpTires()
} } 
```  

这样在多态中就会变得非常有用了。

### Tuple Patterns
Tuple pattern有其极其强大的功能，其可以对tuple的各个数值做以类型匹配。

```Swift
let color = (1.0, 1.0, 1.0, 1.0)
switch color {
  case (0.0, 0.5...1.0, let blue, _):
    println("Green and \(blue * 100)% blue")
  case let (r, g, b, 1.0) where r == g && g == b:
    println("Opaque grey \(r * 100)%")
```  

我们甚至可以对其中的各个数值做以相应的模式匹配。

### Pattern Matching的应用PList校验
比如我们有下面的方法来校验Plist中的内容是否有效

```Swift
func stateFromPlist(list: Dictionary<String, AnyObject>)
  -> State?
stateFromPlist(["name": "California",
                "population": 38_040_000,
                "abbr": "CA"])
``` 

这时我们要对`population`的值做以限制，如果是字符串返回`nil`，如果是超过某个范围的时候返回`nil`，如果是`abbr`中字母的个数大于2时候我们也返回`nil`，利用tuple pattern matching的强大特性，我们可以这样去做：

```Swift
func stateFromPlist(list: Dictionary<String, AnyObject>)
  -> State? {

switch (list["name"], list["population"], list["abbr"]) { 
case ( 
        .Some(let listName as NSString), 
        .Some(let pop as NSNumber),
        .Some(let abbr as NSString)
      ) where abbr.length == 2:
    return State(name: listName, population: pop, abbr: abbr)
  default:
return nil 
   } 
} 
```  

这就利用了tuple和限制想结合的方式优雅的解决了这个问题。

## Swift是否是跨平台的？

Swift目前不能跨平台，以后可能会有。请参考：https://www.zhihu.com/question/41240188

## Swift的缺点

目前现在ABI不稳定，也就是二进制接口不稳定，这就是：如果同一个手机上有两个不同版本Swift，那么就需要有两个不同版本的库来支持，增大了包。

## Swift中的方法调度

请参考我的技术博客：https://mikefighting.github.io/2017/08/01/Why-Swift-IS-swift/

