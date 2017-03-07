# Underwood: A simple way to use Netflix Hystrix

[![][license img]][license]

Underwood is a library over Hystrix that allows you to use Java 8 lambdas, keeping your code simple and clean.

The following example is a simple HystrixCommand and its invocation:

```java
public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        return "Hello " + name + "!";
    }
}
```

```java
String greet = new CommandHelloWorld("Bob").execute();
```

But using Underwood you just write:

```java
String greet = Underwood.forSingle(String.class)
  .withGroup("ExampleGroup")
  .withName(name)
  .execute(() -> "Hello " + name + "!");
```

As you see, you don't have to create an extra class for each command anymore!
## Binaries

Example for Maven:

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

[license]:LICENSE.txt
[license img]:https://img.shields.io/github/license/mashape/apistatus.svg
