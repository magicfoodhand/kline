# KLine

## What is it?

Code Generated GraphQL Kotlin Server with Apollo React Front End

### Easy Development
Start developing with ```./kline.sh start```; this brings up a database (postgres), 
starts the React dev server (includes live updates), 
starts a Kotlin Server (after running Database Migrations), all behind an nginx proxy.

### Backend
- Kotlin all the way
- Wonderful [kotlin-graphql](https://github.com/ExpediaDotCom/graphql-kotlin) for code first generation of GraphQL schemas, built on [graphql-java](https://github.com/graphql-java/graphql-java)
- Great Http Server [Javalin](https://javalin.io/)
- Fantastic Connection Pooling [Hikari CP](https://github.com/brettwooldridge/HikariCP).
- Amazing ORM [Exposed](https://github.com/JetBrains/Exposed)
- All the Metrics [metrics-core](https://metrics.dropwizard.io/3.1.0/manual/core/) and [metrics-healthchecks](https://metrics.dropwizard.io/3.1.0/manual/healthchecks/)
- Lightning Fast Serialization [Jackson](https://github.com/FasterXML/jackson)
- Latest testing with [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)

### Frontend
- React all the way, courtesy of [create-react-app](https://facebook.github.io/create-react-app/)
- Better GraphQL [Apollo Client](https://www.apollographql.com/docs/react/)
- Beautiful Toast Notifications [Toastify](https://github.com/fkhadra/react-toastify)
- Don't mess up Dates [date-fns](https://github.com/date-fns/date-fns)

#### Honorable Mentions
- Exactly what I want Date Picker [react-datepicker](https://reactdatepicker.com/)
- Anything Dates in One [BigCalender](https://github.com/intljusticemission/react-big-calendar)
- Use Kotlin for Everything [create-kotlin-react-app](https://github.com/JetBrains/create-react-kotlin-app)

### Docker and Docker Compose
Stop worrying about cross platform dependencies with Docker, easily run it with Docker Compose.

#### Docker
- Build once, run everywhere

#### Docker Compose
- Run everything on one machine
    - Use [Docker Swarm](https://docs.docker.com/engine/swarm/) to run on multiple machines

#### NGINX
- Creates a proxy so that it seems like backend and frontend are running on the same server
- ```nginx.conf```

### Easy Deployment
No matter how you want to run your app it should be easy. This project includes Docker Images to simplify deployment of
server and frontend, as well as build scripts to run without Docker. 

### Backend
The Docker Image is ready to run, just needs a DATABASE_URL set as an environment variable. 
Running without Docker is easy too, same rules apply.

#### [Heroku](https://www.heroku.com/)
- Create new project: ```heroku create [<name>]```
- Push only the server: ```git subtree push --prefix api heroku master```

### Frontend
The Docker Image from Dockerfile.live is the live/production version, just needs a GRAPHQL_URL. 
Running without Docker is easy too.

#### [Netlify](https://www.netlify.com/)
- Create New Project (connect to Github)
- Build Command: ```cd web && npm build``` 
- Publish Directory: ```web/build```

#### [Heroku](https://www.heroku.com/)
- Create new project: ```heroku create [<name>]```
- Push only the frontend: ```git subtree push --prefix web heroku master```

## What isn't it?

### Authenticated
It's the wild west out here folks

### Mobile or Desktop App
While it wouldn't be too hard to add a base Mobile App ([React Native](https://facebook.github.io/react-native/)) or Desktop App ([Tornado FX](https://github.com/edvin/tornadofx), [Proton Native](https://proton-native.js.org/#/)), 
not every website needs those. This project is primarily focused on web apps with a database.

## Why KLine?
This project is inspired by [Rails](https://rubyonrails.org/), [K Line](https://en.wikipedia.org/wiki/K_Line) is actually a Japanese shipping company. That's the goal of this project, ship it!
