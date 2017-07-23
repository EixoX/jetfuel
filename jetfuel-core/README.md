# Welcome to eixox's Jetfuel
We've made this library to provide state of the art aspect driven utilities as well as a nice usecase programming paradigm.
It is and always will be zero dependencies. That is, we'll only put core code in here. Nothing will depend on anything else.
By using our library you have access to:
* Annotation based restrictions to improve validations.
* A persistence api with direct I/O to databases, files, no-sqls etc.
* An aspect driven api for reflection.
* A usecase programming paradigm that enables deploys on any servlet container or on amazon lambda.

**Step 0:** Fork or clone this repo. 

Hint: Forking is easier to update from our mainstream and also easier to help us fix bugs in the unlikely event that one is found.

# jetfuel-servlet

This project is a boilerplate to help you deploy your awesome API using any servlet container. Specially usefull when you're running your dev environment prior to publishing a serverless api on amazon or launching your own elastic environment.
This standard guide uses eclipse with WTP and tomcat 9.0 as a server.

We map /package/subpackage/ClassName to package.subpackage.ClassName and use reflection to instantiate and run it, as long as it extends UsecaseImplementation<TParams, TResult>.

**Step 1:** Make sure you already have another project using our **jetfuel usecase** programming paradigm. 

**Step 2:** Locate private static final String PACKAGE_ROOT on the only code file of this project and change it to the root name of your usecases. 

Hint: If your code is organized as ours: com.eixox.usecases.crm.person.LogIn and com.eixox.usecases.profiler.account.Detail. Set “com.eixox.usecases” as package root so you can call /crm/person/LogIn and /profiler/account/Detail on your API.

**Step 3:** Use pom.xml to add other projects as dependencies to your project. 

Or simply right click on the project name -> Maven -> Add Dependency and every dependency added that has a class that extends UsecaseImplementation<TParams, TResult> will be automagically accessible through the API.

**Step 4:** Deploy your project to the server. 

I use and recommend the WTP eclipse extensions. The server plugin detects changes in files and automatically reloads the application making this the ideal platform for developing the api.