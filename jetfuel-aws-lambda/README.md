# jetfuel-aws-lambda
```
This project is a boilerplate to help you deploy your awesome API using no servers on amazon aws. 
```
This standard guide uses eclipse with aws extensions to facilitate deploying your function to lambda. You also need to fork (or clone) and hopefully contribute to another open source project: 
[jetfuel] (https://github.com/EixoX/jetfuel). There you'll find guides to implement usecases that can be called using amazon api gateway paired with lambda and exactaly zero servers to manage.


```java
We map /package/subpackage/ClassName to package.subpackage.ClassName and use reflection to instantiate and run it, as long as it extends UsecaseImplementation<TParams, TResult>.
```

```
Step 0: Make sure you already have another project using our **jetfuel usecase** programming paradigm. 
```

```
Step 1: Fork or clone this repo. 
```

Hint: Forking is easier to update from our mainstream and also easier to help us fix bugs in the unlikely event that one is found.

```
Step 2: Locate private static final String PACKAGE_ROOT on the only code file of this project and change it to the root name of your usecases. 
```

Hint: If your code is organized as ours: com.eixox.usecases.crm.person.LogIn and com.eixox.usecases.profiler.account.Detail. Set “com.eixox.usecases” as package root so you can call /crm/person/LogIn and /profiler/account/Detail on your API.

```
Step 3: Use pom.xml to add other projects as dependencies to your project. 
```

Or simply right click on the project name -> Maven -> Add Dependency and every dependency added that has a class that extends UsecaseImplementation<TParams, TResult> will be automagically accessible through the API.

```
Step 4: Deploy your function to AWS Lambda.
```

I use and recommend the aws eclipse extensions. So if your’re using eclipse try http://docs.aws.amazon.com/toolkit-for-eclipse/v1/user-guide/setup-install.html

```
Step 5: Configure API Gateway as a proxy to your lambda function
```

http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-set-up-simple-proxy.html