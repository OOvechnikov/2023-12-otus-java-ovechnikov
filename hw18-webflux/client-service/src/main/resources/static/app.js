let stompClient = null;

const AGGREGATE_ROOM = '1408';

const chatLineElementId = "chatLine";
const roomIdElementId = "roomId";
const messageElementId = "message";


const setConnected = (connected, roomId) => {
    const connectBtn = document.getElementById("connect");
    const connect1408Btn = document.getElementById("connect-1408");
    const disconnectBtn = document.getElementById("disconnect");

    connectBtn.disabled = connected && (roomId !== AGGREGATE_ROOM);
    connect1408Btn.disabled= connected && (roomId === AGGREGATE_ROOM);

    disconnectBtn.disabled = !connected;
    const chatLine = document.getElementById(chatLineElementId);
    chatLine.hidden = !connected;
}

const connect_1408 = () => {
    document.getElementById(roomIdElementId).value = AGGREGATE_ROOM;
    connect();
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        const userName = frame.headers["user-name"];
        const roomId = document.getElementById(roomIdElementId).value;
        setConnected(true, roomId);
        console.log(`Connected to roomId: ${roomId} frame:${frame}`);
        const topicName = `/topic/response.${roomId}`;
        const topicNameUser = `/user/${userName}${topicName}`;
        stompClient.subscribe(topicName, (message) => showMessage(JSON.parse(message.body).messageStr));
        stompClient.subscribe(topicNameUser, (message) => showMessage(JSON.parse(message.body).messageStr));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendMsg = () => {
    const roomId = document.getElementById(roomIdElementId).value;
    const message = document.getElementById(messageElementId).value;
    if (roomId.trim() === AGGREGATE_ROOM){
        alert('В комнате #' + AGGREGATE_ROOM + ' нельзя отправлять сообщения.')
        return true;
    }
    stompClient.send(`/app/message.${roomId}`, {}, JSON.stringify({'messageStr': message}))
}

const showMessage = (message) => {
    const chatLine = document.getElementById(chatLineElementId);
    let newRow = chatLine.insertRow(-1);
    let newCell = newRow.insertCell(0);
    let newText = document.createTextNode(message);
    newCell.appendChild(newText);
}
