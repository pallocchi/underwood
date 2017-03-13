# Underwood: A simple way to use Netflix Hystrix

[![][travis img]][travis]
[![][maven img]][maven]
[![][license img]][license]

Underwood is a library over [Hystrix](https://github.com/Netflix/Hystrix) that allows you to use Java 8 lambdas, keeping your code simple and clean.

The following example is a simple `HystrixCommand` and its invocation:

```java
public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
		.andCommandKey(HystrixCommandKey.Factory.asKey("ExampleCommand")));
        this.name = name;
    }

    @Override
    protected String run() {
        return "Hello " + name + "!";
    }
    
    @Override
	protected String getFallback() {
        return "Hello anonymous!";
	}

}
```

```java
String greet = new CommandHelloWorld("Frank").execute();
```

But using **Underwood** you just write:

```java
String greet = Underwood.forSingle(String.class)
  .withGroup("ExampleGroup")
  .withName("ExampleCommand")
  .withFallback(e -> "Hello anonymous!")
  .execute(() -> "Hello " + name + "!");
```

As you see, you don't have to create an extra class for each command anymore!

## Examples

### Single

```java
// Retrieve a single string
String greet = Underwood.forSingle(String.class)
  .withGroup("ExampleGroup")
  .withName("ExampleCommand")
  .withFallback(e -> "Hello anonymous!")
  .execute(() -> "Hello Frank!");
```

### Optional

```java
// Retrieve an optional string
Optional<String> greet = Underwood.forOptional(String.class)
  .withGroup("ExampleGroup")
  .withName("ExampleCommand")
  .withFallback(e -> Optional.empty())
  .execute(() -> Optional.of("Hello Frank!"));
```

### List

```java
// Retrieve a list of strings
List<String> presidents = Underwood.forList(String.class)
  .withGroup("ExampleGroup")
  .withName("ExampleCommand")
  .withFallback(e -> Collections.emptyList())
  .execute(() -> Arrays.asList("Frank"));
```

### Task

```java
// Execution without results
Underwood.forTask()
  .withGroup("ExampleGroup")
  .withName("ExampleCommand")
  .execute(() -> System.out.println("Hello Frank!"));
```

### Timeout

```java
// Add a timeout of 5 seconds
String greet = Underwood.forSingle(String.class)
  .withGroup("ExampleGroup")
  .withName("ExampleCommand")
  .withTimeout(5000)
  .withFallback(e -> "Hello anonymous!")
  .execute(() -> "Hello Frank!");
```

## Maven

Add repository:

```xml
<repository>
    <id>ppallocchi</id>
    <url>https://dl.bintray.com/ppallocchi/maven</url>
</repository>
```

Add dependency:

```xml
<dependency>
    <groupId>com.pallocchi</groupId>
    <artifactId>underwood</artifactId>
    <version>x.y.z</version>
</dependency>
```
 
## LICENSE

MIT License

Copyright (c) 2017 Pablo Pallocchi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

[travis]:https://travis-ci.org/ppallocchi/underwood
[travis img]:https://travis-ci.org/ppallocchi/underwood.svg?branch=master

[license]:LICENSE.txt
[license img]:https://img.shields.io/github/license/mashape/apistatus.svg

[maven]:https://bintray.com/ppallocchi/maven/underwood/_latestVersion
[maven img]:https://api.bintray.com/packages/ppallocchi/maven/underwood/images/download.svg
