FROM node:9

ARG UID
RUN adduser graphiql --uid $UID --disabled-password --gecos ""

ENV APP /usr/src/app
RUN mkdir $APP
WORKDIR $APP

COPY package.json yarn.lock $APP/
RUN yarn

COPY . $APP/

RUN yarn build

RUN yarn global add serve
CMD ["serve","-s", "build"]