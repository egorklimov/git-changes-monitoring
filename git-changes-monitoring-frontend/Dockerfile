FROM node:13-alpine
WORKDIR /app

# install dependencies
COPY package*.json ./
RUN npm install --silent

# copy source code
COPY src/ ./src/
COPY public/ ./public/

RUN npm config set proxy http://docker-host:8080/api  # set manully
CMD ["npm", "start"]
