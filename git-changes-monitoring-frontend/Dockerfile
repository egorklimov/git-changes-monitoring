FROM node:13-alpine
WORKDIR /app

# install dependencies
COPY package*.json ./

# change proxy to docker network
RUN sed -i "s|http://localhost:8080/api|http://docker-host:8080/api|g" package.json

RUN npm install --silent

# copy source code
COPY src/ ./src/
COPY public/ ./public/

CMD ["npm", "start"]
