/* Hello, World! program in node.js */
console.log("Hello, World!");
var http = require("http");
var fs = require("fs");
var path = require('path');
var express = require('express');
var url = require('url');
var Dropbox = require('dropbox');
var app = express();
var router = express.Router();
var util = require('util');
var formidable = require('formidable');
var dropboxRootFolder = "/My Photos";
var localDir = "uploads/";
var events = require('events');
// Create an eventEmitter object
var eventEmitter = new events.EventEmitter();
//var multiparty = require('multiparty');
//var formidable = require('formidable');
var DROPBOX_APP_KEY    = "ayv9ikjskd4ugh0";
var DROPBOX_APP_SECRET = "888i1qsmd21ahw8";
console.log("After importing http : ");
/*app.get('/', function (req, res) {
   res.send('Hello World Here : ');
})*/

var bodyParser = require('body-parser');

// configure app to use bodyParser()
// this will let us get the data from a POST
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var onRequest = function (req, res) {
	console.log("inside onRequest() : ");
}

// middleware to use for all requests
router.use(function(req, res, next) {
    // do logging
    console.log('Something is happening.');
    next(); // make sure we go to the next routes and don't stop here
});


app.get('/', function (req, res) {
	console.log("inside when user is on base Url : ");
    res.send("Hello.............");
});

//Route for upload
router.route('/file_upload')

    // create a bear (accessed at POST http://localhost:8080/api/bears)
    .post(function(req, res) {
        var responseData = {
        	status : 0
        }
        //console.log(req.body);
        var form = new formidable.IncomingForm();
        form.maxFieldsSize = 20 * 1024 * 1024;
        //var form = new multiparty.Form();

      	// form.parse analyzes the incoming stream data, picking apart the different fields and files for you.

      	form.parse(req, function(err, fields, files) {
        	if (err) {

          		// Check for and handle any errors here.
          		console.log("Error inside parse () : ");
          		console.error(err.message);
          		res.json(responseData);
          		return;
        	}
	        console.log("inside parse : ");
	        console.log("Name : "+fields.name);
	        var fileName  = fields.name.toString();
	        var base64data = fields.image_base64;
	        var access_key = fields.access_key;
	        var _id = fields.id;
	        var dbx = new Dropbox({ accessToken: access_key });
	        var imagedata = ''
	        var originaldata = new Buffer(base64data, 'base64');

			//console.log(originaldata);
			console.log("ID : "+_id)
			console.log("isExist : "+fs.existsSync(localDir+fileName));

			dbx.filesAlphaGetMetadata({path: dropboxRootFolder+'/'+fileName})
		    .then(function(response) {
		      /*displayFiles(response.entries);*/
		    	console.log("Inside response for fetching data : ");
		      	//eventEmitter.emit("checkFileResponseHandler", res, {status : 1, name : fileName, id : _id, data : originaldata}, dbx);
		      	if (fs.existsSync(localDir+fileName)) {
					responseData["status"] = 1;
			        responseData["id"] = _id;
			        res.json(responseData);
				} else {
					fs.writeFile(localDir+fileName, originaldata,  'base64', function(err){
			            if (err){
			            	console.log("Error while writing : ");
			            	responseData["status"] = 0;
			            	res.json(responseData);
			            	throw err
			            }
			            responseData["status"] = 1;
				        responseData["id"] = _id;
				        res.json(responseData);
			        });
				}
		    })
		    .catch(function(error) {
		    	console.log("inside error in filesAlphaGetMetadata: ");
		    	//console.log(error);
		    	/*eventEmitter.emit("checkFileResponseHandler", res, {status : 0, name : fileName, id : _id, data : originaldata}, dbx);*/
		    	fs.writeFile(localDir+fileName, originaldata,  'base64', function(err){
		            if (err){
		            	console.log("Error while writing : ");
		            	responseData["status"] = 0;
		            	res.json(responseData);
		            	throw err
		            }
		            console.log('File saved.');
		            fs.readFile(localDir+fileName,function(error, data){
		            	if (error) {
		            		console.log("Error in reading file : ");
		            		res.json(responseData);
		            		return;
		            	}
		            	console.log("Reading of file is done : ");
		            	dbx.filesUpload({ path: dropboxRootFolder+'/'+fileName, contents: data })
					      .then(function (response) {
					        console.log("Inside response of dropbox upload");
					        responseData["status"] = 1;
					        responseData["id"] = _id;
					        res.json(responseData);
					      })
					      .catch(function (err) {
					        console.log(err);
					        responseData["status"] = 0;
					        res.json(responseData);
					    });
		            })
		        })
		    });
  		});

  		//form.onPart(part);
  		//var checkFileResponseHandler = function ()

  		// Create an event handler as follows
		eventEmitter.on("checkFileResponseHandler", function (res, responseData, dbx) {
			console.log("inside checkFileResponseHandler : "+responseData.status);
			var fileName = responseData.name;
			if (responseData.status == 1) {
				console.log("inside if when status = 1 : "+responseData.name);
				eventEmitter.emit("checkLocalFileExistence", res, responseData.name, responseData.id, responseData.data)
			} else {
				console.log("inside else when status = 0 : "+responseData.name);
				fs.writeFile(localDir+fileName, responseData.data,  'base64', function(err){
		            if (err){
		            	console.log("Error while writing : ");
		            	responseData["status"] = 0;
		            	res.json(responseData);
		            	throw err
		            }
		            console.log('File saved.');
		            fs.readFile(localDir+fileName,function(error, data){
		            	if (error) {
		            		console.log("Error in reading file : ");
		            		res.json(responseData);
		            		return;
		            	}
		            	console.log("Reading of file is done : ");
		            	dbx.filesUpload({ path: dropboxRootFolder+'/'+fileName, contents: data })
					      .then(function (response) {
					        console.log("Inside response of dropbox upload");
					        responseData["status"] = 1;
					        responseData["id"] = responseData.id;
					        res.json(responseData);
					      })
					      .catch(function (err) {
					        console.log(err);
					        responseData["status"] = 1;
					        responseData["id"] = responseData.id;
					        res.json(responseData);
					    });
		            })
		        })
			}

		})

		eventEmitter.on("checkLocalFileExistence", function(res, fileName, fileId, originaldata) {
			console.log("inside checkLocalFileExistence() : "+fileName+" Id : "+fileId)
			console.log("isExist : "+fs.existsSync(localDir+fileName));
			if (fs.existsSync(localDir+fileName)) {
				responseData["status"] = 1;
		        responseData["id"] = fileId;
		        res.json(responseData);
			} else {
				fs.writeFile(localDir+fileName, originaldata,  'base64', function(err){
		            if (err){
		            	console.log("Error while writing : ");
		            	responseData["status"] = 0;
		            	res.json(responseData);
		            	throw err
		            }
		            responseData["status"] = 1;
			        responseData["id"] = _id;
			        res.json(responseData);
		        });
			}

		})

    });

app.use('/', router);

/*var image = fs.readFile('tagpeople.png', function(err, data) {
	console.log(data);
    fs.writeFile('outputImage.png', data, 'binary', function (err) {
        if (err) {
            console.log("There was an error writing the image")
        }

        else {
            console.log("There file was written")
        }
    });
});*/

var server = app.listen(8081,function () {
	console.log("inside function while listen : ");
})
// Console will print the message
console.log('Server running at http://127.0.0.1:8081/');
