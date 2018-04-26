'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendLikeNotifications = functions.database.ref('/Notifications/{user_id}/{notification_id}')
	.onCreate((snapshot, context) => {
      const to_user_id = context.params.user_id;
      const notification = snapshot.val();
      const notification_id = context.params.notification_id;
      const from_user_id = notification.fromUserID;
      
//      if (!snapshot.after.val()) {
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
    	  
    	  if(!result[2].hasChildren()) {
    		  return console.log('There are no notification tokens to send to.');
    	  }
    	  
    	  console.log('There are ', result[2].numChildren(), ' tokens to send notifications to.');
    	  
    	  console.log('Notification: ', notification.type);
    	  if (notification.type === 'like') {
	    	  const payload = {
				  notification: {
					  title: "Foodie",
					  body: `${from_user.fullName} ${notification.content}`,
					  icon: `${from_user.profileURL}`,
					  click_action: "com.example.mvp.foodie.POST_NOTIFICATION_TARGET"
				  },
				  data: {
					  post_id: `${notification.postID}`
				  }
		      };
	    	  
	    	  return admin.messaging().sendToDevice(token_ids, payload).then(response => {
		    	  return console.log("Like Notifications sent.");
		      });
	      } else if (notification.type === 'comment') {
	    	  const payload = {
				  notification: {
					  title: "Foodie",
					  body: `${from_user.fullName} ${notification.content}`,
					  icon: `${from_user.profileURL}`,
					  click_action: "com.example.mvp.foodie.POST_NOTIFICATION_TARGET"
				  },
				  data: {
					  post_id: `${notification.postID}`
				  }
		      };
	    	  
	    	  return admin.messaging().sendToDevice(token_ids, payload).then(response => {
		    	  return console.log("Comment Notifications sent.");
		      });
	      } else {
	    	  return console.log("Not valid notification")
	      }
      });
      
	});
