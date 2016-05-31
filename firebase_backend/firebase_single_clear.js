var Firebase = require("firebase");

var root = new Firebase("https://team6coupletones.firebaseio.com");

root.on("value", function(users) {

    users.forEach(function(user) {
        //Iterating through all users
        var history = user.child("history");
        var userKey = user.key();
        console.log("Deleting " + userKey + " history");
        history.ref().remove();
    });

});