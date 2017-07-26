# Welcome to eixox's Jetfuel
We've made this library to provide state of the art aspect driven utilities as well as a nice usecase programming paradigm.
It is and always will be zero dependencies. That is, we'll only put core code in here. Nothing will depend on anything else.
By using our library you have access to:
* Annotation based restrictions to improve validations.
* A persistence api with direct I/O to databases, files, no-sqls etc.
* An aspect driven api for reflection.
* A usecase programming paradigm that enables deploys on any servlet container and on amazon lambda.

**Step 0:** Fork or clone this repo. Create your maven project and add this as a dependency.

> Hint: Forking is easier to update from our mainstream and also easier to help us fix bugs in the unlikely event that one is found.

## Writing usecases

It is very simple and straightforward to write your own usecase. Checkout: 
> com.eixox.usecases.Healthcheck java code.

```java
public class Healthcheck extends UsecaseImplementation<Void, Date> {

	@Override
	protected void mainFlow(UsecaseExecution<Void, Date> execution) throws Exception {
		execution.result = new Date();
		execution.result_type = UsecaseResultType.SUCCESS;
	}

}
```
And that's it. You can fireup your api and navigate to /Healthcheck to see the usecase exectuion printed out on the screen:

```json
{
execution_start: 1500860302622,
execution_end: 1500860302622,
result_type: "SUCCESS",
result: 1500860302622,
params: null,
validation: null,
exception: null
}
```
> Always super fast to the millisecond! Check it out.

### Nice hint for your pom:

We recommend setting your code to utf-8 and running on java 1.8. You can achieve this by making your pom.xml look like this:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.eixox</groupId>
	<artifactId>jetfuel</artifactId>
	<version>2.0</version>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	</properties>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.6.1</version>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
					<encoding>UTF-8</encoding>
					<forceJavacCompilerUse>true</forceJavacCompilerUse>
				</configuration>
			</plugin>
		</plugins>
	</build>
    .
    .
    .
```

## jetfuel-servlet

This project is a boilerplate to help you deploy your awesome API using any servlet container. Specially usefull when you're running your dev environment prior to publishing a serverless api on amazon or launching your own elastic environment.
This standard guide uses eclipse with WTP and tomcat 9.0 as a server.

We map URLs in the form of /package/subpackage/ClassName to package.subpackage.ClassName and use reflection to instantiate and run it, as long as it extends UsecaseImplementation<TParams, TResult>.

**Step 1:** Make sure you already have another project using our **jetfuel usecase** programming paradigm. 

**Step 2:** Locate private static final String PACKAGE_ROOT on the only code file of this project and change it to the root name of your usecases. 

Hint: If your code is organized as ours: com.eixox.usecases.crm.person.LogIn and com.eixox.usecases.profiler.account.Detail. Set “com.eixox.usecases” as package root so you can call /crm/person/LogIn and /profiler/account/Detail on your API.

**Step 3:** Use pom.xml to add other projects as dependencies to your project. 

Or simply right click on the project name -> Maven -> Add Dependency and every dependency added that has a class that extends UsecaseImplementation<TParams, TResult> will be automagically accessible through the API.

**Step 4:** Deploy your project to the server. 

I use and recommend the WTP eclipse extensions. The server plugin detects changes in files and automatically reloads the application, making this the ideal platform for developing the api.

## jetfuel-aws-lambda

> Ready to run your awesome usecases serverless on amazon lambda?

This project is a boilerplate to help you deploy your awesome API using no servers on amazon aws. 
This standard guide uses eclipse with aws extensions.


We map /package/subpackage/ClassName to package.subpackage.ClassName and use reflection to instantiate and run it, as long as it extends UsecaseImplementation<TParams, TResult>.

**Step 0:** Make sure you already have another project using our **jetfuel usecase** programming paradigm. 

**Step 1** Fork or clone this repo. 

> Hint: Forking is easier to update from our mainstream and also easier to help us fix bugs in the unlikely event that one is found.

**Step 2:** Locate private static final String PACKAGE_ROOT on the only code file of this project and change it to the root name of your usecases. 

> Hint: If your code is organized as ours: com.eixox.usecases.crm.person.LogIn and com.eixox.usecases.profiler.account.Detail. Set "com.eixox.usecases" as package root so you can call /crm/person/LogIn and /profiler/account/Detail on your API.

**Step 3:** Use pom.xml to add other projects as dependencies to your project. 

Or simply right click on the project name -> Maven -> Add Dependency and every dependency added that has a class that extends UsecaseImplementation<TParams, TResult> will be automagically accessible through the API.

**Step 4:** Deploy your function to AWS Lambda.

I use and recommend the aws eclipse extensions. So if your're using eclipse try http://docs.aws.amazon.com/toolkit-for-eclipse/v1/user-guide/setup-install.html


**Step 5:** Configure API Gateway as a proxy to your lambda function
http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-set-up-simple-proxy.html