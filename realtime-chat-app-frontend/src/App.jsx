import { useState } from "react";

import "./App.css";
import ChatPage from "./components/ChatPage";
import UsernamePage from "./components/UsernamePage";

function App() {
  const [username, setUsername] = useState(null);

  return (
    <div>
      {username ? (
        <ChatPage username={username} />
      ) : (
        <UsernamePage setUsername={setUsername} />
      )}
    </div>
  );
}

export default App;
