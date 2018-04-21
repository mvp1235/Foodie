'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendLikeNotifications = functions.database.ref('/Notifications/{user_id}/{notification_id}')
	.onWrite((change, context) => {
      const user_id = context.params.user_id;
      const notification_id = context.params.notification_id;
      
      if (!change.after.val()) {
    	  return console.log('A notification has been deleted from the database: ', notification_id);
      }
      
      const toUser = admin.database().ref(`/Users/${user_id}`).once('value');
      const deviceTokens = admin.database().ref(`/Users/${user_id}/tokenIDs`).once('value');
      
      return Promise.all([toUser, deviceTokens]).then(result => {
    	  const from_user = result[0].val();
    	  const token_ids = result[1].val();
    	  
    	  console.log('New like notification to user: ', from_user.uID);
    	  
    	  if(!result[1].hasChildren()) {
    		  return console.log('There are no notification tokens to send to.');
    	  }
    	  
    	  console.log('There are ', result[1].numChildren(), ' tokens to send notifications to.');
    	  
    	  const payload = {
			  notification: {
				  title: "Like Notification",
				  body: `${from_user.fullName} liked your post.`,
				  icon: "default"
			  }
	      };
    	  
    	  return admin.messaging().sendToDevice(token_ids, payload).then(response => {
	    	  return console.log("Like Notifications sent.");
	      });
      });
      
	});
