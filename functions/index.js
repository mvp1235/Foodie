'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendLikeNotifications = functions.database.ref('/Notifications/{user_id}/{notification_id}')
	.onCreate((snapshot, context) => {
      const to_user_id = context.params.user_id;
      const from_user_id = snapshot.val().fromUserID;
      const notification_id = context.params.notification_id;
      
//      if (!change.after.val()) {
//    	  return console.log('A notification has been deleted from the database: ', notification_id);
//      }
      
      const toUser = admin.database().ref(`/Users/${to_user_id}`).once('value');
      const deviceTokens = admin.database().ref(`/Users/${to_user_id}/tokenIDs`).once('value');
      const fromUser = admin.database().ref(`/Users/${from_user_id}`).once('value');
      
      return Promise.all([toUser, fromUser, deviceTokens]).then(result => {
    	  const to_user = result[0].val();
    	  const from_user = result[1].val();
    	  const token_ids = result[2].val();
    	  
    	  console.log('New like notification to user: ', to_user.uID);
    	  
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
