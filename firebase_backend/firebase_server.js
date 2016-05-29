var Firebase = require("firebase");
var cronJob = require('cron').CronJob;

var root = new Firebase("https://team6coupletones.firebaseio.com");

var job = new cronJob({
  cronTime: '00 00 03 * * *', // run every day at 3AM
  onTick: function() {

    root.on("value", function(users) {

        users.forEach(function(user) {
            //Iterating through all users
            var history = user.child("history");
            var userKey = user.key();
            console.log("Deleting " + userKey + " history");
            history.ref().remove();
        });

        console.log("all done!");
    });

    
  },
  start: false,
  timeZone: "America/Los_Angeles"
});

job.start();