/**
 * 
 */

$(document).ready(function() { // This function is called 
	// initial call to long poll servlet
    jQuery.get("LongPollServlet", null, updateClientScreen);
});


function updateClientScreen(data)
	// update screen with information from data


	// call again
	jQuery.get("LongPollServlet", null, updateClientScreen); //Sends no data to server, calls updateClientScreen whenever get response comes back
}






