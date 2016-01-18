// Static variables
var DEBUG_MODE = true;
var DEBUG_PROMPT = "[DEBUG] ";

// Logger
function logger(message) {
    if (DEBUG_MODE) {
        console.log(DEBUG_PROMPT + message);
    }
}