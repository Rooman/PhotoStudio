console.log("Notification js");

function open(){
   console.log("Open socket");
}

function close(){
   console.log("Close socket");
}

function message(msgEvent){
 console.log(msgEvent);
 const notificationMessageDiv = document.getElementById("notification-message");
 notificationMessageDiv.innerHTML = createNotificationHtml(JSON.parse(msgEvent.data));

 const notificationDiv = document.getElementById("notification");
 notificationDiv.classList.remove("notification-form-message--hidden");
}

function createNotificationHtml(message) {
 const orderId=message.orderId;
 const messageText=message.message;

 const messageStatus = messageText.replace( orderId, ' <a href="order/' + orderId+ '">' + orderId + '</a> ');
 return messageStatus;
}

const notificationCloseDiv = document.getElementById("notification-close");

notificationCloseDiv.addEventListener('click', function(){
    const notificationDiv = document.getElementById("notification");
    notificationDiv.classList.add("notification-form-message--hidden");
});

const path = /*@{/notification}*/ "localhost:8080/dev/notification";
const ws = new WebSocket("ws://" + path);
//const ws = new WebSocket("ws://localhost:8080/dev/notification");
ws.onopen = open;
ws.onmessage=message;
ws.onclose = close;
