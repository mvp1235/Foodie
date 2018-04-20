'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendLikeNotifications = functions.database.ref('/Notifications/{user_id}/{notification_id}')
	.onWrite((change, context) => {
      const user_id = context.params.user_id;
      const notification_id = context.params.notification_id;
      
      console.log('We have a notification to send to: ', user_id);
      
      if (!change.after.val()) {
    	  return console.log('A notification has been deleted from the database: ', notification_id);
      }
      
      const fromUser = admin.database().ref(`/Users/${user_id}`).once('value');
      const deviceToken = admin.database().ref(`/Users/${user_id}/tokenID`).once('value');
      
      return Promise.all([fromUser, deviceToken]).then(result => {
    	  const from_user = result[0].val();
    	  const token_id = result[0].val();
    	  
    	  console.log('New like notification from user: ', from_user.uID);
    	  
    	  const payload = {
			  notification: {
				  title: "Like Notification",
				  body: `${from_user.fullName} liked your post.`,
				  icon: "default"
			  }
	      };
    	  
    	  return admin.messaging().sendToDevice(token_id, payload).then(response => {
	    	  return console.log("Like Notification sent.");
	      });
      });
      
	});
