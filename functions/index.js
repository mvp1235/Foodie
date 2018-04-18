'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

/**
 * Triggers when a user gets a new follower and sends a notification.
 *
 * Followers add a flag to `/followers/{followedUid}/{followerUid}`.
 * Users save their device notification tokens to `/users/{followedUid}/notificationTokens/{notificationToken}`.
 */
exports.sendLikeNotifications = functions.database.ref(/Notifications/{user_id}/{notification_id}')
    .onWrite((change, context) => {
    	const user_id = event.params.user_id;
        const notification_id = event.params.notification_id;
        
        if(!change.after.val()) {
            return console.log('Notification ', notification_id, ' for User ', user_id, ' is removed.');
        }
        
        console.log('The UserID is: a', user_id);
        
        //Get the list of device notification tokens
        const getDeviceTokensPromise = admin.database()
            .ref(`/Users/${user_id}/tokenIDs`).once('value');
        
        //Get the user profile of the notification
        const getUserProfilePromise = admin.database()
            .ref(`/Users/${user_id}`).once('value');
        
        // The snapshot to the user's tokens
        let tokensSnapshot;
        
        //The array containing all the user's tokens
        let tokens;

      return Promise.all([getDeviceTokensPromise, getUserProfilePromise]).then(results => {
        tokensSnapshot = results[0];
        const follower = results[1];

        // Check if there are any device tokens.
        if (!tokensSnapshot.hasChildren()) {
          return console.log('There are no notification tokens to send to.');
        }
        console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
        console.log('Fetched user profile', user);

      //Notification details
        const payload = {
            notification: {
                title: 'Like Notification',
                body: `${user.fullName} liked your post.`,
                icon: user.profileURL
                
            }
        };

        // Listing all tokens as an array.
        tokens = Object.keys(tokensSnapshot.val());
        // Send notifications to all tokens.
        return admin.messaging().sendToDevice(tokens, payload);
      }).then((response) => {
        // For each message check if there was an error.
        const tokensToRemove = [];
        response.results.forEach((result, index) => {
          const error = result.error;
          if (error) {
            console.error('Failure sending notification to', tokens[index], error);
            // Cleanup the tokens who are not registered anymore.
            if (error.code === 'messaging/invalid-registration-token' ||
                error.code === 'messaging/registration-token-not-registered') {
              tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
            }
          }
        });
        return Promise.all(tokensToRemove);
      });
    });