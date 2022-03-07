import { MessageResponse } from "./general.model";

export const REG_W_1001 = 'Email should not be empty.';
export const REG_W_1002 = 'Please enter a valid email id.';
export const REG_W_1003 = 'Password should not be empty.';
export const REG_W_1004 = 'Password length is less than the minimum length.';
export const REG_W_1005 = 'Password length is greater than the maximum length.';
export const REG_W_1006 = 'Password must have special characters.';
export const REG_W_1007 = 'First name should not be empty.';
export const REG_W_1008 = 'Last name should not be empty.';
export const REG_W_1009 = 'User name should not be empty.';
export const REG_W_1010 = 'Please enter a valid first name.';
export const REG_W_1011 = 'Please enter a valid last name.';
export const REG_W_1012 = 'Please enter a valid user name.';
export const REG_W_1013 = 'Please enter a valid phone number.';
export const REG_I_1014 = 'User registered successfully.';

export const LOG_W_1014 = 'Invalid credentials.';
export const LOG_W_1015 = 'User name does not exits.';
export const LOG_I_1017 = 'User authenticated successfully.';
export const LOG_I_1020 = "Password changed successfully.";
export const LOG_W_1021 = "Cannot change password of another user.";
export const LOG_E_1022 = "Error in changing the password.";

export const TWT_W_1016 = 'No tweet in topic for the event.';
export const TWT_I_1001 = 'Tweet posted successfully.';
export const TWT_W_1002 = 'Tweet posting failed.';
export const TWT_I_1003 = 'Tweets retrieved successfully.';
export const TWT_E_1004 = 'Error in retrieving tweets.';
export const TWT_I_1005 = 'Tweet updated successfully.';
export const TWT_E_1006 = 'Error in updating tweet.';
export const TWT_I_1007 = 'Tweet deleted successfully.';
export const TWT_E_1008 = 'Error in deleting tweet.';
export const TWT_W_1009 = 'Cannot insert or update tweet of another user.';

export const CMT_I_1001 = 'Comment posted successfully.';
export const CMT_W_1002 = 'Comment posting failed.';
export const CMT_I_1003 = 'Comments retrieved successfully.';
export const CMT_E_1004 = 'Error in retrieving comments.';
export const CMT_I_1005 = 'Comment updated successfully.';
export const CMT_E_1006 = 'Error in updating comment.';
export const CMT_I_1007 = 'Comment deleted successfully.';
export const CMT_E_1008 = 'Error in deleting comment.';
export const CMT_W_1009 = 'Cannot insert or update comment of another user.';

export const APP_E_SVC_UNAVAILABLE = 'Service Unavailable.';

export interface HttpErrors {
  messages: MessageResponse[];
}