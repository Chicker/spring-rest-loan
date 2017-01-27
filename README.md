## How to use this project as template for a new project

Some steps:
* Clone template repository:

```
git clone https://Chicker@bitbucket.org/Chicker/spring-rest-hello.git <new-project-name> 
```

* Rename project in the `settings.gradle` file
* Delete the `.git` folder and then re-init new repo with `git init .` command 


## Development

### Running

Using gradle plugin `gretty` allows run our web application 
with jetty (or another servlet container) runner. Gradle task for this:
```
./gradlew appRun
```

