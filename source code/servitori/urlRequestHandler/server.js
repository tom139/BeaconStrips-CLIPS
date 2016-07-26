'use strict';

var express = require('express');
var bodyParser = require('body-parser');
var app = express();

const AppInfo = require('./AppInfoHandler.js');
const Beacons = require('./BeaconRequestHandler.js');
const Login   = require('./LoginHandler.js');
const Logout  = require('./Logout.js');
const PathsResults = require('./PathsResults.js');
const Buildings  = require('./Buildings.js');
const Registration = require('./RegistrationHandler.js');

// indica di fare il parse del body di tutte le richieste
// in entrata come JSON object
app.use(bodyParser.json());

const prepareAndExecute = function(handlerType, req, res) {
   var handler = new handlerType;
   handler.request = req;
   handler.response = res;
   handler.execute();
}

app.get('/appinfo', function(req, res) {
   console.log('handle appinfo');
   prepareAndExecute(AppInfo, req, res);
});

app.get('/beacons', function(req, res) {
   console.log('handle beacons');
   var handler = new Beacons;
   handler.request  = req;
   handler.response = res;
   handler.execute();
});

app.post('/login', function(req, res) {
   console.log('handle login');
   var handler = new Login;
   handler.request = req;
   handler.response = res;
   handler.execute();
});

app.get('/logout', function(req, res) {
   console.log('handle logout');
   prepareAndExecute(Logout, req, res);
});

app.get('/pathsresults', function(req, res) {
   console.log('handle pathsresults');
   prepareAndExecute(PathsResults, req, res);
});

app.post('/buildings', function(req, res) {
   console.log('handle buildings');
   prepareAndExecute(Buildings, req, res);
});

app.post('/newuser', function(req, res) {
   console.log('handle new user');
   prepareAndExecute(Registration, req, res);
});

app.listen(1234);

console.log('listening on port 1234');
