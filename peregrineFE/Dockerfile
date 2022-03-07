FROM node:14.17.0
ENV NODE_ENV=/peregrineFE/node_modules

# Create a directory where our app will be placed
RUN mkdir -p /peregrineFE/

# Change directory so that our commands run inside this new directory
WORKDIR /peregrineFE/

# Get all the code needed to run the app
COPY . /peregrineFE/

# Expose the port the app runs in
EXPOSE 4200

# Serve the app
CMD [ "npm", "run", "start:docker:fe" ]
